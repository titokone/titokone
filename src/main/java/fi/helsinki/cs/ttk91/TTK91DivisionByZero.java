// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

/*
 * Created on Mar 17, 2004
 */
package fi.helsinki.cs.ttk91;

/*
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
public class TTK91DivisionByZero extends TTK91RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3469501783104969979L;

	public TTK91DivisionByZero(String message) {
        super(message);
    }
}
