/** This data class contains information about the memory. */
public class RandomAccessMemory implements Memory {

    // Memory implementations:
    public int getLength() {  }
    public int getValue(int memorySlot) {  }
    // public int[] getSymbolTable() // to be replaced with something smarter
    public int[] getMemory() {  } // Don't forget to clone it.
    public int[] getCodeArea() {  } // no promises about self-changing code.
    public int[] getDataArea() {  }
}
