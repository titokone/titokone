// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

/**
 * This class represents a situation where loading a ResourceBundle
 * from a file and instantiating it has failed.
 */
public class ResourceLoadFailedException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9214486168502350614L;

	public ResourceLoadFailedException(String message) {
        super(message);
    }
}
