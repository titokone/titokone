/** This class tells GUIBrain what the processor has done. RunDebugger 
creates objects from this class and passes them to onwards.*/

//KESKEN varsinkin javadocin osalta

public class RunInfo extends DebugInfo{

       
    public static final short NOOPERATION = 0;
    public static final short DATA_TRANSFER_OPERATION = 1;
    public static final short ALU_OPERATION = 2;
    public static final short JUMP_OPERATION = 3;
    public static final short STACK_OPERATION = 4;
    public static final short SUB_OPERATION = 5;
    public static final short SERVICE_OPERATION = 6;
 
    public static final short IMMEDIATE = 0;
    public static final short DIRECT = 1;
    public static final short DIRECT_REGISTER = 2;
    public static final short INDIRECT_REGISTER = 3;
    public static final short INDEXED_DIRECT = 4;
    public static final short INDEXED_INDIRECT= 5;
    public static final short INDEXED_DIRECT_REGISTER = 6; 
    public static final short INDEXED_INDERECT_REGISTER = 7;
   
    private boolean registerChanged;
    private boolean memoryChanged;
    private boolean selfChangingCode;


   /** This field contains the operation type. */
    private int operationType;
    /**This field contains line number.*/
    private int lineNumber;
    /** This field contains contents of the line, */
    private String lineContents;
    /** This field contains the command in binary format. */
    private int binary;
   
   
    private String changedCodeAreaData;
    
    private int memoryFetchType;
    private int numberOfMemoryfetches;
    private int oldSP;
    private int oldFP;
    private int SP;
    private int FP;
    private int opcode;
    /** This field contains first operand of the command. */
    private int Rj; 
    /** This field contains index register. */
    private int Ri;
    private int ADDR;

    private String index;
    
    
    private int aluResult;
    
    
    
    private int[] registerArray;
   
    
    
    
    public RunInfo(int lineNumber, String lineContents, int oldPC, int newPC
		   int oldSP, int newSP, int FP){}
    
    public void setBinary(int binary){}

    public void setIndexRegister(int register){}
   
    public void setChangedCodeAreaData(String symbolic){}
    

    /** This method tells GUIBrain what kind of operation happened.
        @returns int value which represents operation type.*/
    public int whatOperationHappened(){}
    
    /** This method returns both old and new SP and FP. */
    public int[] returnPointers(){}
    
    /** This methot tells GUIBrain how many memoryfetches were made. */
    public int returnMemoryfetches(){}
    /** This method tells what kind of memoryfetch was made.*/
    public int returnFetchType(){}
    
    /** */
    Public int returnLineNumber(){}
    
    /** */
    public String returnLineContents(){}
    
    /** */
    public int returnBinary(){}
    
    /** This method tells GUIBrain which registers changed and what is new
	value.*/
    public int[] whatRegisterChanged(){} //??
    
    /** This method tells GUIBrain which line in data area changed and what is
	new value.*/
    public int[] whatMemoryLineChanged(){}
    
    /** This method tells GUIBrain what was result of an OUT command (device 
	and value).*/
    public int[] whatOUT(){}
    
    /** This method tells GUIBrain what was result of an IN command (device and
     *value.*/
    public int[] whatIN(){}

    /** This method returns all registers stored into an array. 
     @return int[] Array containing registers.*/
    public int[] returnAllRegisters(){}

       
    
    
   
} 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

