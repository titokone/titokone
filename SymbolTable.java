package fi.helsinki.cs.titokone;

import java.util.HashMap;

/** This class represents the symbol table of a TTK-91 program. It contains 
    only symbols with defined values. */
public class SymbolTable {
    /** This field contains the SymbolTable's symbols. */
    private HashMap symbols;
	
    /** This function eturns the integer value that corresponds to a 
	given symbol.
	@param symbolName Name of the symbol.
	@return Integer value that corresponds to the symbol.
	@throws SymbolNotDefinedException If there is no such
	symbol in the SymbolTable. */
    public int getSymbolValue(String symbolName) 
	throws SymbolNotDefinedException {}
	
    /** This method returns a string array that contains all the
	defined symbols.
	@return An Array that contains all the defined symbols. */
    public String[] getAllSymbols() {}

    /** This method adds a new symbol to the symbol table. If the
	symbol was already definedm this changes its value.
	@param symbolName Name of the symbol.
	@param symbolValue Integer value of the symbol. */
    public void defineSymbol(String symbolName, int symbolValue) {}

    /** This method returns the symbol table as a hashmap. The 
	hashmap returned is a copy, not a reference.
	@return A HashMap containing the symbol table, with the symbol
	names as key Strings and the integer values as Integer
	objects. */
    public HashMap toHashMap() {}
}
