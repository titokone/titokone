package fi.hu.cs.titokone;

import java.util.LinkedList;

/** This class produces objects describing what has changed due to the last
    command having been run. */
public class RunDebugger{ 

	
	/** constant numerical value for operation type NOP */
    public static final short NO_OPERATION = 0;
    /** constant numerical value for Data transfer operation type  */
    public static final short DATA_TRANSFER_OPERATION = 1;
    /** constant numerical value for ALU-operation type  */
    public static final short ALU_OPERATION = 2;
    /** constant numerical value for Comparing operation type */
    public static final short COMP_OPERATION = 3;
    /** constant numerical value for branching operation type */
    public static final short BRANCH_OPERATION = 4;
    /** constant numerical value for subroutines operation type  */
    public static final short SUB_OPERATION = 5;
    /** constant numerical value for stack operation type  */
    public static final short STACK_OPERATION = 6;
    /** constant numerical value for SVC operation type  */
    public static final short SVC_OPERATION = 7;
  
    /** constant short for supervisor call Halt */
    public static final short SVC_HALT  = 11;
    /** constant short for supervisor call Read */
    public static final short SVC_READ  = 12;
    /** constant short for supervisor call Write */
    public static final short SVC_WRITE = 13;
    /** constant short for supervisor call Time */
    public static final short SVC_TIME  = 14; 
    /** constant short for supervisor call Date */
    public static final short SVC_DATE  = 15;
    
    /** constant short for CRT-device */
    public static final short CRT = 0;
    /** constant short for KBD-device */
    public static final short KBD = 1;
    /** constant short for STDIN-device */
    public static final short STDIN = 6;
    /** constant short for STDOUT-device */
    public static final short STDOUT = 7;
    
    /** this field represents the old value of program counter */
    private int oldPC;
    /** this field represents the old value of stack pointer */
    private int oldSP;
    /** this field represents the old value of frame pointer */
    private int oldFP;
    
    /** This field represents the Integer-array of registers R0-R7 */
    private Integer registers[];
    /** Runinfo for each command line of the program */
    private RunInfo info;
    /** Comment-line generated to runinfo */
    private String comments;
    /** This field represents the status of the program */
    private String statusMessage;
    /** List of changed memory lines */
    private LinkedList changedMemoryLines = new LinkedList();
    
        
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
    public void cycleStart(String lineContents, int newPC, int newSP, int newFP){ 
		info = new RunInfo(lineContents, oldPC, newPC, oldSP, newSP,
		                   oldFP, newFP);
		
		oldPC = newPC;
		oldFP = newFP;
		oldSP = newSP;	   
    }

    
    // TO DO: Sini, miten kielikäännökset syötetään, mikä muoto täällä?  Tarvitaanko operaatioiden String-esitystä
    //  tässä muodossa vai syötetäänkö suoraan debuggerissa osaksi kommenttia?

    /** This method tells what kind of operation was made. 
          can be used in the comment 
	@param i Type of operation. */ 
    public void setOperationType(int opcode){
    		
			this.info.setOperationType(opcode);
			
		switch(opcode) {
			case NO_OPERATION:
				this.info.setOperation("No operation");
			break;
			
			case DATA_TRANSFER_OPERATION:
				this.info.setOperation("Data transfer");
			break;
			
			case ALU_OPERATION:
				this.info.setOperation("ALU-operation");
			break;
			
			case COMP_OPERATION:
				this.info.setOperation("Comparing");
			break;
			
			case BRANCH_OPERATION:
				this.info.setOperation("Branching");
			break;
			
			case SUB_OPERATION:
				this.info.setOperation("Subroutine");
			break;
			
			case STACK_OPERATION:
				this.info.setOperation("Stack operation");
			break;
		
			case SVC_OPERATION:
				this.infosetOperation("Supervisor call");
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
    public void selfChangingCode(int lineNumber, int binary, String newContents) {
		this.info.setChangedCodeAreaData(linenumber, binary, newContents);
    }

        
    /** This method tells debugger what value was found from the ADDR part of 
	the command.
	@param value int containing the value.
    */
    public void setValueAtADDR(int value){
    	this.info.setADDR(value);
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
    public void addChangedMemoryLine(int row, MemoryLine changedMemoryLine){
        Object[] entry = new Object[2];
        entry[0] = new Integer(row);
        entry[1] = changedMemoryLine;
        changedMemoryLines.add (entry);
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

    /** This method return the current runinfo
        after the line is executed
        @return RunInfo of the current line
    */
    public RunInfo cycleEnd() {
        info.setChangedMemoryLines (changedMemoryLines);
        changedMemoryLines = new LinkedList();
        return info;
    }
}
