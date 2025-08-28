import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.util.List;

public class ViewAllCustomer {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private Customer customer;
    private JPanel contentPanel;

    public ViewAllCustomer(MainApplication mainApp, Customer customer) {
        this.mainApp = mainApp;
        this.customer = customer;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(new Color(245, 222, 179));

    // Add title panel at the top
    JLayeredPane titlePanel = createTitlePanel();
    mainPanel.add(titlePanel, BorderLayout.NORTH);

    // Create a panel for the content below the title
    JPanel contentContainer = new JPanel(new GridBagLayout());
    contentContainer.setBackground(new Color(245, 222, 179));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;

    // Add action panel
    gbc.gridy = 0;
    gbc.weighty = 0.0;
    JPanel actionPanel = createActionPanel();
    contentContainer.add(actionPanel, gbc);

    // Add customer details panel
    gbc.gridy = 1;
    gbc.weighty = 1.0;
    contentPanel = createCustomerDetailsPanel();
    contentContainer.add(contentPanel, gbc);

    mainPanel.add(contentContainer, BorderLayout.CENTER);
}




    private JLayeredPane createTitlePanel() {
    JLayeredPane titlePanel = new JLayeredPane();
    titlePanel.setPreferredSize(new Dimension(mainPanel.getWidth(), 150));
    titlePanel.setBackground(new Color(245, 222, 179));
    titlePanel.setOpaque(true);
    
    // Get the panel width for centering calculations
    int panelWidth = mainPanel.getWidth();
    int titleWidth = 600;
    int titleX = (panelWidth - titleWidth) / 2;
    
    String title = "Viewing " + customer.getFirstName() + " " + customer.getLastName();
    JLabel titleLabel = new JLabel(title, JLabel.CENTER);
    titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
    titleLabel.setForeground(new Color(139, 69, 19));
    titleLabel.setBounds(titleX, 20, titleWidth, 50);
    
    String description = "Use this form to view all information about " + customer.getFirstName() + " " + customer.getLastName();
    JTextArea descriptionLabel = new JTextArea(description);
    descriptionLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
    descriptionLabel.setForeground(new Color(139, 69, 19));
    descriptionLabel.setBackground(titlePanel.getBackground());
    descriptionLabel.setEditable(false);
    descriptionLabel.setBounds(titleX, 70, titleWidth, 30);
    
    JPanel exitButtonPanel = createExitButton();
    exitButtonPanel.setBounds(10, 10, 40, 40);
    
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


    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        actionPanel.setBackground(new Color(245, 222, 179));
        
        JButton editButton = createStyledButton("Edit");
        JButton orderHistoryButton = createStyledButton("Order History");
        JButton paymentHistoryButton = createStyledButton("Payment History");
        
        editButton.addActionListener(e -> handleEdit());
        orderHistoryButton.addActionListener(e -> showOrderHistory());
        paymentHistoryButton.addActionListener(e -> showPaymentHistory());
        
        actionPanel.add(editButton);
        actionPanel.add(orderHistoryButton);
        actionPanel.add(paymentHistoryButton);

        actionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 150, 120)));
        
        return actionPanel;
    }

    private JPanel createCustomerDetailsPanel() {
    JPanel containerPanel = new JPanel(new BorderLayout());
    containerPanel.setBackground(new Color(245, 222, 179));
    
    JPanel detailsPanel = new JPanel(new GridBagLayout());
    detailsPanel.setBackground(new Color(245, 222, 179));
    
    // Add sections in the original order
    addSection(detailsPanel, "Personal Information", createPersonalInfoPanel());
    addSection(detailsPanel, "Contact Information", createContactInfoPanel());
    addSection(detailsPanel, "Business Information", createBusinessInfoPanel());
    addSection(detailsPanel, "Additional Information", createAdditionalInfoPanel());
    addSection(detailsPanel, "Financial Summary", createFinancialSummaryPanel());
    
    JScrollPane scrollPane = new JScrollPane(
        detailsPanel,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    SwingUtilities.invokeLater(() -> {
    scrollPane.getVerticalScrollBar().setValue(0);
});
    
    // Thinner scrollbar
    scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
    
    JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
    verticalBar.setUI(new BasicScrollBarUI() {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(139, 69, 19);
            this.trackColor = new Color(222, 184, 135);
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }
    });
    
    containerPanel.add(scrollPane, BorderLayout.CENTER);
    return containerPanel;
}




    private void addSection(JPanel parent, String title, JPanel content) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 5, 10);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        titleLabel.setForeground(new Color(139, 69, 19));
        parent.add(titleLabel, gbc);
        
        content.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19)));
        gbc.insets = new Insets(0, 10, 20, 10);
        parent.add(content, gbc);
    }


   private void addField(JPanel panel, String label, String value) {
    // Create a single line panel for each field
    JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    fieldPanel.setBackground(Color.WHITE);
    
    // Create label
    JLabel labelComponent = new JLabel(label);
    labelComponent.setFont(new Font("Roboto", Font.BOLD, 14));
    
    // Create text area with wider width before wrapping
    JTextArea valueComponent = new JTextArea();
    valueComponent.setText(value);
    valueComponent.setFont(new Font("Roboto", Font.PLAIN, 14));
    valueComponent.setLineWrap(true);
    valueComponent.setWrapStyleWord(true);
    valueComponent.setEditable(false);
    valueComponent.setBackground(Color.WHITE);
    valueComponent.setMargin(new Insets(5, 5, 5, 5));
    
    // Set wider width for value component
    int textWidth = valueComponent.getFontMetrics(valueComponent.getFont())
                                .stringWidth(value) + 20; // Add padding
    int preferredWidth = Math.min(700, Math.max(200, textWidth)); // Min 200, Max 700
    
    // Calculate height based on content
    valueComponent.setSize(preferredWidth, Short.MAX_VALUE);
    int preferredHeight = valueComponent.getPreferredSize().height;
    
    valueComponent.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    
    // Add components to the field panel
    fieldPanel.add(labelComponent);
    fieldPanel.add(valueComponent);
    
    // Set panel height to match content
    fieldPanel.setPreferredSize(new Dimension(800, preferredHeight + 20));
    
    // Add the field panel to the main panel
    panel.add(fieldPanel);
}


