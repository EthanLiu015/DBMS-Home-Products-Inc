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
    private JTextField zipCodeField, customerCreditField, repIDField, companyField, websiteField, emailField;
    private JFormattedTextField businessPhoneField, cellPhoneField;
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
        
        // Create scroll pane using the factory
        JScrollPane scrollPane = UIFactory.createThemedScrollPane(formPanel);
        
        // Add components to main panel
        mainPanel.add(UIFactory.createTitlePanel(mainApp, mainPanel, 
            isEditMode ? "Edit Customer: " + existingCustomer.getFirstName() + " " + existingCustomer.getLastName() : "Create New Customer", 
            "Use this page to edit or create a new customer.", "CustomerPresentation"), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(UIFactory.createSaveCancelButtonPanel(this::handleSaveButton, () -> mainApp.showScreen("CustomerPresentation")), BorderLayout.SOUTH);

        // Populate fields after all components are created
        if (isEditMode && existingCustomer != null) {
            SwingUtilities.invokeLater(this::populateFields);
        }
    }

    private void createFormFields(JPanel panel, GridBagConstraints gbc) {
        // Personal Information Section
        UIFactory.addSectionHeader(panel, "Personal Information", gbc);
        
        firstNameField = UIFactory.createRestrictedTextField(200, 30);
        UIFactory.addFormField(panel, "First Name:", firstNameField, gbc);
        
        lastNameField = UIFactory.createRestrictedTextField(200, 30);
        UIFactory.addFormField(panel, "Last Name:", lastNameField, gbc);
        
        titleField = UIFactory.createRestrictedTextField(200, 50);
        UIFactory.addFormField(panel, "Title:", titleField, gbc);
        
        // Contact Information Section
        UIFactory.addSectionHeader(panel, "Contact Information", gbc);
        
        streetField = UIFactory.createRestrictedTextField(300, 50);
        UIFactory.addFormField(panel, "Street:", streetField, gbc);
        
        cityField = UIFactory.createRestrictedTextField(300, 50);
        UIFactory.addFormField(panel, "City:", cityField, gbc);
        
        String[] states = {"", "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                          "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                          "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
                          "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
                          "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
        stateComboBox = new JComboBox<>(states);
        stateComboBox.setPreferredSize(new Dimension(300, 25));
        UIFactory.addFormField(panel, "State:", stateComboBox, gbc);
        
        zipCodeField = UIFactory.createNumericOnlyTextField(300, 5);
        UIFactory.addFormField(panel, "Zip Code:", zipCodeField, gbc);
        
        businessPhoneField = UIFactory.createPhoneField(300);
        UIFactory.addFormField(panel, "Business Phone:", businessPhoneField, gbc);
        
        cellPhoneField = UIFactory.createPhoneField(300);
        UIFactory.addFormField(panel, "Cell Phone:", cellPhoneField, gbc);


        emailField = UIFactory.createRestrictedTextField(300, 100);
        UIFactory.addFormField(panel, "Email:", emailField, gbc);
        
        // Business Information Section
        UIFactory.addSectionHeader(panel, "Business Information", gbc);
        
        companyField = UIFactory.createRestrictedTextField(300, 50);
        UIFactory.addFormField(panel, "Company:", companyField, gbc);
        
        websiteField = UIFactory.createRestrictedTextField(300, 200);
        UIFactory.addFormField(panel, "Website:", websiteField, gbc);

        repIDField = UIFactory.createNumericTextField(300);
        UIFactory.addFormField(panel, "Sales Representative ID:", repIDField, gbc);

        String[] statusOptions = {"", "Active", "Inactive"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setPreferredSize(new Dimension(300, 25));
        UIFactory.addFormField(panel, "Status:", statusComboBox, gbc);

        // Notes Section
        UIFactory.addSectionHeader(panel, "Additional Information", gbc);
        notesArea = UIFactory.createRestrictedTextArea(4, 40, 1000);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        notesScrollPane.setPreferredSize(new Dimension(300, 80));
        UIFactory.addFormField(panel, "Notes:", notesScrollPane, gbc);

        // Financial Information Section
        UIFactory.addSectionHeader(panel, "Financial Information", gbc);

        customerCreditField = UIFactory.createNumericTextField(100);
        UIFactory.addFormField(panel, "Credit Limit:", customerCreditField, gbc);

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
        String errorMessage;
        if (isEditMode) {
            errorMessage = CustomerService.updateCustomer(customer);
        } else {
            errorMessage = CustomerService.addCustomer(customer);
        }
        
        if (errorMessage == null) {
            JOptionPane.showMessageDialog(mainPanel, 
                isEditMode ? "Customer updated successfully!" : "Customer saved successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            mainApp.showScreen("CustomerPresentation");
        } else {
            JOptionPane.showMessageDialog(mainPanel, 
                errorMessage, 
                "Save Error", JOptionPane.ERROR_MESSAGE);
        }
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
        customerCreditField.setText(String.format("%.2f", existingCustomer.getCustomerCredit()));
        repIDField.setText(String.valueOf(existingCustomer.getRepID()));
        statusComboBox.setSelectedItem(existingCustomer.getStatus());
        notesArea.setText(existingCustomer.getNotes());
    }


}
