package database.src.users;
/**
 * Interface for a booking. Implemented by Booking.java
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
public interface IBooking extends java.io.Serializable {
    /**
     * This method returns email field from the User class
     * @return An integer representing the users email.
     */
    String getUserEmail();

    /**
     * This method returns the PartySize field
     * @return an integer which represents the number of people in this booking
     */
    int getPartySize();

    /**
     * This method returns the id field.
     * @return an integer which represents this booking's unique booking ID
     */
    int getId();

    /**
     * This method returns the bookingTime field
     * @return a String representing the time wanted for the booking
     */
    String getBookingTime();

    /**
     * This method sets the partySize field
     * @param size An integer containing the number of people in the reservation
     */
    void setPartySize(int size);

    /**
     * This method sets the bookingTime field
     * @param time A String representing the time wanted for the booking
     */
    void setBookingTime(String time);
}