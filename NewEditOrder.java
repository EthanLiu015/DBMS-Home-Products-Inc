import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Calendar;
import java.sql.SQLException;
import java.util.List;




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
    addCustomerSection(formPanel, gbc);
    addOrderDetailsSection(formPanel, gbc);
    addProductSection(formPanel, gbc);
    
    // Center the form panel
    JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    centeringPanel.setBackground(new Color(245, 222, 179));
    centeringPanel.add(formPanel);
    
    // Create scroll pane and add the centering panel to it
    JScrollPane scrollPane = new JScrollPane(centeringPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.setBackground(new Color(245, 222, 179));
    
    // Add components to main panel
    mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
}
    private void addCustomerSection(JPanel panel, GridBagConstraints gbc) {
        addSectionHeader(panel, "Customer Information", gbc);
        customerID = createNumericTextField(300);
        addFormField(panel, "Customer ID:", customerID, gbc);
    }

    private void addProductSection(JPanel panel, GridBagConstraints gbc) {
    addProductSectionHeader(panel, "Products", gbc);
    JPanel productPanel = new JPanel(new BorderLayout(10, 10));
    productPanel.setBackground(new Color(245, 222, 179));
    productPanel.setPreferredSize(new Dimension(700, 300));
    
    // Product selection panel
    JPanel selectionPanel = new JPanel();
    selectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 15)); // Increased vertical padding to 15
    selectionPanel.setBackground(new Color(245, 222, 179));
    
    JComboBox<String> productComboBox = new JComboBox<>(getProductList());
    productComboBox.setPreferredSize(new Dimension(300, 25));
    JTextField quantityField = createNumericOnlyTextField(80);
    
    JLabel productLabel = new JLabel("Product:", SwingConstants.RIGHT);
    JLabel quantityLabel = new JLabel("Quantity:", SwingConstants.RIGHT);
    productLabel.setPreferredSize(new Dimension(60, 25));
    quantityLabel.setPreferredSize(new Dimension(60, 25));
    
    JButton addButton = createStyledButton("Add");
    addButton.addActionListener(e -> addProductFromDropdown(productComboBox, quantityField));
    
    selectionPanel.add(productLabel);
    selectionPanel.add(productComboBox);
    selectionPanel.add(quantityLabel);
    selectionPanel.add(quantityField);
    selectionPanel.add(addButton);
    
    // Product table
    productTableModel = new DefaultTableModel(
        new String[]{"Product ID", "Description", "Quantity"}, 0);
    productTable = new JTable(productTableModel);
    
    // Set column widths
    productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
    productTable.getColumnModel().getColumn(1).setPreferredWidth(300);
    productTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    
    productPanel.add(selectionPanel, BorderLayout.NORTH);
    productPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
    
    // Remove button
    JButton removeButton = createStyledButton("Remove Selected");
    removeButton.addActionListener(e -> removeProduct(productTable, productTableModel));
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.setBackground(new Color(245, 222, 179));
    buttonPanel.add(removeButton);
    productPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    panel.add(productPanel, gbc);
    gbc.gridy++;
}

