import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class ExitButton extends buttons
{
    public ExitButton(GreenfootImage normal, GreenfootImage hoverImage)
    {
        super(normal,hoverImage);
    }
    public void act() 
    {
        super.act();
        if(Greenfoot.mouseClicked(this) == true)
        {
            setImage(image);
            Greenfoot.stop();
        }
    }   
}
