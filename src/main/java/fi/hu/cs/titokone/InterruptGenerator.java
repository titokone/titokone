package fi.hu.cs.titokone;

/**
 *  interface for devices which generate interrupts and 
 *  require something to report them to
 */
public interface InterruptGenerator
{
    /**
     *  give this device a place to push its 
     *  interrupts. 
     */
    public void link(Interruptable i);
}