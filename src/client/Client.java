package database.src.client;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.Timer;



/**
 * This program implements the client side of a restaurant reservation page;
 * It sends and receives information from the server side and displays the corresponding info
 *
 * @author Shawn Shu, lab sec 02
 * @version November 23, 2025
 */
public class Client {
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    JFrame frame;
    JPanel panel;

    /**
     * This constructor initializes fields of network IO
     *
     */
    public Client() {
        socket = null;
        reader = null;
        writer = null;
    }

    /**
     * This method connects the client with the server
     *
     */
    //@Override
    public void connect() {
        try {
            socket = new Socket("localhost", 4242);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method disconnects the client from the server
     *
     */
    //@Override
    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            System.out.println("Disconnected from server");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method initializes the frame and other GUI components
     *
     */
    //@Override
    public void setGUI() {
        frame = new JFrame("Restaurant Reservation");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        frame.add(panel);
        frame.setVisible(true);
        panel.setLayout(null);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 30, 150, 30);
        panel.add(loginButton);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(250, 30, 150, 30);
        panel.add(createAccountButton);

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBounds(450, 30, 150, 30);
        panel.add(deleteAccountButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPage();
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccountPage();
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });

    }

//    public void buttonMenu() {
//
//    }
    /**
     * This method creates the login page;
     * Login information is sent to the server for validation
     *
     */
    //@Override
    public void loginPage() {
        panel.removeAll();
        panel.repaint();
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 20, 80, 25);
        panel.add(emailLabel);

        JTextField emailTextField = new JTextField(20);
        emailTextField.setBounds(100, 20, 165, 25);
        panel.add(emailTextField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton submitLoginButton = new JButton("Login");
        submitLoginButton.setBounds(100, 80, 100, 25);
        panel.add(submitLoginButton);

        submitLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());
                writer.println("LOGIN " + email + " " + password);
                try {
                    String response = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response);
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    /**
     * This method creates the create account page;
     * Account information is sent to the server to be stored in the database
     *
     */
    public void createAccountPage() {
        panel.removeAll();
        panel.repaint();
        JLabel usernameLabel = new JLabel("Enter Username(email):");
        usernameLabel.setBounds(10, 20, 150, 25);
        panel.add(usernameLabel);

        JTextField usernameTextField = new JTextField(20);
        usernameTextField.setBounds(150, 20, 250, 25);
        panel.add(usernameTextField);

        JLabel createPasswordLabel = new JLabel("Enter password:");
        createPasswordLabel.setBounds(10, 50, 100, 25);
        panel.add(createPasswordLabel);

        JPasswordField createPasswordField = new JPasswordField(20);
        createPasswordField.setBounds(150, 50, 250, 25);
        panel.add(createPasswordField);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(150, 80, 150, 25);
        panel.add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String newPassword = new String(createPasswordField.getPassword());
                writer.println("CREATE_ACCOUNT " + username + " " + newPassword);
                try {
                    String response = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    /**
     * This method creates the delete account page;
     * Deleted account information will be sent to the server to remove from the database
     */
    public void deleteAccount() {
        panel.removeAll();
        panel.repaint();
        JLabel deleteAccountLabel = new JLabel("Username to delete:");
        deleteAccountLabel.setBounds(10, 20, 150, 25);
        panel.add(deleteAccountLabel);

        JTextField deleteAccountTextField = new JTextField(20);
        deleteAccountTextField.setBounds(160, 20, 200, 25);
        panel.add(deleteAccountTextField);

        JButton deleteAccountButton = new JButton("Confirm Deletion");
        deleteAccountButton.setBounds(100, 80, 150, 25);
        panel.add(deleteAccountButton);

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String deleteUsername = deleteAccountTextField.getText();
                writer.println("DELETE_ACCOUNT " + deleteUsername);
                try {
                    String response = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
        client.setGUI();
    }
}

