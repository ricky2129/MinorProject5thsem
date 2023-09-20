import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ChatServer {
    private final int port;
    private final ServerSocket serverSocket;
    private final UserList userList;
    private final ClientManager clientManager;

    public ChatServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.userList = new UserList();
        this.clientManager = new ClientManager();

        System.out.println("Chat server is running on port " + port);
    }

    public void start() {
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, userList, clientManager);
                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        int port = 12345;

        try {
            ChatServer chatServer = new ChatServer(port);
            chatServer.start();
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }
}
