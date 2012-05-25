// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.devices;

import fi.helsinki.cs.titokone.*;

/**
 * a "VIC" which baseaddress and mode changing support
 */
public abstract class VIC
        implements IODevice, InterruptGenerator {
    protected Interruptable pic;

    public int getPortCount() {
        return 5;
    }

    @Override
    public int getPort(int n) {
        if (n < 0 || n > 4) {
            throw new RuntimeException("should not be possible " + n);
        }
        return 0;
    }

    @Override
    public void setPort(int n, int value) {
        Display d = getDisplay();
        if (n == 0) {
            d.setBaseAddress(value);
            return;
        }
        if (n < 0 || n > 4) {
            throw new RuntimeException("should not be possible " + n);
        }
    }

    public void reset() {
    }

    public void update() {
    }

    public void link(Interruptable pic) {
        this.pic = pic;
    }

    public abstract Display getDisplay();

}