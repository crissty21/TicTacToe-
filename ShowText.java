import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import javafx.collections.ListChangeListener.Change;

/**
 * Write a description of class ShowText here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class ShowText extends decorations {
    GreenfootImage img;
    decorations parent;
    private boolean moveWithParrent;

    public ShowText(int x, decorations _parent) {
        img = new GreenfootImage(26, 15);
        img.setColor(new Color(103, 38, 032));
        img.setFont(MainMenu.BurstFont);
        updateImage(x);
        parent = _parent;
        moveWithParrent = false;
        ChangeLine parentAsCL = (ChangeLine) parent;
        if (parentAsCL != null) {
            parentAsCL.setChild(this);
        }
    }

    public ShowText(int x, decorations _parent, boolean mwp) {
        this(x, _parent);
        moveWithParrent = mwp;
    }

    public ShowText(int x, decorations _parent, boolean mwp, Color textColor, int fontDim) {
        img = new GreenfootImage(26, 15);
        img.setColor(textColor);
        img.setFont(MainMenu.BurstFont.deriveFont(fontDim));
        updateImage(x);
        parent = _parent;
        moveWithParrent = mwp;
        moveWithParrent = false;
        ChangeLine parentAsCL = (ChangeLine) parent;
        if (parentAsCL != null) {
            parentAsCL.setChild(this);
        }

    }

    public void updateImage(int x) {
        String text;
        if (x < 10) {
            text = "0" + Integer.toUnsignedString(x);
        } else {
            text = Integer.toUnsignedString(x);
        }
        img.clear();
        img.drawString(text, 0, 16);
        setImage(img);
    }

    public void act() {

    }
}
