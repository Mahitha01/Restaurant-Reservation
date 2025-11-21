package database.src.database1;

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
    /**
     * This method saves the map of users to a file named "users.db"
     * using serialization
     * @param users A map containing user IDs as keys and IUser objects
     * @throws IOException Thrown if an I/O Error occurs
     */
    void saveUsers(Map<String, IUser> users) throws IOException;

    /**
     * This method saves the list of reservations to a file named "reservations.db"
     * using serialization
     *
     * @param reservations A list of IBooking objects representing reservations
     * @throws IOException Thrown if an I/O error occurs
     */
    void saveReservations(List<IBooking> reservations) throws IOException;

    /**
     * This method loads the map of users from "users.db" using deserialization
     *
     * @return If file doesn't exist, an empty map will be returned
     * @throws IOException Thrown if an I/O error occurs
     * @throws ClassNotFoundException Thrown if class of a serialized object can't be found
     */
    Map<String, IUser> loadUsers() throws IOException, ClassNotFoundException;

    /**
     * This method loads a list of reservations from "reservations.db" file using deserialization
     *
     * @return A list containing IBooking objects, or an empty list if file isn't found
     * @throws IOException Thrown if an I/O error occurs
     * @throws ClassNotFoundException Thrown if the class of a serialized object can't be found
     */
    List<IBooking> loadReservations() throws IOException, ClassNotFoundException;
}

