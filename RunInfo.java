/** */

public class RunInfo extends DebugInfo{
    
    
    public static final short NOOPERATION = 0;
    public static final short DATA_TRANSFER_OPERATION = 1;
    public static final short ALU_OPERATION = 2;
    public static final short JUMP_OPERATION = 3;
    public static final short STACK_OPERATION = 4;
    public static final short SUB_OPERATION = 5;
    public static final short SERVICE_OPERATION = 6;
    
    private int operationType;
    /**This field contains line number.*/
    private int lineNumber;
    /** This field contains contents of the line, */
    private String lineContents;
    /** This field contains the command in binary format. */
    private int binaryCommand;
    
    
    private String statusMessage;
    private String comments;
    
    
    private int memoryFetchType;
    private int oldSP;
    private int oldFP;
    private int SP;
    private int FP;
    private int commandValue;
    /** This field contains first operand of the command. */
    private int Rj; 
    /** This field contains index register. */
    private int Ri;
    private String index;
    
    
    private int aluResult;
    
    
    
    private int[] registerArray;
   
    
    
    
    public RunInfo(int lineNumber, String lineContents, int binary, int oldSP,
                   int oldFP, int SP, int FP){}
    
    /** This method tells GUIBrain what kind of operation happened.
        @returns int value which represents operation type.*/
    public int whatOperationHappened(){}
    
    /** This method tells GUIBrain what was statusMessage. */
    public String returnStatusMessage(){}
    
    /** This method tells GUIBrain what the comments were. */
    public String returnComments(){}   
    
    /** This method returns both old and new SP and FP. */
    public int[] returnPointers(){}
    
    /** */
    Public int returnLineNumber(){}
    
    /** */
    public String returnLineContents(){}
    
    /** */
    public int returnBinaryCommand(){}
    
    /** This method tells GUIBrain which registers changed and what is new
     value.*/
    public int[] whatRegisterChanged(){}
    
    /** This method tells GUIBrain which line in data area changed and what is
     new value.*/
    public int[] whatMemoryLineChanged(){}
    
    /** This method tells GUIBrain what was result of an OUT command (device and 
     value).*/
    public int[] whatOUT(){}
    
    /** This method tells GUIBrain what was result of an IN command (device and
     *value.*/
    public int[] whatIN(){}
    
    public int[] 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}