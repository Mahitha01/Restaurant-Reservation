package database.src.client;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Font;
import javax.swing.Timer;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;

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
    Timer dateTimer;
    JLabel dateLabel;
    ComponentListener frameListener;
    Font inputFont;

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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        frame = new JFrame("Restaurant Reservation");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        frame.add(panel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (dateTimer != null && dateTimer.isRunning()) dateTimer.stop();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                if (dateTimer != null && dateTimer.isRunning()) dateTimer.stop();
            }
        });
        panel.setLayout(null);

        Font btnFont = new Font("SansSerif", Font.BOLD, 14);
        inputFont = new Font("SansSerif", Font.PLAIN, 15);

        addTime(panel);

        int frameWidth = frame.getWidth();
        int buttonWidth = 150;
        int buttonHeight = 30;
        int spacing = 30;
        int totalWidth = 3 * buttonWidth + 2 * spacing;
        int startX = (frameWidth - totalWidth) / 2;
        int y = 30;

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(startX, y, buttonWidth, buttonHeight);
        loginButton.setFont(btnFont);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(startX + buttonWidth + spacing, y, buttonWidth, buttonHeight);
        createAccountButton.setFont(btnFont);

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBounds(startX + 2 * (buttonWidth + spacing), y, buttonWidth, buttonHeight);
        deleteAccountButton.setFont(btnFont);
        panel.add(loginButton);
        panel.add(createAccountButton);
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

        frame.setVisible(true);
    }

    /**
     * This method adds real time tracking to the panel
     * @param panel is the JPanel that is currently being shown to the user
     */
    public void addTime(JPanel panel) {

        if (dateTimer != null && dateTimer.isRunning()) {
            dateTimer.stop();
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (dateLabel != null) {
            try { panel.remove(dateLabel); } catch (Exception ignored) {}
            dateLabel = null;
        }

        int y = 10;
        if (frame != null) {
            y = frame.getHeight() - 50;
        } else if (panel != null) {
            y = panel.getHeight() - 50;
        }
        if (y < 10) y = 10;

        dateLabel = new JLabel("Date: " + LocalDateTime.now().format(fmt));
        dateLabel.setBounds(10, y, 300, 25);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(dateLabel);

        dateTimer = new Timer(1000, e -> {
            if (dateLabel != null) dateLabel.setText("Date: " + LocalDateTime.now().format(fmt));
        });
        dateTimer.start();

        if (frame != null) {
            if (frameListener != null) {
                try { frame.removeComponentListener(frameListener); } catch (Exception ignored) {}
                frameListener = null;
            }
            frameListener = new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (dateLabel == null) return;
                    int newY = frame.getHeight() - 50;
                    if (newY < 10) newY = 10;
                    dateLabel.setBounds(10, newY, 300, 25);
                }
            };
            frame.addComponentListener(frameListener);
        }
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

        Font btnFont = new Font("SansSerif", Font.BOLD, 14);
        int frameWidth = frame.getWidth();
        int buttonWidth = 150;
        int buttonHeight = 30;
        int spacing = 30;
        int totalWidth = 3 * buttonWidth + 2 * spacing;
        int startX = (frameWidth - totalWidth) / 2;
        int y = 30;

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(startX, y, buttonWidth, buttonHeight);
        loginButton.setFont(btnFont);
        panel.add(loginButton);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(startX + buttonWidth + spacing, y, buttonWidth, buttonHeight);
        createAccountButton.setFont(btnFont);
        panel.add(createAccountButton);

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBounds(startX + 2 * (buttonWidth + spacing), y, buttonWidth, buttonHeight);
        deleteAccountButton.setFont(btnFont);
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

        int labelX = 10; int labelW = 80; int labelH = 25;
        int inputX = 100; int inputW = 165; int inputH = 40; int vSpacing = 12;

        int emailY = 20;
        int passwordY = emailY + inputH + vSpacing;

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(labelX, emailY + (inputH - labelH)/2, labelW, labelH);
        panel.add(emailLabel);

        JTextField emailTextField = new JTextField(20);
        emailTextField.setBounds(inputX, emailY, inputW, inputH);
        emailTextField.setFont(inputFont);
        emailTextField.setMargin(new Insets(4,4,4,4));
        emailTextField.setBorder(new EmptyBorder(4,6,4,6));
        panel.add(emailTextField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(labelX, passwordY + (inputH - labelH)/2, labelW, labelH);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(inputX, passwordY, inputW, inputH);
        passwordField.setFont(inputFont);
        passwordField.setMargin(new Insets(4,4,4,4));
        passwordField.setBorder(new EmptyBorder(4,6,4,6));
        panel.add(passwordField);

        JButton submitLoginButton = new JButton("Login");
        submitLoginButton.setBounds(inputX, passwordY + inputH + vSpacing, 100, 30);
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
                    if ("RIGHT_CREDENTIALS".equals(response)) {
                        writer.println("GET_BOOKINGS " + email + " " + password);
                        writer.flush();
                        String rawBookings = reader.readLine();
                        String bookings = formatBookingsDisplay(rawBookings);
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

        int uLabelX = 10; int uLabelW = 150; int uLabelH = 25;
        int uInputX = 150; int uInputW = 250; int uInputH = 40; int uVSpacing = 12;

        int userY = 20;
        int passY = userY + uInputH + uVSpacing;

        JLabel usernameLabel = new JLabel("Enter Username(email):");
        usernameLabel.setBounds(uLabelX, userY + (uInputH - uLabelH)/2, uLabelW, uLabelH);
        panel.add(usernameLabel);

        JTextField usernameTextField = new JTextField(20);
        usernameTextField.setBounds(uInputX, userY, uInputW, uInputH);
        usernameTextField.setFont(inputFont);
        usernameTextField.setMargin(new Insets(4,4,4,4));
        usernameTextField.setBorder(new EmptyBorder(4,6,4,6));
        panel.add(usernameTextField);

        JLabel createPasswordLabel = new JLabel("Enter password:");
        createPasswordLabel.setBounds(uLabelX, passY + (uInputH - uLabelH)/2, uLabelW, uLabelH);
        panel.add(createPasswordLabel);

        JPasswordField createPasswordField = new JPasswordField(20);
        createPasswordField.setBounds(uInputX, passY, uInputW, uInputH);
        createPasswordField.setFont(inputFont);
        createPasswordField.setMargin(new Insets(4,4,4,4));
        createPasswordField.setBorder(new EmptyBorder(4,6,4,6));
        panel.add(createPasswordField);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(uInputX, passY + uInputH + uVSpacing, 150, 30);
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
                    String rawBookings = reader.readLine();
                    String bookings = formatBookingsDisplay(rawBookings);
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

        int dLabelX = 10; int dLabelW = 150; int dLabelH = 25;
        int dInputX = 160; int dInputW = 200; int dInputH = 40; int dVSpacing = 12;

        int dY = 20;

        JLabel deleteAccountLabel = new JLabel("Username to delete:");
        deleteAccountLabel.setBounds(dLabelX, dY + (dInputH - dLabelH)/2, dLabelW, dLabelH);
        panel.add(deleteAccountLabel);

        JTextField deleteAccountTextField = new JTextField(20);
        deleteAccountTextField.setBounds(dInputX, dY, dInputW, dInputH);
        deleteAccountTextField.setFont(inputFont);
        deleteAccountTextField.setMargin(new Insets(4,4,4,4));
        deleteAccountTextField.setBorder(new EmptyBorder(4,6,4,6));
        panel.add(deleteAccountTextField);

        dY += dInputH + dVSpacing;

        JLabel deletePasswordLabel = new JLabel("Password:");
        deletePasswordLabel.setBounds(dLabelX, dY + (dInputH - dLabelH)/2, dLabelW, dLabelH);
        panel.add(deletePasswordLabel);

        JPasswordField deletePasswordField = new JPasswordField(20);
        deletePasswordField.setBounds(dInputX, dY, dInputW, dInputH);
        deletePasswordField.setFont(inputFont);
        deletePasswordField.setMargin(new Insets(4,4,4,4));
        deletePasswordField.setBorder(new EmptyBorder(4,6,4,6));
        panel.add(deletePasswordField);

        JButton deleteAccountButton = new JButton("Confirm Deletion");
        deleteAccountButton.setBounds(100, dY + dInputH + dVSpacing, 150, 30);
        panel.add(deleteAccountButton);

        JButton menu = new JButton("Go back");
        menu.setBounds(500, 30, 150, 30);
        panel.add(menu);

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String deleteUsername = deleteAccountTextField.getText();

                writer.println("DELETE_ACCOUNT " + deleteUsername + " " + new String(deletePasswordField.getPassword()));
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
        dateComboBox.setBounds(20, 80, 200, 40);
        dateComboBox.setFont(inputFont);
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

        JComboBox<String> timeComboBox = new JComboBox<>();
        timeComboBox.setBounds(20, 150, 200, 40);
        timeComboBox.setFont(inputFont);
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

        JComboBox<Integer> partySizeBox = new JComboBox<>();
        partySizeBox.setBounds(20,210,200,40);
        partySizeBox.setFont(inputFont);

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
        showTables.setBounds(350, 300, 120, 25);
        panel.add(showTables);

        JTextArea tablesTextArea = new JTextArea(5, 30);
        tablesTextArea.setEditable(false);
        tablesTextArea.setFocusable(false);

        JScrollPane tablesScrollPane = new JScrollPane(tablesTextArea);
        tablesScrollPane.setBounds(350, 330, 350, 100);
        panel.add(tablesScrollPane);

        JLabel tablesLabel = new JLabel("Tables Available: ");
        tablesLabel.setBounds(20, 240, 120, 25);
        panel.add(tablesLabel);

        JComboBox<String> tableComboBox = new JComboBox<>();
        tableComboBox.setBounds(20, 270, 120, 40);
        tableComboBox.setFont(inputFont);
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
        bookingsTextArea.setFocusable(false);
        bookingsTextArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bookingsTextArea.setLineWrap(true);
        bookingsTextArea.setWrapStyleWord(true);
        bookingsTextArea.setText(bookings);

        JScrollPane bookingsScrollPane = new JScrollPane(bookingsTextArea);
        bookingsScrollPane.setBounds(350, 80, 350, 200);
        panel.add(bookingsScrollPane);

        //6
        JLabel cancelLabel = new JLabel("Cancel Reservation(enter booking ID):");
        cancelLabel.setBounds(350, 450, 300, 25);
        panel.add(cancelLabel);

        JTextField cancelTextField = new JTextField();
        cancelTextField.setBounds(350, 480, 150, 40);
        cancelTextField.setFont(inputFont);
        cancelTextField.setMargin(new Insets(4,4,4,4));
        cancelTextField.setBorder(new EmptyBorder(4,6,4,6));
        panel.add(cancelTextField);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(350, 530, 150, 30);
        panel.add(cancelButton);

        writer.println("GET_BOOKINGS " + email + " " + password);
        String updated = reader.readLine();
        String formattedBookings = formatBookingsDisplay(updated);
        bookingsTextArea.setText(formattedBookings);

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
            tablesTextArea.setCaretPosition(0);
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
                        tablesTextArea.setCaretPosition(0);
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
                    String updated = reader.readLine();
                    bookingsTextArea.setText(formatBookingsDisplay(updated));

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
                        tablesTextArea.setCaretPosition(0);
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
                    String updated = reader.readLine();
                    bookingsTextArea.setText(formatBookingsDisplay(updated));

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
                        tablesTextArea.setCaretPosition(0);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.repaint();
    }

    /**
     * Formats booking information
     * @param rawBookings The raw booking string from the server
     * @return A formatted string with each booking on separate lines with clear labels
     */
    private String formatBookingsDisplay(String rawBookings) {
        if (rawBookings == null || rawBookings.isEmpty() || rawBookings.equals("Error in server")) {
            return "You have no reservations at this time";
        }

        StringBuilder formatted = new StringBuilder();
        String[] bookings = rawBookings.split(";");

        if (bookings.length == 0 || (bookings.length == 1 && bookings[0].trim().isEmpty())) {
            return "you have no reservations at this time";
        }

        for (int i = 0; i < bookings.length; i++) {
            String booking = bookings[i].trim();
            if (booking.isEmpty()) continue;

            try {
                String[] parts = booking.split(",");
                String partySize = "";
                String dateTime = "";
                String id = "";
                String table = "";

                for (String part : parts) {
                    part = part.trim();
                    if (part.startsWith("PartySize:")) {
                        partySize = part.substring(10).trim();
                    } else if (part.startsWith("Time:")) {
                        dateTime = part.substring(5).trim();
                    } else if (part.contains("ID:")) {
                        String[] idAndTable = part.split("Booked Table:");
                        if (idAndTable.length >= 1) {
                            id = idAndTable[0].replace("ID:", "").trim();
                        }
                        if (idAndTable.length >= 2) {
                            table = idAndTable[1].trim();
                        }
                    }
                }

                String date = "";
                String time = "";
                if (dateTime.contains(" ")) {
                    String[] dt = dateTime.split(" ", 2);
                    date = dt[0];
                    time = dt.length > 1 ? dt[1] : "";
                }

                formatted.append("Table ").append(table).append(" for ").append(partySize)
                         .append(" ").append(partySize.equals("1") ? "guest" : "guests").append(" on ").append(date)
                         .append(" at ").append(time).append(" (ID: ").append(id).append(")\n");

            } catch (Exception e) {
                formatted.append(booking).append("\n");
            }
        }

        return formatted.toString();
    }

    public static void main(String[] args) {
        Client client = new Client();

        Thread connector = new Thread(() -> client.connect(), "Client-Connect-Thread");
        connector.setDaemon(true);
        connector.start();

        javax.swing.SwingUtilities.invokeLater(() -> client.setGUI());
    }
}
