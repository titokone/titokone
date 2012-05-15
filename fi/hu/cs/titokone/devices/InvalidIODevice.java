package fi.hu.cs.titokone.devices;
import fi.hu.cs.titokone.IODevice;
import fi.hu.cs.titokone.Processor;
import fi.hu.cs.ttk91.TTK91InvalidDevice;
import fi.hu.cs.titokone.Message;
/**
 *  dummy device to actually do nothing and signify and invalid device
 */
public class InvalidIODevice
implements IODevice
{
    protected int size;
    public InvalidIODevice(int n)
    {
        this.size=n;
    }
    public int getPortCount()
    {
        return size;
    }    
    @Override
    public int getPort(int n)
    throws TTK91InvalidDevice
    {
        if(n>=0||n<size)        
            throw new TTK91InvalidDevice(new Message (Processor.INVALID_DEVICE_MESSAGE).toString());
        throw new RuntimeException("should not happen "+n);
    }
    @Override
    public void setPort(int n,int value)
    throws TTK91InvalidDevice
    {
        if(n>=0||n<size)
            throw new TTK91InvalidDevice(new Message (Processor.INVALID_DEVICE_MESSAGE).toString());
        throw new RuntimeException("should not happen "+n);    
    }
    public void reset()
    {}
    
    
}