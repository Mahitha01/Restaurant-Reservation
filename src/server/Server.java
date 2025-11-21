package database.src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import database.src.database1.DatabaseManager;
import database.src.users.Booking;
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
                System.out.println("Fount client! "+clientSocket.getLocalAddress());
                Thread clientThread = new Thread(new Server(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            // all of this for the login section
            String[] userEmailPassword;
            loginLoop: while(true) {
                String loginOptions = reader.readLine(); 
                if(loginOptions.equals("OPTION_CREATE_ACCOUNT")) {
                    writer.println("OPTION_CREATE_ACCOUNT");
                    writer.flush();
                    while(true) {
                        String createAccountOptions = reader.readLine();
                        if(createAccountOptions.equals("BACK")) {
                            writer.println("BACK");
                            writer.flush();
                            continue loginLoop;
                        }
                        String[] emailPassword = createAccountOptions.split(" ");
                        dm.load();
                        if(dm.createUser(emailPassword[0], emailPassword[1])) {
                            writer.println("SUCCESS_CREATE_ACCOUNT");
                            writer.flush();
                            dm.save();
                            userEmailPassword = emailPassword;
                            break loginLoop;
                        }
                        writer.println("INVALID_CREATE_ACCOUNT");
                        writer.flush();
                    }
                }
                String[] emailPassword = loginOptions.split(" ");
                if(dm.authenticate(emailPassword[0], emailPassword[1])) {
                    userEmailPassword = emailPassword;
                    writer.println("RIGHT_CREDENTIALS");
                    writer.flush();
                    break;
                } else {
                    writer.println("WRONG_CREDENTIALS");
                    writer.flush();
                }
            }
            // end login section

            // begin reservation section
            List<IBooking> bookings = dm.getUserBookings(userEmailPassword[0]);
            String totalBookings = "";
            for(IBooking booking:bookings) {
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

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
