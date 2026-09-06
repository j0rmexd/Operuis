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
