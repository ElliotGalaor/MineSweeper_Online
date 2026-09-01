import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
//import java.net.ServerSocket;
import java.net.Socket;

public class HandlerServeur {

    private static int PORT = 10000;
    private static String HOST = "localhost";
    private Socket socket;
    private DataInputStream entree;
    private DataOutputStream sortie;
    private boolean connected = false;

    HandlerServeur() {
        
    }

    public boolean connect() {
        try {
            connected = true;
            socket=new Socket(HOST,PORT);

            //attente
            // ouverture des streams
            entree = new DataInputStream(socket.getInputStream());
            sortie = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            connected = false;
            //e.printStackTrace( );
        }
        return connected;
    }

    public void disconnect() {
        try {
            sortie.close() ;
            entree.close() ;
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace( );
        }
        connected = false;
    }

    public boolean getConnection() {
        return connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public int readInt() {
        try {
            return entree.readInt();
        }
        catch (IOException e) {
            e.printStackTrace( );
        }
        return 0;
    }

    public String readString() {
        try {
            return entree.readUTF();
        }
        catch (IOException e) {
            e.printStackTrace( );
        }
        return "";
    }

    public void writeInt(int msg) {
        try {
            sortie.writeInt(msg);
        }
        catch (IOException e) {
            e.printStackTrace( );
        }
    }

    public void writeString(String msg) {
        try {
            sortie.writeUTF(msg);
        }
        catch (IOException e) {
            e.printStackTrace( );
        }
    }
}
