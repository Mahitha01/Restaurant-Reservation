package database.src.client;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

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
     * This method connects the client to the server
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
            JOptionPane.showMessageDialog(null, "Failed to connect to server");
        }
    }
    /**
     * This method disconnects the client with the server
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
        frame.setSize(700, 400);
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

    /**
     * This method allows user to go back to the set GUI page
     *
     */
    public void goback() {
        panel.removeAll();
        panel.repaint();

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
    /**
     * This method creates the login page;
     * Login information are sent to server for validation
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

        JButton menu = new JButton("Go back");
        menu.setBounds(500, 30, 150, 30);
        panel.add(menu);

        submitLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());
                writer.println("LOGIN " + email + " " + password);
                writer.flush();
                try {
                    String response = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response);
                    if (response.equals("RIGHT_CREDENTIALS")) {
                        writer.println("RESERVE " + email + " " + password);
                        writer.flush();
                        String bookings = reader.readLine();
                        System.out.println("Bookings: " + bookings);
                        if (bookings == null) {
                            bookings = "Error in server";
                        }
                        reservationPage(email, password, bookings);
                    }
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goback();
            }
        });
    }

    /**
     * This method creates the create account page;
     * Account information are sent to server to store in database
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

        JButton menu = new JButton("Go back");
        menu.setBounds(500, 30, 150, 30);
        panel.add(menu);

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

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goback();
            }
        });
    }
    /**
     * This method creates the delete account page;
     * Deleted account information will be sent to server to remove from database
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

        JButton menu = new JButton("Go back");
        menu.setBounds(500, 30, 150, 30);
        panel.add(menu);

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

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goback();
            }
        });
    }

    /**
     * This method allows the user to choose a reservation based on the date and time,
     * or cancel a reservation based on reservation id;
     * The client sends the information to the server, which stores it in the database;
     * If a reservation is successful the client side will display it using simple GUI
     * @param email A String representing the username
     * @param password A String representing the password
     * @param bookings A String containing all the reservations
     */
    public void reservationPage(String email, String password, String bookings) {
        panel.removeAll();
        panel.repaint();

        //1
        JLabel dateLabel = new JLabel("MM-DD-YYYY");
        dateLabel.setBounds(20, 50, 200, 25);
        panel.add(dateLabel);

        JTextField dateTextField = new JTextField();
        dateTextField.setBounds(20, 80, 200, 25);
        panel.add(dateTextField);

        JLabel timeLabel = new JLabel("Time");
        timeLabel.setBounds(20, 120, 200, 25);
        panel.add(timeLabel);

        JTextField timeTextField = new JTextField();
        timeTextField.setBounds(20, 150, 200, 25);
        panel.add(timeTextField);





        //2
        JButton reserveButton = new JButton("Confirm Reservation");
        reserveButton.setBounds(20, 180, 160, 30);
        panel.add(reserveButton);

        //3
        JLabel bookingsLabel = new JLabel("Current Bookings:");
        bookingsLabel.setBounds(350, 50, 200, 25);
        panel.add(bookingsLabel);

        JTextArea bookingsTextArea = new JTextArea(5, 30);
        bookingsTextArea.setEditable(false);
        bookingsTextArea.setText(bookings);

        JScrollPane bookingsScrollPane = new JScrollPane(bookingsTextArea);
        bookingsScrollPane.setBounds(350, 80, 300, 100);
        panel.add(bookingsScrollPane);

        //4
        JLabel cancelLabel = new JLabel("Cancel Reservation(enter booking ID):");
        cancelLabel.setBounds(350, 200, 300, 25);
        panel.add(cancelLabel);

        JTextField cancelTextField = new JTextField();
        cancelTextField.setBounds(350, 250, 150, 25);
        panel.add(cancelTextField);

        JButton cancelButton = new JButton("Confirm cancellation");
        cancelButton.setBounds(350, 280, 150, 30);
        panel.add(cancelButton);

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateTextField.getText();
                String time = timeTextField.getText();

                writer.println("MAKE_RESERVATION " + email + " " + date + " " + time);

                try {
                    String response = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response);

                    writer.println("RESERVE " + email + " " + password);
                    String updated = reader.readLine();
                    bookingsTextArea.setText(updated);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = cancelTextField.getText();
                writer.println("CANCEL_RESERVATION " + email + " " + id);

                try {
                    String response = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response);

                    writer.println("RESERVE " + email + " " + password);
                    String updated = reader.readLine();
                    bookingsTextArea.setText(updated);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.repaint();
    }
    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
        client.setGUI();
    }
}
