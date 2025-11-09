package database;

import users.IBooking;
import users.IUser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

interface IPersistence {
    void saveUsers(Map<String, IUser> users) throws IOException;
    void saveReservations(List<IBooking> reservations) throws IOException;
    Map<String, IUser> loadUsers() throws IOException, ClassNotFoundException;
    List<IBooking> loadReservations() throws IOException, ClassNotFoundException;
}

