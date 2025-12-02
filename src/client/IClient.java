package database.src.client;
import java.io.IOException;
/**
 * This interface contains methods to be implemented in the client class
 *
 * @author Shawn Shu, lab sec 02
 * @version November 23, 2025
 */
public interface IClient {
    /**
     * This method connects the client with the server
     *
     */
    public void connect();

    /**
     * This method disconnects the client with the server
     *
     */
    public void disconnect();

    /**
     * This method initializes the frame and other GUI components
     *
     */
    public void setGUI();

    /**
     * This method allows user to go back to the set GUI page
     *
     */
    public void goback();
    /**
     * This method creates the login page;
     * Login information are sent to server for validation
     *
     */
    public void loginPage();

    /**
     * This method creates the create account page;
     * Account information are sent to server to store in database
     *
     */
    public void createAccountPage();

    /**
     * This method creates the delete account page;
     * Deleted account information will be sent to server to remove from database
     */
    public void deleteAccount();

    /**
     * This method allows the user to choose a reservation based on the date and time,
     * or cancel a reservation based on reservation id;
     * The client sends the information to the server, which stores it in the database;
     * If a reservation is successful the client side will display it using simple GUI
     * @param email A String representing the username
     * @param password A String representing the password
     * @param bookings A String containing all the reservations
     */
    public void reservationPage(String email, String password, String bookings) throws IOException;
}
