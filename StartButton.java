import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StartButton extends buttons
{
    public StartButton(GreenfootImage normal, GreenfootImage hoverImage)
    {
        super(normal,hoverImage);
    }
    public void act() 
    {
        super.act();
        if(Greenfoot.mouseClicked(this) == true)
        {
            Greenfoot.stop();
        }
    }    
}
