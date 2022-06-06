import greenfoot.*;

public class ZoomElement extends Actor
{

    private boolean startAnim;
    private int contor;
    private Type tip;
    private static GreenfootImage xImg = new GreenfootImage("as.png");
    private static GreenfootImage oImg = new GreenfootImage("os.png");
    private static GreenfootImage[] explozie = {
            new GreenfootImage("box1.png"),
            new GreenfootImage("box2.png"),
            new GreenfootImage("box3.png"),
            new GreenfootImage("box4.png"),
            new GreenfootImage("box5.png"),
            new GreenfootImage("box6.png"),
            new GreenfootImage("box7.png"),
            new GreenfootImage("box8.png"),
            new GreenfootImage("box9.png"),
            new GreenfootImage("box10.png"),
            new GreenfootImage("box11.png"),
            new GreenfootImage("box12.png"),
            new GreenfootImage("box13.png")
    };
    public ZoomElement(Type _tip)
    {
        startAnim = false;
        tip = _tip;
        switch(tip)
        {
            case X: setImage(xImg);break;
            case Y: setImage(oImg);break;
            default: setImage(explozie[0]);
        }
        
    }
    public static void scaleImgs() {
        int dim = 20;
        xImg.scale(dim, dim);
        oImg.scale(dim, dim);
        for(GreenfootImage iter : explozie)
        {
            iter.scale(dim, dim);
        }
    }
    public void act() {
        if (startAnim) {
            contor++;
            if (contor % 5 == 0) {
                setImage(explozie[contor / 5]);
            }
            if (contor == 30) {
                Next.start = true;
            }
            if (contor >= 60) {
                startAnim = false;
            }
        }
    }
}
