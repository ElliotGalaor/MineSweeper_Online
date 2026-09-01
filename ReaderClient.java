/**
 * ReaderClient
 * @author Elliot
 * @version 0.0
 */

 public class ReaderClient implements Runnable {
    private Thread processRead; // thread associé a la classe
    private HandlerServeur serv;
    private App app;
    private boolean started = false;
    

    /** création thread + son lancement */
    ReaderClient(App app, HandlerServeur serv) {
        this.serv = serv;
        this.app = app;
        processRead = new Thread(this) ; // creation du thread
        processRead.start() ; // lancement du thread
    }

    ReaderClient() {

    }


    public void readJoueurUpdate() {
        int id = serv.readInt();
        int x = serv.readInt();
        int y = serv.readInt();
        int val = serv.readInt();
        int newScore = serv.readInt();
        if(id==-10) {
            app.gameOverOnline();
            if(processRead!=null) processRead = null;
        }
        else if (id == -20) {
            app.winOnline();
            if(processRead!=null) processRead = null;
        }
        else {
            app.updateExt(id,x,y,val,newScore);
            if(app.numJoueur==id) {
                app.score++;
                app.majScore();
                if(val == -1) {
                    app.gameOverOnline();
                }
            }
        }        
    }

    /** surcharge de Runnable : comportement du processus */
    public void run() {
        int i = 0;
        while (processRead != null) { // mettre condition de sortie
            if(!started) {
                i = serv.readInt();
                if(i == 1) {
                    started = true;
                }
                else {
                    stop();
                }
            }
            readJoueurUpdate();
        }
    }

    public void stop() {
        if(processRead!=null) processRead = null;
    }
}
