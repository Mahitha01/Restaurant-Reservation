package database.Database2;

import java.util.*;
import java.io.*;

/**
 * This program implements the interface Databaseable and manipulates files and arraylists
 * containing Reservation information and objects
 *
 * @author Shawn Shu
 * @version November 7, 2025
 */
public class ReservationDatabase implements Databaseable {
    private FileProcessing file;
    private ArrayList<Reservation> reservations;
    private String fileName;

    /**
     * This constructor initializes all fields
     * @param fileName A String representing the file name
     */
    public ReservationDatabase(String fileName) {
        this.file = new FileProcessing();
        this.reservations = new ArrayList<Reservation>();
        this.fileName = fileName;

        List<String> allReservations = file.readFile(fileName);
        for (String line: allReservations) {
            Reservation currentReservation = Reservation.reserve(line);
            if (currentReservation != null) {
                reservations.add(currentReservation);
            }
        }
    }

    /**
     * This method adds a Reservation object
     * @param reserve A Reservation object
     */
    @Override
    public synchronized void addReservation(Reservation reserve) {
        reservations.add(reserve);
        ArrayList<String> lines = new ArrayList<String>();
        for (Reservation r: reservations) {
            lines.add(r.toString());
        }
        file.writeFile(fileName, lines);
    }

    /**
     * This method removes a Reservation object
     * @param reserve A Reservation object
     */
    @Override
    public synchronized void removeReservation(Reservation reserve) {
        for (Reservation r: reservations) {
            if (r.getName().equals(reserve.getName()) && r.getDate().equals(reserve.getDate())
                    && r.getID().equals(reserve.getID()) && r.getNumGuests() == reserve.getNumGuests()) {
                reservations.remove(r);
            }
        }
        ArrayList<String> lines = new ArrayList<String>();
        for (Reservation r: reservations) {
            lines.add(r.toString());
        }
        file.writeFile(fileName, lines);
    }

    /**
     * This method returns a Reservation object
     * @param ID A String representing the reservation ID
     * @return A Reservation object found based on the ID
     */
    @Override
    public synchronized Reservation getReservation(String ID) {
        for (Reservation r: reservations) {
            if (r.getID().equals(ID)) {
                return r;
            }
        }
        return null;
    }
}
