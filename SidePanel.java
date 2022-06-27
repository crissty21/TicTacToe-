import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * primul pas in pornirea easter egg-ului
 * creaza butoanele responsabile de cod
 * porneste animatia in cazul in care butoanele sunt in ordinea corecta
 */
public class SidePanel extends buttons {
    private boolean doSuper;
    private decorations secreteBG = new decorations(new GreenfootImage(1, 1));
    private decorations bau = new decorations(new GreenfootImage(1, 1));
    private boolean animOn = false;
    private int contor = 0;
    private int animSpeed = 15;

    private Symbols[] simboluri = new Symbols[3];
    private GreenfootImage[] easterEgg = {
            new GreenfootImage("images\\secret_ending_1.png"),
            new GreenfootImage("images\\secret_ending_2.png"),
            new GreenfootImage("images\\secret_ending_3.png"),
            new GreenfootImage("images\\secret_ending_4.png"),
            new GreenfootImage("images\\secret_ending_5.png"),
            new GreenfootImage("images\\secret_ending_6.png"),
            new GreenfootImage("images\\secret_ending_7.png"),
            new GreenfootImage("images\\secret_ending_8.png"),
            new GreenfootImage("images\\secret_ending_9.png"),
            new GreenfootImage("images\\secret_ending_10.png"),
            new GreenfootImage("images\\secret_ending_11.png"),
            new GreenfootImage("images\\secret_ending_12.png"),
            new GreenfootImage("images\\secret_ending_13.png"),
            new GreenfootImage("images\\secret_ending_14.png"),
            new GreenfootImage("images\\secret_ending_15.png"),
            new GreenfootImage("images\\secret_ending_16.png"),
            new GreenfootImage("images\\secret_ending_17.png"),
            new GreenfootImage("images\\secret_ending_18.png"),
    };

    public SidePanel(GreenfootImage idle, GreenfootImage selected) {
        super(idle, selected);
        doSuper = true;
        simboluri[0] = new Symbols(5);
        simboluri[1] = new Symbols(3);
        simboluri[2] = new Symbols(0);
    }

    public void act() {

        if (doSuper) {

            super.act();
        }
        if (Greenfoot.mouseClicked(this) && doSuper) {
            doSuper = false;
            decorations backLight = new decorations(new GreenfootImage("images\\computer_light.png"));
            getWorld().addObject(backLight, 875, 645);
            backLight.pulseFor(20, 5);
            getWorld().addObject(new decorations(new GreenfootImage("images\\computer_stripes.png")), 875, 645);
            resetimage();
            getWorld().addObject(simboluri[0], 875, 615);
            getWorld().addObject(simboluri[1], 875, 645);
            getWorld().addObject(simboluri[2], 875, 675);
        }
        if (simboluri[0].tip == 1 && simboluri[1].tip == 2 && simboluri[2].tip == 3 && animOn == false) {
            getWorld().setPaintOrder(decorations.class);
            secreteBG = new decorations(new GreenfootImage("images\\secret_background.png"));
            secreteBG.goInvisible();
            getWorld().addObject(secreteBG, 450, 350);
            // secreteBG.fadeIn(5);
            getWorld().addObject(bau, 450, 350);
            animOn = true;
            Brain.gameState = State.ended;
        }
        if (animOn) {
            contor++;
            if (contor > 50 && contor / animSpeed - ((int) 50 / animSpeed) < easterEgg.length) {
                if (contor % animSpeed == 0)
                    bau.setImage(easterEgg[contor / animSpeed - ((int) 50 / animSpeed)]);

            }
            if (contor / animSpeed - ((int) 50 / animSpeed) == easterEgg.length + 3) {
                bau.setMyImage(new GreenfootImage("images\\secret_writing.png"));
                bau.goInvisible();
                secreteBG.getImage().setTransparency(255);
                bau.fadeIn(10);

            }
            if (contor / animSpeed - ((int) 50 / animSpeed) > easterEgg.length + 25) {
                getWorld().removeObject(bau);
                getWorld().removeObject(secreteBG);
                animOn = false;
                Greenfoot.setWorld(new MainMenu());
            }

        }

    }
}
