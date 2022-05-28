import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class Brain extends Actor
{
    public static int[][] grid = new int[20][20]; //tablou care va retine mutarile efectuate de jucatori
    // 0 = casuta libera / 1 = casuta ocupata de jucatorul X(mov) / -1 = casuta ocupata de jucatorul O(verde)

    public static boolean next; // tine minte ordinea jucatorilor 
    // true -> urmeaza jucatorul X / false -> jucatorul O

    public static int n=10; // numarul de locuri in careu
    public static int win=3; // numarul de elemente necesare in ordinea castigarii
    public static int ct;

    public static int mutari; //pt a putea indentifica egalitatea
    //daca mutari == 100 => egalitate

    boolean ok=true;

    double a;
    public Brain()
    {
        ok = true;
        next = true;
        ct=0;
        mutari=0;
        setImage(new GreenfootImage("placa mov.png"));
    }
    public void creare_grid()
    {
        for(int i=1;i<=n;i++)
        {
            for(int j=1;j<=n;j++)
            {
                grid[i][j]=0;
                Gride obj = new Gride();
                getWorld().addObject(obj,i*50+305,j*50+15);
                obj.r=i;
                obj.c=j;
            }
        }
    }
    public void act() 
    {
        if(ok)
        {
            ok=false;
            creare_grid();
        }
    }    
}
