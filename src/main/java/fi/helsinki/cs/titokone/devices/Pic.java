// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.devices;

import fi.helsinki.cs.titokone.*;

import java.util.ArrayList;

/**
 * a simple programmable interrupt controller with 32 lines
 * ports:
 * 0-command port/state port
 * read to get the state of which interrupts are high
 * write to send commands
 * -0 -> clear all interrupts
 * -1 -> inhibit all interrupts
 * -2 -> re-enable interrupts
 * 1-interrupt enable port
 * read- to see which interrupts are enabled
 * write to enable/disable interrupts
 * to clear a specific interrupt , disable and re-enable it
 */
public class Pic
        implements IODevice,
        Interruptable,
        InterruptGenerator {
    protected Interruptable processor;
    protected int enabled = 0;
    protected int flagged = 0;
    protected int seen = 0;
    protected boolean disabled = false;
    protected ArrayList<InterruptGenerator> generators =
            new ArrayList<InterruptGenerator>();

    public int getPortCount() {
        return 2;
    }

    @Override
    public int getPort(int n) {
        if (n == 0) {
            return flagged; //show what interrupts are high
        } else if (n == 1) {
            return enabled; //show enabled interrupts
        }
        throw new RuntimeException("should not be possible " + n);
    }

    @Override
    public void setPort(int n, int value) {
        if (n == 0) {
            switch (value) {
                case 0:
                    flagged = 0;
                    return; //clear
                case 1:
                    disabled = true;
                    return; //disable
                case 2:
                    disabled = false;
                    return; //re-enable
            }
        } else if (n == 1) {
            flagged = flagged & value;
            enabled = value; //set enabled interrupts
            return;
        }
        throw new RuntimeException("should not be possible " + n);
    }

    public void reset() {
        enabled = 0;
        flagged = 0;
        seen = 0;
        disabled = false;
    }

    public void update() {
        if (!disabled) {
            if (((~seen) & flagged & enabled) > 0) {
                processor.flagInterrupt(this);
            }
            seen = (flagged & enabled);
        }
    }

    public void link(Interruptable il) {
        if (processor == null) {
            processor = il;
        }
    }

    public void flagInterrupt(InterruptGenerator ig) {
        int i = 0;
        for (InterruptGenerator igg : generators) {
            if (ig == igg) {
                break;
            }
            i++;
        }
        flagged |= (1 << i);
    }

    public void add(InterruptGenerator ig) {
        generators.add(ig);
    }
}