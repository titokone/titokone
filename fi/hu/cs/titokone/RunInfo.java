package fi.hu.cs.titokone;


/** This class tells GUIBrain what the processor has done. RunDebugger 
creates objects from this class and passes them to onwards.*/

//TODO: Javadoc

public class RunInfo extends DebugInfo{

   
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

    private int oldPC;
    private int newPC;
    private int oldSP;
    private int oldFP;
    private int newSP;
    private int newFP;
    private int opcode;
    /** This field contains first operand of the command. */
    private int rj; 
    private int valueOfRj;
    /** This field contains index register. */
    private int ri;
    private int valueOfRi;
    private int addr;
    private int valueAtADDR;
    private int valueOfFirstFetch;
    private int valueOfSecondFetch;


    private boolean compareOp;
    private int aluResult;
    private int srBit;
    private boolean compareResult;
   
    private boolean conditionalJump;
    private int whichSRBit;
    private boolean srStatus;


    private boolean externalOperation;
    private String deviceName;
    private int deviceNumber;
    private int value;

    private int svcOperation;

       
    private int[][] changedRegisters;
    private int[][] changedMemory;
    
    
    /** This constructor initializes the RunInfo and sets its starting values.
     @param lineNumber Line number of current line.
     @param lineContents String containing symbolic command.
     @param oldPC value of the old PC.
     @param newPC value of the new PC.
     @param oldSP value of the old SP.
     @param newSP value of the new SP.
     @param oldFP value of the old FP.
     @param newFP value of the old FC.
*/

    public RunInfo(int lineNumber, String lineContents, int oldPC, int newPC,
		   int oldSP, int newSP, int oldFP, int newFP){

    	this.lineNumber = lineNumber;
		this.lineContents = lineContents; 
		this.oldPC = oldPC;
		this.newPC = newPC;
		this.oldSP = oldSP;
		this.newSP = newSP;
		this.oldFP = oldFP;
		this.newFP = newFP;
    
		this.externalOperation = false;
		this.registerChanged = false;	
		this.memoryChanged = false;
		this.selfChangingCode = false;
	}
    
    /** This method sets the type of operation performed.
	@param type Type of operation.
	*/
    public void setOperationType(int type){
		this.operationType = type;
    }

    /** This method sets the binary value of the command.
	@param binary Binary value of the command.
    */
    public void setBinary(int binary){
		this.binary = binary;
    }

    /** this method sets the index register.
	@param register Number of the register.
	@param value Value of the register.
    */
    public void setIndexRegister(int register, int value){
	this.ri = register;
	this.valueOfRi = value;
    }
   
    /** This method sets the first operand.
	@param register Number of the register.
	@param value Value of the register.	
    */
    public void setFirstOperand(int register, int value){
    	this.rj = register;
	this.valueOfRj = value;
    }

    /** This method sets the type of the fetch.
	@param type Type of the fetch.
    */
    public void setFetchType(int type){
    	this.operationType = type;
    }

    /** This method sets the number of fetches.
	@param fetches Number of fetches.
    */
    public void setNumberOfFetches(int fetches){
    	this.numberOfMemoryFetches = fetches;
    }

    /** This method sets the value of the first fetch.
	@param value Value of the first fetch.
     */
    public void setFirstFetch(int value){
    	this.valueOfFirstFetch = value;
    }

    /** This method sets the value of the second fetch.
	@param value Value of the second fetch.
*/
   public void setSecondFetch(int value){
   	this.valueOfSecondFetch = value;
   }


    /** This method sets the value of ADDR.
	@param ADDR Int containing the ADDR.
    */
    public void setADDR(int addr){
    	this.addr = addr;
    }

    /** This method sets the value found at ADDR.
	@param value Value found at the ADDR.
    */
    public void setValueAtADDR(int value){
    	this.valueAtADDR = value;
    }
    /** This method tells info that something was written to the codearea and
	what it was and its possible symbolic presentation.
	@param line Number of the line.
	@param binary New value written.
	@param symbolic Possible symbolic command.
    */
    public void setChangedCodeAreaData(int line, int binary, String symbolic){
    	this.selfChangingCode = true;
	if(symbolic != null)
    		this.changedCodeAreaData = symbolic;
    }
    
    /** This sets the result of performed ALU operation
	@param result Result of the operation.
    */
    public void setALUResult(int result){
    	this.aluResult = result;
    }
    
    /** This method tells info that a compare operation was made and what SR 
	bit was changed to what value.
	@param whichBit Number of the bit.
	@param newValue New value of the bit.
    */
    public void setCompareOperation(int whichBit, boolean newValue){
    	this.srBit = whichBit;
	this.srStatus = true; //onko tarpeellinen?
			      // voiko useita bittejä asettaa kerralla?
    }
    