private void addProductSectionHeader(JPanel panel, String text, GridBagConstraints gbc) {
    JLabel headerLabel = new JLabel(text);
    headerLabel.setFont(new Font("Roboto", Font.BOLD, 16));
    headerLabel.setForeground(new Color(139, 69, 19));
    
    JSeparator separator = new JSeparator();
    separator.setForeground(new Color(139, 69, 19));
    
    JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
    headerPanel.setBackground(new Color(245, 222, 179));
    
    // Special alignment for product header only
    headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 15));
    
    headerPanel.add(headerLabel, BorderLayout.NORTH);
    headerPanel.add(separator, BorderLayout.CENTER);
    
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(headerPanel, gbc);
    
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridy++;
}



    private void addOrderDetailsSection(JPanel panel, GridBagConstraints gbc) {
        addSectionHeader(panel, "Order Details", gbc);
    
    // Order Date with MM/dd/yyyy format
    SpinnerDateModel orderDateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
    orderDateSpinner = new JSpinner(orderDateModel);
    JSpinner.DateEditor orderDateEditor = new JSpinner.DateEditor(orderDateSpinner, "MM/dd/yyyy");
    orderDateSpinner.setEditor(orderDateEditor);
    orderDateSpinner.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Order Date:", orderDateSpinner, gbc);
    
    // Shipping Date with MM/dd/yyyy format
    SpinnerDateModel shippingDateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
    shippingDateSpinner = new JSpinner(shippingDateModel);
    JSpinner.DateEditor shippingDateEditor = new JSpinner.DateEditor(shippingDateSpinner, "MM/dd/yyyy");
    shippingDateSpinner.setEditor(shippingDateEditor);
    shippingDateSpinner.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Shipping Date:", shippingDateSpinner, gbc);
        
        // Status
        statusComboBox = new JComboBox<>(ORDER_STATUS);
        addFormField(panel, "Status:", statusComboBox, gbc);
        
        // Shipping Method
        shippingMethodComboBox = new JComboBox<>(SHIPPING_METHODS);
        addFormField(panel, "Shipping Method:", shippingMethodComboBox, gbc);
        
        // Sales Tax
        salesTax = createNumericTextField(300);
        addFormField(panel, "Sales Tax:", salesTax, gbc);
    }

    private void createFormFields(JPanel panel, GridBagConstraints gbc) {
    // Order Information Section
    addSectionHeader(panel, "Order Information", gbc);
    
    customerID = createNumericTextField(300);
    addFormField(panel, "Customer ID:", customerID, gbc);
    
    // Order Date with MM/dd/yyyy format
    SpinnerDateModel orderDateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
    orderDateSpinner = new JSpinner(orderDateModel);
    JSpinner.DateEditor orderDateEditor = new JSpinner.DateEditor(orderDateSpinner, "MM/dd/yyyy");
    orderDateSpinner.setEditor(orderDateEditor);
    orderDateSpinner.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Order Date:", orderDateSpinner, gbc);
    
    // Shipping Date
    SpinnerDateModel shippingDateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
    shippingDateSpinner = new JSpinner(shippingDateModel);
    JSpinner.DateEditor shippingDateEditor = new JSpinner.DateEditor(shippingDateSpinner, "MM/dd/yyyy");
    shippingDateSpinner.setEditor(shippingDateEditor);
    shippingDateSpinner.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Shipping Date:", shippingDateSpinner, gbc);
    
    // Status
    statusComboBox = new JComboBox<>(ORDER_STATUS);
    statusComboBox.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Status:", statusComboBox, gbc);
    
    // Shipping Method
    shippingMethodComboBox = new JComboBox<>(SHIPPING_METHODS);
    shippingMethodComboBox.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Shipping Method:", shippingMethodComboBox, gbc);
    
    // Sales Tax
    salesTax = createNumericTextField(300);
    addFormField(panel, "Sales Tax:", salesTax, gbc);
    
    // Products Section
    addSectionHeader(panel, "Products", gbc);
    addProductSection(panel, gbc);
}

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(new Color(245, 222, 179));

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        saveButton.addActionListener(e -> handleSaveButton());
        cancelButton.addActionListener(e -> mainApp.showScreen("OrderPresentation"));

        panel.add(saveButton);
        panel.add(cancelButton);
        return panel;
    }

    private JPanel createExitButton() {
    JPanel exitButtonPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int diameter = Math.min(getWidth(), getHeight()) - 10;
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            
            g2.setColor(getBackground());
            g2.fillOval(x, y, diameter, diameter);
            
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            int padding = diameter / 4;
            g2.drawLine(x + padding, y + padding, x + diameter - padding, y + diameter - padding);
            g2.drawLine(x + diameter - padding, y + padding, x + padding, y + diameter - padding);
        }
    };
    
    exitButtonPanel.setPreferredSize(new Dimension(40, 40));
    exitButtonPanel.setOpaque(false);
    exitButtonPanel.setBackground(new Color(139, 0, 0));
    
    exitButtonPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            System.exit(0);
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            exitButtonPanel.setBackground(new Color(100, 0, 0));
            exitButtonPanel.repaint();
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            exitButtonPanel.setBackground(new Color(139, 0, 0));
            exitButtonPanel.repaint();
        }
    });
    
    return exitButtonPanel;
}

    private void handleSaveButton() {
    // Check required fields first
    if (customerID.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Customer ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (orderDateSpinner.getValue() == null) {
        JOptionPane.showMessageDialog(mainPanel, "Order Date is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (statusComboBox.getSelectedItem() == null || statusComboBox.getSelectedItem().toString().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Status is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (salesTax.getText().trim().isEmpty()) {
    JOptionPane.showMessageDialog(mainPanel, "Sales tax is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
    return;
    }
    
    try {
        // Validate customer ID exists
        int customerIDValue = Integer.parseInt(customerID.getText().trim());
        if (!OrderService.isValidCustomer(customerIDValue)) {
            JOptionPane.showMessageDialog(mainPanel, "Customer ID " + customerIDValue + " does not exist.", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double salesTaxValue = 0.0; // Declare variable in outer scope
        try {
            salesTaxValue = Double.parseDouble(salesTax.getText().trim());
            if (salesTaxValue < 0.0 || salesTaxValue > 0.10) {
                JOptionPane.showMessageDialog(mainPanel, 
                "Sales tax must be between 0.0 and 0.10", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, 
            "Please enter a valid number for sales tax.", 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Set basic order details
        order.setCustomerID(customerIDValue);
        order.setStatus((String) statusComboBox.getSelectedItem());
        order.setShippingMethod((String) shippingMethodComboBox.getSelectedItem());
        order.setOrderDate(new java.sql.Date(((Date)orderDateSpinner.getValue()).getTime()));
        order.setShippingDate(new java.sql.Date(((Date)shippingDateSpinner.getValue()).getTime()));
        order.setSalesTax(salesTaxValue); // Now accessible
        
        // Verify order items exist
        if (order.getOrderItems().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, 
                "Please add at least one product to the order.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
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
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(mainPanel, 
            "Please ensure all numeric fields contain valid numbers.", 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}


private JPanel createProductPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    
    // Create table model with columns
    String[] columns = {"Product ID", "Description", "Quantity", "Unit Price", "Quoted Price"};
    DefaultTableModel model = new DefaultTableModel(columns, 0);
    JTable productTable = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(productTable);
    
    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton addButton = createStyledButton("Add Product");
    JButton removeButton = createStyledButton("Remove Product");
    
    addButton.addActionListener(e -> addProduct(productTableModel));
    removeButton.addActionListener(e -> removeProduct(productTable, productTableModel));
    
    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);
    
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    
    return panel;
}

    private void addProduct(DefaultTableModel model) {
        String productId = JOptionPane.showInputDialog("Enter Product ID:");
        if (productId != null && !productId.trim().isEmpty()) {
            try {
                String description = JOptionPane.showInputDialog("Enter Product Description:");
                String quantity = JOptionPane.showInputDialog("Enter Quantity:");
                String price = JOptionPane.showInputDialog("Enter Unit Price:");
                
                int qty = Integer.parseInt(quantity);
                double unitPrice = Double.parseDouble(price);
                double quotedPrice = getQuotedPrice(unitPrice, qty);
                
                Order.OrderItem item = new Order.OrderItem();
                item.setProductID(productId);
                item.setProductDescription(description);
                item.setQuantityOrdered(qty);
                item.setQuotedPrice(quotedPrice);
                
                order.addOrderItem(item);
                
                model.addRow(new Object[]{
                    productId,
                    description,
                    qty,
                    unitPrice,
                    quotedPrice
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers");
            }
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





// Form Validation
private boolean validateForm() {
    if (customerID.getText().trim().isEmpty() || 
        productTable.getModel().getRowCount() == 0 ||
        orderDateSpinner.getValue() == null ||
        statusComboBox.getSelectedItem() == null ||
        salesTax.getText().trim().isEmpty()) {
        return false;
    }
    
    try {
        double taxRate = Double.parseDouble(salesTax.getText());
        return taxRate >= 0.0 && taxRate <= 0.10;
    } catch (NumberFormatException e) {
        return false;
    }
}

    private JLayeredPane createTitlePanel() {
    JLayeredPane titlePanel = new JLayeredPane();
    titlePanel.setBackground(new Color(245, 222, 179));
    titlePanel.setOpaque(true);
    

    JLabel titleLabel = new JLabel("Create New Order", JLabel.CENTER);
    titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
    titleLabel.setForeground(new Color(139, 69, 19));
    titleLabel.setBounds(100, 20, 600, 50);
    
    // Description Label
    JTextArea descriptionLabel = new JTextArea("Use this page to create a new Order.");
    descriptionLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
    descriptionLabel.setForeground(new Color(139, 69, 19));
    descriptionLabel.setWrapStyleWord(false);
    descriptionLabel.setLineWrap(false);
    descriptionLabel.setEditable(false);
    descriptionLabel.setBackground(titlePanel.getBackground());
    
    // Exit Button
    JPanel exitButtonPanel = createExitButton();
    exitButtonPanel.setBounds(10, 10, 40, 40);
    
    // Back Button with hover effect
    JButton backButton = createStyledButton("Back");
    backButton.setBounds(10, 60, 100, 40);
    backButton.addActionListener(e -> mainApp.showScreen("OrderPresentation"));
    
    // Logo
    JLabel logoLabel = new JLabel();
    logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    
    // Get window size using SwingUtilities
    Window window = SwingUtilities.getWindowAncestor(mainPanel);
    int windowWidth = window != null ? window.getWidth() : 1024; // Default fallback width
    logoLabel.setBounds(windowWidth - 140, 10, 120, 90);
    
    // Add components to title panel
    titlePanel.add(exitButtonPanel, JLayeredPane.DEFAULT_LAYER);
    titlePanel.add(backButton, JLayeredPane.DEFAULT_LAYER);
    titlePanel.add(titleLabel, JLayeredPane.DEFAULT_LAYER);
    titlePanel.add(descriptionLabel, JLayeredPane.DEFAULT_LAYER);
    titlePanel.add(logoLabel, JLayeredPane.PALETTE_LAYER);
    
    // Handle resizing
    mainPanel.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            Dimension panelSize = mainPanel.getSize();
            int titleHeight = Math.max(panelSize.height / 8, 150);
            titlePanel.setPreferredSize(new Dimension(panelSize.width, titleHeight));
            
            // Resize logo
            int logoHeight = titleHeight * 3 / 4;
            int logoWidth = logoHeight * 4 / 3;
            ImageIcon logoIcon = new ImageIcon("Logo.jpg");
            Image scaledLogo = logoIcon.getImage().getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
            logoLabel.setBounds(panelSize.width - logoWidth - 20, (titleHeight - logoHeight) / 2, logoWidth, logoHeight);
            
            // Center title
            int titleLabelWidth = panelSize.width - logoWidth - 100;
            titleLabel.setBounds((panelSize.width - titleLabelWidth) / 2, 20, titleLabelWidth, 50);
            
            // Position description
            int descWidth = 800;
            int centerPosition = (panelSize.width - descWidth) / 2;
            descriptionLabel.setBounds(
                centerPosition + 170,
                titleLabel.getY() + titleLabel.getHeight() + 10,
                descWidth,
                40
            );
            
            titlePanel.revalidate();
            titlePanel.repaint();
        }
    });
    
    titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 150, 120)));
    return titlePanel;
}

    private JTextField createRestrictedTextField(int maxLength) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 25));
        field.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= maxLength) {
                    super.insertString(offs, str, a);
                }
            }
        });
        return field;
    }

    private JTextField createNumericTextField(int width) {
    JTextField field = new JTextField();
    field.setPreferredSize(new Dimension(width, 25));
    
    ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[0-9.]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }
        
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[0-9.]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
    
    return field;
}

    private JTextField createNumericOnlyTextField(int width) {
    JTextField field = new JTextField();
    field.setPreferredSize(new Dimension(width, 25));
    
    ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            // Get the current text plus the new string
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String resultingText = currentText.substring(0, offset) + string + currentText.substring(offset);
            
            if (isValidDecimal(resultingText)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            // Get the current text and calculate the resulting text after replacement
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String resultingText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
            
            if (isValidDecimal(resultingText)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
        
        private boolean isValidDecimal(String text) {
            if (text.isEmpty()) return true;
            try {
                if (text.equals(".")) return true;
                double value = Double.parseDouble(text);
                return text.matches("^\\d*\\.?\\d*$");
            } catch (NumberFormatException e) {
                return false;
            }
        }
    });
    
    return field;
}

   private void addSectionHeader(JPanel panel, String text, GridBagConstraints gbc) {
    JLabel headerLabel = new JLabel(text);
    headerLabel.setFont(new Font("Roboto", Font.BOLD, 16));
    headerLabel.setForeground(new Color(139, 69, 19));
    
    JSeparator separator = new JSeparator();
    separator.setForeground(new Color(139, 69, 19));
    
    JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
    headerPanel.setBackground(new Color(245, 222, 179));
    
    // Align with the start of the fields
    headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 120, 10, 15));
    
    headerPanel.add(headerLabel, BorderLayout.NORTH);
    headerPanel.add(separator, BorderLayout.CENTER);
    
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(headerPanel, gbc);
    
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridy++;
}




    private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc) {
    JLabel label = new JLabel(labelText);
    label.setPreferredSize(new Dimension(120, 25));
    
    // Create a container panel for label and field with FlowLayout
    JPanel fieldContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    fieldContainer.setBackground(new Color(245, 222, 179));
    
    fieldContainer.add(label);
    field.setPreferredSize(new Dimension(300, 25));
    fieldContainer.add(field);
    
    // Add the container to the main panel
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    panel.add(fieldContainer, gbc);
    gbc.gridwidth = 1;
    gbc.gridy++;
}


    private JButton createStyledButton(String text) {
    JButton button = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (getModel().isPressed()) {
                g2.setColor(new Color(170, 120, 60)); // Even darker when pressed
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(190, 140, 80)); // Darker shade on hover
            } else {
                g2.setColor(new Color(222, 174, 111)); // Normal color
            }
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw the text
            FontMetrics metrics = g2.getFontMetrics(getFont());
            int x = (getWidth() - metrics.stringWidth(getText())) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), x, y);
        }
    };
    
    button.setFont(new Font("Roboto", Font.PLAIN, 16));
    button.setForeground(Color.WHITE);
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(139, 69, 19), 2),
        BorderFactory.createEmptyBorder(5, 15, 5, 15)
    ));
    button.setFocusPainted(false);
    button.setContentAreaFilled(false); // Important: disable default button fill
    
    // Add these properties
    button.setOpaque(true);
    button.setRolloverEnabled(true);
    
    return button;
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
        JOptionPane.showMessageDialog(mainPanel, 
            "Please select a product.", 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (quantityText.isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, 
            "Please enter a quantity.", 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    try {
        int quantity = Integer.parseInt(quantityText);
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(mainPanel, 
                "Quantity must be greater than 0.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String productId = selected.split(" - ")[0];
        String description = selected.split(" - ")[1];
        
        // Get the regular unit price from database
        double unitPrice = ProductService.getProductPrice(productId);
        
        // Calculate quoted price based on quantity (implement any discount logic here)
        double quotedPrice = unitPrice * quantity; // Quoted price should be unitPrice * quantity
        
        // Create and add OrderItem to the order
        Order.OrderItem item = new Order.OrderItem();
        item.setProductID(productId);
        item.setProductDescription(description);
        item.setQuantityOrdered(quantity);
        item.setQuotedPrice(quotedPrice);
        order.addOrderItem(item);
        
        // Add to table display
        productTableModel.addRow(new Object[]{
            productId,
            description,
            quantity,
            String.format("$%.2f", unitPrice),
            String.format("$%.2f", quotedPrice)
        });
        
        quantityField.setText("");
        
        // Calculate totals after adding product
        calculateTotals();
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(mainPanel, 
            "Please enter a valid number for quantity.", 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
    } catch (ArrayIndexOutOfBoundsException e) {
        JOptionPane.showMessageDialog(mainPanel, 
            "Invalid product format.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}


}
