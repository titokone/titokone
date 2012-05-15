package fi.hu.cs.titokone;
/**
 *  common interface for all devices (IODevices and MMAPDevices)
 */
public interface Device
{
    /**
     *  "reboot" to zero state
     */
    public void reset();
}