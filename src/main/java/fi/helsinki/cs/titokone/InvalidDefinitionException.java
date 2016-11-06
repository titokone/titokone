// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html
package fi.helsinki.cs.titokone;

/**
 * This class represents a situation where a symboltable has been
 * queried for a definition it does not contain.
 */
public class InvalidDefinitionException extends InvalidSymbolException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3847334956491503166L;

	/**
     * This constructor sets up an instance of the class.
     *
     * @param message The message to describe the problem more
     *                verbosely, for the user's eyes.
     */
    public InvalidDefinitionException(String message) {
        super(message);
    }
}
