import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//numele clasei vine de la numele lui Bogdan Biro
//nu ma judeca =]]

public class Biro extends Actor
{
    public static boolean liber;
    int ct;
    boolean once;
    public static GreenfootSound cucurigu = new GreenfootSound("cucurigu.wav");

    public static GreenfootImage[] pui = new GreenfootImage[7];
    public Biro()
    {

        pui[0] = new GreenfootImage("cocos1.png");
        pui[1] = new GreenfootImage("cocos2.png");
        pui[2] = new GreenfootImage("cocos3.png");
        pui[3] = new GreenfootImage("cocos4.png");
        pui[4] = new GreenfootImage("cocos5.png");
        pui[5] = new GreenfootImage("cocos6.png");
        pui[6] = new GreenfootImage("cocos7.png");
        liber = true;
        once = true;
        ct=0;
        setImage(pui[0]);
    }

    public void act() 
    {
        if(liber)
        {
            ct++;
            if(ct%10==0)
                setImage(pui[ct/10]);
            if(ct==7)
            {
                //cucurigu.play();
                cucurigu.setVolume(50);
            }
            if(ct==60)
            {
                setImage(pui[0]);
                ct=0;
                liber = false;                
            }
        }
    }    
}
