import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HandlerClient {

    private ServerSocket gestSock;
    private Socket socket;
    private DataInputStream entree;
    private DataOutputStream sortie;
    private int id = -1;
    private String nom = "";
    private int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int s) {
        score = s;
    }

    HandlerClient() {

    }

    HandlerClient(int id, ServerSocket g) {
        gestSock = g;
        connect(id);
    }

    public void connect(int id) {
        try {
            this.id = id;
            socket=gestSock.accept() ;

            //attente
            // ouverture des streams
            entree = new DataInputStream(socket.getInputStream());
            sortie = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            id=-1;
            e.printStackTrace( );
        }
        if(entree==null || sortie == null) {
            id=-1;
        }
    }

    public void disconnect() {
        try {
            sortie.close() ;
            entree.close() ;
            socket.close();
            gestSock.close() ;
            id=-1;
        }
        catch (IOException e) {
            id=-1;
            e.printStackTrace( );
        }
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public int getId() {
        return id;
    }

    public boolean isConnected() {
        return id==-1 ? false : true;
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

    public void writeHit(int id, int x, int y, int val, int newScore) {
        try {
            sortie.writeInt(id);
            sortie.flush();
            sortie.writeInt(x);
            sortie.flush();
            sortie.writeInt(y);
            sortie.flush();
            sortie.writeInt(val);
            sortie.flush();
            sortie.writeInt(newScore);
            sortie.flush();
        }
        catch (IOException e) {
            e.printStackTrace( );
        }
    }

    public void gameOver(int id) {
        try {
            if(this.id == id) {
                sortie.writeInt(-20);
                sortie.writeInt(-20);
                sortie.writeInt(-20);
                sortie.writeInt(-20);
                sortie.writeInt(-20);
            }
            else {
                sortie.writeInt(-10);
                sortie.writeInt(-10);
                sortie.writeInt(-10);
                sortie.writeInt(-10);
                sortie.writeInt(-10);
            }

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
