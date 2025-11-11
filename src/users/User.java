package database.src.users;

import java.util.List;

/**
 * User Class
 * Creates a new User for account creation in Client and Server class
 * Also can use for deletion as well
 * A user would have an Email and Password, that can be checked by iterating through a list of
 * users in a .txt file
 *
 * @author Tommy Wei, lab sec 02
 * @version November 11, 2025
 */
public class User implements IUser {
    private String email;
    private String password;

    /**
     *
     * @param email
     * @param password
     */
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

    @Override
    public int getPasswordHash() {
        return (password.hashCode());
    }

    @Override
    public List<IBooking> getReservations() {
        return List.of();
    }

    @Override
    public void addReservation(IBooking r) {

    }

    @Override
    public void cancelReservation(int reservationId) {

    }
}
