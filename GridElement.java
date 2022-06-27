import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * clasa de baza a elementelor din joc
 * este mostenita de de clasa {@link GridElement}
 */
public class GridElement extends Actor {
    // coordonatele elementului din careu
    protected coordonates myCoordonates;
    // lista cu linile din care face parte obiectul
    protected List<List<GridElement>> Lines;
    // valoarea elementului
    protected Type Val;
    // offestul vecinilor, in coordonate relative
    private final int[][] offsetNeigh = { { -1, -1, -1, 0, 0, 1, 1, 1 }, { -1, 0, 1, -1, 1, -1, 0, 1 } };
    // private int hasLineOver = -1;

    public GridElement() {
        myCoordonates = new coordonates(0, 0);
        Val = Val.notOpened;
        Lines = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            Lines.add(new ArrayList<GridElement>());
    }

    public GridElement(int CoordX, int CoordY) {
        this();
        myCoordonates.x = CoordX;
        myCoordonates.y = CoordY;
    }

    /*
     * public void addLineOver(int orientare)
     * {
     * hasLineOver = orientare;
     * }
     * public int getLineOver()
     * {
     * return hasLineOver;
     * }
     */

    /**
     * functie ce verifica daca vecinul cu indexul i, are aceasi valoare cu noi
     * 
     * @param i indexul vecinului testat
     * @return boolean ce transmite daca cele doua elemente tesate au aceasi valoare
     */
    public boolean addNeigh(int i) {
        boolean added = false;
        MyList<? extends GridElement> desiredLine;
        GridElement desiredNeigh = null;
        desiredLine = Brain.Elements.get(myCoordonates.x + offsetNeigh[0][i]);
        if (desiredLine != null) {
            desiredNeigh = (GridElement) desiredLine.get(myCoordonates.y + offsetNeigh[1][i]);
        } else {
            return added;
        }
        if (desiredNeigh != null) // avem vecin in careu
        {
            if (desiredNeigh.getStatus() == Val) {
                // trebuie sa stim in ce lista de linii il adaugam
                addLineOnLine(Brain.mapNeigh[i], desiredNeigh.getLine(Brain.mapNeigh[i]));
                added = true;
            }
        }
        return added;
    }

    /**
     * sincronizeaza liniile tuturor elementelor vecine elementului curent
     */
    public void sincLines() {
        for (int line = 0; line <= 3; line++) {
            int sizeOfLine = Lines.get(line).size();
            // luam fiecare vecin
            for (int i = 1; i < sizeOfLine; i++) {
                // adaugam in lista vecinului, toate elemente ce lipsesc
                for (int j = 0; j < sizeOfLine; j++) {
                    if (Lines.get(line).get(i).containsOnLine(line, Lines.get(line).get(j)) == false) {
                        Lines.get(line).get(i).addOnLine(line, Lines.get(line).get(j));
                    }
                }
            }
        }
    }

    /**
     * adauga un element pe o linie
     * 
     * @param index linia pe care adaugam
     * @param vecin elementul adaugat
     */
    public void addOnLine(int index, GridElement vecin) {
        Lines.get(index).add(vecin);
    }

    /**
     * adaugam o lista de elemente pe o linie
     * 
     * @param index   linia pe care adaugam
     * @param newLine lista ce va fi adaugata
     */
    public void addLineOnLine(int index, List<GridElement> newLine) {
        Lines.get(index).addAll(newLine);
    }

    /**
     * adauga elementul curent pe toate liniile prorpii
     */
    public void selfAdd() {
        for (int i = 0; i <= 3; i++)
            addOnLine(i, this);
    }

    /**
     * returneaza lungimea unei linii
     * 
     * @param index linia a carei lungimi o dorim
     * @return lungimea acesteia
     */
    public int getLineLenght(int index) {
        return Lines.get(index).size();
    }

    /**
     * testeaza daca o anumita linie este castigatoare
     * 
     * @param index linia testata
     * @return true - castigatoare / false - necastigatoare
     */
    public boolean won(int index) {
        return Lines.get(index).size() >= Brain.winReq;
    }

    /**
     * returneaza o anumita linie
     * 
     * @param index indexul liniei dorite
     * @return linia de la indexul trimis ca parametru
     */
    public List<GridElement> getLine(int index) {
        return Lines.get(index);
    }

    /**
     * testeaza daca o anumita linie contine un anumit element
     * 
     * @param line   linia verificata
     * @param second elementul testat
     * @return true daca il contine, falsa in caz contrar
     */
    public boolean containsOnLine(int line, GridElement second) {
        return Lines.get(line).contains(second);
    }

    public int getCoordX() {
        return myCoordonates.x;
    }

    public int getCoordY() {
        return myCoordonates.y;
    }

    public void setStatus(Type newStatus) {
        Val = newStatus;
    }

    public Type getStatus() {
        return Val;
    }

    public coordonates getCoordonates() {
        return myCoordonates;
    }
}
