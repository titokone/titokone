package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.*;

/**
 * previous class changed to an interface to make it possible to
 * hide implementation of memory ( to make memory mapped devices)
 */
public interface RandomAccessMemory
        extends TTK91Memory {


    /**
     * Returns the size of the code area.
     *
     * @return Size of the code area.
     */
    public int getCodeAreaSize();

    /**
     * Returns the size of the data area.
     *
     * @return Size of the data area.
     */
    public int getDataAreaSize();

    /**
     * Returns memory line at given slot.
     *
     * @param index Index to memory.
     * @return Memory line at given slot.
     */
    public MemoryLine getMemoryLine(int index);


    /**
     * This method returns a copy of all the memory lines.
     *
     * @return An array containing all the memory lines.
     */
    public MemoryLine[] getMemoryLines();


    //! wtf is this doing here.. move to constructor or something

    /**
     * Changes the symbol table stored in this class. It is not
     * used by RandomAccessMemory, but can be returned as a HashMap.
     *
     * @param symbols The new symboltable to store here.
     */
    public void setSymbolTable(SymbolTable symbols);


    /**
     * Sets new memory line to given memory slot.
     *
     * @param index      Index to memory.
     * @param memoryLine New memory line which will replace the old.
     */
    public void setMemoryLine(int index, MemoryLine memoryLine)
            throws TTK91AddressOutOfBounds;

    //! this crap should probably be moved to  some other interface or
    //! utility class

    /**
     * Sets the size of the code area.
     *
     * @param size Size of the code area.
     */
    public void setCodeAreaLength(int size);

    /**
     * Sets the size of the data area.
     *
     * @param size Size of the data area.
     */
    public void setDataAreaLength(int size);

    // Added by HT, 12.10.2004, Koskelo-project. Modiefied by Kohahdus
    // to substract code and data area size 2006-11-23.

    /**
     * Return the number of data references made.
     */
    public int getMemoryReferences();
}
