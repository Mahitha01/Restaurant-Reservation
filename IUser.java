package database;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface IUser extends java.io.Serializable {
    String getUsername();
    String getPasswordHash();
    List<IReservation> getReservations();
    void addReservation(IReservation r);
    void cancelReservation(int reservationId);
}

