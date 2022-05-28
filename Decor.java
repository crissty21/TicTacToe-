import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class decor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Decor extends Actor

{
    GreenfootImage[] img = new GreenfootImage[1];
    public Decor(int a)
    {
        switch(a)
        {
            case 1:
            {
                img[0] = new GreenfootImage("roata.png");
                break;
            }
            case 2://logo
            {
                img[0] = new GreenfootImage("logo.png");
                img[0].scale((int)(img[0].getWidth()/1.1),(int)(img[0].getHeight()/1.1));
                break;
            }
            
        }
        setImage(img[0]);
    }   
}
