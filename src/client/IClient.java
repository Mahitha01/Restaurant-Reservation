package database.src.client;

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
     * This method disconnects the client from the server
     *
     */
    public void disconnect();

    /**
     * This method initializes the frame and other GUI components
     *
     */
    public void setGUI();

    /**
     * This method creates the login page;
     * Login information is sent to the server for validation
     *
     */
    public void loginPage();

    /**
     * This method creates the create account page;
     * Account information is sent to the server to be stored in the database
     *
     */
    public void createAccountPage();

    /**
     * This method creates the delete account page;
     * Deleted account information will be sent to the server to remove from the database
     */
    public void deleteAccount();

    /**
     * This method displays the available time slots;
     * All times are sent from the server
     *
     */
    public void getAvailableTime();

    /**
     * This method displays the available tables;
     * All tables are sent from the server
     *
     */
    public void getAvailableTable();

    /**
     * This method sends reservation information to the server;
     * The server stores the reservation information in the database
     *
     */
    public void addReservation();

    /**
     * This method sends cancel reservation information to the server;
     * The server removes the reservation information from the database
     */
    public void cancelReservation();
}

