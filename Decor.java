import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//elemente de decor
public class Decor extends Actor {
    GreenfootImage[] img = {
            new GreenfootImage("logo.png"),
            new GreenfootImage("roata.png")
    };

    public Decor(int a) {
        setImage(img[a]);
    }
}
