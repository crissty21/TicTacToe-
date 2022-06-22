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
    private ShowText child;

    public ChangeLine(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
    }
    public void setChild(ShowText kid)
    {
        child = kid;
    }
    private int function(int x)
    {
        return (int)Math.round((x-175.471)/7.176)+1;
    }
    public void act() {
        super.act();
        lineSize = function(getX());
        if (Greenfoot.mouseDragEnded(this)) {
            lineSize = localLineSize;
        }
        if(child != null)
        {
            child.updateImage(lineSize);
        }
    }

}
