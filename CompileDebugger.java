

public class CompileDebugger extends Debugger {

    public CompileDebugger(){}

    
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

    
    /** This method tells debugger that an EQU compiler instruction was found
	and it is added to the symboltable . */
    public void foundEQU(String name, int value){ }


    /** This method tells that an index was found and it is added to the symboltable. */
    public void indexFound(String name){}
    /** This method tells that for given index points to given line. */ 
    public void indexADDR(String name, int memoryline){}


    /** This method informs debugger what was compiled.
	 @param symbolic symbolic command line.
	 @param numeric Numeric representation of command.
	 @param machinecommand line as an integer.
	 @param comment */

    public void compiledLine(String symbolic, String numeric, int machinecommand, String comment){ }
    
    public void compiledLine(int line, String symbolic, String numeric, int machinecommand, String comment){ }

}

