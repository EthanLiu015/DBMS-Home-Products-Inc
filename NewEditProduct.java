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
    
    // Create scroll pane using the factory
    JScrollPane scrollPane = UIFactory.createThemedScrollPane(formPanel);
    
    // Add components to main panel
    mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    mainPanel.add(UIFactory.createSaveCancelButtonPanel(this::handleSaveButton, () -> mainApp.showScreen("ProductPresentation")), BorderLayout.SOUTH);
}

    private void createFormFields(JPanel panel, GridBagConstraints gbc) {
        // Product Information Section
        UIFactory.addSectionHeader(panel, "Product Information", gbc);

        // ProductID field with mask formatter
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
                productIDField = new JTextField(); // Fallback
            }
        }
        UIFactory.addFormField(panel, "Product ID:", productIDField, gbc);

        // Product Description
        productDescriptionArea = UIFactory.createRestrictedTextArea(3, 20, 200);
        JScrollPane descScrollPane = new JScrollPane(productDescriptionArea);
        descScrollPane.setPreferredSize(new Dimension(300, 75));
        UIFactory.addFormField(panel, "Description:", descScrollPane, gbc);

        // Unit Price field
        unitPriceField = UIFactory.createNumericTextField(300);
        UIFactory.addFormField(panel, "Unit Price:", unitPriceField, gbc);

        // Units on Hand field
        unitsOnHandField = UIFactory.createNumericOnlyTextField(300);
        UIFactory.addFormField(panel, "Units on Hand:", unitsOnHandField, gbc);

        // Product Class dropdown
        String[] productClasses = {"SG", "HW", "AP", "GS", "TO"};
        productClassComboBox = new JComboBox<>(productClasses);
        productClassComboBox.setPreferredSize(new Dimension(300, 25));
        UIFactory.addFormField(panel, "Product Class:", productClassComboBox, gbc);

        // Warehouse dropdown (populate from database)
        List<Integer> warehouses = ProductService.getAllWarehouseIDs();
        Integer[] warehouseArray = warehouses.toArray(new Integer[0]);
        warehouseComboBox = new JComboBox<>(warehouseArray);
        warehouseComboBox.setPreferredSize(new Dimension(300, 25));
        UIFactory.addFormField(panel, "Warehouse:", warehouseComboBox, gbc);
    }

    private JLayeredPane createTitlePanel() {
        return UIFactory.createTitlePanel(mainApp, mainPanel,
            isEditMode ? "Edit Product: " + existingProduct.getProductID() : "Create New Product",
            "Use this page to edit or create a new Product.", "ProductPresentation");
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