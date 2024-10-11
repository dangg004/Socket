package ex3;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345; // Port number
    private static Set<PrintWriter> clientWriters = new HashSet<>(); // To keep track of client writers

    public static void main(String[] args) {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start(); // Accept client and start a new thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast messages to all connected clients
    private static void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    // ClientHandler class for handling client connections
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String userName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // Setup input and output streams
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                // Request and store the username
                out.println("Enter your name: ");
                userName = in.readLine();
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }
                System.out.println(userName + " has joined.");
                broadcast(userName + " has joined the chat.");

                // Listen for messages from the client
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(userName + ": " + message);
                    broadcast(userName + ": " + message); // Broadcast the message to all clients
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Cleanup
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
                broadcast(userName + " has left the chat.");
                System.out.println(userName + " has disconnected.");
            }
        }
    }
}
