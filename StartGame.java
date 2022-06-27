import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * clasa responsabila de initierea lumii unde se va desfasura jocul
 */
public class StartGame extends buttons {
    /**
     * constructorul clasei cand se cunosc cele doua imagini ale butonului: de idle
     * si hover
     * functioneaza prin apelarea constructorului superclasei
     * 
     * @param normal     imaginea starii de idle
     * @param hoverImage imaginea starii de hover
     */
    public StartGame(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
    }

    public void act() {
        super.act();
        if (Greenfoot.mouseClicked(this)) {
            if (ChangeLine.lineSize > ChangeSize.size) {
                ChangeLine.lineSize = ChangeSize.size;
            }
            if (ChangeSize.size > 10)
                Oponent.pOrC = false;
            Greenfoot.setWorld(new Lvl10X10(ChangeSize.size, ChangeLine.lineSize, Oponent.pOrC));
            // Greenfoot.setWorld(new Lvl10X10());
        }
    }
}
