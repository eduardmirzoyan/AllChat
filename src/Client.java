import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Client {

    private Socket socket;
    //private String IP = "67.164.98.78"; // Use this for online connections! must port forward
    private String IP = "localhost";
    private int PORT = 8888;
    private ObjectOutputStream objOut;
    private ServerConnection serverConn;

    DateTimeFormatter dtf;

    private final int WIDTH = 400; // Original 506
    private final int HEIGHT = 500; // Original 527
    private final static String newline = "\n";
    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private TextArea chatbox;
    private TextField textbox;

    private int clientCount;

    private String clientName;
    private ArrayList<String> allClientNames = new ArrayList<>();

    public Client() throws IOException, ClassNotFoundException {
        socket = new Socket(IP, PORT);
        System.out.println("I have connected to a server...");

        clientCount = 1;

        dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        objOut = new ObjectOutputStream(socket.getOutputStream()); // TO SEND DATA
        objOut.flush();

        serverConn = new ServerConnection(socket, this);
        new Thread(serverConn).start();
        //in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // To RECEIVE DATA

        frame = new JFrame();
        panel = new JPanel();
        label = new JLabel("Clients: " + clientCount + " " + allClientNames);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(panel);
        panel.add(label);

        chatbox = new TextArea("Welcome to Virtual Chat!" + newline, 25, 50);
        chatbox.setEditable(false);
        panel.add(chatbox, BorderLayout.NORTH);

        textbox = new TextField(50);
        panel.add(textbox, BorderLayout.SOUTH);
        textbox.requestFocus();

        textbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    if(textbox.getText().equals("/leave")) {
                        System.out.println(clientName + " has left.");
                        System.exit(0);

                    } else {
                        objOut.writeObject(new DataPacket(clientName, textbox.getText()));
                        textbox.setText("");
                        System.out.println("I have sent a chat message!");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        frame.setTitle("Virtual Chatbox");
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        clientName = JOptionPane.showInputDialog(null, "Enter your alias:");
        System.out.println("I have chosen the name: " + clientName);
        objOut.writeObject(new DataPacket(clientName));
        objOut.flush();
        System.out.println("Name data sent!");

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Client client = new Client();

        while(client.isConnected()) {
            client.update();

            client.panel.repaint();
        }
        client.objOut.flush();
        client.socket.close();
        System.exit(0);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ArrayList<String> getAllClientNames() {
        return allClientNames;
    }

    public void setAllClientNames(ArrayList<String> allClientNames) {
        this.allClientNames = allClientNames;
    }

    public boolean isConnected() { return socket.isConnected(); }

    public void update() {
        clientCount = allClientNames.size();
        label.setText("Clients: " + clientCount + " " + allClientNames);
    }

    public void broadcast(String message) {
        chatbox.append("[" + dtf.format(LocalDateTime.now()) + "] " + message + newline);
    }
}
