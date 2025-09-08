import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ViewAllSalesRep {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private SalesRep salesRep;
    private JPanel contentPanel;

    public ViewAllSalesRep(MainApplication mainApp, SalesRep salesRep) {
        this.mainApp = mainApp;
        this.salesRep = salesRep;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 222, 179));

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);

        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.weighty = 0.0;
        JPanel actionPanel = createActionPanel();
        contentContainer.add(actionPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        contentPanel = createSalesRepDetailsPanel();
        contentContainer.add(contentPanel, gbc);

        mainPanel.add(contentContainer, BorderLayout.CENTER);
    }

    private JLayeredPane createTitlePanel() {
        String title = "Viewing " + salesRep.getFirstName() + " " + salesRep.getLastName();
        String description = "Use this form to view all information about " + salesRep.getFirstName() + " " + salesRep.getLastName();
        return UIFactory.createTitlePanel(mainApp, mainPanel, title, description, "SalesRepPresentation");
    }


    private JPanel createActionPanel() {
        JPanel actionPanel = UIFactory.createActionPanel();
        
        JButton editButton = UIFactory.createStyledButton("Edit");
        editButton.addActionListener(e -> handleEdit());
        actionPanel.add(editButton);
        
        return actionPanel;
    }



    private JPanel createSalesRepDetailsPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(245, 222, 179));
        
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(new Color(245, 222, 179));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = -1; // Start before the first section

        UIFactory.addViewSection(detailsPanel, "Personal Information", createPersonalInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Contact Information", createContactInfoPanel(), gbc);
        UIFactory.addViewSection(detailsPanel, "Business Information", createBusinessInfoPanel(), gbc);

        JScrollPane scrollPane = UIFactory.createThemedScrollPane(detailsPanel);
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        return containerPanel;
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Sales Rep ID: ", String.valueOf(salesRep.getRepID())));
        panel.add(UIFactory.createInfoField("First Name: ", salesRep.getFirstName()));
        panel.add(UIFactory.createInfoField("Last Name: ", salesRep.getLastName()));
        panel.add(UIFactory.createInfoField("Title: ", salesRep.getTitle()));
        
        return panel;
    }

private JPanel createContactInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        panel.add(UIFactory.createInfoField("Business Phone:", salesRep.getBusinessPhone()));
        panel.add(UIFactory.createInfoField("Cell Phone:", salesRep.getCellPhone()));
        panel.add(UIFactory.createInfoField("Home Phone:", salesRep.getHomePhone()));
        panel.add(UIFactory.createInfoField("Fax Number:", salesRep.getFaxNumber()));
        panel.add(UIFactory.createInfoField("Street:", salesRep.getStreet()));
        panel.add(UIFactory.createInfoField("City:", salesRep.getCity()));
        panel.add(UIFactory.createInfoField("State:", salesRep.getState()));
        panel.add(UIFactory.createInfoField("Zip Code:", salesRep.getZipCode()));
        
        return panel;
    }

private JPanel createBusinessInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        double commissionPercentage = salesRep.getCommission() * 100;
        panel.add(UIFactory.createInfoField("Commission Rate:", String.format("%.1f%%", commissionPercentage)));
        panel.add(UIFactory.createInfoField("Manager ID:", String.valueOf(salesRep.getManager())));
        
        return panel;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

private void handleEdit() {
        mainApp.showEditSalesRep(salesRep);
    }
}
