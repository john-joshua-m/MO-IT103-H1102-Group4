package com.mycompany.motorphpayrollsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List; // Import List for UserManager.getUsers()
import java.util.logging.Level;
import java.util.logging.Logger;


//This is a separate GUI, eto yung login box na lalabas when we open the system

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private PayrollSystemGUI payrollSystemGUI; // Reference to the main GUI

    public LoginGUI(PayrollSystemGUI payrollSystemGUI) {
        this.payrollSystemGUI = payrollSystemGUI;
        setTitle("Login - MotorPH Payroll System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        initComponents();
        addListeners();

        UserManager.getInstance();

    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light blueish white
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("MOTORPH LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102)); // Dark blue
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Login Button
        loginButton = createStyledButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(messageLabel, gbc);
    }

    private void addListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        // This allows pressing Enter key to login
        usernameField.addActionListener(e -> authenticateUser());
        passwordField.addActionListener(e -> authenticateUser());
    }

    private void authenticateUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password.");
            return;
        }

        // This our authenticator
        User authenticatedUser = UserManager.getInstance().authenticateUser(username, password);

        if (authenticatedUser != null) {
            messageLabel.setText("Login Successful!");
            JOptionPane.showMessageDialog(this, "Welcome, " + authenticatedUser.getFirstName() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            payrollSystemGUI.setLoggedInUser(authenticatedUser); // Pass authenticated user to main GUI
            this.dispose(); // Close login window
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    // Helper for styled button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 51, 102)); 
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 128)); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 51, 102)); 
            }
        });
        return button;
    }
}
