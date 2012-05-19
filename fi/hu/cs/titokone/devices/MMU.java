package fi.hu.cs.titokone.devices;
import fi.hu.cs.titokone.*;
import fi.hu.cs.ttk91.*;
import java.util.*;
/** 
 *   a nice nastiness of a device which registers itself as an io device
 *   and takes over memory management. 
 *    
 *  write to port 0 to map memory
 *  0xFFFFFFFF will reset MMU
 *  to map memory write 3 values "from" "to" and "length" 
 *  currently only 1 mapping can be active at one time.
 *  new mapping will overwrite the old one
 *
 *  reading port 0 will tell you the state of mapping command
 *  0=from,1=to,2=length
 *  NOTE: currently virtual memory size is fixed to the "physical memory"
 *          size together with MMAPped devices. MMU can only be used
 *          to move memory to a more convenient place
 */
public abstract class MMU
implements IODevice,
            RandomAccessMemory,
            InterruptGenerator
{
    int from=0,to=0,length=0;
    int count=0;
    public MMU()  
    {
    }
    public void link(Interruptable il)
    {
        //just a reservation
    }
    public void reset()
    {
        from=0;
        to=0;
        length=0;
        count=0;
    }
    public int getPortCount()
    {
        return 5; //reserve a couple ports
    }
    public int getPort(int i)
    {
        if(i<0||i>=getPortCount())
            throw new RuntimeException("shouldnt happen");
        return count;
    }
    public void setPort(int i,int value)
    {
        if(i<0||i>=getPortCount())
            throw new RuntimeException("shouldnt happen");
        switch(count)
        {
            case 0:
                if(value==0xffffffff)
                {
                    count=0;
                    from=0;
                    to=0;
                }else{
                    from=value;
                    count++;
                }   
                break;
            case 1:
                to=value;
                count++;
                break;
            case 2:
                length=value;
                count=0;
                break;
        }
    }
    public void update()
    {}
    public int getSize()
    {
        return getMem().getSize();
    }
    public int getCodeAreaSize()
    {
        return getMem().getCodeAreaSize();
    }
    public int getDataAreaSize()
    {
        return getMem().getDataAreaSize();
    }
    public MemoryLine getMemoryLine(int index)
    {
        return getMem().getMemoryLine(mapslot(index));
    }
    public MemoryLine[] getMemoryLines()
    {
        MemoryLine[] ret=getMem().getMemoryLines();
        if(from!=0||to!=0||length!=0)
        {
            for(int i=0;i<length;i++)
            {
                ret[to+i]=getMem().getMemoryLine(from+i);
            }
        }
        return ret;
    }
    public void setSymbolTable(SymbolTable symbols)
    {
        getMem().setSymbolTable(symbols);
    }
    public void setMemoryLine(int index,MemoryLine memoryLine)
    throws TTK91AddressOutOfBounds
    {
        getMem().setMemoryLine(mapslot(index),memoryLine);        
    }
    public void setCodeAreaLength(int size)
    {
        getMem().setCodeAreaLength(size);
    }
    public void setDataAreaLength(int size)
    {
        getMem().setDataAreaLength(size);
    }
    public int getMemoryReferences()
    {
        return getMem().getMemoryReferences();
    }
    public int getValue(int memoryslot)
    {
        return getMemoryLine(memoryslot).getBinary();
    }
    protected int mapslot(int slot)
    {
        if(from!=0||to!=0||length!=0)
        {
            return slot-to+from;
        }
        
        return slot;
    }
    public HashMap<String,Integer> getSymbolTable()
    {
        return getMem().getSymbolTable();
    }
    public int[] getMemory()
    {
        MemoryLine[] temp=getMemoryLines();
        int[] ret=new int[temp.length];
        for(int i=0;i<temp.length;i++)
            ret[i]=temp[i].getBinary();
        return ret;
    }
    public int[] getCodeArea()
    {
        int[] codeArea = new int [getCodeAreaSize()];
        for(int i=0;i<codeArea.length;i++)
            codeArea[i]=getMemoryLine(i).getBinary();
        return codeArea;
    }
    public int[] getDataArea()
    {
        int cz=getCodeAreaSize();
        int[] dataArea = new int [getDataAreaSize()];
        for(int i=0;i<dataArea.length;i++)
            dataArea[i]=getMemoryLine(i+cz).getBinary();
        return dataArea;
    }    
    
    /**
     *  override this to return the memory instance currently active.     
     */
    protected abstract RandomAccessMemory getMem();
    
}
