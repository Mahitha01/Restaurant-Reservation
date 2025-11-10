package database.src.database;

import database.src.users.IBooking;

import java.io.IOException;
/**
 * Interface to manage users and bookings/reservations. Implemented by DatabaseManager.java
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
public interface IDatabaseManager {
    boolean createUser(String username, String password);
    boolean deleteUser(String username);
    boolean authenticate(String username, String password);
    boolean addReservation(String username, IBooking r);
    boolean cancelReservation(int reservationId);
    void save() throws IOException;
    void load() throws IOException, ClassNotFoundException;
}