    /** This method tells info what was read from given device and what was 
	the value.
	@param deviceName Name of the device.
	@param device Number of the device.
	@param value Value read.
    */
    public void setIN(String deviceName, int device, int value){
    	this.externalOperation = true;
	this.deviceName = deviceName;
    	this.deviceNumber = device;
    	this.value = value;
    }
    

    /** This method tells info what was written to the  given device and what 
	was the value.
	@param deviceName Name of the device.
	@param device Number of the device.
	@param value Value written.
    */
    public void setOUT(String deviceName, int device, int value){
    	this.externalOperation = false;
	this.deviceName = deviceName;
	this.device = device;
	this.value = value;
    }
    
    /** This method tells info that a conditional jump was made and what was 
	checked SR bit and its value.
	@param whichBit Int containig number of the bit.
	@param status  Value of the bit.
    */
    public void setConditionalJump(int whichBit, boolean status){
    
    }


    /** This method sets what kind of SVC operation was made.
     */
    public void setSVCOperation(int operation){
    	this.svcOperation = operation;
    }


    /** This returns information if conditional jump was made.
	@return boolean True if conditional jump was made.
    */
    public boolean getConditionalJump(){}
 
    /** This method returns information which SR bit was used.
	@return int Number of the SR bit.
    */
    public int getWhichBit(){}
    /** This method returns value of the SR bit.
	@return boolean Value of the bit.
    */
    public boolean getBit(){}

    /** This method tells GUIBrain what kind of operation happened.
        @return int value which represents operation type.*/
    public int whatOperationHappened(){}
    
    /** This method returns both old and new PC, SP and FP.
    @return int[] Array containing pointers.
    */
    public int[] getPointers(){}
    
    /** This methot tells GUIBrain how many memoryfetches were made.
	@return int How many fetches were made.
    */
    public int getMemoryfetches(){}

    /** This method tells what kind of memoryfetch was made.
    @return int What kind of memorygetch was done.
    */
    public int getFetchType(){}
    
    /** This method returns value of the first memoryfetch.
	@return int Integer containing the value of fetch.
    */
    public int getValueOfFirstFetch(){}

    /** This method returns value of the second memoryfetch.
	@return int Integer containing the value of fetch.
    */
    public int getValueOfSecondFetch(){}

    /** This method returns the number of the line..
	@return int Integer containing theline number.
    */
    public int getLineNumber(){}
    
    /** This method returns the symbolic command found on the line..
	@return String String containing the symbolic command.
    */
    public String getLineContents(){}
    
    /** This method returns the binary command.
	@return int Integer containing the binary command.
    */
    public int getBinary(){}
    
    /** This method tells GUIBrain which registers changed and what are new
	values.
	@return int[] Integer array containing register numbers and new values.
*/
    public int[] whatRegisterChanged(){}
    
    /** This method tells GUIBrain which lines in dataarea changed and what are
	new values.
	@return int[] Integer array containing line numbers and new values.
    */
    public int[] whatMemoryLineChanged(){}
    
    /** This method tells GUIBrain what was result of an OUT command (device 
	and value).
	@return int[] Integer array containing device number and new value.
    */
    public int[] whatOUT(){}
    
    /** This method tells GUIBrain what was result of an IN command (device and
     *value.
	@return int[] Integer array containing device number and new value.
    */
    public int[] whatIN(){}
    
    /** This method returns name of the used device.
	@return String devicename. */
    public String whatDevice(){}

    /** This method returns value of the first operand.
	@return int[] Integer array containing the number and value of the 
	first operand.
    */
    public int[] getFirstOperand(){}

    /** This method returns value of the index registers.
	@return int[] Integer array containing  number and value of the index 
	register.
    */
    public int[] getIndexRegister(){}

    /** This method returns value of the ADDR part of the command.
	@return int Integer containing the value of the ADDR part of command.
    */
    public int getADDR(){}

    /** This method returns value found at the ADDR.
	@return int Integer containing the value found at ADDR..
    */
    public int getValueAtADDR(){}

    /** This method returns the result of the ALU operation.
	@return int Integer containing the result.
    */
    public int getALUResult(){}


    /** This method tells GUIBrain that if a compare operation was made.
	@return boolean telling if operation was made.
    */
    public boolean getCompareOP(){}

    /** This method returns both which SR bit was set and what is new value.
	0 represents false and 1 true.
	@return An integer array containing which SR bit was changed and it's
	new value.*/
    public int[] getCompareResult(){}

    /** This method returns type of the SVC operation.
	@return int Integer containing the operation type.
    */
    public int getSVC(){}
       
    
    
   
} 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

