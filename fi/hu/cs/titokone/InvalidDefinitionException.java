/* Copyright 2004 University of Helsinki, Department of Computer
   Science. See license.txt for details. */
package fi.hu.cs.titokone;

/** This class represents a situation where a symboltable has been
    queried for a definition it does not contain. */
public class InvalidDefinitionException extends InvalidSymbolException {
    /** This constructor sets up an instance of the class. 
	@param message The message to describe the problem more
	verbosely, for the user's eyes. */
    public InvalidDefinitionException(String message) {
	super(message);
    }
}
