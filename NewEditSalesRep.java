import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.util.List;
import java.util.stream.Collectors;



public class NewEditSalesRep {
    private MainApplication mainApp;
    private SalesRep salesRep;
    private JPanel mainPanel;
    private boolean isEditMode = false;
    private SalesRep existingSalesRep;

    // Form Fields
    private JTextField repIDField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField titleField;
    private JTextField businessPhoneField;
    private JTextField cellPhoneField;
    private JTextField homePhoneField;
    private JTextField faxNumberField;
    private JTextField zipCodeField;
    private JTextField streetField;
    private JTextField cityField;
    private JComboBox<String> stateComboBox;
    private JTextField commissionField;
    private JComboBox<Integer> managerComboBox;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public NewEditSalesRep(MainApplication mainApp, SalesRep existingSalesRep) {
        this.mainApp = mainApp;
        this.isEditMode = existingSalesRep != null;
        this.existingSalesRep = existingSalesRep;
        createAndShowGUI();
        if (isEditMode) {
            populateFields();
        }
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Create form fields with proper formatting and restrictions
        createFormFields(panel, gbc);
        
        return panel;
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

    // Populate fields after all components are created
    if (isEditMode && existingSalesRep != null) {
        SwingUtilities.invokeLater(this::populateFields);
    }
}

