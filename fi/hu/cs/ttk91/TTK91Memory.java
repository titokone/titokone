/*
 * Created on Feb 24, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fi.hu.cs.ttk91;

import java.util.HashMap;

/**
 * @author mustakko
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
