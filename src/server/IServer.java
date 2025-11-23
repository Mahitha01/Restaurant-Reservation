package database.src.server;

import database.src.database1.DatabaseManager;
import java.io.*;

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
    String[] login(BufferedReader reader, PrintWriter writer, DatabaseManager dm) throws IOException, ClassNotFoundException;

    /**
     * This method manages the bookings of the user who is logged in.
     * @param reader BufferedReader object that reads from the client
     * @param writer PrintWriter object that writes back to the client
     * @param dm DatabaseManager object to update the user's data
     * @param userEmailPassword the String array containing the user's information
     */
    void reserve(BufferedReader reader, PrintWriter writer, DatabaseManager dm, String[] userEmailPassword) throws IOException;

    /**
     * Thread that is created when a user connects
     * This allows multiple users to connect at the same time
     */
    void run();
}
