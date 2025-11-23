package database.src.server;

import java.io.*;
import java.net.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import database.src.database1.DatabaseManager;
import database.src.users.IBooking;

/**
 * Server side for program, gets user information and calls database to store
 * information
 * 
 * @author Raphie Lubiniecki, lab sec 02
 * @version November 17th, 2025
 */

public class Server implements Runnable {
    private static int baseId = 0;
    private Socket socket;
    private static Object lock = new Object();

    /**
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
    public String[] login(String email, String password, BufferedReader reader, PrintWriter writer, DatabaseManager dm) throws IOException, ClassNotFoundException{
        String[] userDetails = new String[0];
        if(dm.authenticate(email, password)) {
            userDetails = new String[]{email, password};
            writer.println("RIGHT_CREDENTIALS");
            writer.flush();
        } else {
            writer.println("WRONG_CREDENTIALS");
            writer.flush();
        }
        return userDetails;
    }

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

    public void deleteAccount(String username, BufferedReader reader, PrintWriter writer,
                              DatabaseManager dm) throws IOException {
        if (dm.deleteUser(username)) {
            writer.println("DELETED_SUCCESSFULLY");
            writer.flush();
            dm.save();
        } else {
            writer.println("DELETE_FAILED");
            writer.flush();
        }
    }
    /**
     * This method manages the bookings of the user who is logged in.
     * @param reader BufferedReader object that reads from the client
     * @param writer PrintWriter object that writes back to the client
     * @param dm DatabaseManager object to update the user's data
     * @param userEmailPassword the String array containing the user's information
     */
    //@Override
    public void reserve(BufferedReader reader, PrintWriter writer, DatabaseManager dm, String[] userEmailPassword) throws IOException{
        List<IBooking> bookings = dm.getUserBookings(userEmailPassword[0]);
        String totalBookings = "";
        for(IBooking booking : bookings) {
            totalBookings += String.format("%s,%d,%d ", booking.getBookingTime(), booking.getPartySize(), booking.getId());
        }
        writer.println(totalBookings); // all of the reservations
        writer.flush();

        while(true) {
            String choice = reader.readLine();
            if(choice.equals("EXIT")) break;
            if(choice.equals("ADD_RESERVATION")) {
                writer.println("ADD_RESERVATION"); // returns so the user can then input their stuff
                writer.flush();

            }
            String[] data = choice.split("-");
            if(data[0].equals("CANCEL_RESERVATION")) {
                String[] reservationToRemove = data[1].split(",");
                boolean successfulCancel = dm.cancelReservation(Integer.parseInt(reservationToRemove[2]));
                dm.save();
                bookings = dm.getUserBookings(userEmailPassword[0]);
                for(IBooking booking:bookings) {
                    totalBookings += String.format("%s,%d,%d ", booking.getBookingTime(), booking.getPartySize(), booking.getId());
                }
                String toSendBack = "";
                if(successfulCancel) toSendBack += "SUCCESS-";
                else toSendBack += "FAIL-";
                toSendBack += totalBookings;
                writer.println(toSendBack);
                writer.flush();
            }
        }
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
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String line = reader.readLine();
            while (line != null) {
                String[] content = line.split(" ");
                String action = content[0];
                String[] userDetails = new String[0];

                switch(action) {
                    case "CREATE_ACCOUNT":
                        userDetails = createAccount(content[1], content[2], reader, writer, dm);
                        break;
                    case "LOGIN":
                        userDetails = login(content[1], content[2], reader, writer, dm);
                        break;
                    case "DELETE_ACCOUNT":
                        deleteAccount(content[1], reader, writer, dm);
                        break;
                    case "RESERVE":
                        reserve(reader, writer, dm, userDetails);
                        break;
                }
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
