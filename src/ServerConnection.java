import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;

// RECIEVE DATA HERE
public class ServerConnection implements Runnable {

    private Socket server;
    private ObjectInputStream objIn;
    private Client client;

    public ServerConnection(Socket server, Client client) throws IOException, ClassNotFoundException {
        this.server = server;
        this.client = client;
        objIn = new ObjectInputStream(server.getInputStream()); // To RECEIVE DATA
    }

    @Override
    public void run() {

        DataPacket data;
        try {
            while(client.isConnected()) {
                data = (DataPacket) objIn.readObject();

                client.setAllClientNames(data.getAllNames());

                String name = data.getName();
                String message = data.getMessage();
                if(!message.equals("unknown")) {

                    String chat;
                    if(message.contains(" /say")) {
                        chat = name + " " + message.substring(0, message.length() - 5);
                    } else {
                        chat = name + ": " + message;
                    }
                    client.broadcast(chat);
                }

            }
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Server has closed. Goodbye :)");
            System.exit(0);
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
