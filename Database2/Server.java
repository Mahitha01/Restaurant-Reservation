package database.Database2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server side for program, gets user information and calls database to store
 * information
 * 
 * @author Raphie Lubiniecki, lab sec 02
 * @version November 17th, 2025
 */

public class Server implements Runnable {
    private static int baseId = 0;
    private Socket socket;
    private static Object lock = new Object();

    /**
     * This constructor creates a Server object
     * When a client connects to the server, a new server object is created
     * and a new thread is created.
     * 
     * @param socket The client socket used to communicate with the server
     */
    public Server(Socket socket) {
        this.socket = socket;
    }

    /**
     * Starts the server
     * Doesn't end until there is a force exit (always runs), like real life servers
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);

            while (true) {
                System.out.println("Waiting...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Fount client!");
                Thread clientThread = new Thread(new Server(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Thread that is created when a user connects
     * This allows multiple users to connect at the same time
     */
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println("What is your name?");
            writer.flush();
            String name = reader.readLine();
            writer.println("What time do you want?");
            writer.flush();
            String time = reader.readLine();
            writer.println("How many guests?");
            writer.flush();
            int guests = Integer.parseInt(reader.readLine());
            int id = 0;
            synchronized (lock) {
                id = baseId++;
            }
            Reservable reservable = new Reservation(name, time, String.valueOf(id), guests);

            String line = String.format("%s,%s,%s,%d", reservable.getName(), reservable.getDate(), reservable.getID(),
                    reservable.getNumGuests());
            FileProcessing fp = new FileProcessing();
            List<String> list = new ArrayList<>();
            list.add(line);
            fp.writeFile("data.txt", list);

            writer.println(String.format("Confirmation: Name: %s, Date: %s, Guests: %d", name, time, guests));
            writer.flush();
            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
