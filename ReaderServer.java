/**
 * ReaderServer
 * @author Elliot
 * @version 0.0
 */

 public class ReaderServer implements Runnable {
    private Thread processRead; // thread associé a la classe
    private HandlerClient cli;
    private Serveur serv;
    private boolean connectingBool = false;
    

    /** création thread + son lancement */
    ReaderServer(Serveur serv, HandlerClient cli) {
        this.serv = serv;
        this.cli = cli;
        processRead = new Thread(this) ; // creation du thread
    }

    ReaderServer() {

    }

    public boolean processReadRun() {
        return (processRead != null);
    }

    public boolean isConnecting() {
        return connectingBool;
    }

    public void start() {
        processRead.start() ; // lancement du thread
    }

    public void connecting() {
        connectingBool = true;
    }

    public void readJoueurUpdate() {
        int x = cli.readInt();
        int y = cli.readInt();
        if(serv.getFirstHit()) {
            serv.initChamp(x, y);
            serv.setFirstHit(false);
        }
        serv.testFinPartie(cli.getId(),x,y);
        serv.sendEveryoneHit(cli.getId(),x,y);
    }

    /** surcharge de Runnable : comportement du processus */
    public void run() {
        while (processRead != null) { // mettre condition de sortie
            if(!serv.gameOverBool()) {
                if(connectingBool) {
                    int test = cli.readInt();
                    if(test == 1) {
                        connectingBool = false;
                        if(processRead!=null) processRead = null;
                    }
                    if(test == 0) {
                        if(processRead!=null) processRead = null;
                    }
                }
                else {
                    readJoueurUpdate();
                }
            }
        }
    }

    public void stop() {
        if(processRead!=null) processRead = null;
    }
}
