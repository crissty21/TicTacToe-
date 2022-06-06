import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.*;

public class Element extends Actor {
    private int x, y;
    private List<List<Element>> Lines; // lista cu linile din care face parte obiectul
    private Type Val;
    private Brain refToBrain;
    private boolean selected;
    private int contor;
    private Gun refToGun;

    private static GreenfootImage xImg = new GreenfootImage("as.png");
    private static GreenfootImage oImg = new GreenfootImage("os.png");
    private static GreenfootImage[] explozie = {
            new GreenfootImage("box1.png"),
            new GreenfootImage("box2.png"),
            new GreenfootImage("box3.png"),
            new GreenfootImage("box4.png"),
            new GreenfootImage("box5.png"),
            new GreenfootImage("box6.png"),
            new GreenfootImage("box7.png"),
            new GreenfootImage("box8.png"),
            new GreenfootImage("box9.png"),
            new GreenfootImage("box10.png"),
            new GreenfootImage("box11.png"),
            new GreenfootImage("box12.png"),
            new GreenfootImage("box13.png")
    };

    public Element() {
        setImage(explozie[0]);
        x = y = 0;
        Val = Val.notOpened;
        Lines = new ArrayList<>();
        selected = false;
        for (int i = 0; i < 4; i++)
            Lines.add(new ArrayList<Element>());
    }

    public Element(int CoordX, int CoordY) {
        this();
        x = CoordX;
        y = CoordY;
    }

    public Element(int CoordX, int CoordY, Brain refToOwner, Gun _refToGun) {
        this(CoordX, CoordY);
        refToBrain = refToOwner;
        refToGun = _refToGun;
    }

    public void setStatus(Type newStatus) {
        Val = newStatus;
    }

    public Type getStatus() {
        return Val;
    }

    public int getLineLenght(int index) {
        return Lines.get(index).size();
    }

    public int getCoordX() {
        return x;
    }

    public int getCoordY() {
        return y;
    }

    public void addOnLine(int index, Element vecin) {
        Lines.get(index).add(vecin);
    }

    public void addLineOnLine(int index, List<Element> newLine) {
        Lines.get(index).addAll(newLine);
    }

    public List<Element> getLine(int index) {
        return Lines.get(index);
    }

    public boolean containsOnLine(int line, Element second) {
        return Lines.get(line).contains(second);
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

    public void choose() // selecteaza x sau 0 pt caseta
    {
        refToBrain.clicked(this);
        if (Brain.CurrentPlayer == Type.X) {
            Brain.CurrentPlayer = Type.Y;
            setImage(xImg);
        } else {
            Brain.CurrentPlayer = Type.X;
            setImage(oImg);
        }
        Next.start1 = true;
        Brain.gameState = State.waitForMove;
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            // cooldown
            if (Brain.gameState == State.waitForMove) {
                if (Val == Type.notOpened) // verifica daca nu a fost deschisa cutia
                {
                    refToGun.lookAtMe(this);
                    Cocos.startAnimation = true;
                    Brain.mutari++;
                    Val = Brain.CurrentPlayer;
                    Brain.gameState = State.WaitingForBullet;
                    selected = true;
                    contor = 0;
                }
            }
        }
        if (selected && Brain.gameState == State.animationOn) {
            contor++;
            if (contor % 5 == 0) {
                setImage(explozie[contor / 5]);
            }
            if (contor == 30) {
                Next.start = true;
            }
            if (contor >= 60) {
                selected = false;
                choose();
            }
        }
    }

    public static float resizeImgs(int size) {
        int initialSpace = 50,
                maxSpace = 500,
                dim = 38;
        int newHeight, newWidth;
        float raport = (float) initialSpace / (float) ((float) maxSpace / (float) size);
        
        GreenfootImage temp = new GreenfootImage((int)(initialSpace/raport),(int)(initialSpace/raport));
        xImg.scale((int) (dim / raport), (int) (dim / raport));
        oImg.scale((int) (dim / raport), (int) (dim / raport));
        temp.drawImage(xImg, 0, 0);
        xImg = temp;
        temp = new GreenfootImage((int)(initialSpace/raport),(int)(initialSpace/raport));
        temp.drawImage(oImg, 0, 0);
        oImg = temp;
        int i=0;
        for (GreenfootImage iter : explozie) {
            newWidth = (int) (iter.getWidth() / raport);
            newHeight = (int) (iter.getHeight() / raport);
            if (newWidth == 0) {
                newWidth = 1;
            }
            if (newHeight == 0) {
                newHeight = 1;
            }
            iter.scale(newWidth, newHeight);
            temp = new GreenfootImage((int)(initialSpace/raport),(int)(initialSpace/raport));
            //temp.fillRect(0, 0, (int)(initialSpace/raport),(int)(initialSpace/raport));
            temp.drawImage(iter, 0, 0);
            explozie[i++] = temp;
        }
        return raport;
    }

    public static void initImgs() {
        xImg = new GreenfootImage("as.png");
        oImg = new GreenfootImage("os.png");
        explozie[0] = new GreenfootImage("box1.png");
        explozie[1] = new GreenfootImage("box2.png");
        explozie[2] = new GreenfootImage("box3.png");
        explozie[3] = new GreenfootImage("box4.png");
        explozie[4] = new GreenfootImage("box5.png");
        explozie[5] = new GreenfootImage("box6.png");
        explozie[6] = new GreenfootImage("box7.png");
        explozie[7] = new GreenfootImage("box8.png");
        explozie[8] = new GreenfootImage("box9.png");
        explozie[9] = new GreenfootImage("box10.png");
        explozie[10] = new GreenfootImage("box11.png");
        explozie[11] = new GreenfootImage("box12.png");
        explozie[12] = new GreenfootImage("box13.png");
    }

}
