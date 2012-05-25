// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

/**
 * common interface for all devices (IODevices and MMAPDevices)
 */
public interface Device {
    /**
     * "reboot" to zero state
     */
    public void reset();

    /**
     * update state of device
     */
    public void update();
}