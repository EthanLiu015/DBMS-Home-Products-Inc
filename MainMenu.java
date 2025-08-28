import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.ArrayList;

public class MainMenu {
    private MainApplication mainApp;
    private JPanel mainPanel;
    private JPopupMenu helpPopup;

    public MainMenu(MainApplication mainApp) {
        this.mainApp = mainApp;
        createAndShowGUI();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    public void createAndShowGUI() {
        mainPanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        JLayeredPane titlePanel = createTitlePanel();
        contentPanel.add(titlePanel, BorderLayout.NORTH);

        JLabel backgroundLabel = createBackgroundLabel();
        contentPanel.add(backgroundLabel, BorderLayout.CENTER);

        mainPanel.add(contentPanel);
    }

private JLayeredPane createTitlePanel() {
    JLayeredPane titlePanel = new JLayeredPane();
    titlePanel.setBackground(new Color(245, 222, 179));
    titlePanel.setOpaque(true);

    JLabel titleLabel = new JLabel("Home Products Inc.", JLabel.CENTER);
    titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
    titleLabel.setForeground(new Color(139, 69, 19));
    titleLabel.setBounds(100, 20, 600, 50);

    JTextArea descriptionLabel = new JTextArea("Welcome to the Main Menu. Please use this page to navigate through the database.");
    descriptionLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
    descriptionLabel.setForeground(new Color(139, 69, 19));
    descriptionLabel.setWrapStyleWord(false);
    descriptionLabel.setLineWrap(false);
    descriptionLabel.setEditable(false);
    descriptionLabel.setBackground(titlePanel.getBackground());

    JPanel exitButtonPanel = createExitButton();
    exitButtonPanel.setBounds(10, 10, 40, 40);

    JLabel logoLabel = new JLabel();
    logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    logoLabel.setBounds(700, 10, 120, 90);

    titlePanel.add(exitButtonPanel, JLayeredPane.DEFAULT_LAYER);
    titlePanel.add(titleLabel, JLayeredPane.DEFAULT_LAYER);
    titlePanel.add(descriptionLabel, JLayeredPane.DEFAULT_LAYER);
    titlePanel.add(logoLabel, JLayeredPane.PALETTE_LAYER);

    mainPanel.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            Dimension panelSize = mainPanel.getSize();
            int titleHeight = Math.max(panelSize.height / 8, 150);
            titlePanel.setPreferredSize(new Dimension(panelSize.width, titleHeight));

            int logoHeight = titleHeight * 3 / 4;
            int logoWidth = logoHeight * 4 / 3;
            ImageIcon logoIcon = new ImageIcon("Logo.jpg");
            Image scaledLogo = logoIcon.getImage().getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
            logoLabel.setBounds(panelSize.width - logoWidth - 20, (titleHeight - logoHeight) / 2, logoWidth, logoHeight);

            int titleLabelWidth = panelSize.width - logoWidth - 100;
            int newFontSize = Math.max(36, panelSize.width / 30);
            titleLabel.setFont(new Font("Futura", Font.BOLD, newFontSize));
            titleLabel.setBounds((panelSize.width - titleLabelWidth) / 2, 20, titleLabelWidth, 50);

            int descriptionFontSize = Math.max(20, panelSize.width / 80);
            descriptionLabel.setFont(new Font("Roboto", Font.PLAIN, descriptionFontSize));
            descriptionLabel.setBounds(
                (panelSize.width - 800) / 2,
                titleLabel.getY() + titleLabel.getHeight() + 25,
                800,
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

    private JLabel createBackgroundLabel() {
    JLabel backgroundLabel = new JLabel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon backgroundIcon = new ImageIcon("MainMenuFormBackground.jpg");
            Image backgroundImage = backgroundIcon.getImage();
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    };
    
    backgroundLabel.setLayout(new GridBagLayout());
    
    // Button panel - centered both vertically and horizontally
    JPanel buttonPanel = createButtonPanel();
    GridBagConstraints buttonGbc = new GridBagConstraints();
    buttonGbc.gridx = 0;
    buttonGbc.gridy = 0;
    buttonGbc.gridwidth = 2;
    buttonGbc.anchor = GridBagConstraints.CENTER;
    buttonGbc.weightx = 1.0;
    buttonGbc.weighty = 1.0;
    backgroundLabel.add(buttonPanel, buttonGbc);
    
    // Create help popup before creating help button
    createHelpPopup();
    
    // Help button (bottom left)
    JButton helpButton = createButton("Help");
    helpButton.setPreferredSize(new Dimension(150, 50));
    helpButton.setFont(new Font("Roboto", Font.PLAIN, 24));
    
    helpButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            helpPopup.show(helpButton, helpButton.getWidth(), -helpPopup.getPreferredSize().height / 2);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            Point p = e.getPoint();
            SwingUtilities.convertPointToScreen(p, helpButton);
            if (!helpPopup.getBounds().contains(p)) {
                helpPopup.setVisible(false);
            }
        }
    });
    
    GridBagConstraints helpGbc = new GridBagConstraints();
    helpGbc.gridx = 0;
    helpGbc.gridy = 1;
    helpGbc.anchor = GridBagConstraints.SOUTHWEST;
    helpGbc.weightx = 0.5;
    helpGbc.insets = new Insets(0, 20, 10, 0);
    backgroundLabel.add(helpButton, helpGbc);
    
