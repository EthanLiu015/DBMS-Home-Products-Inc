import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.util.Calendar;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * A utility class for creating common Swing UI components for the Home Products Inc. application.
 * This helps to reduce code duplication and maintain a consistent look and feel.
 */
public final class UIFactory {

    private static final Color PRIMARY_BACKGROUND = new Color(245, 222, 179);
    private static final Color PRIMARY_FOREGROUND = new Color(139, 69, 19);
    private static final Color BORDER_COLOR = new Color(200, 150, 120);
    private static final Color BUTTON_COLOR = new Color(222, 174, 111);
    private static final Color BUTTON_HOVER_COLOR = new Color(190, 140, 80);
    private static final Color BUTTON_PRESSED_COLOR = new Color(170, 120, 60);
    private static final Color EXIT_BUTTON_COLOR = new Color(139, 0, 0);
    private static final Color EXIT_BUTTON_HOVER_COLOR = new Color(100, 0, 0);

    private UIFactory() {
        // Private constructor to prevent instantiation
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                if (getModel().isPressed()) {
                    g2.setColor(BUTTON_PRESSED_COLOR);
                } else if (getModel().isRollover()) {
                    g2.setColor(BUTTON_HOVER_COLOR);
                } else {
                    g2.setColor(BUTTON_COLOR);
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
            BorderFactory.createLineBorder(PRIMARY_FOREGROUND, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setRolloverEnabled(true);

        return button;
    }

    public static JPanel createExitButton() {
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
        exitButtonPanel.setBackground(EXIT_BUTTON_COLOR);

        exitButtonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exitButtonPanel.setBackground(EXIT_BUTTON_HOVER_COLOR);
                exitButtonPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButtonPanel.setBackground(EXIT_BUTTON_COLOR);
                exitButtonPanel.repaint();
            }
        });

