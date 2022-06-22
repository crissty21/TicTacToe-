import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartGame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StartGame extends buttons
{
    public StartGame(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
    }
    public void act() 
    {
        super.act();
        if(Greenfoot.mouseClicked(this))
        {
            Greenfoot.setWorld(new Lvl10X10(ChangeSize.size,ChangeLine.lineSize));
        }
    }    
}
