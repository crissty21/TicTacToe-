import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ChangeSize here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class ChangeSize extends buttons {
    public static int size = 80;
    private int localSize = size;
    public ChangeSize(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
    }

    private int function(int x)
    {
        return (int)Math.round((x-235.188)/1.271);
    }
    public void act() {
        super.act();
        size = function(getX());
        if (Greenfoot.mouseDragEnded(this)) {
            size = localSize;
        }
    }
}
