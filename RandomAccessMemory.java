/** This class represents the memory of TTK-91 computer. */
public class RandomAccessMemory implements Memory {

	/** Creates memory with given size. 
	    @param size Size of the memory.
	*/
	public RandomAccessMemory (int size) {}

	/** Returns the length of the memory.
	    return Length of the memory.
	*/
	public int getLength() {}
	
	/** Returns the value of indexed memory slot.
	    @return Value of indexed memory slot.
	*/
	public int getValue(int memorySlot) {}
	
	/** Returns symbol table.
	    @return Symbol table.
	*/
	public SymbolTable getSymbolTable() {}
	
	/** Returns memory dump.
	    @return Memory dump.
	*/
	public int[] getMemory() {}
	
	/** Returns code area dump.
	    @return Code area dump.
	*/
	public int [] getCodeArea() {}
	
	/** Returns data area dump.
	    @return Data area dump.
	*/
	public int[] getDataArea() {}
	
	/** Returns the length of code area.
	    @return Length of code area.
	*/
	public int getCodeAreaLength() {}
	
	/** Returns the length of data area.
	    @param len Length of data area.
	*/
	public int getDataAreaLength() {}

	/** Returns memory line at given slot.
	    @param index Index to memory.
	    @return Memory line at given slot.
	*/
	public MemoryLine getMemoryLine (int index) {}

	/** Sets new memory line to given memory slot.
	    @param index Index to memory.
	    @param memoryLine New memory line which will replace old.
	*/
	public void setMemoryLine (int index, MemoryLine memoryLine) {}
	
	/** Sets the length of code area.
	    @param len Length of code area.
	*/
	public void setCodeAreaLength(int len) {}
	
	/** Sets the length of data area.
	    @param len Length of data area.
	*/
	public void setDataAreaLength(int len) {}
}