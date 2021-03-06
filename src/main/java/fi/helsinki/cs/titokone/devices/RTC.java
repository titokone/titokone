// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.devices;

import fi.helsinki.cs.titokone.*;

/**
 * an "RTC" , though not recording real time but clocks since reset
 */
public class RTC
        implements IODevice, InterruptGenerator {
    protected Interruptable pic;
    protected int count = 0;
    protected int watchdog = 0;

    public int getPortCount() {
        return 1;
    }

    @Override
    public int getPort(int n) {
        if (n == 0) {
            return count;
        }
        throw new RuntimeException("should not be possible " + n);
    }

    @Override
    public void setPort(int n, int value) {
        if (n == 0) {
            watchdog = value;
            return;
        }
        throw new RuntimeException("should not be possible " + n);
    }

    public void reset() {
        watchdog = count = 0;
    }

    public void update() {
        count++;
        if (watchdog > 0) {
            watchdog--;
            if (watchdog == 0) {
                pic.flagInterrupt(this);
            }
        }
    }

    public void link(Interruptable pic) {
        this.pic = pic;
    }
}