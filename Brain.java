import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * enum pentru cele trei stari in care se poate afla un obiect al clasei
 * {@link GridElement}
 * notOpened - nu a fost deschisa
 * X - este a jucatorului cu x
 * Y - este a jucatorului cu y
 */
enum Type {
    notOpened, X, Y;
};

/**
 * enum ce retine starile jocului
 */
enum State {
    waitForMove, WaitingForBullet, animationOn;
}

/**
 * clasa folosita pentru override la metoda get()
 */
class MyList<T> extends ArrayList<T> {
    // ne asiguram ca indexul se afla pe plansa
    @Override
    public T get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        }
        return super.get(index);
    }
}

/**
 * clasa folosita la retinerea unui vector2
 * este utilizata in retinerea de coordonate
 */
class coordonates {
    public int x;
    public int y;

    public coordonates(int _x, int _y) {
        x = _x;
        y = _y;
    }

    // in functie de dimensiunea tablei de joc, aceasta functie returneaza al
    // catelea element este cel retinut in coordonate
    public int convertToIndex(int size) {
        int rezultat;
        rezultat = x * size + y;
        return rezultat;
    }
}

/**
 * Clasa Principala a jocului
 * Aceasta creaza plansa jocului, retine miscarile, decide cand unul dintre
 * jucatori a castigat, controleaza miscarile calculatorului
 */
public class Brain extends Actor {
    // Lista ce retine obiectele de pe plansa
    public static MyList<MyList<? extends GridElement>> Elements = new MyList<>();
    // 0 -> -
    // 1 -> |
    // 2 -> \
    // 3 -> /
    // pt a putea indentifica egalitatea
    // daca mutari == 100 => egalitate
    public static int mutari;
    // numarul de elemente consecutive necesare castigarii
    public static int winReq;
    // mapeaza vecinii pe linii, in functie de oridinea lor
    public static int[] mapNeigh;
    // tine minte care jucator urmeaza
    protected static Type currentPlayer;
    // retine starea curenta a jocului
    protected static State gameState;
    // dimensiunea careului
    private static int size;

    // divelul de adancime a inteligentei artificiale
    private int AiLevel;
    // variabila folosita pentru scalarea imaginilor
    // este folosita in situatia unui careu cu mai mult de 10 elemente
    private float raport;
    // semafor folosit pentru crearea unui eveniment begin play
    private boolean once;
    // referinta la tun
    private Gun refToGun;

    // in cazul in care se joaca cu mai mult de 20 de elemente pe o linie,
    // dimensiuniile acestora devin foarte mici, si astfel va fi necesar sa cream o
    // lupa
    // aceasta clasa retine un set de 25 de puncte, care citesc cele 25 de obiecte
    // ale clasei Element, si le maresc
    private List<pointer> crossair = new ArrayList<>();
    // offsetul la care se adauga pointerii
    private final int[][] offsetPointer = {
            { -2, -2, -2, -2, -2, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2 },
            { -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2 } };
    // variabila folosita de inteligenta artificiala pentru a citi plansa de joc
    private Type[][] AiMatrix;
    // ultimul element adaugat pe plansa
    private coordonates LastAddedElement;
    // pentru a optimiza Algoritmul de alegere a urmatoarei miscari a
    // calculatorului, am optat pentru o parcurgere in spirala a careului
    // aceasta superlista retine cate o lista cu coordonatele parcurgerii in
    // spirala, incepand de la fiecare element
    List<List<coordonates>> a = new ArrayList<>();
    // elementul ales de Algoritmul calculatorului
    private Element aiElement;
    // retine daca jucam impotriva altui jucator sau a calculatorului
    private boolean AI;
    // imaginea de fundal
    // se va modifica in functie de dimensiunea placii de joc
    private GreenfootImage backBoard = new GreenfootImage("images\\back_board.png");

    public Brain() {
    }

    public Brain(int _size, int _winReq, Gun _refToGun, boolean AiOn) {
        this();
        init(_size, _winReq, _refToGun, AiOn);
        createMap();
        // setImage(new GreenfootImage("placa mov.png"));
    }

    private void init(int _size, int _winReq, Gun _refToGun, boolean AiOn) {
        // initializeaza variabile
        AI = AiOn;
        refToGun = _refToGun;
        gameState = State.waitForMove;
        size = _size;
        winReq = _winReq;
        once = true;
        mutari = 0;
        currentPlayer = Type.X;
        if (size > 10) {
            raport = Element.resizeImgs(size);
            Line.resizeImgs(raport);
            Bullet.raport = raport;
        } else
            raport = -1;

        if (AI) {
            // se executa doar daca se joaca contra calculatorului
            AiLevel = 6;
            LastAddedElement = new coordonates(Greenfoot.getRandomNumber(size), Greenfoot.getRandomNumber(size));
            AiMatrix = new Type[_size][_size];
        }

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
        return (int) ((x + offset) * (50 / raport) + 305 + 50 / 2);
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
        return (int) ((y + offset) * (50 / raport) + 15 + 50 / 2);
    }

    public void createGrid(int n) {
        // creaza gridul
        Elements.clear();
        MyList<Element> TempList;
        for (int i = 0; i < n; i++) {
            TempList = new MyList<>();
            for (int j = 0; j < n; j++) {
                Element aiElement = new Element(i, j, this, refToGun);
                TempList.add(aiElement);
                getWorld().addObject(aiElement, turnXinCoord(i, n), turnYinCoord(j, n));

            }
            Elements.add(TempList);
        }

    }

