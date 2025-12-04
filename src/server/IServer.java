package database.src.server;

import database.src.database.DatabaseManager;
import java.io.*;
import java.util.List;

/**
 * This is an interface for the Server class
 * @author Mahitha Kodali, lab sec 02
 * @version November 24, 2025
 */
public interface IServer {
    /**
     * Starts the server
     *  Doesn't end until there is a force exit (always runs), like real life servers
     */
    void start();

    /**
     * This method communicates with the client and the clients choices are all handled.
     * @param reader is a BufferedReader object that reads from the client
     * @param writer is a PrintWriter object that writes back to the client
     * @param dm is a DatabaseManager object that checks if user exists or adds user depending
     * on if they are logging in or creating an account
     * @return String array containing email, password
     */
    String[] login(BufferedReader reader, PrintWriter writer, DatabaseManager dm)
            throws IOException, ClassNotFoundException;

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
    String[] createAccount(String email, String password, BufferedReader reader,
                           PrintWriter writer, DatabaseManager dm) throws IOException;

    /**
     * This methods deletes an account with username
     * @param username the username of the user who wants to delete their account
     * @param reader a BufferedReader object to read from the client
     * @param writer A printWriter object to write back to the client
     * @param dm A DatabaseManager object used for deleting the user
     * @throws IOException from writing or saving in database
     */
    void deleteAccount(String username, BufferedReader reader, PrintWriter writer,
                              DatabaseManager dm) throws IOException;

    /**
     * This method sends back all the bookings this user has already made
     * @param email A string representing the user's email
     * @param writer A PrintWriter object that communicates with the client
     * @param dm A DatabaseManager object that finds this specific user's bookings.
     * @return A string containing all bookings
     */
    String getBookings(String email, PrintWriter writer, DatabaseManager dm);

    /**
     * This method makes a reservation
     * @param content a string array containing the details the client entered
     * @param writer A printWriter object used for communicating with the server
     * @param dm A DatabaseManager object used for adding a reservation to the database.
     */
    void makeReservation(String[] content, PrintWriter writer, DatabaseManager dm);

    /**
     * This method cancels a reservation
     * @param id the unique id given per reservation
     * @param writer A printWriter object used for communicating with the server
     * @param dm A DatabaseManager object used for cancelling a reservation.
     */
    void cancelReserve(int id, PrintWriter writer, DatabaseManager dm);

    /**
     * This method creates a list of dates for the next 3 months for the drop-down, which the user can choose from
     * @return A String arraylist containing the dates for the next 3 months.
     */
    List<String> Dates();

    /**
     * Thread that is created when a user connects
     * This allows multiple users to connect at the same time
     */
    void run();
}
