import greenfoot.*;

public class ZoomElement extends Actor {
    private Element refToCopied;
    private Type tip;
    private int curentImg;
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
                setImage(explozie[0]);
        }
    }

    public static void scaleImgs() {
        int dim = 20;
        xImg.scale(dim, dim);
        oImg.scale(dim, dim);
        for (GreenfootImage iter : explozie) {
            iter.scale(dim, dim);
        }
    }

    public void act() {
        int img = refToCopied.imgDisplayed();
        if (img != curentImg) {
            curentImg = img;
            switch (img) {
                case -1:
                    setImage(xImg);
                    break;
                case -2:
                    setImage(oImg);
                    break;
                case -3:
                    break;
                default:
                    setImage(explozie[img]);
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
