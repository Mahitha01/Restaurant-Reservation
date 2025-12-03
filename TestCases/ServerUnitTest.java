package database.TestCases;

import database.src.database.DatabaseManager;
import database.src.server.IServer;
import database.src.server.Server;
import database.src.users.Booking;
import database.src.users.IBooking;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.List;

/**
 * This class is a testCase for the Server Class
 *
 * @author Mahitha Kodali, lab sec 02
 * @version November 24, 2025
 */
public class ServerUnitTest {
    @Test
    public void testCreateAccount() throws IOException, ClassNotFoundException {
        new File("users.db").delete();
        new File("reservations.db").delete();
        DatabaseManager dm = new DatabaseManager();
        dm.load();
        PrintWriter pwTest = new PrintWriter(new StringWriter());
        Server server = new Server(null);
        String[] result = server.createAccount("Test@gmail.com", "Test123", null, pwTest, dm);

        assertEquals("Test@gmail.com", result[0]);
        assertEquals("Test123", result[1]);
        assertTrue(dm.authenticate("Test@gmail.com", "Test123"));

        //Tests for duplicate users
        String[] duplicate = server.createAccount("Test@gmail.com", "Test123", null, pwTest, dm);
        assertEquals(0, duplicate.length);
    }

    @Test
    public void testLogin() throws IOException, ClassNotFoundException {
        DatabaseManager dm = new DatabaseManager();
        dm.load();
        dm.createUser("loginTest@gmail.com", "LoginTest123");
        PrintWriter pwTest = new PrintWriter(new StringWriter());
        Server server = new Server(null);

        String[] result = server.login("loginTest@gmail.com", "LoginTest123", null, pwTest, dm);

        assertEquals("loginTest@gmail.com", result[0]);
        assertEquals("LoginTest123", result[1]);

        //login fail
        String[] failPass = server.login("loginTest@gmail.com", "wrongPass", null, pwTest, dm);
        String[] failEmail = server.login("Wrongemail@gmail.com", "LoginTest123", null, pwTest, dm);

        assertEquals(0, failPass.length);
        assertEquals(0, failEmail.length);
    }

    @Test
    public void testDeleteAccount() throws IOException, ClassNotFoundException {
        DatabaseManager dm = new DatabaseManager();
        dm.load();
        dm.createUser("deletedAcc", "delete");

        Server server = new Server(null);
        PrintWriter pwTest = new PrintWriter(new StringWriter());

        server.deleteAccount("deletedAcc", null, pwTest, dm);
        assertFalse(dm.authenticate("deletedAcc", "delete"));

        //tests for crashing
        server.deleteAccount("nouser", null, pwTest, dm);
        assertFalse(dm.authenticate("nouser", "anyPass"));
    }

    @Test
    public void testGetBookings() throws IOException, ClassNotFoundException {
        new File("users.db").delete();
        new File("reservations.db").delete();

        DatabaseManager dm = new DatabaseManager();
        dm.load();

        Server server = new Server(null);

        PrintWriter pwTest = new PrintWriter(new StringWriter());

        dm.createUser("testBook@gmail.com", "testBookpass");

        IBooking b1 = new Booking("testBook@gmail.com", "testBookpass", 6, "12-01-2025 13:00", 5);
        IBooking b2 = new Booking("testBook@gmail.com", "testBookpass", 4, "11-30-2025 18:30", 4);

        dm.addReservation("testBook@gmail.com", b1);
        dm.addReservation("testBook@gmail.com", b2);

        String[] result = server.getBookings("testBook@gmail.com", pwTest, dm).split(";");

        assertTrue(result[0].contains("PartySize: 6, Time: 12-01-2025 13:00"));
        assertTrue(result[1].contains("PartySize: 4, Time: 11-30-2025 18:30"));
    }
}
