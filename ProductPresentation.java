import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ProductPresentation {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private List<Product> products;
    private JTable productTable;

    public ProductPresentation(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.products = ProductService.getAllProducts();
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

        JPanel productPanel = createProductPanel();
        contentPanel.add(productPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private JLayeredPane createTitlePanel() {
        return UIFactory.createTitlePanel(mainApp, mainPanel, "Products", 
            "Use this page to view, edit, or create products.", "MainMenu");
    }

    private JTable createProductTable() {
        String[] columnNames = {"Product ID", "Description", "Price", "Units", "Class", "Warehouse", "Actions"};
        Object[][] data = new Object[products.size()][7];
        
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            data[i][0] = product.getProductID();
            data[i][1] = product.getProductDescription();
            data[i][2] = String.format("$%.2f", product.getUnitPrice());
            data[i][3] = product.getUnitsonHand();
            data[i][4] = product.getProductClass();
            data[i][5] = product.getWarehouse();
            data[i][6] = "Edit";
        }
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        JTable table = new JTable(model);
        UIFactory.styleTable(table);
        
        // Set custom column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        table.getColumn("Actions").setCellRenderer(new UIFactory.TableButtonRenderer("Edit"));
        table.getColumn("Actions").setCellEditor(new UIFactory.TableButtonEditor("Edit", 
            row -> mainApp.showEditProduct(products.get(row))));
        
        return table;
    }

    public void refreshProductTable() {
        this.products = ProductService.getAllProducts();
        
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);
        
        for (Product product : products) {
            model.addRow(new Object[]{
                product.getProductID(),
                product.getProductDescription(),
                String.format("$%.2f", product.getUnitPrice()),
                product.getUnitsonHand(),
                product.getProductClass(),
                product.getWarehouse(),
                "Edit"
            });
        }
    }
    private JPanel createProductPanel() {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(new Color(245, 222, 179));
        
        productTable = createProductTable();
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(productTable);
        
        productPanel.add(scrollPane, BorderLayout.CENTER);
        return productPanel;
    }

    // Update createActionPanel() method
    private JPanel createActionPanel() {
        return UIFactory.createPresentationActionPanel("Create New", () -> mainApp.showNewProduct());
    }
}
