package database.Database2;

/**
 * This interface performs adding, removing , and retrieving a Reservation object
 *
 * @author Shawn Shu
 * @version November 7, 2025
 */
public interface Databaseable {
    /**
     * This method adds a Reservation object
     * @param reserve A Reservation object
     */
    void addReservation(Reservation reserve);

    /**
     * This method removes a Reservation object
     * @param reserve A Reservation object
     */
    void removeReservation(Reservation reserve);

    /**
     * This method returns a Reservation object
     * @param ID A String representing the reservation ID
     * @return A Reservation object found based on the ID
     */
    Reservation getReservation(String ID);
}

