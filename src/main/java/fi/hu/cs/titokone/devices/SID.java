package fi.hu.cs.titokone.devices;
import fi.hu.cs.titokone.IODevice;
import fi.hu.cs.titokone.InterruptGenerator;
import fi.hu.cs.titokone.Interruptable;
import java.awt.Toolkit;
/**
 *  a Sound Interface Device
 */
public class SID
implements IODevice,InterruptGenerator
{
    protected Interruptable pic;
    public int getPortCount()
    {
        return 2;
    }
    @Override
    public int getPort(int n)
    {
        if(n==0||n==1)
            return 0;
        throw new RuntimeException("should not be possible "+n);
    }
    @Override
    public void setPort(int n,int value)
    {   
        switch(n)
        {
            case 0:
                if(value==0)
                {
                    Toolkit.getDefaultToolkit().beep();
                }
                return;
            case 1:return;
        }
        throw new RuntimeException("should not be possible "+n);
    }
    public void reset()
    {}
    public void update()
    {}
    public void link(Interruptable pic)
    {
        this.pic=pic;
    }
}