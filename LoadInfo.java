package fi.hu.cs.titokone;

/**This class provides info about the loading of a program. It has the code area and the data 
area as a string array. The code is in its numeric form as well as a list of symbolic operation 
codes with comments and symbols in place. LoadInfo also stores the values of FP and SP.*/
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
  /** This fiels stores the status message to be shown to user in GUI. */
  private String statusMessage;
  
  /**
     @param codeArea Has the opcodes as MemoryLine array
     @param dataArea Has the data part as MemoryLine array
     @param initSP The initial value of SP
     @param initFP The initial value of FP
     @param statusMessage Message to GUI to be displayed at the status bar.
    */ 
  public LoadInfo(MemoryLine[] codeArea, MemoryLine[] dataArea, 
		  int initSP, int initFP, String statusMessage) { }
  
  /**@return String array that contains the symbolic operation codes.
   */
  public String[] getSymbolicCommands() { }

  /**@return Int array that contains operation codes in their numeric form.
    */
  public int[] getBinaryCommands() { }

  /**@return Int array that contains the data segment of a program in memory.
    */
  public int[] getData() { }

  /**@return The value of the Stack pointer (SP) after the program is loaded into memory.
    */
  public int getSP() { }

  /**@return The value of the Frame pointer (FP) after the program is loaded into memory.
    */
  public int getFP() { }
  
  /**@return The message for GUI to be displayed at the status bar.
    */
  public String getStatusMessage() { }

}
