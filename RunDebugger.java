/** This class produces objects describing what has changed due to the last
    command having been run. */
public class Debugger{ 
    // define methods like 'ALU happened, parameters x, y, z' and
    // a 'command cycle complete', which returns the delta object for 
    // the gui. Contains eg. the commentary. Processor can then return it 
    // back to ControlBridge.
   
    private String language = 'English';

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
    
    public Debugger(String language){}

    /** This method tells debugger that a new cycle has been started. It
     initiates parameter values and stores old PC and IR. */
    public void cycleStart(int lineNumber, String lineContents, int oldPC, 
			   int newPC, int oldSP, int newSP, int oldFP,
			   int newFP){ }



    public void memoryFetchType(int i){ }
    public void numberOfFetches(int i){ }

    public void setOperationType(int i){}
 
    
    public void runCommand(int opcode, int firstOperand, int memoryFetches,
			   int indexRegister, int ADDR){ }

    public void selfChangingCode(int lineNumber, int binary, 
				 String newContents){}

    public void setNoOperation(){}
    
    public void setValueAtADDR(int value){}

    public void setChangedRegisters(int[][] registers){}

    public void setChangedMemoryLines(int[][] lines){}

    public void setALUResult(int result){}

    public void setCompareResult(int whichBit, boolean status){}

    public void setIN(int device, int value){}

    public void setOUT(int device, int value){}

    public void setConditionalJump(int whichSR, boolean status){}
    

















    //vanhaa tauhkaa, älä välitä


	/** This method tells debugger that a value was loaded to a given 
	    register.
	*/
    public void immediate(int register, int value){ }

	/** This method tells debugger that direct memoryfetch was made. */
    public void direct(int register, int where, int value){ }
    
    public void directRegister(int toregister, int fromregister, int where,
                                int value){ }

    public void indirectRegister(int toregister, int fromregister,){ }
    
    /** This method tells debugger that an indexed direct memoryfetch was 
	used. */
    public void indexedDirect(int toregister, int index, int where, 
			      int value){ }

    /** This method tells debugger that an indexed direct from register 
        memoryfetch was used. */
    public void indexedDirectRegister(int toregister, int index, 
                                        int fromregister, int where, 
				      int value){ }
 	
    /** This method tells debugger that an indirect memoryfetch was used. */
    public void indirect(int toregister, int first, int second, int value){ }
   
    /** This method tells debugger that an indexed indirect memoryfetch was 
     used. */
    public void indexedIndirect(int toregister, int index, int first, 
                                int second, int value){ }

	

    /* ---- Running --- */  
    public void noOperation(){ }

    /** Store/Load */
    public void store(int Ri, int ADDR, int address){}
    
    public void load(int Ri, int Rj, int ADDR, int value){}

    public void in(int device, int register, int value){ }

    public void out(int device, int register, int value){ }

    /* ALU */

    public void add(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void sub(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void mul(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void div(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void mod(int Rj, int Ri, int ADDR, int value, int newRj){}


    public void and(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void or(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void xor(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void shl(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void shr(int Rj, int Ri, int ADDR, int value, int newRj){}
    public void shra(int Rj, int R1, int ADDR, int value, int newRj){}


    
    public void compare(int SR, int Ri, int Rj){ }
 
    public void jump(int where){ }

    public void conditionalJump(int SRIndex, int where, boolean conditionTrue)
    {}
 

    /** This method tells debugger that a command cycle was completed and
        it should return a debuginfo package.
	@returns DebugInfo
	*/


    public RunInfo cycleComplete(){ }


}
