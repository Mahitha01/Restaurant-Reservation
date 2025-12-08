package database.src.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import database.src.database.DatabaseManager;
import database.src.users.Booking;
import database.src.users.IBooking;

/**
 * Server side for program, gets user information and calls database to store
 * information
 *
 * @author Mahitha Kodali, lab sec 02
 * @version November 24, 2025
 */

public class Server implements Runnable {
    private static int baseId = 0;
    private Socket socket;
    private static Object lock = new Object();
    //private Map<String, Map<String, Boolean>> tableAvailability = new HashMap<>();
    /**1
     * This constructor creates a Server object
     * When a client connects to the server, a new server object is created
     * and a new thread is created.
     *
     * @param socket The client socket used to communicate with the server
     */
    public Server(Socket socket) {
        this.socket = socket;

    }

    /**
     * Starts the server
     * Doesn't end until there is a force exit (always runs), like real life servers
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);

            while (true) {
                System.out.println("Waiting...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Found client! " + clientSocket.getLocalAddress());
                Thread clientThread = new Thread(new Server(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method communicates with the client and the clients choices are all handled.
     * @param reader is a BufferedReader object that reads from the client
     * @param writer is a PrintWriter object that writes back to the client
     * @param dm is a DatabaseManager object that checks if user exists or adds user depending
     * on if they are logging in or creating an account
     * @return String array containing email, password
     */
    //@Override
    public String[] login(String email, String password, BufferedReader reader, PrintWriter writer, DatabaseManager dm)
            throws IOException, ClassNotFoundException {
        String[] userDetails = new String[0];
        if (dm.authenticate(email, password)) {
            userDetails = new String[]{email, password};
            writer.println("RIGHT_CREDENTIALS");
            writer.flush();
        } else {
            writer.println("WRONG_CREDENTIALS");
            writer.flush();
        }
        return userDetails;
    }

    /**
     * This method creates an account
     * @param email a string representing the user's email
     * @param password a string representing the user's password
     * @param reader a BufferedReader object to read from the client
     * @param writer A printWriter object to write back to the client
     * @param dm A DatabaseManager object used for creating a user
     * @return an Array of strings that have the userdetails
     * @throws IOException from writing or saving in database
     */
    public String[] createAccount(String email, String password, BufferedReader reader,
                                  PrintWriter writer, DatabaseManager dm) throws IOException {
        String[] userDetails = new String[0];
        if (dm.createUser(email, password)) {
            writer.println("SUCCESS_CREATE_ACCOUNT");
            writer.flush();
            dm.save();
            userDetails = new String[]{email, password};
        } else {
            writer.println("INVALID_CREATE_ACCOUNT");
            writer.flush();
        }
        return userDetails;
    }

    /**
     * This methods deletes an account with username
     * @param username the username of the user who wants to delete their account
     * @param reader a BufferedReader object to read from the client
     * @param writer A printWriter object to write back to the client
     * @param dm A DatabaseManager object used for deleting the user
     * @throws IOException from writing or saving in database
     */
    public void deleteAccount(String username, String password, BufferedReader reader, PrintWriter writer,
                              DatabaseManager dm) throws IOException {
        if (dm.authenticate(username, password)) {
            if (dm.deleteUser(username, password)) {
                writer.println("DELETED_SUCCESSFULLY");
                writer.flush();
                dm.save();
            } else {
                writer.println("DELETE_FAILED");
                writer.flush();
            }
        } else {
            writer.println("INCORRECT_PASSWORD");
            writer.flush();
        }
    }

    /**
     * This method sends back all the bookings this user has already made
     * @param email A string representing the user's email
     * @param writer A PrintWriter object that communicates with the client
     * @param dm A DatabaseManager object that finds this specific user's bookings.
     * @return A string containing all bookings
     */
    public String getBookings(String email, PrintWriter writer, DatabaseManager dm) {
        List<IBooking> books = dm.getUserBookings(email);
        String bookings = "";
        for (IBooking b : books) {
            bookings += "PartySize: " + b.getPartySize() + ", Time: " + b.getBookingTime() +
                    ", ID: " + b.getId() + " Booked Table: " + b.getTableNum() + ";";
        }
        writer.println(bookings);
        return bookings;
    }

    /**
     * This method makes a reservation
     * @param content a string array containing the details the client entered
     * @param writer A printWriter object used for communicating with the server
     * @param dm A DatabaseManager object used for adding a reservation to the database.
     */
    public void makeReservation(String[] content, PrintWriter writer, DatabaseManager dm) {
        String date = content[4];
        String time = content[5];
        String dateTime = content[4] + " " + content[5];
        int tableNum = Integer.parseInt(content[6]);
        IBooking booking = new Booking(content[1], content[2], Integer.parseInt(content[3]),
                dateTime, tableNum);
        boolean booked = dm.addReservation(content[1], booking);
        dm.occupyTable(date, time, tableNum);
        try {
            dm.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (booked) {
            writer.println("SUCCESSFULLY_RESERVED");
        } else {
            writer.println("RESERVATION_FAILED");
        }
    }

    /**
     * This method cancels a reservation
     * @param id the unique id given per reservation
     * @param writer A printWriter object used for communicating with the server
     * @param dm A DatabaseManager object used for cancelling a reservation.
     */
    public void cancelReserve(int id, PrintWriter writer, DatabaseManager dm) {
        boolean success = dm.cancelReservation(id);

        try {
            dm.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            writer.println("SUCCESSFULLY_CANCELLED");
        } else {
            writer.println("CANCELLATION_FAILED");
        }
    }

    /**
     * This method creates a list of dates for the next 3 months for the drop-down, which the user can choose from
     * @return A String arraylist containing the dates for the next 3 months.
     */
    private List<String> Dates() {
        List<String> dates = new ArrayList<>();
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusMonths(3);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            dates.add(d.format(dtf));
        }
        return dates;
    }

