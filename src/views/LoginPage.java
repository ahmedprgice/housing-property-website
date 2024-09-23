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

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(23, 76, 124));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        add(loginButton, gbc);

        gbc.gridy = 4;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        add(messageLabel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void authenticate() {
        String username = usernameField.getText().trim(); // Remove leading/trailing spaces
        String password = new String(passwordField.getPassword()).trim(); // Remove leading/trailing spaces
    
        User user = fileHandler.authenticateUser(username, password);
        if (user != null) {
            messageLabel.setText("Login successful!");
            messageLabel.setForeground(new Color(46, 204, 113));
            new HomeFinderApp(user).setVisible(true); // Pass the authenticated user object
            this.dispose(); // Close the login window
        } else {
            messageLabel.setText("Invalid username or password.");
            messageLabel.setForeground(new Color(231, 76, 60));
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}
