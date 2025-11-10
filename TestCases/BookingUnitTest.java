package database.TestCases;
import database.Class.Booking;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class BookingUnitTest {
    @Test
    public void testConstructorandGetters() {
        Booking testBook = new Booking("testing@gmail.com", "testing123",
                4, "13:30");
        assertEquals("testing@gmail.com", testBook.getEmail());
        assertEquals("testing123", testBook.getPassword());
        assertEquals(4, testBook.getPartySize());
        assertEquals("13:30", testBook.getBookingTime());
    }

    @Test
    public void testMutators() {
        Booking b = new Booking("testing@gmail.com", "testing123",
                4, "13:30");
        b.setPartySize(10);
        b.setBookingTime("19:30");

        assertEquals(10, b.getPartySize());
        assertEquals("19:30", b.getBookingTime());
    }

    @Test
    public void testAvailability() {
        Booking testB = new Booking("user@gmail.com", "userPassword",
                2, "13:30");
        ArrayList<String> t = new ArrayList<>();
        t.add("3,4,true");
        t.add("1,2,true");
        t.add("2,10,true");
        t.add("4,2,false");
        testB.setTables(t);

        assertTrue(testB.isAvailable(3));
        assertTrue(testB.isAvailable(1));
        assertTrue(testB.isAvailable(2));
        assertFalse(testB.isAvailable(4));
    }
}
