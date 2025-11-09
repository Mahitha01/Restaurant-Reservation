package database;

import java.io.IOException;
import java.util.List;
import java.util.Map;

interface IPersistence {
    void saveUsers(Map<String, IUser> users) throws IOException;
    void saveReservations(List<IReservation> reservations) throws IOException;
    Map<String, IUser> loadUsers() throws IOException, ClassNotFoundException;
    List<IReservation> loadReservations() throws IOException, ClassNotFoundException;
}

