package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

    private JLabel emailLabel;
    private JTextField emailText;
    private JLabel passwordLabel;
    private JPasswordField passwordText;
    private JCheckBox showPassword;
    private JButton loginButton;
    private User authenticatedUser;
    private NavigationManager navigationManager;

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public LoginPage(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        emailLabel = new JLabel("Email:");
        emailText = new JTextField();
        passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField();
        showPassword = new JCheckBox("Show Password");
        loginButton = new JButton("Login");

        panel.add(emailLabel);
        panel.add(emailText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(showPassword);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);

        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    passwordText.setEchoChar((char) 0);
                } else {
                    passwordText.setEchoChar('*'); // Ascunde parola
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailText.getText();
                String password = new String(passwordText.getPassword());

                authenticatedUser = IMDB.getInstance().authenticateUser(email, password);

                if (authenticatedUser != null) {
                    JOptionPane.showMessageDialog(null, "Welcome back " + authenticatedUser.getUsername() + "!");
                    navigationManager.showHomePage(authenticatedUser);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong credentials!");
                }
            }
        });
    }




}