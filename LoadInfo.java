/** */
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
  
  /** */
  public LoadInfo(MemoryLine[] codeArea, MemoryLine[] dataArea, 
		  int initSP, int initFP, String statusMessage) { }
  
  public String[] getSymbolicCommands() { }
  public int[] getBinaryCommands() { }
  public int[] getData() { }
  public int getSP() { }
  public int getFP() { }
  
}
