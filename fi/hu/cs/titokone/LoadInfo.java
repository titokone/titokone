package fi.hu.cs.titokone;

/**This class provides info about the loading of a program. It has the
code area and the data area as a MemoryLine array. The code is in its 
numeric form as well as a list of symbolic operation codes with comments and
symbols in place, if known. LoadInfo also stores the values of FP and 
SP.*/
public class LoadInfo extends DebugInfo {
  /** This field contains the contents of the code area after the loading
      is complete. */
  private MemoryLine[] codeArea;

 /** 
   This field contains the symbotable
 */
  private SymbolTable symbols;


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
     @param symbols has the symboltable 
     @param initSP The initial value of SP
     @param initFP The initial value of FP
     @param statusMessage Message to GUI to be displayed at the status bar.
    */ 
  public LoadInfo(MemoryLine[] codeArea, MemoryLine[] dataArea, 
                  SymbolTable symbols, int initSP, int initFP, String statusMessage) { 
  
    this.codeArea = codeArea;
    this.dataArea = dataArea;
    this.symbols = symbols;
    this.initSP = initSP;
    this.initFP = initFP;
    this.statusMessage = statusMessage;
    }
  
  /**@return String array that contains the symbolic operation codes.
   */
  public String[] getSymbolicCommands() { 
    String[] retString = new String[codeArea.size];
    for (int i=0 ; i<codeArea.size ; i++) {
      retString[i] = codeArea[i].getSymbolic();
    }
    return retString;
  }

  /**@return Int array that contains operation codes in their numeric form.
    */
  public int[] getBinaryCommands() { 
    int[] retInt = new int[codeArea.size];
    for (int i=0 ; i<codeArea.size ; i++) {
      retInt[i] = codeArea[i].getBinary();
    }
    return retInt;
  }
  
  /**@return Int array that contains the data segment of a program in memory.
    */
  public int[] getData() { 
    int[] retInt = new int[dataArea.size];
    for (int i=0 ; i<dataArea.size ; i++) {
      retInt[i] = dataArea[i].getBinary();
    }
    return retInt;
  }

  /**@return The value of the Stack pointer (SP) after the program is loaded into memory.
    */
  public int getSP() {
    return initSP;
  }

  /**@return The value of the Frame pointer (FP) after the program is loaded into memory.
    */
  public int getFP() { 
    return initFP;
  } 
  
   /**@return The message for GUI to be displayed at the status bar.
    */
  public String getStatusMessage() { 
    return statusMessage;
  }

}
