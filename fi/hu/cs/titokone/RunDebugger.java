package fi.hu.cs.titokone;

/** This class produces objects describing what has changed due to the last
    command having been run. */
public class RunDebugger{ 

    public static final short NO_OPERATION = 0;
    public static final short DATA_TRANSFER_OPERATION = 1;
    public static final short ALU_OPERATION = 2;
    public static final short COMP_OPERATION = 3;
    public static final short BRANCH_OPERATION = 4;
    public static final short SUB_OPERATION = 5;
    public static final short STACK_OPERATION = 6;
    public static final short SVC_OPERATION = 7;
  
    
    public static final short SVC_HALT  = 11;
    public static final short SVC_READ  = 12;
    public static final short SVC_WRITE = 13;
    public static final short SVC_TIME  = 14; 
    public static final short SVC_DATE  = 15;
    
    
    public static final short CRT = 0;
    public static final short KBD = 1;
    public static final short STDIN = 6;
    public static final short STDOUT = 7;
    
 //nämä pois
    public static final short IMMEDIATE = 0; // 23(R1)
    public static final short DIRECT = 1; // =23
    public static final short INDIRECT = 2 //@23
    public static final short DIRECT_REGISTER = 3; // R1 = 0(R1)
    public static final short INDIRECT_REGISTER = 4; //@R1 = @0(R1)
    public static final short INDEXED_DIRECT = 5; // =vakio(r2)
    public static final short INDEXED_IMMEDIATE= 6; // vakio(r2)
    public static final short INDEXED_INDIRECT = 7; //@vakio(r2)


       
    private int oldPC;
    private int oldSP;
    private int oldFP;
    
    private Integer registers[];
    
    private RunInfo info;
    private String comments;
    private String statusMessage;
    private int[] pointers;
    private int lineNumber;
    private String lineContents;
    
    /** This constructor initializes the RunDebugger. After initialization it
	waits until processor starts new running cycle.
    */
    public RunDebugger(){
    	oldPC = 0;
		oldFP = 0;
		oldSP = 0;
    }

    /** This method tells debugger that a new cycle has been started. It
     initiates parameter values and stores old PC and IR.
     @param lineContents String containing symbolic command.
     @param oldPC value of the old PC.
     @param newPC value of the new PC.
     @param oldSP value of the old SP.
     @param newSP value of the new SP.
     @param oldFP value of the old FP.
     @param newFP value of the old FC.
    */
    public void cycleStart(String lineContents,
			   int newPC, int newSP, int newFP){ 
	
					   			   
		this.info = new RunInfo(lineContents, oldPC, newPC, oldSP, newSP,
		                   oldFP, newFP);
		 
		this.oldPC = newPC;
		this.oldFP = newFP;
		this.oldSP = newSP;	   
    }


   /** This method sets the value of first memory fetch.
     @param value Value found in memory.*/
    public void setFirstFetch(int value){
    	this.info.setFirstFetch(value);
    }

    /** This method sets the value of second memory fetch.
     @param value Value found in memory.*/
    public void setSecondFetch(int value){
    	this.info.setSecondFetch(value);
    }

    /** This method tells what kind of operation was made. 
	@param i Type of operation. */ 
    public void setOperationType(int opcode){
    	switch(opcode) {
		
			case NO_OPERATION:
				this.info.setOperationType("No operation");
			break;
			
			case DATA_TRANSFER_OPERATION:
				this.info.setOperationType("Data transfer");
			break;
			
			case ALU_OPERATION:
				this.info.setOperationType("ALU-operation");
			break;
			
			case COMP_OPERATION:
				this.info.setOperationType("Comparing");
			break;
			
			case BRANCH_OPERATION:
				this.info.setOperationType("Branching");
			break;
			
			case SUB_OPERATION:
				this.info.setOperationType("Subroutine");
			break;
			
			case STACK_OPERATION:
				this.info.setOperationType("Stack-operation");
			break;
		
			case SVC_OPERATION:
				this.infosetOperationType("Supervisor call");
		    break;
		}
	    
    }
 
