package fi.hu.cs.titokone;


/** This class tells GUIBrain what the processor has done. RunDebugger 
creates objects from this class and passes them to onwards.*/

public class RunInfo extends DebugInfo{

    /** 	this field is set to true if something is stored to code area of memory */
    private boolean selfChangingCode;

    /** this field contains number representation of changed codeline */
    private int selfChangingBinary;
    /** this field contains symbolic representation of changed codeline */
    private String selfChanged;
    

   /** This field contains the number of operation type. */
   private int operationType;
   /** This field contains the description of operation type */
   private String operationDescription;
   
    /**This field contains line number.*/
    private int lineNumber;
    /** This field contains contents of the line, */
    private String lineContents;
    /** This field contains the command in binary format. */
    private int binary;
   
    /** This array contains the current values of registers 0-7 */
    private int [] registers;
    
    /** This String contains the colon-representation of current line */
    private String binaryString;
    
    /** This String represents the binary / data value of changed code */
    private String changedCodeAreaData;
    
    /** This int represents the number of memoryfetches */
    private int numberOfMemoryfetches;

    /** Old value of program counter */
    private int oldPC;
	/** New value of program counter */    
    private int newPC;
    /** Old value of frame pointer */
    private int oldFP;
    /** New value of frame pointer */
    private int newFP;
    /** Old value of stack pointer */
    private int oldSP;
    /** New value of stack pointer */
    private int newSP;
    
    /** This field contains first operand of the command. */
    private int rj; 
    /** This field contains the value of first operand of the command */
    private int valueOfRj;
    /** This field contains index register. */
    private int ri;
    /** This field contains the value of index register */
    private int valueOfRi;
    /** This field represents the address */
    private int addr;
    /** This field contains the value of address field */
    private int valueAtADDR;

    /** This field contains the value of ALU-operation */
    private int aluResult;
    /** This field contains the representation which bit has the value true after comparing 
          0 - less, 1 - greater, 2 - equal 
    */
    //TO DO: Bittien tila säilyy vertailun jälkeen, päivitetäänkö ainoastaan muutokset GUIBRAINILLE? Jos ylipäänsä tarvitaan...
    private int srBit;
    
	/** This boolean value tells is the operation in or out -operation */
    private boolean externalOperation;
    /** This boolean value is set true if operation is in operation, otherwise false */
    private boolean isIN;
    
    /** This String value contains the name of the device */
    private String deviceName;
    
    /** This value contains the value of the device */
    private int deviceNumber;
    
    /** This value contains the value read or written from / to device */
    private int valueOfDevice;

    /** This value contains the String representation of SVC-operation */
    private String svcOperation;

