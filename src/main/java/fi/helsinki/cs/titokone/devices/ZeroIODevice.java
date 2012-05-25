package fi.helsinki.cs.titokone.devices;
import fi.helsinki.cs.titokone.IODevice;
/**
 *  a device which returns 0's and eats output
 */
public class ZeroIODevice
implements IODevice
{
    public int getPortCount()
    {
        return 1;
    }
    @Override
    public int getPort(int n)
    {
        if(n==0)
            return 0;
        throw new RuntimeException("should not be possible "+n);
    }
    @Override
    public void setPort(int n,int value)
    {
        if(n!=0)
            throw new RuntimeException("should not be possible "+n);
    }
    public void reset()
    {}
    public void update()
    {}
}