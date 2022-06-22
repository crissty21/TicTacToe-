import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ShowText here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class ShowText extends decorations {
    GreenfootImage img;

    public ShowText() {
        img = new GreenfootImage(26, 15);
        img.setColor(Color.RED);
        img.setFont(MainMenu.BurstFont);
        
        updateImage(3);
    }

    public void updateImage(int x) {
        String text;
        if (x < 10) {
            text = "0" + Integer.toUnsignedString(x);
        } else {
            text = Integer.toUnsignedString(x);
        }
        img.drawString(text, 0, 16);
        setImage(img);
    }

    public void act() {
        // Add your action code here.
    }
}
