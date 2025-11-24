package database.src.database;

import database.src.users.IBooking;
import database.src.users.IUser;
import database.src.users.User;

import java.io.*;
import java.util.*;
/**
 * Manages all user and booking data and uses FilePersistence.java to store these into the database.
 * Stores data temporarily and feeds it to FilePersistence.java which stores it to the database.
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
public class DatabaseManager implements database.src.database.IDatabaseManager {
    private final Map<String, IUser> users = new HashMap<>();
    private final List<IBooking> reservations = new ArrayList<>();
    private final Object lock = new Object();
    private final FilePersistence storage = new FilePersistence();

    /**
     * This method creates a new user with the given email and adds them
     * to the database
     *
     * @param email A String representing the user's email
     * @param password A String representing the user's password
     * @return If user was successfully created and added; otherwise return false
     */
    @Override
    public boolean createUser(String email, String password) {
        synchronized (lock) {
            if (users.containsKey(email)) return false;

            IUser newUser = new User(email, password);
            users.put(email, newUser);

            return true;
        }
    }

    /**
     * This method deletes a user from the database based on their username
     * @param username A String representing the email/username of the user to be deleted
     * @return If user was successfully deleted; otherwise return false
     */
    @Override
    public boolean deleteUser(String username) {
        synchronized (lock) {
            return users.remove(username) != null;
        }
    }
    
    /**
     * This method authenticates a user by checking their email and password
     * @param email A String representing the email/username of the user
     * @param password A String representing the password of the user
     * @return If user exists & the password matches; otherwise return false
     */
    @Override
    public boolean authenticate(String email, String password) {
        synchronized (lock) {
            IUser u = users.get(email);
            if (u == null) return false;
            return u.getPasswordHash() == password.hashCode();
        }
    }

    /**
     * This method adds the reservation to the reservations list
     * and the user's reservation list
     * @param username A String representing the username of the user
     * @param r An IBooking object representing the reservation
     * @return If reservation is successfully added
     */
    @Override
    public boolean addReservation(String username, IBooking r) {
        synchronized (lock) {
            reservations.add(r);
            IUser u = users.get(username);
            if (u != null) u.addReservation(r);
            return true;
        }
    }

    /**
     * This method returns a specific user's reservations
     * @param email A string representing the user's email
     * @return an Arraylist of all the reservations or an empty list if none exist
     */
    @Override
    public List<IBooking> getUserBookings(String email) {
        IUser user = users.get(email);
        if (user == null) {
            return new ArrayList<>();
        }
        return user.getReservations();
    }
    /**
     * This method  cancels and removes a reservation by the provided ID
     * @param reservationId An integer representing the ID of the reservation
     * @return If the reservation was found and successfully cancelled; otherwise returns false
     */
    @Override
    public boolean cancelReservation(int reservationId) {
        synchronized (lock) {
            for (int i = 0; i < reservations.size(); i++) {
                IBooking res = reservations.get(i);
                if (res.getId() == reservationId) {
                    reservations.remove(i);
                    IUser u = users.get(res.getUserEmail());
                    if (u != null) u.cancelReservation(reservationId);
                    return true;
                }
            }
            return false;
        }
    }
    /**
     * This method saves the current state of users and reservations to files
     * @throws IOException thrown if an I/O error occurs
     */
    @Override
    public void save() throws IOException {
        synchronized (lock) {
            storage.saveUsers(users);
            storage.saveReservations(reservations);
        }
    }
    /**
     * This method loads the stored user and reservation data from files
     * @throws IOException Thrown if an I/O error occurs
     * @throws ClassNotFoundException Thrown if the class of a serialized object can't be found
     */
    @Override
    public void load() throws IOException, ClassNotFoundException {
        synchronized (lock) {
            users.clear();
            reservations.clear();
            users.putAll(storage.loadUsers());
            reservations.addAll(storage.loadReservations());
        }
    }
}

