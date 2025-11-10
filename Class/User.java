package database.Class;
import database.Interface.UserInterface;

/**
 * User Class
 * Creates a new User for account creation in Client and Server class
 * Also can use for deletion as well
 * A user would have an Email and Password, that can be checked by iterating through a list of
 * users in a .txt file
 */
public class User implements UserInterface {
    private String email;
    private String password;
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
