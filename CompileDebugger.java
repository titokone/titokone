package fi.hu.cs.titokone;

/** This class is used by compiler when it compiles code. For each line 
    compiled compiler asks CompileDebugger to create a CompileInfo object and 
    passes it to the GUIBrain. */



public class CompileDebugger {
    
    private CompileInfo info;
    private String language = "English";

    private String statusMessage;
    private String comments;



    /*------- Compiler instructions -----------*/
    /** This method tells that an EQU was found and it is added to the 
	symboltable.*/
    public void foundEQU(String name, int value){}

    /** This method tells debugger that a DS compiler instruction was found
	and it is added to the symboltable. */
    public void foundDS(String name){ }
    /** This method tells where data area for given DS is in the memory. */
    public void reserveDS(String name, int size, int ADDR){}
 
    /** This method tells debugger that a DC compiler instruction was found
	and it is added to the symboltable. */
    public void foundDC(String name){ }
    /** This method tells where given DC is located in the memory. */
    public void reserveDC(String name, int value, int ADDR){}

    /** This method tells debugger that a symbol was found on a line but it is 
	unknown.*/
    public void foundSymbol(String name){}
    /** THis Method  tells debugger that a known symbol was found and it's 
	value is given as a partameter. */
    public void foundSymbol(String name, int value){}

    /** This method tells that a label was found and it is added to the
     symboltable. */
    public void foundLabel(String name){}
    /** This method tells that for given label points to given line. */ 
    public void foundLabel(String name, int lineNumber){}

    
    
    public CompileDebugger(String Language){}

    public void firstPhase(int lineNumber, boolean lineEmpty){}
    public void firstPhase(int lineNumber, String lineContents){}
    public void firstPhase(){}		   
    public void finalFirstPhase(String[] memory){}





    /** This method informs debugger what was compiled. It creates a new 
     *CompileInfo object.

	 */
    
    public void secondPhase(int lineNumber, String lineContents, int binary,
			     String IR){}


    public void finalPhase(){}
    

    public void setInitPointers(int sp, int fp){}
    
    public void setMemoryline(int line, int value){}
  

    private void setStatusMessage(String message){}
    
    /** This private method sets collected comments to the CompileInfo.*/
    private void setComments(String comment){}
    
    
    /** This method returns the created CompileInfo-object. It calls private 
     method setComments and then returns CompileInfo.*/
    public CompileInfo lineCompiled(){}
}

