package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.TTK91RuntimeException;

/**
 * interface for devices which do INput/OUTput ie. expose
 * io ports
 */
public interface IODevice
        extends Device {
    /**
     * number of ports required by this device
     */
    public int getPortCount();

    /**
     * read port number n , if reading is not implemented
     * should return a "0" instead
     * offsets are calculated starting from 0 to size-1
     * offsets are relative to the device's base address
     * determined on startup.
     * NOTE: since these are ports and not memory , these
     * might be backed up by something which gives out
     * funny values..so you might not get the same value
     * out  you wrote in.
     *
     * @throws TTK91InvalidDevice if reading is forbidden for this device
     */
    public int getPort(int n)
            throws TTK91RuntimeException;

    /**
     * write port number n, if writing is not implemented
     * should do nothing.
     * NOTE: ports are not memory. so writing a value might not do
     * anything and what you write might not be what you get when
     * reading
     *
     * @throws TTK91InvalidDevice if writing is forbidden for this device
     */
    public void setPort(int n, int value)
            throws TTK91RuntimeException;
}