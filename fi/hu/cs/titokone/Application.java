package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.TTK91Application;
import fi.hu.cs.ttk91.TTK91NoKbdData;
import fi.hu.cs.ttk91.TTK91NoStdInData;
import java.util.logging.Logger;
import java.util.StringTokenizer;

/** This class represents a compiled TTK-91-application. It also contains 
    information about what has gotten printed during its running and 
    what it should be reading as various inputs during its run. */
public class Application implements TTK91Application {
    /** This field lists the delimiters accepted as any-combination 
	delimitation markup in a string intended for either keyboard
	or stdin input. */
    public static final String DELIMITERS = " \n\r\t\f,.:;";

    /** This array contains the code to the application; one command
	per line. The initial FP value can be calculated from 
	code.length - 1. If the code area is empty, this field contains 
	a zero-length array. */
    private MemoryLine[] code;
    /** This array contains the initial data area of the application,
	determined at compile time by DS and DC pseudocommands. The
	initial SP value can be determined from code.length +
	initialData.length - 1. (It points to the last reserved memory
	address, since there is no "top of stack" yet.) If the data
	area is empty, this field contains a zero-length array. 
        For reserved but uninitialized data rows, the array contains 
        null values. */
    private MemoryLine[] initialData;
    /** This field stores the local symbol table of the application. */
    private SymbolTable symbols;

    /** Stdout and crt store the outputs of this application. */
    private String stdout, crt;
    /** Stdincontent and kbdcontent store the possible preset input 
	of this application, and stdinpointer and kbdpointer point to
	the position that should be read next. */
    private int[] stdincontent = new int[0], kbdcontent = new int[0];
    private int stdinpointer = 0, kbdpointer = 0;

    /** This constructor sets up a new Application. If an application is 
	compiled from source code, the compiler will know when various
	symbols are used. This constructor enables the compiler or other
	code/data source to store the symbolic commands as well as the 
	binary into instances of MemoryLine, which can contain either 
	only the integer values representing the command or both the 
	integer value and the symbolic form as a string. This way eg. a
	command of the value 18874378, which means STORE R1, 10, can be
	stored also as "STORE R1, LUKU" (where LUKU has been defined to be 
	a symbol for 10) to appear more readable to a human user.
	If you do not have this symbolic data available, eg. because the
	application is being parsed from its binary form, you can use the 
	other constructor instead.
	@param code An array containing the compiled code as MemoryLines. 
	The MemoryLines represent both the integer and the symbolic form
	of the commands. Including the symbolic form is optional. This 
	value may be null or of length 0 in the unlikely case that there 
	is no code area.
	@param data An array containing the initial data area and its
	contents as MemoryLines. The MemoryLines represent both the
	integer and the symbolic form of commands, but especially in
	the case of data, including the symbolic form is
	optional. This value may be null or of length 0 if there is no
	data area.
	@param symbols An instance of SymbolTable, containing the 
	symbol table for this application. This value can be null if 
	there are no local symbols available. (Locally, an empty 
	symboltable is created.) */
    public Application(MemoryLine[] code, MemoryLine[] data, 
		       SymbolTable symbols) {
	if(code != null)
	    this.code = code;
	else
	    this.code = new MemoryLine[0];
	if(data != null)
	    this.data = data;
	else
	    this.data = new MemoryLine[0];
	if(symbols != null)
	    this.symbols = symbols;
	else
	    this.symbols = new SymbolTable();
    }

    /** This method returns the (initial) code area of the application. 
	Any self-modifying code in the application will have no 
	effect on the return value; changes in the memory representation 
	of the code and in the initial code are thus separated.
	@return An array containing the code area of the application. The 
	array will be of length 0 if for some reason this application has
	no code area. */
    public MemoryLine[] getCode() {
	return code.clone();
    }

    /** This method returns the initial data area of the application with 
	its contents. Any modifications the code does to the data area 
	during its running have no effect on the return value; changes in
	the memory representation of the data and the initial state of the
	data for this application are thus separated.
	@return An array containing the data area with its contents as 
	MemoryLines. The array will be of length 0 if this application has 
	no initial data area. */
    public MemoryLine[] getInitialData() {
	return data.clone();
    }

    /** This method returns the symbol table containing the application's 
	local symbols. 
	@return The application's symbol table, minus global symbols like 
	HALT, unless they have been overwritten. */
    public SymbolTable getSymbolTable() {
	return symbols.clone();
    }

    /** This method stores one more line to the CRT ("screen") memory 
	of the application. The results can be queried by getCrt(); this
	method will not actually make the line show on any physical screen.
	@param line A new line to "write to the screen". */
    public void writeToCrt(int line) {
	crt += "" + line + System.getProperty("line.separator", "\n");
    }

    /** This method stores one more line to the StdOut ("file") memory 
	of the application. The results can be queried by getStdOut(); this
	method will not actually make the line show on any file.
	@param line A new line to "write to the file". */
    public void writeToStdOut(int line) {
	stdout += "" + line + System.getProperty("line.separator", "\n");
    }

