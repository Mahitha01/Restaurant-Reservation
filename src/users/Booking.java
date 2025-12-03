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
    private int tableNum;
    private int id; //unique id
    private static int nextId = 1;
    private static Object lock = new Object();

    /**
     * This constructor initializes all the parameters and increments id which represents a unique id per Booking
     * @param email A String representing the users email
     * @param password A String representing the users password
     * @param partySize An integer containing the number of people in the reservation
     * @param bookingTime A String representing the time wanted for the booking
     * @param tableNum An int representing the table reserved.
     */
    public Booking(String email, String password, int partySize, String bookingTime, int tableNum) {
        super(email, password);
        this.partySize = partySize;
        this.bookingTime = bookingTime;
        this.tableNum = tableNum;
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
     * This returns the booked table number
     * @return An int representing the table number
     */
    public int getTableNum() {
        return tableNum;
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

}

