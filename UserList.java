import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public class UserList {
    private final ConcurrentHashMap<String, PrintWriter> users = new ConcurrentHashMap<>();

    public void addUser(String username, PrintWriter writer) {
        users.put(username, writer);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean isUsernameAvailable(String username) {
        return !users.containsKey(username);
    }

    public void sendMessageToUser(String username, String message) {
        PrintWriter writer = users.get(username);
        if (writer != null) {
            writer.println(message);
        }
    }
}
