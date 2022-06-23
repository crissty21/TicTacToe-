import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Clasa responsabila de afisarea de text
 */
public class ShowText extends decorations {
    // imaginea obiectului
    private GreenfootImage img;
    // legatura cu obiectul din care va lua textul ce va fi afisat
    private decorations parent;
    // permite miscarea obiectului cu parintele
    private boolean moveWithParrent;

    /**
     * constructorul clasei cand se stie doar parintele si valoare initiala
     * 
     * @param x       //valoarea initiala ce va fi afisata la crearea obiectului
     * @param _parent //parintele obiectului
     */
    public ShowText(int x, decorations _parent) {
        // cream o imagine noua cu textul
        img = new GreenfootImage(26, 15);
        img.setColor(new Color(103, 38, 032));
        img.setFont(MainMenu.BurstFont);
        updateImage(x);

        parent = _parent;
        moveWithParrent = false;
        buttons parentAsCL = (buttons) parent;
        parentAsCL.setChild(this);

    }

    /**
     * constructorul clasei cand se stie valoare initiala, parintele si daca se va
     * misca sau nu cu acesta
     * 
     * @param x       valoarea initiala
     * @param _parent parintele
     * @param mwp     true = Move With Parent / false = stay still
     */
    public ShowText(int x, decorations _parent, boolean mwp) {
        this(x, _parent);
        moveWithParrent = mwp;
    }

    /**
     * constructorul clasei cand se stie valoarea initiala, culoarea textului,
     * dimensiunea acestuia, parintele, si daca se va misca sau nu obiectul cu
     * acesta
     * 
     * @param x         valoarea initiala
     * @param _parent   parintele
     * @param mwp       true = Move With Parent / false = stay still
     * @param textColor culoarea textului
     * @param fontDim   dimensiunea textului
     */
    public ShowText(int x, decorations _parent, boolean mwp, Color textColor, int fontDim) {
        img = new GreenfootImage(26, 15);
        img.setColor(textColor);
        img.setFont(MainMenu.BurstFont.deriveFont(fontDim));
        updateImage(x);
        parent = _parent;
        moveWithParrent = mwp;
        moveWithParrent = false;
        buttons parentAsCL = (buttons) parent;
        parentAsCL.setChild(this);

    }

    /**
     * updatateaza imaginea obiectului, astfel incat aceasta va arata valoare
     * parametrului x
     * 
     * @param x noua valoare de afisat
     */
    public void updateImage(int x) {
        String text;
        if (x < 10) {
            text = "0" + Integer.toUnsignedString(x);
        } else {
            text = Integer.toUnsignedString(x);
        }
        img.clear();
        img.drawString(text, 0, 16);
        setImage(img);
    }
    /**
     * act() - este responsabil de miscarea obiectului cu parintele, daca este permis
     */
    public void act() {
        if (moveWithParrent) {
            setLocation(parent.getX(), getY());
        }
    }
}
