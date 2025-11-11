package database.TestCases;
import database.src.users.User;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Test cases for the User.java
 *
 * @author Mahitha Kodali, lab sec 02
 * @version November 7, 2025
 */
public class UserUnitTest {
    @Test
    public void testConstructor() {
        User user = new User("test@gmail.com", "test123");
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals("test123", user.getPassword());
    }
}

