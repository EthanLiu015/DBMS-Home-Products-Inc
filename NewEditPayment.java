import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Calendar;
import java.sql.SQLException;
import java.util.List;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;


public class NewEditPayment {
    private MainApplication mainApp;
    private Payment payment;
    private JPanel mainPanel;
    
    // Payment Form Fields
    private JTextField customerIDField;
    private JComboBox<String> orderComboBox;
    private JSpinner paymentDateSpinner;
    private JTextField amountField;
    private JComboBox<String> methodComboBox;
    private JTextField cardHolderField;
    private JTextField cardNumberField;
    private JSpinner expirationDateSpinner;
    private JCheckBox creditCardCheckBox;
    
    // Constants
    private static final String[] PAYMENT_METHODS = {
        "Credit Card", "Cash", "Check", "Bank Transfer"
    };
    
    public NewEditPayment(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.payment = new Payment();
        createAndShowGUI();
        customerIDField.setText("1"); // Set a default customer ID
        updateOrderComboBox();
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
        addPaymentDetailsSection(formPanel, gbc);
        addCreditCardSection(formPanel, gbc);
        
        // Center the form panel
        JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centeringPanel.setBackground(new Color(245, 222, 179));
        centeringPanel.add(formPanel);
        
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(centeringPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(245, 222, 179));
        
        // Add components to main panel
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        // Add listeners
        setupListeners();
    }
    
    private void addCustomerSection(JPanel panel, GridBagConstraints gbc) {
        addSectionHeader(panel, "Customer Information", gbc);
        
        customerIDField = createNumericTextField(300);
        addFormField(panel, "Customer ID:", customerIDField, gbc);
        
        orderComboBox = new JComboBox<>();
        orderComboBox.setPreferredSize(new Dimension(300, 25));
        addFormField(panel, "Order:", orderComboBox, gbc);
    }
    
    private void addPaymentDetailsSection(JPanel panel, GridBagConstraints gbc) {
        addSectionHeader(panel, "Payment Details", gbc);
        
        // Payment Date
        SpinnerDateModel paymentDateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        paymentDateSpinner = new JSpinner(paymentDateModel);
        JSpinner.DateEditor paymentDateEditor = new JSpinner.DateEditor(paymentDateSpinner, "MM/dd/yyyy");
        paymentDateSpinner.setEditor(paymentDateEditor);
        addFormField(panel, "Payment Date:", paymentDateSpinner, gbc);
        
        // Amount
        amountField = createNumericTextField(300);
        addFormField(panel, "Amount:", amountField, gbc);
        
        // Payment Method
        methodComboBox = new JComboBox<>(PAYMENT_METHODS);
        addFormField(panel, "Payment Method:", methodComboBox, gbc);
    }
    
    private void addCreditCardSection(JPanel panel, GridBagConstraints gbc) {
        addSectionHeader(panel, "Credit Card Information", gbc);
        
        creditCardCheckBox = new JCheckBox("Pay by Credit Card");
        addFormField(panel, "", creditCardCheckBox, gbc);
        
        cardHolderField = new JTextField();
        cardHolderField.setEnabled(false);
        addFormField(panel, "Card Holder:", cardHolderField, gbc);
        
        cardNumberField = createRestrictedTextField(16);
        cardNumberField.setEnabled(false);
        addFormField(panel, "Card Number:", cardNumberField, gbc);
        
        // Expiration Date
        SpinnerDateModel expirationDateModel = new SpinnerDateModel(new Date(), null, null, Calendar.MONTH);
        expirationDateSpinner = new JSpinner(expirationDateModel);
        JSpinner.DateEditor expirationDateEditor = new JSpinner.DateEditor(expirationDateSpinner, "MM/yyyy");
        expirationDateSpinner.setEditor(expirationDateEditor);
        expirationDateSpinner.setEnabled(false);
        addFormField(panel, "Expiration Date:", expirationDateSpinner, gbc);
    }
    
