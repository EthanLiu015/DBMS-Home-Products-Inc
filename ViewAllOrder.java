import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;


public class ViewAllOrder {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private Order order;
    private JPanel contentPanel;

    public ViewAllOrder(MainApplication mainApp, Order order) {
        this.mainApp = mainApp;
        this.order = order;
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

        // Add order details panel
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        contentPanel = createOrderDetailsPanel();
        contentContainer.add(contentPanel, gbc);

        mainPanel.add(contentContainer, BorderLayout.CENTER);
    }




    private JLayeredPane createTitlePanel() {
        String title = "Viewing Order " + order.getOrderID();
        String description = "Use this form to view all information about OrderID " + order.getOrderID();
        return UIFactory.createTitlePanel(mainApp, mainPanel, title, description, "OrderPresentation");
    }

    private JPanel createOrderDetailsPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(245, 222, 179));
        
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(new Color(245, 222, 179));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = -1; // Start before the first section

        // Add sections in the original order
        UIFactory.addViewSection(detailsPanel, "Order Information", createOrderInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Customer Information", createContactInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Payment Information", createPaymentInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Product Information", createProductInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Shipping Information", createShippingInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Financial Summary", createFinancialSummaryPanel(), gbc);
        
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(detailsPanel);
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        return containerPanel;
    }



private JPanel createOrderInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Order ID: ", String.valueOf(order.getOrderID())));
        panel.add(UIFactory.createInfoField("Order Date: ", String.valueOf(order.getOrderDate())));
        panel.add(UIFactory.createInfoField("Status: ", order.getStatus()));
        
        return panel;
    }

private JPanel createContactInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Customer ID: ", String.valueOf(order.getCustomerID())));
        panel.add(UIFactory.createInfoField("Customer Name: ", order.getCustomerFirstName() + " " + order.getCustomerLastName()));
        
        return panel;
    }

private JPanel createPaymentInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Get unique payments using a Set to track payment IDs
        List<Payment> payments = order.getPayments();
        Map<Integer, Payment> uniquePayments = new LinkedHashMap<>();
        
        for (Payment payment : payments) {
            uniquePayments.putIfAbsent(payment.getPaymentID(), payment);
        }
        
        if (uniquePayments.isEmpty()) {
            panel.add(UIFactory.createInfoField("Payments: ", "No payments recorded for this order"));
            return panel;
        }
        
        double totalPaid = 0.0;
        int count = 1;
        
        for (Payment payment : uniquePayments.values()) {
            totalPaid += payment.getAmount();
            
            // Add separator between payments
            if (count > 1) {
                panel.add(Box.createVerticalStrut(20));
                panel.add(UIFactory.createInfoField("Payment " + count, "-------------------"));
            } else {
                panel.add(UIFactory.createInfoField("Payment 1", "-------------------"));
            }
            
            // Add payment details
            panel.add(UIFactory.createInfoField("Payment ID: ", String.valueOf(payment.getPaymentID())));
            panel.add(UIFactory.createInfoField("Payment Date: ", String.valueOf(payment.getPaymentDate())));
            panel.add(UIFactory.createInfoField("Card Holder: ", payment.getCardHolder()));
            panel.add(UIFactory.createInfoField("Payment Method: ", payment.getMethod()));
            panel.add(UIFactory.createInfoField("Amount: ", String.format("$%.2f", payment.getAmount())));
            panel.add(UIFactory.createInfoField("Credit Card: ", payment.isCreditCard() ? "Yes" : "No"));
            
            count++;
        }
        
        // Add total amount paid
        panel.add(Box.createVerticalStrut(20));
        panel.add(UIFactory.createInfoField("Total Amount Paid: ", String.format("$%.2f", totalPaid)));
        
        return panel;
    }




private JPanel createProductInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        List<Order.OrderItem> items = order.getOrderItems();
        if (items.isEmpty()) {
            panel.add(UIFactory.createInfoField("Products: ", "No products in this order"));
            return panel;
        }
        
        for (int i = 0; i < items.size(); i++) {
            Order.OrderItem item = items.get(i);
            
            // Add separator between products
            if (i > 0) {
                panel.add(Box.createVerticalStrut(20));
                panel.add(UIFactory.createInfoField("Product " + (i + 1), "-------------------"));
            } else {
                panel.add(UIFactory.createInfoField("Product 1", "-------------------"));
            }
            
            // Add product details
            panel.add(UIFactory.createInfoField("Product ID: ", item.getProductID()));
            panel.add(UIFactory.createInfoField("Description: ", item.getProductDescription()));
            panel.add(UIFactory.createInfoField("Quantity: ", String.valueOf(item.getQuantityOrdered())));
            if (item.getQuantityOrdered() > 0) {
                panel.add(UIFactory.createInfoField("Price per Unit: ", String.format("$%.2f", item.getQuotedPrice() / item.getQuantityOrdered())));
            } else {
                panel.add(UIFactory.createInfoField("Price per Unit: ", "$0.00"));
            }
            panel.add(UIFactory.createInfoField("Subtotal: ", String.format("$%.2f", item.getQuotedPrice())));
        }
        
        return panel;
    }


private JPanel createShippingInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Shipping Date: ", String.valueOf(order.getShippingDate())));
        panel.add(UIFactory.createInfoField("Shipping Method: ", order.getShippingMethod()));
        
        return panel;
    }

private JPanel createFinancialSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        double subtotal = order.calculateSubtotal();
        double tax = order.calculateTax();
        double total = order.calculateTotal();
        double discount = OrderService.calculateDiscount(order);
        double regularTotal = subtotal + discount;
        double discountPercentage = (regularTotal > 0) ? (discount / regularTotal) * 100 : 0;
        
        panel.add(UIFactory.createInfoField("Unit Price Total: ", String.format("$%.2f", regularTotal)));
        panel.add(UIFactory.createInfoField("Discount Savings: ", String.format("$%.2f (%.0f%%)", discount, discountPercentage)));
        panel.add(UIFactory.createInfoField("Subtotal (Quoted Price): ", String.format("$%.2f", subtotal)));
        panel.add(UIFactory.createInfoField("Sales Tax Rate: ", String.format("%.0f%%", order.getSalesTax() * 100)));
        panel.add(UIFactory.createInfoField("Tax Amount: ", String.format("$%.2f", tax)));
        panel.add(UIFactory.createInfoField("Final Total: ", String.format("$%.2f", total)));
        
        return panel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
