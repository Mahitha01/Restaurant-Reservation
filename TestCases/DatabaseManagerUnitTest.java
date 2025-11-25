package database.TestCases;
import database.src.database.DatabaseManager;
import database.src.users.Booking;
import database.src.users.IBooking;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Test cases for DatabaseManager.java
 *
 * @author Mahitha Kodali, lab sec 02
 * @version November 11, 2025
 */
public class DatabaseManagerUnitTest {
    @Test
    public void testCreateUserandAuthenticate() {
        DatabaseManager dm = new DatabaseManager();
        assertTrue(dm.createUser("user1@gmail.com", "user1Password"));
        assertFalse(dm.createUser("user1@gmail.com", "user1Password"));
        assertTrue(dm.authenticate("user1@gmail.com", "user1Password"));
        assertFalse(dm.authenticate("user1@gmail.com", "wrongPassword"));
    }

    @Test
    public void testDeleteUser() {
        DatabaseManager dm = new DatabaseManager();
        dm.createUser("user2@gmail.com", "user2Password");
        assertTrue(dm.deleteUser("user2@gmail.com"));
        assertFalse(dm.deleteUser("user2@gmail.com"));
    }

    @Test
    public void testAddandCancelReservation() {
        DatabaseManager dm = new DatabaseManager();
        dm.createUser("user3@gmail.com", "user3Password");
        IBooking testBooking = new Booking("user3@gmail.com", "user3Password",
                4, "19:30");

        assertTrue(dm.addReservation("user3@gmail.com", testBooking));
        assertTrue(dm.cancelReservation(testBooking.getId()));
        assertFalse(dm.cancelReservation(testBooking.getId()));
    }

    @Test
    public void testSaveandLoad() {
        DatabaseManager dm = new DatabaseManager();
        dm.createUser("user4@gmail.com", "user4Password");
        IBooking testBooking2 = new Booking("user4@gmail.com", "user4Password",
                2, "13:30");

        try {
            dm.save();
            dm.load();
            assertTrue(true);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /*@Test
    public void testAvailability() {
        DatabaseManager dm = new DatabaseManager();
        ArrayList<String> t = new ArrayList<>();
        t.add("3,4,true");
        t.add("1,2,true");
        t.add("2,10,true");
        t.add("4,2,false");
        dm.setTables(t);

        assertTrue(dm.isAvailable(3));
        assertTrue(dm.isAvailable(1));
        assertTrue(dm.isAvailable(2));
        assertFalse(dm.isAvailable(4));
    }*/
}
