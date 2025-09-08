import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class CustomerPresentation {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private List<Customer> customers;
    private JTable customerTable;

    public CustomerPresentation(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.customers = CustomerService.getAllCustomers();
        createAndShowGUI();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    private void createAndShowGUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 222, 179));

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);

        // Content panel to hold actions and table
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 222, 179));

        JPanel actionPanel = createActionPanel();
        contentPanel.add(actionPanel, BorderLayout.NORTH);

        JPanel customerPanel = createCustomerPanel();
        contentPanel.add(customerPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

        private JLayeredPane createTitlePanel() {
            return UIFactory.createTitlePanel(mainApp, mainPanel, "Customers", "Use this page to view, edit, or create customers.", "MainMenu");
        }
    
    private JPanel createActionPanel() {
        return UIFactory.createPresentationActionPanel("Create New", () -> mainApp.showNewCustomer());
    }

    
    private JPanel createCustomerPanel() {
        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.setBackground(new Color(245, 222, 179));
        
        customerTable = createCustomerTable();
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(customerTable);
        
        customerPanel.add(scrollPane, BorderLayout.CENTER);
        return customerPanel;
    }



    private JTable createCustomerTable() {
        String[] columnNames = {"Customer ID", "Name", "Phone", "Email", "Actions"};
        Object[][] data = new Object[customers.size()][5];
        
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            data[i][0] = customer.getCustomerID();
            data[i][1] = customer.getFirstName() + " " + customer.getLastName();
            data[i][2] = customer.getCustomerCellPhone();
            data[i][3] = customer.getCustomerEmail();
            data[i][4] = "View";
        }
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        
        JTable table = new JTable(model);
        UIFactory.styleTable(table);
        
        // Set custom column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(0).setMaxWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        table.getColumn("Actions").setCellRenderer(new UIFactory.TableButtonRenderer("View"));
        table.getColumn("Actions").setCellEditor(new UIFactory.TableButtonEditor("View", 
            row -> mainApp.showCustomerDetails(customers.get(row))));
        return table;
    }

    public void refreshCustomerTable() {
        // Get fresh data
        this.customers = CustomerService.getAllCustomers();
        
        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0); // Clear existing rows
        
        // Repopulate with new rows
        for (Customer customer : customers) {
            model.addRow(new Object[]{
                customer.getCustomerID(),
                customer.getFirstName() + " " + customer.getLastName(),
                customer.getCustomerCellPhone(),
                customer.getCustomerEmail(),
                "View"
            });
        }
    }
}