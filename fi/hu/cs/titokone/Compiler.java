/*
Ensimmäisellä kierroksella jos tulee dc tai ds niin tehdään symbol found ja finalizing first 
roundissa sitten tulee definingdc ja definingds.y

finalizing_first_roundin lopussa pitää palauttaa sekä data-alue että koodi erikseen. kahtena 
taulukkona siis.
*/

package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.TTK91CompileException;
import java.util.StringTokenizer;
import java.util.HashMap;

/** This class knows everything about the relation between symbolic
    code and binary code. It can transform a full source to binary or
    one symbolic command to binary or vice versa. Empty out all compiler 
    commands and empty lines at the start
    of round 2.*/
public class Compiler {

    /** This field contains the source code as a String array.
    private String[] source;

    /** This field contains the symbol table in its incomplete form. Some of 
	the values corresponding to the keys may be null, if they have not
	been defined yet. */
    private HashMap symbols;

    /** This field holds the initial value of a symboltable, if it gets filled up
	a new table is created doubling it's size.
      */
    private int SYMBOLTABLEINITIALSIZE = 30;

    /** This field tells how many symbols have been found so far. */
    private int symbolsFoundSoFar;

    /** This field tells the next line to be checked. */
    private int nextLine;

    /** This field holds all the valid symbols on a label. */
    private final String VALIDLABELCHARS = "0123456789abcdefghijklmnopqrstuvwxyzåäö_";
    private final int NOTVALID = -1;
    private final int EMPTY = -1;

    /** This field keeps track of whether we are in the first round of compilation
	or the second. It is set by compile() and updated by compileLine(). */
    private boolean firstRound;

    /** This field counts the number of actual command lines found during the 
	first round. */
    private int commandLineCount;

    /** This field keeps track of the size of the data area expanded during the
	first round. */
    private int dataAreaSize;
    
    /** This array contains the code.  During the first round this field holds the 
	clean version of the code (stripped of compiler commands like def, ds, dc etc.)
      */
    private String[] code;

    /**	This field is the initial size of the code array. */
    private final int CODEARRAYINITIALSIZE = 100;

    /** This array contains the compiled source code.
      */
    private int[] compiledCode;

    /** This array contains the data. During first round this array holds the values of 
	variables defined.
      */
    private String[] data;

    /** This field contains the CompileDebugger instance to inform of any compilation
	happenings. */
    private CompileDebugger compileDebugger;

/*---------- Constructor ----------*/

    /** This constructor sets up the class. It also initializes an instance of 
	CompileDebugger. */
    public Compiler() {
	compileDebugger = newCompileDebugger();
    }

    /** This function initializes transforms a symbolic source code into an 
	application class. After this, call compileLine() to actually compile
	the application one line at a time, and finally getApplication() to get
	the finished application. 
	@param source The symbolic source code to be compiled. */
    public void compile(String source) { 
	firstRound = true;
	this.source = source.split();
	symbols = new HashMap();
	symbolsFoundSoFar = 0;
	nextLine = 0;
	commandLineCount = 0;
	dataAreaSize = 0;
	code = new String[CODEARRAYINTIALSIZE];
	data = new String[SYMBOLTABLEINITIALSIZE];
    }

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
    public CompileInfo compileLine() throws TTK91CompileException { 

	if (firstRound) {
		if (nextLine == source.length()) {
			initializeSecondRound();
		} else {
			firstRoundProcess(source[nextLine]);
			++nextLine;
		}
	} else {
		if (nextLine == code.length()) {
			// TODO create application;
		} else {
			secondRoundProcess(code[nextLine]);
			++nextLine;
		}
	}

    }


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
    private CompileInfo firstRoundProcess(String line) throws TTK91CompileException {
	String[] lineTemp = parseLine(line);
	if (lineTemp == null) {
		throw new TTK91CompileException();	// TODO check exception
	} else {
		if (!lineTemp[0].equals("")) {		// label found
			if (lineTemp[1].equals("DC")) {
				
			} else {			
				if (lineTemp[1].equals("DS")) {

				} else {			
					if (lineTemp[1].equals("EQU")) {

					} else {
					
						if (lineTemp[1].equals("DEF")) {

						} else {	// valid code with a label 
						
						}
					}
				}
			}			
		} else {
			try {
				Integer.parseInt(lineTemp[4]);	
			} catch(NumberFormatException e) {	// address part must have a variable 
				
			}
		}
	}
    }

    /** This method initializes the code and data area arrays for the
	second round processing according to the dataAreaSize and 
	commandLineCount variables. It also resets the StringTokenizer by
	calling tokenize(). Before entering the second round we must clear all the empty lines
	like compiler-codes (pseudo-codes) and lines with nothing but comments.
     */
    private void initializeSecondRound() {

	compiledCode = new int[commandLineCount];
	nextLine = 0;
	firstRound = false;

    }

    /** This function transforms any commands to binary and stores both
	forms in the code array, or sets any initial data values in the
	data array. It uses CompileConstants as its aid. 
	@param line The line of code to process.
	@return A CompileInfo object describing what was done, or 
	null if the second round and thus the compilation has been 
	completed. */
    private CompileInfo secondRoundProcess(String line) { 

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


    }

    /** This function generates a StringTokenizer from the source string. 
	The delimiters used are \r\n\f.
	@param source The source string to tokenize.
	@return A StringTokenizer containing the source. */
    private StringTokenizer tokenize(String source) { }

