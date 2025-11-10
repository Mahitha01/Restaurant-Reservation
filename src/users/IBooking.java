package database.src.users;

public interface IBooking extends java.io.Serializable {
    String getUserEmail();
    int getPartySize();
    String getBookingTime();
    void setPartySize(int size);
    void setBookingTime(String time);
    int getId();
}