    private void setupListeners() {
        // Customer ID change listener
        customerIDField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateOrderComboBox(); }
            public void removeUpdate(DocumentEvent e) { updateOrderComboBox(); }
            public void insertUpdate(DocumentEvent e) { updateOrderComboBox(); }
        });
        
        // Credit Card checkbox listener
        creditCardCheckBox.addActionListener(e -> {
            boolean isSelected = creditCardCheckBox.isSelected();
            cardHolderField.setEnabled(isSelected);
            cardNumberField.setEnabled(isSelected);
            expirationDateSpinner.setEnabled(isSelected);
            if (isSelected) {
                methodComboBox.setSelectedItem("Credit Card");
            }
        });
        
        // Payment Method combo box listener
        methodComboBox.addActionListener(e -> {
            if (!methodComboBox.getSelectedItem().equals("Credit Card")) {
                creditCardCheckBox.setSelected(false);
            }
        });
    }

    private void handleSaveButton() {
    if (!validateForm()) {
        JOptionPane.showMessageDialog(mainPanel, 
            "Please fill in all required fields correctly.", 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Set payment details
        payment.setCustomerID(Integer.parseInt(customerIDField.getText()));
        payment.setOrderID(getSelectedOrderID());
        payment.setPaymentDate(new java.sql.Date(((Date)paymentDateSpinner.getValue()).getTime()));
        payment.setAmount(Double.parseDouble(amountField.getText()));
        payment.setMethod((String)methodComboBox.getSelectedItem());
        
        if (creditCardCheckBox.isSelected()) {
            payment.setCardHolder(cardHolderField.getText());
            payment.setCardNumber(cardNumberField.getText());
            payment.setExpirationDate(new java.sql.Date(((Date)expirationDateSpinner.getValue()).getTime()));
            payment.setCreditCard(true);
        }

        if (PaymentService.addPayment(payment)) {
            JOptionPane.showMessageDialog(mainPanel, 
                "Payment processed successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            mainApp.showScreen("MainMenu");
        } else {
            JOptionPane.showMessageDialog(mainPanel, 
                "Unable to process payment. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(mainPanel, 
            "Please ensure all numeric fields contain valid numbers.", 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}

private boolean validateForm() {
    if (customerIDField.getText().trim().isEmpty() ||
        orderComboBox.getSelectedItem() == null ||
        paymentDateSpinner.getValue() == null ||
        amountField.getText().trim().isEmpty() ||
        methodComboBox.getSelectedItem() == null) {
        return false;
    }

    if (creditCardCheckBox.isSelected()) {
        if (cardHolderField.getText().trim().isEmpty() ||
            cardNumberField.getText().trim().isEmpty() ||
            expirationDateSpinner.getValue() == null) {
            return false;
        }
        // Validate card number format
        if (!cardNumberField.getText().matches("\\d{16}")) {
            return false;
        }
    }

    try {
        double amount = Double.parseDouble(amountField.getText());
        return amount > 0;
    } catch (NumberFormatException e) {
        return false;
    }
}

private void updateOrderComboBox() {
    orderComboBox.removeAllItems();
    try {
        int customerId = Integer.parseInt(customerIDField.getText().trim());
        List<Order> orders = PaymentService.getUnpaidOrders(customerId);
        for (Order order : orders) {
            String item = String.format("Order #%d - $%.2f", 
                order.getOrderID(), order.calculateTotal());
            orderComboBox.addItem(item);
        }
    } catch (NumberFormatException ignored) {
    }
}

private int getSelectedOrderID() {
    String selected = (String)orderComboBox.getSelectedItem();
    if (selected != null) {
        return Integer.parseInt(selected.split("#")[1].split(" ")[0]);
    }
    return -1;
}

// Add the remaining helper methods from NewEditOrder:
private JLayeredPane createTitlePanel() {
    JLayeredPane titlePanel = new JLayeredPane();
    titlePanel.setBackground(new Color(245, 222, 179));
    titlePanel.setOpaque(true);
    

    JLabel titleLabel = new JLabel("Create New Payment", JLabel.CENTER);
    titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
    titleLabel.setForeground(new Color(139, 69, 19));
    titleLabel.setBounds(100, 20, 600, 50);
    
    // Description Label
    JTextArea descriptionLabel = new JTextArea("Use this page to create a new Payment.");
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
    backButton.addActionListener(e -> mainApp.showScreen("MainMenu"));
    
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

private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(new Color(245, 222, 179));

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        saveButton.addActionListener(e -> handleSaveButton());
        cancelButton.addActionListener(e -> mainApp.showScreen("MainMenu"));

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

}
