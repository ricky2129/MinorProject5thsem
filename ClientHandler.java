import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final UserList userList;
    private final ClientManager clientManager;

    private String username;
    private String ipAddress; // Store the client's IP address

    private PrintWriter out;

    public ClientHandler(Socket clientSocket, UserList userList, ClientManager clientManager) {
        this.clientSocket = clientSocket;
        this.userList = userList;
        this.clientManager = clientManager;

        try {
            this.ipAddress = clientSocket.getInetAddress().getHostAddress(); // Get the client's IP address
        } catch (Exception e) {
            System.err.println("Error getting IP address: " + e.getMessage());
        }

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("User connected: " + username + " (IP: " + ipAddress + ")"); // Display user connection
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            out.println("Welcome to the chat server. Please enter your username:");
            username = in.readLine();
            userList.addUser(username, out); // Add the username and PrintWriter to the UserList
            clientManager.addClient(this); // Add this client to the ClientManager
            clientManager.broadcastMessage(username + " has joined the chat. (IP: " + ipAddress + ")"); // Broadcast user join message

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                clientManager.broadcastMessage(username + ": " + inputLine);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            userList.removeUser(username); // Remove the user from the UserList
            clientManager.removeClient(this); // Remove this client from the ClientManager
            clientManager.broadcastMessage(username + " has left the chat. (IP: " + ipAddress + ")"); // Broadcast user leave message
            System.out.println("User disconnected: " + username + " (IP: " + ipAddress + ")"); // Display user disconnection
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
