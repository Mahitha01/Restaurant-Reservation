package database.src.database;

import database.src.users.IBooking;
import database.src.users.IUser;

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

    @Override
    public boolean createUser(String email, String password) {
        synchronized (lock) {
            if (users.containsKey(email)) return false;

            IUser newUser = new database.src.users.User(email, password);
            users.put(email, newUser);

            return true;
        }
    }

    @Override
    public boolean deleteUser(String username) {
        synchronized (lock) {
            return users.remove(username) != null;
        }
    }

    @Override
    public boolean authenticate(String email, String password) {
        synchronized (lock) {
            IUser u = users.get(email);
            if (u == null) return false;
            return u.getPasswordHash() == password.hashCode();
        }
    }

    @Override
    public boolean addReservation(String username, IBooking r) {
        synchronized (lock) {
            reservations.add(r);
            IUser u = users.get(username);
            if (u != null) u.addReservation(r);
            return true;
        }
    }

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

    @Override
    public void save() throws IOException {
        synchronized (lock) {
            storage.saveUsers(users);
            storage.saveReservations(reservations);
        }
    }

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

