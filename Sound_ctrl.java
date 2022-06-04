import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

public class Sound_ctrl extends Actor
{
    boolean liber,once = true;
    
    GreenfootImage voiceOn;
    GreenfootImage voiceClick;
    public Sound_ctrl()
    {
        liber = true;
        voiceOn = new GreenfootImage("voiceon.png");
        voiceClick = new GreenfootImage("voiceclick.png");
    }

    public void act() 
    {
         
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(liber)
            if (mouse != null)
            {  
                setImage(voiceOn);  
                List<Sound_ctrl> objects = getWorld().getObjectsAt(mouse.getX(), mouse.getY(),Sound_ctrl.class);  
                for (Object object : objects)  
                {  
                    if (object == this)  
                    { 
                        setImage(voiceClick);
                    } 
                }  
            }
        if(Greenfoot.mouseClicked(this))
        {
            
        }
    }    
}
