package database.TestCases;

import database.Database2.Reservation;
import database.Database2.ReservationDatabase;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for ReservationDatabase.java
 * 
 * @author Shawn Shu, lab sec 02
 * @version November 8, 2025
**/
public class ReservationDatabaseUnitTest {
    @Test
    public void testAddingAndGettingReservation() {
        ReservationDatabase rd = new ReservationDatabase("Customer4.txt");
        Reservation r = new Reservation("Andrew Jackson", "2025-12-7", "07", 7);
        rd.addReservation(r);
        Reservation currentReservation = rd.getReservation("07");
        assertNotNull(currentReservation);
        assertEquals(r, currentReservation);
    }

    @Test
    public void testRemovingReservation() {
        ReservationDatabase rd = new ReservationDatabase("Customer5.txt");
        Reservation currentReservation = new Reservation("John Tyler", "2025-12-8", "08", 8);
        rd.addReservation(currentReservation);
        rd.removeReservation(currentReservation);
        assertEquals(currentReservation, rd.getReservation("08"));
    }
}

