import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class buttons here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class buttons extends decorations {

    private GreenfootImage hoverImage;
    public buttons(){}
    public buttons(GreenfootImage normal, GreenfootImage _hoverImage)
    {
        super(normal);
        hoverImage = _hoverImage;
    }
    public void act() {
        super.act();
        if (Greenfoot.mouseMoved(this))
            setImage(hoverImage);
        if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this))
            setImage(image);
    }
}
