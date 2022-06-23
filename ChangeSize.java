import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * slider pentru selectarea dimensiunii tablei de joc
 */
public class ChangeSize extends buttons {
    // valoare ce va fi pasata mai departe nivelului de joc
    public static int size = 80;

    /**
     * constructorul clasei cand se stiu cele doua imaginii ale butonului: idle si
     * hover
     * functioneaza prin apelarea constructorului super clasei
     * 
     * @param normal     imaginea starii de idel
     * @param hoverImage imaginea starii de hover
     */
    public ChangeSize(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
        value = size;
    }

    /**
     * transforma coordonatele din lume intr-o valoarea intreaga ce va fi afisata
     * prin intermediul clasei {@link ShowText}
     * 
     * @param x coordonatele din lume
     * @return
     */
    private int function(int x) {
        return (int) Math.round((x - 235.188) / 1.271);
    }

    public void act() {
        super.act();
        value = function(getX());
        child.updateImage(value);
        if (Greenfoot.mouseDragEnded(this)) {
            size = value;
        }
    }
}
