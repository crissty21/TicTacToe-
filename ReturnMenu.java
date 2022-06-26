import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ReturnMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ReturnMenu extends buttons
{
    public ReturnMenu(GreenfootImage idle, GreenfootImage selected)
    {
        super(idle, selected);
    }
    public void act() 
    {
        super.act();
        if(Greenfoot.mouseClicked(this))
        {
            Greenfoot.setWorld(new MainMenu());
        }
    }    
}
