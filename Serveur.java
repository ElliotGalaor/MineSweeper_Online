//import java.net.* ; //Sockets
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
//import java.io.* ; //Streams
public class Serveur {

    private static int PORT = 10000;
    private List<HandlerClient> listClient;
    private List<ReaderServer> listReader;
    private int nbJoueur;
    private int level;
    private Champ c;
    private ServerSocket gestSock;
    private boolean firstHit = true;
    private boolean gameOver = false;
    
    Serveur() {
        listClient = new ArrayList<>();
        listReader = new ArrayList<>();
        try {
            gestSock=new ServerSocket(PORT);
        }
        catch (IOException e) {
            e.printStackTrace( );
        }
        c = new Champ();
        level = 0;
        nbJoueur = 0;
        System.out.println("Démarrage du serveur") ;
    }

    /**
     * Setter du booléen de fin de partie pour sync le reader et le server
     * @param v
     */
    public void setGameOver(boolean v) {
        gameOver = v;
    }

    /**
     * Getter du booléen de fin de partie
     * @return
     */
    public boolean gameOverBool() {
        return gameOver;
    }

    /**
     * Getter du booléen de début de partie
     * @return
     */
    public boolean getFirstHit() {
        return firstHit;
    }

    /**
     * Setter du booléen de début de partie
     * @param b
     */
    public void setFirstHit(boolean b) {
        firstHit = b;
    }

    /**
     * Getter du nombre de joueur connecté
     * @return
     */
    public int getNbJoueur() {
        return listClient.size();
    }

    /**
     * Getter du handlerClient dans la liste de HandlerClient
     * @param id
     * @return
     */
    public HandlerClient getHandler(int id) {
        return listClient.get(id-1);
    }

    /**
     * Génère un champ vide 
     * @param x
     * @param y
     */
    public void initChamp(int x, int y) {
        c.setLevel(level);
        c.initVideOnline();
        c.init(x,y);
    }

    /**
     * Renvoi la valeur de la case de coordonnées (x,y)
     * @param x
     * @param y
     * @return
     */
    public int getValCase(int x, int y) {
        return c.getVal(x,y);
    }

    /**
     * Envoie à tous les joueurs la valeur de la case, les coordonées de la case, le joueur ayant cliqué et son nouveau score
     * @param id
     * @param x
     * @param y
     */
    public void sendEveryoneHit(int id, int x, int y) {
        int val = c.getVal(x,y);
        int newScore = getHandler(id).getScore();
        for (HandlerClient h : listClient) {
            h.writeHit(id,x,y,val,newScore);
        }
    }

    /**
     * Vérifie si toutes les cases non bombes ont été découvertent
     * @param id
     * @param x
     * @param y
     * @return
     */
    public boolean testFinPartie(int id, int x, int y) {
        c.hitHere(x,y);
        boolean test = c.IsGameWon();
        if(test) {
            for (HandlerClient h : listClient) {
                h.gameOver(id);
            }
            for (ReaderServer r : listReader) {
                r.stop();
            }
            gameOver = true;
        }
        return test;
    }

    /**
     * Connecte un client et l'ajoute à la liste
     * @return
     */
    public boolean connectClient() {
        boolean test = false;
        nbJoueur++;  
        System.out.println("Connexion au joueur n°" + nbJoueur + " ...");
        HandlerClient cli = new HandlerClient(nbJoueur,gestSock);
        ReaderServer v = new ReaderServer(this,cli);
        if(cli.getId()!=-1) {
            String name = cli.readString();
            if(name.length()>0) {
                cli.setNom(name);
                System.out.println("Client " + name + " connecté.") ;
                cli.writeInt(nbJoueur);
                cli.writeInt(level);
                listClient.add(cli);
                listReader.add(v);
                return true;
            }
            else {
                System.out.println("ERR : Client is nameless.") ;
            }
        }
        return test;
    }



    public static void main(String [] args) {
        HandlerClient hand = new HandlerClient();
        Serveur s = new Serveur();
        String step = "Connexion Client";
        
        while(true) {
            switch(step) {
                case "Connexion Client":
                    if(s.connectClient()) {
                        //step = "Confirmation nb de joueur";
                        hand = s.getHandler(s.getNbJoueur());
                        int testDebut = hand.readInt();
                        if(testDebut==1) {
                            for (HandlerClient h : s.listClient) {
                                h.writeInt(1);
                            }
                            for (ReaderServer r : s.listReader) {
                                r.start();
                            }
                            step = "Jeu en cours";
                        }
                    }
                    break;
                case "Jeu en cours":
                    if(s.gameOverBool()) {
                        step = "Fin de partie";
                    }
                    break;
                case "Fin de partie":
                    for(ReaderServer r : s.listReader) {
                        r.connecting();
                    }
                    for(ReaderServer r : s.listReader) {
                        while(r.processReadRun()) {

                        }
                        if(r.isConnecting()) {
                            int i = s.listReader.indexOf(r);
                            s.listClient.remove(i);
                            s.listReader.remove(i);
                        }
                    }
                    if(s.listClient.isEmpty()) {
                        step = "Connexion Client";
                    }
                    else {
                        for(HandlerClient cli : s.listClient) {
                            String name = cli.readString();
                            if(name.length()>0) {
                                cli.setNom(name);
                                System.out.println("Client " + name + " connecté.") ;
                                cli.writeInt(s.listClient.indexOf(cli));
                                cli.writeInt(s.level);
                            }
                            else {
                                System.out.println("ERR : Client is nameless.") ;
                            }
                        }
                        for (ReaderServer r : s.listReader) {
                            r.start();
                        }
                        step = "Jeu en cours";
                    }
                    break;
                default:
                    // code block
            }
        }
    }
}