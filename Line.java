import java.util.Scanner;

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

    public static float resizeImgs(float raport)
    {
        int newWidth, newHeight;
        for(GreenfootImage iter : linie)
        {
            newWidth = (int)(iter.getWidth()/raport);
            newHeight = (int)(iter.getHeight()/raport);
            if(newWidth == 0)newWidth = 1;
            if(newHeight == 0 )newHeight = 1;
            iter.scale(newWidth, newHeight);
        }
        return raport;
    }
    public static void initImgs()
    {
        linie[0] = new GreenfootImage("linie_1.png");
        linie[1] = new GreenfootImage("linie_2.png");
        linie[2] = new GreenfootImage("linie_3.png");
        linie[3] = new GreenfootImage("linie_4.png");
    }
}
