import greenfoot.*;

/**
 * clasa de baza a decoratiunilor din MainMenu
 */
public class decorations extends Actor {
    //variabila pentru animatia de fadeIn
    //true = start \ false = stop
    private boolean bFadeIn;
    //variabila pentru animatia de fadeOut
    //true = start \ false = stop
    private boolean bFadeOut;
    //variabila pentru animatia de pulse
    //true = start \ false = stop
    private boolean bPulse;
    //variabila pentru viteza de fade
    private int fadeSpeed;
    //variabila pentru transparenta curenta a imaginii
    //este folosita la animatiile de fade 
    private int fadeIndex;
    
    //variabila pentru pornirea animatiei de moveFromTo
    private boolean bMove;
    //doua variabile pentru animatia de move 
    private coordonates start;
    private coordonates finish;
    //viteza cu care se va misca obiectul in cadrul animatiei de moveFromTo
    private int moveSpeed;
    
        //imaginea obiectului
        protected GreenfootImage image;

    public decorations() {}

    /**
     * constructorul clasei Decorations
     * @param img imaginea obiectului 
     */
    public decorations(GreenfootImage img) {
        image = img;
        setImage(image);
        //blocam toate animatiile 
        bFadeIn = false;
        bFadeOut = false;
        bMove = false;
        bPulse = false;
    }

    /**
     * functie ce porneste animatia de fadeIn
     * @param speed viteza cu care va aparea obiectul
     */
    public void fadeIn(int speed) {
        bFadeIn = true;
        bFadeOut = false;
        fadeSpeed = speed;
        fadeIndex = 0;
    }

    /**
     * functie ce porneste animatia de fadeOut
     * @param speed viteza cu care va disparea obiectul
     */
    public void fadeOut(int speed) {
        bFadeIn = false;
        bFadeOut = true;
        fadeSpeed = speed;
        fadeIndex = 255;
    }

    
    /**
     * functie ce porneste animatia de pulse
     * @param speed viteza cu care va aparea si va disparea obiectul
     */
    public void pulse(int speed) {
        bPulse = true;
        fadeSpeed = speed;
    }

    
    /**
     * functie ce va porni animatia de moveFromTo
     * @param _start coordonatele punctului de pornire
     * @param _finish coordonatele punctului de sosire
     * @param speed viteza cu care se va deplasa 
     */
    public void moveFromTo(coordonates _start, coordonates _finish, int speed) {
        start = _start;
        finish = _finish;
        bMove = true;
        moveSpeed = speed;
        setLocation(start.x, start.y);
    }

    /**
     * functie ce seteaza un obiect invizibil
     */
    public void goInvisible() {
        image.setTransparency(0);
    }

    /**
     * act() - se va ocupa de rularea animatiilor
     */
    public void act() {
        if (bFadeIn) {
            if (fadeIndex < 255) {
                image.setTransparency(fadeIndex);
                fadeIndex += fadeSpeed;
            } else {
                fadeIndex = 255;
                image.setTransparency(255);
                bFadeIn = false;
            }
        } else if (bFadeOut) {
            if (fadeIndex > 0) {
                image.setTransparency(fadeIndex);
                fadeIndex -= fadeSpeed;
            } else {
                fadeIndex = 0;
                image.setTransparency(0);
                bFadeOut = false;
            }
        }
        if (bPulse) {
            if (fadeIndex == 0) {
                bFadeIn = true;
                bFadeOut = false;
            }
            if (fadeIndex == 255) {
                bFadeOut = true;
                bFadeIn = false;
            }
        }
        if (bMove) {
            coordonates newLocation = lerp(finish, moveSpeed);
            setLocation(newLocation.x, newLocation.y);
            if (Math.abs(newLocation.x - finish.x) <= 1 && Math.abs(newLocation.y - finish.y) <= 1) {
                setLocation(finish.x, finish.y);
                bMove = false;
            }
        }
    }

    /**
     * interpolare liniara intre coordonatele obiectului si coordonatele pasate 
     * @param other setul secund cu care se vor interpola coordonatele obiectului
     * @param speed viteza de interpolare
     * @return set de coordonate intermediare 
     */
    protected coordonates lerp(coordonates other, double speed) {
        // interpolare liniara
        double dx = other.x - getX(), dy = other.y - getY();
        double direction = Math.atan2(dy, dx);
        Double x = getX() + (speed * Math.cos(direction));
        Double y = getY() + (speed * Math.sin(direction));
        return new coordonates(x.intValue(), y.intValue());
    }
}
