import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.util.List;

public class ViewAllCustomer {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private Customer customer;
    private JPanel contentPanel;

    public ViewAllCustomer(MainApplication mainApp, Customer customer) {
        this.mainApp = mainApp;
        this.customer = customer;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 222, 179));

        // Add title panel at the top
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);

        // Create a panel for the content below the title
        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Add action panel
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        JPanel actionPanel = createActionPanel();
        contentContainer.add(actionPanel, gbc);

        // Add customer details panel
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        contentPanel = createCustomerDetailsPanel();
        contentContainer.add(contentPanel, gbc);

        mainPanel.add(contentContainer, BorderLayout.CENTER);
    }



    private JLayeredPane createTitlePanel() {
        String title = "Viewing " + customer.getFirstName() + " " + customer.getLastName();
        String description = "Use this form to view all information about " + customer.getFirstName() + " " + customer.getLastName();
        return UIFactory.createTitlePanel(mainApp, mainPanel, title, description, "CustomerPresentation");
    }


    private JPanel createActionPanel() {
        JPanel actionPanel = UIFactory.createActionPanel();
        
        JButton editButton = UIFactory.createStyledButton("Edit");
        JButton orderHistoryButton = UIFactory.createStyledButton("Order History");
        JButton paymentHistoryButton = UIFactory.createStyledButton("Payment History");
        
        editButton.addActionListener(e -> handleEdit());
        orderHistoryButton.addActionListener(e -> showOrderHistory());
        paymentHistoryButton.addActionListener(e -> showPaymentHistory());
        
        actionPanel.add(editButton);
        actionPanel.add(orderHistoryButton);
        actionPanel.add(paymentHistoryButton);

        return actionPanel;
    }

    private JPanel createCustomerDetailsPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(245, 222, 179));
        
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(new Color(245, 222, 179));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = -1; // Start before the first section

        // Add sections in the original order
        UIFactory.addViewSection(detailsPanel, "Personal Information", createPersonalInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Contact Information", createContactInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Business Information", createBusinessInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Additional Information", createAdditionalInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Financial Summary", createFinancialSummaryPanel(), gbc);
        
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(detailsPanel);
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });

        containerPanel.add(scrollPane, BorderLayout.CENTER);
        return containerPanel;
    }



private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Customer ID: ", String.valueOf(customer.getCustomerID())));
        panel.add(UIFactory.createInfoField("First Name: ", customer.getFirstName()));
        panel.add(UIFactory.createInfoField("Last Name: ", customer.getLastName()));
        panel.add(UIFactory.createInfoField("Title: ", customer.getTitle()));
        
        return panel;
    }

private JPanel createContactInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Street:", customer.getStreet()));
        panel.add(UIFactory.createInfoField("City:", customer.getCity()));
        panel.add(UIFactory.createInfoField("State:", customer.getState()));
        panel.add(UIFactory.createInfoField("Zip Code:", customer.getZipCode()));
        panel.add(UIFactory.createInfoField("Business Phone:", customer.getCustomerBusinessPhone()));
        panel.add(UIFactory.createInfoField("Cell Phone:", customer.getCustomerCellPhone()));
        panel.add(UIFactory.createInfoField("Email:", customer.getCustomerEmail()));
        
        return panel;
    }

private JPanel createBusinessInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Company:", customer.getCompany()));
        panel.add(UIFactory.createInfoField("Website:", customer.getWebsite()));
        panel.add(UIFactory.createInfoField("Sales Rep ID:", String.valueOf(customer.getRepID())));
        panel.add(UIFactory.createInfoField("Status:", customer.getStatus()));
        
        return panel;
    }

private JPanel createAdditionalInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Notes:", customer.getNotes()));
        
        return panel;
    }

private JPanel createFinancialSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        double creditLimit = customer.getCustomerCredit();
        double totalOrders = calculateTotalOrders();
        double remainingCredit = creditLimit - totalOrders;
        
        panel.add(UIFactory.createInfoField("Credit Limit:", String.format("$%.2f", creditLimit)));
        panel.add(UIFactory.createInfoField("Total Lifetime Orders:", String.format("$%.2f", totalOrders)));
        panel.add(UIFactory.createInfoField("Remaining Credit:", String.format("$%.2f", remainingCredit)));
        
        return panel;
    }

    private double calculateTotalOrders() {
        return CustomerService.calculateCustomerTotalOrders(customer.getCustomerID());
    }

    private void handleEdit() {
        mainApp.showEditCustomer(customer);
    }


    private void showOrderHistory() {
        List<Order> orderHistory = CustomerService.getCustomerOrderHistory(customer.getCustomerID());

        JDialog dialog = new JDialog();
        dialog.setTitle("Order History - " + customer.getFirstName() + " " + customer.getLastName());
        dialog.setModal(true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(mainPanel);

        String[] columnNames = {"Order ID", "Date", "Status", "Total Amount"};
        Object[][] data = new Object[orderHistory.size()][4];

        for (int i = 0; i < orderHistory.size(); i++) {
            Order order = orderHistory.get(i);
            data[i][0] = order.getOrderID();
            data[i][1] = order.getOrderDate();
            data[i][2] = order.getStatus();
            data[i][3] = String.format("$%.2f", CustomerService.calculateTotal(order.getOrderID()));
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane);

        dialog.setVisible(true);
    }

private void showPaymentHistory() {
        List<Payment> paymentHistory = CustomerService.getCustomerPaymentHistory(customer.getCustomerID());

        JDialog dialog = new JDialog();
        dialog.setTitle("Payment History - " + customer.getFirstName() + " " + customer.getLastName());
        dialog.setModal(true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(mainPanel);

        String[] columnNames = {"Payment ID", "Date", "Amount", "Method"};
        Object[][] data = new Object[paymentHistory.size()][4];

        for (int i = 0; i < paymentHistory.size(); i++) {
            Payment payment = paymentHistory.get(i);
            data[i][0] = payment.getPaymentID();
            data[i][1] = payment.getPaymentDate();
            data[i][2] = String.format("$%.2f", payment.getAmount());
            data[i][3] = payment.getMethod();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane);

        dialog.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}