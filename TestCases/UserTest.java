package database.TestCases;

import database.src.users.User;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the User class
 * 
 * @author Raphie Lubiniecki, lab sec 02
 * @version November 8th, 2025
 */

public class  UserTest {
    @Test
    public void testGetters() {
        User user1 = new User("joe@hotmail.com", "1234");
        String expectedEmail1 = "joe@hotmail.com";
        String expectedPassword1 = "1234";
        int expectedPasswordHash1 = expectedPassword1.hashCode();
        String actualEmail1 = user1.getEmail();
        String actualPassword1 = user1.getPassword();
        int actualPasswordHash1 = user1.getPasswordHash();
        assertEquals(expectedEmail1, actualEmail1);
        assertEquals(expectedPassword1, actualPassword1);
        assertEquals(expectedPasswordHash1, actualPasswordHash1);

        User user2 = new User("xyzabcdefg@gmail.com", "hYPh3N");
        String expectedEmail2 = "xyzabcdefg@gmail.com";
        String expectedPassword2 = "hYPh3N";
        int expectedPasswordHash2 = expectedPassword2.hashCode();
        String actualEmail2 = user2.getEmail();
        String actualPassword2 = user2.getPassword();
        int actualPasswordHash2 = user2.getPasswordHash();
        assertEquals(expectedEmail2, actualEmail2);
        assertEquals(expectedPassword2, actualPassword2);
        assertEquals(expectedPasswordHash2, actualPasswordHash2);
    }
}
