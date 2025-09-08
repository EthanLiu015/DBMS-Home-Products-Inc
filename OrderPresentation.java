import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class OrderPresentation {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private List<Order> orders;
    private JTable orderTable;

    public OrderPresentation(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.orders = OrderService.getAllOrders();
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

        JPanel orderPanel = createOrderPanel();
        contentPanel.add(orderPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private JLayeredPane createTitlePanel() {
        return UIFactory.createTitlePanel(mainApp, mainPanel, "Orders", "Use this page to view or create orders.", "MainMenu");
    }
    
    private JPanel createActionPanel() {
        return UIFactory.createPresentationActionPanel("Create New", () -> mainApp.showNewOrder());
    }
    
    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBackground(new Color(245, 222, 179));
        orderTable = createOrderTable();
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(orderTable);
        
        orderPanel.add(scrollPane, BorderLayout.CENTER);
        return orderPanel;
    }

    private JTable createOrderTable() {
        String[] columnNames = {"OrderID", "CustomerID", "OrderDate", "ShippingDate", "Actions"};
        Object[][] data = new Object[orders.size()][5];
        
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getOrderID();
            data[i][1] = order.getCustomerID();
            data[i][2] = order.getOrderDate();
            data[i][3] = order.getShippingDate();
            data[i][4] = "View";
        }
        
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
        
        table.getColumn("Actions").setCellRenderer(new UIFactory.TableButtonRenderer("View"));
        table.getColumn("Actions").setCellEditor(new UIFactory.TableButtonEditor("View", 
            row -> mainApp.showOrderDetails(orders.get(row))));
        
        return table;
    }

    public void refreshTable() {
        // Get fresh data
        this.orders = OrderService.getAllOrders();
        
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        model.setRowCount(0); // Clear existing rows
        
        // Repopulate with new rows
        for (Order order : orders) {
            model.addRow(new Object[]{
                order.getOrderID(),
                order.getCustomerID(),
                order.getOrderDate(),
                order.getShippingDate(),
                "View"
            });
        }
    }
}