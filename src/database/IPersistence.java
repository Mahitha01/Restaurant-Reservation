package database.src.database;

import database.src.users.IBooking;
import database.src.users.IUser;

import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * Interface for to save users and reservation data. Implemented by FilePersistence.java
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
interface IPersistence {
    void saveUsers(Map<String, IUser> users) throws IOException;
    void saveReservations(List<IBooking> reservations) throws IOException;
    Map<String, IUser> loadUsers() throws IOException, ClassNotFoundException;
    List<IBooking> loadReservations() throws IOException, ClassNotFoundException;
}

