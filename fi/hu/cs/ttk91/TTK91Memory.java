/*
 * Created on Feb 24, 2004
 *
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
package fi.hu.cs.ttk91;

import java.util.HashMap;

public interface TTK91Memory {
	public int getSize();
	/**
	 * First usable index is 0, last is getSize()-1
	 * @param memoryslot memory address where required data is 
	 * @return data from requested address
	 */
	public int getValue(int memoryslot);
	
	/**
	 * 
	 * @return HashMap with symbol name as a key, and Integer as value. 
	 *         Integer describes the memory slot where the value is stored.
	 */
	public HashMap getSymbolTable();
	public int[] getMemory();
	public int[] getCodeArea();
	public int[] getDataArea(); 
}
