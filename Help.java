import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class help here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Help extends Actor
{
    
    boolean once;
    boolean tip;
    //public static Numere _4;
    GreenfootImage imgs[] = new GreenfootImage[3];
    
    public Help()
    {
        imgs[0] = new GreenfootImage("help.png");
        imgs[1] = new GreenfootImage("help2.png");
        imgs[2] = new GreenfootImage("helppanel.png");
        setImage(imgs[1]);
        tip = true;
    }

    public void act() 
    {
        if(once)
        {
            once = false;
        }
        MouseInfo mouse = Greenfoot.getMouseInfo();  //animatia de miscare a butonului
        if(tip)
        if (mouse != null) 
        {  
            setImage(imgs[1]);
            List objects = getWorld().getObjectsAt(mouse.getX(), mouse.getY(),Help.class);  
            for (Object object : objects)  
            {  
                if (object == this)  
                { 
                    setImage(imgs[0]);
                } 
            }  
        }
        if(Greenfoot.mouseClicked(this))
        {
            if(tip)
            {
                tip = false;
                setImage(imgs[2]);
                setLocation(500, 350);
                getWorld().setPaintOrder(Help.class);
            }
            else
            {
                tip = true;
                setImage(imgs[1]);
                setLocation(860,660);
            }
        }
    }    
}
