import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class image here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class image extends Actor
{
    GreenfootImage img = new GreenfootImage(1200,800); //a new image with the dim. of the world
    int[][] a = new int[300][200]; //the array we are working on
    int[][] b = new int[300][200]; //aux for the above
    
    
    int n =300,m=200;
    public image()
    {
        for(int i=0;i<n;i++)
        for(int j=0;j<m;j++)
        if(Greenfoot.getRandomNumber(10) > 7)
        a[i][j] =  1;//first generation 
        this.setImage(img);
        actualizare_img();
        //System.out.println(nr_vecini(5,5));
    }
    void actualizare_img() //update the image
    {
        img.clear();
        for(int i=0;i<n;i++)
        for(int j=0;j<m;j++)
        if(a[i][j] == 1)
            img.fillRect(i*4,j*4,4,4);
    }
    int nr_vecini(int i, int j) //no. neightboards
    {
        int s = 0;
        if(i>0 && j>0) s+=a[i-1][j-1];
        if(i>0) s+= a[i-1][j];
        if(j>0) s+= a[i][j-1];
        if(i<n-1 && j<m-1) s+= a[i+1][j+1];
        if(i<n-1) s+= a[i+1][j];
        if(j<m-1) s+= a[i][j+1];
        if(i<n-1 && j>0) s+=a[i+1][j-1];
        if(i>0 && j<m-1) s+=a[i-1][j+1];
        return s;
    }
    void transpunere() //excange 
    {
        for(int i=0;i<n;i++)
        for(int j=0;j<m;j++)
        a[i][j] = b[i][j];
    }
    public void act()
    {
        int nr;
        for(int i=0;i<n;i++)
        for(int j=0;j<m;j++)
        {
            nr = nr_vecini(i,j);
            //System.out.println(nr_vecini(i,j));
            if(a[i][j] == 0)//is dead
            {
                if(nr == 3)
                    b[i][j] = 1;
                else b[i][j] = 0;
            }
            else
            {
                if(nr < 2 || nr > 3)
                    b[i][j] = 0;
                else b[i][j] = 1;
            }
        }
        transpunere();
        actualizare_img();
    }
}
