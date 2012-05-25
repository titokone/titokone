// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

/**
 * interface for devices which generate interrupts and
 * require something to report them to
 */
public interface InterruptGenerator {
    /**
     * give this device a place to push its
     * interrupts.
     */
    public void link(Interruptable i);
}