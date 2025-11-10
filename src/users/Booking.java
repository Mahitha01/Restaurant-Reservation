package database.src.users;
import java.io.*;
import java.util.ArrayList;

public class Booking extends User implements IBooking {
    private int partySize;
    private String bookingTime;
    private ArrayList<String> tables; //in format of #table number, #table Size, #availble or not
    private boolean isAvailable;
    private int id; //unique id
    private static int nextId = 1;
    private static Object lock = new Object();

    public Booking(String email, String password, int partySize, String bookingTime) {
        super(email, password);
        this.partySize = partySize;
        this.bookingTime = bookingTime;
        synchronized (lock) {
            this.id = nextId++;
        }
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

