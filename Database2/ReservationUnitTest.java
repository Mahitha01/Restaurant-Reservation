package database.Database2;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for Reservation.java
 * 
 * @author Shawn Shu, lab sec 02
 * @version November 8, 2025
**/
public class ReservationUnitTest {
    @Test
    public void testConstructor() {
        Reservation r = new Reservation("George Washington", "2025-12-1", "01", 1);
        assertEquals("George Washington", r.getName());
        assertEquals("2025-12-1", r.getDate());
        assertEquals("01", r.getID());
        assertEquals(1, r.getNumGuests());
    }

    @Test
    public void testMutators() {
        Reservation r = new Reservation("John Adams", "2025-12-2", "02", 2);
        r.setName("Thomas Jefferson");
        r.setDate("2025-12-3");
        r.setID("03");
        r.setNumGuests(3);
        assertEquals("Thomas Jefferson", r.getName());
        assertEquals("2025-12-3", r.getDate());
        assertEquals("03", r.getID());
        assertEquals(3, r.getNumGuests());


    }

    @Test
    public void testToString() {
        Reservation r = new Reservation("James Madison", "2025-12-4", "04", 4);
        assertEquals("James Madison,2025-12-4,04,4", r.toString());
    }

    @Test
    public void testReserve() {
        String info = "John Quincy Adams,2025-12-5,05,5";
        Reservation r = Reservation.reserve(info);
        assertEquals("John Quincy Adams", r.getName());
        assertEquals("2025-12-5", r.getDate());
        assertEquals("05", r.getID());
        assertEquals(5, r.getNumGuests());

    }
}

