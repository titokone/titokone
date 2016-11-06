// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.ttk91;

/**
 * Exception denoting program overrun.
 * Added by kohahdus project fall 2006
 */
public class TTK91ExecutionOverrun extends TTK91RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6601519372758412513L;

	public TTK91ExecutionOverrun(String message) {
        super(message);
    }

}
