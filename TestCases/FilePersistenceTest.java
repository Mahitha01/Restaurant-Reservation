package database.TestCases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.src.database.FilePersistence;
import org.junit.Test;

import database.src.users.Booking;
import database.src.users.IBooking;
import database.src.users.IUser;
import database.src.users.User;

/**
 * Test cases for the FilePersistence class
 * 
 * @author Raphie Lubiniecki, lab sec 02
 * @version November 9th, 2025
 */

public class FilePersistenceTest {

    @Test
    public void testSaveUsers() throws IOException {
        IUser user1 = new User("mouse@hotmail.com", "woods");
        IUser user2 = new User("happy@gmail.com", "bear");
        Map<String, IUser> map = new HashMap<>();
        map.put("mouse@hotmail.com", user1);
        map.put("happy@gmail.com", user2);

        FilePersistence fp = new FilePersistence();
        fp.saveUsers(map);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.db"));
        try {
            Map<String, IUser> readMap = (Map<String, IUser>) ois.readObject();
            IUser readUser1 = readMap.get("mouse@hotmail.com");
            IUser readUser2 = readMap.get("happy@gmail.com");
            assertEquals(user1.getEmail(), readUser1.getEmail());
            assertEquals(user1.getPasswordHash(), readUser1.getPasswordHash());
            assertEquals(user2.getEmail(), readUser2.getEmail());
            assertEquals(user2.getPasswordHash(), readUser2.getPasswordHash());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        }
        ois.close();
    }

    @Test
    public void testSaveReservations() throws IOException {
        IBooking expectedBooking = new Booking("a@gmail.com", "hihihihi", 6, "12:30", 2);
        List<IBooking> expectedReservations = new ArrayList<>(Arrays.asList(expectedBooking));
        FilePersistence fp = new FilePersistence();
        fp.saveReservations(expectedReservations);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("reservations.db"));
        try {
            List<IBooking> bookings = (List<IBooking>) ois.readObject();
            IBooking actualBooking = bookings.get(0);
            assertEquals(expectedBooking.getUserEmail(), actualBooking.getUserEmail());
            assertEquals(expectedBooking.getPartySize(), actualBooking.getPartySize());
            assertEquals(expectedBooking.getId(), actualBooking.getId());
            assertEquals(expectedBooking.getBookingTime(), actualBooking.getBookingTime());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        }
        ois.close();
    }

    @Test
    public void testLoadUsers() throws IOException {
        IUser user1 = new User("mouse@hotmail.com", "woods");
        IUser user2 = new User("happy@gmail.com", "bear");
        Map<String, IUser> map = new HashMap<>();
        map.put("mouse@hotmail.com", user1);
        map.put("happy@gmail.com", user2);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.db"));
        oos.writeObject(map);

        FilePersistence fp = new FilePersistence();
        try {
            Map<String, IUser> actualMap = (Map<String, IUser>) fp.loadUsers();
            IUser readUser1 = actualMap.get("mouse@hotmail.com");
            IUser readUser2 = actualMap.get("happy@gmail.com");
            assertEquals(user1.getEmail(), readUser1.getEmail());
            assertEquals(user1.getPasswordHash(), readUser1.getPasswordHash());
            assertEquals(user2.getEmail(), readUser2.getEmail());
            assertEquals(user2.getPasswordHash(), readUser2.getPasswordHash());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        }
        oos.close();
    }

    @Test
    public void testLoadReservations() throws IOException {
        IBooking expectedBooking = new Booking("a@gmail.com", "hihihihi", 6, "12:30", 5);
        List<IBooking> expectedReservations = new ArrayList<>(Arrays.asList(expectedBooking));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("reservations.db"));
        oos.writeObject(expectedReservations);

        FilePersistence fp = new FilePersistence();
        try {
            List<IBooking> actualReservations = fp.loadReservations();
            IBooking actualBooking = actualReservations.get(0);
            assertEquals(expectedBooking.getUserEmail(), actualBooking.getUserEmail());
            assertEquals(expectedBooking.getPartySize(), actualBooking.getPartySize());
            assertEquals(expectedBooking.getId(), actualBooking.getId());
            assertEquals(expectedBooking.getBookingTime(), actualBooking.getBookingTime());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        }
        oos.close();
    }
}
