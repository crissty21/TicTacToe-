import java.util.ArrayList;
import java.util.List;

import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartButton here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class StartButton extends buttons {
    private BackX back;
    private List<decorations> objsToBeRemoved;

    public StartButton(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
        objsToBeRemoved = new ArrayList<>();
    }

    private int function1(int x) {
        return (int) (1.271 * x + 235.188);
    }

    private int function2(int x) {
        return (int) (7.176 * x + 205.471);
    }
    private int offset = 27;
    public void act() {
        super.act();
        if (Greenfoot.mouseClicked(this) == true) {
            getWorld().setPaintOrder(decorations.class);
            decorations blackBack = new decorations(new GreenfootImage("images\\mask.png"));
            getWorld().addObject(blackBack, 300, 200);
            blackBack.fadeIn(5);
            objsToBeRemoved.add(blackBack);
            decorations setting = new decorations(new GreenfootImage("images\\settings_screen.png"));
            getWorld().addObject(setting, 300, 240);
            objsToBeRemoved.add(setting);
            decorations changeSize = new decorations(new GreenfootImage("images\\size_change.png"));
            getWorld().addObject(changeSize, 265, 340);
            objsToBeRemoved.add(changeSize);
            ChangeSize adjChangeSize = new ChangeSize(new GreenfootImage("images\\progress_pointer_idle.png"),
                    new GreenfootImage("images\\progress_pointer_select.png"));
            getWorld().addObject(adjChangeSize, 204, 282);
            objsToBeRemoved.add(adjChangeSize);
            adjChangeSize.allowMoveLR(239, 361);
            adjChangeSize.moveFromTo(new coordonates(239, 282), new coordonates(function1(ChangeSize.size), 282), 3);
            ChangeLine adjSettings = new ChangeLine(new GreenfootImage("images\\progress_pointer_idle.png"),
                    new GreenfootImage("images\\progress_pointer_select.png"));
            getWorld().addObject(adjSettings, 192, 350);
            objsToBeRemoved.add(adjSettings);
            adjSettings.allowMoveLR(192, 314);
            adjSettings.moveFromTo(new coordonates(227, 350), new coordonates(function2(ChangeLine.lineSize), 350), 3);
            back = new BackX(new GreenfootImage("images\\back_idle.png"),
                    new GreenfootImage("images\\back_select.png"));
            getWorld().addObject(back, 200, 220);
            objsToBeRemoved.add(back);

            StartGame start = new StartGame(new GreenfootImage("images\\done_idle.png"),
                    new GreenfootImage("images\\done_select.png"));
            getWorld().addObject(start, 390, 335);
            objsToBeRemoved.add(start);

            ShowText firShowText = new ShowText(ChangeLine.lineSize, adjSettings, false, new Color(12, 12, 13),19);
            getWorld().addObject(firShowText,345,346);

          
            firShowText = new ShowText(ChangeLine.lineSize, adjChangeSize, true);
            getWorld().addObject(firShowText,adjChangeSize.getX(),adjChangeSize.getY() - 27);


        }
        if (back != null) {
            if (Greenfoot.mouseClicked(back)) {
                getWorld().removeObjects(objsToBeRemoved);
            }
        }
    }
}
