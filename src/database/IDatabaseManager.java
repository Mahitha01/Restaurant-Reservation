package database;

import users.IBooking;

import java.io.IOException;

public interface IDatabaseManager {
    boolean createUser(String username, String password);
    boolean deleteUser(String username);
    boolean authenticate(String username, String password);
    boolean addReservation(String username, IBooking r);
    boolean cancelReservation(int reservationId);
    void save() throws IOException;
    void load() throws IOException, ClassNotFoundException;
}