private JPanel createPersonalInfoPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    addField(panel, "Customer ID: ", String.valueOf(customer.getCustomerID()));
    addField(panel, "First Name: ", customer.getFirstName());
    addField(panel, "Last Name: ", customer.getLastName());
    addField(panel, "Title: ", customer.getTitle());
    
    return panel;
}


private JPanel createContactInfoPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    addField(panel, "Street:", customer.getStreet());
    addField(panel, "City:", customer.getCity());
    addField(panel, "State:", customer.getState());
    addField(panel, "Zip Code:", customer.getZipCode());
    addField(panel, "Business Phone:", customer.getCustomerBusinessPhone());
    addField(panel, "Cell Phone:", customer.getCustomerCellPhone());
    addField(panel, "Email:", customer.getCustomerEmail());
    
    return panel;
}

private JPanel createBusinessInfoPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    addField(panel, "Company:", customer.getCompany());
    addField(panel, "Website:", customer.getWebsite());
    addField(panel, "Sales Rep ID:", String.valueOf(customer.getRepID()));
    addField(panel, "Status:", customer.getStatus());
    
    return panel;
}

private JPanel createAdditionalInfoPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    addField(panel, "Notes:", customer.getNotes());
    
    return panel;
}

private JPanel createFinancialSummaryPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    double creditLimit = customer.getCustomerCredit();
    double totalOrders = calculateTotalOrders();
    double remainingCredit = creditLimit - totalOrders;
    
    addField(panel, "Credit Limit:", String.format("$%.2f", creditLimit));
    addField(panel, "Total Lifetime Orders:", String.format("$%.2f", totalOrders));
    addField(panel, "Remaining Credit:", String.format("$%.2f", remainingCredit));
    
    return panel;
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

    private double calculateTotalOrders() {
        return CustomerService.calculateCustomerTotalOrders(customer.getCustomerID());
    }

    private void handleEdit() {
        mainApp.showEditCustomer(customer);
    }


    private void showOrderHistory() {
    List<Order> orderHistory = CustomerService.getCustomerOrderHistory(customer.getCustomerID());

    JDialog dialog = new JDialog();
    dialog.setTitle("Order History - " + customer.getFirstName() + " " + customer.getLastName());
    dialog.setModal(true);
    dialog.setSize(800, 600);
    dialog.setLocationRelativeTo(mainPanel);

    String[] columnNames = {"Order ID", "Date", "Status", "Total Amount"};
    Object[][] data = new Object[orderHistory.size()][4];

    for (int i = 0; i < orderHistory.size(); i++) {
        Order order = orderHistory.get(i);
        data[i][0] = order.getOrderID();
        data[i][1] = order.getOrderDate();
        data[i][2] = order.getStatus();
        data[i][3] = String.format("$%.2f", CustomerService.calculateTotal(order.getOrderID()));
    }

    JTable table = new JTable(data, columnNames);
    JScrollPane scrollPane = new JScrollPane(table);
    dialog.add(scrollPane);

    dialog.setVisible(true);
}

private void showPaymentHistory() {
    List<Payment> paymentHistory = CustomerService.getCustomerPaymentHistory(customer.getCustomerID());

    JDialog dialog = new JDialog();
    dialog.setTitle("Payment History - " + customer.getFirstName() + " " + customer.getLastName());
    dialog.setModal(true);
    dialog.setSize(800, 600);
    dialog.setLocationRelativeTo(mainPanel);

    String[] columnNames = {"Payment ID", "Date", "Amount", "Method"};
    Object[][] data = new Object[paymentHistory.size()][4];

    for (int i = 0; i < paymentHistory.size(); i++) {
        Payment payment = paymentHistory.get(i);
        data[i][0] = payment.getPaymentID();
        data[i][1] = payment.getPaymentDate();
        data[i][2] = String.format("$%.2f", payment.getAmount());
        data[i][3] = payment.getMethod();
    }

    JTable table = new JTable(data, columnNames);
    JScrollPane scrollPane = new JScrollPane(table);
    dialog.add(scrollPane);

    dialog.setVisible(true);
}

    public JPanel getMainPanel() {
        return mainPanel;
    }
}