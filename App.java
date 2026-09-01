/**
 * Magnifique programme
 * @author Bobo
 * @version 1.0
 */
//import java.awt.Color;

// import java.io.DataInputStream;
// import java.io.DataOutputStream;
// import java.io.IOException;
// import java.net.Socket;
// import java.net.UnknownHostException;

import javax.swing.*;

public class App extends JFrame {

    boolean flagMode = false;
    boolean newGame = true;
    ImageIcon iconLoser = new ImageIcon("./src/Loser.jpg");
    ImageIcon iconWinner = new ImageIcon("./src/Winner.jpg");
    ImageIcon iconSad = new ImageIcon("./src/Sad.jpg");
    ImageIcon iconInternet = new ImageIcon("./src/Internet.png");
    ImageIcon iconGameOver = new ImageIcon("./src/GameOver.png");
    Champ c = new Champ(this);
    GUI gui;
    int score = 0;
    boolean gameOverBool = false;
    boolean connected = false;
    Compteur compteur;
    String chatServeur = "";
    HandlerServeur serv = new HandlerServeur();
    ReaderClient servCom;
    int numJoueur;
    
/**
 * Constructeur côté serveur (App sans affichage)
 */
    App() {
        c.initVide();
        c.displayValue();    
        // setBackground(Color.DARK_GRAY);    
    }

    /**
     * Constructeur côté client
     * @param x
     * @param y
     */
    App(int x, int y) {
        c.init(x,y);
        c.displayValue();

    }

    /**
     * Initialisation de l'affichage (GUI)
     */
    public void launch() {
        gui = new GUI(c,this);
        setContentPane(gui);            //affectation du JPanel dans la Jframe

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setLocationRelativeTo(null);
        //this.setLocation(getX()-(getWidth())/2, getY()-(getHeight()/2));
    }

    /**
     * Met à jour le score du client
     */
    public void majScore() {
        gui.updateScore();
    }

    /**
     * Gestion de fin de partie (défaite) en solo
     */
    public void gameOver() {
        compteur.stop();
        gameOverBool = true;
        c.showAll();
        int response = JOptionPane.showOptionDialog(
                null,
                "Game Over! Would you like to play again or quit?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                iconLoser, // No custom icon
                new Object[]{"Play Again", "Quit"}, 
                "Play Again" 
        );

        if (response == JOptionPane.YES_OPTION) {
            newGame(gui.getLevelComboBox().getSelectedIndex());
        } else {
           quit();
        }
    }

    /**
     * Gestion de fin de partie (Victoire) en solo
     */
    public void win() {
        compteur.stop();
        gameOverBool = true;
        int response = JOptionPane.showOptionDialog(
                null,
                "You Win ! Would you like to play again or quit?",
                "Victory",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                iconWinner, // No custom icon
                new Object[]{"Play Again", "Quit"}, 
                "Play Again" 
        );

        if (response == JOptionPane.YES_OPTION) {
            newGame(gui.getLevelComboBox().getSelectedIndex());
            newGame = true;
        } else {
           quit();
        }
    }

    /**
     * Affichage du Dialog de vérification pour quitter le démineur
     */
    public void verifQuit() {
        int response = JOptionPane.showOptionDialog(
            null,
            "Are you sure you want to quit ?",
            "Quitting menu",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            iconSad, // No custom icon
            new Object[]{"Yes", "No"}, 
            "No" 
        );

        if (response == JOptionPane.YES_OPTION) {
            quit();
        } else {
            
        }
    }

    /**
     * 
     * @param args not used
     * @throws Exception It's not my problem
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Démineur");
        App app = new App();
        app.launch();
        
        
        
        //System.out.println(c.getVal(1,1));
    }

    /**
     * Met à jour l'affichage en multijoueur, 
     * Score des autres joueur pas encore implémenté
     * @param id
     * @param x
     * @param y
     * @param val
     * @param newScore
     */
    public void updateExt(int id, int x, int y, int val, int newScore) {
        c.setValueCase(x, y, val);
        if(connected) {
            c.showMinesOnline(x,y);
        }
    }

    /**
     * Connexion au serveur et lancement de la partie multijoueur
     */
    public void connect() {
        if(compteur!=null) compteur.stop();
        connected = serv.connect();
        if(connected) {
            serv.writeString("Elliot");

            numJoueur = serv.readInt(); // reception d’un nombre
            System.out.println("Joueur n°: "+numJoueur);
            chatServeur = chatServeur + "\nJoueur n°: " + numJoueur;
            gui.updateChat();

            int dimChamp = serv.readInt();
            chatServeur+="\n Grille de level " + dimChamp;
            gui.updateChat();
            gui.updateMenu();
            newGameOnline(dimChamp);  
            int startGame = JOptionPane.showOptionDialog(
                null,
                "Il y a " + numJoueur + " joueur(s) dans la partie, voulez vous commencer ?",
                "Partie en ligne",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                iconInternet, // No custom icon
                new Object[]{"Yes", "No"}, 
                "No" 
            ); 
            if (startGame == JOptionPane.YES_OPTION) {
                serv.writeInt(1);
                //if(serv.readInt()==0) { disconnect(); }
                servCom = new ReaderClient(this,serv);
            } else {
                serv.writeInt(0);
                JOptionPane.showMessageDialog(new JLabel(), "En attente d'un autre joueur...");
                //if(serv.readInt()==0) { disconnect(); }
                servCom = new ReaderClient(this,serv);
            }
            
        }  
        else {
            chatServeur = "Connexion au serveur impossible";
            gui.updateChat();
            pack();
        }    
    }

