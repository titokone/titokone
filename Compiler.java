package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.TTK91CompileSource;
import fi.hu.cs.ttk91.TTK91CompileException;
import java.util.StringTokenizer;
import java.util.Hashtable;

/** This class knows everything about the relation between symbolic
    code and binary code. It can transform a full source to binary or
    one symbolic command to binary or vice versa. Empty out all compiler 
    commands and empty lines at the start
    of round 2.*/
public class Compiler {
    /** This field contains the source code as a StringTokenizer, delimited by 
	\n\r\f. */
    private StringTokenizer source;
    /** This field contains the symbol table in its incomplete form. Some of 
	the values corresponding to the keys may be null, if they have not
	been defined yet. */
    private Hashtable symbols;

    /** This field keeps track of whether we are in the first round of compilation
	or the second. It is set by compile() and updated by compileLine(). */
    private boolean firstRound;

    /** This field counts the number of actual command lines found during the 
	first round. */
    private int commandLineCount;

    /** This field keeps track of the size of the data area expanded during the
	first round. */
    private int dataAreaSize;
    
    /** This array contains the code. It will not be touched until the second 
	round of compilation. */
    private MemoryLine[] code;

    /** This array contains the data. It will not be touched until the second 
	round of compilation. */
    private MemoryLine[] data;

    /** This field contains the debugger instance to inform of any compilation
	happenings. */
    private Debugger debugger;

    /** This constructor sets up the class. It also initializes an instance of 
	CompileConstants and Debugger. */
    public Compiler() { }

    /** This function initializes transforms a symbolic source code into an 
	application class. After this, call compileLine() to actually compile
	the application one line at a time, and finally getApplication() to get
	the finished application. 
	@param source The symbolic source code to be compiled. */
    public void compile(TTK91CompileSource source) { }

    /** This function goes through one line of the code. On the first round, it
	gathers the symbols and their definitions to a symbol table and conducts 
	syntax-checking, on the second round it transforms each command to its 
	binary format. For the transformations, the CompileConstants class is 
	used. It calls the private methods firstRoundProcess() and 
	secondRoundProcess() to do the actual work, if there is any to do.
	The transfer from first round of compilation to the second is done 
	automatically; during it, initializeSecondRound() is called.
	@return A CompileInfo debug information object, describing what happened
	during the compilation of this line and whether this is the first or second
	round of compilation or null if there are no more lines left to process.
	@throws TTK91CompileException If a) there is a syntax error during the first
	round of checking (error code 101) or b) a symbol is still undefined after 
	the first round of compilation is finished. */
    public CompileInfo compileLine() throws TTK91CompileException { }

    /** This method returns the readily-compiled application if the compilation
	is complete, or null otherwise. */
    public Application getApplication() { }

    /** This function transforms a binary command number to a MemoryLine 
	containing both the binary and the symbolic command corresponding 
	to it.
	@param binary The command to be translated as binary.
	@return A MemoryLine instance containing both the information 
	about the symbolic command and the binary version of it. */
    public MemoryLine getSymbolicAndBinary(int binary) { }

    /** This function transforms a MemoryLine containing only the binary 
	command to a MemoryLine containing both the binary and the 
	symbolic command corresponding to it.
	@param binaryOnly A MemoryLine containing the binary only of the 
	command to be translated as binary. If the MemoryLine contains
	both, the pre-set symbolic value is ignored.
	@return A MemoryLine instance containing both the information 
	about the symbolic command and the binary version of it. */
    public MemoryLine getSymbolicAndBinary(MemoryLine binaryOnly) { }

    /** This function gathers new symbol information from the given line 
	and checks its syntax. If a data reservation is detected, the 
	dataAreaSize is incremented accordingly. If the line contains 
	an actual command, commandLineCount is incremented. 
	@param line The line of code to process. 
	@return A CompileInfo object describing what was done, or null if 
	the first round has been completed. This will be the sign for
	the compiler to prepare for the second round and start it. */
    private CompileInfo firstRoundProcess(String line) { }

    /** This method initializes the code and data area arrays for the
	second round processing according to the dataAreaSize and 
	commandLineCount variables. It also resets the StringTokenizer by
	calling tokenize(). 

	Before entering the second round we must clear all the empty lines
	like compiler-codes (pseudo-codes) and lines with nothing but comments.
     */
    private void initializeSecondRound() { }

    /** This function transforms any commands to binary and stores both
	forms in the code array, or sets any initial data values in the
	data array. It uses CompileConstants as its aid. 
	@param line The line of code to process.
	@return A CompileInfo object describing what was done, or 
	null if the second round and thus the compilation has been 
	completed. */
    private CompileInfo secondRoundProcess(String line) { }

/* Antti: 04.03.04
Do a pure binary translation first, then when all sections are complete, convert the binary to an 
integer. Needs variables for opcode(8bit), first operand(3 bit)(r0 to r7), m-part(memory format, 
direct, indirect or from code), possible index register (3 bit) and 16 bits for the address.
Once STORE is converted to a 00000001 and the rest of the code processed we get 32bit binary that
is the opcode from symbolic opcode.

Antti 08.03.04
Teemu said that we should support the machine spec instead of Koksi with this one. No need to 
support opcodes like Muumuu LOAD R1, 100 kissa etc. Only tabs and extra spacing. So 
we need to support opcodes like LOAD R1,           100 but not codes like LOAD R1, =R2.

Basic functionality: (Trim between each phase.) 
	Check if there is a label (8 first are the meaningful ones also must have one non-number))
	Convert opcode (8bit)
	check which register (0 to 7)
	=, Rj/addr or @ (00, 01 or 10)
	if addr(Ri) or Rj(Ri)(0 to 7)
	convert address (16bit)
	check if the rest is fine (empty of starts with ;)

	Store both formats to a data array (symbolic and binary).
*/

    /** This function generates a StringTokenizer from the source string. 
	The delimiters used are \r\n\f.
	@param source The source string to tokenize.
	@return A StringTokenizer containing the source. */
    private StringTokenizer tokenize(String source) { }
}
