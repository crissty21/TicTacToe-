import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * clasa lumii jocului
 * aici se va desfasura nivelul principal
 */
public class Lvl10X10 extends World {

    public Lvl10X10(int gridSize, int linesToWin, boolean againstAi) {
        
        super(900, 700, 1);
        Element.initImgs();
        Line.initImgs();
        Cocos biro = new Cocos();
        addObject(new Brain(gridSize, linesToWin, againstAi, biro), 591, 303);
        addObject(biro, 167, 460);
        biro.moveFromTo(new coordonates(1, 460), new coordonates(167, 460), 3);
        addObject(new Next(), 779, 647);
        setBackground(new GreenfootImage("images\\bakground_plate.png"));

        addObject(new decorations(new GreenfootImage("images\\foreground_plate.png")), 450, 350);
        addObject(new decorations(new GreenfootImage("images\\change_backboard.png")), 780, 645);
        addObject(new decorations(new GreenfootImage("images\\magnifier.png")), 210, 99);
        addObject(new ReturnMenu(new GreenfootImage("images\\return_idle.png"),
                new GreenfootImage("images\\return_select.png")), 65, 680);
        addObject(new SidePanel(new GreenfootImage("images\\side_panel_idle.png"),
                new GreenfootImage("images\\side_panel_select.png")), 873, 645);
        addObject(new decorations(new GreenfootImage("images\\char_name.png")), 218, 668);
        setPaintOrder(Symbols.class, Line.class, Next.class, Element.class, pointer.class, decorations.class);
    }

}
