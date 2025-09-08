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
    return UIFactory.createTitlePanel(mainApp, mainPanel, "Home Products Inc.", 
        "Welcome to the Main Menu. Please use this page to navigate through the database.", null);
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
    JButton helpButton = UIFactory.createMainMenuButton("Help");
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
        JButton button = UIFactory.createMainMenuButton(label);
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

}
