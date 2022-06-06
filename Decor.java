import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//elemente de decor
public class Decor extends Actor {
    GreenfootImage[] img = {
            new GreenfootImage("logo.png"),
            new GreenfootImage("roata.png"),
            new GreenfootImage("lupa.png")
    };

    public Decor(int a) {
        img[2].setTransparency(100);
        setImage(img[a]);
    }
}
