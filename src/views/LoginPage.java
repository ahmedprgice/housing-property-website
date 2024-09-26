package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controllers.FileHandler;
import models.User;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    private FileHandler fileHandler;

    public LoginPage() {
        fileHandler = FileHandler.getInstance();

        setTitle("Login Page");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(5, 33, 67));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        gbc.gridwidth = 1; // Reset to single column for the following components

        // Username Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(23, 76, 124));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        add(loginButton, gbc);

        // Message Label for feedback
        gbc.gridy = 4;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        add(messageLabel, gbc);

        // Action Listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate(); // Call authenticate method
            }
        });
        // Set window properties
        setLocationRelativeTo(null); // Center the window
        setVisible(true); // Show the window
    }

    private void authenticate() {
        String username = usernameField.getText().trim(); // Get username
        String password = new String(passwordField.getPassword()).trim(); // Get password

        User user = fileHandler.authenticateUser(username, password); // Authenticate user
        if (user != null) {
            messageLabel.setText("Login successful!");
            messageLabel.setForeground(new Color(46, 204, 113));
            new HomeFinderApp(user).setVisible(true); // Open home app with user info
            this.dispose(); // Close the login window
        } else {
            messageLabel.setText("Invalid username or password.");
            messageLabel.setForeground(new Color(231, 76, 60)); // Set error message color
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage()); // Launch the LoginPage
    }
}
