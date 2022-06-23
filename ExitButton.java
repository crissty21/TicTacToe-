import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * clasa responsabila de inchiderea ferestrei cu setari
 */
public class ExitButton extends buttons {
    /**
     * constructorul clasei cand se cunosc cele doua imagini ale butonului: de idle
     * si hover
     * functioneaza prin apelarea constructorului superclasei 
     * @param normal     imaginea starii de idle
     * @param hoverImage imaginea starii de hover
     */
    public ExitButton(GreenfootImage normal, GreenfootImage hoverImage) {
        super(normal, hoverImage);
    }
    
    public void act() {
        super.act();
        if (Greenfoot.mouseClicked(this) == true) {
            setImage(image);
            Greenfoot.stop();
        }
    }
}