    private void handleSaveButton() {
    // Validate required fields
    if (!validateFields()) {
        return;
    }

    // Create or update sales rep object
    SalesRep salesRep;
    if (isEditMode) {
        salesRep = existingSalesRep;
    } else {
        salesRep = new SalesRep();
        // RepID will be generated in the database
    }

    // Set all fields
    salesRep.setFirstName(firstNameField.getText().trim());
    salesRep.setLastName(lastNameField.getText().trim());
    salesRep.setTitle(titleField.getText().trim());
    salesRep.setBusinessPhone(businessPhoneField.getText().trim());
    salesRep.setCellPhone(cellPhoneField.getText().trim());
    salesRep.setHomePhone(homePhoneField.getText().trim());
    salesRep.setFaxNumber(faxNumberField.getText().trim());
    salesRep.setStreet(streetField.getText().trim());
    salesRep.setCity(cityField.getText().trim());
    salesRep.setState((String) stateComboBox.getSelectedItem());
    salesRep.setZipCode(zipCodeField.getText().trim());
    salesRep.setCommission(Double.parseDouble(commissionField.getText().trim()));
    salesRep.setManager((Integer) managerComboBox.getSelectedItem());

    // Save or update the sales rep
    boolean success = isEditMode ? 
        SalesRepService.updateSalesRep(salesRep) : 
        SalesRepService.addSalesRep(salesRep);

    if (success) {
        JOptionPane.showMessageDialog(mainPanel, 
            isEditMode ? "Sales Rep updated successfully!" : "Sales Rep saved successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        mainApp.showScreen("SalesRepPresentation");
    } else {
        JOptionPane.showMessageDialog(mainPanel, 
            "Unable to " + (isEditMode ? "update" : "save") + " sales rep. Please check your data and try again.", 
            "Save Error", JOptionPane.ERROR_MESSAGE);
    }
}


   private void createFormFields(JPanel panel, GridBagConstraints gbc) {
    // Personal Information Section
    addSectionHeader(panel, "Personal Information", gbc);
    
    // First Name
    firstNameField = new JTextField();
    firstNameField.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "First Name:", firstNameField, gbc);
    
    // Last Name
    lastNameField = new JTextField();
    lastNameField.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Last Name:", lastNameField, gbc);
    
    // Title
    titleField = new JTextField();
    titleField.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Title:", titleField, gbc);

    // Contact Information Section
    addSectionHeader(panel, "Contact Information", gbc);
    
    /// Add numeric validation to phone fields
businessPhoneField = new JTextField();
businessPhoneField.setPreferredSize(new Dimension(300, 25));
((AbstractDocument) businessPhoneField.getDocument()).setDocumentFilter(new DocumentFilter() {
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

// Apply the same numeric filter to other phone fields
cellPhoneField = new JTextField();
cellPhoneField.setPreferredSize(new Dimension(300, 25));
((AbstractDocument) cellPhoneField.getDocument()).setDocumentFilter(new NumericOnlyFilter());

homePhoneField = new JTextField();
homePhoneField.setPreferredSize(new Dimension(300, 25));
((AbstractDocument) homePhoneField.getDocument()).setDocumentFilter(new NumericOnlyFilter());

faxNumberField = new JTextField();
faxNumberField.setPreferredSize(new Dimension(300, 25));
((AbstractDocument) faxNumberField.getDocument()).setDocumentFilter(new NumericOnlyFilter());
    
    // Address fields
    streetField = new JTextField();
    streetField.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Street:", streetField, gbc);
    
    cityField = new JTextField();
    cityField.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "City:", cityField, gbc);
    
    // State dropdown
    String[] states = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", 
                      "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", 
                      "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", 
                      "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", 
                      "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
    stateComboBox = new JComboBox<>(states);
    stateComboBox.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "State:", stateComboBox, gbc);
    
    
// Zip code numeric validation
zipCodeField = new JTextField();
zipCodeField.setPreferredSize(new Dimension(300, 25));
((AbstractDocument) zipCodeField.getDocument()).setDocumentFilter(new NumericOnlyFilter());

    // Business Information Section
    addSectionHeader(panel, "Business Information", gbc);
    
    // Commission field with validation (5% to 10%)
    // Commission field with decimal validation (5% to 10%)
commissionField = new JTextField();
commissionField.setPreferredSize(new Dimension(300, 25));
((AbstractDocument) commissionField.getDocument()).setDocumentFilter(new DocumentFilter() {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        if (newText.matches("^[0-9]*\\.?[0-9]*$")) {
            super.insertString(fb, offset, string, attr);
        }
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
        if (newText.matches("^[0-9]*\\.?[0-9]*$")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
});
    addFormField(panel, "Commission Rate:", commissionField, gbc);
    
    List<SalesRep> allReps = SalesRepService.getAllSalesReps();
List<Integer> managers = allReps.stream()
    .map(SalesRep::getRepID)
    .collect(Collectors.toList());
Integer[] managerArray = managers.toArray(new Integer[0]);
managerComboBox = new JComboBox<>(managerArray);

// After creating businessPhoneField
addFormField(panel, "Business Phone:", businessPhoneField, gbc);

// After creating cellPhoneField
addFormField(panel, "Cell Phone:", cellPhoneField, gbc);

// After creating homePhoneField
addFormField(panel, "Home Phone:", homePhoneField, gbc);

// After creating faxNumberField
addFormField(panel, "Fax Number:", faxNumberField, gbc);

// After creating zipCodeField
addFormField(panel, "Zip Code:", zipCodeField, gbc);

// After creating managerComboBox
managerComboBox.setPreferredSize(new Dimension(300, 25));
addFormField(panel, "Manager:", managerComboBox, gbc);
}

private JFormattedTextField createFormattedPhoneField() {
    try {
        MaskFormatter phoneFormatter = new MaskFormatter("(###) ###-####");
        phoneFormatter.setPlaceholderCharacter('_');
        JFormattedTextField field = new JFormattedTextField(phoneFormatter);
        field.setPreferredSize(new Dimension(300, 25));
        return field;
    } catch (ParseException e) {
        return new JFormattedTextField();
    }
}

private boolean validateFields() {
    if (firstNameField.getText().trim().isEmpty()) {
        showValidationError("First Name is required.");
        return false;
    }

    if (lastNameField.getText().trim().isEmpty()) {
        showValidationError("Last Name is required.");
        return false;
    }

    if (commissionField.getText().trim().isEmpty()) {
        showValidationError("Commission Rate is required.");
        return false;
    }

    try {
        double commission = Double.parseDouble(commissionField.getText().trim());
        if (commission < 0.05 || commission > 0.10) {
            showValidationError("Commission must be between 5% and 10%.");
            return false;
        }
    } catch (NumberFormatException e) {
        showValidationError("Invalid commission format.");
        return false;
    }

    if (managerComboBox.getSelectedItem() == null) {
        showValidationError("Manager is required.");
        return false;
    }

    return true;
}


    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

     private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(new Color(245, 222, 179));

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        saveButton.addActionListener(e -> handleSaveButton());
        cancelButton.addActionListener(e -> mainApp.showScreen("SalesRepPresentation"));

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

private JLayeredPane createTitlePanel() {
    JLayeredPane titlePanel = new JLayeredPane();
    titlePanel.setBackground(new Color(245, 222, 179));
    titlePanel.setOpaque(true);
    

    JLabel titleLabel = new JLabel(isEditMode ? 
    "Edit Sales Rep: " + existingSalesRep.getFirstName() + " " + existingSalesRep.getLastName() 
    : "Create New Sales Rep", JLabel.CENTER);
    titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
    titleLabel.setForeground(new Color(139, 69, 19));
    titleLabel.setBounds(100, 20, 600, 50);
    
    // Description Label
    JTextArea descriptionLabel = new JTextArea("Use this page to edit or create a new sales rep.");
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
    backButton.addActionListener(e -> mainApp.showScreen("SalesRepPresentation"));
    
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

private void addSectionHeader(JPanel panel, String text, GridBagConstraints gbc) {
    JLabel headerLabel = new JLabel(text);
    headerLabel.setFont(new Font("Roboto", Font.BOLD, 16));
    headerLabel.setForeground(new Color(139, 69, 19));
    
    JSeparator separator = new JSeparator();
    separator.setForeground(new Color(139, 69, 19));
    
    JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
    headerPanel.setBackground(new Color(245, 222, 179));
    
    // Align headers with fields by matching the left padding
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

private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc) {
    JLabel label = new JLabel(labelText);
    label.setPreferredSize(new Dimension(150, 25));  // Set fixed width for labels
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

    private void populateFields() {
    firstNameField.setText(existingSalesRep.getFirstName());
    lastNameField.setText(existingSalesRep.getLastName());
    titleField.setText(existingSalesRep.getTitle());
    businessPhoneField.setText(existingSalesRep.getBusinessPhone());
    cellPhoneField.setText(existingSalesRep.getCellPhone());
    homePhoneField.setText(existingSalesRep.getHomePhone());
    faxNumberField.setText(existingSalesRep.getFaxNumber());
    streetField.setText(existingSalesRep.getStreet());
    cityField.setText(existingSalesRep.getCity());
    stateComboBox.setSelectedItem(existingSalesRep.getState());
    zipCodeField.setText(existingSalesRep.getZipCode());
    commissionField.setText(String.format("%.2f", existingSalesRep.getCommission()));
    managerComboBox.setSelectedItem(existingSalesRep.getManager());
}


    private void clearFields() {
        repIDField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        titleField.setText("");
        businessPhoneField.setText("");
        cellPhoneField.setText("");
        homePhoneField.setText("");
        faxNumberField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateComboBox.setSelectedIndex(0);
        zipCodeField.setText("");
        commissionField.setText("");
        managerComboBox.setSelectedIndex(0);
    }

    class DecimalDocumentFilter extends DocumentFilter {
    private final int maxIntegerDigits;
    private final int maxFractionDigits;

    public DecimalDocumentFilter(int maxIntegerDigits, int maxFractionDigits) {
        this.maxIntegerDigits = maxIntegerDigits;
        this.maxFractionDigits = maxFractionDigits;
    }

    protected boolean isValid(String text) {
        if (text.isEmpty()) return true;
        try {
            double value = Double.parseDouble(text);
            return value >= 0.05 && value <= 0.10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

// Add this helper class for reusability
class NumericOnlyFilter extends DocumentFilter {
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
}

}