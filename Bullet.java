import java.util.List;


import greenfoot.*;

public class Bullet extends Actor {
    private Element refToTarget;
    private GreenfootImage glont;
    private Vector2d newLocation, moveHere;
    private int ct;
    public static float raport = 1;

    public Bullet(Element target) {
        initVars(target);
        initialSettings(target);
    }

    private void initialSettings(Element target) {
        resizeImg();
        turnTowards(target.getX(), target.getY());
        turn(-50);
    }

    private void initVars(Element target) {
        glont = new GreenfootImage("bullet.png");
        refToTarget = target;
        ct = 0;
        moveHere = new Vector2d(refToTarget.getX(), refToTarget.getY());
    }

    public void act() {
        ct++;
        if (ct == 20) {
            setImage(glont);
        }
        newLocation = lerp(moveHere, 10);
        if (reachedDestination()) {
            Brain.gameState = State.animationOn;
            getWorld().removeObject(this);
        }
    }

    private boolean reachedDestination() {
        setLocation(newLocation.getX(), newLocation.getY());
        if (isTouching(Element.class)) {
            List<Element> intersectingObjs = getIntersectingObjects(Element.class);
            for (Element iter : intersectingObjs) {
                if (iter == refToTarget) {
                    return true;
                }
            }
        }
        return false;
    }

    private Vector2d lerp(Vector2d other, double speed) {
        // interpolare liniara
        double dx = other.getX() - getX(), dy = other.getY() - getY();
        double direction = Math.atan2(dy, dx);
        double x = getX() + (speed * Math.cos(direction));
        double y = getY() + (speed * Math.sin(direction));
        return new Vector2d(x, y);
    }

    private void resizeImg() {
        int newWidth, newHeight;
        newWidth = (int) (glont.getWidth() / raport);
        newHeight = (int) (glont.getHeight() / raport);
        if (newWidth == 0)
            newWidth = 1;
        if (newHeight == 0)
            newHeight = 1;
        glont.scale(newWidth, newHeight);
    }
}

class Vector2d {
    private int X, Y;

    public Vector2d(int x, int y) {
        X = x;
        Y = y;
    }

    public Vector2d(Double x, Double y) {
        X = x.intValue();
        Y = y.intValue();
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }
}