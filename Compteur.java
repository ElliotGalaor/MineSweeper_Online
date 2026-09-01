/**
 * Compteur pour le score
 * @author Elliot
 * @version 0.0
 */

 public class Compteur implements Runnable {
    private Thread processScores ; // thread associé a la classe
    private App app;
    

    /** création thread + son lancement */
    Compteur(App app) {
        this.app = app;
        processScores = new Thread(this) ; // creation du thread
        processScores.start() ; // lancement du thread
    }

    Compteur() {

    }

    public void incrementeCompteur(App app) {
        app.score++;
        app.majScore();
    }

    /** surcharge de Runnable : comportement du processus */
    public void run() {
        while (processScores != null) { // mettre condition de sortie
            try { 
                Thread.sleep(1000); // dodo 1s
                if(processScores != null) incrementeCompteur(app);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if(processScores!=null) processScores = null;
    }
}
