import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * buton ce ne intoarce la meniul principal
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
