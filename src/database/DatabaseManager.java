package database.src.database;

import database.src.users.IBooking;
import database.src.users.IUser;
import database.src.users.User;

import java.io.*;
import java.util.*;
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
    private final Map<String, String[]> hoursPerDay = new HashMap<>();
    private final Map<String, Map<String, Set<Integer>>> lockedTables = new HashMap<>();
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
            if (!tablesPerDay.containsKey(date)) {
                tablesPerDay.put(date, new HashMap<>());
            }

            ArrayList<String> tables = new ArrayList<>();
            int capacity = 2;
            for (int x = 1; x <= 20; x++) {
                tables.add(x + "," + capacity + ",true");
                if (x % 4 == 0) {
                    capacity += 2;
                }
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
            if (!isWithinHours(day, time)) return false;

            Map<String, ArrayList<String>> perDay = tablesPerDay.get(day);
            if (perDay == null) return false;
            ArrayList<String> tables = perDay.get(time);
            if (tables == null) {
                return false;
            }
            for (String table : tables) {
                String[] line = table.split(",");
                int tableNumber = Integer.parseInt(line[0]);
                if (tableNumber == target && Boolean.parseBoolean(line[2]) && !isTableLocked(day, time, target)) {
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
            Map<String, ArrayList<String>> perDay = tablesPerDay.get(day);
            if (perDay == null) return result;
            ArrayList<String> tables = perDay.get(time);
            if (tables == null) return result;

            for (String table : tables) {
                String[] data = table.split(",");
                int tableSize = Integer.parseInt(data[1]);
                int tableNum = Integer.parseInt(data[0]);
                if ((tableSize >= partySize) && Boolean.parseBoolean(data[2]) && !isTableLocked(day, time, tableNum)) {
                    result.add(data[0] + "," + data[1]);
                }
            }
            return result;
        }
    }

    /**
     * This method returns all available tables at a particular time of a particular day
     * @param day A String representing the date
     * @param time A String representing the time
     * @return An arraylist of tables numbers and capacity that are available in the format "tableNumber,capacity"
     */
    public ArrayList<String> getAllAvailTables(String day, String time) {
        synchronized (lock) {
            ArrayList<String> result = new ArrayList<>();
            Map<String, ArrayList<String>> perDay = tablesPerDay.get(day);
            if (perDay == null) return result;
            ArrayList<String> tables = perDay.get(time);
            if (tables == null) return result;

            for (String table : tables) {
                String[] data = table.split(",");
                int tableNum = Integer.parseInt(data[0]);
                if (Boolean.parseBoolean(data[2]) && !isTableLocked(day, time, tableNum)) {
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
     * @param partySize An int representing the party size (0 returns all available tables)
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
            if (partySize == 0) {
                return getAllAvailTables(day, time);
            }

            return getAvailTables(day, time, partySize);
        }
    }

    /**
     * This method allows the user to occupy a table
     * @param day A String representing the day
     * @param time A String representing the time
     * @param tableNum An int representing the table number
     */
     public void occupyTable(String day, String time, int tableNum) {
        synchronized(lock) {
            if (!isWithinHours(day, time)) return;
            if (isTableLocked(day, time, tableNum)) return;

            Map<String, ArrayList<String>> perDay = tablesPerDay.get(day);
            if (perDay == null) return;
            ArrayList<String> tables = perDay.get(time);
            if (tables == null) {
                return;
            }
            for (int i = 0; i < tables.size(); i++) {
                String[] parts = tables.get(i).split(",");
                int num = Integer.parseInt(parts[0]);
                boolean available = Boolean.parseBoolean(parts[2]);
                if (num == tableNum && available) {
                    parts[2] = "false";
                    tables.set(i, String.join(",", parts));
                    saveTablesNow();
                    return;
                }
            }
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
            Map<String, ArrayList<String>> perDay = tablesPerDay.get(day);
            if (perDay == null) return false;
            ArrayList<String> tables = perDay.get(time);
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
     * Set seating arrangement for a specific day/time. If tableCapacities is empty or null,
     * the default layout (20 tables with increasing capacity every 4 tables) is used.
     *
     * @param day the date string identifying the day
     * @param time the time slot (HH:mm)
     * @param tableCapacities a map from table number to capacity; if null or empty default layout is applied
     */
    public void setSeatingArrangement(String day, String time, Map<Integer, Integer> tableCapacities) {
        synchronized (lock) {
            if (!tablesPerDay.containsKey(day)) tablesPerDay.put(day, new HashMap<>());

            ArrayList<String> tables = new ArrayList<>();
            int maxTable = 20;
            if (tableCapacities != null && !tableCapacities.isEmpty()) {
                for (Integer k : tableCapacities.keySet()) if (k != null && k > maxTable) maxTable = k;
            }

            for (int x = 1; x <= maxTable; x++) {
                int capacity;
                if (tableCapacities != null && tableCapacities.containsKey(x)) {
                    capacity = tableCapacities.get(x);
                } else {
                    int baseGroup = (x - 1) / 4;
                    capacity = 2 + baseGroup * 2;
                }
                tables.add(x + "," + capacity + ",true");
            }

            tablesPerDay.get(day).put(time, tables);
            saveTablesNow();
        }
    }

    /**
     * Lock specific table numbers for a day/time.
     *
     * @param day the date string identifying the day
     * @param time the time slot (HH:mm) for which to lock tables
     * @param tableNumbers a collection of table numbers to lock
     */
    public void lockTables(String day, String time, Collection<Integer> tableNumbers) {
        synchronized (lock) {
            lockedTables.computeIfAbsent(day, k -> new HashMap<>())
                    .computeIfAbsent(time, k -> new HashSet<>())
                    .addAll(tableNumbers);
            saveTablesNow();
        }
    }

    /**
     * Unlock specific tables for a day/time.
     *
     * @param day the date string identifying the day
     * @param time the time slot (HH:mm) for which to unlock tables
     * @param tableNumbers a collection of table numbers to unlock
     */
    public void unlockTables(String day, String time, Collection<Integer> tableNumbers) {
        synchronized (lock) {
            Map<String, Set<Integer>> perDay = lockedTables.get(day);
            if (perDay == null) return;
            Set<Integer> set = perDay.get(time);
            if (set == null) return;
            set.removeAll(tableNumbers);
            if (set.isEmpty()) perDay.remove(time);
            if (perDay.isEmpty()) lockedTables.remove(day);
            saveTablesNow();
        }
    }

    /**
     * Lock a section of tables inclusive [startTable, endTable].
     *
     * @param day the date string identifying the day
     * @param time the time slot (HH:mm) for which to lock the section
     * @param startTable the starting table number (inclusive)
     * @param endTable the ending table number (inclusive)
     */
    public void lockSection(String day, String time, int startTable, int endTable) {
        synchronized (lock) {
            if (startTable > endTable) {
                int tmp = startTable; startTable = endTable; endTable = tmp;
            }
            Set<Integer> toLock = new HashSet<>();
            for (int i = startTable; i <= endTable; i++) toLock.add(i);
            lockTables(day, time, toLock);
        }
    }

    private boolean isTableLocked(String day, String time, int tableNum) {
        Map<String, Set<Integer>> perDay = lockedTables.get(day);
        if (perDay == null) return false;
        Set<Integer> s = perDay.get(time);
        return s != null && s.contains(tableNum);
    }

    /**
     * Set operating hours for a specific day. Times should be in HH:mm format.
     *
     * @param day the date string identifying the day
     * @param openHHmm opening time in HH:mm format
     * @param closeHHmm closing time in HH:mm format
     */
    public void setHours(String day, String openHHmm, String closeHHmm) {
        synchronized (lock) {
            hoursPerDay.put(day, new String[]{openHHmm, closeHHmm});
            saveTablesNow();
        }
    }

    /**
     * Get the operating hours for a specific day.
     *
     * @param day the date string identifying the day
     * @return an Optional containing a String[2] with open and close times (HH:mm), or empty if not set
     */
    public Optional<String[]> getHours(String day) {
        synchronized (lock) {
            return Optional.ofNullable(hoursPerDay.get(day));
        }
    }

    /**
     * Check whether a given time is within the configured operating hours for a day.
     * If no hours are configured for the day, this returns true.
     *
     * @param day the date string identifying the day
     * @param timeStr the time to check (HH:mm)
     * @return true if the time is within hours or hours are not configured; false otherwise
     */
    private boolean isWithinHours(String day, String timeStr) {
        synchronized (lock) {
            String[] hours = hoursPerDay.get(day);
            if (hours == null) return true;
            try {
                LocalTime t = LocalTime.parse(timeStr);
                LocalTime open = LocalTime.parse(hours[0]);
                LocalTime close = LocalTime.parse(hours[1]);
                if (close.isBefore(open)) {
                    return !t.isBefore(open) || !t.isAfter(close);
                } else {
                    return !t.isBefore(open) && !t.isAfter(close);
                }
            } catch (Exception ex) {
                return true;
            }
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

    /**
     * Save the tables/hours/locks state to the tables DB file.
     *
     * @throws IOException if an I/O error occurs while writing the file
     */
    public void saveTables() throws IOException {
        synchronized (lock) {
            TablesState st = new TablesState();
            st.tablesPerDay = this.tablesPerDay;
            st.hoursPerDay = this.hoursPerDay;
            st.lockedTables = this.lockedTables;
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TABLES_DB))) {
                oos.writeObject(st);
            }
        }
    }

    /**
     * Load the tables/hours/locks state from the tables DB file if it exists.
     *
     * @throws IOException if an I/O error occurs while reading the file
     * @throws ClassNotFoundException if a serialized class cannot be found
     */
    public void loadTables() throws IOException, ClassNotFoundException {
        synchronized(lock) {
            File f = new File(TABLES_DB);
            if (!f.exists()) {
                return;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                Object obj = ois.readObject();
                if (obj instanceof TablesState) {
                    TablesState st = (TablesState) obj;
                    this.tablesPerDay.clear();
                    this.tablesPerDay.putAll(st.tablesPerDay != null ? st.tablesPerDay : new HashMap<>());
                    this.hoursPerDay.clear();
                    this.hoursPerDay.putAll(st.hoursPerDay != null ? st.hoursPerDay : new HashMap<>());
                    this.lockedTables.clear();
                    this.lockedTables.putAll(st.lockedTables != null ? st.lockedTables : new HashMap<>());
                } else if (obj instanceof Map) {
                    this.tablesPerDay.clear();
                    this.tablesPerDay.putAll((Map<String, Map<String, ArrayList<String>>>) obj);
                }
            }
        }
    }

    public void saveTablesNow() {
        try {
            saveTables();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Serializable container for tables + hours + locks. */
    private static class TablesState implements Serializable {
        private static final long serialVersionUID = 1L;
        public Map<String, Map<String, ArrayList<String>>> tablesPerDay;
        public Map<String, String[]> hoursPerDay;
        public Map<String, Map<String, Set<Integer>>> lockedTables;
    }
}
