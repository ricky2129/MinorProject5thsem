import java.io.*;
import java.net.*;

public class ChatClient {
    private final String serverHost;
    private final int serverPort;
    private String username;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            socket = new Socket(serverHost, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            setUsername();
            new Thread(this::readMessages).start();

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String inputLine;
            while ((inputLine = userInput.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void setUsername() throws IOException {
        System.out.print("Enter your username: ");
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        username = userInput.readLine();
        out.println(username);
    }

    private void readMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            System.err.println("Connection to the server lost.");
        } finally {
            close();
        }
    }

    private void close() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 12345;

        ChatClient chatClient = new ChatClient(serverHost, serverPort);
        chatClient.start();
    }
}