    /** This method tells what was operation run and its parts.
	@param opcode Operation code of the command.
	@param firstOperand First operand.
	@param valueOfFirstOperand Value of the register.
	@param memoryFetchType Type of memoryfetch.
	@param indexRegister Index register.
	@param valueOfIndex Value of the index register.
	@param ADDR ADDR part of the command.
	@param numberOfFetches How many fetches were made.
	@param binaryString String containing operation splitted into parts 
	and presented as integers parted with doubledots. 
    */
    public void runCommand(int opcode, int firstOperand, 
			   int valueOfFirstOperand, int indexRegister, int valueOfIndex, int addr, 
			   int numberOfFetches, String binaryString){
	
		  
		  this.info.setFirstOperand(firstOperand, valueOfFirstOperand);
		  this.info.setIndexRegister(indexRegister, valueOfIndex);
		  this.info.setADDR(addr);	 
		  this.info.setNumberOfFetches(numberOfFetches);
		  this.info.setBinary(binaryString);
			    
    }

    /** This method tells debugger that something was written in the codearea.
	@param lineNumber number of the line where something was written.
	@param binary Binary value written.
	@param newContents String containing possible new symbolic command.
    */
    public void selfChangingCode(int lineNumber, int binary, 
				 String newContents) {
		//???
		
    
    }

    
    /** This method tells debugger that a NOP was executed.
     */
    public void setNoOperation(){
    	//???
    }

    /** This method tells debugger what value was found from the ADDR part of 
	the command.
	@param value int containing the value.
    */
    public void setValueAtADDR(int value){
    	//???
	    
    }

    /** This method tells debugger that one or more registers were changed.
	First cell contains number of the register and second the new value..
	@param registers Array containing new values.
    */
    public void setRegisters(Integer [] registers){
    	this.info.setRegisters(registers);	
    }

    /** This method tells debugger that one or more memorylines were changed.
	First cell contains number of the line and second the new value..
	@param lines Array containing new values.
    */
    public void setChangedMemoryLines(int[][] lines){
    
    }
    
    /** This method sets the result of ALU operation. 
	@param result Value of result. */
    public void setALUResult(int result){
    	this.info.setALUResult(result);
    }

    /** This method tells what was the result of compare operation.
	@param whichBit Number of SR bit changed.
	@param status New status of the bit.
    */
    public void setCompareResult(int whichBit){
    		this.info.setCompareOperation(whichbit);
    }

    /** This method tells debugger that something was read from the given
	device. Devices are STDIN and KBD.
	@param deviceNumber Number of the device.
	@param value Value written.
    */
    public void setIN(int deviceNumber, int value){
    	switch(deviceNumber) {
	    	
	    	case KBD:
	    		this.info.setIN("Keyboard", KBD, value);
	    	break;
	    	
	    	case STDIN:
	    		this.infosetIN("Standard input", STDIN, value);
	    	break;
	    		
    	}
    	
    	
    }

    /** This method tells debugger that something was written to the given
	device. Devices are STDOUT and CRT.
	@param deviceNumber Number of the device.
	@param value Value written.
    */
    public void setOUT(int deviceNumber, int value){
    	switch(deviceNumber) {
	    	
	    	case CRT:
	    		this.info.setOUT("Display", CRT, value);
	    	break;
	    	
	    	case STDOUT:
	    		this.info.setOUT("Standard output", STDOUT, value);
	    	break;
    	}
    	
	    
    }

     /** This method tells debugger which SVC operation was done.
	@param operation Int containing operation type.
    */
    public void setSVCOperation(int operation){
    	switch(operation) {
    		case SVC_HALT:
    			this.info.setSVCOperation("Halt");
    	    break;
    	    
    	    case SVC_READ:
    	    	this.info.setSVCOperation("Read");
    	    break;
    	    
    	    case SVC_WRITE:
    	    	this.info.setSVCOperation("Write");
    	    break;
    	    
    	    case SVC_TIME:
    	    	this.info.setSVCOperation("Time");
    	    break;
    	    
    	    case SVC_DATE:
    	    	this.info.setSVCOperation("Date");
    	    break;
	    	
	    }
    }

    /** This method tells debugger if command was CALL or EXIT operation and
	it comments accordingly. True stands for CALL and false for EXIT.
	@param type Boolean containing information which operation was done.
    */
    public void setSubOperation(int type){
    
    }
}
