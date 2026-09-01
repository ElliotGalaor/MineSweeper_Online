/**
 * Graphical User Interface
 * @author Bobo
 */
import javax.swing.*;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Case extends JPanel implements MouseListener{
    private App app;
    private String txt = "";
    private int dim = 20;
    public int x;
    public int y;
    private int valueCase = 999;
    private Color c = Color.RED;
    private Color colorCase = Color.LIGHT_GRAY;
    private boolean flag = false;
    private boolean shown = false;
    private Toolkit toolkit = getToolkit(); //env. outil
    private boolean boom = false;
    private boolean hoverFlag = false;
    private boolean mouseClicked = false;

    public int getValue() {
        return valueCase;
    }

    public void setValue(int val) {
        valueCase = val;
    }

    public boolean getShown() {
        return shown;
    }

    public void setShown(boolean val) {
        shown = val;
    }

    public Case() {
        flag = false;
    }

    public Case (App app) {
        flag = false;
        this.app = app;
        setPreferredSize(new Dimension(dim,dim)); // taille de la case
        addMouseListener(this); // ajout listener souris
        setBorder(BorderFactory.createLineBorder(app.connected ? new Color(0x850707) : Color.WHITE));
        setBackground(app.connected ? new Color(0x42c5f5) : Color.DARK_GRAY);
    }
    
    /** le dessin de la case */
    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc); // appel méthode mère (efface le dessin précedent)
        if(!flag) {
            if(boom) {
                gc.setColor(Color.RED);
                gc.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
                gc.drawImage(toolkit.getImage("./src/Mine.png"), (getWidth()-20)/2,0,this);
            }
            else {
                gc.setColor(colorCase);
                gc.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
                gc.setColor(c);
                gc.drawString(txt, getWidth()/2 - 3, getHeight()/2 + 5); // dessin du texte à la position 10, 10
            }
        }
        else {
            gc.setColor(Color.BLUE);
            gc.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
            //gc.drawImage(toolkit.getImage("./src/Flag.png"), (getWidth()-20)/2,0,this);
            //System.out.println(didIt); 
        }
        mouseClicked = false;
    }

    public void showCaseGameOver() {
        if(valueCase == -1) {
            c = Color.BLACK;
            colorCase = Color.red;
            txt = "¤";
            //boom = true;
        }
        repaint(); // comme on veut redessiner, on force l’appel de paintComponent()
    }

    public void showCaseOnline() {
        if(valueCase == -1) {
            c = Color.BLACK;
            colorCase = Color.red;
            txt = "¤";
        }
        else {
            colorCase = Color.DARK_GRAY;
            txt = String.valueOf(valueCase); // chgt du texte à redessiner
            if(valueCase == 0) { c = Color.CYAN; }
            if(valueCase == 1) { c = Color.BLUE; }
            if(valueCase == 2) { c = Color.GREEN; }
            if(valueCase == 3) { c = Color.RED; }
            if(valueCase == 4) { c = Color.YELLOW; }
            if(valueCase == 5) { c = Color.PINK; }
            if(valueCase == 6) { c = Color.WHITE; }
            if(valueCase == 7) { c = Color.MAGENTA; }
            if(valueCase == 8) { c = Color.ORANGE; }
        }
        repaint(); // comme on veut redessiner, on force l’appel de paintComponent()
    }

    public void showCase() {
        if(!shown) {
            if(valueCase == -1) {
                c = Color.BLACK;
                colorCase = Color.red;
                txt = "¤";
                //boom = true;
                if(!app.gameOverBool) {
                    app.gameOver();
                }
            }
            else if(valueCase == 0){
                colorCase = Color.DARK_GRAY;
                txt = " ";
            }
            else {
                colorCase = Color.DARK_GRAY;
                txt = String.valueOf(valueCase); // chgt du texte à redessiner
                if(valueCase == 1) { c = Color.BLUE; }
                if(valueCase == 2) { c = Color.GREEN; }
                if(valueCase == 3) { c = Color.RED; }
                if(valueCase == 4) { c = Color.YELLOW; }
                if(valueCase == 5) { c = Color.PINK; }
                if(valueCase == 6) { c = Color.WHITE; }
                if(valueCase == 7) { c = Color.MAGENTA; }
                if(valueCase == 8) { c = Color.ORANGE; }
                // if(!app.gameOverBool) {
                //     app.score += valueCase;
                //     app.majScore();
                // }
            }
            repaint(); // comme on veut redessiner, on force l’appel de paintComponent()
            shown = true;
            if(valueCase != -1 && !app.gameOverBool) {
                app.c.caseFound -= 1;
                if(app.c.caseFound == 0) {
                    app.win();
                }
                System.out.println(app.c.minesFound);
            }
            if(valueCase==0 && !app.gameOverBool && !flag) {
                app.c.showMines(x+1, y);
                app.c.showMines(x+1, y+1);
                app.c.showMines(x+1, y-1);
                app.c.showMines(x, y-1);
                app.c.showMines(x, y+1);
                app.c.showMines(x-1, y);
                app.c.showMines(x-1, y+1);
                app.c.showMines(x-1, y-1);
            }
        }
    }

    public void putFlag() {
        flag = !flag;
        repaint();
    }

    public boolean isFlag() {
        return flag;
    }

    /** la gestion de la souris */
    @Override
    public void mousePressed (MouseEvent e) {
        if(app.connected) {
            if(!shown) {
                app.clickedHereOnline(x, y);
                shown = true;
            }
        }
        else {
            if(app.newGame || valueCase == 999) {
                app.clickedHere(x, y);
            }
            else {
            mouseClicked = true;
                if(SwingUtilities.isLeftMouseButton(e) && !(flag && !app.flagMode) && !shown) {
                    if(app.flagMode && !shown && !hoverFlag) {
                        putFlag();
                    }
                    else if(!app.flagMode && !shown) {      /// on peut pas rentrer ici si on est en flagmode
                        showCase();
                    }
                }
                else if(SwingUtilities.isLeftMouseButton(e) && shown) {
                    if(app.c.nbFlagAround(x,y) == valueCase) {
                        app.c.showNotFlagged(x,y);
                    }
                }
                if(SwingUtilities.isRightMouseButton(e) && !shown) {
                    if(!hoverFlag) {
                        putFlag();
                    }
                }
            }
            app.newGame = false;
        }
    }

    /** la gestion de la souris */
    @Override
    public void mouseEntered (MouseEvent e) {
        System.out.print(x + ", " + y + " : " + flag + "\n");
        if(!app.connected) {
            if(flag) {
                hoverFlag = false;
            }
            if(!shown && !flag && app.flagMode && !mouseClicked) {
                hoverFlag = true;
                flag = true;
                repaint();
            }
        }
    }

    /** la gestion de la souris */
    @Override
    public void mouseReleased (MouseEvent e) {

    }

    /** la gestion de la souris */
    @Override
    public void mouseClicked (MouseEvent e) {

    }

    /** la gestion de la souris */
    @Override
    public void mouseExited (MouseEvent e) {
        //System.out.print(x + ", " + y + " : Out\n");
        if(hoverFlag && !app.connected) { ///pbm a résoudre
            hoverFlag = false;
            if(!mouseClicked) {
                flag = false;
                repaint(); 
            }
        }
    }
}