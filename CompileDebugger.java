

public class CompileDebugger extends Debugger {

    
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

    
  

    /** This method tells that a label was found and it is added to the
     symboltable. */
    public void labelName(String name){}
    /** This method tells that for given label points to given line. */ 
    public void labelADDR(String name, int lineNumber){}

    
    
    public CompileDebugger(){}

    public void newLine(String statusMessage, short phase, int lineNumber,
                        boolean lineEmpty){}
   
    
    /** This method informs debugger what was compiled. It creates a new 
     *CompileInfo object.
	 @param symbolic symbolic command line.
	 @param numeric Numeric representation of command.
	 @param machinecommand line as an integer.
	 @param comment */

    public void newLine(String statusMessage, short phase, int lineNumber,
                        String lineContents){
    }
    
    public void finalPhase(String statusMessage, short phase, int lineNumber){}
    
    public void binary(int binary){}
    public void setStatusMessage(String message){}
    public void setComments(String comment){}
    
    
    /** This method returns the created CompileInfo-object. */
    public CompileInfo lineCompiled(){}
}

