// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

/*
 * Created on Feb 24, 2004
 */
package fi.helsinki.cs.ttk91;

/*
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
public abstract class TTK91Exception extends Exception {
    public TTK91Exception(String message) {
        super(message);
    }

    public TTK91Exception(Throwable cause) {
        super(cause);
    }

    public TTK91Exception(String message, Throwable cause) {
        super(message, cause);
    }

}
