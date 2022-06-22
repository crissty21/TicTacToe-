import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ChangeLine here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class ChangeLine extends buttons {
    public static int lineSize = 14;
    private int localLineSize = lineSize;

    public ChangeLine(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
    }

    private int function(int x)
    {
        return (int)Math.round((x-205.471)/7.176);
    }
    public void act() {
        super.act();
        lineSize = function(getX());
        if (Greenfoot.mouseDragEnded(this)) {
            lineSize = localLineSize;
        }
    }

}
