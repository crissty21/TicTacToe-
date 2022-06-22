import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Write a description of class MainMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainMenu extends World
{
    public static greenfoot.Font BurstFont;
    
    private decorations pressClick;
    private boolean once;
    private buttons start;
    private buttons exit;

    public MainMenu() {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1);
        once = true;
        decorations logo = new decorations(new GreenfootImage("images\\logo_burst.png"));
        logo.goInvisible();
        logo.moveFromTo(new coordonates(300, 200), new coordonates(300, 100), 3);
        logo.fadeIn(4);
        addObject(logo, 300, 200);

        pressClick = new decorations(new GreenfootImage("images\\press_click.png"));
        pressClick.pulse(5);
        addObject(pressClick, 300, 300);

        addObject(new decorations(new GreenfootImage("images\\bb_copyright.png")), 300, 375);
        addObject(new decorations(new GreenfootImage("images\\cc_copyright.png")), 300, 385);

        start = new StartButton(new GreenfootImage("images\\start_idle.png"),
                new GreenfootImage("images\\start_select.png"));
        exit = new ExitButton(new GreenfootImage("images\\exit_idle.png"),
                new GreenfootImage("images\\exit_select.png"));

        setPaintOrder(decorations.class, buttons.class);
        createFont();
    }

    public void act() {
        if (Greenfoot.mouseClicked(null)) {
            if (once) {
                once = false;
                removeObject(pressClick);
                addObject(start, 300, 100);
                start.moveFromTo(new coordonates(300, 100), new coordonates(300, 220), 5);
                addObject(exit, 300, 100);
                exit.moveFromTo(new coordonates(300, 100), new coordonates(300, 260), 5);
            }
        }
    }
    private void createFont() {

        File f = new File("C:\\Users\\Cristi\\java proj\\greenfoot\\Burst_numeric_little-Regular.ttf");
        try {
            FileInputStream in = new FileInputStream(f);
            Font dynamicFont, dynamicFont32;

            dynamicFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(f));
            dynamicFont32 = dynamicFont.deriveFont(24f);

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
