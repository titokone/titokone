package fi.hu.cs.titokone;

 /** This class is used to tell GUIBrain what compiler has done at the moment.
    CompileDebugger creates objects from this class and passes them to the 
    GUIBrain.
*/
public class CompileInfo extends DebugInfo {
  // Variables common to both rounds.
  
    public static final short FIRST_ROUND = 0;
    public static final short FINALIZING_FIRST_ROUND =1;
    public static final short SECOND_ROUND = 2;
    public static final short FINALIZING = 3; 
 
    private short phase;

  /** This field contains the number of the line being processed or 
      -1 during the finalizing phase. */
  private int lineNumber;

  /** This field hold the contents of a compiled line. */
  private String lineContents;
 
  /** These arrays contain codelines, data and the symboltable after first round. */
  String[] instructions;
  String[] data;
  String[][] symbolTable;

  /** This field is by default false, but if the compiled line was 
      empty (or consisted of whitespace only), the true value here says 
      that other checks can be skipped. */
  private boolean lineEmpty = false;

  /** This field contains the name of a symbol found on this line 
      from the parameter field. It will be set to "" if no symbol 
      was found on this line. See also symbolValue, symbolDefined and
      label variables. */
  private String symbolName = "";

  /** This field is true if the symbol's value was also defined on this
      line. In this case, the value is set in symbolValue. */
  private boolean symbolDefined = false;

  /** This field contains the value of the symbol found. If symbolDefined
      is false, the value in this field should be ignored. */
  private int symbolValue;

  /** This field contains the address of the symbol.*/
  private int symbolAddress;

  /** This field contains information if a symbol was foud.*/
  private boolean symbolFound;
  
  /** This field contains information if a label was found.*/
  private boolean labelFound;

  /** This field contains the name of a label found from the beginning of
      this line. It will be set to "" if no label was found on this line.*/
  private String labelName = "";
  
    /** This field contains line as a binary.*/
  private int lineBinary;

    /** This field is true if found label was defined before. */
  private boolean labelDefined = false;

    /** This field contains value of the current label.*/
  private int labelValue;

  // Variables for finalization phase.

  /** This field contains true if we have gone through all DS and DC 
      commands and are setting FP and SP accordingly, or false if 
      we are still going through DS and DC commands. */
  private boolean finalFinal;

/*---------- Constructor ----------*/
 
    /** This is normal constructor for CompileInfo. It sets initial values for
	phase, lineNumber and lineContents.
	@param phase Short indicating which phase is going on.
	@param lineNumber Integer value of the current line number.
	@param lineContents String containing symbolic command.
    */
  public CompileInfo(short phase, int lineNumber, String lineContents) {
	this.phase = phase;
	this.lineNumber = lineNumber;
	this.lineContents = lineContents;
  }
  
    /** This constructor is used when no actual line is compiled but other
	actions are made like finalizing the rounds.
	@param phase short indicating which phase is going on.
    */
  public CompileInfo(short phase) {
 	this.phase = phase;
  }

/*---------- set-methods ----------*/

    
    /** This method sets lineEmpty value to true. */
    public void setLineEmpty(){
	lineEmpty = true;
    }

    /** This method sets symbolFound field.*/
    public void setSymbolFound(){
	symbolFound = true;
    }

    /** This method sets the name of a found symbol.
	@param name String containing the symbol name.
    */
    public void setSymbolName(String name){
	symbolName = name;
    }
    
    /** This method sets the name of a found symbol and its value.
	@param name Name of the symbol.
	@param value Value of the symbol.
	@param address Memoryaddress of the symbol.
    */
    public void setSymbolName(String name, int value) {
	symbolName = name;
	symbolValue = value;
	symbolDefined = true;
    }

    /** This method sets labelFound field.*/
    public void setLabelFound(){
	labelFound = true;
    }
 
    /** This method sets the name of a found label and sets the labelDefined 
	field to true.
	@param name Name of the label.
    */
    public void setLabelName(String name) { 
    	labelName = name;
    }
    
