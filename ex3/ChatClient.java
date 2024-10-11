package ex3;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change to server address if needed
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            new Thread(new IncomingReader(socket)).start(); // Start a thread for reading incoming messages

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 Scanner scanner = new Scanner(System.in)) {

                String userInput;
                while (true) {
                    userInput = scanner.nextLine();
                    if (userInput.equalsIgnoreCase("exit")) {
                        break; // Exit the chat if the user types "exit"
                    }
                    out.println(userInput); // Send message to server
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // IncomingReader class to handle incoming messages
    private static class IncomingReader implements Runnable {
        private Socket socket;

        public IncomingReader(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message); // Print incoming messages to the console
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
