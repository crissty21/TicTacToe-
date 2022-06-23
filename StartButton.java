import java.util.ArrayList;
import java.util.List;

import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * butonul ce porneste jocul
 */
public class StartButton extends buttons {
    /**
     * aceasta clasa va afisa meniul de setari
     */
    //back este un obiect din coltul clasei, care apasat, va inchide fereastra de setari 
    private BackX back;
    //obiectele din fereastra, ce vor fi sterse in cazul apasarii butonului back
    private List<decorations> objsToBeRemoved;
 

    /**
     * constructorul clasei cand se stiu imaginiile starii de idel si starii de hover
     * @param normal    imaginea starii de idel
     * @param hoverImage    imaginea starii de hover
     */
    public StartButton(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
        objsToBeRemoved = new ArrayList<>();
    }

    /**
     * transforma variabila intreaga x, in coordonate in lume
     */
    private int function1(int x) {
        return (int) (1.271 * x + 235.188);
    }
    private int function2(int x) {
        return (int) (7.176 * x + 205.471);
    }
    
    public void act() {
        super.act();
        if (Greenfoot.mouseClicked(this) == true) {
            getWorld().setPaintOrder(decorations.class);
            //fundalul negru semitrasparent
            decorations blackBack = new decorations(new GreenfootImage("images\\mask.png"));
            getWorld().addObject(blackBack, 300, 200);
            blackBack.fadeIn(5);
            objsToBeRemoved.add(blackBack);

            //backgroundul ferestrei de setari
            decorations setting = new decorations(new GreenfootImage("images\\settings_screen.png"));
            getWorld().addObject(setting, 300, 200);
            objsToBeRemoved.add(setting);
            //backgroundul ferestrei responsabile de setarea dimensiunii liniei de win
            decorations changeLineSizeBg = new decorations(new GreenfootImage("images\\size_change.png"));
            getWorld().addObject(changeLineSizeBg, 265, 300);
            objsToBeRemoved.add(changeLineSizeBg);

            //butonul ce modifica dimensiunea tablei de joc 
            ChangeSize adjGridSize = new ChangeSize(new GreenfootImage("images\\progress_pointer_idle.png"),
                    new GreenfootImage("images\\progress_pointer_select.png"));
            getWorld().addObject(adjGridSize, 204, 242);
            objsToBeRemoved.add(adjGridSize);
            adjGridSize.allowMoveLR(239, 361);
            adjGridSize.moveFromTo(new coordonates(239, 242), new coordonates(function1(ChangeSize.size), 242), 3);

            // butonul ce modifica dimensiunea linie cu care se castiga
            ChangeLine adjLineSize = new ChangeLine(new GreenfootImage("images\\progress_pointer_idle.png"),
                    new GreenfootImage("images\\progress_pointer_select.png"));
            getWorld().addObject(adjLineSize, 192, 310);
            objsToBeRemoved.add(adjLineSize);
            adjLineSize.allowMoveLR(192, 314);
            adjLineSize.moveFromTo(new coordonates(227, 310), new coordonates(function2(ChangeLine.lineSize), 310), 3);
            
            //butonul ce inchide fereastra 
            back = new BackX(new GreenfootImage("images\\back_idle.png"),
                    new GreenfootImage("images\\back_select.png"));
            getWorld().addObject(back, 200, 180);
            objsToBeRemoved.add(back);

            //butonul de done, ce simbolizeaza salvarea setarilor si inceperea jocului
            StartGame start = new StartGame(new GreenfootImage("images\\done_idle.png"),
                    new GreenfootImage("images\\done_select.png"));
            getWorld().addObject(start, 390, 295);
            objsToBeRemoved.add(start);

            //scrisul ce arata dimensiunea liniei 
            ShowText showText = new ShowText(ChangeLine.lineSize, adjLineSize, false, new Color(12, 12, 13),19);
            getWorld().addObject(showText,345,306);

            //scrisul ce arata dimensiunea liniei de castig
            showText = new ShowText(ChangeLine.lineSize, adjGridSize, true);
            getWorld().addObject(showText,adjGridSize.getX(),adjGridSize.getY() - 27);

            //butoanele din care selectezi contra cui joci 
            getWorld().addObject(new Oponent(new GreenfootImage("images\\player_machine_idle.png"), new GreenfootImage("images\\player_machine_select.png"), true), 230, 340);
            getWorld().addObject(new Oponent(new GreenfootImage("images\\player_player_idle.png"), new GreenfootImage("images\\player_player_select.png"), false), 380, 340);

        }
        //apasarea butonului de back
        if (back != null) {
            if (Greenfoot.mouseClicked(back)) {
                getWorld().removeObjects(objsToBeRemoved);
            }
        }
    }
}
