/** This class represents symbol table of TTK-91 program. */
public class SymbolTable {

	private Hashtable table;
	
	/** Returns integer value that corresponds to given symbol.
	    @param symbolName Name of the symbol.
	    @return Integer value that corresponds to symbol.
	    @throws SymbolNotDefinedException If there is no definition to given symbol.
	*/
	public int getSymbolValue (String symbolName) throws SymbolNotDefinedException (?) {}
	
	/** Returns string array that contains all defined symbols.
	    @return Array that contains all defined symbols.
	*/
	public String[] getAllSymbols() {}

	/** Adds new symbol to symbol table. If symbol was already defined this changes its value.
	    @param symbolName Name of the symbol.
	    @param symbolValue Integer value of the symbol.
	*/
	public void defineSymbol (String symbolName, int symbolValue) {}
}