import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Cocos extends Actor {
    public static boolean startAnimation;
    private int ct;
    private static GreenfootImage[] pui = new GreenfootImage[7];

    public Cocos() {
        init();
        setImage(pui[0]);
    }

    private void init() {
        pui[0] = new GreenfootImage("cocos1.png");
        pui[1] = new GreenfootImage("cocos2.png");
        pui[2] = new GreenfootImage("cocos3.png");
        pui[3] = new GreenfootImage("cocos4.png");
        pui[4] = new GreenfootImage("cocos5.png");
        pui[5] = new GreenfootImage("cocos6.png");
        pui[6] = new GreenfootImage("cocos7.png");
        startAnimation = true;
        ct = 0;
    }

    public void act() {
        if (startAnimation) {
            ct++;
            if (ct % 10 == 0) {
                setImage(pui[ct / 10]);
            }
            if (ct == 60) {
                setImage(pui[0]);
                ct = 0;
                startAnimation = false;
            }
        }
    }
}
