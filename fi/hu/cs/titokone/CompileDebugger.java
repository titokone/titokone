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

    /** This field contains current comment. This string is created by
     * debugger when its different methods are called.*/
    private String comment;

/*----------- Constructor -----------*/

    /** This is the only constructor for CompileDebugger. It is called
	when compiler is created.*/
    public CompileDebugger() {    }

/*----------- Compiler instructions -----------*/

    /** This method tells that an EQU was found and it is added to the 
	symboltable.
	@param name String containing name of the symbol.
	@param value Int containing the value.
    */
    public void foundEQU(String name, int value) {
	info.setSymbolFound();
	info.setSymbolName(name, value);
    }

    /** This method tells debugger that a DS compiler instruction was found
	and it is added to the symboltable.
	@param name String containing name of the symbol.
    */
    public void foundDS(String name) { 
	info.setSymbolFound();
	info.setSymbolName(name);
    }

    /** This method tells debugger that a DC compiler instruction was found
	and it is added to the symboltable. 
	@param name String containing name of the symbol.
        @param isNew Boolean containing information if the DC is new or is it 
	already in symboltable..
    */
    public void foundDC(String name) {
   	info.setSymbolFound();
	info.setSymbolName(name);
    }

    /** This Method  tells debugger that a symbol was used as an 
	address. 
	@param name String containing name of the symbol.
    */
    public void foundSymbol(String name) {

	boolean wasReservedWord = false;

	if (name.equalsIgnoreCase("CRT")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 0);
		wasReservedWord = true;
	} 

	if (name.equalsIgnoreCase("KBD")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 1);
		wasReservedWord = true;
	} 

	if (name.equalsIgnoreCase("STDIN")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 6);
		wasReservedWord = true;
	} 

	if (name.equalsIgnoreCase("STDOUT")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 7);
		wasReservedWord = true;
	} 

	if (name.equalsIgnoreCase("HALT")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 11);
		wasReservedWord = true;
	} 

	if (name.equalsIgnoreCase("READ")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 12);
		wasReservedWord = true;
	} 

	if (name.equalsIgnoreCase("WRITE")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 13);
		wasReservedWord = true;
	} 

	if (name.equalsIgnoreCase("TIME")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 14);
		wasReservedWord = true;
	} 

	if (name.equalsIgnoreCase("DATE")) {  	
		info.setSymbolFound();
		info.setSymbolName(name, 15);
		wasReservedWord = true;
	} 

	if (!wasReservedWord) {
		info.setSymbolFound();
		info.setSymbolName(name);
	}

    }


    /** This method tells that a label was found and it is added to the
     symboltable. Boolean new tells debugger that if it was new.
     @param name String containing name of the symbol.
     @param isNew Boolean containing the onformation if the symbol was new.
    */
    public void foundLabel(String name, boolean isNew) {
	//TODO this can't happen! label must have a line number
    }

    /** This method tells that for given label points to given line.
	@param name String containing name of the symbol.
	@param lineNumber Int containing the linenumber of the label.
    */
    public void foundLabel(String name, int lineNumber) {
	info.setLabelDefined(name, lineNumber);
    }

    /** This method tells that a DEF  was found and it is added to the
     symboltable. 
     @param name String containing name of the symbol.
     @param value String containing the value.
    */
    public void foundDEF(String name, String value) {
//TODO	info.setComment();
//	info.setSymbolFound();
//	info.setSymbolName(name, value);
    }

    /**	This method sets the compiled value of a line during
	the second round of compilation.     */
    public void setBinary(int binary) {
	info.setLineBinary(binary);
    }
    
    /** This method tells debugger that first round of compilation is
    in progres and line wasn't empty. It creates CompileInfo object
    and sets its phase to 1, lineNumber and lineContents fields.
    @param lineNumber Number of the compiled line.
    @param lineContents String containing the symbolic command.
    */
    public void firstPhase(int lineNumber, String lineContents) {
	info = new CompileInfo(CompileInfo.FIRST_ROUND, lineNumber, lineContents);
    } 

    /** This method is used when all lines are checked in the first
    phase of compilation and compiler is setting symbols and
    labels. 
    */
    public void firstPhase() {
	info = new CompileInfo(CompileInfo.FINALIZING_FIRST_ROUND);
    }
    
    /** This method is used when all DC and DS are defined and
    	compiler is ready to move to the second phase. Compiler tells
    	debugger what are code lines and then what is dataArea in memory
    	and what it contains. GUIBrain then redraws GUI and writes
    	codelines leaving binary cells empty. Then it draws data area
    	where number of first data line is codeArea.length
    	@param codeArea String array containing codelines.
    	@param dataArea String array containing data.
    	@param symbolTable 2-dimensional String array containing the symbol table.
    */
    public void finalFirstPhase(String[] codeArea, String[] dataArea, String[][] symbolTable) {
	info.setInstructions(codeArea);
	info.setData(dataArea);
	info.setSymbolTable(symbolTable);
    }

    /**	This method sets the comment to the compileInfo. 
    */
    public void setComment(String message) {
	info.setComments(message);
    }

    /**	This method sets the status info to the compileInfo. 
    */
    public void setStatusMessage(String message) {
	info.setStatusMessage(message);
    }

    /** This method tells debugger that the second round of
    compilation is in progress. It creates CompileInfo object and sets
    its phase to 3, lineContents and binary fields. It also creates a
    local comment and the IR is cathenated after it.
    @param lineNumber number of the compiled line.
    @param lineContents Contents of the line.
    @param binary Line as a binary (presented as an integer).
    @param IR String containing binary command splitted into parts
    which are presented as integers.
    */
    public void secondPhase(int lineNumber, String lineContents) {
	info = new CompileInfo(CompileInfo.SECOND_ROUND, lineNumber, lineContents);
    }

    /** This method tells debugger that final phase of compilation is
    in progress. It creates CompileInfo object and sets its phase to 4.
    */
    public void finalPhase() {
	info = new CompileInfo(CompileInfo.FINALIZING);
    }
    
    /** This method returns the created CompileInfo-object. It sets
    comments in the CompileInfo and then returns it.*/
    public CompileInfo lineCompiled() {
	return info;
    }
}

