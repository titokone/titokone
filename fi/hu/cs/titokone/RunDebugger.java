package fi.hu.cs.titokone;

/** This class produces objects describing what has changed due to the last
    command having been run. */
public class RunDebugger{ 
    // define methods like 'ALU happened, parameters x, y, z' and
    // a 'command cycle complete', which returns the delta object for 
    // the gui. Contains eg. the commentary. Processor can then return it 
    // back to ControlBridge.
   

    public static final short NOOPERATION = 0;
    public static final short DATA_TRANSFER_OPERATION = 1;
    public static final short ALU_OPERATION = 2;
    public static final short JUMP_OPERATION = 3;
    public static final short STACK_OPERATION = 4;
    public static final short SUB_OPERATION = 5;
    public static final short EXTERNAL_OPERATION = 6;
    public static final short SVC_OPERATION = 7;
  
 
    public static final short IMMEDIATE = 0;
    public static final short DIRECT = 1;
    public static final short DIRECT_REGISTER = 2;
    public static final short INDIRECT_REGISTER = 3;
    public static final short INDEXED_DIRECT = 4;
    public static final short INDEXED_INDIRECT= 5;
    public static final short INDEXED_DIRECT_REGISTER = 6; 
    public static final short INDEXED_INDERECT_REGISTER = 7;


    private RunInfo info;
    private String comments;
    private String statusMessage;
    
    public RunDebugger(){}

    /** This method tells debugger that a new cycle has been started. It
     initiates parameter values and stores old PC and IR. */
    public void cycleStart(int lineNumber, String lineContents, int oldPC, 
			   int newPC, int oldSP, int newSP, int oldFP,
			   int newFP){ }


    /** This method tells what kind of memoryfetch was made.
     @param i Type of fetchs.*/
    public void memoryFetchType(int i){ }

    /** This method tells haw many fetches were made.
     @param i Number of fetches.*/
    public void numberOfFetches(int i){ }
    
    /** */
    public void setFirstFetch(int value){}

    /** */
    public void setSecondFetch(int value){}

    /** This method tells what kind of operation was made. 
	@param i Type of operation. */ 
    public void setOperationType(int i){}
 
    
    public void runCommand(int opcode, int firstOperand, int memoryFetches,
			   int indexRegister, int ADDR, String binaryString){ }

    public void selfChangingCode(int lineNumber, int binary, 
				 String newContents){}

    

    public void setNoOperation(){}
    
    public void setValueAtADDR(int value){}

    public void setChangedRegisters(int[][] registers){}

    public void setChangedMemoryLines(int[][] lines){}
    
    /** This method sets the result of ALU operation. 
	@param result Value of result. */
    public void setALUResult(int result){}

    public void setCompareResult(int whichBit, boolean status){}

    public void setIN(String deviceName, int devicenumber, int value){}

    public void setOUT(String DeviceName, int deviceNumber, int value){}

    public void setConditionalJump(int whichSR, boolean status){}
    
    public void setSVCOperation(int operation){}















}
