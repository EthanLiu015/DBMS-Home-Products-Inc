import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.ArrayList;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewEditProduct {
    private MainApplication mainApp;
    private Product product;
    private JPanel mainPanel;
    private JTextField productIDField;
    private JTextArea productDescriptionArea;
    private JTextField unitPriceField;
    private JTextField unitsOnHandField;
    private JComboBox<String> productClassComboBox;
    private JComboBox<Integer> warehouseComboBox;
    private boolean isEditMode = false;
    private Product existingProduct;

    public NewEditProduct(MainApplication mainApp, Product existingProduct) {
        this.mainApp = mainApp;
        this.isEditMode = existingProduct != null;
        this.existingProduct = existingProduct;
        createAndShowGUI();
        if (isEditMode) {
            populateFields();
        }
    }

    private void createAndShowGUI() {
    mainPanel = new JPanel(new BorderLayout());
    
    // Create the form panel that will go inside the scroll pane
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(new Color(245, 222, 179));
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(10, 10, 10, 10);
    
    // Add form fields to the form panel
    createFormFields(formPanel, gbc);
    
    // Create scroll pane and add the form panel to it
    JScrollPane scrollPane = new JScrollPane(formPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    
    // Add components to main panel
    mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
}

    private void createFormFields(JPanel panel, GridBagConstraints gbc) {
        // Product Information Section
        addSectionHeader(panel, "Product Information", gbc);

        // ProductID field with mask formatter
        // Replace the existing ProductID field initialization with this:
if (!isEditMode) {
    productIDField = new JTextField();
    productIDField.setPreferredSize(new Dimension(300, 25));
    // Add document filter to enforce the format
    ((AbstractDocument) productIDField.getDocument()).setDocumentFilter(new ProductIDDocumentFilter());
} else {
    // Keep the existing masked formatter for edit mode
    try {
        MaskFormatter formatter = new MaskFormatter("UU##");
        formatter.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        productIDField = new JFormattedTextField(formatter);
        productIDField.setPreferredSize(new Dimension(300, 25));
    } catch (ParseException e) {
        productIDField = new JTextField();
    }
}
        addFormField(panel, "Product ID:", productIDField, gbc);

        // Product Description
        productDescriptionArea = new JTextArea();
        productDescriptionArea.setRows(3);
        productDescriptionArea.setLineWrap(true);
        productDescriptionArea.setWrapStyleWord(true);
        ((AbstractDocument) productDescriptionArea.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= 200) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() - length + text.length()) <= 200) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        JScrollPane descScrollPane = new JScrollPane(productDescriptionArea);
        descScrollPane.setPreferredSize(new Dimension(300, 75));
        addFormField(panel, "Description:", descScrollPane, gbc);

        // Unit Price field
        unitPriceField = new JTextField();
        unitPriceField.setPreferredSize(new Dimension(300, 25));
        ((AbstractDocument) unitPriceField.getDocument()).setDocumentFilter(new DocumentFilter() {
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
        addFormField(panel, "Unit Price:", unitPriceField, gbc);

        // Units on Hand field
        unitsOnHandField = new JTextField();
        unitsOnHandField.setPreferredSize(new Dimension(300, 25));
        ((AbstractDocument) unitsOnHandField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("[0-9]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("[0-9]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        addFormField(panel, "Units on Hand:", unitsOnHandField, gbc);

        // Product Class dropdown
        String[] productClasses = {"SG", "HW", "AP", "GS", "TO"};
        productClassComboBox = new JComboBox<>(productClasses);
        productClassComboBox.setPreferredSize(new Dimension(300, 25));
        addFormField(panel, "Product Class:", productClassComboBox, gbc);

        // Warehouse dropdown (populate from database)
        List<Integer> warehouses = ProductService.getAllWarehouseIDs();
        Integer[] warehouseArray = warehouses.toArray(new Integer[0]);
        warehouseComboBox = new JComboBox<>(warehouseArray);
        warehouseComboBox.setPreferredSize(new Dimension(300, 25));
        addFormField(panel, "Warehouse:", warehouseComboBox, gbc);
    }

    private JLayeredPane createTitlePanel() {
    JLayeredPane titlePanel = new JLayeredPane();
    titlePanel.setBackground(new Color(245, 222, 179));
    titlePanel.setOpaque(true);
    

    JLabel titleLabel = new JLabel(isEditMode ? 
        "Edit Product: " + existingProduct.getProductID()
        : "Create New Product", JLabel.CENTER);
    titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
    titleLabel.setForeground(new Color(139, 69, 19));
    titleLabel.setBounds(100, 20, 600, 50);
    
    // Description Label
JTextArea descriptionLabel = new JTextArea("Use this page to edit or create a new Product.");
descriptionLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
descriptionLabel.setForeground(new Color(139, 69, 19));
descriptionLabel.setWrapStyleWord(false);
descriptionLabel.setLineWrap(false);
descriptionLabel.setEditable(false);
descriptionLabel.setBackground(titlePanel.getBackground());

// Center alignment
descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
descriptionLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

// Calculate center position
int textWidth = descriptionLabel.getPreferredSize().width;
int panelWidth = titlePanel.getWidth();
int x = (panelWidth - textWidth) / 2;
descriptionLabel.setBounds(x, titleLabel.getY() + titleLabel.getHeight() + 10, textWidth, 40);
    
    // Exit Button
    JPanel exitButtonPanel = createExitButton();
    exitButtonPanel.setBounds(10, 10, 40, 40);
    
    // Back Button with hover effect
    JButton backButton = createStyledButton("Back");
    backButton.setBounds(10, 60, 100, 40);
    backButton.addActionListener(e -> mainApp.showScreen("ProductPresentation"));
    
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


private void handleSaveButton() {
    // Validate required fields
    if (productIDField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Product ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (productDescriptionArea.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Product Description is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (unitPriceField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Unit Price is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (unitsOnHandField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Units on Hand is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (productClassComboBox.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(mainPanel, "Product Class is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (warehouseComboBox.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(mainPanel, "Warehouse is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Create or update product object
    Product product;
    if (isEditMode) {
        product = existingProduct;
    } else {
        product = new Product();
    }
    
    // Set all fields
    product.setProductID(productIDField.getText().trim());
    product.setProductDescription(productDescriptionArea.getText().trim());
    
    try {
        product.setUnitPrice(Double.parseDouble(unitPriceField.getText().trim()));
        product.setUnitsonHand(Short.parseShort(unitsOnHandField.getText().trim()));
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(mainPanel, "Invalid number format in price or units.", 
            "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    product.setProductClass((String) productClassComboBox.getSelectedItem());
    product.setWarehouse((Integer) warehouseComboBox.getSelectedItem());
    
    if (!isEditMode && ProductService.productIDExists(productIDField.getText().trim())) {
        JOptionPane.showMessageDialog(mainPanel, 
            "Product ID already exists. Please choose a different ID.", 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Save or update the product
    boolean success;
    if (isEditMode) {
        success = ProductService.updateProduct(product);
    } else {
        success = ProductService.addProduct(product);
    }
    
    if (success) {
        JOptionPane.showMessageDialog(mainPanel, 
            isEditMode ? "Product updated successfully!" : "Product saved successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        mainApp.showScreen("ProductPresentation");
    } else {
        JOptionPane.showMessageDialog(mainPanel, 
            "Unable to " + (isEditMode ? "update" : "save") + " product. Please check your data and try again.", 
            "Save Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void populateFields() {
    productIDField.setText(existingProduct.getProductID());
    productIDField.setEnabled(false);  // Disable the field in edit mode
    productIDField.setBackground(new Color(240, 240, 240));  // Visual feedback that it's disabled
    
    productDescriptionArea.setText(existingProduct.getProductDescription());
    unitPriceField.setText(String.valueOf(existingProduct.getUnitPrice()));
    unitsOnHandField.setText(String.valueOf(existingProduct.getUnitsonHand()));
    productClassComboBox.setSelectedItem(existingProduct.getProductClass());
    warehouseComboBox.setSelectedItem(existingProduct.getWarehouse());
}

public JPanel getMainPanel() {
    return mainPanel;
}

private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    panel.setBackground(new Color(245, 222, 179));

    JButton saveButton = createStyledButton("Save");
    JButton cancelButton = createStyledButton("Cancel");

    saveButton.addActionListener(e -> handleSaveButton());
    cancelButton.addActionListener(e -> mainApp.showScreen("ProductPresentation"));

    panel.add(saveButton);
    panel.add(cancelButton);
    return panel;
}

private JPanel createExitButton() {
    JPanel exitButtonPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
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

private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc) {
    JLabel label = new JLabel(labelText);
    label.setPreferredSize(new Dimension(150, 25));
    panel.add(label, gbc);
    
    gbc.gridx++;
    panel.add(field, gbc);
    gbc.gridx--;
    gbc.gridy++;
}

private JButton createStyledButton(String text) {
    JButton button = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (getModel().isPressed()) {
                g2.setColor(new Color(170, 120, 60));
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(190, 140, 80));
            } else {
                g2.setColor(new Color(222, 174, 111));
            }
            g2.fillRect(0, 0, getWidth(), getHeight());
            
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
    button.setContentAreaFilled(false);
    button.setOpaque(true);
    button.setRolloverEnabled(true);
    
    return button;
}

private void addSectionHeader(JPanel panel, String text, GridBagConstraints gbc) {
    JLabel headerLabel = new JLabel(text);
    headerLabel.setFont(new Font("Roboto", Font.BOLD, 16));
    headerLabel.setForeground(new Color(139, 69, 19));
    
    JSeparator separator = new JSeparator();
    separator.setForeground(new Color(139, 69, 19));
    
    JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
    headerPanel.setBackground(new Color(245, 222, 179));
    headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 165, 10, 15));
    
    headerPanel.add(headerLabel, BorderLayout.NORTH);
    headerPanel.add(separator, BorderLayout.CENTER);
    
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(headerPanel, gbc);
    
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridy++;
}

// Custom document filter for decimal validation
class DecimalDocumentFilter extends DocumentFilter {
    private final int maxIntegerDigits;
    private final int maxFractionDigits;

    public DecimalDocumentFilter(int maxIntegerDigits, int maxFractionDigits) {
        this.maxIntegerDigits = maxIntegerDigits;
        this.maxFractionDigits = maxFractionDigits;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder builder = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        builder.insert(offset, string);
        if (isValid(builder.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder builder = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        builder.replace(offset, offset + length, text);
        if (isValid(builder.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isValid(String text) {
        if (text.isEmpty()) return true;
        
        String[] parts = text.split("\\.");
        if (parts.length > 2) return false;
        
        // Check integer part
        if (parts[0].length() > maxIntegerDigits) return false;
        if (!parts[0].matches("\\d*")) return false;
        
        // Check decimal part if exists
        if (parts.length == 2) {
            if (parts[1].length() > maxFractionDigits) return false;
            if (!parts[1].matches("\\d*")) return false;
        }
        
        return true;
    }
}

// Custom document filter for product ID validation
class ProductIDDocumentFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (isValid(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) {
            super.insertString(fb, offset, string.toUpperCase(), attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
        if (isValid(newText)) {
            super.replace(fb, offset, length, text.toUpperCase(), attrs);
        }
    }

    private boolean isValid(String text) {
        if (text.length() > 4) return false;
        return text.matches("[A-Za-z]{0,2}[0-9]{0,2}");
    }
}

private boolean isValidWarehouse(int warehouseId) {
    return ProductService.warehouseExists(warehouseId);
}

// Method to clear all form fields
private void clearFields() {
    productIDField.setText("");
    productDescriptionArea.setText("");
    unitPriceField.setText("");
    unitsOnHandField.setText("");
    productClassComboBox.setSelectedIndex(0);
    warehouseComboBox.setSelectedIndex(0);
}

}