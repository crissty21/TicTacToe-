import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Gun extends Actor {
    private GreenfootImage gunOn, gunOff;
    private boolean onOff;
    private int ct;
    private Actor target;

    public Gun() {
        init();
    }

    private void init() {
        gunOn = new GreenfootImage("gun1.png");
        gunOff = new GreenfootImage("gun.png");
        setImage(gunOff);
        turn(-30);
        onOff = false;
    }

    public void lookAtMe(Actor me) {
        turnTowards(me.getX(), me.getY());
        target = me;
    }

    public void turnGunOnOff() {
        onOff = true;
        ct = 0;
        setImage(gunOn);
        Element targetAsElement = (Element) target;
        if (targetAsElement != null) {
            getWorld().addObject(new Bullet((Element)target), 90, 610);
        }
    }

    public void act() {
        if (onOff) {
            ct++;
            if (ct == 30) {
                setImage(gunOff);
                onOff = false;
            }
        }
    }
}
