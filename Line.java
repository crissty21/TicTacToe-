import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * linia ce vine trasata la final
 */
public class Line extends Actor
{
    int orientare;
    
    public static GreenfootImage[] linie = {
        new GreenfootImage("linie_1.png"),
        new GreenfootImage("linie_2.png"),
        new GreenfootImage("linie_3.png"),
        new GreenfootImage("linie_4.png")
    };

    public Line(int ori)
    {
        setImage(linie[ori]);
    }
}
