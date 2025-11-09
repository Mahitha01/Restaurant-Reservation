/**
 * This program implements the interface Reservable and
 * creates a Reservation object and have accessor & mutators for each field
 *
 * @author Shawn Shu, lab sec 02
 * @version November 7, 2025
 */
public class Reservation implements Reservable {
    private String name;
    private String date;
    private String ID;
    private int numGuests;

    /**
     * The constructor instantiates all fields of the Reservation object
     *
     * @param name name of the customer
     * @param date reservation date
     * @param ID reservation ID
     * @param numGuests number of guests attending
     */
    public Reservation (String name, String date, String ID, int numGuests) {
        this.name = name;
        this.date = date;
        this.ID = ID;
        this.numGuests = numGuests;
    }

    /**
     * This method returns the name field
     * @return A String representing the name of the customer
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * This method returns the date field
     * @return A String representing the event date
     */
    @Override
    public String getDate() {
        return date;
    }

    /**
     * This method returns the ID field
     * @return A String representing the reservation ID
     */
    @Override
    public String getID() {
        return ID;
    }

    /**
     * This method returns the numGuests field
     * @return An integer representing the number of attendees
     * of the event
     */
    @Override
    public int getNumGuests() {
        return numGuests;
    }

    /**
     * This method sets the name field
     * @param name A String representing the updated name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the date field
     * @param date A String representing the updated date
     */
    @Override
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This method sets the ID field
     * @param ID A String representing the updated reservation ID
     */
    @Override
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * This method sets the numGuests field
     * @param numGuests An integer representing the updated number of attendees
     */
    @Override
    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    /**
     * This method returns a String containing all instances of a Reservation object
     * @return A String containing all fields of the Reservation class
     */
    @Override
    public String toString() {
        return name + "," + date + "," + ID + "," + numGuests;
    }

    /**
     * This method returns a new Reservation object
     * @param info A String representing all information needed for making a reservation
     * @return A new Reservation object
     */
    public static Reservation reserve(String info) {
        try {
            String[] parts = info.split(",");
            return new Reservation(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }
}