    /** This value is set to true if state of the memory is changed */
    //TO DO: asetetaanko ainoastaan data-alueelle viitatessa? käytössä on myös selfChangingCode
    private boolean memoryChanged;
    
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
		this.selfChangingCode = false;
		this.memoryChanged = true;
		
	}
    
	
	
	public void setRegisters(int [] registers) {
		this.registers = registers;	
	}
	
    /** This method sets the type of operation performed.
	@param type Type of operation.
	*/
    public void setOperationType(int type){
		this.operationType = type;
    }
	
    /** This method sets the description of operation. Can be used in a comment line */
    @param String-representation of the description
    public void setOperation(String description) {
		this.operationDescription = description;
    }
    
    /** This method sets the binary value of the command.
	@param binary Binary value of the command.
    */
    public void setBinary(String binary){
		this.binaryString = binary;
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
    	this.numberOfMemoryfetches = fetches;
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
		this.changedCodeAreaData = symbolic;
		this.selfChangingBinary = binary;
		this.selfChanged = symbolic;
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
    */
    public void setCompareOperation(int whichBit){
          this.srBit = whichBit;
    }

    /** This method tells is external operation executed
         @return boolean true if command is an external operation 
    */
    public boolean isExternalOp() {
	 	return this.externalOperation;   
    }
    /** This method tells is external operation in or out 
          @return true if external operation is in operation, otherwise false
    */
    
    public boolean isInOp() {
	 	return this.isIn;   
    }
    
    
    /** This method tells info what was read from given device and what was 
	the value.
	@param deviceName Name of the device.
	@param device Number of the device.
	@param value Value read.
    */
    public void setIN(String deviceName, int device, int value){
    	this.externalOperation = true;
		this.isIn = true;
    	this.deviceName = deviceName;
    	this.deviceNumber = device;
    	this.valueOfDevice = value;
    }
    

    /** This method tells info what was written to the  given device and what 
	was the value.
	@param deviceName Name of the device.
	@param device Number of the device.
	@param value Value written.
    */
    public void setOUT(String deviceName, int device, int value){
    	this.externalOperation = true;
		this.isIn = false;
    	this.deviceName = deviceName;
		this.deviceNumber = device;
		this.value = value;
    }
    
    
    /** This method sets what kind of SVC operation was made.
     */
    public void setSVCOperation(String operation){
    	this.svcOperation = operation;
    }
     

    /** This method tells GUIBrain what kind of operation happened.
        @return int value which represents operation type.*/
    public int getOperationtype(){
		return this.operationType;    
	}
    
    /** This method returns both old and new PC, SP and FP.
    @return int[] Array containing pointers.
    */
    public int[] getPointers(){
	   int [] pointers = new int[4];
	   pointers[0] = oldPC;
	   pointers[1] = newPC;
	   pointers[2] = newSP;
	   pointers[3] = newFP;
	   
	   return pointers; 
	}
    
    /** This methot tells GUIBrain how many memoryfetches were made.
	@return int How many fetches were made.
    */
    public int getMemoryfetches(){
		return this.numberOfMemoryfetches;    
	}
    
    /** This method returns the number of the line..
	@return int Integer containing theline number.
    */
    public int getLineNumber(){
		return this.lineNumber;    
	}
    
    /** This method returns the symbolic command found on the line..
	@return String String containing the symbolic command.
    */
    public String getLineContents(){
	    return this.lineContents;
	}
    
    /** This method returns the binary command.
	@return int Integer containing the binary command.
    */
    public String getBinary(){
		return this.binary;    
	}
    
    /** This method tells GUIBrain which registers changed and what are new
	values.
	@return int[] Integer array containing register numbers and new values.
	*/
   	public int[] getRegisters(){
		return this.registers;	
	}
    
	
	/** This method tells are there changed memorylines
		@return true, if state of the memory is changed, otherwise false
	*/
	public boolean memoryChanged() {
		r
    }
	
    /** This method tells GUIBrain which lines in dataarea changed and what are
	new values.
	@return int[] Integer array containing line numbers and new values.
    */
     public object[] whatMemoryLineChanged(){
	    
	}

    
        
    /** This method tells GUIBrain what was result of an OUT command (device 
	and value).
	@return int[] Integer array containing device number and new value.
    */
    
    public int[] whatOUT(){
	    int [] outD = new int[2];
	    outD[0] = this.deviceNumber;
	    outD[1] = this.value;

	    return outD;
	}
    
    /** This method tells GUIBrain what was result of an IN command (device and
     *value.
	@return int[] Integer array containing device number and new value.
    */
    public int[] whatIN(){
		int [] inD = new int[2];
		inD[0] = this.deviceNumber;
		inD[1] = this.value;
		
		return inD;
	}
    
    /** This method returns name of the used device.
	@return String devicename. */
    public String whatDevice(){
		return this.deviceName;    
	}

    /** This method returns value of the first operand.
	@return int[] Integer array containing the number and value of the 
	first operand.
    */
    public int[] getFirstOperand(){
		int [] reg = new int[2];
		reg[0] = this.rj;
		reg[1] = this.valueOfRj;
		
		return reg;
	}

    /** This method returns value of the index registers.
	@return int[] Integer array containing  number and value of the index 
	register.
    */
    public int[] getIndexRegister(){
		int [] reg = new int[2];
		reg[0] = this.ri;
		reg[1] = this.valueOfRi;
		
		return reg;
	}

    /** This method returns value of the ADDR part of the command.
	@return int Integer containing the value of the ADDR part of command.
    */
    public int getADDR(){
		return this.addr;    
	}

    /** This method returns value found at the ADDR.
	@return int Integer containing the value found at ADDR..
    */
    public int getValueAtADDR(){
	    return this.valueAtADDR;
	}

    /** This method returns the result of the ALU operation.
	@return int Integer containing the result.
    */
    public int getALUResult(){
	    return this.aluResult;
	}

    
    /** This method returns type of the SVC operation.
	@return int Integer containing the operation type.
    */
    public String getSVC(){
		return this.svcOperation;    
	}
    
	//TO DO, Guille.
	public String getSymbolUsed() {
    }
    
    
   
} 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

