import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    private Socket client;
    private ObjectOutputStream objOut;
    ObjectInputStream objIn;
    private ArrayList<ClientHandler> allClients;

    private String clientName;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> allClients) throws IOException, ClassNotFoundException, InterruptedException {
        this.client = clientSocket;
        this.allClients = allClients;
        this.clientName = "";

        objOut = new ObjectOutputStream(client.getOutputStream()); // REMEMBER TO FLUSH!!!
        objOut.flush();

        objIn = new ObjectInputStream(client.getInputStream());

        clientName = ((DataPacket) objIn.readObject()).getName();
        System.out.println("Name data received!");
        System.out.println("Client has chosen the name: " + clientName);
        sayFromServer("HAS JOINED.");
    }

    @Override
    public void run() {
        try {
            while(true) {
                objOut.writeObject(new DataPacket(getAllClientNames()));
                objOut.flush();

                DataPacket clientData = (DataPacket) objIn.readObject();
                System.out.println("Client " + clientName + " has said: " + clientData.getMessage());

                sayFromClient(clientData.getMessage());
            }
        } catch (EOFException | SocketException e) {
            try {
                client.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println(clientName + " has lost connection.");
            allClients.remove(this);
            sayFromServer("HAS LEFT.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //out.close();

    }

    public String getName() {
        return clientName;
    }

    @Override
    public String toString() {
        return clientName; // WORK ON LATER
    }

    public ArrayList<String> getAllClientNames() {

        ArrayList<String> allClientNames = new ArrayList<>();
        for(ClientHandler aClient : allClients) {
            allClientNames.add(aClient.getName());
        }

        return allClientNames;
    }

    public void sayFromClient(String message) {
        for(ClientHandler aClient : allClients) {
            try {
                aClient.objOut.writeObject(new DataPacket(clientName, message, getAllClientNames()));
                aClient.objOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sayFromServer(String message) {
        for(ClientHandler aClient : allClients) {
            try {
                aClient.objOut.writeObject(new DataPacket(clientName, message + " /say", getAllClientNames()));
                aClient.objOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
