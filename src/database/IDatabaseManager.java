package database.src.database;

import database.src.users.IBooking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * @param time A String in the format of HH:mm
     */
    void addDay(String date, String time);

    /**
     * This method returns all tables that can hold more that the partySize
     * @param day A String representing the day
     * @param time A string representing the time
     * @param partySize An integer containing the number of people
     * @return An arrayList that contains the available tables that can hold the partySize
     */
    ArrayList<String> getAvailTables(String day, String time, int partySize);


    /**
     * This method checks the availability of a specific table
     * @param day A String representing the day
     * @param time A String representing the time
     * @param target An integer representing the table number wanted
     * @return true if table is available and false if isn't
     */
    boolean isAvailable(String day, String time, int target);

    /**
     * Get all tables for a day and time
     * @param day A String representing the day
     * @param time A String representing the time
     * @param partySize A String representing the partySize
     * @return all tables available at that particular day and time
     */
    ArrayList<String> getRealTimeTables(String day, String time, int partySize);

    /**
     * This method frees a table from a reservation, changes the availability of the table back to true
     * for the particular day and time
     * @param day A String representing the day
     * @param time A String representing the time slot booked for
     * @param tableNum The table number booked or trying to be freed
     * @return Returns true if successfully freed the table
     */
    boolean freeTable(String day, String time, int tableNum);

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

    // --- Management features ---
    /** Set seating arrangement for a specific day/time. */
    void setSeatingArrangement(String day, String time, Map<Integer, Integer> tableCapacities);

    /** Lock specific table numbers for a day/time. */
    void lockTables(String day, String time, Collection<Integer> tableNumbers);

    /** Unlock specific tables for a day/time. */
    void unlockTables(String day, String time, Collection<Integer> tableNumbers);

    /** Lock a section of tables inclusive [startTable, endTable] */
    void lockSection(String day, String time, int startTable, int endTable);

    /** Set operating hours for a specific day in HH:mm format. */
    void setHours(String day, String openHHmm, String closeHHmm);

    /** Get operating hours for a day if set. */
    Optional<String[]> getHours(String day);
}
