import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

enum Type {
    notOpened, X, Y;
};

enum State {
    waitForMove, WaitingForBullet, animationOn;
}

// clasa folosita pentru override la metoda get()
class MyList<T> extends ArrayList<T> {
    @Override
    public T get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        }
        return super.get(index);
    }
}

class coordonates {
    public int x;
    public int y;

    public coordonates(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public int convertToIndex(int size) {
        int rezultat;
        rezultat = x * size + y;
        return rezultat;
    }
}

public class Brain extends Actor {
    public static MyList<MyList<? extends GridElement>> Elements = new MyList<>();
    // 0 -> -
    // 1 -> |
    // 2 -> \
    // 3 -> /

    protected static Type CurrentPlayer; // tine minte care jucator urmeaza
    public static int winReq; // numarul de elemente consecutive necesare castigarii
    private static int size; // dimensiunea careului
    public static int mutari; // pt a putea indentifica egalitatea
    // daca mutari == 100 => egalitate
    protected static State gameState;
    private int AiLevel;
    private float raport;
    private boolean ok;// semafor folosit pentru crearea unui eveniment begin play
    private Gun refToGun;
    public static int[] mapNeigh;// mapeaza vecinii pe linii, in functie de oridinea lor

    private final int[][] offsetPointer = {
            { -2, -2, -2, -2, -2, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2 },
            { -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2 } };

    private List<pointer> crossair = new ArrayList<>();
    Type[][] AiMatrix;
    private coordonates LastAddedElement;
    // change name here
    List<List<coordonates>> allWays = new ArrayList<>();
    private Element temp;

    public Brain() {
    }

    public Brain(int _size, int _winReq, Gun _refToGun) {
        this();
        init(_size, _winReq, _refToGun);
        createMap();
        // setImage(new GreenfootImage("placa mov.png"));
    }

    private void init(int _size, int _winReq, Gun _refToGun) {
        // initializeaza variabile
        refToGun = _refToGun;
        gameState = State.waitForMove;
        size = _size;
        winReq = _winReq;
        ok = true;
        mutari = 0;
        CurrentPlayer = Type.X;
        if (size > 10) {
            raport = Element.resizeImgs(size);
            Line.resizeImgs(raport);
            Bullet.raport = raport;
        } else
            raport = -1;
        GreenfootImage img = new GreenfootImage(500, 500);
        setImage(img);
        AiLevel = 0;
        LastAddedElement = new coordonates(Greenfoot.getRandomNumber(size), Greenfoot.getRandomNumber(size));
        AiMatrix = new Type[_size][_size];
    }

    private void createMap() {
        // creaza mapa vecinilor
        mapNeigh = new int[8];
        mapNeigh[0] = 2;
        mapNeigh[1] = 1;
        mapNeigh[2] = 3;
        mapNeigh[3] = 0;
        mapNeigh[4] = 0;
        mapNeigh[5] = 3;
        mapNeigh[6] = 1;
        mapNeigh[7] = 2;
    }

    private int turnXinCoord(int x, int dim) {
        // turning index into coordonates
        int offset;
        switch (dim) {
            case 10:
                offset = 1;
                break;
            case 9:
                offset = 1;
                break;
            case 8:
                offset = 2;
                break;
            case 7:
                offset = 2;
                break;
            case 6:
                offset = 3;
                break;
            case 5:
                offset = 3;
                break;
            case 4:
                offset = 4;
                break;
            case 3:
                offset = 5;
                break;
            default:
                offset = 1;
                break;
        }
        if (raport == -1)
            return (x + offset) * 50 + 305;
        return (int) ((x + offset) * (50 / raport) + 305 + 50 / raport / 2);
    }

    private int turnYinCoord(int y, int dim) {
        // turning index into coordonates
        int offset;
        switch (dim) {
            case 10:
                offset = 1;
                break;
            case 9:
                offset = 1;
                break;
            case 8:
                offset = 2;
                break;
            case 7:
                offset = 2;
                break;
            case 6:
                offset = 3;
                break;
            case 5:
                offset = 3;
                break;
            case 4:
                offset = 4;
                break;
            case 3:
                offset = 4;
                break;
            default:
                offset = 1;
                break;
        }
        if (raport == -1)
            return (y + offset) * 50 + 15;
        return (int) ((y + offset) * (50 / raport) + 15 + 50 / raport / 2);
    }

