import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Element extends GridElement {
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
    private static boolean AI;
    

    public Element(int CoordX, int CoordY) {
        super(CoordX, CoordY);
        //initImgs();
        setImage(explozie[0]);
        selected = false;
        contor = 0;
    }

    public Element(int CoordX, int CoordY, Brain refToOwner, Gun _refToGun) {
        this(CoordX, CoordY);
        refToBrain = refToOwner;
        refToGun = _refToGun;
    }

    public static void setAi(boolean value) {
        AI = value;
    }

    public int imgDisplayed() {
        GreenfootImage curentImage = getImage();
        if (curentImage == xImg)
            return -1;
        if (curentImage == oImg)
            return -2;
        return contor / 5;
    }

    public void choose() // selecteaza x sau 0 pt caseta
    {
        refToBrain.clicked(this);
        if (Brain.currentPlayer == Type.X) {
            Brain.currentPlayer = Type.Y;
            setImage(xImg);
        } else {
            Brain.currentPlayer = Type.X;
            setImage(oImg);
        }
        Next.start1 = true;
        Brain.gameState = State.waitForMove;
    }

    public void openIt() {
        refToGun.lookAtMe(this);
        Cocos.startAnimation = true;
        Brain.mutari++;
        Val = Brain.currentPlayer;
        Brain.gameState = State.WaitingForBullet;
        selected = true;
        contor = 0;
    }

    public void act() {

        if (Greenfoot.mouseClicked(this)) {
            if (AI) {
                // in cazul acesta, putem adauga doar daca e randul jucatorului Y
                if (Brain.currentPlayer == Type.Y) {
                    // cooldown
                    if (Brain.gameState == State.waitForMove) {
                        if (Val == Type.notOpened) // verifica daca nu a fost deschisa cutia
                        {
                            openIt();
                        }
                    }
                }
            } else {
                if (Brain.gameState == State.waitForMove) {
                    if (Val == Type.notOpened) // verifica daca nu a fost deschisa cutia
                    {
                        openIt();
                    }
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
                contor = 0;
            }
        }
    }

    public static float resizeImgs(int size) {
        int initialSpace = 50,
                maxSpace = 500,
                dim = 38;
        int newHeight, newWidth;
        float raport = (float) initialSpace / (float) ((float) maxSpace / (float) size);

        GreenfootImage temp = new GreenfootImage((int) (initialSpace / raport), (int) (initialSpace / raport));
        xImg.scale((int) (dim / raport), (int) (dim / raport));
        oImg.scale((int) (dim / raport), (int) (dim / raport));
        temp.drawImage(xImg, 0, 0);
        xImg = temp;
        temp = new GreenfootImage((int) (initialSpace / raport), (int) (initialSpace / raport));
        temp.drawImage(oImg, 0, 0);
        oImg = temp;
        int i = 0;
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
            temp = new GreenfootImage((int) (initialSpace / raport), (int) (initialSpace / raport));
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
