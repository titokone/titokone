package fi.hu.cs.titokone;

/** This class is used by compiler when it compiles code. For each line 
    compiled compiler asks CompileDebugger to create a CompileInfo object and 
    passes it to the GUIBrain. */



public class CompileDebugger {
    
    /** This field contains current CompileInfo object. */
    private CompileInfo info;

    /** This field contains current phase of compilation. */
    private int phase;
    /** This field contains current statusmessage. */
    private String statusMessage;
    /** This field contains current comments.This string is created by
     * debugger when its different methods are called.*/
    private String comments;



    /*------- Compiler instructions -----------*/
    /** This method tells that an EQU was found and it is added to the 
	symboltable.
	@param name String containing name of the symbol.
	@param value Int containing the value.
    */
    public void foundEQU(String name, int value){}

    /** This method tells debugger that a DS compiler instruction was found
	and it is added to the symboltable.
	@param name String containing name of the symbol.
	@param value Int containing the value.
    */
    public void foundDS(String name, int value){ }

    /** This method tells where data area for given DS is in the
	memory. This method is used when compiles has checked all
	lines in the first phase and has called firstPhase()
	method. This method sets definingDS field in CompileInfo to
	true and then sets corresponding fields.
	@param name String containing name of the symbol.
	@param size Int containing the size of DS
	@param ADDR Starting address of the segment..
    */
    public void reserveDS(String name, int size, int ADDR){}
 
    /** This method tells debugger that a DC compiler instruction was found
	and it is added to the symboltable. 
	@param name String containing name of the symbol.
        @param isNew Boolean containing information if the DC is new or is it 
	already in symboltable..
    */
    public void foundDC(String name, boolean isNew){ }
    /** This method tells where given DC is located in the
	memory. This method is used when compiles has checked all
	lines in the first phase and has called firstPhase()
	method. This method sets definingDC field in CompileInfo to
	true and then sets corresponding fields.
	@param name String containing name of the symbol.
	@param value Int containing the value.
	@param ADDR Address of the DC.
    */
    public void reserveDC(String name, int value, int ADDR){}

    /** This method tells debugger that a symbol was found on a line but it is 
	unknown.
	@param name String containing name of the symbol.
        @param isNew Boolean containing the information if the symbol was new..
    */
    public void foundSymbol(String name, boolean isNew){}

    /** THis Method  tells debugger that a known symbol was found and it's 
	value is given as a paratmeter. 
	@param name String containing name of the symbol.
	@param value Int containing the value.
    */
    public void foundSymbol(String name, int value){}

    /** This method tells that a label was found and it is added to the
     symboltable. Boolean new tells debugger that if it was new.
     @param name String containing name of the symbol.
     @param isNew Boolean containing the onformation if the symbol was new.
    */
    public void foundLabel(String name, boolean isNew){}

    /** This method tells that for given label points to given line.
	@param name String containing name of the symbol.
	@param lineNumber Int containing the linenumber of the label.
        @param isNew Boolean containing the information if the label was new.
    */
    public void foundLabel(String name, int lineNumber, boolean isNew){}

    /** This method tells that a DEF  was found and it is added to the
     symboltable. 
     @param name String containing name of the symbol.
     @param value String containing the value.
    */
    public void foundDEF(String name, String value){}
    
    /** This is the only constructor for CompileDebugger. It is called
	when compiler is created.*/
    public CompileDebugger(){}


    /** This method tells debugger that first round of compilation is
	in progress and the line compiled is empty. It creates
	CompileInfo-object and sets its phase to 1 and lineEmpty to true.
	@param lineNumber Number of the compiled line.
	@param lineEmpty True if line was empty.
    */
    public void firstPhase(int lineNumber, boolean lineEmpty){} 
    
    /** This method tells debugger that first round of compilation is
    in progres and line wasn't empty. It creates CompileInfo object
    and sets its phase to 1, lineNumber and lineContents fields.
    @param lineNumber Number of the compiled line.
    @param lineContents String containing the symbolic command.
    */
    public void firstPhase(int lineNumber, String lineContents){} 

    /** This method is used when all lines are checked in the first
    phase of compilation and compiler is setting symbols and
    labels. 
    */
    public void firstPhase(){}
    
    /** This method is used when all DC and DS are defined and
    compiler is ready to move to the second phase. Compiler tells
    debugger what are actual code lines and then what is dataArea in 
    memory and what it contains. GUIBrain then redraws GUI and writes
    codelines leaving binary cells empty. Then it draws data area
    where number of first data line is i+codeArea.length
    @param codeArea String array containing codelines.
    @param dataArea Integer array containing data, or nulls where 
    the data has no values.
    */
    public void finalFirstPhase(String[] codeArea, Integer[] dataArea){}




    /** This method tells debugger that the second round of
    compilation is in progress. It creates CompileInfo object and sets
    its phase to 3, lineContents and binary fields. It also creates a
    local comment and the IR is !!!KATENOIDAAN!!! after it.
    @param lineNumber number of the compiled line.
    @param lineContents Contents of the line.
    @param binary Line as a binary (presented as an integer).
    @param IR String containing binary command splitted into parts
    which are presented as integers.
    */
    public void secondPhase(int lineNumber, String lineContents, int binary,
			     String IR){}

    /** This method tells debugger that final phase of compilation is
    in progress. It creates CompileInfo object and sets its phase to 4.
    */
    public void finalPhase(){}
    
    /** This method sets the initial values of SP and FP.
	@param sp Value of the SP,
	@param fp Value of the FP.
    */
    public void setInitPointers(int sp, int fp){}
    
    /** This method sets given memorylines to the given values. It is
	used in the finalizing first phase of compilation. Compiler
	uses it to tell GUIBrain  
	@param lines An int array where first cell is linenumber and second
	is the new value.
    */
    public void setMemoryline(int[][] lines){}
  

   /** This method returns the created CompileInfo-object. It sets
    comments in the CompileInfo and then returns it.*/
    public CompileInfo lineCompiled() {}
}

