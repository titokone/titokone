/** This class produces objects describing what has changed due to the last
    command having been run. */
public class Debugger extends Translatable{ 
    // define methods like 'ALU happened, parameters x, y, z' and
    // a 'command cycle complete', which returns the delta object for 
    // the gui. Contains eg. the commentary. Processor can then return it 
    // back to ControlBridge.
    /** Constructor for Debugger. 
     */
    public static final short NOOPERATION = 0;
    public static final short BASIC_OPERATION = 1;
    public static final short ALU_OPERATION = 2;
    public static final short JUMP_OPERATION = 3;
    public static final short OTHER_OPERATION = 4;
    
    public Debugger(){}

    /** This method tells debugger that a new cycle has been started. It
     initiates parameter values and stores old PC and IR. */
    public void cycleStart(int lineNumber, int oldPC, int newPC, int IR, int SP,
                            int FP, String lineContents){ }

/*-------- Memoryfetch types ---------*/
// joitain vielä puutuu
    public void memoryFetchType(int i){ }

	/** This method tells debugger that a value was loaded to a given register.
	*/
    public void immediate(int register, int value){ }

	/** This method tells debugger that direct memoryfetch was made. */
    public void direct(int register, int where, int value){ }
    
    public void directRegister(int toregister, int fromregister, int where,
                                int value){ }

    public void indirectRegister(int toregister, int fromregister, int  ...){ }
    
    /** This method tells debugger that an indexed direct memoryfetch was used. */
    public void indexedDirect(int toregister, int index, int where, int value){ }

    /** This method tells debugger that an indexed direct from register 
        memoryfetch was used. */
    public void indexedDirectRegister(int toregister, int index, 
                                        int fromregister, int where, int value){ }
 	
    /** This method tells debugger that an indirect memoryfetch was used. */
    public void indirect(int toregister, int first, int second, int value){ }
   
    /** This method tells debugger that an indexed indirect memoryfetch was 
     used. */
    public void indexedIndirect(int toregister, int index, int first, 
                                int second, int value){ }

	

    /* ---- Running --- */  
    public void noOperation(){ }

    /** Store/Load */
    public void store(String command, int Ri, int Rj){}
    
    public void load(String command, int Ri, int Rj){}

    public void in(String command, int device, int value){ }

    public void out(String command, int device, int value){ }

    /* ALU */

    public void alu(String command, String symbol, int result){}


		      
    public void compare(String command, int SR, int Ri, int Rj){ }
 
    public void jump(String command, int where){ }

    public void conditionalJump(int SRIndex,  String command, int where, 
                                boolean conditionTrue) {}
 
    /* Other */

    public void call(String command, int newSP, int newFP){}

    public void exit(String command, int newSP, int newFP){}

    public void push(String command, int newSP, int value){}
   
    public void pop(String command, int newSP, int value){}

    public void pushr(String command, int newSP, int[] values){}

    public void popr(String command, int newSP, int[] values){}

    public void svc(String command, int serviceid){}
  

	/** This method tells debugger that a command cycle was completed and it 
   should return a debuginfo package.
	@returns DebugInfo
	*/


    public RunInfo cycleComplete(){ }


}
