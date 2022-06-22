import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class buttons here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class buttons extends decorations {

    private GreenfootImage hoverImage;
    private boolean moveLR;
    private int limits, limitd;

    public buttons() {
    }

    public buttons(GreenfootImage normal, GreenfootImage _hoverImage) {
        super(normal);
        hoverImage = _hoverImage;
        moveLR = false;
    }

    public void allowMoveLR(int _limits, int _limitd) {
        moveLR = true;
        limits = _limits;
        limitd = _limitd;
    }

    public void act() {
        super.act();
        if (Greenfoot.mouseMoved(this)) {
            setImage(hoverImage);
        }
        if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) {
            setImage(image);
        }

        if (Greenfoot.mouseDragged(this)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (moveLR) {
                if (mouse.getX() >= limits && mouse.getX() <= limitd) {
                    setLocation(mouse.getX(), getY());
                } else {
                    if (mouse.getX() < limits)
                        setLocation(limits, getY());
                    else
                        setLocation(limitd, getY());
                }
            }
        }
    }
}
