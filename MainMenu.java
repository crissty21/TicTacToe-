import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//biblioteci folosite pentru crearea fontului
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * lumea meniului principal
 * aceasta se ocupa cu setarea valorilor initiale ale jocului 
 * si de pornirea acestuia
 */
public class MainMenu extends World
{
    //fontul textului din joc
    public static greenfoot.Font BurstFont;
    
    //text ce pulseaze care te indruma sa apesi click pentru a incepe
    private decorations pressClick;
    //semafor
    private boolean once;
    //butonul de startButton
    private buttons startButton;
    //butonul de exitButton
    private buttons exitButton;

    /**
     * default constructor 
     */
    public MainMenu() {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1);
        
        once = true;
        //logoul jocului
        //acesta se va spawna invizibil, se va misca in sus si va avea un efect de fade in
        decorations logo = new decorations(new GreenfootImage("images\\logo_burst.png"));
        logo.goInvisible();
        logo.moveFromTo(new coordonates(300, 200), new coordonates(300, 100), 1);
        logo.fadeIn(4);
        addObject(logo, 300, 200);

        //textul "prees click to continue"
        //va avea un efect de pulse
        pressClick = new decorations(new GreenfootImage("images\\press_click.png"));
        pressClick.pulse(5);
        addObject(pressClick, 300, 300);

        //copyright claims
        addObject(new decorations(new GreenfootImage("images\\bb_copyright.png")), 300, 375);
        addObject(new decorations(new GreenfootImage("images\\cc_copyright.png")), 300, 385);

        //butoanele de startButton si exitButton
        //acestea nu sunt inca adaugate in lume 
        startButton = new StartButton(new GreenfootImage("images\\start_idle.png"),
                new GreenfootImage("images\\start_select.png"));
        exitButton = new ExitButton(new GreenfootImage("images\\exit_idle.png"),
                new GreenfootImage("images\\exit_select.png"));

        setPaintOrder(decorations.class, buttons.class);
        createFont();
    }

    /**
     * act() se ocupa de animatii
     */
    public void act() {
        if (Greenfoot.mouseClicked(null)) {
            if (once) {
                once = false;
                //adaugam butoanele de startButton si exitButton
                //acestea se vor adauga in spatele logoului si vor avea un efect de coborare
                addObject(startButton, 300, 100);
                startButton.moveFromTo(new coordonates(300, 100), new coordonates(300, 220), 5);
                addObject(exitButton, 300, 100);
                exitButton.moveFromTo(new coordonates(300, 100), new coordonates(300, 260), 5);

                //eliminam textul "press click to continue"
                removeObject(pressClick);
            }
        }
    }
    /**
     * functie ce creaza fontul utilizand un fisier True Type Font
     */
    private void createFont() {
        
        File f = new File("Burst_numeric_little-Regular.ttf");
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
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

    }
}
