package database;

import users.IBooking;
import users.IUser;

import java.io.*;
import java.util.*;

public class FilePersistence implements IPersistence {
    private static final String USERS_DB = "users.db";
    private static final String RESERVATIONS_DB = "reservations.db";

    @Override
    public void saveUsers(Map<String, IUser> users) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_DB))) {
            out.writeObject(users);
        }
    }

    @Override
    public void saveReservations(List<IBooking> reservations) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RESERVATIONS_DB))) {
            out.writeObject(reservations);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, IUser> loadUsers() throws IOException, ClassNotFoundException {
        File f = new File(USERS_DB);
        if (!f.exists()) return new HashMap<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (Map<String, IUser>) in.readObject();
        }
    }

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
