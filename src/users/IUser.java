package users;

import java.util.List;

public interface IUser extends java.io.Serializable {
    String getEmail();
    int getPasswordHash();
    List<IBooking> getReservations();
    void addReservation(IBooking r);
    void cancelReservation(int reservationId);
}

