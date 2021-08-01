import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;

    private ServerSocket server = null;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = null;

    private JFrame frame;
    private JLabel label;

    private String serverData;

    public Server(int port) {
        this.port = port;
        pool = Executors.newFixedThreadPool(4);
        frame = new JFrame();
        frame.setTitle("Chat Box Server");
        frame.setSize(300, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        serverData = "Server is running.";
        label = new JLabel(serverData);
        frame.add(label);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Server server = new Server(8888);
        server.start();
    }

    private void start() throws IOException, ClassNotFoundException, InterruptedException {
        server = new ServerSocket(port);
        System.out.println("Waiting for client(s)...");
        while(true) {
            Socket client = server.accept();
            System.out.println("Accepted client... ");
            ClientHandler clientThread = new ClientHandler(client, clients);
            clients.add(clientThread);
            pool.execute(clientThread);
            label.setText("Server is running: " + clients.size() + " clients " + clients);
        }

    }

}
