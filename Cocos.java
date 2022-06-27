
import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * clasa characterului jocului, cocosul gangster
 */
public class Cocos extends Actor {

    private int ct;
    // vectori de imagini pentru animatii
    private static GreenfootImage[] cocosClipit = new GreenfootImage[7];
    private static GreenfootImage[] cocosLose = new GreenfootImage[3];
    private static GreenfootImage[] cocosWin = new GreenfootImage[3];

    private boolean clipit;
    private boolean incruntat;
    private boolean uimit;

    private coordonates start, finish;
    private boolean bMove;
    private int moveSpeed;

    public Cocos() {
        init();
        setImage(cocosClipit[0]);
    }

    private boolean End;

    private void init() {
        cocosClipit[0] = new GreenfootImage("images\\biro_1.png");
        cocosClipit[1] = new GreenfootImage("images\\biro_2.png");
        cocosClipit[2] = new GreenfootImage("images\\biro_3.png");
        cocosClipit[3] = new GreenfootImage("images\\biro_4.png");
        cocosClipit[4] = new GreenfootImage("images\\biro_3.png");
        cocosClipit[5] = new GreenfootImage("images\\biro_2.png");
        cocosClipit[6] = new GreenfootImage("images\\biro_1.png");

        cocosWin[0] = new GreenfootImage("images\\biro_1.png");
        cocosWin[1] = new GreenfootImage("images\\biro_5.png");
        cocosWin[2] = new GreenfootImage("images\\biro_6.png");

        cocosLose[0] = new GreenfootImage("images\\biro_1.png");
        cocosLose[1] = new GreenfootImage("images\\biro_7.png");
        cocosLose[2] = new GreenfootImage("images\\biro_8.png");

        clipit = false;
        incruntat = false;
        uimit = false;
        End = false;
        ct = 0;
    }

    /**
     * porneste animatia de clipit
     */
    public void startClipit() {
        clipit = true;
        ct = 0;
    }

    /**
     * porneste animatia de uimire
     */
    public void startUimit() {
        uimit = true;
        setImage(cocosClipit[0]);
        ct = 0;
    }

    /**
     * porneste animatia de incruntare
     */
    public void startIncruntat() {
        incruntat = true;
        setImage(cocosClipit[0]);
        ct = 0;
    }

    /**
     * functie ce va porni animatia de moveFromTo
     * 
     * @param _start  coordonatele punctului de pornire
     * @param _finish coordonatele punctului de sosire
     * @param speed   viteza cu care se va deplasa
     */
    public void moveFromTo(coordonates _start, coordonates _finish, int speed) {
        start = _start;
        finish = _finish;
        bMove = true;
        moveSpeed = speed;
        setLocation(start.x, start.y);
    }

    /**
     * functia act() ruleaza in fiecare tick
     * reda diferite animatii
     */
    public void act() {
        if (bMove) {
            coordonates newLocation = lerp(finish, moveSpeed);
            setLocation(newLocation.x, newLocation.y);
            if (Math.abs(newLocation.x - finish.x) <= 1 && Math.abs(newLocation.y - finish.y) <= 1) {
                setLocation(finish.x, finish.y);
                bMove = false;
            }
        }
        ct++;

        if (!clipit && !incruntat && !uimit && !End) {
            if (ct == 300) {
                clipit = true;
                ct = 0;
            }
        }
        if (clipit) {

            if (ct % 10 == 0) {
                setImage(cocosClipit[ct / 10]);
            }
            if (ct == 60) {
                setImage(cocosClipit[0]);
                ct = 0;
                clipit = false;
            }
        }

        if (incruntat) {
            if (ct % 10 == 0) {
                setImage(cocosWin[ct / 10]);
            }
            if (ct == 20) {
                ending(1);
                incruntat = false;
            }

        }

        if (uimit) {
            if (ct % 10 == 0) {
                setImage(cocosLose[ct / 10]);
            }
            if (ct == 20) {
                ending(2);
                uimit = false;
            }
        }

        if (ct == 600 && End) {
            Greenfoot.setWorld(new MainMenu());
        }
    }

    /**
     * incheie jocul
     * 
     * @param tip tipul in care s-a terminat jocul:
     *            1 - a castigat X
     *            2 - a castigat 0
     *            3 - egalitate
     */
    public void ending(int tip) {
        decorations placa = new decorations(new GreenfootImage("images\\win_plaque.png"));
        decorations scris = new decorations(new GreenfootImage(1, 1));
        placa.goInvisible();
        scris.goInvisible();
        getWorld().addObject(placa, 450, 350);
        if (tip == 1) {
            scris.setMyImage(new GreenfootImage("images\\x_wins.png"));

        } else if (tip == 2) {
            scris.setMyImage(new GreenfootImage("images\\0_wins.png"));
        } else {
            scris.setMyImage(new GreenfootImage("images\\draw_wins.png"));
        }
        getWorld().addObject(scris, 450, 380);
        placa.fadeIn(5);
        scris.fadeIn(5);
        getWorld().setPaintOrder(decorations.class);
        End = true;
    }

    /**
     * interpolare liniara intre coordonatele obiectului si coordonatele pasate
     * 
     * @param other setul secund cu care se vor interpola coordonatele obiectului
     * @param speed viteza de interpolare
     * @return set de coordonate intermediare
     */
    protected coordonates lerp(coordonates other, double speed) {
        // interpolare liniara
        double dx = other.x - getX(), dy = other.y - getY();
        double direction = Math.atan2(dy, dx);
        Double x = getX() + (speed * Math.cos(direction));
        Double y = getY() + (speed * Math.sin(direction));
        return new coordonates(x.intValue(), y.intValue());
    }
}
