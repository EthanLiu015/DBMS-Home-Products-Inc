import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class SalesRepPresentation {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private List<SalesRep> salesReps;
    private JTable salesRepTable;

    public SalesRepPresentation(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.salesReps = SalesRepService.getAllSalesReps();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 222, 179));

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);

        // Content panel to hold actions and table
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 222, 179));

        JPanel actionPanel = createActionPanel();
        contentPanel.add(actionPanel, BorderLayout.NORTH);

        JPanel salesRepPanel = createSalesRepPanel();
        contentPanel.add(salesRepPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
}

        private JLayeredPane createTitlePanel() {
            return UIFactory.createTitlePanel(mainApp, mainPanel, "Sales Reps", 
                "Use this page to view, edit, or create Sales Reps.", "MainMenu");
        }
    
    private JPanel createActionPanel() {
        return UIFactory.createPresentationActionPanel("Create New", () -> mainApp.showNewSalesRep());
    }

private JPanel createSalesRepPanel() {
    JPanel salesRepPanel = new JPanel(new BorderLayout());
    salesRepPanel.setBackground(new Color(245, 222, 179));

    salesRepTable = createSalesRepTable();
    JScrollPane scrollPane = UIFactory.createThemedScrollPane(salesRepTable);
    
    salesRepPanel.add(scrollPane, BorderLayout.CENTER);
    return salesRepPanel;
}

public JPanel getMainPanel() {
    return mainPanel;
}

private JTable createSalesRepTable() {
    String[] columnNames = {"Sales Rep ID", "Full Name", "Business Number", "Cell Number", "Actions"};
    Object[][] data = new Object[salesReps.size()][5];
    
    for (int i = 0; i < salesReps.size(); i++) {
        SalesRep rep = salesReps.get(i);
        data[i][0] = rep.getRepID();
        data[i][1] = rep.getFirstName() + " " + rep.getLastName();
        data[i][2] = rep.getBusinessPhone();
        data[i][3] = rep.getCellPhone();
        data[i][4] = "View";
    }
    
    DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 4;
        }
    };
    
    JTable table = new JTable(model);
    UIFactory.styleTable(table);
    
    // Set custom column widths
    table.getColumnModel().getColumn(0).setPreferredWidth(100);
    table.getColumnModel().getColumn(0).setMaxWidth(100);
    
    table.getColumn("Actions").setCellRenderer(new UIFactory.TableButtonRenderer("View"));
    table.getColumn("Actions").setCellEditor(new UIFactory.TableButtonEditor("View", 
        row -> mainApp.showSalesRepDetails(salesReps.get(row))));
    
    return table;
}

    public void refreshSalesRepTable() {
        this.salesReps = SalesRepService.getAllSalesReps();
        
        DefaultTableModel model = (DefaultTableModel) salesRepTable.getModel();
        model.setRowCount(0);
        
        for (SalesRep rep : salesReps) {
            model.addRow(new Object[]{
                rep.getRepID(),
                rep.getFirstName() + " " + rep.getLastName(),
                rep.getBusinessPhone(),
                rep.getCellPhone(),
                "View"
            });
        }
    }

}