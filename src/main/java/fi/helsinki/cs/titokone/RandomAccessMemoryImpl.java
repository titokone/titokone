package fi.helsinki.cs.titokone;

import java.util.HashMap;
import fi.helsinki.cs.ttk91.TTK91Memory;
import fi.helsinki.cs.ttk91.TTK91AddressOutOfBounds;

/** This class represents the memory of a TTK-91 computer. */
public class RandomAccessMemoryImpl 
implements TTK91Memory,RandomAccessMemory
{
    private SymbolTable symbols = new SymbolTable();
    private int size;
    private MemoryLine[] memory;
    private int codeAreaSize=0;
    private int dataAreaSize=0;

    //Added by Harri Tuomikoski, 12.10.2004, Koskelo-project
    private int memory_references=0;

    /** Creates a memory with a given size and initializes it with
        rows containing 0.
        @param size Size of the memory. */
    public RandomAccessMemoryImpl(int size) {
        if (size < 0) throw new IllegalArgumentException (new Message("Memory size cannot be negative.").toString());
        this.size = size;
        memory = new MemoryLine[size];
        for (int i=0; i < size; i++)
            memory[i] = new MemoryLine (0, "");

        this.memory_references = 0; //Added by HT, 12.10.2004, Koskelo-project
    }

    /** Returns the size of the memory. Defined in TTK91Memory.
        @return Size of the memory. */
    public int getSize() {
        return size;
    }
	
    /** Returns the value of an indexed memory slot.
        @return Value of an indexed memory slot. */
    public int getValue(int memorySlot) {
        ++this.memory_references; //Added by HT, 12.10.2004, Koskelo-project
        return memory[memorySlot].getBinary();
    }
	
    /** Returns the symbol table of currently used symbols as 
        a hashmap, with String names of the symbols as keys 
        referencing the Integer values of the symbols. 
        Defined in TTK91Memory.
        @return The symboltable as a hashmap. */
    public HashMap<String,Integer> getSymbolTable() {
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

    /** This method returns a copy of all the memory lines. 
	@return An array containing all the memory lines. */
    @SuppressWarnings("unchecked")
    public MemoryLine[] getMemoryLines() {
        return (MemoryLine[]) memory.clone();
    }

    /** Changes the symbol table stored in this class. It is not 
        used by RandomAccessMemory, but can be returned as a HashMap.
        @param symbols The new symboltable to store here. */
    public void setSymbolTable(SymbolTable symbols) {
        if (symbols == null) 
	    throw new IllegalArgumentException (new Message("Tried to set " +
							    "symbol table to " +
							    "null.").toString());
        this.symbols = symbols;
    }

    /** Sets new memory line to given memory slot.
        @param index Index to memory.
        @param memoryLine New memory line which will replace the old. */
    public void setMemoryLine(int index, MemoryLine memoryLine) 
        throws TTK91AddressOutOfBounds {
        String errorMessage;
        String[] errorParameters;
        if (memoryLine == null) {
            errorMessage = new Message("Trying to load a null memory " +
                           "line.").toString();
            throw new IllegalArgumentException(errorMessage);
        }
        if(index > memory.length) {
            errorParameters = new String[2];
            errorParameters[0] = "" + index;
            errorParameters[1] = "" + memory.length;
            errorMessage = new Message("Address {0} too large, memory size " +
                           "{1} (indexing starts at 0).", 
                           errorParameters).toString();
            throw new TTK91AddressOutOfBounds(errorMessage);
        }
        if(index < 0) {
            errorMessage = new Message("Address {0} below zero.", 
                           "" + index).toString();
            throw new TTK91AddressOutOfBounds(errorMessage);

        }
        memory[index] = memoryLine;
        memory_references++;        
    }

    /** Sets the size of the code area.
        @param size Size of the code area. */
    public void setCodeAreaLength(int size) {
        if (size < 0) throw new IllegalArgumentException ("Code area size cannot be negative.");
        if (size > this.size) throw new IllegalArgumentException ("Code area size cannot be bigger than the size of the whole memory.");
        codeAreaSize = size;
    }

    /** Sets the size of the data area.
        @param size Size of the data area. */
    public void setDataAreaLength(int size) {
        if (size < 0) throw new IllegalArgumentException ("Data area size cannot be negative.");
        if (size > this.size) throw new IllegalArgumentException ("Data area size cannot be bigger than size of the whole memory.");
        dataAreaSize = size;
    }

    // Added by HT, 12.10.2004, Koskelo-project. Modiefied by Kohahdus
    // to substract code and data area size 2006-11-23.
    /** Return the number of data references made. */
    public int getMemoryReferences() {
        return memory_references - getCodeAreaSize() - getDataAreaSize();

    }//getMemoryReferences
}
