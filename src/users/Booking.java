package users;

import java.util.ArrayList;

public class Booking extends User implements IBooking {
    private int partySize;
    private String bookingTime;
    private ArrayList<String> tables; //in format of #table number, #table Size, #availble or not
    private boolean isAvailable;
    private int id;

    public Booking(String email, String password, int partySize, String bookingTime, int id) {
        super(email, password);
        this.partySize = partySize;
        this.bookingTime = bookingTime;
        this.id = id;
    }

    @Override
    public String getUserEmail() {
        return super.getEmail();
    }

    public int getPartySize() {
        return partySize;
    }

    public int getId() {
        return id;
    }

    public String getBookingTime() {
        return bookingTime;
    }
    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }

    public boolean isAvailable(int target) { // {3,4,true}
        if (tables == null) {
            return false;
        }

        for (String table : tables) {
            String[] line = table.split(",");
            int tableNumber = Integer.parseInt(line[0]);
            if (tableNumber == target && Boolean.parseBoolean(line[2])) {
                return true;
            }
        }

        return false;
    }
}

