package database.src.database;

import database.src.users.IBooking;
import database.src.users.IUser;

import java.io.*;
import java.util.*;
/**
 * This class serializes and deserializes user and reservation data
 * into our file-based database and handles persistence.
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
public class FilePersistence implements IPersistence {
    private static final String USERS_DB = "users.db";
    private static final String RESERVATIONS_DB = "reservations.db";
    /**
     * This method saves the map of users to a file named "users.db"
     * using serialization
     * @param users A map containing user IDs as keys and IUser objects
     * @throws IOException Thrown if an I/O Error occurs
     */
    @Override
    public void saveUsers(Map<String, IUser> users) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_DB))) {
            out.writeObject(users);
        }
    }
    /**
     * This method saves the list of reservations to a file named "reservations.db"
     * using serialization
     *
     * @param reservations A list of IBooking objects representing reservations
     * @throws IOException Thrown if an I/O error occurs
     */
    @Override
    public void saveReservations(List<IBooking> reservations) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RESERVATIONS_DB))) {
            out.writeObject(reservations);
        }
    }
    /**
     * This method loads the map of users from "users.db" using deserialization
     *
     * @return If file doesn't exist, an empty map will be returned
     * @throws IOException Thrown if an I/O error occurs
     * @throws ClassNotFoundException Thrown if class of a serialized object can't be found
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, IUser> loadUsers() throws IOException, ClassNotFoundException {
        File f = new File(USERS_DB);
        if (!f.exists()) return new HashMap<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (Map<String, IUser>) in.readObject();
        }
    }
    /**
     * This method loads a list of reservations from "reservations.db" file using deserialization
     *
     * @return A list containing IBooking objects, or an empty list if file isn't found
     * @throws IOException Thrown if an I/O error occurs
     * @throws ClassNotFoundException Thrown if the class of a serialized object can't be found
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<IBooking> loadReservations() throws IOException, ClassNotFoundException {
        File f = new File(RESERVATIONS_DB);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (List<IBooking>) in.readObject();
        }
    }
}
