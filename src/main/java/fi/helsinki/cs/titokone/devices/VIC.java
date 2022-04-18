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
    protected State state = State.Normal;

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
        if (state == State.Normal) {
            switch (value) {
                case 0:
                    state = State.SetBase;
                    return;
                case 1:
                    state = State.SetMargin;
                    return;
                case 2:
                    state = State.SetMode;
                    return;
            }
        } else {
            switch (state) {
                case SetBase:
                    d.setBaseAddress(value);
                    break;
                case SetMargin:
                    d.setMarginColor(value);
                    break;
                case SetMode:
                    d.setMode(value);
                    break;
			default:
				break;
            }
            state = State.Normal;
            return;
        }
        if (n < 0 || n > 4) {
            throw new RuntimeException("should not be possible " + n);
        }
    }

    public void reset() {
        state = State.Normal;
    }

    public void update() {
    }

    public void link(Interruptable pic) {
        this.pic = pic;
    }

    public abstract Display getDisplay();

    enum State {
        Normal,
        SetBase,
        SetMargin, //set margin color
        SetMode;
    }

}