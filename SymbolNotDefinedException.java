package fi.hu.cs.titokone;

/** This class represents a situation where a symboltable has been
    queried for a symbol it does not contain. */
public class SymbolNotDefinedException extends Exception {
    /** This constructor sets up an instance of the class. 
	@param message The message to describe the problem more
	verbosely, for the user's eyes. */
    public SymbolNotDefinedException(String message) {}
}
