import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Calendar;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;




public class NewEditOrder {
    private MainApplication mainApp;
    private Order order;
    private JPanel mainPanel;
    
    // Essential Order Fields
    private JTextField customerID;
    private JSpinner orderDateSpinner;
    private JSpinner shippingDateSpinner;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> shippingMethodComboBox;
    private JTextField salesTax;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    
    // Constants
    private static final String[] SHIPPING_METHODS = {
        "UPS Second Day", "UPS Ground", "US Certified Mail", 
        "Federal Express", "US Mail Overnight", "US Standard Ground", 
        "US Second Day"
    };
    private static final String[] ORDER_STATUS = {"Paid", "Partial", "Unpaid"};

    public NewEditOrder(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.order = new Order();
        createAndShowGUI();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createAndShowGUI() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Create the form panel with specific size
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            public Dimension getPreferredSize() {
                // Set preferred width to match Customer form
                return new Dimension(800, super.getPreferredSize().height);
            }
        };
        formPanel.setBackground(new Color(245, 222, 179));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Add sections to the form panel
        UIFactory.addSectionHeader(formPanel, "Customer Information", gbc);
        addCustomerSectionFields(formPanel, gbc);

        UIFactory.addSectionHeader(formPanel, "Order Details", gbc);
        addOrderDetailsSectionFields(formPanel, gbc);

        UIFactory.addSectionHeader(formPanel, "Products", gbc);
        JPanel productPanel = createProductSectionPanel();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(productPanel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        
        // Center the form panel
        JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centeringPanel.setBackground(new Color(245, 222, 179));
        centeringPanel.add(formPanel);
        // Create scroll pane using the factory
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(centeringPanel);
        // Add components to main panel
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(UIFactory.createSaveCancelButtonPanel(this::handleSaveButton, () -> mainApp.showScreen("OrderPresentation")), BorderLayout.SOUTH);
    }
    private void addCustomerSectionFields(JPanel panel, GridBagConstraints gbc) {
        customerID = UIFactory.createNumericTextField(100);
        UIFactory.addFormField(panel, "Customer ID:", customerID, gbc);
    }

