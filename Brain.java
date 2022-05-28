import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

enum Type{
    notOpened, X, Y;
};
class Element
{
    int x, y;
    Gride BoxInWorld; //referinta la obiectul prorpiu zis din lume
    List<List<Element>> Lines= new ArrayList<>(); //lista cu linile din care face parte obiectul
    Type Val;
    public Element()
    {
        x = y = 0;
        Val = Val.notOpened;
        BoxInWorld = new Gride();
        
        for(int i=0;i<4;i++)
            Lines.add(new ArrayList<Element>());
    }
    public Element(int CoordX, int CoordY)
    {
        this();
        x = CoordX;
        y = CoordY;
    }
    public Element(int CoordX, int CoordY, Gride RefToObject)
    {
        this(CoordX, CoordY);
        BoxInWorld = RefToObject;
    }
    public void setStatus(Type newStatus)
    {
        Val = newStatus;
    }
    public Type getStatus()
    {
        return Val;
    }
    public int getLineLenght(int index)
    {
        return Lines.get(index).size();
    }
    public int getCoordX()
    {
        return x;
    }
    public int getCoordY()
    {
        return y;
    }
    public void addOnLine(int index, Element vecin)
    {
        Lines.get(index).add(vecin);
    }
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
    protected static Type CurrentPlayer;
    public static boolean next; // tine minte ordinea jucatorilor 

    public static int n=10; // numarul de locuri in careu
    public static int mutari; //pt a putea indentifica egalitatea
    //daca mutari == 100 => egalitate
    Map<Integer, Integer> mapNeigh;
    int[][] offsetNeigh = {{-1,-1,-1,0,0,1,1,1},{-1,0,1,-1,1,-1,0,1}};
    boolean ok=true;
    public Brain()
    {
        ok = true;
        next = true;
        mutari=0;
        setImage(new GreenfootImage("placa mov.png"));
        //cod temporal
        if(next) CurrentPlayer = Type.X;
        else CurrentPlayer = Type.Y;
        createMap();
    }
    private void createMap()
    {
        mapNeigh = new HashMap<Integer, Integer>();
        mapNeigh.put(4,0);
        mapNeigh.put(5,0);
        mapNeigh.put(7,1);
        mapNeigh.put(2,1);
        mapNeigh.put(8,2);
        mapNeigh.put(1,2);
        mapNeigh.put(6,3);
        mapNeigh.put(3,3);
    }
    public void creare_grid()
    {
        MyList<Element> TempList;
        for(int i=0;i<n;i++)
        {
            TempList = new MyList<>();
            for(int j=0;j<n;j++)
            {
                Gride obj = new Gride(i,j);
                Element temp = new Element(i, j, obj);
                TempList.add(temp);
                getWorld().addObject(obj,(i+1)*50+305,(j+1)*50+15);
            }
            Elements.add(TempList);
        }
    }
    protected Element clicked(Gride Clicked)
    {
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
            desiredLine = Elements.get(Clicked.getCoordX()+offsetNeigh[0][i]);
            if(desiredLine != null)
                desiredNeigh = desiredLine.get(Clicked.getCoordY()+offsetNeigh[1][i]);
            else continue;
            if(desiredNeigh != null) //avem vecin in careu
            {
                if(desiredNeigh.getStatus() == justClicked.getStatus())
                {
                    //trebuie sa stim in ce lista de linii il adaugam 
                    justClicked.addOnLine(/,desiredNeigh);
                    //mapeaza manual cu un vector bidimensional |
                }
            }
        }
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
    public static Type getNextPlayer()
    {
        return CurrentPlayer;
    }
}
