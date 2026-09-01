import java.util.*;

/**
 * 
 */
public class Champ {
    private App app;

    private static int DEF_HEIGHT = 5;
    private static int DEF_WIDTH = 5;
    private int level = 0;

    private boolean[][] bombeMap = new boolean[DEF_HEIGHT][DEF_WIDTH];
    private static int[] tabSize = { 5, 10, 15, 20, 25};
    private static int[] tabMines = { 1, 14, 40, 100, 180};
    private Case[][] champMines = new Case[DEF_HEIGHT][DEF_HEIGHT];
    public int minesFound = tabMines[0];
    public int caseFound = tabSize[0]*tabSize[0]-tabMines[0];


    private Random generator = new Random();

    public Champ(App app) {
        this.app = app;
    }

    public Champ() {

    }

    public void hitHere(int x, int y) {
        champMines[x][y].setShown(true);
    }

    public boolean IsGameWon() {
        int res = 0;
        for(int i = 0 ; i < champMines.length ; i++) {
            for(int j = 0 ; j < champMines[0].length ; j++) {
                if(champMines[i][j].getShown() && !isMines(i,j)) {
                    res++;
                }
            }
        }
        return (res==tabSize[level]*tabSize[level]-tabMines[level]);
    }

    public int getDim() {
        return tabSize[level];
    }

    public static int getLevel(int dim) {
        for(int i=0;i<tabSize.length;i++) {
            if(tabSize[i]==dim) return i;
        }
        return 0;
    }

    public void setLevel(int i) {
        level = i;
    }

    public void changeMine(int i, int j) {
        System.out.println("Changed successfully");
        app.newGame = false;
        bombeMap[i][j] = false;
        champMines[i][j].setValue(getVal(i, j));
        for(int n = 1 ; n != 0 ;) {
            int x = generator.nextInt(bombeMap.length);
            int y = generator.nextInt(bombeMap[0].length);
            if(!(bombeMap[x][y] || (x == i && y==j))) {
                bombeMap[x][y] = true;
                champMines[x][y].setValue(-1);
            }
        }
    }

    public void clickedHere(int x, int y) {
        app.newGame = false;
    }

    public void showAll() {
        for(int i = 0 ; i < bombeMap.length ; i++) {
            for(int j = 0 ; j < bombeMap[0].length ; j++) {
                champMines[i][j].showCaseGameOver();
            }
        }
    }

    public Case getChampMines(int i,int j) {
        return champMines[i][j];
    }

    public int getHeight() {
        return bombeMap.length;
    }

    public int getWidth() {
        return bombeMap[0].length;
    }

    /**
     * Not used
     */
    public void init_50_50() {
        bombeMap = new boolean[bombeMap.length][bombeMap[0].length];
        for(int i = 0 ; i < bombeMap.length ; i++) {
            for(int j = 0 ; j < bombeMap[0].length ; j++) {
                bombeMap[i][j] = generator.nextBoolean();
            }
        }
    }

    public void showMines(int i, int j) {
        try {
            champMines[i][j].showCase();
        }
        catch(Exception e) {
            
        }
    }

    public void showMinesOnline(int i, int j) {
        try {
            champMines[i][j].showCaseOnline();
        }
        catch(Exception e) {
            
        }
    }

    public boolean isMines(int i, int j) {
        return bombeMap[i][j];
    }

    public void showNotFlagged(int i, int j) {
        for(int x=i-1 ; x <= i+1; x++) {
            for(int y=j-1 ; y <= j+1 ; y++) {
                if(x != -1 && x != bombeMap.length && y != -1 && y != bombeMap[0].length && !champMines[x][y].isFlag()) {
                    showMines(x, y);
                }
            }
        }
    }

    public int nbFlagAround(int i, int j) {
        int n = 0;
        for(int x=i-1 ; x <= i+1; x++) {
            for(int y=j-1 ; y <= j+1 ; y++) {
                if(x != -1 && x != bombeMap.length && y != -1 && y != bombeMap[0].length && champMines[x][y].isFlag()) {
                    n++;
                }
            }
        }
        return n;
    }

    public boolean checkZero(int x,int y, int startX,int startY) {
        return (x == startX && y==startY) || (x+1 == startX && y==startY) || (x-1 == startX && y==startY) || (x == startX && y+1==startY) || (x+1 == startX && y+1==startY) || (x-1 == startX && y+1==startY) || (x == startX && y-1==startY) || (x+1 == startX && y-1==startY) || (x-1 == startX && y-1==startY);
    }

    /**
     * Currently used
     * @param startX
     * @param startY
     */
    public void init(int startX, int startY) {
        minesFound = tabMines[level];
        caseFound = tabSize[level]*tabSize[level]-tabMines[level];
        bombeMap = new boolean[bombeMap.length][bombeMap[0].length];
        for(int n = tabMines[level] ; n != 0 ;) {
            int x = generator.nextInt(bombeMap.length);
            int y = generator.nextInt(bombeMap[0].length);
            if(!(bombeMap[x][y] || checkZero(x,y,startX,startY))) {
                bombeMap[x][y] = true;

                n--;
            }
        }
        initChampVal();
    }

    public void initVide() {
        minesFound = tabMines[level];
        caseFound = tabSize[level]*tabSize[level]-tabMines[level];
        bombeMap = new boolean[bombeMap.length][bombeMap[0].length];
        for(int n = tabMines[level] ; n != 0 ;) {
            int x = generator.nextInt(bombeMap.length);
            int y = generator.nextInt(bombeMap[0].length);
            if(!bombeMap[x][y]) {
                bombeMap[x][y] = true;
                n--;
            }
        }
        initChamp();
    }

