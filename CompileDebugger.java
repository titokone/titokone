

public class CompileDebugger extends Debugger {
    
    CompileInfo info;
    
    /*------- Compiler instructions -----------*/
    /** This method tells that an EQU was found and it is added to the symboltable.*/
    public void foundEQU(String name, int value){}

    /** This method tells debugger that a DS compiler instruction was found
	and it is added to the symboltable. */
    public void foundDS(String name, int size){ }
    /** This method tells where data area for given DS is in the memory. */
    public void reserveDS(String name, int size, int ADDR){}
 
    /** This method tells debugger that a DC compiler instruction was found
	and it is added to the symboltable. */
    public void foundDC(String name, int value){ }
    /** This method tells where given DC is located in the memory. */
    public void reserveDC(String name, int ADDR){}

    
    public void foundSymbol(String name){}
    public void foundSymbol(String name, int value){}

    /** This method tells that a label was found and it is added to the
     symboltable. */
    public void labelName(String name){}
    /** This method tells that for given label points to given line. */ 
    public void labelADDR(String name, int lineNumber){}

    
    
    public CompileDebugger(){}

    public void firstPhase(String statusMessage, short phase, int lineNumber,
                        boolean lineEmpty){}
    public void firstPhase(String statusMessage, short phase,
			   int lineNumber){}
			   
    
    /** This method informs debugger what was compiled. It creates a new 
     *CompileInfo object.
	 @param symbolic symbolic command line.
	 @param numeric Numeric representation of command.
	 @param machinecommand line as an integer.
	 @param comment */
    
    public void secondPhase(String statusMessage, short phase, int lineNumber,
                        String lineContents, String numeric){
    }
    
    public void finalPhase(String statusMessage, short phase, int lineNumber){}
    
    public void binary(int binary){}
    private void setStatusMessage(String message){}
    private void setComments(String comment){}
    
    
    /** This method returns the created CompileInfo-object. */
    public CompileInfo lineCompiled(){}
}

