import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

public class GridElement extends Actor {
    protected coordonates myCoordonates;
    protected List<List<GridElement>> Lines; // lista cu linile din care face parte obiectul
    protected Type Val;
    private final int[][] offsetNeigh = { { -1, -1, -1, 0, 0, 1, 1, 1 }, { -1, 0, 1, -1, 1, -1, 0, 1 } }; // offsetul la
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
    public boolean addNeigh(int i) {
        boolean added;
        MyList<? extends GridElement> desiredLine;
        GridElement desiredNeigh = null;
        added = false;
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

    public void addOnLine(int index, GridElement vecin) {
        Lines.get(index).add(vecin);
    }

    public void addLineOnLine(int index, List<GridElement> newLine) {
        Lines.get(index).addAll(newLine);
    }

    public void selfAdd() {
        for (int i = 0; i <= 3; i++)
            addOnLine(i, this);
    }

    public int getLineLenght(int index) {
        return Lines.get(index).size();
    }

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

    public boolean won(int index) {
        return Lines.get(index).size() >= Brain.winReq;
    }

    public List<GridElement> getLine(int index) {
        return Lines.get(index);
    }

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
