import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MainMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainMenu extends World
{
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
}
