import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Lvl10X10 extends World {

    public static greenfoot.Font BurstFont;

    public Lvl10X10(int gridSize, int linesToWin) {
        // se plaseaza obiectele in functie de raportul din clasa variabile
        super(900, 700, 1);

        Element.initImgs();
        Line.initImgs();
        Gun refToGun = new Gun();
        addObject(new Cocos(refToGun), 48, 554);
        addObject(refToGun, 90, 610);
        addObject(new Brain(gridSize, linesToWin, refToGun), 580, 290);
        addObject(new Next(), 530, 630);
        addObject(new Decor(1), 90, 644);
        setBackground(new GreenfootImage("background 2.png"));
        addObject(new Decor(2), 130, 150);
        setPaintOrder(Decor.class, Gun.class, Bullet.class, Element.class, pointer.class);
        createFont();

    }

    private void createFont() {

        File f = new File("C:\\Users\\Cristi\\java proj\\greenfoot\\Burst_numeric_little-Regular.ttf");
        try {
            FileInputStream in = new FileInputStream(f);
            Font dynamicFont, dynamicFont32;

            dynamicFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(f));
            dynamicFont32 = dynamicFont.deriveFont(32f);

            java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(dynamicFont32);
            BurstFont = new greenfoot.Font(dynamicFont32.getName(), dynamicFont32.getStyle() % 2 == 1,
                    dynamicFont32.getStyle() / 2 == 1, dynamicFont32.getSize());
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FontFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
