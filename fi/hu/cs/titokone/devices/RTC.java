package fi.hu.cs.titokone.devices;
import fi.hu.cs.titokone.IODevice;
import fi.hu.cs.titokone.InterruptGenerator;
import fi.hu.cs.titokone.Interruptable;
/**
 *  an "RTC" , though not recording real time but clocks since reset
 */
public class RTC
implements IODevice,InterruptGenerator
{
    protected Interruptable pic;
    protected int count=0;
    protected int watchdog=0;
    public int getPortCount()
    {
        return 1;
    }
    @Override
    public int getPort(int n)
    {
        if(n==0)
            return count;
        throw new RuntimeException("should not be possible "+n);
    }
    @Override
    public void setPort(int n,int value)
    {
        if(n==0)
        {
            watchdog=value;
            return;
        }
        throw new RuntimeException("should not be possible "+n);
    }
    public void reset()
    {
        watchdog=count=0;
    }
    public void update()
    {
        count++;
        if(watchdog>0)
        {
            watchdog--;
            if(watchdog==0)
                pic.flagInterrupt(this);
        }
    }
    public void link(Interruptable pic)
    {
        this.pic=pic;
    }
}