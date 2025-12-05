package database.src.database;

import database.src.users.IBooking;
import database.src.users.IUser;
import database.src.users.User;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final Map<String, Map<String, ArrayList<String>>> tablesPerDay = new HashMap<>();
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
     * This method adds a day and time with all the tables
     * @param date A String in the format of mm,dd,time
     * @param time A String in the format of HH:mm
     */
    public void addDay(String date, String time) {
        synchronized (lock) {
            ArrayList<String> tables = new ArrayList<>();
            int capacity = 2;
            for (int x = 1; x <= 20; x++) {
                tables.add(x + "," + capacity + ",true");
                if (x % 4 == 0) {
                    capacity += 2;
                }
            }
            if (!tablesPerDay.containsKey(date)) {
                tablesPerDay.put(date, new HashMap<>());
            }

            tablesPerDay.get(date).put(time, tables);
        }
    }


    /**
     * This method checks the availability of a specific table
     * @param day A String representing the day
     * @param time A String representing the time
     * @param target An integer representing the table number wanted
     * @return true if table is available and false if isn't
     */
    public boolean isAvailable(String day, String time, int target) { // {3,4,true}
        synchronized (lock) {
            ArrayList<String> tables = tablesPerDay.get(day).get(time);
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
     * @param day A String representing the day
     * @param time A string representing the time
     * @param partySize An integer containing the number of people
     * @return An arrayList that contains the available tables that can hold the partySize
     */
    public ArrayList<String> getAvailTables(String day, String time, int partySize) {
        synchronized (lock) {
            ArrayList<String> result = new ArrayList<>();
            ArrayList<String> tables = tablesPerDay.get(day).get(time);

            for (String table : tables) {
                String[] data = table.split(",");
                int tableSize = Integer.parseInt(data[1]);
                if ((tableSize >= partySize) && Boolean.parseBoolean(data[2])) {
                    result.add(data[0] + "," + data[1]);
                }
            }
            return result;
        }
    }

    /**
     * Get all tables for a day and time
     * @param day A String representing the day
     * @param time A String representing the time
     * @param partySize A String representing the partySize
     * @return all tables available at that particular day and time
     */
    public ArrayList<String> getRealTimeTables(String day, String time, int partySize) {
        synchronized(lock) {
            if (!tablesPerDay.containsKey(day)) {
                tablesPerDay.put(day, new HashMap<>());
            }
            if (!(tablesPerDay.get(day).containsKey(time))) {
                addDay(day, time);
            }

            ArrayList<String> tables = tablesPerDay.get(day).get(time);
            if (tables == null) {
                return new ArrayList<>();
            }

            return getAvailTables(day, time, partySize);
        }
    }

    /**
     * This method allows the user to occupy a table
     * @param day A String representing the day
     * @param time A String representing the time
     * @param tableNum An int representing the table number
     * @return true of the table is occupied
     */
    public boolean occupyTable(String day, String time, int tableNum) {
        synchronized(lock) {
            ArrayList<String> tables = tablesPerDay.get(day).get(time);
            if (tables == null) {
                return false;
            }
            for (int i = 0; i < tables.size(); i++) {
                String[] parts = tables.get(i).split(",");
                int num = Integer.parseInt(parts[0]);
                boolean available = Boolean.parseBoolean(parts[2]);
                if (num == tableNum && available) {
                    parts[2] = "false";
                    tables.set(i, String.join(",", parts));
                    saveTablesNow();
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * This method frees a table from a reservation, changes the availability of the table back to true
     * for the particular day and time
     * @param day A String representing the day
     * @param time A String representing the time slot booked for
     * @param tableNum The table number booked or trying to be freed
     * @return Returns true if successfully freed the table
     */
    public boolean freeTable(String day, String time, int tableNum) {
        synchronized (lock) {
            ArrayList<String> tables = tablesPerDay.get(day).get(time);
            if (tables == null) {
                return false;
            }
            for (int i = 0; i < tables.size(); i++) {
                String[] parts = tables.get(i).split(",");
                int num = Integer.parseInt(parts[0]);
                if (num == tableNum) {
                    parts[2] = "true";
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
            if (u != null) {
                u.addReservation(r);
                return true;
            }
            return false;
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

                    String bookingTime = res.getBookingTime();
                    String[] parts = bookingTime.split(" ");
                    String date = parts[0];
                    String time = parts[1];
                    freeTable(date, time, res.getTableNum());

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
                tablesPerDay.putAll((Map<String, Map<String, ArrayList<String>>>) ois.readObject());
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
