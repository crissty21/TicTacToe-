import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * linia ce vine trasata la final
 */
public class Line extends Actor
{
    int orientare,c,r;
    boolean set = true;
    double a;
    
    public static GreenfootImage[] linie = new GreenfootImage[5];
    public Line(int ori,int z,int b)
    {
        orientare = ori;
        c=z;
        r=b;
        a=1;
            linie[0] = new GreenfootImage("linie_1.png");
            linie[1] = new GreenfootImage("linie_2.png");
            linie[2] = new GreenfootImage("linie_3.png");
            linie[3] = new GreenfootImage("linie_4.png");
    }
    
    public void act() 
    {
        if(set)
        {
            setImage(linie[orientare-1]);
            set = false;
            switch(orientare)
            {
                case 1:
                {
                    if(Brain.grid[c][r]==Brain.grid[c-1][r])
                        getWorld().addObject(new Line(orientare,c-1,r),getX(),getY()-(int)(a*50));
                    else
                        Finalitate.liber = true;
                    break;
                }
                case 2:
                {
                    if(Brain.grid[c][r]==Brain.grid[c][r+1])
                        getWorld().addObject(new Line(orientare,c,r+1),getX()+(int)(a*50),getY());
                    else
                        Finalitate.liber = true;
                    break;
                }
                case 3:
                {
                    if(Brain.grid[c][r]==Brain.grid[c-1][r-1])
                        getWorld().addObject(new Line(orientare,c-1,r-1),getX()-(int)(a*50),getY()-(int)(a*50));
                    else
                        Finalitate.liber = true;
                    break;
                }
                case 4:
                {
                    if(Brain.grid[c][r]==Brain.grid[c-1][r+1])
                        getWorld().addObject(new Line(orientare,c-1,r+1),getX()+(int)(a*50),getY()-(int)(a*50));
                    else
                        Finalitate.liber = true;
                    break;
                }
            }
                        
        }
        
    }    
}
