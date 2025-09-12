import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Represents the presentation screen for displaying a list of customers.
 * This class is responsible for creating the UI to show all customers in a table,
 * providing actions like creating a new customer, and navigating to view customer details.
 * @author Ethan Liu
 * @version 1.0.1
 */
public class CustomerPresentation {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private List<Customer> customers;
    private JTable customerTable;

    /**
     * Constructs the CustomerPresentation screen.
     * @param mainApp The main application instance, used for navigation.
     */
    public CustomerPresentation(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.customers = CustomerService.getAllCustomers();
        createAndShowGUI();
    }

    /**
     * Returns the main panel of this screen.
     * @return The main JPanel.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }


    /**
     * Initializes and lays out the graphical user interface for this screen.
     */
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

    /**
     * Creates the title panel for the screen using the UIFactory.
     * @return A JLayeredPane containing the title and description.
     */
    private JLayeredPane createTitlePanel() {
        return UIFactory.createTitlePanel(mainApp, mainPanel, "Customers", "Use this page to view, edit, or create customers.", "MainMenu");
    }

    /**
     * Creates the action panel containing the "Create New" button.
     * @return A JPanel with the action buttons.
     */
    private JPanel createActionPanel() {
        return UIFactory.createPresentationActionPanel("Create New", () -> mainApp.showNewCustomer());
    }

    /**
     * Creates the main content panel which holds the customer table.
     * @return A JPanel containing the scrollable customer table.
     */
    private JPanel createCustomerPanel() {
        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.setBackground(new Color(245, 222, 179));
        
        customerTable = createCustomerTable();
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(customerTable);
        
        customerPanel.add(scrollPane, BorderLayout.CENTER);
        return customerPanel;
    }

    /**
     * Creates and configures the JTable to display customer data.
     * This includes setting up the table model, columns, and action buttons.
     * @return A configured JTable instance.
     */
    private JTable createCustomerTable() {
        String[] columnNames = {"Customer ID", "Name", "Phone", "Email", "Actions"};
        Object[][] data = new Object[customers.size()][5];
        
        // Populate the data array from the list of customers
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            data[i][0] = customer.getCustomerID();
            data[i][1] = customer.getFirstName() + " " + customer.getLastName();
            data[i][2] = customer.getCustomerCellPhone();
            data[i][3] = customer.getCustomerEmail();
            data[i][4] = "View";
        }
        
        // Create a non-editable table model, except for the actions column
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only the "Actions" column is editable
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

        // Set a custom renderer and editor for the "Actions" column to display a button
        table.getColumn("Actions").setCellRenderer(new UIFactory.TableButtonRenderer("View"));
        table.getColumn("Actions").setCellEditor(new UIFactory.TableButtonEditor("View", 
            row -> mainApp.showCustomerDetails(customers.get(row))));
        return table;
    }

    /**
     * Refreshes the customer table with the latest data from the database.
     * This method clears the existing table data and repopulates it.
     */
    public void refreshCustomerTable() {
        // Get fresh data
        this.customers = CustomerService.getAllCustomers();
        
        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0); // Clear existing rows
        
        // Repopulate the table with the updated list of customers
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