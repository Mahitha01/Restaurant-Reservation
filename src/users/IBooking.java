package database.src.users;
/**
 * Interface for a booking. Implemented by Booking.java
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
public interface IBooking extends java.io.Serializable {
    String getUserEmail();
    int getPartySize();
    String getBookingTime();
    void setPartySize(int size);
    void setBookingTime(String time);
    int getId();
}