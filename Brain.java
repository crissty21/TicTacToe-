import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

enum Type{
    notOpened, X, Y;
};
class Element
{
    int x, y;
    Gride BoxInWorld; //referinta la obiectul prorpiu zis din lume
    List<List<Element>> Lines; //lista cu linile din care face parte obiectul
    Type Val;
    public Element()
    {
        x = y = 0;
        Val = Val.notOpened;
        BoxInWorld = new Gride();
        Lines = new ArrayList<>();
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
    public void addLineOnLine(int index, List<Element> newLine)
    {
        Lines.get(index).addAll(newLine);
    }
    public List<Element> getLine(int index)
    {
        return Lines.get(index);
    }
    public boolean containsOnLine(int line, Element second)
    {
        return Lines.get(line).contains(second);
    }

    public void sincLines()
    {
        for(int line=0;line<=3;line++)
        {
            int sizeOfLine = Lines.get(line).size();
            //luam fiecare vecin 
            for(int i = 1;i<sizeOfLine;i++)
            {
                //adaugam in lista vecinului, toate elemente ce lipsesc
                for(int j = 0; j<sizeOfLine;j++)
                {
                    if(Lines.get(line).get(i).containsOnLine(line, Lines.get(line).get(j)) == false)
                    {
                        Lines.get(line).get(i).addOnLine(line, Lines.get(line).get(j));
                    }
                }
            }
        }
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
    int[] mapNeigh;
    final int[][] offsetNeigh = {{-1,-1,-1,0,0,1,1,1},{-1,0,1,-1,1,-1,0,1}};
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
                    justClicked.addLineOnLine(mapNeigh[i],desiredNeigh.getLine(mapNeigh[i]));
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

    public static Type getNextPlayer()
    {
        return CurrentPlayer;
    }
}
