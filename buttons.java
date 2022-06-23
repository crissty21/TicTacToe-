import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * clasa abstracta ce implementeaza functii pentru butoane
 */
public abstract class buttons extends decorations {
    /**
     * butoanele vor avea cel putin 2 stari: idle si hover
     * imaginea starii de idel va vi setata prin constructorul clasei decoratiunii 
     */
    //imaginea starii de hover
    private GreenfootImage hoverImage;
    //permite miscarea butonului de la stanga la dreapta
    //intre valorile specificate in variabilele limitd si limits
    private boolean moveLR;
    private int limits, limitd;
    //folosita pentru retinerea si transmiterea de valori specifice 
    public int value;
    //in cazul in care butonul va avea text afisat, acesta va fi retinut in variabila child
    protected ShowText child;


    public buttons() {}
    
    /**
     * constructorul clasei buttons
     * @param normal imaginea butonului in starea idle
     * @param _hoverImage imaginea butonului in stare de hover
     */
    public buttons(GreenfootImage normal, GreenfootImage _hoverImage) {
        super(normal);
        hoverImage = _hoverImage;
        moveLR = false;
    }
    /**
     * leaga un obiect al clasei {@link ShowText} de obiectul curent
     * acest obiect va fi responsabil de afisarea valori retinute in variabila value
     * @param kid obiectul clasei ShowText
     */
    public void setChild(ShowText kid)
    {
        child = kid;
    }

    /**
     * permite butonului sa se miste stanga dreapta, intre doua limite
     * @param _limits limita stanga
     * @param _limitd limita dreapta
     */
    public void allowMoveLR(int _limits, int _limitd) {
        moveLR = true;
        limits = _limits;
        limitd = _limitd;
    }

    /**
     * act() este responsabil de miscarea butonului
     * de rularea animatiilor din clasa {@link decorations} 
     */
    public void act() {
        super.act();
        if (Greenfoot.mouseMoved(this)) {
            setImage(hoverImage);
        }
        if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) {
            setImage(image);
        }

        if (Greenfoot.mouseDragged(this)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (moveLR) {
                if (mouse.getX() >= limits && mouse.getX() <= limitd) {
                    setLocation(mouse.getX(), getY());
                } else {
                    if (mouse.getX() < limits)
                        setLocation(limits, getY());
                    else
                        setLocation(limitd, getY());
                }
            }
        }
    }
}
