import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * lumea pentru modul de joc 10 x 10
 */
public class Lvl10X10 extends World
{
    public Lvl10X10()
    {    
        //se plaseaza obiectele in functie de raportul din clasa variabile
        super(900,700, 1);  
        
        Element.initImgs();
        Line.initImgs();
        Gun refToGun = new Gun();
        addObject(new Cocos(refToGun),48,554);
        addObject(refToGun,90,610);
        addObject(new Brain(10,4,refToGun),580,290);
        addObject(new Next(1),530,630);
        addObject(new Decor(1),90,644);
        setBackground(new GreenfootImage("background 2.png"));
        
        setPaintOrder(Decor.class, Gun.class, Bullet.class);
    }
}
