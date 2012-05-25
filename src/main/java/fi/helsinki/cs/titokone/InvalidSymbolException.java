/* Copyright 2004 University of Helsinki, Department of Computer
   Science. See license.txt for details. */
package fi.helsinki.cs.titokone;

/**
 * This class represents a situation where a symboltable has been
 * queried for a symbol it does not contain.
 */
public class InvalidSymbolException extends RuntimeException {
    /**
     * This constructor sets up an instance of the class.
     *
     * @param message The message to describe the problem more
     *                verbosely, for the user's eyes.
     */
    public InvalidSymbolException(String message) {
    }
}