    /** This method tells CompileInfo that a given label was defined and it's
	value. It sets labelDefined field to true.
	@param name String containing the name of the label.
	@param value An integer value where the label points.
    */
    public void setLabelDefined(String name, int value) { 
	labelName = name;
	labelValue = value;
	labelDefined = true;
    }
    
    /** This method tells what integer represents the compiled line of the
	code.
	@param binary An integer value representing the symbolic command.
    */
    public void setLineBinary(int binary) {
	lineBinary = binary;
    }

    /** This method sets the boolean field finalFinal to true.
     */
    public void setFinal() {
	finalFinal = true;
    }

    /** This method sets given line to given value.
	@param lineNumber Number of the line.
	@param value New value for the line.
    */
    public void setMemoryline(int lineNumber, String value) {
	this.lineNumber = lineNumber;
	lineContents = value;
    }

    /** This method sets memory array to contain all codelines after first
	round of compilation. It contains only symbolic lines.
	@param instructions array containing code-lines.
    */
    public void setInstructions(String[] instructions) {
	this.instructions = instructions;	
    }

    /** This method sets data area of a memory to contain all codelines after first
	round of compilation. It contains variable values.
	@param data array containing data-area.
    */
    public void setData(String[] data) {
	this.data = data;	
    }

    public void setSymbolTable(String[][] symbolTable) {
	this.symbolTable = symbolTable;
    }

/*---------- get-methods ----------*/

    /** This message tells that an empty line or line containing only 
	whitespaces was compiled.
    */
    public boolean getLineEmpty() {
	return lineEmpty;
    }
    
    /** This method returns current phase as a short. 0 for first round, 1 for
	finalizing first round, 2 for second and 3 for final.
	@return Short containing phase.
    */
    public short getPhase() {
	return phase;
    }

    /** This method return symbolic contents of the line.
	@return String containing symbolic representation of a compiled line. 
    */
    public String getLineContents() {
	return lineContents;
    }

    /** This method returns found symbolname.
	@return String containing the name.
    */
    public String getSymbolName() {
	return symbolName;
    }

    /** This method returns true if a symbol was defined.
	@return boolean containing information if symbol was defined.
    */
    public boolean getSymbolDefined() {
	return symbolDefined;
    }

    /** This method returns true if a label was found.
	@return boolean containing information if label was found.
    */
    public boolean getLabelFound() {
	return labelFound;
    }

    /** This method returns true if a symbol was found.
	@return boolean containing information if symbol was found.
    */
    public boolean getSymbolFound() {
	return symbolFound;
    }

    /** This method returns value of current symbol.
	@return An integer containing symbol's value.
    */
    public int getSymbolValue() {
	return symbolValue;
    }

    /** This method returns compiled binary machinecommand represented as an
	integer value.
	@return An integer representing machine command.
    */
    public int getLineBinary() {
	return lineBinary;
    }

    /** This method returns the name of the current label.
	@return Name of the current label.
    */
    public String getLabelName() { 
	return labelName;
    } 

    /** This method returns value of the current label.
	@return An integer containing value of the label. */
    public int getLabelValue() {
	return labelValue;
    } 

    /** This method returns true if field finalFinal is set.
	@return Boolean.
    */
    public boolean getFinalPhase() {
	return finalFinal;
    }

    /** This method returns codelines of the memory after compiler has finished first 
	round of compilation. This array contains all codelines other than  white
	spaces, empty lines and variable definitions has been removed.
	@return String array containing symbolic lines.
    */
    public String[] getInstructions() {
	return instructions;
    }

    /** This method returns the variable values after compiler has finished first 
	round of compilation. This array contains all codelines but all white
	spaces and empty lines has been removed.
	@return String array containing variable values.
    */
    public String[] getData() {
	return data;
    }

    /**	This method returns the symboltable gathered during the first round.
	@return Array representing the symboltable. Each entry has 2 values.
	First position (0) tells the name of the symbol and the second one 
	(1) holds the value of that symbol. "" If it wasn't set.
    */
    public String[][] getSymbolTable() {
	return symbolTable;
    }

}