    private JPanel createProductSectionPanel() {
        JPanel productPanel = new JPanel(new BorderLayout(10, 10));
        productPanel.setBackground(new Color(245, 222, 179));
        
        // Product selection panel
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBackground(new Color(245, 222, 179));
        GridBagConstraints selGbc = new GridBagConstraints();
        selGbc.insets = new Insets(5, 5, 5, 5);
        selGbc.anchor = GridBagConstraints.WEST;
        
        JComboBox<String> productComboBox = new JComboBox<>(getProductList());
        productComboBox.setPreferredSize(new Dimension(300, 25));
        JTextField quantityField = UIFactory.createNumericOnlyTextField(80);
        
        JButton addButton = UIFactory.createStyledButton("Add");
        addButton.addActionListener(e -> addProductFromDropdown(productComboBox, quantityField));
        
        selGbc.gridx = 0;
        selectionPanel.add(new JLabel("Product:"), selGbc);

        selGbc.gridx = 1;
        selectionPanel.add(productComboBox, selGbc);

        selGbc.gridx = 2;
        selectionPanel.add(new JLabel("Quantity:"), selGbc);

        selGbc.gridx = 3;
        selectionPanel.add(quantityField, selGbc);

        selGbc.gridx = 4;
        selectionPanel.add(addButton, selGbc);

        selGbc.gridx = 5;
        selGbc.weightx = 1.0;
        selectionPanel.add(Box.createHorizontalGlue(), selGbc);
        
        // Product table
        productTableModel = new DefaultTableModel(
            new String[]{"Product ID", "Description", "Quantity", "Unit Price", "Quoted Price"}, 0);
        productTable = new JTable(productTableModel);
        
        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setPreferredSize(new Dimension(650, 200));

        productPanel.add(selectionPanel, BorderLayout.NORTH);
        productPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Remove button
        JButton removeButton = UIFactory.createStyledButton("Remove Selected");
        removeButton.addActionListener(e -> removeProduct(productTable, productTableModel));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(245, 222, 179));
        buttonPanel.add(removeButton);
        productPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return productPanel;
    }

    private void addOrderDetailsSectionFields(JPanel panel, GridBagConstraints gbc) {
        orderDateSpinner = UIFactory.createDatePicker("MM/dd/yyyy", 150);
        UIFactory.addFormField(panel, "Order Date:", orderDateSpinner, gbc);
        
        shippingDateSpinner = UIFactory.createDatePicker("MM/dd/yyyy", 150);
        UIFactory.addFormField(panel, "Shipping Date:", shippingDateSpinner, gbc);
        
        // Status
        statusComboBox = new JComboBox<>(ORDER_STATUS);
        statusComboBox.setPreferredSize(new Dimension(100, 25));
        UIFactory.addFormField(panel, "Status:", statusComboBox, gbc);
        
        // Shipping Method
        shippingMethodComboBox = new JComboBox<>(SHIPPING_METHODS);
        shippingMethodComboBox.setPreferredSize(new Dimension(200, 25));
        UIFactory.addFormField(panel, "Shipping Method:", shippingMethodComboBox, gbc);
        
        // Sales Tax
        salesTax = UIFactory.createNumericTextField(75);
        UIFactory.addFormField(panel, "Sales Tax:", salesTax, gbc);
    }
    private Optional<String> getValidationError() {
        if (customerID.getText().trim().isEmpty()) {
            return Optional.of("Customer ID is required.");
        }
        if (orderDateSpinner.getValue() == null) {
            return Optional.of("Order Date is required.");
        }
        if (statusComboBox.getSelectedItem() == null || statusComboBox.getSelectedItem().toString().trim().isEmpty()) {
            return Optional.of("Status is required.");
        }
        if (salesTax.getText().trim().isEmpty()) {
            return Optional.of("Sales tax is required.");
        }
        if (productTableModel.getRowCount() == 0) {
            return Optional.of("Please add at least one product to the order.");
        }

        int customerIDValue;
        try {
            customerIDValue = Integer.parseInt(customerID.getText().trim());
            if (!CustomerService.isValidCustomer(customerIDValue)) {
                return Optional.of("Customer ID " + customerIDValue + " does not exist.");
            }
        } catch (NumberFormatException e) {
            return Optional.of("Customer ID must be a valid number.");
        }

        try {
            double salesTaxValue = Double.parseDouble(salesTax.getText().trim());
            if (salesTaxValue < 0.0 || salesTaxValue > 0.10) {
                return Optional.of("Sales tax must be between 0.0 and 0.10.");
            }
        } catch (NumberFormatException e) {
            return Optional.of("Please enter a valid number for sales tax.");
        }

        return Optional.empty();
    }

    private void handleSaveButton() {
        Optional<String> validationError = getValidationError();
        if (validationError.isPresent()) {
            JOptionPane.showMessageDialog(mainPanel, validationError.get(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear the existing list of items and rebuild it from the table model.
        // This ensures the order object is in sync with what the user sees on the screen,
        // preventing errors from stale data.
        order.getOrderItems().clear();
        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            Order.OrderItem item = new Order.OrderItem();
            item.setProductID((String) productTableModel.getValueAt(i, 0));
            item.setProductDescription((String) productTableModel.getValueAt(i, 1));
            item.setQuantityOrdered((Integer) productTableModel.getValueAt(i, 2));
            
            // The quoted price is stored as a formatted string (e.g., "$123.45"), so we parse it.
            String quotedPriceStr = (String) productTableModel.getValueAt(i, 4);
            try {
                double quotedPrice = Double.parseDouble(quotedPriceStr.replace("$", ""));
                item.setQuotedPrice(quotedPrice);
            } catch (NumberFormatException e) {
                // This is a fallback in case the price format is invalid.
                item.setQuotedPrice(0.0);
            }
            order.addOrderItem(item);
        }

        // Set basic order details
        order.setCustomerID(Integer.parseInt(customerID.getText().trim()));
        order.setStatus((String) statusComboBox.getSelectedItem());
        order.setShippingMethod((String) shippingMethodComboBox.getSelectedItem());
        order.setOrderDate(new java.sql.Date(((Date)orderDateSpinner.getValue()).getTime()));
        order.setShippingDate(new java.sql.Date(((Date)shippingDateSpinner.getValue()).getTime()));
        order.setSalesTax(Double.parseDouble(salesTax.getText().trim()));

        // Calculate totals before saving
        calculateTotals();

        // Save the order
        if (OrderService.addOrder(order)) {
            JOptionPane.showMessageDialog(mainPanel,
                "Order saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            mainApp.showScreen("OrderPresentation");
        } else {
            JOptionPane.showMessageDialog(mainPanel,
                "Unable to save order. Please check your data and try again.",
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }


private void removeProduct(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        }
    }

private void calculateTotals() {
        double subtotal = order.calculateSubtotal();
        
        // Handle empty sales tax field
        double taxRate = 0.0;
        String salesTaxText = salesTax.getText().trim();
        if (!salesTaxText.isEmpty()) {
            try {
                taxRate = Double.parseDouble(salesTaxText);
            } catch (NumberFormatException e) {
                // If invalid, set to 0 and show warning
                taxRate = 0.0;
                JOptionPane.showMessageDialog(mainPanel, 
                    "Invalid sales tax format. Using 0% tax rate.", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        
        order.setSalesTax(taxRate);
        double tax = order.calculateTax();
        double total = order.calculateTotal();
        
        // If you need to update any UI elements showing the totals, do it here
        // For example, if you have labels to display totals:
        // subtotalLabel.setText(String.format("$%.2f", subtotal));
        // taxLabel.setText(String.format("$%.2f", tax));
        // totalLabel.setText(String.format("$%.2f", total));
    }




    private JLayeredPane createTitlePanel() {
        return UIFactory.createTitlePanel(mainApp, mainPanel, "Create New Order", "Use this page to create a new Order.", "OrderPresentation");
    }

private double getQuotedPrice(double unitPrice, int quantity) {
        return unitPrice * quantity;
    }

private String[] getProductList() {
        List<Product> products = ProductService.getAllProducts();
        return products.stream()
            .map(p -> p.getProductID() + " - " + p.getProductDescription())
            .toArray(String[]::new);
    }


private void addProductFromDropdown(JComboBox<String> productComboBox, JTextField quantityField) {
    String selected = (String) productComboBox.getSelectedItem();
    String quantityText = quantityField.getText().trim();

    // Validate inputs
    if (selected == null || selected.trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Please select a product.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (quantityText.isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Please enter a quantity.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        int quantityToAdd = Integer.parseInt(quantityText);
        if (quantityToAdd <= 0) {
            JOptionPane.showMessageDialog(mainPanel, "Quantity must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String productId = selected.split(" - ")[0];
        String description = selected.split(" - ")[1];

        // Check if product already exists in the order
        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            if (productId.equals(productTableModel.getValueAt(i, 0))) {
                // Product exists, update quantity
                int existingQuantity = (int) productTableModel.getValueAt(i, 2);
                int newQuantity = existingQuantity + quantityToAdd;

                double unitPrice = ProductService.getProductPrice(productId);
                double newQuotedPrice = unitPrice * newQuantity;

                // Update the OrderItem in the order object
                Order.OrderItem itemToUpdate = order.getOrderItems().get(i);
                itemToUpdate.setQuantityOrdered(newQuantity);
                itemToUpdate.setQuotedPrice(newQuotedPrice);

                // Update the table model
                productTableModel.setValueAt(newQuantity, i, 2);
                productTableModel.setValueAt(String.format("$%.2f", newQuotedPrice), i, 4);

                quantityField.setText("");
                calculateTotals();
                return; // Exit the method
            }
        }

        // If product is not in the order, add it as a new item
        double unitPrice = ProductService.getProductPrice(productId);
        double quotedPrice = unitPrice * quantityToAdd;

        // Create and add OrderItem to the order
        Order.OrderItem item = new Order.OrderItem();
        item.setProductID(productId);
        item.setProductDescription(description);
        item.setQuantityOrdered(quantityToAdd);
        item.setQuotedPrice(quotedPrice);
        order.addOrderItem(item);

        // Add to table display
        productTableModel.addRow(new Object[]{
            productId,
            description,
            quantityToAdd,
            String.format("$%.2f", unitPrice),
            String.format("$%.2f", quotedPrice)
        });

        quantityField.setText("");
        calculateTotals();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(mainPanel, "Please enter a valid number for quantity.", "Validation Error", JOptionPane.ERROR_MESSAGE);
    } catch (ArrayIndexOutOfBoundsException e) {
        JOptionPane.showMessageDialog(mainPanel, "Invalid product format.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


}
