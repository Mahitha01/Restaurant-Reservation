package database.Database2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {
    private static int baseId = 0;
    private Socket socket;
    public Server(Socket socket) {
        this.socket = socket;
    }
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            
            while(true) {
                System.out.println("Waiting...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Fount client!");
                Thread clientThread = new Thread(new Server(clientSocket));
                clientThread.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
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
            
            writer.println(String.format("Confirmation: Name: %s, Date: %s, Guests: %d", name, time, guests));
            writer.flush();
            reader.close();
            writer.close();

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