    private void setBoard(int n) {
        if (n < 10) {
            backBoard.scale(backBoard.getWidth() - 50 * (10 - n), backBoard.getHeight() - 50 * (10 - n));
        }

        if (n % 2 == 0) {
            setLocation((int) (turnXinCoord(n / 2, n) - 25 / Math.abs(raport)),
                    (int) (turnYinCoord(n / 2, n) - 25 / Math.abs(raport)));

        } else {
            setLocation(turnXinCoord(n / 2, n), turnYinCoord(n / 2, n));
        }
        setImage(backBoard);

    }

    private void createPointers() {
        pointer aiElement;
        for (int i = 0; i < 25; i++) {
            aiElement = new pointer(raport, i);
            getWorld().addObject(aiElement, 100, 100);
            crossair.add(aiElement);
        }
    }

    // ai function
    private void transcriptGrid() {
        Type aiElement;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                aiElement = Elements.get(j).get(i).getStatus();
                AiMatrix[i][j] = aiElement;
            }
        }
    }

    // ai function
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

    // ai function
    private void createAllParcurgeri() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                a.add(getParcurgere(new coordonates(i, j)));

    }

    // ai function
    protected GridElement AiMove(coordonates lastAdded) {
        int bestMove = Integer.MIN_VALUE;
        int valoare;
        Element nextMove = null;
        // aiElement
        transcriptGrid();
        int localAiLevel = 0;
        int worstMove;
        // preia primul vecini
        List<coordonates> MyParcurgere = a.get(lastAdded.convertToIndex(size));
        boolean staticEval = false;
        do {
            if (bestMove <= 15 && localAiLevel == AiLevel) {
                staticEval = true;
                localAiLevel = 0;
            }
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
                    /*
                     * System.out.println(
                     * nextNeigh.x + " " + nextNeigh.y + " " + valoare + " " + bestMove + " " +
                     * localAiLevel);
                     */
                    if (valoare == bestMove) {

                        if (Greenfoot.getRandomNumber(300) == 100) {
                            nextMove = (Element) Elements.get(nextNeigh.y).get(nextNeigh.x);
                            // System.err.println("sda");
                            if (nextMove == null) {
                                System.err.println("null pointer class brain cast failed");
                                break;
                            }
                        }
                    }

                    if (valoare > bestMove) {
                        bestMove = valoare;
                        nextMove = (Element) Elements.get(nextNeigh.y).get(nextNeigh.x);
                        if (nextMove == null) {
                            System.err.println("null pointer class brain cast failed");
                            break;
                        }
                    }
                    worstMove = Integer.min(worstMove, valoare);
                    AiMatrix[nextNeigh.x][nextNeigh.y] = Type.notOpened;
                }
            }
            localAiLevel++;

        } while (bestMove <= 15 && worstMove > -15 && staticEval == false);

        return nextMove;
    }

    // ai function
    private Type inversType(Type oldOne) {
        if (oldOne == Type.X)
            return Type.Y;
        else
            return Type.X;
    }

    // ai function
    private boolean checkWon(int[] lenghts) {
        for (int i : lenghts) {
            if (i >= winReq)
                return true;
        }
        return false;
    }

    // ai function
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

    // ai function
    private int staticEvaluation(int[] lenghts) {
        int max = 0;
        int sum = 0;
        for (int i : lenghts) {
            // repair this
            if (i > max) {
                max = i;
                sum = max;
            }
        }
        return sum;
    }

    // ai function
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

                if (curentPlayer == Type.X)
                    return -staticEvaluation(lenghts); // bad I
                else
                    return staticEvaluation(lenghts); // good II
            } else
                return 0;
        } else {
            int bestMove;
            int valoare;
            // adaugam in mod normal**
            // terminal condition if won

            List<coordonates> MyParcurgere = a.get(lastAdded.convertToIndex(size));
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
        Clicked.setStatus(currentPlayer);
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
                        if (raport == -1) {
                            getWorld().addObject(new Line(mapNeigh[i]), (int) (iter.getX()),
                                    (int) (iter.getY()));
                        } else {
                            getWorld().addObject(new Line(mapNeigh[i]), (int) (iter.getX() - 6 / raport),
                                    (int) (iter.getY() - 6 / raport));
                        } // iter.addLineOver(mapNeigh[i]);
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
        if (once) {
            // begin play
            if (AI) {
                createAllParcurgeri();
            }
            createGrid(size);
            createPointers();
            setBoard(size);
            ZoomElement.scaleImgs();
            once = false;
        }
        if (size > 20) {
            movePointer();
        }
        if (AI) {
            // se executa doar daca jucam contra Ai-ului
            if (currentPlayer == Type.X) {
                if (Brain.gameState == State.waitForMove) {
                    aiElement = (Element) AiMove(LastAddedElement);
                    if (aiElement != null)
                        aiElement.openIt();
                    else
                        System.err.println("null pointer cast failed");
                }
            }
        }
    }

    private void movePointer() {
        if (Greenfoot.mouseMoved(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            int index = 0;
            int cordx, cordy;
            for (pointer iter : crossair) {
                if (iter == null) {
                    System.err.println("null pointer in class brain");
                    break;
                }
                cordx = mouse.getX() + (int) (50 / raport * offsetPointer[0][index]);
                cordy = mouse.getY() + (int) (50 / raport * offsetPointer[1][index]);
                iter.setLocation(cordx, cordy);
                index++;
            }
        }
    }
}
