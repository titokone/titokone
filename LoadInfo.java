/**This class provides info about the loading of a program. It has the code area and the data 
area as a string array. It also stores the values of FP and SP.*/
public class LoadInfo extends DebugInfo {
  /** This field contains the contents of the code area after the loading
      is complete. */
  private MemoryLine[] codeArea;
  /** This field contains the contents of the data area after the loading
      is complete. */
  private MemoryLine[] dataArea;
  /** This field contains the value to be stored to the SP register. */
  private int initSP;
  /** This field contains the value to be stored to the FP register. */
  private int initFP;
  
  /** 

@returns LoadInfo that contains programs code as binary and as a list of symbolic operation codes 
with lines untouched (symbols in place as well as comments)*/
  public LoadInfo(MemoryLine[] codeArea, MemoryLine[] dataArea, 
		  int initSP, int initFP, String statusMessage) { }
  
  /**@returns String array that contains the symbolic operation codes.
   */
  public String[] getSymbolicCommands() { }

  /**@returns int array that contains operation codes in their numeric form.
    */
  public int[] getBinaryCommands() { }

  /**@returns int array that contains the data segment of a program in memory.
    */
  public int[] getData() { }

  /**@returns The value of the Stack pointer (SP) after the program is loaded into memory.
    */
  public int getSP() { }

  /**@returns The value of the Frame pointer (FP) after the program is loaded into memory.
    */
  public int getFP() { }
  
}
