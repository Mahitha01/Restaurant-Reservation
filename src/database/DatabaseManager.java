package database.src.database;

import database.src.users.IBooking;
import database.src.users.IUser;
import database.src.users.User;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
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
    private final Map<String, ArrayList<String>> tablesPerDay = new HashMap<>();
    private static final String TABLES_DB = "tables.db";
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
     * This method adds a day with all the available tables
     * @param date A String in the format of mm,dd,time
     * @param tables An arraylist containing the available tables
     */
    public void addDay(String date, ArrayList<String> tables) {
        synchronized (lock) {
            tablesPerDay.put(date, tables);
        }
    }

    /**
     * This method returns the available tables for a particular day
     * @param day A String containing the day wanted.
     * @return An arrayList of tables available
     */
    public ArrayList<String> getAvailTables(String day) {
        synchronized (lock) {
            return tablesPerDay.get(day);
        }
    }

    /**
     * This method checks the availability of a specific table
     * @param day A String representing the day
     * @param target An integer representing the table number wanted
     * @return true if table is available and false if isn't
     */
    public boolean isAvailable(String day, int target) { // {3,4,true}
        synchronized (lock) {
            ArrayList<String> tables = tablesPerDay.get(day);
            if (tables == null) {
                return false;
            }
            for (String table : tables) {
                String[] line = table.split(",");
                int tableNumber = Integer.parseInt(line[0]);
                if (tableNumber == target && Boolean.parseBoolean(line[2])) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * This method returns all tables that can hold more that the partySize
     * @param partySize An integer containing the number of people
     * @return An arrayList that contains the available tables that can hold the partySize
     */
    public ArrayList<String> getAvailTables(String day, int partySize) {
        synchronized (lock) {
            ArrayList<String> result = new ArrayList<>();
            ArrayList<String> tables = tablesPerDay.get(day);
            if (tables == null) {
                return result;
            }

            for (String table : tables) {
                String[] data = table.split(",");
                int tableSize = Integer.parseInt(data[1]);
                if ((tableSize >= partySize) && Boolean.parseBoolean(data[2])) {
                    result.add(table);
                }
            }
            return result;
        }
    }

    /**
     * Get all tables for a day
     * @param day A String representing all tables
     * @return
     */
    public ArrayList<String> getRealTimeTables(String day) {
        synchronized(lock) {
            return tablesPerDay.getOrDefault(day, new ArrayList<>());
        }
    }

    /**
     * This method allows the user to occupy a table
     * @param day A String representing the day
     * @param tableNum An int representing the table number
     * @param customer A String representing the user
     * @return
     */
    public boolean occupyTable(String day, int tableNum, String customer) {
        synchronized(lock) {
            ArrayList<String> tables = tablesPerDay.get(day);
            if (tables == null) {
                return false;
            }
            for (int i = 0; i < tables.size(); i++) {
                String[] parts = tables.get(i).split(",");
                int num = Integer.parseInt(parts[0]);
                boolean available = Boolean.parseBoolean(parts[2]);
                if (num == tableNum && available) {
                    parts[2] = "false";
                    parts[3] = customer;
                    parts[4] = LocalDateTime.now().toString();
                    tables.set(i, String.join(",", parts));
                    saveTablesNow();
                    return true;
                }
            }
            return false;
        }
    }

    public boolean freeTable(String day, int tableNum) {
        synchronized (lock) {
            ArrayList<String> tables = tablesPerDay.get(day);
            if (tables == null) {
                return false;
            }
            for (int i = 0; i < tables.size(); i++) {
                String[] parts = tables.get(i).split(",");
                int num = Integer.parseInt(parts[0]);
                if (num == tableNum) {
                    parts[2] = "true";
                    parts[3] = "";
                    parts[4] = LocalDateTime.now().toString();
                    tables.set(i, String.join(",", parts));
                    saveTablesNow();
                    return true;
                }
            }
            return false;
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
        synchronized (lock) {
            IUser user = users.get(email);
            if (user == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(user.getReservations());
        }
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
            saveTables();
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
            loadTables();
        }
    }

    public void saveTables() throws IOException {
        synchronized (lock) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TABLES_DB))) {
                oos.writeObject(tablesPerDay);
            }
        }
    }

    public void loadTables() throws IOException, ClassNotFoundException {
        synchronized(lock) {
            File f = new File(TABLES_DB);
            if (!f.exists()) {
                return;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                tablesPerDay.putAll((Map<String, ArrayList<String>>) ois.readObject());
            }
        }
    }

    public void saveTablesNow() {
        try {
            saveTables();;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
