import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * slider ce modifica dimensiunea liniei de win
 */
public class ChangeLine extends buttons {
    // valoarea ce va fi pasata mai departe
    public static int lineSize = 14;

    /**
     * constructor ce va seta imaginiile de idle si hover prin apelarea
     * constructorului superclasei
     * 
     * @param normal     imaginea de idle
     * @param hoverImage imaginea de hover
     */
    public ChangeLine(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
        value = lineSize;
    }
    /**
     * transforma coordonatele din lume intr-o valoarea intreaga ce va fi afisata
     * prin intermediul clasei {@link ShowText}
     * 
     * @param x coordonatele din lume
     * @return
     */
    private int function(int x) {
        return (int) Math.round((x - 175.471) / 7.176) + 1;
    }

    public void act() {
        super.act();
        value = function(getX());
        if (Greenfoot.mouseDragEnded(this)) {
            lineSize = value;
        }
        if (child != null) {
            child.updateImage(value);
        }
    }

}