    // Version label (right side)
    JLabel versionLabel = new JLabel("Version 1.0.0");
    versionLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
    versionLabel.setForeground(new Color(139, 69, 19));
    
    GridBagConstraints versionGbc = new GridBagConstraints();
    versionGbc.gridx = 1;
    versionGbc.gridy = 1;
    versionGbc.anchor = GridBagConstraints.SOUTHEAST;
    versionGbc.weightx = 0.5;
    versionGbc.insets = new Insets(0, 0, 10, 20);
    backgroundLabel.add(versionLabel, versionGbc);
    
    return backgroundLabel;
}


    private void createHelpPopup() {
    helpPopup = new JPopupMenu();
    helpPopup.setBackground(new Color(245, 222, 179));
    helpPopup.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(139, 69, 19), 2),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    String[] helpText = {
        "- Click on Customers to view, edit, or create a new customer.",
        "- Click on Orders to view or create a new order.",
        "- Click on Payments to create a new payment.",
        "- Click on Sales Representatives to view, edit, or create a new sales representative.",
        "- Click on Products to view, edit, or create a new Product."
    };

    JPanel helpPanel = new JPanel();
    helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
    helpPanel.setBackground(new Color(245, 222, 179));

    for (String text : helpText) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.PLAIN, 14));
        label.setForeground(new Color(139, 69, 19));
        label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        helpPanel.add(label);
        helpPanel.add(Box.createVerticalStrut(5));
    }

    helpPopup.add(helpPanel);

    helpPopup.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseExited(MouseEvent e) {
            Point p = e.getPoint();
            SwingUtilities.convertPointToScreen(p, helpPopup);
            if (!helpPopup.getBounds().contains(p)) {
                helpPopup.setVisible(false);
            }
        }
    });
}


        private JPanel createButtonPanel() {
    JPanel centeringPanel = new JPanel(new GridBagLayout());
    centeringPanel.setOpaque(false);
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    buttonPanel.setOpaque(false);

    String[] buttonLabels = {
        "Customers",
        "Orders",
        "Payments",
        "Sales Representatives",
        "Products"
    };

    // Calculate initial sizes based on screen resolution
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int buttonWidth = screenSize.width / 2;  // 50% of screen width
    int buttonHeight = screenSize.height / 12;  // Approximately 8% of screen height
    int fontSize = Math.max(32, screenSize.height / 30);  // Dynamic font size

    for (String label : buttonLabels) {
        JButton button = createButton(label);
        button.setFont(new Font("Roboto", Font.PLAIN, fontSize));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        button.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        buttonPanel.add(button);
        buttonPanel.add(Box.createVerticalStrut(30));

        if (label.equals("Customers")) {
            button.addActionListener(e -> mainApp.showScreen("CustomerPresentation"));
        } else if (label.equals("Orders")) {
            button.addActionListener(e -> mainApp.showScreen("OrderPresentation"));
        } else if (label.equals("Products")) {
            button.addActionListener(e -> mainApp.showScreen("ProductPresentation"));
        } else if (label.equals("Sales Representatives")) {
            button.addActionListener(e -> mainApp.showScreen("SalesRepPresentation"));
        } else if (label.equals("Payments")) {
            button.addActionListener(e -> mainApp.showScreen("NewEditPayment"));
        }
    }

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    centeringPanel.add(buttonPanel, gbc);

    return centeringPanel;
}

    private JButton createButton(String label) {
    JButton button = new JButton() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Button background
            if (getModel().isPressed()) {
                g2.setColor(new Color(170, 120, 60)); // Darker when pressed
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(190, 140, 80)); // Dark on hover
            } else {
                g2.setColor(new Color(222, 174, 111)); // Normal color
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            // Text rendering with larger font size
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = ((getHeight() - textHeight) / 2) + fm.getAscent();
            g2.drawString(label, x, y);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(139, 69, 19));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
        }
    };

    button.setFont(new Font("Roboto", Font.PLAIN, 32));
    button.setForeground(Color.WHITE);
    button.setContentAreaFilled(false);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setRolloverEnabled(true);

    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            button.setBackground(button.getBackground().darker());
            button.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            button.setBackground(new Color(222, 174, 111));
            button.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(button.getBackground().darker());
            button.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(new Color(222, 174, 111));
            button.repaint();
        }
    });

    return button;
}


    private JButton createHelpButton() {
    JButton helpButton = createButton("Help");
    
    // Set dimensions based on screen size
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int helpButtonWidth = screenSize.width / 32;  // Dynamic width
    int helpButtonHeight = screenSize.height / 64;  // Dynamic height
    helpButton.setPreferredSize(new Dimension(helpButtonWidth, helpButtonHeight));
    
    // Style the help button
    helpButton.setFont(new Font("Roboto", Font.PLAIN, 16));
    helpButton.setBackground(new Color(222, 174, 111));
    helpButton.setForeground(Color.WHITE);
    
    // Add hover effects
    helpButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            helpButton.setBackground(new Color(190, 140, 80));
            helpButton.repaint();
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            helpButton.setBackground(new Color(222, 174, 111));
            helpButton.repaint();
        }
    });
    
    return helpButton;
}
}
