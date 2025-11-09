package database;

import java.io.*;
import java.util.*;

public class DatabaseManager implements IDatabaseManager {
    private final Map<String, IUser> users = new HashMap<>();
    private final List<IReservation> reservations = new ArrayList<>();
    private final Object lock = new Object();
    private final FilePersistence storage = new FilePersistence();

    @Override
    public boolean createUser(String username, String password) {
        synchronized (lock) {
            if (users.containsKey(username)) return false;

            // add user logic here (need user classes)
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
    public boolean authenticate(String username, String password) {
        synchronized (lock) {
            IUser u = users.get(username);
            if (u == null) return false;
            return u.getPasswordHash().equals(Integer.toHexString(password.hashCode()));
        }
    }

    @Override
    public boolean addReservation(String username, IReservation r) {
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
                IReservation res = reservations.get(i);
                if (res.getId() == reservationId) {
                    reservations.remove(i);
                    IUser u = users.get(res.getUsername());
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

