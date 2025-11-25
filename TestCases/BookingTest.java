package database.TestCases;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import database.src.users.Booking;
import org.junit.Test;

/**
 * Test cases for the Booking class
 * 
 * @author Raphie Lubiniecki, lab sec 02
 * @version November 8th, 2025
 */

public class BookingTest {
    @Test
    public void testGetters() {
        Booking book1 = new Booking("harrypotter@yahoo.edu", "vvvvv", 4, "10:45");
        String expectedEmail1 = "harrypotter@yahoo.edu";
        String expectedPassword1 = "vvvvv";
        int expectedParty1 = 4;
        String expectedTime1 = "10:45";
        String actualEmail1 = book1.getEmail();
        String actualPassword1 = book1.getPassword();
        int actualParty1 = book1.getPartySize();
        String actualTime1 = book1.getBookingTime();
        assertEquals(expectedEmail1, actualEmail1);
        assertEquals(expectedPassword1, actualPassword1);
        assertEquals(expectedParty1, actualParty1);
        assertEquals(expectedTime1, actualTime1);

        Booking book2 = new Booking("randomemail@purdue.edu", "Password123", 8, "12:00");
        String expectedEmail2 = "randomemail@purdue.edu";
        String expectedPassword2 = "Password123";
        int expectedParty2 = 8;
        String expectedTime2 = "12:00";
        String actualEmail2 = book2.getEmail();
        String actualPassword2 = book2.getPassword();
        int actualParty2 = book2.getPartySize();
        String actualTime2 = book2.getBookingTime();
        assertEquals(expectedEmail2, actualEmail2);
        assertEquals(expectedPassword2, actualPassword2);
        assertEquals(expectedParty2, actualParty2);
        assertEquals(expectedTime2, actualTime2);
    }

    @Test
    public void testSetters() {
        Booking book1 = new Booking("harrypotter@yahoo.edu", "vvvvv", 4, "10:45");
        book1.setPartySize(6);
        book1.setBookingTime("18:00");
        assertEquals(6, book1.getPartySize());
        assertEquals("18:00", book1.getBookingTime());

        Booking book2 = new Booking("randomemail@purdue.edu", "Password123", 8, "12:00");
        book2.setPartySize(2);
        book2.setBookingTime("8:00");
        assertEquals(2, book2.getPartySize());
        assertEquals("8:00", book2.getBookingTime());
    }
}
