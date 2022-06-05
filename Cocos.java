import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Cocos extends Actor {
    public static boolean startAnimation;
    int ct;
    Gun refToGun;
    public static GreenfootImage[] pui = new GreenfootImage[7];

    public Cocos(Gun _refToGun) {
        init(_refToGun);
        setImage(pui[0]);
    }

    private void init(Gun _refToGun) {
        pui[0] = new GreenfootImage("cocos1.png");
        pui[1] = new GreenfootImage("cocos2.png");
        pui[2] = new GreenfootImage("cocos3.png");
        pui[3] = new GreenfootImage("cocos4.png");
        pui[4] = new GreenfootImage("cocos5.png");
        pui[5] = new GreenfootImage("cocos6.png");
        pui[6] = new GreenfootImage("cocos7.png");
        startAnimation = true;
        refToGun = _refToGun;
        ct = 0;
    }

    public void act() {
        if (startAnimation) {
            ct++;
            if (ct % 10 == 0)
                setImage(pui[ct / 10]);
            if (ct == 60) {
                refToGun.turnGunOnOff();
                setImage(pui[0]);
                ct = 0;
                startAnimation = false;
            }
        }
    }
}