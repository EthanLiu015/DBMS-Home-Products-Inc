import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class NewEditCustomer {
    private MainApplication mainApp;
    private Customer customer;
    private JPanel mainPanel;
    private JTextField firstNameField, lastNameField, streetField, cityField;
    private JComboBox<String> stateComboBox;
    private JTextField zipCodeField, customerCreditField;
    private JTextField repIDField, companyField, websiteField, emailField;
    private JTextField businessPhoneField, cellPhoneField;
    private JTextField titleField;
    private JComboBox<String> statusComboBox;
    private JTextArea notesArea;
    private boolean isEditMode = false;
    private Customer existingCustomer;

    public NewEditCustomer(MainApplication mainApp, Customer existingCustomer) {
        this.mainApp = mainApp;
        this.isEditMode = existingCustomer != null;
        this.existingCustomer = existingCustomer;
        createAndShowGUI();
        if (isEditMode) {
            populateFields();
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
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
    if (isEditMode && existingCustomer != null) {
        SwingUtilities.invokeLater(this::populateFields);
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
        private void createFormFields(JPanel panel, GridBagConstraints gbc) {
    // Personal Information Section
    addSectionHeader(panel, "Personal Information", gbc);
    
    firstNameField = createRestrictedTextField(50, 200);
    addFormField(panel, "First Name:", firstNameField, gbc);
    
    lastNameField = createRestrictedTextField(50, 200);
    addFormField(panel, "Last Name:", lastNameField, gbc);
    
    titleField = createRestrictedTextField(50, 200);
    addFormField(panel, "Title:", titleField, gbc);
    
    // Contact Information Section
    addSectionHeader(panel, "Contact Information", gbc);
    
    streetField = createRestrictedTextField(100, 300);
    addFormField(panel, "Street:", streetField, gbc);
    
    cityField = createRestrictedTextField(50, 300);
    addFormField(panel, "City:", cityField, gbc);
    
    String[] states = {"", "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                      "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                      "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
                      "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
                      "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
    stateComboBox = new JComboBox<>(states);
    stateComboBox.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "State:", stateComboBox, gbc);
    
    zipCodeField = createNumericTextField(300);
    addFormField(panel, "Zip Code:", zipCodeField, gbc);
    
    businessPhoneField = createPhoneField(300);
    addFormField(panel, "Business Phone:", businessPhoneField, gbc);
    
    cellPhoneField = createPhoneField(300);
    addFormField(panel, "Cell Phone:", cellPhoneField, gbc);


    emailField = createRestrictedTextField(100, 300);
    addFormField(panel, "Email:", emailField, gbc);
    
    // Business Information Section
    addSectionHeader(panel, "Business Information", gbc);
    
    companyField = createRestrictedTextField(50, 300);
    addFormField(panel, "Company:", companyField, gbc);
    
    websiteField = createRestrictedTextField(400, 300);
    addFormField(panel, "Website:", websiteField, gbc);

    repIDField = createNumericTextField(300);
    addFormField(panel, "Sales Representative ID:", repIDField, gbc);

    String[] statusOptions = {"", "Active", "Inactive"};
    statusComboBox = new JComboBox<>(statusOptions);
    statusComboBox.setPreferredSize(new Dimension(300, 25));
    addFormField(panel, "Status:", statusComboBox, gbc);

    // Notes Section
    addSectionHeader(panel, "Additional Information", gbc);
        notesArea = createNotesArea();
    notesArea.setRows(4);
    notesArea.setColumns(40);
    addFormField(panel, "Notes:", new JScrollPane(notesArea), gbc);

    // Financial Information Section
    addSectionHeader(panel, "Financial Information", gbc);

    customerCreditField = createNumericTextField(100);
    addFormField(panel, "Credit Limit:", customerCreditField, gbc);

}

    private JTextField createRestrictedTextField(int maxLength, int width) {
    JTextField field = new JTextField();
    field.setPreferredSize(new Dimension(width, 25));
    return field;
}

    private JTextField createPhoneField(int width) {
    JTextField field = new JTextField();
    field.setPreferredSize(new Dimension(width, 25));
    return field;
}

    private JTextField createNumericTextField(int width) {
    JTextField field = new JTextField();
    field.setPreferredSize(new Dimension(width, 25));
    
    // Only allow numbers and decimal point
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

    private JTextArea createNotesArea() {
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setRows(4);
        area.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= 1000) {
                    super.insertString(offs, str, a);
                }
            }
        });
        return area;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(new Color(245, 222, 179));

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        saveButton.addActionListener(e -> handleSaveButton());
        cancelButton.addActionListener(e -> mainApp.showScreen("CustomerPresentation"));

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
    if (firstNameField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "First Name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (lastNameField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Last Name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (repIDField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(mainPanel, "Sales Representative ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validate Sales Rep ID exists
    try {
        int repID = Integer.parseInt(repIDField.getText().trim());
        if (!CustomerService.isValidSalesRep(repID)) {
            JOptionPane.showMessageDialog(mainPanel, "Sales Representative ID " + repID + " does not exist.", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(mainPanel, "Sales Representative ID must be a valid number.", 
            "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Create or update customer object
    Customer customer;
    if (isEditMode) {
        customer = existingCustomer;
        customer.setCustomerID(existingCustomer.getCustomerID());
    } else {
        customer = new Customer();
    }
    
    // Set all fields
    customer.setFirstName(firstNameField.getText().trim());
    customer.setLastName(lastNameField.getText().trim());
    customer.setTitle(titleField.getText().trim());
    customer.setCustomerEmail(emailField.getText().trim());
    customer.setCustomerBusinessPhone(businessPhoneField.getText().trim());
    customer.setCustomerCellPhone(cellPhoneField.getText().trim());
    customer.setStreet(streetField.getText().trim());
    customer.setCity(cityField.getText().trim());
    customer.setState((String) stateComboBox.getSelectedItem());
    customer.setZipCode(zipCodeField.getText().trim());
    customer.setCompany(companyField.getText().trim());
    customer.setWebsite(websiteField.getText().trim());
    
    // Handle credit limit
    try {
        double creditLimit = Double.parseDouble(customerCreditField.getText().trim());
        customer.setCustomerCredit(creditLimit);
    } catch (NumberFormatException e) {
        customer.setCustomerCredit(0.0);
    }
    
    customer.setRepID(Integer.parseInt(repIDField.getText().trim()));
    customer.setStatus((String) statusComboBox.getSelectedItem());
    customer.setNotes(notesArea.getText().trim());
    
    // Save or update the customer
    boolean success;
    if (isEditMode) {
        success = CustomerService.updateCustomer(customer);
    } else {
        success = CustomerService.addCustomer(customer);
    }
    
    if (success) {
        JOptionPane.showMessageDialog(mainPanel, 
            isEditMode ? "Customer updated successfully!" : "Customer saved successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        mainApp.showScreen("CustomerPresentation");
    } else {
        JOptionPane.showMessageDialog(mainPanel, 
            "Unable to " + (isEditMode ? "update" : "save") + " customer. Please check your data and try again.", 
            "Save Error", JOptionPane.ERROR_MESSAGE);
    }
}



    private JLayeredPane createTitlePanel() {
    JLayeredPane titlePanel = new JLayeredPane();
    titlePanel.setBackground(new Color(245, 222, 179));
    titlePanel.setOpaque(true);
    

    JLabel titleLabel = new JLabel(isEditMode ? 
        "Edit Customer: " + existingCustomer.getFirstName() + " " + existingCustomer.getLastName() 
        : "Create New Customer", JLabel.CENTER);
    titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
    titleLabel.setForeground(new Color(139, 69, 19));
    titleLabel.setBounds(100, 20, 600, 50);
    
    // Description Label
    JTextArea descriptionLabel = new JTextArea("Use this page to edit or create a new customer.");
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
    backButton.addActionListener(e -> mainApp.showScreen("CustomerPresentation"));
    
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

    private JTextField createNumericOnlyTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 25));
        field.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                if (str.matches("\\d*")) {
                    super.insertString(offs, str, a);
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
    firstNameField.setText(existingCustomer.getFirstName());
    lastNameField.setText(existingCustomer.getLastName());
    titleField.setText(existingCustomer.getTitle());
    emailField.setText(existingCustomer.getCustomerEmail());
    businessPhoneField.setText(existingCustomer.getCustomerBusinessPhone());
    cellPhoneField.setText(existingCustomer.getCustomerCellPhone());
    streetField.setText(existingCustomer.getStreet());
    cityField.setText(existingCustomer.getCity());
    stateComboBox.setSelectedItem(existingCustomer.getState());
    zipCodeField.setText(existingCustomer.getZipCode());
    companyField.setText(existingCustomer.getCompany());
    websiteField.setText(existingCustomer.getWebsite());
    customerCreditField.setText(String.valueOf(existingCustomer.getCustomerCredit()));
    repIDField.setText(String.valueOf(existingCustomer.getRepID()));
    statusComboBox.setSelectedItem(existingCustomer.getStatus());
    notesArea.setText(existingCustomer.getNotes());
}


}
