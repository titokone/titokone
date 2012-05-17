package fi.hu.cs.titokone;

/**
 *  interface for devices which are interruptable
 */
public interface Interruptable
{
    /**
     *  an interrupt line has gone high, on the rising edge you
     *  will call this to flag an interrupt. 
     */
    public void flagInterrupt(InterruptGenerator ig);    
}