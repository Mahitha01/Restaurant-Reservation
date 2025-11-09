/**
 * This interface creates abstract methods for all getters and setters,
 * as well as the toString() method that returns all fields,
 * and a method that returns a new Reservation object
 *
 * @author Shawn Shu
 * @version November 7, 2025
 */
public interface Reservable {
    /**
     * This method returns the name field
     * @return A String representing the name of the customer
     */
    String getName();

    /**
     * This method returns the date field
     * @return A String representing the event date
     */
    String getDate();

    /**
     * This method returns the ID field
     * @return A String representing the reservation ID
     */
    String getID();

    /**
     * This method returns the numGuests field
     * @return An integer representing the number of attendees
     * of the event
     */
    int getNumGuests();

    /**
     * This method sets the name field
     * @param name A String representing the updated name
     */
    void setName(String name);

    /**
     * This method sets the date field
     * @param date A String representing the updated date
     */
    void setDate(String date);

    /**
     * This method sets the ID field
     * @param ID A String representing the updated reservation ID
     */
    void setID(String ID);

    /**
     * This method sets the numGuests field
     * @param numGuests An integer representing the updated number of attendees
     */
    void setNumGuests(int numGuests);

    /**
     * This method returns a String containing all instances of a Reservation object
     * @return A String containing all fields of the Reservation class
     */
    String toString();

    /**
     * This method returns a new Reservation object
     * @param info A String representing all information needed for making a reservation
     * @return A new Reservation object
     */
    static Reservation reserve(String info) {
        String[] parts = info.split(",");
        return new Reservation(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
    }

}
