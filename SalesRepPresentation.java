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

    public SalesRepPresentation(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.salesReps = SalesRepService.getAllSalesReps();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
    mainPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;

    // Add title panel
    gbc.gridy = 0;
    gbc.weighty = 0.0;
    JLayeredPane titlePanel = createTitlePanel();
    mainPanel.add(titlePanel, gbc);

    // Add action panel
    gbc.gridy = 1;
    gbc.weighty = 0.0;
    JPanel actionPanel = createActionPanel();
    mainPanel.add(actionPanel, gbc);

    // Add sales rep panel
    gbc.gridy = 2;
    gbc.weighty = 1.0;
    JPanel salesRepPanel = createSalesRepPanel();
    mainPanel.add(salesRepPanel, gbc);
}

        private JLayeredPane createTitlePanel() {
    JLayeredPane titlePanel = new JLayeredPane();
    titlePanel.setBackground(new Color(245, 222, 179));
    titlePanel.setOpaque(true);
    
    // Title Label
    JLabel titleLabel = new JLabel("Sales Reps", JLabel.CENTER);
    titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
    titleLabel.setForeground(new Color(139, 69, 19));
    titleLabel.setBounds(100, 20, 600, 50);
    
    // Description Label
    JTextArea descriptionLabel = new JTextArea("Use this page to view, edit, or create Sales Reps.");
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
    
    private JPanel createActionPanel() {
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
    actionPanel.setBackground(new Color(245, 222, 179));
    actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    
    JButton createNewButton = createStyledButton("Create New");
    createNewButton.addActionListener(e -> {
        mainApp.showNewSalesRep();
    });
    
    // Set preferred size based on panel dimensions
    Dimension panelSize = mainPanel.getSize();
    actionPanel.setPreferredSize(new Dimension(panelSize.width, 53));
    
    actionPanel.add(createNewButton);
    
    // Add resize listener to maintain proportions
    mainPanel.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            Dimension newPanelSize = mainPanel.getSize();
            actionPanel.setPreferredSize(new Dimension(newPanelSize.width, 53));
            actionPanel.revalidate();
        }
    });
    
    return actionPanel;
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

private JPanel createSalesRepPanel() {
    JPanel salesRepPanel = new JPanel(new BorderLayout());
    salesRepPanel.setBackground(new Color(245, 222, 179));
    
    JTable table = createSalesRepTable();
    
    // Create a custom viewport with matching background
    JViewport viewport = new JViewport() {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(245, 222, 179));
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    };
    viewport.setView(table);
    
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewport(viewport);
    scrollPane.setBackground(new Color(245, 222, 179));
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.getViewport().setBackground(new Color(245, 222, 179));
    
    // Style the scrollbar
    JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
    verticalBar.setBackground(new Color(245, 222, 179));
    verticalBar.setUI(new BasicScrollBarUI() {
        protected Color thumbColor = new Color(139, 69, 19);
        protected Color trackColor = new Color(222, 184, 135);
        
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(139, 69, 19);
            this.trackColor = new Color(222, 184, 135);
        }
        
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    });
    
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
    table.setFont(new Font("Roboto", Font.PLAIN, 14));
    table.setRowHeight(30);
    table.setBackground(new Color(245, 222, 179));
    table.setForeground(new Color(139, 69, 19));
    table.setGridColor(new Color(222, 184, 135));
    table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
    table.getTableHeader().setBackground(new Color(222, 184, 135));
    table.getTableHeader().setForeground(new Color(139, 69, 19));
    
    // Set custom column widths
    table.getColumnModel().getColumn(0).setPreferredWidth(100);
    table.getColumnModel().getColumn(0).setMaxWidth(100);
    table.getColumnModel().getColumn(1).setPreferredWidth(200);
    table.getColumnModel().getColumn(2).setPreferredWidth(150);
    table.getColumnModel().getColumn(3).setPreferredWidth(200);
    table.getColumnModel().getColumn(4).setPreferredWidth(100);
    
    // Add the button column
    ButtonRenderer buttonRenderer = new ButtonRenderer();
    buttonRenderer.setForeground(Color.WHITE);
    buttonRenderer.setBackground(new Color(139, 69, 19));
    table.getColumn("Actions").setCellRenderer(buttonRenderer);
    table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), mainApp, salesReps));
    
    return table;
}

    public void refreshSalesRepTable() {
    this.salesReps = SalesRepService.getAllSalesReps();
    
    JTable table = (JTable)((JScrollPane)((JPanel)mainPanel.getComponent(2)).getComponent(0)).getViewport().getView();
    DefaultTableModel model = (DefaultTableModel) table.getModel();
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

    ButtonEditor editor = (ButtonEditor) table.getColumnModel().getColumn(4).getCellEditor();
    editor.updateSalesReps(salesReps);
}

    class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setBackground(new Color(222, 174, 111));
        setForeground(Color.WHITE);
        setFont(new Font("Roboto", Font.BOLD, 14));
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText("View");
        if (isSelected) {
            setBackground(new Color(190, 140, 80));
        } else {
            setBackground(new Color(222, 174, 111));
        }
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private MainApplication mainApp;
    private List<SalesRep> salesReps;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, MainApplication mainApp, List<SalesRep> salesReps) {
        super(checkBox);
        this.mainApp = mainApp;
        this.salesReps = salesReps;
        button = new JButton();
        button.setOpaque(true);
        button.setBackground(new Color(222, 174, 111));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(190, 140, 80));
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(222, 174, 111));
                button.repaint();
            }
        });
        
        button.addActionListener(e -> fireEditingStopped());
    }

    public void updateSalesReps(List<SalesRep> newSalesReps) {
        this.salesReps = newSalesReps;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        label = "View";
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.getSelectedRow();
            SalesRep selectedRep = salesReps.get(row);
            mainApp.showSalesRepDetails(selectedRep); 
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
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

class StyledButtonRenderer extends JButton implements TableCellRenderer {
    private boolean isHovered = false;
    private int hoveredRow = -1;
    private int hoveredColumn = -1;

    public StyledButtonRenderer() {
        setOpaque(true);
        setFont(new Font("Roboto", Font.PLAIN, 14));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText("View");
        isHovered = (row == hoveredRow && column == hoveredColumn);
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isHovered) {
            g2.setColor(new Color(170, 120, 60)); // Darker shade when hovered
        } else {
            g2.setColor(new Color(222, 174, 111)); // Normal color
        }

        g2.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw border
        g2.setColor(new Color(139, 69, 19));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // Draw text
        FontMetrics metrics = g2.getFontMetrics(getFont());
        int x = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), x, y);
    }

    public void setHovered(int row, int column) {
        this.hoveredRow = row;
        this.hoveredColumn = column;
    }
}

}