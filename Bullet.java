import java.util.List;

import javax.print.FlavorException;

import greenfoot.*;

public class Bullet extends Actor {
    private Element refToTarget;
    private GreenfootImage glont;
    private coordonates newLocation, moveHere;
    private boolean once;
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
        once = true;
        moveHere = new coordonates(refToTarget.getX(), refToTarget.getY());
    }

    public void act() {

        if (isTouching(Gun.class) == false && once) {
            setImage(glont);
            once = false;
        }
        newLocation = lerp(moveHere, 20);
        if (reachedDestination()) {
            Brain.gameState = State.animationOn;
            getWorld().removeObject(this);
        }
    }

    private boolean reachedDestination() {
        setLocation(newLocation.x, newLocation.y);
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

    private coordonates lerp(coordonates other, double speed) {
        // interpolare liniara
        double dx = other.x - getX(), dy = other.y - getY();
        double direction = Math.atan2(dy, dx);
        double x = getX() + (speed * Math.cos(direction));
        double y = getY() + (speed * Math.sin(direction));
        return new coordonates((int) x, (int) y);
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
