/** This class represents a compiled TTK-91-application. It also contains 
    information about what has gotten printed during its running and 
    what it should be reading as various inputs during its run. */
public class Application implements TTK91Application {
    /** This array contains the code to the application; one command
	per line. The MemoryLine objects may contain either only the
	integers, or both the integer and the corresponding 
	symbolic command. The initial FP value can be calculated from 
	code.length - 1. */
    private MemoryLine[] code;
    /** This array contains the initial data area of the application, 
	determined at compile time by DS and DC pseudocommands. The 
	initial SP value can be determined from code.length + 
	initialData.length - 1. (It points to the last reserved memory
	address, since there is no "top of stack" yet.) */
    private MemoryLine[] initialData;

    /** This constructor sets up a new Application which does not know
	where its symbols are used. An application eg. read from binary
	format will not contain any commands of the form STORE R1, X, but 
	only numbers like STORE R1, 100. To store the symbols being used
	as well as the symbol table, use the other constructor.
	@param binary An array containing the compiled code as integers, 
	one command per line. This covers code only; the initial data area 
	is separated into the second parameter. This value may be null or
	of length 0 in the unlikely case that there is no code area.
	@param data An array containing the initial data area and its 
	contents, one integer per line. This value may be null or of 
	length 0 if there is no data area.
	@param SymbolTable An instance of SymbolTable, containing the 
	symbol table for this application. This value can be null if 
	there are no local symbols available. */
    public Application(int[] binary, int[] data, SymbolTable symbols) { }

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
	contents as MemoryLines. The MemoryLines represent both the integer
	and the symbolic form of commands, but especially in the case of 
	data, including the symbolic form is optional. This value may be 
	null or of length 0 if there is no data area.
	@param SymbolTable An instance of SymbolTable, containing the 
	symbol table for this application. This value can be null if 
	there are no local symbols available. */
    public Application(MemoryLine[] code, MemoryLine[] data, 
		       SymbolTable symbols) { }

    /** This method returns the (initial) code area of the application. 
	Any self-modifying code in the application will have no 
	effect on the return value; changes in the memory representation 
	of the code and in the initial code are thus separated.
	@return An array containing the code area of the application. The 
	array will be of length 0 if for some reason this application has
	no code area. */
    public MemoryLine[] getCode() { }

    /** This method returns the initial data area of the application with 
	its contents. Any modifications the code does to the data area 
	during its running have no effect on the return value; changes in
	the memory representation of the data and the initial state of the
	data for this application are thus separated.
	@return An array containing the data area with its contents as 
	MemoryLines. The array will be of length 0 if this application has 
	no initial data area. */
    public MemoryLine[] getInitialData() { }

    /** This method stores one more line to the CRT ("screen") memory 
	of the application. The results can be queried by getCrt(); this
	method will not actually make the line show on any GUI.
	@param line A new line to "write to the screen". This should
	be a string representation of an integer, eg. "114422", but
	no validity checking is done at this point. */
    public void writeToCrt(String line) {  }

    /** This method stores one more line to the StdOut ("file") memory 
	of the application. The results can be queried by getStdOut(); this
	method will not actually make the line show on any file.
	@param line A new line to "write to the file". This should
	be a string representation of an integer, eg. "114422", but
	no validity checking is done at this point. */
    public void writeToStdOut(String line) {  }

    /** This method reads the next line from a keyboard "buffer" set up
	before by setKbd(). If setKbd() is called after this method 
	has been called, the previous keyboard buffer is ignored and 
	reading continues from the top line of the new string. 
	@return The next integer corresponding to the kbd data set earlier
	by setKbd(). 
	@throws NoInputException If there is no more keyboard data available
	in the buffer or if the data does not transform into an integer. 
	The caller may then decide to get their keyboard
	data via other routes, eg. the user. */
    public String readNextFromKbd() throws NoInputException { }

    /** This method reads the next line from a file read "buffer" set
	up before by setStdIn(), and transforms it to an integer. If
	setStdIn() is called after this method has been called, the
	previous file read buffer is ignored and reading continues
	from the top line of the new string.
	@return The next integer corresponding to the file read data set 
	earlier by setStdIn(). 
	@throws NoInputException If there is no more file read data 
	available in the buffer, or if the data does not transform into 
	an integer. The caller may then decide to get their 
	stdin data via other routes, eg. by reading from some actual file. */
    public int readNextFromStdIn() 
	throws NoInputException, TTK91RuntimeException {  }

    /** This method returns the symbol table containing the application's 
	local symbols. 
	@return The application's symbol table, minus global symbols like 
	HALT. */
    public SymbolTable getSymbolTable() { }

    /* The implementation of TTK91Application *************************/

    /** This method returns the binary code of this application in an 
	array of integers.
	@return The binary code in an array, data areas excluded. */
    public int[] getSerialized() { }

    /** This method returns what was written to a file during the
	running of this application.
	@return What the application printed to a file during its last run,
	delimited with System.getProperty("line.separator", "\n"). */
    public String getStdOut() {  }

    /** This method returns what was printed to the screen during the 
	running of this application.
	@return What the application printed to screen during its last 
	run, delimited with System.getProperty("line.separator", "\n"). */
    public String getCrt() {  }

    /** This method can be used to set in advance what values any 
	keyboard reads should return.
	@param input What the application should "read from the keyboard"
	during its run, delimited by \n, \r or \r\n. */ 
    public void setKbd(String input) { }

    /** This method can be used to set in advance what values any 
	file reads should return.
	@param input What the application should "read from a file"
	during its run, delimited by \n, \r or \r\n. */ 
    public void setStdIn(String input) { }
}
