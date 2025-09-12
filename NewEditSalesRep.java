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
    private JFormattedTextField businessPhoneField;
    private JFormattedTextField cellPhoneField;
    private JFormattedTextField homePhoneField;
    private JFormattedTextField faxNumberField;
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
        mainPanel.add(UIFactory.createSaveCancelButtonPanel(this::handleSaveButton, () -> mainApp.showScreen("SalesRepPresentation")), BorderLayout.SOUTH);

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
        UIFactory.addSectionHeader(panel, "Personal Information", gbc);
        
        // First Name
        firstNameField = UIFactory.createRestrictedTextField(300, 30);
        UIFactory.addFormField(panel, "First Name:", firstNameField, gbc);
        
        // Last Name
        lastNameField = UIFactory.createRestrictedTextField(300, 30);
        UIFactory.addFormField(panel, "Last Name:", lastNameField, gbc);
        
        // Title
        titleField = UIFactory.createRestrictedTextField(300, 50);
        UIFactory.addFormField(panel, "Title:", titleField, gbc);

        // Contact Information Section
        UIFactory.addSectionHeader(panel, "Contact Information", gbc);
        
        businessPhoneField = UIFactory.createPhoneField(300);
        UIFactory.addFormField(panel, "Business Phone:", businessPhoneField, gbc);
        
        cellPhoneField = UIFactory.createPhoneField(300);
        UIFactory.addFormField(panel, "Cell Phone:", cellPhoneField, gbc);
        
        homePhoneField = UIFactory.createPhoneField(300);
        UIFactory.addFormField(panel, "Home Phone:", homePhoneField, gbc);
        
        faxNumberField = UIFactory.createPhoneField(300);
        UIFactory.addFormField(panel, "Fax Number:", faxNumberField, gbc);
        
        // Address fields
        streetField = UIFactory.createRestrictedTextField(300, 50);
        UIFactory.addFormField(panel, "Street:", streetField, gbc);
        
        cityField = UIFactory.createRestrictedTextField(300, 50);
        UIFactory.addFormField(panel, "City:", cityField, gbc);
        
        // State dropdown
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

        // Business Information Section
        UIFactory.addSectionHeader(panel, "Business Information", gbc);
        
        commissionField = UIFactory.createNumericTextField(300);
        UIFactory.addFormField(panel, "Commission Rate:", commissionField, gbc);
        
        List<SalesRep> allReps = SalesRepService.getAllSalesReps();
        List<Integer> managers = allReps.stream()
            .map(SalesRep::getRepID)
            .collect(Collectors.toList());
        Integer[] managerArray = managers.toArray(new Integer[0]);
        managerComboBox = new JComboBox<>(managerArray);
        managerComboBox.setPreferredSize(new Dimension(300, 25));
        UIFactory.addFormField(panel, "Manager:", managerComboBox, gbc);
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

private JLayeredPane createTitlePanel() {
        return UIFactory.createTitlePanel(mainApp, mainPanel,
            isEditMode ? "Edit Sales Rep: " + existingSalesRep.getFirstName() + " " + existingSalesRep.getLastName() : "Create New Sales Rep",
            "Use this page to edit or create a new sales rep.", "SalesRepPresentation");
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

}