    /** This method reads the next line from a keyboard "buffer" set up
	before by setKbd(). If setKbd() is called after this method 
	has been called, the previous keyboard buffer is ignored and 
	reading continues from the beginning of the new input. 
	@return The next integer corresponding to the kbd data set earlier
	by setKbd(). 
	@throws TTK91NoKbdData If there is no more keyboard data
	available in the buffer. The caller may then decide to get
	their keyboard data via other routes, eg. the user. */
    public int readNextFromKbd() throws TTK91NoKbdData {
	Logger logger;
	String[] messageParams = { kbdpointer, kbdcontent.length };;

	if(kbdpointer >= kbdcontent.length) {
	    logger = Logger.getLogger(this.getClass().getPackage());
	    logger.fine(new Message("Application has no more keyboard data, read: " + 
				    "{0}, buffer length {1}.", 
	                            messageParams));
	    throw new TTK91NoKbdData(new Message("No more keyboard data " +
						 "stored on application."));
	}
	else {
	    return kbdcontent[kbdpointer++]; // increment post-indexing.
	}
    }

    /** This method reads the next line from a file read "buffer" set
	up before by setStdIn(). If setStdIn() is called after this
	method has been called, the previous file read buffer is
	ignored and reading continues from the beginning of the new
	input.
	@return The next integer corresponding to the file read data set 
	earlier by setStdIn(). 
	@throws TTK91NoStdInData If there is no more file read data 
	available in the buffer. The caller may then decide to get their
	data via other routes, eg. by reading from some actual file. */
    public int readNextFromStdIn() throws TTK91NoStdInData {
	Logger logger;
	String[] messageParams = { stdinpointer, stdincontent.length };
	
	if(stdinpointer >= stdincontent.length) {
	    logger = Logger.getLogger(this.getClass().getPackage());
	    logger.fine(new Message("Application has no more stdin data, read: " +
				    "{0}, buffer length {1}.", 
				    messageParams));
	    throw new TTK91NoStdInData(new Message("No more stdin data stored " +
						   "on application."));
	}
	else {
	    return stdincontent[stdinpointer++]; // increment post-indexing.
	}
    }

    /** This method checks whether input would be a valid string
        to give to setKbd or setStdIn.
        @param input The string to check.
        @return True if the string would be valid input, false 
        otherwise. */
    public static boolean checkInput(String input) {
	StringTokenizer stringChopper;
	if(input == null)
	    return false;
	stringChopper = new StringTokenizer(input);
	while(stringChopper.hasMoreTokens()) {
	    try {
		Integer.parseInt(stringChopper.nextToken());
	    }
	    catch(NumberFormatException notParseableToInteger) {
		return false;
	    }
	}
	return true;
    }

    /* The implementation of TTK91Application *************************/

    /** This method returns what was written to a file during the
	running of this application, and clears the buffer.
	@return What the application printed to a file during its last run,
	delimited with System.getProperty("line.separator", "\n"). */
    public String readStdOut() {
	String result = stdout;
	stdout = "";
	return result;
    }

    /** This method returns what was printed to the screen during the 
	running of this application, and clears the buffer.
	@return What the application printed to screen during its last 
	run, delimited with System.getProperty("line.separator", "\n"). */
    public String readCrt() {
	String result = crt;
	crt = "";
	return result;
    }

    /** This method can be used to set in advance what values any 
	keyboard reads should return.
	@param input What (integers) the application should "read from the
	keyboard" during its run, delimited by '\n', '\r', '\r\n', '\f',
	'\t', ' ', ',', '.', ':', ';' or any length combination
	thereof.
	@throws IllegalArgumentException If the input string is not valid. */ 
    public void setKbd(String input) {
	String errorMessage;
	String logMessageParams = { input, "" };
	StringTokenizer stringChopper;
	Logger logger;
	int i;

	if(!checkInput(input)) {
	    errorMessage = new Message("Keyboard input string \"{0}\" invalid, " + 
				       "should be eg. \n-separated list of " +
				       "integers.").toString();
	    throw new IllegalArgumentException(new Message(errorMessage, input));
	}
	else {
	    stringChopper = new StringTokenizer(input, DELIMITERS);
	    kbdcounter = 0;
	    kbdcontent = new int[stringChopper.countTokens()];
	    i = 0;
	    while(stringChopper.hasMoreTokens()) {
		kbdcontent[i++] = Integer.parseInt(stringChopper.nextToken());
	    }
	    logger = Logger.getLogger(this.getClass().getPackage());
	    logMessageParams[1] = "" + kbdcontent.length;
	    logger.fine(new Message("Accepted \"{0}\" as keyboard input, tokens " +
				    "found: {1}.", logMessageParams));
	}
							   
    }

    /** This method can be used to set in advance what values any 
	file reads should return. The input is checked.
	@param input What (integers) the application should "read from
	a file" during its run, delimited by '\n', '\r', '\r\n', '\t', '\f',
	' ', ',', '.', ':', ';' or any-length combination thereof. 
	@throws IllegalArgumentException If the input string is not valid. */ 
    public void setStdIn(String input) {
	String errorMessage;
	String logMessageParams = { input, "" };
	StringTokenizer stringChopper;
	int i;

	if(!checkInput(input)) {
	    errorMessage = new Message("Stdin input string \"{0}\" invalid, " + 
				       "should be eg. \n-separated list of " +
				       "integers.").toString();
	    throw new IllegalArgumentException(new Message(errorMessage, input));
	}
	else {
	    stringChopper = new StringTokenizer(input, DELIMITERS);
	    stdincounter = 0;
	    stdincontent = new int[stringChopper.countTokens()];
	    i = 0;
	    while(stringChopper.hasMoreTokens()) {
		stdincontent[i++] = Integer.parseInt(stringChopper.nextToken());
	    }
	    logger.getLogger(this.getClass().getPackage());
	    logMessageParams[1] = "" + stdincontent.length;
	    logger.fine(new Message("Accepted \"{0}\" as stdin input, tokens " +
				    "found: {1}.", 
	                            logMessageParams));
	}
    }
}
