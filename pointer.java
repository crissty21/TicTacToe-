import greenfoot.*;

public class pointer extends Actor {
    
        private int index;
        private Element inter, oldInter;
        private ZoomElement added;
    public pointer(float _raport, int _index) {
        int dim = 1;
        GreenfootImage image = new GreenfootImage(dim, dim);
        image.setColor(new Color(0,0,0,0));
        image.fillRect(0, 0, dim, dim);
        setImage(image);
        index = _index;
    }

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
                    added = new ZoomElement(inter.getStatus(),inter);
                    getWorld().addObject(added, 154 + (28 * (index / 5)), 44 + 28 * (int) (index % 5));
                    
                }
            }
            else if (isTouching(Brain.class) == false) {
                if (added != null) {
                    getWorld().removeObject(added);
                    oldInter = null;
                }
            }
        }
    }
}
