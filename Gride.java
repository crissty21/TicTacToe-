import greenfoot.*;  
/**
 * boxurile din careu
 */    
public class Gride extends Actor
{
    int c,r;//coordonatele elementului in tabloul jocului
    boolean his = true;
    int aux,a,b;
    boolean start = false;
    int contor;
    
    GreenfootImage xImg = new GreenfootImage("as.png");
    GreenfootImage oImg = new GreenfootImage("os.png");
    
    
    public static GreenfootImage[] explozie = new GreenfootImage[13];
    
    public Gride()
    {
         explozie[0] = new GreenfootImage("box1.png");
            explozie[1] = new GreenfootImage("box2.png");
            explozie[2] = new GreenfootImage("box3.png");
            explozie[3] = new GreenfootImage("box4.png");
            explozie[4] = new GreenfootImage("box5.png");
            explozie[5] = new GreenfootImage("box6.png");
            explozie[6] = new GreenfootImage("box7.png");
            explozie[7] = new GreenfootImage("box8.png");
            explozie[8] = new GreenfootImage("box9.png");
            explozie[9] = new GreenfootImage("box10.png");
            explozie[10] = new GreenfootImage("box11.png");
            explozie[11] = new GreenfootImage("box12.png");
            explozie[12] = new GreenfootImage("box13.png");
        setImage(explozie[0]);
    }

    public void clear() //selecteaza x sau 0 pt caseta 
    {

        if(Brain.next)
        {
            setImage(xImg);
            Brain.grid[c][r]=1;
            Next.start1 = true;
            Brain.next = false;
        }
        else
        {
            setImage(oImg);
            Brain.grid[c][r]=-1;
            Next.start1 = true;
            Brain.next = true;
        }
    }

    public void end(int orientare) // pentru a pozitiona corect linia initiala
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
}
