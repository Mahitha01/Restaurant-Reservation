package database.src.users;

import java.util.List;
/**
 * Interface for a User. Implemented by User.java
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
public interface IUser extends java.io.Serializable {
    /**
     * This method returns the email field
     * @return a String representing the user's email
     */
    String getEmail();

    /**
     * This method returns the password field
     * @return A String representing the user's password
     */
    String getPassword();

    /**
     * This method returns the hashcode of the password field.
     * @return An int that represents the password of the user in hashcode
     */
    int getPasswordHash();

    /**
     * This method returns an empty list as a placeholder.
     * It will eventually return all the reservations made for the next 3 months.
     * @return An empty list
     */
    List<IBooking> getReservations();

    /**
     * This method adds a reservation
     * @param r a Booking containing the booking details
     */
    void addReservation(IBooking r);

    /**
     * This method cancels a reservation
     * @param reservationId an integer with the unique reservation/booking id
     */
    void cancelReservation(int reservationId);
}

