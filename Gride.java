import greenfoot.*;  
/**
 * boxurile din careu
 */    
public class Gride extends Brain
{
    int c,r;//coordonatele elementului in tabloul jocului
    boolean his = true;
    boolean start = false;
    int contor;
    public Element tem;
    static GreenfootImage xImg = new GreenfootImage("as.png");
    static GreenfootImage oImg = new GreenfootImage("os.png");

    static GreenfootImage[] explozie = {
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

    public Gride()
    {
        setImage(explozie[0]);
    }

    public Gride(int x, int y)
    {
        r=x;
        c=y;
        setImage(explozie[0]);
    }
    public int getCoordX()
    {
        return r;
    }
    public int getCoordY()
    {
        return c;
    }
    public void clear() //selecteaza x sau 0 pt caseta 
    {
        tem = clicked(this);
        if(CurrentPlayer == Type.X)
        {
            CurrentPlayer = Type.Y;
            setImage(xImg);
        }
        else
        {
            CurrentPlayer = Type.X;
            setImage(oImg);
        }
        Next.start1 = true;  
    }
    public void act() 
    {
        if(Greenfoot.mouseClicked(this))
        {   
            if(his) //verifica daca nu a fost deschisa cutia
            {
                his = false; //marcheaza casuta ca fiind deschisa
                Brain.mutari++;
                start = true;
                contor = 0;
            }
        }
        if(start)
        {
            contor++;
            if(contor % 5 == 0)
            {
                setImage(explozie[contor / 5]);
            }
            if(contor == 30)Next.start = true;  
            if(contor >= 60)
            {
                start = false;
                clear();
            }
        }
    }    
    /*    public void end(int orientare) // pentru a pozitiona corect linia initiala
    {
        switch(orientare)
        {
            case 1: //vertical
                {
                    while(Brain.grid[a+1][r]==1 || Brain.grid[a+1][r]==-1)
                        if(Brain.grid[a+1][r] == Brain.grid[a][r])a+=1; 
                        else break;
                    getWorld().addObject(new Line(aux,a,r),getX() , getY() + (a-c)*((int)(50)));
                    Finalitate.liber = true;
                    break;
                }
            case 2: //orizontal
                {
                    while(Brain.grid[c][b-1]==1 || Brain.grid[c][b-1]==-1)
                        if(Brain.grid[c][b-1] == Brain.grid[c][b])b-=1;
                        else break;
                    getWorld().addObject(new Line(aux,c,b),getX() - (r-b)*((int)(50)) , getY());
                    Finalitate.liber = true;
                    break;
                }
            case 4: //diagonal sus stanga
                {
                    while(Brain.grid[a+1][b-1]==1 || Brain.grid[a+1][b-1]==-1)
                    {
                        if(Brain.grid[a+1][b-1] == Brain.grid[a][b])
                        {
                            a+=1;
                            b-=1;
                        }
                        else break;
                    }
                    getWorld().addObject(new Line(aux,a,b),getX() - (r-b)*((int)(50)) , getY() + (a-c)*((int)(50)));
                    Finalitate.liber = true;
                    break;
                }
            case 3: //diagonal sus dreapta
                {
                    while(Brain.grid[a+1][b+1]==1 || Brain.grid[a+1][b+1]==-1)
                    {
                        if(Brain.grid[a+1][b+1] == Brain.grid[a][b])
                        {
                            a+=1;
                            b+=1;
                        }
                        else break;
                    }
                    getWorld().addObject(new Line(aux,a,b),getX() + (b-r)*((int)(50)) , getY() + (a-c)*((int)(50)));
                    Finalitate.liber = true;
                    break;
                }

        }
    }*/
}
