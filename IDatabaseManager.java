package database;

import java.io.IOException;

public interface IDatabaseManager {
    boolean createUser(String username, String password);
    boolean deleteUser(String username);
    boolean authenticate(String username, String password);
    boolean addReservation(String username, IReservation r);
    boolean cancelReservation(int reservationId);
    void save() throws IOException;
    void load() throws IOException, ClassNotFoundException;
}
