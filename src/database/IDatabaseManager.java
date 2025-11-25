package database.src.database;

import database.src.users.IBooking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface to manage users and bookings/reservations. Implemented by DatabaseManager.java
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
public interface IDatabaseManager {

    /**
     * This method creates a new user with the given email and adds them
     * to the database
     *
     * @param username A String representing the user's email
     * @param password A String representing the user's password
     * @return If user was successfully created and added; otherwise return false
     */
    boolean createUser(String username, String password);

    /**
     * This method adds a day with all the available tables
     * @param date A String in the format of mm,dd,time
     * @param tables An arraylist containing the available tables
     */
    void addDay(String date, ArrayList<String> tables);

    /**
     * This method returns the available tables for a particular day that can hold the number of people attending.
     * @param day A String containing the day wanted.
     * @param partySize An int holding the amount of people attending
     * @return An arrayList that contains the available tables that can hold the partySize
     */
    ArrayList<String> getAvailTables(String day, int partySize);


    /**
     * This method checks the availability of a specific table
     * @param day A String representing the day
     * @param target An integer representing the table number wanted
     * @return true if table is available and false if isn't
     */
    boolean isAvailable(String day, int target);

    /**
     * This method deletes a user from the database based on their username
     * @param username A String representing the email/username of the user to be deleted
     * @return If user was successfully deleted; otherwise return false
     */
    boolean deleteUser(String username);

    /**
     * This method authenticates a user by checking their email and password
     * @param username A String representing the email/username of the user
     * @param password A String representing the password of the user
     * @return If user exists & the password matches; otherwise return false
     */
    boolean authenticate(String username, String password);

    /**
     * This method adds the reservation to the reservations list
     * and the user's reservation list
     * @param username A String representing the username of the user
     * @param r An IBooking object representing the reservation
     * @return If reservation is successfully added
     */
    boolean addReservation(String username, IBooking r);

    /**
     * This method returns a specific user's reservations
     * @param email A string representing the user's email
     * @return an Arraylist of all the reservations or an empty list if none exist
     */
    List<IBooking> getUserBookings(String email);

    /**
     * This method  cancels and removes a reservation by the provided ID
     * @param reservationId An integer representing the ID of the reservation
     * @return If the reservation was found and successfully cancelled; otherwise returns false
     */
    boolean cancelReservation(int reservationId);

    /**
     * This method saves the current state of users and reservations to files
     * @throws IOException thrown if an I/O error occurs
     */
    void save() throws IOException;

    /**
     * This method loads the stored user and reservation data from files
     * @throws IOException Thrown if an I/O error occurs
     * @throws ClassNotFoundException Thrown if the class of a serialized object can't be found
     */
    void load() throws IOException, ClassNotFoundException;
}
