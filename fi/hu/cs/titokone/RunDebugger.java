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
    
    
    /** constant String for comment line memory addressing */
    private static final String DIRECT = "direct";
    /** constant String for comment line memory addressing */
    private static final String DIRECT_ADDRESSING = "direct addressing";
    /** constant String for comment line memory addressing */
    private static final String INDIRECT_ADDRESSING = "indirect addressing";
    /** String for memory part of comment */    
    private String memoryComment;
    /** String array for the comment message */
    private String[] parameters = new String[3];
    private int deviceValue;
        
    /** Runinfo for each command line of the program */
    private RunInfo info;
    /** List of changed memory lines */
    private LinkedList changedMemoryLines = new LinkedList();
    /** Compare bit tells compare status of status register.
        0 - greater, 1 - equal, 2 - less. */
    private int compareBit=-1;
    
    /** This constructor initializes the RunDebugger. */
    public RunDebugger(){
    }

    /** This method tells debugger that a new cycle has been started.
        @param lineNumber Tells the number of the current command line in memory.
        @param lineContents String containing symbolic command.
    */
    public void cycleStart(int lineNumber, String lineContents){ 
        this.parameters[1] = lineContents;
	info = new RunInfo(lineNumber, lineContents);
    }

    /** This method sets the type of operation. 
        @param opcode Operation code of command. */ 
    public void setOperationType(int opcode){
        info.setOperationType(opcode);
    }
    
    /** This method tells what was operation run and its parts.
    @param command TTK91 command.
    */
    public void runCommand (int command) {
        // cut up the command
        int opcode = command >>> 24;                             
        int Rj = (command&0xE00000) >>> 21;  
        int M  = (command&0x180000) >>> 19;                      
        int Ri = (command&0x070000) >>> 16;   
        int ADDR = (short) (command&0xFFFF);
        
        info.setBinary (command);
        info.setFirstOperand (Rj);
        info.setIndexRegister (Ri);
        info.setADDR (ADDR);
        info.setNumberOfFetches (M);
        info.setColonString (opcode + ":" + Rj + ":" + M + ":" + Ri + ":" + ADDR);
    	
	switch(M) {
	   case 0:
	   	memoryComment = DIRECT;
           break;
	   
	   case 1:
	        memoryComment = DIRECT_ADDRESSING;
           break;
	   
	   case 2:
	   	memoryComment = INDIRECT_ADDRESSING;
	   break;
	   	
	}
	
	this.parameters[0] = opcode + ":" + Rj + ":" + M + ":" + Ri + ":" + ADDR + " ";
        this.parameters[2] = "R" + Ri;
    
    }
    
    /** This method tells debugger what value was found from the ADDR part of 
    the command.
    @param value int containing the value.
    */
    public void setValueAtADDR(int value){
        info.setValueAtADDR(value);
    }

    /** This method tells debugger that one or more registers were changed.
    If value has not changed, value is null,
      otherwise changed value is in current index
    @param registers Array containing new values.
    */
    public void setRegisters(int[] registers){
        info.setRegisters(registers);   
    }

    /** This method tells debugger that one or more memorylines were changed.
    First cell contains number of the line and second the new value..
    @param row The index of the changed line in memory.
    @param changedMemoryLine The changed memory line.
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
        info.setALUResult(result);
    }

    /** This method tells what was the result of compare operation.
    @param whichBit Number of SR bit set.
    */
    public void setCompareResult(int whichBit){
        compareBit = whichBit;
    }
    
    /** This method sets value of second memory fetch. Indirect memory
        accessing mode needs two memory fetches.
    @param secondFetchValue Value which have got at second memory fetch. */
    public void setSecondFetchValue (int secondFetchValue) {
        info.setSecondFetchValue(secondFetchValue);
    }
    
    /** This method tells debugger that something was read from the given
    device. Devices are STDIN and KBD.
    @param deviceNumber Number of the device.
    @param value Value written.
    */
    public void setIN(int deviceNumber, int value){
        this.deviceValue = value;
	
	switch(deviceNumber) {
            case Processor.KBD:
                info.setIN(new Message("keyboard").toString(), Processor.KBD, value);
            break;
            
            case Processor.STDIN:
                info.setIN(new Message("stdin").toString(), Processor.STDIN, value);
            break;
        }
    }

    /** This method tells debugger that something was written to the given
    device. Devices are STDOUT and CRT.
    @param deviceNumber Number of the device.
    @param value Value written.
    */
    public void setOUT(int deviceNumber, int value){
        this.deviceValue = value;
	
	switch(deviceNumber) {
            
            case Processor.CRT:
                info.setOUT(new Message("display").toString(), Processor.CRT, value);
            break;
            
            case Processor.STDOUT:
                info.setOUT(new Message("stdout").toString(), Processor.STDOUT, value);
            break;
        }
        
        
    }

     /** This method tells debugger which SVC operation was done.
    @param operation Int containing operation type.
    */
    public void setSVCOperation(int operation){
        switch(operation) {
            case SVC_HALT:
                info.setSVCOperation("Halt");
            break;
            
            case SVC_READ:
                info.setSVCOperation("Read");
            break;
            
            case SVC_WRITE:
                info.setSVCOperation("Write");
            break;
            
            case SVC_TIME:
                info.setSVCOperation("Time");
            break;
            
            case SVC_DATE:
                info.setSVCOperation("Date");
            break;
            
        }
    }
    
    /**
   	Sets the comment message of the current RunInfo. 
   */
       
    private void setComments() {
    	if(info.isExternalOp()) {
		String [] param = new String[4];
		param[0] = parameters[0];
		param[1] = parameters[1];
		param[2] = parameters[2];
		param[3] = "" + this.deviceValue;
		
		info.setComments(new Message("{0}{1} Indexing {2}, "+ memoryComment+", value {3}.", param).toString());	
	}	
	else
		info.setComments(new Message("{0}{1} Indexing {2}, "+ memoryComment +".", parameters).toString());
    }
    
    /** Sets value of new PC. 
        @param newPC Value of new PC. */
    public void setNewPC (int newPC) {
        info.setNewPC (newPC);
    }

    /** This method return the current runinfo
        after the line is executed
        @return RunInfo of the current line. */
    public RunInfo cycleEnd() {
        this.setComments();
	info.setChangedMemoryLines (changedMemoryLines);
        info.setCompareOperation (compareBit);
        changedMemoryLines = new LinkedList();
        return info;
    }
}
