package database.src.client;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import javax.swing.Timer;

/**
 * This program implements the client side of a restaurant reservation page;
 * It sends and receives information from the server side and displays the corresponding info
 *
 * @author Shawn Shu, lab sec 02
 * @version November 23, 2025
 */
public class Client implements IClient{
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
        } catch (IOException e) {
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

        addTime(panel);
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
     * This method adds real time tracking to the panel
     * @param panel is the JPanel that is currently being shown to the user
     */
    public void addTime(JPanel panel) {
        JLabel time = new JLabel("Date: " + LocalDateTime.now());
        time.setBounds(500, 500, 160, 25);
        panel.add(time);

        Timer timer = new Timer(1000, e -> {
            time.setText("Date: " + LocalDateTime.now());
        });
        timer.start();
    }
    /**
     * This method allows user to go back to the set GUI page
     *
     */
    public void goback() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        addTime(panel);

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
        panel.revalidate();
        panel.repaint();

        addTime(panel);

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
                        writer.println("GET_BOOKINGS " + email + " " + password);
                        writer.flush();
                        String bookings = reader.readLine().replace(";", "\n");
                        if (bookings == null) {
                            bookings = "Error in server";
                        }
                        reservationPage(email, password, bookings);
                    }
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
     * This method creates the create account page;
     * Account information are sent to server to store in database
     *
     */
    public void createAccountPage() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        addTime(panel);

        JLabel usernameLabel = new JLabel("Enter Username (email):");
        usernameLabel.setBounds(10, 20, 180, 25);
        panel.add(usernameLabel);

        JTextField usernameTextField = new JTextField(30);
        usernameTextField.setBounds(200, 20, 300, 25);
        panel.add(usernameTextField);

        JLabel createPasswordLabel = new JLabel("Enter password:");
        createPasswordLabel.setBounds(10, 50, 140, 25);
        panel.add(createPasswordLabel);

        JPasswordField createPasswordField = new JPasswordField(20);
        createPasswordField.setBounds(200, 50, 300, 25);
        panel.add(createPasswordField);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(200, 80, 160, 25);
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
                writer.flush();
                try {
                    String response = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response);
                    writer.println("GET_BOOKINGS " + username + " " + newPassword);
                    writer.flush();
                    String bookings = reader.readLine().replace(";", "\n");
                    if (bookings == null) {
                        bookings = "Error in server";
                    }
                    reservationPage(username, newPassword, bookings);
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
        panel.revalidate();
        panel.repaint();

        addTime(panel);

        JLabel deleteAccountLabel = new JLabel("Username to delete:");
        deleteAccountLabel.setBounds(10, 20, 180, 25);
        panel.add(deleteAccountLabel);

        JTextField deleteAccountTextField = new JTextField(20);
        deleteAccountTextField.setBounds(200, 20, 300, 25);
        panel.add(deleteAccountTextField);

        JButton deleteAccountButton = new JButton("Confirm Deletion");
        deleteAccountButton.setBounds(200, 80, 200, 25);
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
    public void reservationPage(String email, String password, String bookings) throws IOException {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();

        addTime(panel);

        JButton menu = new JButton("Logout");
        menu.setBounds(500, 30, 150, 30);
        panel.add(menu);

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goback();
            }
        });

        //1
        JLabel dateLabel = new JLabel("Select Dates:");
        dateLabel.setBounds(20, 50, 200, 25);
        panel.add(dateLabel);

        JComboBox<String> dateComboBox = new JComboBox<>();
        dateComboBox.setBounds(20, 80, 200, 25);
        panel.add(dateComboBox);

        writer.println("GET_DATES");
        writer.flush();
        String datesLists = reader.readLine();
        for (String d: datesLists.split(",")) {
            dateComboBox.addItem(d);
        }

        JLabel timeLabel = new JLabel("Select Time:");
        timeLabel.setBounds(20, 120, 200, 25);
        panel.add(timeLabel);

        JComboBox timeComboBox = new JComboBox<>();
        timeComboBox.setBounds(20, 150, 200, 25);
        panel.add(timeComboBox);

        writer.println("GET_TIMES " + dateComboBox.getSelectedItem());
        writer.flush();
        String timeLists = reader.readLine();
        for (String t: timeLists.split(",")) {
            timeComboBox.addItem(t);
        }
        //2
        JLabel partySize = new JLabel("Number of attendees:");
        partySize.setBounds(20, 180, 200, 25);
        panel.add(partySize);

        JComboBox partySizeBox = new JComboBox<>();
        partySizeBox.setBounds(20,210,200,25);

        for (int i = 1; i <= 10; i++) {
            partySizeBox.addItem(i);
        }

        panel.add(partySizeBox);

        //3
        JButton reserveButton = new JButton("Confirm Reservation");
        reserveButton.setBounds(20, 310, 160, 30);
        panel.add(reserveButton);

        //4
        JLabel showTables = new JLabel("Display Tables");
        showTables.setBounds(350, 200, 120, 25);
        panel.add(showTables);

        JTextArea tablesTextArea = new JTextArea(5, 30);
        tablesTextArea.setEditable(false);

        JScrollPane tablesScrollPane = new JScrollPane(tablesTextArea);
        tablesScrollPane.setBounds(350, 230, 350, 100);
        panel.add(tablesScrollPane);

        JLabel tablesLabel = new JLabel("Tables Available: ");
        tablesLabel.setBounds(20, 240, 120, 25);
        panel.add(tablesLabel);

        JComboBox<String> tableComboBox = new JComboBox<>();
        tableComboBox.setBounds(20, 270, 120, 25);
        panel.add(tableComboBox);


        writer.println("GET_REALTIME_TABLES " + dateComboBox.getSelectedItem() + " " +
                timeComboBox.getSelectedItem() + " " + partySizeBox.getSelectedItem());
        writer.flush();
        String tableLists = reader.readLine();
        for (String t : tableLists.split(";")) {
            tableComboBox.addItem(t.split(",")[0]);
        }

        //5
        JLabel bookingsLabel = new JLabel("Current Bookings:");
        bookingsLabel.setBounds(350, 50, 200, 25);
        panel.add(bookingsLabel);

        JTextArea bookingsTextArea = new JTextArea(5, 30);
        bookingsTextArea.setEditable(false);
        bookingsTextArea.setText(bookings);

        JScrollPane bookingsScrollPane = new JScrollPane(bookingsTextArea);
        bookingsScrollPane.setBounds(350, 80, 300, 100);
        panel.add(bookingsScrollPane);

        //6
        JLabel cancelLabel = new JLabel("Cancel Reservation(enter booking ID):");
        cancelLabel.setBounds(350, 400, 300, 25);
        panel.add(cancelLabel);

        JTextField cancelTextField = new JTextField();
        cancelTextField.setBounds(350, 430, 150, 25);
        panel.add(cancelTextField);

        JButton cancelButton = new JButton("Confirm cancellation");
        cancelButton.setBounds(350, 460, 150, 30);
        panel.add(cancelButton);

        writer.println("GET_BOOKINGS " + email + " " + password);
        String updated = reader.readLine().replace(";", "\n");
        bookingsTextArea.setText(updated);

        writer.println("GET_REALTIME_TABLES " + dateComboBox.getSelectedItem() + " " +
                timeComboBox.getSelectedItem() + " 0");
        String response = reader.readLine();

        if (response == null || response.isEmpty()) {
            tablesTextArea.setText("No table info available for " + dateComboBox.getSelectedItem());
        } else {
            String[] tables = response.split(";");
            for (int x = 0; x < tables.length; x++) {
                String[] components = tables[x].split(",");
                tables[x] = "Table Number: " + components[0] + ", Capacity: " + components[1];
            }
            String display = String.join("\n", tables);
            tablesTextArea.setText(display);
        }


        dateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = (String) dateComboBox.getSelectedItem();
                timeComboBox.removeAllItems();
                writer.println("GET_TIMES " + date);
                writer.flush();
                try {
                    String timeLists = reader.readLine();
                    for (String t : timeLists.split(",")) {
                        timeComboBox.addItem(t);
                    }
                } catch (IOException er) {
                    er.printStackTrace();
                }
            }
        });

        partySizeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tableComboBox.removeAllItems();
                    writer.println("GET_REALTIME_TABLES " + dateComboBox.getSelectedItem() + " " +
                            timeComboBox.getSelectedItem() + " " + partySizeBox.getSelectedItem());
                    writer.flush();
                    String tableLists = reader.readLine();
                    for (String t : tableLists.split(";")) {
                        tableComboBox.addItem(t.split(",")[0]);
                    }

                } catch (IOException er) {
                    er.printStackTrace();
                }
            }
        });

        timeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tableComboBox.removeAllItems();
                    writer.println("GET_REALTIME_TABLES " + dateComboBox.getSelectedItem() + " " +
                            timeComboBox.getSelectedItem() + " " + partySizeBox.getSelectedItem());
                    writer.flush();
                    String tableLists = reader.readLine();
                    for (String t : tableLists.split(";")) {
                        tableComboBox.addItem(t.split(",")[0]);
                    }

                    writer.println("GET_REALTIME_TABLES " + dateComboBox.getSelectedItem() + " " +
                            timeComboBox.getSelectedItem() + " 0");
                    String response = reader.readLine();

                    if (response == null || response.isEmpty()) {
                        tablesTextArea.setText("No table info available for " + dateComboBox.getSelectedItem());
                    } else {
                        String[] tables = response.split(";");
                        for (int x = 0; x < tables.length; x++) {
                            String[] components = tables[x].split(",");
                            tables[x] = "Table Number: " + components[0] + ", Capacity: " + components[1];
                        }
                        String display = String.join("\n", tables);
                        tablesTextArea.setText(display);
                    }
                } catch (IOException er) {
                    er.printStackTrace();
                }
            }
        });
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = (String) dateComboBox.getSelectedItem();
                String time = (String) timeComboBox.getSelectedItem();

                writer.println("MAKE_RESERVATION " + email + " " + password + " " +
                        partySizeBox.getSelectedItem() + " " + date + " " + time + " " + tableComboBox.getSelectedItem());
                try {
                    String response1 = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response1);

                    writer.println("GET_BOOKINGS " + email + " " + password);
                    String updated = reader.readLine().replace(";", "\n");
                    bookingsTextArea.setText(updated);

                    tableComboBox.removeAllItems();
                    writer.println("GET_REALTIME_TABLES " + dateComboBox.getSelectedItem() + " " +
                            timeComboBox.getSelectedItem() + " " + partySizeBox.getSelectedItem());
                    String tableLists = reader.readLine();
                    for (String t : tableLists.split(";")) {
                        tableComboBox.addItem(t.split(",")[0]);
                    }

                    writer.println("GET_REALTIME_TABLES " + dateComboBox.getSelectedItem() + " " +
                            timeComboBox.getSelectedItem() + " 0");
                    String response = reader.readLine();

                    if (response == null || response.isEmpty()) {
                        tablesTextArea.setText("No table info available for " + dateComboBox.getSelectedItem());
                    } else {
                        String[] tables = response.split(";");
                        for (int x = 0; x < tables.length; x++) {
                            String[] components = tables[x].split(",");
                            tables[x] = "Table Number: " + components[0] + ", Capacity: " + components[1];
                        }
                        String display = String.join("\n", tables);
                        tablesTextArea.setText(display);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = cancelTextField.getText();
                writer.println("CANCEL_RESERVATION " + id);
                try {
                    String response1 = reader.readLine();
                    JOptionPane.showMessageDialog(frame, response1);

                    writer.println("GET_BOOKINGS " + email + " " + password);
                    String updated = reader.readLine().replace(";", "\n");
                    bookingsTextArea.setText(updated);

                    writer.println("GET_REALTIME_TABLES " + dateComboBox.getSelectedItem() + " " +
                            timeComboBox.getSelectedItem() + " 0");
                    String response = reader.readLine();

                    if (response == null || response.isEmpty()) {
                        tablesTextArea.setText("No table info available for " + dateComboBox.getSelectedItem());
                    } else {
                        String[] tables = response.split(";");
                        for (int x = 0; x < tables.length; x++) {
                            String[] components = tables[x].split(",");
                            tables[x] = "Table Number: " + components[0] + ", Capacity: " + components[1];
                        }
                        String display = String.join("\n", tables);
                        tablesTextArea.setText(display);
                    }
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