        return exitButtonPanel;
    }

    public static JLayeredPane createTitlePanel(MainApplication mainApp, JPanel parentPanel, String title, String description, String backScreenName) {
        JLayeredPane titlePanel = new JLayeredPane();
        titlePanel.setBackground(PRIMARY_BACKGROUND);
        titlePanel.setOpaque(true);

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Futura", Font.BOLD, 36));
        titleLabel.setForeground(PRIMARY_FOREGROUND);

        JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
        descriptionLabel.setForeground(new Color(139, 69, 19));

        JPanel exitButtonPanel = createExitButton();
        exitButtonPanel.setBounds(10, 10, 40, 40);        
        titlePanel.add(exitButtonPanel, JLayeredPane.DEFAULT_LAYER);
        
        if (backScreenName != null) {
            JButton backButton = createStyledButton("Back");
            backButton.setBounds(10, 60, 100, 40);
            backButton.addActionListener(e -> mainApp.showScreen(backScreenName));
            titlePanel.add(backButton, JLayeredPane.DEFAULT_LAYER);
        }

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        titlePanel.add(titleLabel, JLayeredPane.DEFAULT_LAYER);
        titlePanel.add(descriptionLabel, JLayeredPane.DEFAULT_LAYER);
        titlePanel.add(logoLabel, JLayeredPane.PALETTE_LAYER);

        parentPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension panelSize = parentPanel.getSize();
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
                FontMetrics titleMetrics = titleLabel.getFontMetrics(titleLabel.getFont());
                int titleHeightCalculated = titleMetrics.getHeight();
                titleLabel.setBounds(0, 20, panelSize.width, titleHeightCalculated);

                int descriptionFontSize = Math.max(20, panelSize.width / 80);
                descriptionLabel.setFont(new Font("Roboto", Font.PLAIN, descriptionFontSize));
                FontMetrics descMetrics = descriptionLabel.getFontMetrics(descriptionLabel.getFont());
                int descHeightCalculated = descMetrics.getHeight();
                descriptionLabel.setBounds(0, titleLabel.getY() + titleLabel.getHeight() + 10, panelSize.width, descHeightCalculated);

                titlePanel.revalidate();
                titlePanel.repaint();
            }
        });

        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        return titlePanel;
    }

    public static JTextField createNumericTextField(int width) {
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

    public static JTextField createRestrictedTextField(int width, int maxLength) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 25));
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

    public static JFormattedTextField createPhoneField(int width) {
        JFormattedTextField field = null;
        try {
            MaskFormatter phoneFormatter = new MaskFormatter("(###) ###-####");
            phoneFormatter.setPlaceholderCharacter('_');
            field = new JFormattedTextField(phoneFormatter);
            field.setPreferredSize(new Dimension(width, 25));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            field = new JFormattedTextField(); // fallback
        }
        return field;
    }

    public static JTextArea createRestrictedTextArea(int rows, int cols, int maxLength) {
        JTextArea area = new JTextArea(rows, cols);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= maxLength) {
                    super.insertString(offs, str, a);
                }
            }
        });
        return area;
    }

    public static void addSectionHeader(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel headerLabel = new JLabel(text);
        headerLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        headerLabel.setForeground(PRIMARY_FOREGROUND);

        JSeparator separator = new JSeparator();
        separator.setForeground(PRIMARY_FOREGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
        headerPanel.setBackground(PRIMARY_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(separator, BorderLayout.CENTER);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(headerPanel, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy++;
    }

    public static void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));
        panel.add(label, gbc);

        gbc.gridx++;
        panel.add(field, gbc);
        gbc.gridx--;
        gbc.gridy++;
    }

    public static JSpinner createDatePicker(String format, int width) {
        SpinnerDateModel dateModel = new SpinnerDateModel(new java.util.Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, format);
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(width, 25));
        return dateSpinner;
    }

    public static JTextField createNumericOnlyTextField(int width) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 25));

        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("[0-9]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("[0-9]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        return field;
    }

    public static JTextField createNumericOnlyTextField(int width, int maxLength) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 25));

        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("[0-9]*") && (fb.getDocument().getLength() + string.length()) <= maxLength) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("[0-9]*") && (fb.getDocument().getLength() - length + text.length()) <= maxLength) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        return field;
    }

    public static JPanel createInfoField(String label, String value) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Roboto", Font.BOLD, 14));
        labelComponent.setPreferredSize(new Dimension(200, 20));
        fieldPanel.add(labelComponent, BorderLayout.WEST);

        JTextArea valueComponent = new JTextArea(value);
        valueComponent.setFont(new Font("Roboto", Font.PLAIN, 14));
        valueComponent.setWrapStyleWord(true);
        valueComponent.setLineWrap(true);
        valueComponent.setEditable(false);
        valueComponent.setOpaque(false);
        valueComponent.setFocusable(false);
        fieldPanel.add(valueComponent, BorderLayout.CENTER);

        return fieldPanel;
    }

    public static void addViewSection(JPanel parent, String title, JPanel content, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.insets = new Insets(15, 20, 5, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_FOREGROUND);
        parent.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 20, 10, 20);
        content.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        parent.add(content, gbc);
    }

    public static JPanel createSaveCancelButtonPanel(Runnable saveAction, Runnable cancelAction) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(PRIMARY_BACKGROUND);

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        saveButton.addActionListener(e -> saveAction.run());
        cancelButton.addActionListener(e -> cancelAction.run());

        panel.add(saveButton);
        panel.add(cancelButton);
        return panel;
    }

    public static JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        actionPanel.setBackground(PRIMARY_BACKGROUND);
        actionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        return actionPanel;
    }

    public static JPanel createPresentationActionPanel(String buttonText, Runnable action) {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        actionPanel.setBackground(PRIMARY_BACKGROUND);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JButton createNewButton = createStyledButton(buttonText);
        createNewButton.addActionListener(e -> action.run());
        
        actionPanel.add(createNewButton);
        return actionPanel;
    }

    public static JScrollPane createThemedScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBackground(PRIMARY_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(PRIMARY_BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Set faster scroll speed

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_FOREGROUND;
                this.trackColor = BUTTON_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
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
        return scrollPane;
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setBackground(PRIMARY_BACKGROUND);
        table.setForeground(PRIMARY_FOREGROUND);
        table.setGridColor(BUTTON_COLOR);
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        table.getTableHeader().setBackground(BUTTON_COLOR);
        table.getTableHeader().setForeground(PRIMARY_FOREGROUND);
    }

    public static class TableButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public TableButtonRenderer(String text) {
            super(text);
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(PRIMARY_FOREGROUND);
            setFont(new Font("Roboto", Font.BOLD, 14));
            setBorder(BorderFactory.createRaisedBevelBorder());
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(BUTTON_HOVER_COLOR);
            } else {
                setBackground(PRIMARY_FOREGROUND);
            }
            return this;
        }
    }

    public static class TableButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private final Consumer<Integer> action;
        private int viewRow;
        private JTable table;

        public TableButtonEditor(String text, Consumer<Integer> action) {
            super(new JCheckBox());
            this.action = action;
            button = new JButton(text);
            this.label = text;
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setBackground(PRIMARY_FOREGROUND);
            button.setFont(new Font("Roboto", Font.BOLD, 14));
            button.setBorder(BorderFactory.createRaisedBevelBorder());
            
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(BUTTON_HOVER_COLOR);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(PRIMARY_FOREGROUND);
                }
            });
            
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.table = table;
            this.viewRow = row;
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                action.accept(this.table.convertRowIndexToModel(viewRow));
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

    public static JButton createMainMenuButton(String label) {
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(BUTTON_PRESSED_COLOR);
                } else if (getModel().isRollover()) {
                    g2.setColor(BUTTON_HOVER_COLOR);
                } else {
                    g2.setColor(BUTTON_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int x = (getWidth() - textWidth) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);

                // Draw the border
                g2.setColor(PRIMARY_FOREGROUND);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
            }
        };

        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setRolloverEnabled(true);
        return button;
    }
}