    /**
     * Déconnexion du serveur
     */
    public void disconnect() {
        servCom.stop();
        serv.disconnect();
        connected = false;
        chatServeur="";
        gui.updateChat();
        gui.updateMenu();
        newGame(0);
    }


    /**
     * Gestion de la fin de partie (Victoire) en multi
     */
    public void winOnline() {
        int endGame = JOptionPane.showOptionDialog(
                null,
                "Vous avez gagné ! Votre score : " + score + "\n Voulez vous recommencer ?",
                "Fin de la partie",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                iconWinner, // No custom icon
                new Object[]{"Oui", "Non"}, 
                "Oui" 
            ); 
        if (endGame == JOptionPane.YES_OPTION) {
            serv.writeInt(1);
            serv.writeString("Elliot");

            numJoueur = serv.readInt(); // reception d’un nombre
            System.out.println("Joueur n°: "+numJoueur);
            chatServeur = chatServeur + "\nJoueur n°: " + numJoueur;
            gui.updateChat();

            int dimChamp = serv.readInt();
            chatServeur+="\n Grille de level " + dimChamp;
            gui.updateChat();
            gui.updateMenu();
            newGameOnline(dimChamp);  
            servCom = new ReaderClient(this,serv);
        } else {
            serv.writeInt(0);
            disconnect();
        }
    }

    /**
     * Gestion de la fin de partie (défaite) en multi
     */
    public void gameOverOnline() {
        int endGame = JOptionPane.showOptionDialog(
                null,
                "Fin de la partie, votre score : " + score + "\n Voulez vous recommencer ?",
                "Fin de la partie",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                iconGameOver, // No custom icon
                new Object[]{"Oui", "Non"}, 
                "Oui" 
            ); 
        if (endGame == JOptionPane.YES_OPTION) {
            serv.writeInt(1);
            serv.writeString("Elliot");

            numJoueur = serv.readInt(); // reception d’un nombre
            System.out.println("Joueur n°: "+numJoueur);
            chatServeur = chatServeur + "\nJoueur n°: " + numJoueur;
            gui.updateChat();

            int dimChamp = serv.readInt();
            chatServeur+="\n Grille de level " + dimChamp;
            gui.updateChat();
            gui.updateMenu();
            newGameOnline(dimChamp);  
            servCom = new ReaderClient(this,serv);
        } else {
            serv.writeInt(0);
            disconnect();
        }
    }

    /**
     * Affichage du layout des bombe dans la console
     * @param c
     */
    public void menu(Champ c) {
        c.displayHidden();
    }

    /**
     * Ferme le démineur
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Gestion du premier coup de la partie en solo pour générer la bombeMap
     * @param x
     * @param y
     */
    public void clickedHere(int x, int y) {
        newGame = false;
        c.init(x,y);
        c.displayValue();  
        c.showMines(x,y);
        compteur = new Compteur(this);
    }

    /**
     * Gestion des coups en ligne (envoi des coordonnées au serveur)
     * @param x
     * @param y
     */
    public void clickedHereOnline(int x, int y) {
        serv.writeInt(x);
        serv.writeInt(y);
    }

    /**
     * Not used
     */
    // public void newGame() {
    //     //c = new Champ();
    //     compteur.stop();
    //     score = 0;
    //     gameOverBool = false;
    //     c.initVide();
    //     launch();
    // }

    /**
     * Nouvelle partie en solo
     * Currently used
     * @param level
     */
    public void newGame(int level) {
        //c = new Champ();
        newGame = true;
        if(compteur!=null) compteur.stop();
        if(!connected) {
            chatServeur = "";
            gui.updateChat();
        }
        flagMode = false;
        score = 0;
        majScore();
        c.newGame(level);
        gui.newGame(level);
        pack();
        setLocationRelativeTo(null);
        newGame = true;
        gameOverBool = false;
    }

    /**
     * Nouvelle partie en multi
     * @param level
     */
    public void newGameOnline(int level) {
        gui.setLevelComboBox(level);
        if(compteur!=null) compteur.stop();
        flagMode = false;
        newGame = true;
        score = 0;
        majScore();
        gameOverBool = false;
        c.newGame(level);
        gui.newGame(level);
        pack();
        setLocationRelativeTo(null);
    }

}
