package database.src.users;

import java.util.List;
/**
 * Interface for a User. Implemented by User.java
 *
 * @author Max, lab sec 02
 * @version November 11, 2025
 */
public interface IUser extends java.io.Serializable {
    String getEmail();
    int getPasswordHash();
    List<IBooking> getReservations();
    void addReservation(IBooking r);
    void cancelReservation(int reservationId);
}

