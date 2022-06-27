import greenfoot.*; 

/**
 * afiseaza care este urmatorul jucator
 */
public class Next extends Brain {
    public static boolean start, start1;
    public static int ct;
    static int a;

    GreenfootImage[] xtoo = new GreenfootImage[9];
    GreenfootImage[] otox = new GreenfootImage[9];

    public Next() {
        initImgs();
        start = start1 = false;
        ct = 0;
        if (Brain.currentPlayer == Type.X)
            setImage(xtoo[0]);
        else
            setImage(otox[0]);
    }

    private void initImgs() {
        xtoo[0] = new GreenfootImage("images\\x_to_0_1.png");
        xtoo[1] = new GreenfootImage("images\\x_to_0_2.png");
        xtoo[2] = new GreenfootImage("images\\x_to_0_3.png");
        xtoo[3] = new GreenfootImage("images\\x_to_0_4.png");
        xtoo[4] = new GreenfootImage("images\\x_to_0_5.png");
        xtoo[5] = new GreenfootImage("images\\x_to_0_6.png");
        xtoo[6] = new GreenfootImage("images\\x_to_0_7.png");
        xtoo[7] = new GreenfootImage("images\\x_to_0_8.png");
        xtoo[8] = new GreenfootImage("images\\x_to_0_9.png");

        otox[0] = new GreenfootImage("images\\x_to_0_9.png");
        otox[1] = new GreenfootImage("images\\x_to_0_10.png");
        otox[2] = new GreenfootImage("images\\x_to_0_11.png");
        otox[3] = new GreenfootImage("images\\x_to_0_12.png");
        otox[4] = new GreenfootImage("images\\x_to_0_13.png");
        otox[5] = new GreenfootImage("images\\x_to_0_14.png");
        otox[6] = new GreenfootImage("images\\x_to_0_15.png");
        otox[7] = new GreenfootImage("images\\x_to_0_16.png");
        otox[8] = new GreenfootImage("images\\x_to_0_1.png");
    }

    public void act() {
        if (Brain.gameState != State.ended) {
            if (start) {
                ct++;
                if (ct > 5 && ct % 5 == 0)
                    if (currentPlayer == Type.Y)
                        setImage(xtoo[ct / 5 - 2]);
                    else
                        setImage(otox[ct / 5 - 2]);
                if (ct == 50) {
                    ct = 0;
                    start = false;
                    Brain.gameState = State.waitForMove;
                }
            }
        }
    }
}
