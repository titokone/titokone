/*
 * Created on Feb 24, 2004
 */
package fi.helsinki.cs.ttk91;

import java.util.HashMap;

/*
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
public interface TTK91Memory 
{
    /** 
     *  @return the size of the memory. 
     */
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
	public HashMap<String,Integer> getSymbolTable();
    /**
     *  @return whole memory as a dump
     */
	public int[] getMemory();
    /**
     *  @return a code area dump. assumed to be located from
     *          offset 0 to "codeAreaSize"
     */
	public int[] getCodeArea();
    /**
     *  @return a data area dump. Assumed to be located beginning
     *          from the end of codeAreaSize to codeAreaSize+dataAreaSize
     */
	public int[] getDataArea(); 
}
