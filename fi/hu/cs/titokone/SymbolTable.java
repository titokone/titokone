package fi.hu.cs.titokone;

import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

/** This class represents the symbol table of a TTK-91 program. It contains 
    only symbols with defined values. */
public class SymbolTable {
    /** This field contains the SymbolTable's symbols. */
    private HashMap symbols = new HashMap();

    /** This function returns the integer value that corresponds to a 
    given symbol.
    @param symbolName Name of the symbol.
    @return Integer value that corresponds to the symbol.
    @throws SymbolNotDefinedException If there is no such
    symbol in the SymbolTable. */
    public int getSymbolValue(String symbolName) 
        throws InvalidSymbolException {
        if (!symbols.containsKey (symbolName))
            throw new InvalidSymbolException ("Symbol " + symbolName + " not found.");
        return ((Integer)symbols.get (symbolName)).intValue();
    }

    /** This method returns a string array that contains all the
    currently defined symbols. (Not pre-defined value symbols 
    which are not used in the application this symbol table 
    is related to.)
    @return An Array that contains all the defined symbols. */
    public String[] getAllSymbols() {
	    Set keySet = symbols.keySet();
	    String[] keys = new String[keySet.size()];
	    Iterator keyIt = keySet.iterator();
	    int i=0;
	    while (keyIt.hasNext())
	        keys[i++] = (String) keyIt.next();
	    return keys;
    }

    /** This method adds a new symbol to the symbol table. If the
    symbol was already definedm this changes its value.
    @param symbolName Name of the symbol.
    @param symbolValue Integer value of the symbol. */
    public void defineSymbol(String symbolName, int symbolValue) {
        if (symbolName == null) throw new IllegalArgumentException ("symbolName: null");
        symbols.put (symbolName, new Integer(symbolValue));
    }

    /** This method returns the symbol table as a hashmap. The 
    hashmap returned is a copy, not a reference.
    @return A HashMap containing the symbol table, with the symbol
    names as key Strings and the integer values as Integer
    objects. */
    public HashMap toHashMap() {
        return (HashMap) symbols.clone();
    }
}
