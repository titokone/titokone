package fi.hu.cs.titokone;

import java.util.HashMap;
import fi.hu.cs.ttk91.TTK91Memory;

/** This class represents the memory of a TTK-91 computer. */
public class RandomAccessMemory implements TTK91Memory {
    private SymbolTable symbols;
    private int size;
    private MemoryLine[] memory;
    private int codeAreaSize=0;
    private int dataAreaSize=0;

    /** Creates a memory with a given size and initializes it with
        rows containing 0.
        @param size Size of the memory. */
    public RandomAccessMemory(int size) {
        if (size < 0) throw new IllegalArgumentException ("Memory size cannot be negative.");
        this.size = size;
        memory = new MemoryLine[size];
        for (int i=0; i < size; i++)
            memory[i] = new MemoryLine (0, null);
    }

    /** Returns the size of the memory. Defined in TTK91Memory.
        @return Size of the memory. */
    public int getSize() {
        return size;
    }
	
    /** Returns the value of an indexed memory slot.
        @return Value of an indexed memory slot. */
    public int getValue(int memorySlot) {
        return memory[memorySlot].getBinary();
    }
	
    /** Returns the symbol table of currently used symbols as 
        a hashmap, with String names of the symbols as keys 
        referencing the Integer values of the symbols. 
        Defined in TTK91Memory.
        @return The symboltable as a hashmap. */
    public HashMap getSymbolTable() {
        return symbols.toHashMap();	    
    }
	
    /** Returns a memory dump. Defined in TTK91Memory.
        @return Memory dump in integer form. */
    public int[] getMemory() {
        int[] mem = new int[size];
        for (int i=0; i < size; i++)
            mem[i] = memory[i].getBinary();
        return mem;
    }

    /** Returns a code area dump. Defined in TTK91Memory.
        @return Code area dump in integer form. */
    public int[] getCodeArea() {
        int[] codeArea = new int [codeAreaSize];
        for (int i=0; i < codeAreaSize; i++)
            codeArea[i] = memory[i].getBinary();
        return codeArea;
    }

    /** Returns a data area dump.
        @return Data area dump in integer form. */
    public int[] getDataArea() {
        int[] dataArea = new int [dataAreaSize];
        for (int i=0; i < dataAreaSize; i++)
            dataArea[i] = memory[i+codeAreaSize].getBinary();
        return dataArea;
    }
	
    /** Returns the size of the code area.
        @return Size of the code area. */
    public int getCodeAreaSize() {
        return codeAreaSize;
    }

    /** Returns the size of the data area.
        @return Size of the data area. */
    public int getDataAreaSize() {
        return dataAreaSize;
    }

    /** Returns memory line at given slot.
        @param index Index to memory.
        @return Memory line at given slot. */
    public MemoryLine getMemoryLine(int index) {
        return memory[index];
    }

    /** Changes the symbol table stored in this class. It is not 
        used by RandomAccessMemory, but can be returned as a HashMap.
        @param symbols The new symboltable to store here. */
    public void setSymbolTable(SymbolTable symbols) {
        if (symbols == null) throw new IllegalArgumentException ("Parameter symbols=null.");
        this.symbols = symbols;
    }

    /** Sets new memory line to given memory slot.
        @param index Index to memory.
        @param memoryLine New memory line which will replace the old. */
    public void setMemoryLine(int index, MemoryLine memoryLine) {
        if (memoryLine == null) throw new IllegalArgumentException ("Parameter memoryLine=null.");
        memory[index] = memoryLine;
    }

    /** Sets the size of the code area.
        @param size Size of the code area. */
    public void setCodeAreaLength(int size) {
        if (size < 0) throw new IllegalArgumentException ("Code area size cannot be negative.");
        if (size > this.size) throw new IllegalArgumentException ("Code area size cannot be bigger than size of the whole memory.");
        codeAreaSize = size;
    }

    /** Sets the size of the data area.
        @param size Size of the data area. */
    public void setDataAreaLength(int size) {
        if (size < 0) throw new IllegalArgumentException ("Data area size cannot be negative.");
        if (size > this.size) throw new IllegalArgumentException ("Data area size cannot be bigger than size of the whole memory.");
        dataAreaSize = size;
    }
}
