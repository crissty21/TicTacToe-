import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class Finalitate extends Actor
{
    public static boolean liber;
    public static boolean remiza;
    static int timer;
    int world ;
    
    public Finalitate(int a)
    {
        setImage(new GreenfootImage("blue-draught.png"));
        timer = 0;
        world = a;
        liber = false;
        remiza = false;
    }
    public void act() 
    {
        if(remiza)//folosit la colectarea de date
        {
            remiza = false;
            liber = true;
        }
        
    }    
}
