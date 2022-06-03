import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

enum Type{
    notOpened, X, Y;
};

//clasa folosita pentru override la metoda get()
class MyList<T> extends ArrayList<T>
{
    @Override
    public T get(int index)
    {
        if(index < 0 || index > size()-1)
        {
            return null;
        }
        return super.get(index);
    }
}
public class Brain extends Actor
{
    static MyList<MyList<Element>> Elements = new MyList<>();
    //     0 -> -
    //     1 -> |
    //     2 -> \
    //     3 -> /

    protected static Type CurrentPlayer; //tine minte care jucator urmeaza
    public static int winReq; //numarul de elemente consecutive necesare castigarii
    public static int n; // numarul de locuri in careu
    public static int mutari; //pt a putea indentifica egalitatea
    //daca mutari == 100 => egalitate


    int[] mapNeigh;//mapeaza vecinii pe linii, in functie de oridinea lor
    final int[][] offsetNeigh = {{-1,-1,-1,0,0,1,1,1},{-1,0,1,-1,1,-1,0,1}}; //offsetul la care se afla vecinii

    boolean ok;//semafor folosit pentru crearea unui eveniment begin play
    public Brain()
    {
        init();
        createMap();
        setImage(new GreenfootImage("placa mov.png"));
    }

    private void init() 
    {
        //initializeaza variabile
        winReq = 4;
        n=10;
        ok = true;
        mutari=0;
        CurrentPlayer = Type.X;
    }

    private void createMap()
    {
        //creaza mapa vecinilor
        mapNeigh = new int[8];
        mapNeigh[0] = 2;
        mapNeigh[1] = 1;
        mapNeigh[2] = 3;
        mapNeigh[3] = 0;
        mapNeigh[4] = 0;
        mapNeigh[5] = 3;
        mapNeigh[6] = 1;
        mapNeigh[7] = 2;
    }

    private int turnXinCoord(int x)
    {
        //turning index into coordonates
        return (x+1)*50+305;
    }
    private int turnYinCoord(int y)
    {
        //turning index into coordonates
        return (y+1)*50+15;
    }
    
    public void creare_grid()
    {
        Elements.clear();
        MyList<Element> TempList;
        for(int i=0;i<n;i++)
        {
            TempList = new MyList<>();
            for(int j=0;j<n;j++)
            {
                Element temp = new Element(i, j);
                TempList.add(temp);
                getWorld().addObject(temp,turnXinCoord(i),turnYinCoord(j));
            }
            Elements.add(TempList);
        }
    }
    public static Type getNextPlayer()
    {
        return CurrentPlayer;
    }
    
    protected Element clicked(Element Clicked)
    {
        boolean added;
        MyList<Element> desiredLine;
        Element desiredNeigh;
        Element justClicked = Elements.get(Clicked.getCoordX()).get(Clicked.getCoordY());

        justClicked.setStatus(CurrentPlayer);
        // ne adaugam pe noi pe liniile componente
        for(int i=0;i<=3;i++)
            justClicked.addOnLine(i, justClicked);
            
        //verificam vecinii si adaugam pe linie daca sunt deschisi 
        //avem 8 vecini
        for(int i=0;i<8;i++)
        {
            added = false;
            desiredLine = Elements.get(Clicked.getCoordX()+offsetNeigh[0][i]);
            if(desiredLine != null)
                desiredNeigh = desiredLine.get(Clicked.getCoordY()+offsetNeigh[1][i]);
            else continue;
            if(desiredNeigh != null) //avem vecin in careu
            {
                if(desiredNeigh.getStatus() == justClicked.getStatus())
                {
                    //trebuie sa stim in ce lista de linii il adaugam 
                    justClicked.addLineOnLine(mapNeigh[i],desiredNeigh.getLine(mapNeigh[i]));
                    added = true;
                }
            }
            if(added)
            {
                if(justClicked.won(mapNeigh[i]))
                {
                    //adding the Lines
                    for(Element iter : justClicked.getLine(mapNeigh[i]))
                    {
                        getWorld().addObject(new Line(mapNeigh[i]), turnXinCoord(iter.getCoordX()), turnYinCoord(iter.getCoordY()));
                    }
                }
            }
        }
        //dupa ce am gasit toti vecinii, sincronizam listele lor, cu lista din elementul curent 
        justClicked.sincLines();
        return justClicked;
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
/*
 * "java.project.referencedLibraries": [
        "lib/doua stelute sus/*.jar",
        "c:\\path\\to\\jarfile\\commons-logging-1.1.1.jar"
    ]
 */