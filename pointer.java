import java.util.*;

import greenfoot.*;

public class pointer extends Actor {
    public pointer(float _raport, int _index) {
        int dim = 2;
        GreenfootImage image = new GreenfootImage(dim, dim);
        image.setColor(Color.BLACK);
        image.fillRect(0, 0, dim, dim);
        setImage(image);
        index = _index;
    }

    private int index;
    private Element inter, oldInter;
    private ZoomElement added;

    public void act() {
        if (Greenfoot.mouseMoved(null)) {
            inter = (Element) getOneIntersectingObject(Element.class);
            if (inter != null) {
                if (inter != oldInter) // atingem obiect nou
                {
                    if (added != null) {
                        getWorld().removeObject(added);
                    }
                    oldInter = inter;
                    added = new ZoomElement(inter.getStatus());
                    getWorld().addObject(added, 70 + (50 * (index / 3)), 90 + 50 * (int) (index % 3));
                }
            }
            else if (isTouching(Brain.class) == false) {
                if (added != null) {
                    getWorld().removeObject(added);
                }
            }
        }
    }
}
