import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Lvl10X10 extends World {

    //public Lvl10X10(int gridSize, int linesToWin, boolean againstAi) {
        public Lvl10X10() {
        // se plaseaza obiectele in functie de raportul din clasa variabile
        super(900, 700, 1);
        int gridSize = 10; int linesToWin=5; boolean againstAi = false;
        Element.initImgs();
        Line.initImgs();
        Gun refToGun = new Gun();
        addObject(new Cocos(refToGun), 48, 554);
        addObject(refToGun, 90, 610);
        addObject(new Brain(gridSize, linesToWin, refToGun, againstAi), 580, 290);
        addObject(new Next(), 530, 630);
        addObject(new Decor(1), 90, 644);
        setBackground(new GreenfootImage("background 2.png"));
        
        setPaintOrder(Decor.class, Gun.class, Bullet.class, Line.class, Element.class, pointer.class);
        if(gridSize>20)
        {
            addObject(new decorations(new GreenfootImage("images\\magnifier.png")), 130, 150);
        }
    }

}
