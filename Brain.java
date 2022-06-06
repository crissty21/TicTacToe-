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
    private float raport;
    private boolean ok;// semafor folosit pentru crearea unui eveniment begin play
    private Gun refToGun;
    private int[] mapNeigh;// mapeaza vecinii pe linii, in functie de oridinea lor
    private final int[][] offsetNeigh = { { -1, -1, -1, 0, 0, 1, 1, 1 }, { -1, 0, 1, -1, 1, -1, 0, 1 } }; // offsetul la
                                                                                                          // care se
    private final int[][] offsetPointer = {
            { -2, -2, -2, -2, -2, -1, -1, -1, -1, -1,  0,  0, 0, 0, 0,  1,  1, 1, 1, 1,  2,  2, 2, 2, 2 },
            { -2, -1,  0,  1,  2, -2, -1,  0,  1,  2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2, -2, -1, 0, 1, 2 } }; 

    private List<pointer> crossair = new ArrayList<>();

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
        img.setColor(Color.BLACK);
        img.fillRect(0, 0, 500, 500);
        setImage(img);
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

        pointer temp;
        for (int i = 0; i < 25; i++) {
            temp = new pointer(raport, i);
            getWorld().addObject(temp, 100, 100);
            crossair.add(temp);
        }
        ZoomElement.scaleImgs();
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

    public void act() {
        if (ok) {
            // begin play
            ok = false;
            createGrid(size);
        }
        if (size > 20) {
            movePointer();
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