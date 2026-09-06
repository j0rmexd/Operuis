import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OperuisApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Terminal Screen Components
    private JTextField ipField;
    private JPasswordField passwordField;
    private JTextArea terminalLog;

    // Installation Screen Components
    private JLabel installTitleLabel;
    private JLabel textProgressBar;

    public OperuisApp() {
        setTitle("Operuis");
        setSize(550, 460); // Made slightly taller for better layouts
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        buildTerminalScreen();
        buildDashboardScreen();
        buildInstallationScreen();

        add(mainPanel);
        cardLayout.show(mainPanel, "Terminal");
    }

    /**
     * STAGE 1: Terminal SSH Input Interface (With Visible Text Input Boxes)
     */
    private void buildTerminalScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        terminalLog = new JTextArea();
        terminalLog.setBackground(Color.BLACK);
        terminalLog.setForeground(new Color(0, 255, 0)); // Terminal Green
        terminalLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        terminalLog.setEditable(false);
        terminalLog.setText("Operuis Initializer\n---------------------------------------\n" +
                "Type IP address or Meta Quest Name and press Enter:\n\n");

        // Layout panel for text fields at the bottom
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        inputPanel.setBackground(Color.BLACK);

        // IP Field with a clear, visible green border outline
        ipField = new JTextField(20);
        ipField.setBackground(new Color(25, 25, 25));
        ipField.setForeground(Color.GREEN);
        ipField.setCaretColor(Color.GREEN); // Visible typing line cursor
        ipField.setFont(new Font("Monospaced", Font.BOLD, 14));
        ipField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GREEN, 1),
                " [ TARGET IP / META QUEST NAME ] ",
                0, 0,
                new Font("Monospaced", Font.PLAIN, 10),
                Color.GREEN
        ));

        // Password Field with a clear, visible border outline
        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(25, 25, 25));
        passwordField.setForeground(Color.GREEN);
        passwordField.setCaretColor(Color.GREEN);
        passwordField.setFont(new Font("Monospaced", Font.BOLD, 14));
        passwordField.setEnabled(false); // Locked until IP is submitted
        passwordField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                " [ SSH/PSH PASSWORD ] ",
                0, 0,
                new Font("Monospaced", Font.PLAIN, 10),
                Color.DARK_GRAY
        ));

        inputPanel.add(ipField);
        inputPanel.add(passwordField);

        panel.add(new JScrollPane(terminalLog), BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        // Action when pressing Enter on the IP field
        ipField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ipField.getText().trim().isEmpty()) {
                    terminalLog.append("> Host Selected: " + ipField.getText() + "\n");
                    terminalLog.append("> Initiating SSH channel access...\n");
                    terminalLog.append("> Please type your password in the lower box and press Enter:\n\n");

                    // Lock IP and visually activate the password field
                    ipField.setEnabled(false);
                    ipField.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                            " [ HOST CONFIRMED ] ",
                            0, 0,
                            new Font("Monospaced", Font.PLAIN, 10),
                            Color.DARK_GRAY
                    ));

                    passwordField.setEnabled(true);
                    passwordField.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.GREEN, 1),
                            " [ ENTER SSH/PSH PASSWORD ] ",
                            0, 0,
                            new Font("Monospaced", Font.PLAIN, 10),
                            Color.GREEN
                    ));
                    passwordField.requestFocus();
                }
            }
        });

        // Action when pressing Enter on the password field
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordField.getPassword().length > 0) {
                    passwordField.setEnabled(false);
                    terminalLog.append("> Access authentication validated. Syncing panels...\n");

                    runTaskSequence("Connecting", new Runnable() {
                        @Override
                        public void run() {
                            cardLayout.show(mainPanel, "Dashboard");
                        }
                    });
                }
            }
        });

        mainPanel.add(panel, "Terminal");
    }

    /**
     * STAGE 2: Device Tool Management Environment (Legacy iOS Styling)
     */
    private void buildDashboardScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(235, 235, 240));

        JPanel headerBar = new JPanel();
        headerBar.setBackground(new Color(248, 248, 248));
        headerBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        JLabel titleLabel = new JLabel("Operuis Quest Kit", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        titleLabel.setForeground(Color.DARK_GRAY);
        headerBar.add(titleLabel);

        JPanel listPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        listPanel.setBackground(new Color(235, 235, 240));
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton btnMac = createLegacyButton("Install macOS Tweaks");
        JButton btnWin = createLegacyButton("Install Windows Tweaks");
        JButton btnFramework = createLegacyButton("Windows Frameworks for Meta Quest");

        listPanel.add(btnMac);
        listPanel.add(btnWin);
        listPanel.add(btnFramework);

        panel.add(headerBar, BorderLayout.NORTH);
        panel.add(listPanel, BorderLayout.CENTER);

        btnMac.addActionListener(e -> triggerInstallation("macOS Tweaks"));
        btnWin.addActionListener(e -> triggerInstallation("Windows Tweaks"));
        btnFramework.addActionListener(e -> triggerInstallation("Windows Frameworks for Meta Quest"));

        mainPanel.add(panel, "Dashboard");
    }

    /**
     * STAGE 3: Progress Processing Overlay Screen
     */
    private void buildInstallationScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(15, 20, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        installTitleLabel = new JLabel("Deploying Resources...", SwingConstants.CENTER);
        installTitleLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        installTitleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(installTitleLabel, gbc);

        textProgressBar = new JLabel("[            ] 0%", SwingConstants.CENTER);
        textProgressBar.setFont(new Font("Monospaced", Font.BOLD, 18));
        textProgressBar.setForeground(new Color(50, 205, 50));
        gbc.gridy = 1;
        panel.add(textProgressBar, gbc);

        mainPanel.add(panel, "Installation");
    }

    private void triggerInstallation(String taskName) {
        installTitleLabel.setText("Processing Framework: " + taskName);
        cardLayout.show(mainPanel, "Installation");

        runTaskSequence("Sync", new Runnable() {
            @Override
            public void run() {
                cardLayout.show(mainPanel, "Dashboard");
            }
        });
    }

    /**
     * Engine managing custom string tracker progress percentage increments
     */
    private void runTaskSequence(String logPrefix, Runnable onFinish) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int totalBlocks = 12;

                for (int i = 0; i <= totalBlocks; i++) {
                    final int currentStep = i;
                    final int percentage = (int) (((double) i / totalBlocks) * 100);

                    StringBuilder bar = new StringBuilder("[");
                    for (int j = 0; j < totalBlocks; j++) {
                        if (j < currentStep) {
                            bar.append("=");
                        } else {
                            bar.append(" ");
                        }
                    }
                    bar.append("] ").append(percentage).append("%");

                    SwingUtilities.invokeLater(() -> {
                        textProgressBar.setText(bar.toString());
                    });

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                SwingUtilities.invokeLater(onFinish);
            }
        }).start();
    }

    private JButton createLegacyButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Helvetica Neue" , Font.PLAIN, 14));
    button.setBackground(Color.WHITE);
    button.setForeground(new Color(0, 122, 255));
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 215), 1),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
    ));
    return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OperuisApp().setVisible(true);
        });
    }
}