import greenfoot.*;

public class ZoomElement extends Actor {
    private Element refToCopied;
    private Type tip;
    private int curentImg;
    private static GreenfootImage xImg = new GreenfootImage("x_4.png");
    private static GreenfootImage oImg = new GreenfootImage("o_4.png");
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

    /*
     * private Line lineOver;
     * private boolean AddedLine;
     */
    public ZoomElement(Type _tip, Element _refToCopied) {
        refToCopied = _refToCopied;
        tip = _tip;
        choose();
        curentImg = -5;
        /*
         * lineOver = null;
         * AddedLine = false;
         */
    }

    private void choose() {
        switch (tip) {
            case X:
                setImage(xImg);
                break;
            case Y:
                setImage(oImg);
                break;
            default:
                setImage(new GreenfootImage(20, 20));
        }
    }

    public static void scaleImgs() {
        int dim = 20;
        xImg.scale(dim, dim);
        oImg.scale(dim, dim);
        for (GreenfootImage iter : animationO) {
            iter.scale(dim, dim);
        }
        for (GreenfootImage iter : animationX) {
            iter.scale(dim, dim);
        }
        for (GreenfootImage iter : drawOver) {
            iter.scale(dim, dim);
        }
    }

    public void act() {

        if (Brain.gameState != State.ended) {
            int img = refToCopied.imgDisplayed();
            if (curentImg != img) {
                curentImg = img;
                if (img == 0) {
                    setImage(new GreenfootImage(20, 20));
                }
                if (img > 0) {
                    if (img == 1) {
                        setImage(drawOver[0]);
                    } else if (img / 10 == 2) {
                        setImage(animationX[img % 10]);
                    }
                } else {
                    if (img == -1) {
                        setImage(drawOver[1]);
                    } else if (img / 10 == -2) {
                        setImage(animationO[-1 * img % 10]);
                    }
                }
            }
        }
        /*
         * /
         * int temp = refToCopied.getLineOver();
         * if (temp != -1 && AddedLine==false) {
         * lineOver = new Line(temp);
         * getWorld().addObject(lineOver, getX(), getY());
         * AddedLine = true;
         * }
         */
    }
    /*
     * protected void finalize() {
     * if(AddedLine)
     * {
     * getWorld().removeObject(lineOver);
     * }
     * }
     */
}
