import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ChangeSize here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class ChangeSize extends buttons {
    public static int size = 80;
    public ChangeSize(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
        value = size;
    }

    private int function(int x)
    {
        return (int)Math.round((x-235.188)/1.271);
    }
    public void act() {
        super.act();
        value = function(getX());
        child.updateImage(value);
        if (Greenfoot.mouseDragEnded(this)) {
            size = value;
        }
    }
}
