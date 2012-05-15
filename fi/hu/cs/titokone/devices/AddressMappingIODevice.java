package fi.hu.cs.titokone.devices;
import fi.hu.cs.titokone.IODevice;
import fi.hu.cs.ttk91.TTK91InvalidDevice;
/**
 *  a dummy device which maps underlying device to a new address
 */
public class AddressMappingIODevice
implements IODevice
{
    protected IODevice delegate;
    protected int base;
    public AddressMappingIODevice(int base,IODevice delegate)
    {
        this.delegate=delegate;
        this.base=base;
        if(delegate==null||base<0||base>65000)
            throw new IllegalArgumentException("delegate must not be null");
    }
    public int getPortCount()
    {
        return delegate.getPortCount();
    }
    public int getPort(int n)
    throws TTK91InvalidDevice
    {
        return delegate.getPort(n-base);
    }
    public void setPort(int n,int value)
    throws TTK91InvalidDevice
    {
        delegate.setPort(n-base,value);
    }
}