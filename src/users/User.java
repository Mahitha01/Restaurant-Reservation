package database.src.users;
import database.src.users.IBooking;
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
    private List<IBooking> reservations;

    /**
     * This constructor initializes the email and password
     * @param email a String containing the email of the user
     * @param password a String containing the password.
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * This method returns the email field
     * @return a String representing the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method returns the password field
     * @return A String representing the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method returns the hashcode of the password field.
     * @return An int that represents the password of the user in hashcode
     */
    @Override
    public int getPasswordHash() {
        return (password.hashCode());
    }

    /**
     * This method returns an empty list as a placeholder.
     * It will eventually return all the reservations made for the next 3 months.
     * @return An empty list
     */
    @Override
    public List<IBooking> getReservations() {
        return reservations;
    }

    /**
     * This method adds a reservation
     * @param r a Booking containing the booking details
     */
    @Override
    public void addReservation(IBooking r) {
        reservations.add(r);
    }

    /**
     * This method cancels a reservation
     * @param reservationId an integer with the unique reservation/booking id
     */
    @Override
    public void cancelReservation(int reservationId) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == reservationId) {
                reservations.remove(i);
            }
        }
    }
}
