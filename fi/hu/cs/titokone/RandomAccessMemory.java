package fi.hu.cs.titokone;

import java.util.HashMap;
import fi.hu.cs.ttk91.TTK91Memory;

/** This class represents the memory of a TTK-91 computer. */
public class RandomAccessMemory implements TTK91Memory {
   private SymbolTable symbols;

	/** Creates a memory with a given size and initializes it with
	    rows containing 0.
	    @param size Size of the memory. */
	public RandomAccessMemory(int size) {}

	/** Returns the size of the memory. Defined in TTK91Memory.
	    @return Length of the memory. */
	public int getSize() {}
	
	/** Returns the value of an indexed memory slot.
	    @return Value of an indexed memory slot. */
	public int getValue(int memorySlot) {}
	
	/** Returns the symbol table of currently used symbols as 
	    a hashmap, with String names of the symbols as keys 
	    referencing the Integer values of the symbols. 
	    Defined in TTK91Memory.
	    @return The symboltable as a hashmap. */
	public HashMap getSymbolTable() {}
	
	/** Returns a memory dump. Defined in TTK91Memory.
	    @return Memory dump in integer form. */
	public int[] getMemory() {}
	
	/** Returns a code area dump. Defined in TTK91Memory.
	    @return Code area dump in integer form. */
	public int[] getCodeArea() {}
	
	/** Returns a data area dump.
	    @return Data area dump in integer form. */
	public int[] getDataArea() {}
	
	/** Returns the length of the code area.
	    @return Length of the code area. */
	public int getCodeAreaSize() {}
	
	/** Returns the length of the data area.
	    @return Length of the data area. */
	public int getDataAreaSize() {}

	/** Returns memory line at given slot.
	    @param index Index to memory.
	    @return Memory line at given slot. */
	public MemoryLine getMemoryLine(int index) {}

        /** Changes the symbol table stored in this class. It is not 
            used by RandomAccessMemory, but can be returned as a HashMap.
            @param symbols The new symboltable to store here. */
        public void setSymbolTable(SymbolTable symbols) {}

	/** Sets new memory line to given memory slot.
	    @param index Index to memory.
	    @param memoryLine New memory line which will replace the old. */
	public void setMemoryLine(int index, MemoryLine memoryLine) {}
	
	/** Sets the length of the code area.
	    @param length Length of the code area. */
	public void setCodeAreaLength(int length) {}
	
	/** Sets the length of the data area.
	    @param length Length of the data area. */
	public void setDataAreaLength(int length) {}
}
