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
public class TTK91IntegerOverflow extends TTK91RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4227581719563168741L;

	public TTK91IntegerOverflow(String message) {
        super(message);
    }
}