    public void createGrid(int n) {
        // creaza gridul
        Elements.clear();
        MyList<Element> TempList;
        for (int i = 0; i < n; i++) {
            TempList = new MyList<>();
            for (int j = 0; j < n; j++) {
                Element temp = new Element(i, j, this, refToGun);
                TempList.add(temp);
                getWorld().addObject(temp, turnXinCoord(i, n), turnYinCoord(j, n));
            }
            Elements.add(TempList);
        }
        setLocation(turnXinCoord(n / 2, n), turnYinCoord(n / 2, n));

        pointer temp;
        for (int i = 0; i < 25; i++) {
            temp = new pointer(raport, i);
            getWorld().addObject(temp, 100, 100);
            crossair.add(temp);
        }
        ZoomElement.scaleImgs();
    }

    private void transcriptGrid() {
        Type temp;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp = Elements.get(j).get(i).getStatus();
                AiMatrix[i][j] = temp;
            }
        }
    }

    private List<coordonates> getParcurgere(coordonates startPosition) {
        List<coordonates> parcurgeri = new ArrayList<>();
        // creaza pentru elementul de la pozita startPosition, parcurferea in spirala a
        // unei matrici de dim size x size
        int rowIndex = startPosition.x;
        int colIndex = startPosition.y;
        final int UP = 0;
        final int LEFT = 1;
        final int RIGHT = 2;
        final int DOWN = 3;

        parcurgeri.add(startPosition);

        // incepem spre stanga
        int nextDirection = LEFT;
        int i = 0;
        // nevoie de o variabila care se incrementeaza tot la 2 iteratii
        int movesIter = 1, movesGlobal = 1;
        boolean change = false;

        while (i < size * size - 1) {
            // check which direction to go (left, down, right or up)
            switch (nextDirection) {
                case LEFT:
                    colIndex -= 1;
                    if (colIndex >= 0 && rowIndex >= 0 && colIndex < size) {
                        i++;
                        parcurgeri.add(new coordonates(rowIndex, colIndex));
                    }
                    movesIter--;
                    if (movesIter == 0) {
                        if (change) {
                            movesGlobal++;
                            change = false;
                        } else {
                            change = true;
                        }
                        movesIter = movesGlobal;
                        nextDirection = DOWN;
                    }
                    break;

                case DOWN:
                    rowIndex += 1;
                    if (rowIndex < size && colIndex >= 0 && rowIndex >= 0) {
                        i++;
                        parcurgeri.add(new coordonates(rowIndex, colIndex));
                    }
                    movesIter--;
                    if (movesIter == 0) {
                        if (change) {
                            movesGlobal++;
                            change = false;
                        } else {
                            change = true;
                        }
                        movesIter = movesGlobal;
                        nextDirection = RIGHT;
                    }
                    break;

                case RIGHT:
                    colIndex += 1;
                    if (colIndex < size && rowIndex < size && colIndex >= 0) {
                        i++;
                        parcurgeri.add(new coordonates(rowIndex, colIndex));
                    }
                    movesIter--;
                    if (movesIter == 0) {
                        if (change) {
                            movesGlobal++;
                            change = false;
                        } else {
                            change = true;
                        }
                        movesIter = movesGlobal;
                        nextDirection = UP;
                    }
                    break;

                case UP:
                    rowIndex -= 1;
                    if (rowIndex >= 0 && colIndex < size && rowIndex < size) {
                        i++;
                        parcurgeri.add(new coordonates(rowIndex, colIndex));
                    }
                    movesIter--;
                    if (movesIter == 0) {
                        if (change) {
                            movesGlobal++;
                            change = false;
                        } else {
                            change = true;
                        }
                        movesIter = movesGlobal;
                        nextDirection = LEFT;
                    }
                    break;
            }
        }
        switch (nextDirection) {
            case LEFT:
                rowIndex = 0;
                colIndex = size - 1;
                parcurgeri.add(new coordonates(rowIndex, colIndex));
                break;

            case DOWN:
                colIndex = 0;
                rowIndex = 0;
                parcurgeri.add(new coordonates(rowIndex, colIndex));
                break;

            case RIGHT:
                rowIndex = size - 1;
                colIndex = 0;
                parcurgeri.add(new coordonates(rowIndex, colIndex));
                break;

            case UP:
                colIndex = size - 1;
                rowIndex = size - 1;
                parcurgeri.add(new coordonates(rowIndex, colIndex));
                break;
        }
        return parcurgeri;
    }

    private void createAllParcurgeri() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                allWays.add(getParcurgere(new coordonates(i, j)));

    }

    protected GridElement AiMove(coordonates lastAdded) {
        int bestMove = Integer.MIN_VALUE;
        int valoare;
        Element nextMove = null;
        // temp
        transcriptGrid();
        int timesChanged = 0;
        int localAiLevel = 0;
        int worstMove;
        // preia primul vecini
        List<coordonates> MyParcurgere = allWays.get(lastAdded.convertToIndex(size));
        boolean staticEval = false;
        do {
            if (bestMove <= 15 && localAiLevel == 6) {
                staticEval = true;
                localAiLevel=0;
            }
            timesChanged = 0;
            bestMove = Integer.MIN_VALUE;
            worstMove = Integer.MAX_VALUE;
            for (coordonates nextNeigh : MyParcurgere) {
                if (AiMatrix[nextNeigh.x][nextNeigh.y] == Type.notOpened) {
                    AiMatrix[nextNeigh.x][nextNeigh.y] = Type.X;
                    if (localAiLevel == 0)
                        valoare = minimax(AiMatrix, localAiLevel, Type.Y, nextNeigh, Integer.MIN_VALUE,
                                Integer.MAX_VALUE, staticEval);
                    else
                        valoare = minimax(AiMatrix, localAiLevel, Type.Y, nextNeigh, Integer.MIN_VALUE,
                                Integer.MAX_VALUE, staticEval);
                    System.out.println(nextNeigh.x + " " + nextNeigh.y + " " + valoare + " " + bestMove + " " + localAiLevel);
                    if (valoare > bestMove) {
                        bestMove = valoare;
                        nextMove = (Element) Elements.get(nextNeigh.y).get(nextNeigh.x);
                        if (nextMove == null) {
                            System.err.println("null pointer class brain cast failed");
                            break;
                        }
                        timesChanged++;
                    }
                    worstMove = Integer.min(worstMove, valoare);
                    AiMatrix[nextNeigh.x][nextNeigh.y] = Type.notOpened;
                }
            }
            localAiLevel++;

        } while (bestMove <= 15 && worstMove>-15 && staticEval == false);

        return nextMove;
    }

    private Type inversType(Type oldOne) {
        if (oldOne == Type.X)
            return Type.Y;
        else
            return Type.X;
    }

    private boolean checkWon(int[] lenghts) {
        for (int i : lenghts) {
            if (i >= winReq)
                return true;
        }
        return false;
    }

    private int[] checkLenghts(Type[][] grid, Type curentPlayer, coordonates lastAdded) {
        int[] lenghts = { 0, 0, 0, 0 };

        int auxi, auxj;
        // check up + down
        auxi = lastAdded.x - 1;
        auxj = lastAdded.y;
        while (auxi >= 0 && grid[auxi][auxj] == curentPlayer) {
            lenghts[0]++;
            auxi--;
        }
        auxi = lastAdded.x;
        while (auxi < size && grid[auxi][auxj] == curentPlayer) {
            lenghts[0]++;
            auxi++;
        }
        if (lenghts[0] >= winReq) {
            return lenghts;
        }
        // check left + right
        auxi = lastAdded.x;
        auxj = lastAdded.y - 1;
        while (auxj >= 0 && grid[auxi][auxj] == curentPlayer) {
            lenghts[1]++;
            auxj--;
        }
        auxj = lastAdded.y;
        while (auxj < size && grid[auxi][auxj] == curentPlayer) {
            lenghts[1]++;
            auxj++;
        }
        if (lenghts[1] >= winReq) {
            return lenghts;
        }
        // check first diagonal
        auxi = lastAdded.x - 1;
        auxj = lastAdded.y - 1;

        while (auxi >= 0 && auxj >= 0 && grid[auxi][auxj] == curentPlayer) {
            lenghts[2]++;
            auxi--;
            auxj--;
        }
        auxi = lastAdded.x;
        auxj = lastAdded.y;
        while (auxi < size && auxj < size && grid[auxi][auxj] == curentPlayer) {
            lenghts[2]++;
            auxi++;
            auxj++;
        }
        if (lenghts[2] >= winReq) {
            return lenghts;
        }
        // check second diagonal
        auxi = lastAdded.x - 1;
        auxj = lastAdded.y + 1;

        while (auxi >= 0 && auxj < size && grid[auxi][auxj] == curentPlayer) {
            lenghts[3]++;
            auxi--;
            auxj++;
        }
        auxi = lastAdded.x;
        auxj = lastAdded.y;
        while (auxi < size && auxj >= 0 && grid[auxi][auxj] == curentPlayer) {
            lenghts[3]++;
            auxi++;
            auxj--;
        }
        if (lenghts[3] >= winReq) {
            return lenghts;
        }
        return lenghts;
    }

    private int minimax(Type[][] grid, int depth, Type curentPlayer, coordonates lastAdded, int alpha, int beta,
            boolean staticEval) {
        int[] lenghts = checkLenghts(grid, inversType(curentPlayer), lastAdded);
        if (checkWon(lenghts)) {
            if (curentPlayer == Type.X)
                return -15 * (depth + 1); // bad I
            else
                return 15 * (depth + 1); // good II
        } else if (depth == 0) {
            if (staticEval) {
                int sum = 0;
                for (int i : lenghts) {
                    sum += i;
                }
                if (curentPlayer == Type.X)
                    return -sum; // bad I
                else
                    return sum; // good II
            } else
                return 0;
        } else {
            int bestMove;
            int valoare;
            // adaugam in mod normal**
            // terminal condition if won

            List<coordonates> MyParcurgere = allWays.get(lastAdded.convertToIndex(size));
            if (curentPlayer == Type.X) {
                bestMove = Integer.MIN_VALUE;
                for (coordonates nextNeigh : MyParcurgere) {
                    if (grid[nextNeigh.x][nextNeigh.y] == Type.notOpened) {

                        grid[nextNeigh.x][nextNeigh.y] = curentPlayer;
                        valoare = minimax(grid, depth - 1, Type.Y, nextNeigh, alpha, beta, false);
                        bestMove = Integer.max(bestMove, valoare);
                        alpha = Integer.max(alpha, valoare);
                        grid[nextNeigh.x][nextNeigh.y] = Type.notOpened;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
                return bestMove;
            } else {
                bestMove = Integer.MAX_VALUE;
                for (coordonates nextNeigh : MyParcurgere) {
                    if (grid[nextNeigh.x][nextNeigh.y] == Type.notOpened) {
                        grid[nextNeigh.x][nextNeigh.y] = curentPlayer;
                        valoare = minimax(grid, depth - 1, Type.X, nextNeigh, alpha, beta, false);
                        bestMove = Integer.min(bestMove, valoare);
                        beta = Integer.min(beta, valoare);
                        grid[nextNeigh.x][nextNeigh.y] = Type.notOpened;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }

                return bestMove;
            }
        }

    }

    protected void clicked(Element Clicked) {
        boolean added;

        Clicked.setStatus(CurrentPlayer);
        LastAddedElement = Clicked.getCoordonates();

        // ne adaugam pe noi pe liniile componente
        Clicked.selfAdd();

        // verificam vecinii si adaugam pe linie daca sunt deschisi
        // avem 8 vecini
        for (int i = 0; i < 8; i++) {
            added = Clicked.addNeigh(i);
            //
            if (added) {
                if (Clicked.won(mapNeigh[i])) {
                    // adding the Lines
                    for (GridElement iter : Clicked.getLine(mapNeigh[i])) {
                        getWorld().addObject(new Line(mapNeigh[i]), turnXinCoord(iter.getCoordX(), size),
                                turnYinCoord(iter.getCoordY(), size));
                    }
                    break;
                }
            }
        }

        // dupa ce am gasit toti vecinii, sincronizam listele lor, cu lista din
        // elementul curent
        Clicked.sincLines();
    }

    public void act() {
        if (ok) {
            // begin play
            ok = false;
            createAllParcurgeri();
            createGrid(size);
        }
        if (size > 20) {
            movePointer();
        }
        if (CurrentPlayer == Type.X) {
            if (Brain.gameState == State.waitForMove) {
                temp = (Element) AiMove(LastAddedElement);
                if (temp != null)
                    temp.openIt();
                else
                    System.err.println("null pointer cast failed");
            }
        }
    }

    private void movePointer() {
        if (Greenfoot.mouseMoved(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            int index = 0;
            int cordx, cordy;
            for (pointer iter : crossair) {
                cordx = mouse.getX() + (int) (50 / raport * offsetPointer[0][index]);
                cordy = mouse.getY() + (int) (50 / raport * offsetPointer[1][index]);
                iter.setLocation(cordx, cordy);
                index++;
            }
        }
    }
}
/*
 * "java.project.referencedLibraries": [
 * "lib/doua stelute sus/*.jar",
 * "c:\\path\\to\\jarfile\\commons-logging-1.1.1.jar"
 * ]
 */