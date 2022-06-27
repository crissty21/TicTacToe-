import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * linia ce vine trasata la final
 */
public class Line extends Actor {
    int orientare;
    public static GreenfootImage[] linie = {
            new GreenfootImage("images\\line_1.png"),
            new GreenfootImage("images\\line_2.png"),
            new GreenfootImage("images\\line_4.png"),
            new GreenfootImage("images\\line_3.png")
    };

    public Line(int ori) {
        setImage(linie[ori]);
    }

    /**
     * schimba dimensiunea imaginilor in functie de dimensiunea careului de joc
     * 
     * @param raport dimensiunea careului de joc
     */
    public static void resizeImgs(float raport) {
        int newWidth, newHeight;
        for (GreenfootImage iter : linie) {
            newWidth = (int) (iter.getWidth() / raport);
            newHeight = (int) (iter.getHeight() / raport);
            if (newWidth == 0)
                newWidth = 1;
            if (newHeight == 0)
                newHeight = 1;
            iter.scale(newWidth, newHeight);
        }
    }

    /**
     * reinitializeaza toate imaginile
     */
    public static void initImgs() {
        linie[0] = new GreenfootImage("line_1.png");
        linie[1] = new GreenfootImage("line_2.png");
        linie[2] = new GreenfootImage("line_4.png");
        linie[3] = new GreenfootImage("line_3.png");
    }
}
