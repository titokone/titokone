package fi.helsinki.cs.titokone.devices;

import fi.helsinki.cs.titokone.*;

import java.awt.*;

/**
 * a Sound Interface Device
 */
public class SID
        implements IODevice, InterruptGenerator {
    protected Interruptable pic;

    public int getPortCount() {
        return 2;
    }

    @Override
    public int getPort(int n) {
        if (n == 0 || n == 1) {
            return 0;
        }
        throw new RuntimeException("should not be possible " + n);
    }

    @Override
    public void setPort(int n, int value) {
        switch (n) {
            case 0:
                if (value == 0) {
                    Toolkit.getDefaultToolkit().beep();
                }
                return;
            case 1:
                return;
        }
        throw new RuntimeException("should not be possible " + n);
    }

    public void reset() {
    }

    public void update() {
    }

    public void link(Interruptable pic) {
        this.pic = pic;
    }
}