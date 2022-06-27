import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * butoanele cu care se va selecta codul ce va porni animatia easter egg-ului 
 */
public class Symbols extends buttons {
    public int tip;
    private static GreenfootImage[] images = {
            new GreenfootImage("images\\symbol_1.png"),
            new GreenfootImage("images\\symbol_2.png"),
            new GreenfootImage("images\\symbol_3.png"),
            new GreenfootImage("images\\symbol_4.png"),
            new GreenfootImage("images\\symbol_5.png"),
            new GreenfootImage("images\\symbol_6.png"),
            new GreenfootImage("images\\symbol_7.png"),
            new GreenfootImage("images\\symbol_8.png"),
            new GreenfootImage("images\\symbol_9.png")
    };

    public Symbols(int type) {
        setImage(images[type]);
        tip = type;
    }

    public void act() {
        if (Brain.gameState != State.ended) {
            if (Greenfoot.mouseClicked(this)) {
                tip++;
                if (tip > 8)
                    tip = 0;
                setImage(images[tip]);
            }
        }
    }
}
