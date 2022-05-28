import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * afiseaza care este urmatorul jucator
 */
public class Next extends Actor
{
    public static boolean start, start1;
    public static int ct;
    static int a;
    
    GreenfootImage[] np = new GreenfootImage[5];
    public Next(int z) 
    {
        np[0] = new GreenfootImage("light.png");
            np[1] = new GreenfootImage("light1.png");
            np[2] = new GreenfootImage("light2.png");
            np[3] = new GreenfootImage("light3.png");
            np[4] = new GreenfootImage("light4.png");
        start = start1 = false;
        ct = 0;
        setImage(np[0]);
    }

    public void act()
    {
        if (start) {
            ct++;
            if (ct % 5 == 0)
                if (Brain.next == true)
                    setImage(np[ct / 5]);
                else
                    setImage(np[4 - (ct / 5)]);
            if (ct == 20) {
                ct = 0;
                start = false;
            }
        }
        if (start1) {
            if (Brain.next == true)
                setImage(np[0]);
            else
                setImage(np[4]);
            start1 = false;
        }
    }
}
