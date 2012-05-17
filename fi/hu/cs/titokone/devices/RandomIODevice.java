package fi.hu.cs.titokone.devices;
import fi.hu.cs.titokone.IODevice;
import java.security.SecureRandom;
import java.util.Random;
/**
 *  a device which returns random numbers secure randoms by default 
 *  and pseudorandoms if you give a seed.
 */
public class RandomIODevice
implements IODevice
{
    protected Random rnd=new SecureRandom();
    public int getPortCount()
    {
        return 1;
    }
    @Override
    public int getPort(int n)
    {
        if(n==0)
            return rnd.nextInt();
        throw new RuntimeException("should not be possible "+n);
    }
    @Override
    public void setPort(int n,int value)
    {
        if(n==0)
            rnd=new Random(value);
        throw new RuntimeException("should not be possible "+n);
    }
    public void reset()
    {
        rnd=new SecureRandom();
    }
    public void update()
    {}
}