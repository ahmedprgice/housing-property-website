import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controllers.FileHandler;
import models.User;

public class SignupPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JComboBox<User.Role> roleComboBox;
    private JButton signupButton;
    private JLabel messageLabel;

    private FileHandler fileHandler;

    public SignupPage() {
        fileHandler = FileHandler.getInstance();

        setTitle("Signup Page");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(5, 33, 67));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Signup", SwingConstants.CENTER);
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
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(15);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(Color.WHITE);
        add(roleLabel, gbc);

        gbc.gridx = 1;
        roleComboBox = new JComboBox<>(User.Role.values()); // ComboBox for roles
        add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        signupButton = new JButton("Signup");
        signupButton.setBackground(new Color(23, 76, 124));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(new Font("Arial", Font.BOLD, 16));
        signupButton.setFocusPainted(false);
        add(signupButton, gbc);

        gbc.gridy = 6;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        add(messageLabel, gbc);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        User.Role role = (User.Role) roleComboBox.getSelectedItem(); // Get selected role

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Please fill out all fields.");
            messageLabel.setForeground(new Color(231, 76, 60));
        } else {
            User newUser = new User(username, password, email, role);
            fileHandler.saveUser(newUser);
            messageLabel.setText("Signup successful! User saved.");
            messageLabel.setForeground(new Color(46, 204, 113));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignupPage());
    }
}
