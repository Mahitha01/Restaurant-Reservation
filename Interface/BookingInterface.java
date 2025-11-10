package database.Interface;
import java.util.ArrayList;

public interface BookingInterface {
    int getPartySize();
    String getBookingTime();
    void setPartySize(int partySize);
    void setBookingTime(String bookingTime);
    void setTables(ArrayList<String> tables);
    boolean isAvailable(int target);
}