    /**	This method parses a String and tries to find a label, opCode and all the 
	other parts of a Command line.
	@param symbolicOpCode Symbolic form of an operation code. */  
    public String[] parseLine(String symbolicOpCode) {

	String label = "";
	String opcode = "";		
	String firstRegister = "";
	String addressingMode = "";
	String secondRegister = "";
	String address = "";
	String VALIDLABELCHARS = "0123456789abcdefghijklmnopqrstuvwxyzåäö_";
	
	SymbolicInterpreter symbolicInterpreter = new SymbolicInterpreter();
	String[] parsedLine;	
	String wordTemp = "";
	int nextToCheck = 0;	// for looping out the spacing
	int fieldEnd = 0;	// searches the end of a field (' ', ',')

	symbolicOpcode = symbolicOpcode.replace('\t',' ');
	symbolicOpcode = symbolicOpcode.toLowerCase();
	symbolicOpcode = symbolicOpcode.trim();
	fieldEnd = symbolicOpcode.indexOf(";");
	if (fieldEnd != -1) { symbolicOpcode = symbolicOpcode.substring(0, fieldEnd); }

/*label or opCode*/

	fieldEnd = symbolicOpcode.indexOf(" ");
	if (fieldEnd == -1) { fieldEnd = symbolicOpcode.length(); } 

	wordTemp = symbolicOpcode.substring(nextToCheck, fieldEnd);
	if (symbolicInterpreter.getOpcode(wordTemp) == -1) { 	// try to find a label (not a valid 
								//opCode)
				// label must have one non-number (valid chars A-Ö, 0-9 and _)
		boolean allCharsValid = true;
		boolean atLeastOneNonNumber = false;
		for (int i = 0; i < wordTemp.length(); ++i) {
			if (atLeastOneNonNumber == false) {
				if (VALIDLABELCHARS.indexOf(wordTemp.charAt(i)) > 9) {
					atLeastOneNonNumber = true;
				}
			} 
			if (VALIDLABELCHARS.indexOf(wordTemp.charAt(i)) < 0) {
				allCharsValid = false;
			}
		}
		if (atLeastOneNonNumber == false || allCharsValid == false) { 
			return null;
		}
		label = wordTemp;
		nextToCheck = fieldEnd + 1;	
		while (symbolicOpcode.charAt(nextToCheck) == ' ') { ++nextToCheck; }

		fieldEnd = symbolicOpcode.indexOf(" ", nextToCheck);
        	if (fieldEnd == -1) {
                	fieldEnd = symbolicOpcode.length();
        	}
		wordTemp = symbolicOpcode.substring(nextToCheck, fieldEnd);
	}	

	opcode = wordTemp;
	if (symbolicInterpreter.getOpcode(opcode) < 0) { return null; }
	nextToCheck = fieldEnd + 1;

/*first register*/
       if (nextToCheck < symbolicOpcode.length()) {
                while (symbolicOpcode.charAt(nextToCheck) == ' ') { ++nextToCheck; }
                fieldEnd = symbolicOpcode.indexOf(",", nextToCheck);
                if (fieldEnd == -1) {
                        if (symbolicInterpreter.getRegisterId(
                                        symbolicOpcode.substring(nextToCheck)) != -1) {
                                firstRegister = symbolicOpcode.substring(nextToCheck);
                                fieldEnd = symbolicOpcode.length();
                        } else {
                                fieldEnd = nextToCheck - 1;
                        }
                } else {
                        if (symbolicInterpreter.getRegisterId(
                                symbolicOpcode.substring(nextToCheck, fieldEnd)) != -1) {
                                firstRegister = symbolicOpcode.substring(nextToCheck, fieldEnd);
                        }
                }
        nextToCheck = fieldEnd + 1;
        }

/*addressingMode*/
	if (nextToCheck < symbolicOpcode.length()) {
		while (symbolicOpcode.charAt(nextToCheck) == ' ') { ++nextToCheck; }
		if (symbolicOpcode.charAt(nextToCheck) == '=' || 
				symbolicOpcode.charAt(nextToCheck) == '@') {
			addressingMode = "" + symbolicOpcode.charAt(nextToCheck);
			++nextToCheck;
		} else { addressingMode = ""; }
	}

/*address and second register*/
	while (nextToCheck == ' ') { ++nextToCheck; }
	if (symbolicOpcode.indexOf("(", nextToCheck) != -1) {
		if (symbolicOpcode.indexOf(")", nextToCheck) < 
					symbolicOpcode.indexOf("(", nextToCheck)) {
			return null; 
		} else {
			address = symbolicOpcode.substring(nextToCheck, 
					symbolicOpcode.indexOf("(", nextToCheck));
			
			secondRegister = symbolicOpcode.substring(
	symbolicOpcode.indexOf("(", nextToCheck) + 1, symbolicOpcode.indexOf(")", nextToCheck));
			if (symbolicInterpreter.getRegisterId(secondRegister) == -1) {
                        	return null;
                        }

		}
	} else {
		address = symbolicOpcode.substring(nextToCheck);
	}

	if (symbolicInterpreter.getRegisterId(address) != -1) {
		secondRegister = address;
		address = "";
	}


	parsedLine = new String[6];
	parsedLine[0] = label;
	parsedLine[1] = opcode;
	parsedLine[2] = firstRegister;
	parsedLine[3] = addressingMode;
	parsedLine[4] = address;
	parsedLine[5] = secondRegister;
	return parsedLine;
    }
}
