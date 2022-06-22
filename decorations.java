import greenfoot.*;

public class decorations extends Actor {
    private boolean bFadeIn;
    private boolean bFadeOut;
    private boolean bPulse;
    private int fadeSpeed;
    private int fadeIndex;
    protected GreenfootImage image;

    private coordonates start;
    private coordonates finish;
    private int moveSpeed;
    private boolean bMove;

    public decorations() {
    }

    public decorations(GreenfootImage img) {
        image = img;
        setImage(image);
        bFadeIn = false;
    }

    public void fadeIn(int speed) {
        bFadeIn = true;
        bFadeOut = false;
        fadeSpeed = speed;
        fadeIndex = 0;
    }

    public void fadeOut(int speed) {
        bFadeIn = false;
        bFadeOut = true;
        fadeSpeed = speed;
        fadeIndex = 255;
    }

    public void pulse(int speed) {
        bPulse = true;
        fadeSpeed = speed;
    }

    public void moveFromTo(coordonates _start, coordonates _finish, int speed) {
        start = _start;
        setLocation(start.x, start.y);
        finish = _finish;
        bMove = true;
        moveSpeed = speed;
    }

    public void goInvisible() {
        image.setTransparency(0);
    }

    public void act() {
        if (bFadeIn) {
            if (fadeIndex < 255) {
                image.setTransparency(fadeIndex);
                fadeIndex += fadeSpeed;
            } else {
                fadeIndex = 255;
                image.setTransparency(255);
                bFadeIn = false;
            }
        } else if (bFadeOut) {
            if (fadeIndex > 0) {
                image.setTransparency(fadeIndex);
                fadeIndex -= fadeSpeed;
            } else {
                fadeIndex = 0;
                image.setTransparency(0);
                bFadeOut = false;
            }
        }
        if (bPulse) {
            if (fadeIndex == 0) {
                bFadeIn = true;
                bFadeOut = false;
            }
            if (fadeIndex == 255) {
                bFadeOut = true;
                bFadeIn = false;
            }
        }
        if (bMove) {
            coordonates newLocation = lerp(finish, moveSpeed);
            setLocation(newLocation.x, newLocation.y);
            if (Math.abs(newLocation.x - finish.x) <= 1 && Math.abs(newLocation.y - finish.y) <= 1) {
                setLocation(finish.x, finish.y);
                bMove = false;
            }
        }
    }

    protected coordonates lerp(coordonates other, double speed) {
        // interpolare liniara
        double dx = other.x - getX(), dy = other.y - getY();
        double direction = Math.atan2(dy, dx);
        Double x = getX() + (speed * Math.cos(direction));
        Double y = getY() + (speed * Math.sin(direction));
        return new coordonates(x.intValue(), y.intValue());
    }
}
