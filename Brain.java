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
}

public class Brain extends Actor {
    private static MyList<MyList<Element>> Elements = new MyList<>();
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
    private int[] mapNeigh;// mapeaza vecinii pe linii, in functie de oridinea lor
    private final int[][] offsetNeigh = { { -1, -1, -1, 0, 0, 1, 1, 1 }, { -1, 0, 1, -1, 1, -1, 0, 1 } }; // offsetul la
                                                                                                          // care se
    private final int[][] offsetPointer = {
            { -2, -2, -2, -2, -2, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2 },
            { -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2 } };

    private List<pointer> crossair = new ArrayList<>();
    Type[][] AiMatrix;

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
        AiLevel = 3;

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

    private coordonates getFirstValidNeighInSpiral(Type[][] matrix, coordonates startPosition, int noNeighb) {
        //returneaza primul vecin valid pornind intro spirala de la un set de coordonate
        int rowIndex = startPosition.x;
        int colIndex = startPosition.y;
        if (matrix[rowIndex][colIndex] == Type.notOpened) {
            return startPosition;
        }
        final int UP = 0;
        final int LEFT = 1;
        final int RIGHT = 2;
        final int DOWN = 3;

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
                        if (AiMatrix[rowIndex][colIndex] == Type.notOpened) {
                            // avem un copil valid
                            noNeighb--;
                            if (noNeighb == 0)
                                return new coordonates(rowIndex, colIndex);
                        }
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
                        if (AiMatrix[rowIndex][colIndex] == Type.notOpened) {
                            noNeighb--;
                            if (noNeighb == 0)
                                return new coordonates(rowIndex, colIndex);
                        }
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
                        if (AiMatrix[rowIndex][colIndex] == Type.notOpened) {
                            noNeighb--;
                            if (noNeighb == 0)
                                return new coordonates(rowIndex, colIndex);
                        }
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
                        if (AiMatrix[rowIndex][colIndex] == Type.notOpened) {
                            noNeighb--;
                            if (noNeighb == 0)
                                return new coordonates(rowIndex, colIndex);
                        }
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
                if (AiMatrix[rowIndex][colIndex] == Type.notOpened) {
                    return new coordonates(rowIndex, colIndex);
                }
                break;

            case DOWN:
                colIndex = 0;
                rowIndex = 0;

                if (AiMatrix[rowIndex][colIndex] == Type.notOpened) {
                    return new coordonates(rowIndex, colIndex);
                }
                break;

            case RIGHT:
                rowIndex = size - 1;
                colIndex = 0;
                if (AiMatrix[rowIndex][colIndex] == Type.notOpened) {
                    return new coordonates(rowIndex, colIndex);
                }
                break;

            case UP:
                colIndex = size - 1;
                rowIndex = size - 1;
                if (AiMatrix[rowIndex][colIndex] == Type.notOpened) {
                    return new coordonates(rowIndex, colIndex);
                }
                break;
        }
        return new coordonates(0, 0);
    }

    protected Element AiMove() {
        int bestMove = Integer.MIN_VALUE;
        int valoare;
        Element nextMove = null;
        // temp
        transcriptGrid();

        int rowIndex = 1;
        int colIndex = 2;

        // preia primul vecini
        coordonates nextNeigh;

        for (int i = 1; i <= size * size; i++) {
            nextNeigh = getFirstValidNeighInSpiral(AiMatrix, new coordonates(rowIndex, colIndex), i);

            AiMatrix[nextNeigh.x][nextNeigh.y] = Type.X;
            valoare = minimax(AiMatrix, AiLevel, Type.Y, nextNeigh, Integer.MIN_VALUE,
                    Integer.MAX_VALUE);
            if (valoare > bestMove) {
                bestMove = valoare;
                nextMove = Elements.get(nextNeigh.x).get(nextNeigh.y);
            }
            AiMatrix[nextNeigh.x][nextNeigh.y] = Type.notOpened;
        }

        return nextMove;
    }

    private boolean checkWon(Type[][] grid, Type curentPlayer, coordonates lastAdded) {
        int lineLenght;
        int auxi, auxj;
        // check up + down
        auxi = lastAdded.x - 1;
        auxj = lastAdded.y;
        lineLenght = 0;
        while (auxi >= 0 && grid[auxi][auxj] == curentPlayer) {
            lineLenght++;
            auxi--;
        }
        auxi = lastAdded.x;
        while (auxi < size && grid[auxi][auxj] == curentPlayer) {
            lineLenght++;
            auxi++;
        }
        if (lineLenght >= winReq) {
            return true;
        }
        // check left + right
        auxi = lastAdded.x;
        auxj = lastAdded.y - 1;
        lineLenght = 0;
        while (auxj >= 0 && grid[auxi][auxj] == curentPlayer) {
            lineLenght++;
            auxj--;
        }
        auxj = lastAdded.y;
        while (auxj < size && grid[auxi][auxj] == curentPlayer) {
            lineLenght++;
            auxj++;
        }
        if (lineLenght >= winReq) {
            return true;
        }
        // check first diagonal
        auxi = lastAdded.x - 1;
        auxj = lastAdded.y - 1;
        lineLenght = 0;
        while (auxi >= 0 && auxj >= 0 && grid[auxi][auxj] == curentPlayer) {
            lineLenght++;
            auxi--;
            auxj--;
        }
        auxi = lastAdded.x;
        auxj = lastAdded.y;
        while (auxi < size && auxj < size && grid[auxi][auxj] == curentPlayer) {
            lineLenght++;
            auxi++;
            auxj++;
        }
        if (lineLenght >= winReq) {
            return true;
        }
        // check second diagonal
        auxi = lastAdded.x - 1;
        auxj = lastAdded.y + 1;
        lineLenght = 0;
        while (auxi >= 0 && auxj < size && grid[auxi][auxj] == curentPlayer) {
            lineLenght++;
            auxi--;
            auxj++;
        }
        auxi = lastAdded.x;
        auxj = lastAdded.y;
        while (auxi < size && auxj >= 0 && grid[auxi][auxj] == curentPlayer) {
            lineLenght++;
            auxi++;
            auxj--;
        }
        if (lineLenght >= winReq) {
            return true;
        }
        return false;
    }

    private Type inversType(Type oldOne) {
        if (oldOne == Type.X)
            return Type.Y;
        else
            return Type.X;
    }

    private int minimax(Type[][] grid, int depth, Type curentPlayer, coordonates lastAdded, int alpha, int beta) {

        if (checkWon(grid, inversType(curentPlayer), lastAdded)) {

            if (curentPlayer == Type.X)
                return -1; // bad I
            else
                return 1; // good II
        } else if (depth == 0) {
            // check static evaluation
            return 0; // III
        } else {
            int bestMove;
            int valoare;
            // adaugam in mod normal**
            // terminal condition if won
            coordonates nextNeigh;

            if (curentPlayer == Type.X) {
                bestMove = Integer.MIN_VALUE;
                for (int i = 1; i <= size * size; i++) {
                    nextNeigh = getFirstValidNeighInSpiral(grid, lastAdded, i);

                    grid[nextNeigh.x][nextNeigh.y] = curentPlayer;
                    valoare = minimax(grid, depth - 1, Type.Y, nextNeigh, alpha, beta);
                    bestMove = Integer.max(bestMove, valoare);
                    alpha = Integer.max(alpha, valoare);
                    grid[nextNeigh.x][nextNeigh.y] = Type.notOpened;
                    if (beta <= alpha) {
                        i = size;
                        break;
                    }
                }
                return bestMove;
            } else {
                bestMove = Integer.MAX_VALUE;
                for (int i = 1; i <= size * size; i++) {
                    nextNeigh = getFirstValidNeighInSpiral(grid, lastAdded, i);

                    grid[nextNeigh.x][nextNeigh.y] = curentPlayer;
                    valoare = minimax(grid, depth - 1, Type.X, nextNeigh, alpha, beta);
                    bestMove = Integer.min(bestMove, valoare);
                    beta = Integer.min(beta, valoare);
                    grid[nextNeigh.x][nextNeigh.y] = Type.notOpened;
                    if (beta <= alpha) {
                        i = size;
                        break;
                    }
                }
                return bestMove;
            }
        }
    }

    protected void clicked(Element Clicked) {
        boolean added;
        MyList<Element> desiredLine;
        Element desiredNeigh;

        Clicked.setStatus(CurrentPlayer);

        // ne adaugam pe noi pe liniile componente
        for (int i = 0; i <= 3; i++)
            Clicked.addOnLine(i, Clicked);

        // verificam vecinii si adaugam pe linie daca sunt deschisi
        // avem 8 vecini
        for (int i = 0; i < 8; i++) {
            added = false;
            desiredLine = Elements.get(Clicked.getCoordX() + offsetNeigh[0][i]);
            if (desiredLine != null) {
                desiredNeigh = desiredLine.get(Clicked.getCoordY() + offsetNeigh[1][i]);
            } else {
                continue;
            }
            if (desiredNeigh != null) // avem vecin in careu
            {
                if (desiredNeigh.getStatus() == Clicked.getStatus()) {
                    // trebuie sa stim in ce lista de linii il adaugam
                    Clicked.addLineOnLine(mapNeigh[i], desiredNeigh.getLine(mapNeigh[i]));
                    added = true;
                }
            }
            if (added) {
                if (Clicked.won(mapNeigh[i])) {
                    // adding the Lines
                    for (Element iter : Clicked.getLine(mapNeigh[i])) {
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

    private Element temp;

    public void act() {
        if (ok) {
            // begin play
            ok = false;
            createGrid(size);
        }
        if (size > 20) {
            movePointer();
        }
        if (CurrentPlayer == Type.X) {
            CurrentPlayer = Type.Y;
            temp = AiMove();
            System.out.println(temp.getCoordX() + " " + temp.getCoordY());
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