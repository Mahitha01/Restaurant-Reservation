package database.src.users;
import java.io.*;
import java.util.ArrayList;
/**
 * This class stores a user's booking with details such as number of people,
 * booking time, and booking ID.
 *
 * @author Tommy, lab sec 02
 * @version November 11, 2025
 */
public class Booking extends User implements IBooking {
    private int partySize;
    private String bookingTime;
    private ArrayList<String> tables; //in format of #table number, #table Size, #available or not
    private boolean isAvailable;
    private int id; //unique id
    private static int nextId = 1;
    private static Object lock = new Object();

    /**
     * This constructor initializes all the parameters and increments id which represents a unique id per Booking
     * @param email A String representing the users email
     * @param password A String representing the users password
     * @param partySize An integer containing the number of people in the reservation
     * @param bookingTime A String representing the time wanted for the booking
     */
    public Booking(String email, String password, int partySize, String bookingTime) {
        super(email, password);
        this.partySize = partySize;
        this.bookingTime = bookingTime;
        synchronized (lock) {
            this.id = nextId++;
        }
    }

    /**
     * This method returns email field from the User class
     * @return A String representing the users email.
     */
    @Override
    public String getUserEmail() {
        return super.getEmail();
    }

    /**
     * This method returns the PartySize field
     * @return an integer which represents the number of people in this booking
     */
    public int getPartySize() {
        return partySize;
    }

    /**
     * This method returns the id field.
     * @return an integer which represents this booking's unique booking ID
     */
    public int getId() {
        return id;
    }

    /**
     * This method returns the bookingTime field
     * @return a String representing the time wanted for the booking
     */
    public String getBookingTime() {
        return bookingTime;
    }

    /**
     * This method sets the partySize field
     * @param partySize An integer containing the number of people in the reservation
     */
    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    /**
     * This method sets the bookingTime field
     * @param bookingTime A String representing the time wanted for the booking
     */
    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    /**
     * This method sets the tables field.
     * @param tables An arrayList containing Strings describing the tables in the restaurant.
     * It contains the table number, table size and if its available per index of this ArrayList.
     */
    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }

    /**
     * This method checks the availability of a specific table
     * @param target An integer representing the table number wanting to be checked
     * @return true if table is available and false if isn't
     */
    public boolean isAvailable(int target) { // {3,4,true}
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

