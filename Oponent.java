import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * ii permite jucatorului sa selecteze daca va juca contra altui jucator sau
 * contra calculatorului
 */
public class Oponent extends buttons {
    public static boolean pOrC = false;
    private boolean myType;

    /**
     * constructor cand se stiu imaginile celor doua stari: de idle si hover
     * si tipul butonului
     * 
     * @param normal     imaginea starii de idle
     * @param hoverImage imaginea starii de hover
     * @param _pORc      true = se va juca contra ai / false = se va juca contra alt
     *                   jucator
     */
    public Oponent(GreenfootImage normal, GreenfootImage hoverImage, boolean _pORc) {
        super(normal, hoverImage);
        myType = _pORc;
        if (myType == pOrC) {
            setImage(hoverImage);
        }
    }

    public void act() {
        if (pOrC != myType) {
            super.act();
            if (Greenfoot.mouseClicked(this)) {
                pOrC = myType;
            }
        }
    }
}
