package fi.hu.cs.titokone;

import java.util.HashMap;

/** This class represents the symbol table of a TTK-91 program. It contains 
    only symbols with defined values. */
public class SymbolTable {
    /** This field contains the SymbolTable's symbols. */
    private HashMap symbols;

    // The symboltable will not be able to say what a constant symbol
    // not "in use" would have as its value, nor does it need to. This
    // data should be in some form in Compiler, not here. --Sini 17.3.
    /** Constant symbol CRT */
    //private static final int CRT = 0;
    /** Constant symbol KBD */
    //private static final int KBD = 1;
    /** Constant symbol STDIN */
    //private static final int STDIN = 6;
    /** Constant symbol STDOUT */
    //private static final int STDOUT = 7;
    /** Constant symbol HALT */
    //private static final int HALT = 11;
    /** Constant symbol READ */
    //private static final int READ = 12;
    /** Constant symbol WRITE */
    //private static final int WRITE = 13;
    /** Constant symbol TIME */
    //private static final int TIME = 14;
    /** Constant symbol DATE */
    //private static final int DATE = 15;
	
    /** This function eturns the integer value that corresponds to a 
	given symbol.
	@param symbolName Name of the symbol.
	@return Integer value that corresponds to the symbol.
	@throws SymbolNotDefinedException If there is no such
	symbol in the SymbolTable. */
    public int getSymbolValue(String symbolName) 
	throws InvalidSymbolException {}
	
    /** This method returns a string array that contains all the
	currently defined symbols. (Not pre-defined value symbols 
        which are not used in the application this symbol table 
        is related to.)
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
