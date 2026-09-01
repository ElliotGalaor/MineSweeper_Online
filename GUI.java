/**
 * Graphical User Interface
 * @author Bobo
 */
import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GUI extends JPanel implements ActionListener{
    
    private Champ champ;
    private App app;
    private JButton butQuit;
    private JButton butNew;
    private JButton butFlag;
    private JComboBox<Level> levelDifficulty;
    private JPanel panelMines = new JPanel();
    private JLabel label;
    private JMenuItem mQuitter;
    private JMenuItem mNew;
    private JMenuItem mConnect;
    private JMenuItem mDisconnect;
    private JPanel panelUser = new JPanel();
    private JPanel panelEst = new JPanel();
    private JLabel labelEst;
    private JMenuBar menuBar = new JMenuBar(); // création de la barre
    
    //private JFrame frame = new JFrame();

    public void updateScore() {
        label.setText("Score : " + app.score);
    }

    public void updateChat() {
        labelEst.setText(app.chatServeur);
    }

    public void updateMenu() {
        menuBar = new JMenuBar();
        JMenu menuPartie = new JMenu("Partie");
        menuBar.add(menuPartie) ;

        // L’item Quitter
        mQuitter = new JMenuItem("Quitter", KeyEvent.VK_Q);
        menuPartie.add(mQuitter) ;
        mQuitter.addActionListener(this);

        // L’item Nouvelle Partie
        mNew = new JMenuItem("Nouvelle Partie", KeyEvent.VK_N);
        menuPartie.add(mNew) ;
        mNew.addActionListener(this);

        if(app.connected) {
            // L’item Connecter au serveur
            mDisconnect = new JMenuItem("Jouer en solo", KeyEvent.VK_S);
            menuPartie.add(mDisconnect) ;
            mDisconnect.addActionListener(this);
        }
        else {
            // L’item Connecter au serveur
            mConnect = new JMenuItem("Connecter au serveur", KeyEvent.VK_M);
            menuPartie.add(mConnect) ;
            mConnect.addActionListener(this);
        }

        app.setJMenuBar(menuBar) ; // app étant la JFrame
    }

    public JComboBox<Level> getLevelComboBox() {
        return levelDifficulty;
    }

    public void setLevelComboBox(int level) {
        levelDifficulty.setSelectedIndex(level);
    }
    
    GUI(Champ champ, App app) {
        // JPanel des mines
        this.app = app;
        this.champ = champ;
        
        
        BorderLayout panelLayout = new BorderLayout();
        panelUser.setLayout(panelLayout);
        //this.setLocationRelativeTo(null);

        /**
         * Création du menu
         */
        
        // Le menu Partie
        JMenu menuPartie = new JMenu("Partie");
        menuBar.add(menuPartie) ;

        // L’item Quitter
        mQuitter = new JMenuItem("Quitter", KeyEvent.VK_Q);
        menuPartie.add(mQuitter) ;
        mQuitter.addActionListener(this);

        // L’item Nouvelle Partie
        mNew = new JMenuItem("Nouvelle Partie", KeyEvent.VK_N);
        menuPartie.add(mNew) ;
        mNew.addActionListener(this);

        // L’item Connecter au serveur
        mConnect = new JMenuItem("Connecter au serveur", KeyEvent.VK_S);
        menuPartie.add(mConnect) ;
        mConnect.addActionListener(this);

        app.setJMenuBar(menuBar) ; // app étant la JFrame


        
        /**
         * Définition et paramétrage du titre
         */
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new FlowLayout());

        label = new JLabel("Score : " + app.score);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panelNorth.add(label);

        //String[] difficulty = { "Easy", "Normal", "Hard", "Very Hard", "Impossible", "Custom" };
        levelDifficulty = new JComboBox<>(Level.values());
        
        panelNorth.add(levelDifficulty);
        panelUser.add(panelNorth,BorderLayout.NORTH);
        
        labelEst = new JLabel(app.chatServeur);
        panelEst.add(labelEst);
        panelUser.add(panelEst,BorderLayout.EAST);

        /**
         * Définition et paramétrage du démineur
         */
        //majPanelMines();
        majCaseMines();
        panelUser.add(panelMines,BorderLayout.CENTER);

        /**
         * Définition panel des boutons
         */
        JPanel panelSouth = new JPanel();
        panelSouth.setLayout(new FlowLayout());

        butQuit = new JButton("Quit");
        butQuit.addActionListener(this);
        panelSouth.add(butQuit);

        butNew = new JButton("New Game");
        butNew.addActionListener(this);
        panelSouth.add(butNew);

        butFlag = new JButton("Flag");
        butFlag.addActionListener(this);
        panelSouth.add(butFlag);

        panelUser.add(panelSouth,BorderLayout.SOUTH);

        panelUser.setBackground(Color.DARK_GRAY);
        add(panelUser);

    }


    GUI(App app) {
        //setLayout(mgr);


        // JLabel label = new JLabel("Coucou");    //création de contenu dans le JPanel
        // add(label);                 //ajout du contenu dans le JPanel

        // JPanel des mines
        this.app = app;
        this.champ = app.c;
        JPanel panelMines = new JPanel();
        JPanel panelUser = new JPanel();
        BorderLayout panelLayout = new BorderLayout();
        panelUser.setLayout(panelLayout);
        
        /**
         * Définition et paramétrage du titre
         */
        JLabel label = new JLabel("Démineur");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panelUser.add(label,BorderLayout.NORTH);
        
        /**
         * Définition et paramétrage du démineur
         */
        panelMines.setLayout(new GridLayout(champ.getHeight(),champ.getWidth()));
        for(int i = 0 ; i < champ.getHeight() ; i++) {
            for(int j = 0 ; j < champ.getWidth() ; j++) {
                if(champ.isMines(i,j)) {
                    JLabel val = new JLabel(" ¤ ");
                    val.setForeground(Color.BLACK);
                    panelMines.add(val);
                }
                else {
                    int value = champ.getVal(i,j);
                    JLabel val = new JLabel(" " + String.valueOf(value) + " ");
                    if(value == 1) { val.setForeground(Color.BLUE); }
                    if(value == 2) { val.setForeground(Color.GREEN); }
                    if(value == 3) { val.setForeground(Color.RED); }
                    if(value == 4) { val.setForeground(Color.YELLOW); }
                    if(value == 5) { val.setForeground(Color.PINK); }
                    if(value == 6) { val.setForeground(Color.DARK_GRAY); }
                    if(value == 7) { val.setForeground(Color.MAGENTA); }
                    if(value == 8) { val.setForeground(Color.ORANGE); }
                    if(value == 0) {
                        val = new JLabel("  ");
                    }
                    panelMines.add(val);
                }
            }
        }
        panelMines.setBackground(Color.DARK_GRAY);
        panelUser.add(panelMines,BorderLayout.CENTER);

        /**
         * Définition panel des boutons
         */
        JPanel panelSouth = new JPanel();
        panelSouth.setLayout(new FlowLayout());
        butQuit = new JButton("Quit");
        butQuit.addActionListener(this);
        panelSouth.add(butQuit);
        butNew = new JButton("New Game");
        butNew.addActionListener(this);
        panelSouth.add(butNew);
        panelUser.add(panelSouth,BorderLayout.SOUTH);


        add(panelUser);
    }

    GUI(Champ champ) {
        //setLayout(mgr);


        // JLabel label = new JLabel("Coucou");    //création de contenu dans le JPanel
        // add(label);                 //ajout du contenu dans le JPanel

        // JPanel des mines
        JPanel panelMines = new JPanel();
        JPanel panelUser = new JPanel();
        BorderLayout panelLayout = new BorderLayout();
        panelUser.setLayout(panelLayout);
        
        /**
         * Définition et paramétrage du titre
         */
        JLabel label = new JLabel("Démineur");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panelUser.add(label,BorderLayout.NORTH);
        
        /**
         * Définition et paramétrage du démineur
         */
        panelMines.setLayout(new GridLayout(champ.getHeight(),champ.getWidth()));
        for(int i = 0 ; i < champ.getHeight() ; i++) {
            for(int j = 0 ; j < champ.getWidth() ; j++) {
                if(champ.isMines(i,j)) {
                    JLabel val = new JLabel(" ¤ ");
                    val.setForeground(Color.BLACK);
                    panelMines.add(val);
                }
                else {
                    int value = champ.getVal(i,j);
                    JLabel val = new JLabel(" " + String.valueOf(value) + " ");
                    if(value == 1) { val.setForeground(Color.BLUE); }
                    if(value == 2) { val.setForeground(Color.GREEN); }
                    if(value == 3) { val.setForeground(Color.RED); }
                    if(value == 4) { val.setForeground(Color.YELLOW); }
                    if(value == 5) { val.setForeground(Color.PINK); }
                    if(value == 6) { val.setForeground(Color.DARK_GRAY); }
                    if(value == 7) { val.setForeground(Color.MAGENTA); }
                    if(value == 8) { val.setForeground(Color.ORANGE); }
                    if(value == 0) {
                        val = new JLabel("  ");
                    }
                    panelMines.add(val);
                }
            }
        }
        panelMines.setBackground(Color.DARK_GRAY);
        panelUser.add(panelMines,BorderLayout.CENTER);

        JButton butQuit = new JButton("Quit");
        butQuit.addActionListener(this);


        add(panelUser);
    }

    public void newGame(int level) {
        panelMines.removeAll();
        majCaseMines();
        //majPanelMines();
        //majHidden();
        app.pack();
    }

    /**
     * Pas utilisé
     */
    public void majHidden() {
        panelMines.setLayout(new GridLayout(champ.getHeight(),champ.getWidth()));
        panelMines.setBorder(BorderFactory.createEmptyBorder(champ.getWidth(), champ.getHeight(),champ.getWidth(), champ.getHeight()));
        for(int i = 0 ; i < champ.getHeight() ; i++) {
            for(int j = 0 ; j < champ.getWidth() ; j++) {
                JLabel val = new JLabel("   ");
                val.setBackground(Color.LIGHT_GRAY);
                val.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                panelMines.add(val);
            }
        }
        panelMines.setBackground(Color.DARK_GRAY);
    }

    /**
     * Actuellement utilisé
     */
    public void majCaseMines() {
        panelMines.setLayout(new GridLayout(champ.getHeight(),champ.getWidth()));
        panelMines.setBorder(BorderFactory.createEmptyBorder(champ.getWidth(), champ.getHeight(),champ.getWidth(), champ.getHeight()));
        for(int i = 0 ; i < champ.getHeight() ; i++) {
            for(int j = 0 ; j < champ.getWidth() ; j++) {
                Case caseMine = new Case(app);
                caseMine = champ.getChampMines(i,j);
                panelMines.add(caseMine);
            }
        }
        panelMines.setBackground(app.connected ? new Color(0x42c5f5) : Color.DARK_GRAY);

    }

    /**
     * Pas utilisé
     */
    public void majPanelMines() {
        panelMines.setLayout(new GridLayout(champ.getHeight(),champ.getWidth()));
        panelMines.setBorder(BorderFactory.createEmptyBorder(champ.getWidth(), champ.getHeight(),champ.getWidth(), champ.getHeight()));
        for(int i = 0 ; i < champ.getHeight() ; i++) {
            for(int j = 0 ; j < champ.getWidth() ; j++) {
                if(champ.isMines(i,j)) {
                    JLabel val = new JLabel(" ¤ ");
                    val.setForeground(Color.BLACK);
                    val.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    panelMines.add(val);
                }
                else {
                    int value = champ.getVal(i,j);
                    JLabel val = new JLabel(" " + String.valueOf(value) + " ");
                    if(value == 1) { val.setForeground(Color.BLUE); }
                    if(value == 2) { val.setForeground(Color.GREEN); }
                    if(value == 3) { val.setForeground(Color.RED); }
                    if(value == 4) { val.setForeground(Color.YELLOW); }
                    if(value == 5) { val.setForeground(Color.PINK); }
                    if(value == 6) { val.setForeground(Color.DARK_GRAY); }
                    if(value == 7) { val.setForeground(Color.MAGENTA); }
                    if(value == 8) { val.setForeground(Color.ORANGE); }
                    if(value == 0) {
                        val = new JLabel("  ");
                    }
                    val.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    panelMines.add(val);
                }
            }
        }
        panelMines.setBackground(Color.DARK_GRAY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==butQuit || e.getSource()==mQuitter) {
            app.verifQuit();
        }
        if(e.getSource()==butNew || e.getSource()==mNew) {
            app.newGame(levelDifficulty.getSelectedIndex());
        }
        if(e.getSource()==butFlag) {
            app.flagMode = app.newGame ? false : !app.flagMode;
        }
        if(e.getSource() == mConnect) {
            app.connect();
        }
        if(e.getSource() == mDisconnect) {
            app.disconnect();
        }
        System.out.println("La taille du champ est : " + champ.getWidth());
    }
}
