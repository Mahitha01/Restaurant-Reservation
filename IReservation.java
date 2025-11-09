package database;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IReservation extends java.io.Serializable {
    int getId();
    LocalDate getDate();
    LocalTime getTime();
    int getPartySize();
    String getUsername();
}