    /**
     * This method creates a list of time slots for reservation for a particular day
     * @param date A String representing a particular day
     * @return An arrayList of time slots
     */
    private List<String> Times(String date) {
        List<String> times = new ArrayList<>();
        LocalTime start;
        LocalTime end;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate datetday = LocalDate.now();
        String today = datetday.format(format);
        if (date.equals(today) && LocalTime.now().getHour() > 10) {
            LocalTime timeNow = LocalTime.now();
            if (LocalTime.now().getHour() > 17) {
                return new ArrayList<String>();
            }
            int minute = LocalTime.now().getMinute();
            if (minute > 30) {
                timeNow = timeNow.plusHours(1);
                minute = 0;
            } else if (minute > 0) {
                minute = 30;
            }
            start = LocalTime.of(timeNow.getHour(), minute);
            end = LocalTime.of(17, 0);
        } else {
            start = LocalTime.of(10, 0);
            end = LocalTime.of(17, 0);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        for (LocalTime t = start; !t.isAfter(end); t = t.plusMinutes(30)) {
            times.add(t.format(dtf));
        }
        return times;
    }
    /**
     * Thread that is created when a user connects
     * This allows multiple users to connect at the same time
     *
     * Notes for client & UI:
     * This will start with a simple login page where the client enters
     * their email and password, or they have the option to create an account.
     * If they create a new account, it will prompt them to enter an email
     * and a password (email has to be distinct from the other users)
     * For this part, the client will communicate with the server, returning
     * these strings:
     * 1. OPTION_CREATE_ACCOUNT - user chose option create account
     * 2. email password - in the form [email password] the user is trying to login
     *
     * the server will communicate back with the client, returning
     * these strings:
     * 1. WRONG_CREDENTIALS - wrong credentials
     * 2. RIGHT_CREDENTIALS - right credentials
     * 3. OPTION_CREATE_ACCOUNT - user chose option create account (ready for the client
     * to type account stuff)
     *
     * If the client recieves OPTION_CREATE_ACCOUNT, it should send:
     * 1. BACK - go back to regular login
     * 2. email password - in the form [email password] the user is trying to create
     * an email + password
     *
     * Thus, the server will send back 2 things:
     * 1. BACK - tells the client to go back
     * 2. INVALID_CREATE_ACCOUNT can't create account (email already exists)
     * 3. SUCCESS_CREATE_ACCOUNT successfully created account, proceed to next phase
     *
     * The next phase will be on the user making another booking or removing others
     * The server will sent the client Bookings in the form of [bookingTime,partySize,reservationID] followed
     * by a space for each one.
     *
     *
     */
    public void run() {
        try {
            DatabaseManager dm = new DatabaseManager();
            dm.load();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            String line = reader.readLine();
            while (line != null) {
                try {
                    dm.load();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                String[] content = line.split(" ");
                String action = content[0];

                switch(action) {
                    case "CREATE_ACCOUNT":
                        createAccount(content[1], content[2], reader, writer, dm);
                        break;
                    case "LOGIN":
                        login(content[1], content[2], reader, writer, dm);
                        break;
                    case "DELETE_ACCOUNT":
                        deleteAccount(content[1], content[2], reader, writer, dm);
                        break;
                    case "GET_BOOKINGS":
                        getBookings(content[1], writer, dm);
                        break;
                    case "MAKE_RESERVATION":
                        makeReservation(content, writer, dm);
                        break;
                    case "CANCEL_RESERVATION":
                        cancelReserve(Integer.parseInt(content[1]), writer, dm);
                        break;
                    case "GET_REALTIME_TABLES":
                        String date = content[1];
                        String time = content[2];
                        int partySize = Integer.parseInt(content[3]);
                        List<String> tables = dm.getRealTimeTables(date, time, partySize);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < tables.size(); i++) {
                            sb.append(tables.get(i));
                            if (i < tables.size() - 1) {
                                sb.append(";");
                            }
                        }
                        writer.println(sb.toString());
                        break;
                    case "GET_DATES":
                        List<String> dateList = Dates();
                        writer.println(String.join(",", dateList));
                        break;
                    case "GET_TIMES":
                        List<String> timeList = Times(content[1]);
                        writer.println(String.join(",", timeList));
                        break;
                    default:
                        writer.println("ERROR_UNKNOWN_COMMAND");
                        break;
                }
                line = reader.readLine();
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server s = new Server(null);
        s.start();
    }
}