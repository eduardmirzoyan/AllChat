import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.ArrayList;

public class DataPacket implements Serializable {

    private String name = "Unknown"; // Name of MESSAGE SENDER!
    private String message = "unknown"; // Message of MESSAGE SENDER!
    private int count = -1;
    private ArrayList<String> allNames;

    public DataPacket(String name){
        this.name = name;
    }

    public DataPacket(String name, String message){
        this.name = name;
        this.message = message;
    }

    public DataPacket(String name, String message, ArrayList<String> allNames){
        this.name = name;
        this.message = message;
        this.allNames = allNames;
    }

    public DataPacket(int count) {
        this.count = count;
    }

    public DataPacket(ArrayList<String> allNames){
        this.allNames = allNames;
    }

    public DataPacket(String name, ArrayList<String> allNames) {
        this.name = name;
        this.allNames = allNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAllNames() {
        return allNames;
    }

    public void setAllNames(ArrayList<String> allNames) {
        this.allNames = allNames;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
