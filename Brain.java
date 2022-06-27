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
    waitForMove, WaitingForBullet, animationOn, ended;
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

    // referinta la cocos
    Cocos refToCocos;
    // dimensiunea default a unui element din careu
    private int elementDimension = 48;

    public Brain() {
    }

    /**
     * constructorul clasei
     * 
     * @param _size   dimensiunea careului de joc
     * @param _winReq dimensiunea liniei cu care se va castiga
     * @param AiOn    daca se joaca contra calculatorului sau nu
     * @param biro    referinta la {@link Cocos}
     */
    public Brain(int _size, int _winReq, boolean AiOn, Cocos biro) {
        this();
        init(_size, _winReq, AiOn, biro);
        createMap();
    }

    /**
     * functie folosita la initializarea mai multor variabile, si realizarea de
     * setari
     * initiale
     * 
     * @param _size   dimensiunea careului de joc
     * @param _winReq dimensiunea liniei cu care se va castiga
     * @param AiOn    daca se joaca contra calculatorului sau nu
     * @param biro    referinta la {@link Cocos}
     */
    private void init(int _size, int _winReq, boolean AiOn, Cocos biro) {
        // initializeaza variabile
        AI = AiOn;
        gameState = State.waitForMove;
        size = _size;
        winReq = _winReq;
        once = true;
        mutari = 0;
        currentPlayer = Type.X;
        refToCocos = biro;
        // resetam imaginile statica
        Element.initImgs();
        if (size > 10) {
            // scalam elementele ca sa incapa in careu
            raport = Element.resizeImgs(size);
            Line.resizeImgs(raport);
        } else
            raport = -1;

        if (AI) {
            // se executa doar daca se joaca contra calculatorului
            AiLevel = 6;
            LastAddedElement = new coordonates(Greenfoot.getRandomNumber(size), Greenfoot.getRandomNumber(size));
            AiMatrix = new Type[_size][_size];
        }
    }

    /**
     * creaza o mapa a vecinilor, folosita pentru indentificarea vecinilor pe linii
     * 0 -> |
     * 1 -> -
     * 2 -> \
     * 3 -> /
     */
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

    /**
     * transforma parametrii primiti in coordonate in lume
     * este folosita pentru adaugarea de elemente in lume
     * 
     * @param x   indexul liniei elementului
     * @param dim dimensiunea careului
     * @return coordonate din lume
     */
    private int turnXinCoord(int x, int dim) {
        int offset;
        // daca careul este mai mic de 10, se vor plasa elementele mai in interiorul
        // plansei
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
            return (x + offset) * elementDimension + 330;
        return (int) ((x + offset) * (elementDimension / raport) + 330 + elementDimension / 2);
    }

    /**
     * functioneaza la fel ca turnXinCoord ,doar ca pentru indexul coloanei
     * 
     * @param y   indexul coloanei elementului
     * @param dim dimensiunea plansei de joc
     * @return coordonatele in lume
     */
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
            return (y + offset) * elementDimension + 35;
        return (int) ((y + offset) * (elementDimension / raport) + 35 + elementDimension / 2);
    }

    /**
     * creaza gridul de joc, si adauga elementele in lume
     * 
     * @param n dimensiunea plansei
     */
    public void createGrid(int n) {
        // creaza gridul
        Elements.clear();
        MyList<Element> TempList;
        int tip;
        for (int i = 0; i < n; i++) {
            if (i == 0)
                tip = 0;
            else if (i < n - 1)
                tip = 3;
            else
                tip = 6;
            TempList = new MyList<>();
            for (int j = 0; j < n; j++) {
                if (j > 0 && tip % 3 == 0)
                    tip++;
                if (j == n - 1)
                    tip++;
                Element aiElement = new Element(i, j, this, tip);
                TempList.add(aiElement);
                getWorld().addObject(aiElement, turnXinCoord(i, n), turnYinCoord(j, n));

            }
            Elements.add(TempList);
        }

    }

    /**
     * pentru a se putea folosi lupa, s-au creat 25 de pointeri care se vor
     * intersecta cu elemente din careu, si le vor copia
     * aceasta functie creaza si adauga acesti pointeri in lume
     */
    private void createPointers() {
        pointer aiElement;
        for (int i = 0; i < 25; i++) {
            aiElement = new pointer(raport, i);
            getWorld().addObject(aiElement, 100, 100);
            crossair.add(aiElement);
        }
    }

    /**
     * functie care transforma gridul intr-o matrice, pe care sa o poata interpreta
     * algoritmul de Inteligenta Artificiala
     * 
     * AI function
     */
    private void transcriptGrid() {
        Type aiElement;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                aiElement = Elements.get(j).get(i).getStatus();
                AiMatrix[i][j] = aiElement;
            }
        }
    }

    /**
     * pentru a optimiza algoritmul de generare a urmatoarei mutari, am ales sa
     * pargurg careul in spirala, din interior spre exterior
     * aceasta functie genereaza parcurgerea in spirala incepand de la elementul cu
     * coordonatele memorate in startPosition
     * 
     * @param startPosition coordonatele elementului de inceput
     * @return lista cu coordonatele elementelor din parcurgerea in spirala
     * 
     *         AI function
     */
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

    /**
     * generam toate parcurgerile in spirala la inceputul jocului, si le memoram in
     * lista de coordonate a
     * 
     * AI function
     */

    private void createAllParcurgeri() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                a.add(getParcurgere(new coordonates(i, j)));

    }

    /**
     * aceasta functie gaseste urmatoarea mutare a calculatorui, prin apelarea unui
     * algoritm de tip minimax pentru fiecare element din careu
     * 
     * @param lastAdded ultimul element adaugat, elementul de la care se porneste
     *                  parcurgerea in spirala
     * @return coordonatele urmatoarei miscari a calculatorului
     * 
     *         AI function
     */

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
        // se va porni cu o adancime egala cu 0, iar daca nu se gaseste o miscare
        // concludenta, se va adanci pana la nivelul maxim retinut in AiLevel
        // daca se atinge adancimea maxima, se va face o evaluare statica, si se va
        // alege cea mai buna miscare
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
                    // apelarea algoritmului minimax pentru elementul de la coordonatele nextNeigh
                    if (localAiLevel == 0)
                        valoare = minimax(AiMatrix, localAiLevel, Type.Y, nextNeigh, Integer.MIN_VALUE,
                                Integer.MAX_VALUE, staticEval);
                    else
                        valoare = minimax(AiMatrix, localAiLevel, Type.Y, nextNeigh, Integer.MIN_VALUE,
                                Integer.MAX_VALUE, staticEval);
                    if (valoare == bestMove) {
                        // in cazul in care avem mai multe miscari egale calitativ, alegem una aleator
                        if (Greenfoot.getRandomNumber(300) == 100) {
                            nextMove = (Element) Elements.get(nextNeigh.y).get(nextNeigh.x);
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

    /**
     * verifica si returneaza un vector cu dimensiunile linilor pentru elementul
     * trimis ca parametru
     * 
     * @param grid         careul in stadiul curent
     * @param curentPlayer jucatorul curent
     * @param lastAdded    ultimul element adaugat
     * @return vector cu cele 4 dimensiuni ale celor 4 linii posibile |-\/
     * 
     *         AI function
     */
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

    /**
     * evaluarea statica a plansei de joc la un anumit stadiu
     * se va prelua cea mai mare linie
     * 
     * @param lenghts lungimiile ce vor fi evaluate
     * @return cea mai mare linie componenta
     * 
     *         AI function
     */
    private int staticEvaluation(int[] lenghts) {
        int max = 0;
        for (int i : lenghts) {
            // ar fi necesara o imbunatatire aici
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    /**
     * Algoritmul de minimax, ce va evalua plansa de joc, si va returna cate un scor
     * pentru fiecare miscare
     * 
     * @param grid         plansa curenta
     * @param depth        adancimea la care se face evaluarea
     * @param curentPlayer jucatorul pentru care se face evaluarea
     * @param lastAdded    coordonatele ultimului element adaugat
     * @param alpha        alpha, folosit la pruning
     * @param beta         betha, folosit la pruning
     * @param staticEval   variabila care permite sau nu evaluarea statica
     * @return un scor pentru miscarea primita prin lastAdded
     * 
     *         AI function
     */
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
                        // verificam posibilitatea pruning-ului
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
                        // verificam posibilitatea pruning-ului
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }

                return bestMove;
            }
        }

    }

    /**
     * functie ce primeste un element din careul, si il deschide (selecteaza)
     * 
     * @param Clicked elementul ce se doreste a fi deschis
     */
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
                    if (currentPlayer == Type.Y) {
                        refToCocos.startUimit();
                    } else {
                        refToCocos.startIncruntat();
                    }
                    gameState = State.ended;
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
            if (mutari == size * size) {
                // cazul de egalitate
                gameState = State.ended;
                refToCocos.ending(3);
            }
        }

        // dupa ce am gasit toti vecinii, sincronizam listele lor, cu lista din
        // elementul curent
        Clicked.sincLines();
    }

    /**
     * functie ce misca cei 25 de pointeri dupa mouse
     * se apeleaza doar in cazul in care avem un careu mai mare de 20 de elemente
     */
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
                cordx = mouse.getX() + (int) (elementDimension / raport * offsetPointer[0][index]);
                cordy = mouse.getY() + (int) (elementDimension / raport * offsetPointer[1][index]);
                iter.setLocation(cordx, cordy);
                index++;
            }
        }
    }

    /**
     * functia act() - se apeleaza in fiecare tick al jocului
     */
    public void act() {
        if (once) {
            // se efectueaza o singura data, la inceputul jocului
            if (AI) {
                createAllParcurgeri();
            }
            createGrid(size);
            createPointers();
            setImage(backBoard);
            ZoomElement.scaleImgs();
            once = false;
        }
        if (size > 20) {
            movePointer();
        }
        if (gameState != State.ended) {
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

    }
}