    public void initVideOnline() {
        minesFound = tabMines[level];
        caseFound = tabSize[level]*tabSize[level]-tabMines[level];
        bombeMap = new boolean[tabSize[level]][tabSize[level]];
        for(int n = tabMines[level] ; n != 0 ;) {
            int x = generator.nextInt(bombeMap.length);
            int y = generator.nextInt(bombeMap[0].length);
            if(!bombeMap[x][y]) {
                bombeMap[x][y] = true;
                n--;
            }
        }
        initChampOnline();
    }

    public void initChampOnline() {
        champMines = new Case[bombeMap.length][bombeMap[0].length];
        for(int i = 0 ; i < bombeMap.length ; i++) {
            for(int j = 0 ; j < bombeMap[0].length ; j++) {
                if(bombeMap[i][j]) {
                    champMines[i][j] = new Case();
                    champMines[i][j].x = i;
                    champMines[i][j].y = j;
                    //champMines[i][j].setValue(-1);
                }
                else {
                    champMines[i][j] = new Case();
                    champMines[i][j].x = i;
                    champMines[i][j].y = j;
                    //champMines[i][j].setValue(getVal(i, j));
                }
            }
        }
    }

    public void initChamp() {
        champMines = new Case[bombeMap.length][bombeMap[0].length];
        for(int i = 0 ; i < bombeMap.length ; i++) {
            for(int j = 0 ; j < bombeMap[0].length ; j++) {
                if(bombeMap[i][j]) {
                    champMines[i][j] = new Case(app);
                    champMines[i][j].x = i;
                    champMines[i][j].y = j;
                    //champMines[i][j].setValue(-1);
                }
                else {
                    champMines[i][j] = new Case(app);
                    champMines[i][j].x = i;
                    champMines[i][j].y = j;
                    //champMines[i][j].setValue(getVal(i, j));
                }
            }
        }
    }

    public void initChampVal() {
        for(int i = 0 ; i < bombeMap.length ; i++) {
            for(int j = 0 ; j < bombeMap[0].length ; j++) {
                if(bombeMap[i][j]) {
                    champMines[i][j].setValue(-1);
                }
                else {
                    champMines[i][j].setValue(getVal(i, j));
                }
            }
        }
    }

    public void setValueCase(int x, int y, int val) {
        champMines[x][y].setValue(val);
    }

    public void newGame(int level) {
        this.level = level;
        bombeMap = new boolean[tabSize[level]][tabSize[level]];
        initVide();
        displayValue();
    }

    public void newGameOnline(int level) {
        this.level = level;
        bombeMap = new boolean[tabSize[level]][tabSize[level]];
        initVide();
        displayValue();
    }

    public void display() {
        for(int i = 0 ; i < bombeMap.length ; i++) {
            for(int j = 0 ; j < bombeMap[0].length ; j++) {
                if(bombeMap[i][j]) {
                    System.out.print("X");
                }
                else {
                    System.out.print("O");
                }
            }
            System.out.println("");
        }
    }

    public void displayValue() {
        for(int i = 0 ; i < bombeMap.length ; i++) {
            for(int j = 0 ; j < bombeMap[0].length ; j++) {
                if(bombeMap[i][j]) {
                    System.out.print("*");
                }
                else {
                    System.out.print(getVal(i,j));
                }
            }
            System.out.println("");
        }
    }

    public void displayHidden() {
        for(int i = 0 ; i < bombeMap.length ; i++) {
            for(int j = 0 ; j < bombeMap[0].length ; j++) {
                System.out.print("*");
            }
            System.out.println("");
        }
    }

    public int nbMinesAroundPasPropre(int i, int j) {
        int n = 0;
        for(int x=i-1 ; x <= i+1; x++) {
            for(int y=j-1 ; y <= j+1 ; y++) {
                if(bombeMap[x][y]) {
                    n++;
                }
            }
        }
        return n;
    }

    public int nbMinesAround(int i, int j) {
        int n = 0;
        for(int x=i-1 ; x <= i+1; x++) {
            for(int y=j-1 ; y <= j+1 ; y++) {
                if(x != -1 && x != bombeMap.length && y != -1 && y != bombeMap[0].length && bombeMap[x][y]) {
                    n++;
                }
            }
        }
        return n;
    }

    public int getVal(int i, int j) {
        int res = 0;
        try {
            if(bombeMap[i][j]) {
                return -1;
            }
        }
        catch(Exception e) {
            System.out.println("Coordonnées hors du tableau");
        }
        if(i + 1 < bombeMap.length && bombeMap[i+1][j]) {
            res++;
        }
        if(i - 1 >= 0 && bombeMap[i-1][j]) {
            res++;
        }
        if(j + 1 < bombeMap[0].length && bombeMap[i][j+1]) {
            res++;
        }
        if(j - 1 >= 0 && bombeMap[i][j-1]) {
            res++;
        }
        if(j + 1 < bombeMap[0].length && i + 1 < bombeMap.length && bombeMap[i+1][j+1]) {
            res++;
        }
        if(j + 1 < bombeMap[0].length && i - 1 >= 0 && bombeMap[i-1][j+1]) {
            res++;
        }
        if(j - 1 >= 0 && i + 1 < bombeMap.length && bombeMap[i+1][j-1]) {
            res++;
        }
        if(j - 1 >= 0 && i - 1 >= 0 && bombeMap[i-1][j-1]) {
            res++;
        }
        return res;
    }
}
