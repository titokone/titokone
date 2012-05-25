// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.devices;

import fi.helsinki.cs.titokone.*;
import fi.helsinki.cs.ttk91.*;

/**
 * dummy device to actually do nothing and signify and invalid device
 */
public class InvalidIODevice
        implements IODevice {
    protected int size;

    public InvalidIODevice(int n) {
        this.size = n;
    }

    public int getPortCount() {
        return size;
    }

    @Override
    public int getPort(int n)
            throws TTK91RuntimeException {
        if (n >= 0 || n < size) {
            throw new TTK91InvalidDevice(new Message(Processor.INVALID_DEVICE_MESSAGE).toString());
        }
        throw new RuntimeException("should not happen " + n);
    }

    @Override
    public void setPort(int n, int value)
            throws TTK91RuntimeException {
        if (n >= 0 || n < size) {
            throw new TTK91InvalidDevice(new Message(Processor.INVALID_DEVICE_MESSAGE).toString());
        }
        throw new RuntimeException("should not happen " + n);
    }

    public void reset() {
    }

    public void update() {
    }
}