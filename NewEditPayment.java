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
        
        // Create scroll pane using the factory
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(centeringPanel);
        
        // Add components to main panel
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(UIFactory.createSaveCancelButtonPanel(this::handleSaveButton, () -> mainApp.showScreen("MainMenu")), BorderLayout.SOUTH);
        
        // Add listeners
        setupListeners();
    }
    
    private void addCustomerSection(JPanel panel, GridBagConstraints gbc) {
        UIFactory.addSectionHeader(panel, "Customer Information", gbc);
        
        customerIDField = UIFactory.createNumericTextField(300);
        UIFactory.addFormField(panel, "Customer ID:", customerIDField, gbc);
        
        orderComboBox = new JComboBox<>();
        orderComboBox.setPreferredSize(new Dimension(300, 25));
        UIFactory.addFormField(panel, "Order:", orderComboBox, gbc);
    }
    
    private void addPaymentDetailsSection(JPanel panel, GridBagConstraints gbc) {
        UIFactory.addSectionHeader(panel, "Payment Details", gbc);
        
        // Payment Date
        paymentDateSpinner = UIFactory.createDatePicker("MM/dd/yyyy", 300);
        UIFactory.addFormField(panel, "Payment Date:", paymentDateSpinner, gbc);
        
        // Amount
        amountField = UIFactory.createNumericTextField(300);
        UIFactory.addFormField(panel, "Amount:", amountField, gbc);
        
        // Payment Method
        methodComboBox = new JComboBox<>(PAYMENT_METHODS);
        UIFactory.addFormField(panel, "Payment Method:", methodComboBox, gbc);
    }
    
    private void addCreditCardSection(JPanel panel, GridBagConstraints gbc) {
        UIFactory.addSectionHeader(panel, "Credit Card Information", gbc);
        
        creditCardCheckBox = new JCheckBox("Pay by Credit Card");
        UIFactory.addFormField(panel, "", creditCardCheckBox, gbc);
        
        cardHolderField = UIFactory.createRestrictedTextField(300, 100);
        cardHolderField.setEnabled(false);
        UIFactory.addFormField(panel, "Card Holder:", cardHolderField, gbc);
        
        cardNumberField = UIFactory.createNumericOnlyTextField(300);
        cardNumberField.setEnabled(false);
        UIFactory.addFormField(panel, "Card Number:", cardNumberField, gbc);
        
        // Expiration Date
        SpinnerDateModel expirationDateModel = new SpinnerDateModel(new Date(), null, null, Calendar.MONTH);
        expirationDateSpinner = new JSpinner(expirationDateModel);
        JSpinner.DateEditor expirationDateEditor = new JSpinner.DateEditor(expirationDateSpinner, "MM/yyyy");
        expirationDateSpinner.setEditor(expirationDateEditor);
        expirationDateSpinner.setEnabled(false);
        UIFactory.addFormField(panel, "Expiration Date:", expirationDateSpinner, gbc);
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
        return UIFactory.createTitlePanel(mainApp, mainPanel, "Create New Payment", "Use this page to create a new Payment.", "MainMenu");
    }

private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(new Color(245, 222, 179));

        JButton saveButton = UIFactory.createStyledButton("Save");
        JButton cancelButton = UIFactory.createStyledButton("Cancel");

        saveButton.addActionListener(e -> handleSaveButton());
        cancelButton.addActionListener(e -> mainApp.showScreen("MainMenu"));

        panel.add(saveButton);
        panel.add(cancelButton);
        return panel;
    }

}
