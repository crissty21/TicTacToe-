import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Element extends GridElement {
    private int img;
    private int openAnimationSpeed = 5;
    private int contor;
    private boolean selected;
    private boolean mouseOver = false;
    private Brain refToBrain;
    private static int drawOffset = 9;
    private static boolean AI;
    private GreenfootImage temp;
    private static GreenfootImage[] tiles = {
            new GreenfootImage("back_tile_1.png"),
            new GreenfootImage("back_tile_4.png"),
            new GreenfootImage("back_tile_7.png"),
            new GreenfootImage("back_tile_2.png"),
            new GreenfootImage("back_tile_5.png"),
            new GreenfootImage("back_tile_8.png"),
            new GreenfootImage("back_tile_3.png"),
            new GreenfootImage("back_tile_6.png"),
            new GreenfootImage("back_tile_9.png")
    };
    private static GreenfootImage[] drawOver = {
            new GreenfootImage("x_front.png"),
            new GreenfootImage("o_front.png")
    };

    private static GreenfootImage[] animationX = {
            new GreenfootImage("x_1.png"),
            new GreenfootImage("x_2.png"),
            new GreenfootImage("x_3.png"),
            new GreenfootImage("x_4.png")
    };
    private static GreenfootImage[] animationO = {
            new GreenfootImage("o_1.png"),
            new GreenfootImage("o_2.png"),
            new GreenfootImage("o_3.png"),
            new GreenfootImage("o_4.png")
    };
    private GreenfootImage MyImage;

    public Element(int CoordX, int CoordY) {
        super(CoordX, CoordY);
        // initImgs();
        selected = false;
        contor = 0;
    }

    public Element(int CoordX, int CoordY, Brain refToOwner, int tip) {
        this(CoordX, CoordY);
        setImage(tiles[tip]);
        MyImage = tiles[tip];
        refToBrain = refToOwner;
        AI = false;
    }

    public static void setAi(boolean value) {
        AI = value;
    }

    public int imgDisplayed() {
        return img;
    }

    public void choose() // selecteaza x sau 0 pt caseta
    {
        refToBrain.clicked(this);
        if (Brain.currentPlayer == Type.X) {
            Brain.currentPlayer = Type.Y;
            // setImage(xImg);
        } else {
            Brain.currentPlayer = Type.X;
            // setImage(oImg);
        }
        Next.start1 = true;
    }

    public void openIt() {
        // refToGun.lookAtMe(this);
        // Cocos.startAnimation = true;
        Brain.mutari++;
        Val = Brain.currentPlayer;
        // Brain.gameState = State.WaitingForBullet;
        Brain.gameState = State.animationOn;
        selected = true;
        contor = 0;
    }

    public void act() {

        if (Brain.gameState != State.ended) {
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
                if (Brain.currentPlayer == Type.X) {
                    if (contor % openAnimationSpeed == 0) {
                        temp = new GreenfootImage(MyImage);
                        temp.drawImage(animationX[contor / openAnimationSpeed - 1], drawOffset, drawOffset);
                        img = 2 * 10 + contor / openAnimationSpeed - 1;
                        setImage(temp);
                    }
                    if (contor == openAnimationSpeed * 3) {
                        Next.start = true;
                    }
                    if (contor >= openAnimationSpeed * 4) {
                        selected = false;
                        choose();
                        contor = 0;
                    }
                } else {
                    if (contor % openAnimationSpeed == 0) {
                        temp = new GreenfootImage(MyImage);
                        temp.drawImage(animationO[contor / openAnimationSpeed - 1], drawOffset, drawOffset);
                        img = -2 * 10 - contor / openAnimationSpeed + 1;
                        setImage(temp);
                    }
                    if (contor == openAnimationSpeed * 3) {
                        Next.start = true;
                    }
                    if (contor >= openAnimationSpeed * 4) {
                        selected = false;
                        choose();
                        contor = 0;
                    }
                }
            }
            if (!mouseOver && Val == Type.notOpened) {
                if (Greenfoot.mouseMoved(this)) {
                    temp = new GreenfootImage(MyImage);
                    if (Brain.currentPlayer == Type.X) {
                        temp.drawImage(drawOver[0], drawOffset, drawOffset);
                        img = 1;
                    } else {
                        temp.drawImage(drawOver[1], drawOffset, drawOffset);
                        img = -1;
                    }
                    setImage(temp);
                    mouseOver = true;
                }
            }
            if (mouseOver && Val == Type.notOpened && Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) {
                setImage(MyImage);
                img = 0;
                mouseOver = false;
            }
        }
    }

    public static float resizeImgs(int size) {
        int initialSpace = 48,
                maxSpace = 480;
        int newHeight, newWidth;
        float raport = (float) initialSpace / (float) ((float) maxSpace / (float) size);

        GreenfootImage auxiliar = new GreenfootImage((int) (initialSpace / raport), (int) (initialSpace / raport));
        int i = 0;

        drawOffset = (int) (drawOffset / raport);

        for (GreenfootImage iter : tiles) {
            newWidth = (int) (iter.getWidth() / raport);
            newHeight = (int) (iter.getHeight() / raport);
            if (newWidth == 0) {
                newWidth = 1;
            }
            if (newHeight == 0) {
                newHeight = 1;
            }
            iter.scale(newWidth, newHeight);
            auxiliar = new GreenfootImage((int) (initialSpace / raport), (int) (initialSpace
                    / raport));
            auxiliar.drawImage(iter, 0, 0);
            tiles[i++] = auxiliar;
        }

        i = 0;
        for (GreenfootImage iter : drawOver) {
            newWidth = (int) (iter.getWidth() / raport);
            newHeight = (int) (iter.getHeight() / raport);
            if (newWidth == 0) {
                newWidth = 1;
            }
            if (newHeight == 0) {
                newHeight = 1;
            }
            iter.scale(newWidth, newHeight);
            auxiliar = new GreenfootImage((int) (initialSpace / raport), (int) (initialSpace
                    / raport));
            auxiliar.drawImage(iter, 0, 0);
            drawOver[i++] = auxiliar;
        }
        i = 0;
        for (GreenfootImage iter : animationO) {
            newWidth = (int) (iter.getWidth() / raport);
            newHeight = (int) (iter.getHeight() / raport);
            if (newWidth == 0) {
                newWidth = 1;
            }
            if (newHeight == 0) {
                newHeight = 1;
            }
            iter.scale(newWidth, newHeight);
            auxiliar = new GreenfootImage((int) (initialSpace / raport), (int) (initialSpace
                    / raport));
            auxiliar.drawImage(iter, 0, 0);
            animationO[i++] = auxiliar;
        }
        i = 0;
        for (GreenfootImage iter : animationX) {
            newWidth = (int) (iter.getWidth() / raport);
            newHeight = (int) (iter.getHeight() / raport);
            if (newWidth == 0) {
                newWidth = 1;
            }
            if (newHeight == 0) {
                newHeight = 1;
            }
            iter.scale(newWidth, newHeight);
            auxiliar = new GreenfootImage((int) (initialSpace / raport), (int) (initialSpace
                    / raport));
            auxiliar.drawImage(iter, 0, 0);
            animationX[i++] = auxiliar;
        }

        return raport;
    }

    public static void initImgs() {

        drawOffset = 9;
        tiles[0] = new GreenfootImage("back_tile_1.png");
        tiles[1] = new GreenfootImage("back_tile_4.png");
        tiles[2] = new GreenfootImage("back_tile_7.png");
        tiles[3] = new GreenfootImage("back_tile_2.png");
        tiles[4] = new GreenfootImage("back_tile_5.png");
        tiles[5] = new GreenfootImage("back_tile_8.png");
        tiles[6] = new GreenfootImage("back_tile_3.png");
        tiles[7] = new GreenfootImage("back_tile_6.png");
        tiles[8] = new GreenfootImage("back_tile_9.png");

        drawOver[0] = new GreenfootImage("x_front.png");
        drawOver[1] = new GreenfootImage("o_front.png");

        animationX[0] = new GreenfootImage("x_1.png");
        animationX[1] = new GreenfootImage("x_2.png");
        animationX[2] = new GreenfootImage("x_3.png");
        animationX[3] = new GreenfootImage("x_4.png");

        animationO[0] = new GreenfootImage("o_1.png");
        animationO[1] = new GreenfootImage("o_2.png");
        animationO[2] = new GreenfootImage("o_3.png");
        animationO[3] = new GreenfootImage("o_4.png");

    }
}
