/*
Ensimm‰isell‰ kierroksella jos tulee dc tai ds niin tehd‰‰n symbol found ja finalizing first 
roundissa sitten tehd‰‰n symbolitaulu (String[][]) joka palautetaan compileInfossa.

jos m‰‰ritell‰‰n label niin sit‰ ei voi m‰‰ritell‰ en‰‰ miksik‰‰n muuksi, vaan tulee poikkeus.
*/

package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.TTK91CompileException;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Vector;

/** This class knows everything about the relation between symbolic
    code and binary code. It can transform a full source to binary or
    one symbolic command to binary or vice versa. Empty out all compiler 
    commands and empty lines at the start
    of round 2.*/
public class Compiler {

    /** This field contains the source code as a String array. */
    private String[] source;

    /** This field holds the declared variables, labels and other symbols. It acts as a 
	pointer to the symbolTable Vector where the actual data is stored. */
    private HashMap symbols;

    /** This field holds the invalid values to be introduced (i.e. already used labels can't 
	be re-used. */
    private HashMap invalidLabels;

    /** This field tells the next line to be checked. */
    private int nextLine;

    /** This field holds all the valid symbols on a label. */
    private final String VALIDLABELCHARS = "0123456789abcdefghijklmnopqrstuvwxyzÂ‰ˆ_";
    private final int NOTVALID = -1;
    private final int EMPTY = -1;

    /** Maximum value of the address part. */
    private final int MAXINT = 32767;
    /** Minimum value of the address part. */
    private final int MININT = -32767;

    /** This field holds the value of Stdin if it was set with DEF command. */
    private String defStdin;
    /** This field holds the value of Stdout if it was set with DEF command. */
    private String defStdout;

    /** This field keeps track of whether we are in the first round of compilation
	or the second. It is set by compile() and updated by compileLine(). */
    private boolean firstRound;

    /** This field counts the number of actual command lines found during the 
	first round. */
    private int commandLineCount;

    /** This array contains the code.  During the first round this field holds the 
	clean version of the code (stripped of compiler commands like def, ds, dc etc.)
      */
    private Vector code;

    /** This field acts as a symboltable, it is a String array vector where 1:st position
 	holds the name of the symbol and the second either it's value (label, equ) or
	the command (ds 10, dc 10) */
    private Vector symbolTable;

    /** This array contains the data. */
    private String[] data;

    /** This field contains the CompileDebugger instance to inform of any compilation
	happenings. */
    private CompileDebugger compileDebugger;

/*---------- Constructor ----------*/

    /** This constructor sets up the class. It also initializes an instance of 
	CompileDebugger. */
    public Compiler() {
	compileDebugger = new CompileDebugger();
    }

    /** This function initializes transforms a symbolic source code into an 
	application class. After this, call compileLine() to actually compile
	the application one line at a time, and finally getApplication() to get
	the finished application. 
	@param source The symbolic source code to be compiled. */
    public void compile(String source) { 
	firstRound = true;
	this.source = splitALine(source);

	nextLine = 0;
	defStdin = "";
	defStdout = "";

	code = new Vector();
	symbols = new HashMap();	
	symbolTable = new Vector();
	invalidLabels = new HashMap();

	invalidLabels.put("crt", new Integer(0));
	invalidLabels.put("kbd", new Integer(1));
	invalidLabels.put("stdin", new Integer(6));
	invalidLabels.put("stdout", new Integer(7));
	invalidLabels.put("halt", new Integer(11));
	invalidLabels.put("read", new Integer(12));
	invalidLabels.put("write", new Integer(13));
	invalidLabels.put("time", new Integer(14));
	invalidLabels.put("date", new Integer(15));
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

	CompileInfo info;

	if (firstRound) {
		if (nextLine == source.length) {
			compileDebugger.firstPhase();
			info = initializeSecondRound();
			return info;
		} else {
			compileDebugger.firstPhase(nextLine, source[nextLine]);
			info = firstRoundProcess(source[nextLine]);
			++nextLine;
			return info;
		}
	} else {
		if (nextLine == code.size()) {
			// TODO create application;
			compileDebugger.finalPhase();
			info =  compileDebugger.lineCompiled();
			return info;
		} else {
//			compileDebugger.secondPhase(nextLine, source[nextLine], );
			info = secondRoundProcess((String)code.get(nextLine));
			++nextLine;
			return info;
		}
	}
    }


// TODO
//    /** This method returns the readily-compiled application if the compilation
//	is complete, or null otherwise. */
//    public Application getApplication() { }

    /** This function transforms a binary command number to a MemoryLine 
	containing both the binary and the symbolic command corresponding 
	to it.
	@param binary The command to be translated as binary.
	@return A MemoryLine instance containing both the information 
	about the symbolic command and the binary version of it. */
    public MemoryLine getSymbolicAndBinary(int binary) { 
	BinaryInterpreter bi = new BinaryInterpreter();
	return new MemoryLine(binary, bi.binaryToString(binary));
    }

    /** This function transforms a MemoryLine containing only the binary 
	command to a MemoryLine containing both the binary and the 
	symbolic command corresponding to it.
	@param binaryOnly A MemoryLine containing the binary only of the 
	command to be translated as binary. If the MemoryLine contains
	both, the pre-set symbolic value is ignored.
	@return A MemoryLine instance containing both the information 
	about the symbolic command and the binary version of it. */
    public MemoryLine getSymbolicAndBinary(MemoryLine binaryOnly) { 
	BinaryInterpreter bi = new BinaryInterpreter();
	return new MemoryLine(binaryOnly.getBinary(), bi.binaryToString(binaryOnly.getBinary()));
    }

    /** This function gathers new symbol information from the given line 
	and checks its syntax. If a data reservation is detected, the 
	dataAreaSize is incremented accordingly. If the line contains 
	an actual command, commandLineCount is incremented. 
	@param line The line of code to process. 
	@return A CompileInfo object describing what was done, or null if 
	the first round has been completed. This will be the sign for
	the compiler to prepare for the second round and start it. */
    private CompileInfo firstRoundProcess(String line) throws TTK91CompileException {
	String[] lineTemp = parseCompilerCommandLine(line);
	boolean nothingFound = true;
	String comment = "";
	String[] commentParameters;
	int intValue = 0;
	String[] symbolTableEntry = new String[2];
	boolean labelFound = false;
	boolean variableUsed = false;
	boolean symbolDefined = false;

	if (lineTemp == null) {
		lineTemp = parseLine(line);
		if (lineTemp == null) { 
// not a valid command
			throw new TTK91CompileException(); 
		} else {

			if (lineTemp[1].equals("")) {
// line empty;

			} else {
				code.add(line);
				if (!lineTemp[0].equals("")) {
					nothingFound = false;
					labelFound = true;
// label found
				
					if (invalidLabels.containsKey(lineTemp[0])) {
// not a valid label			
						throw new TTK91CompileException();
					} else {
						invalidLabels.put(lineTemp[0], null);

						if (symbols.containsKey(lineTemp[0])) {
							symbolTableEntry[0] = lineTemp[0];
							symbolTableEntry[1] = "" + 
									(code.size() - 1);
symbolTable.add(Integer.parseInt((String)symbols.get(lineTemp[0])), symbolTableEntry);
						} else {
							symbols.put(lineTemp[0], 
								new Integer(code.size() - 1));
							symbolTableEntry[0] = lineTemp[0];
                                                	symbolTableEntry[1] = "" +
              	                                          		(code.size() - 1);
                	                                symbolTable.add(symbolTableEntry);
						}
	
						compileDebugger.foundLabel(lineTemp[0], 
									code.size() -1);
					}
				}

				try {
					Integer.parseInt(lineTemp[4]);	
				} catch(NumberFormatException e) {	
// variable used	
					nothingFound = false; 	
					variableUsed = true;
					compileDebugger.foundSymbol(lineTemp[4]);

					if (!symbols.containsKey(lineTemp[0])) {
						if (invalidLabels.get(lineTemp[4]) == null) {
							symbols.put(lineTemp[4], new 
								Integer(symbolTable.size()));
							symbolTableEntry[0] = lineTemp[0];
                                                        symbolTableEntry[1] = "";
							symbolTable.add(symbolTableEntry);
						} else {
// reserver word was used	
		symbols.put(lineTemp[4], new Integer(symbolTable.size()));
                symbolTableEntry[0] = lineTemp[0];
                symbolTableEntry[1] = "" + (Integer)invalidLabels.get(lineTemp[4]);
                symbolTable.add(symbolTableEntry);
						}
					} 
				}
			}
		}
	} else {
// compiler command
		boolean allCharsValid = true;
		boolean atLeastOneNonNumber = false;

		if (invalidLabels.containsKey(lineTemp[0])) {
			throw new TTK91CompileException();
		}

		for (int i = 0; i < lineTemp[0].length(); ++i) {
			if (atLeastOneNonNumber == false) {
				if (VALIDLABELCHARS.indexOf(lineTemp[0].charAt(i)) > 9) {
					atLeastOneNonNumber = true;
				}
			}			
			if (VALIDLABELCHARS.indexOf(lineTemp[0].charAt(i)) < 0) {
				allCharsValid = false;
			}
		}

		if (atLeastOneNonNumber == false || allCharsValid == false) { 
// not a valid label;
			throw new TTK91CompileException(); 
		} else {
			if (invalidLabels.containsKey(lineTemp[0])) {
				throw new TTK91CompileException();
			} 

			if (lineTemp[1].equalsIgnoreCase("ds")) {
				intValue = 0;
				try {
					intValue = Integer.parseInt(lineTemp[2]);	
				} catch(NumberFormatException e) {	
					throw new TTK91CompileException(); 
				}	
				if (intValue < 0 || intValue > MAXINT) {
					throw new TTK91CompileException(); 
				}
			}

			if (lineTemp[1].equalsIgnoreCase("dc")) {
				intValue = 0;
				if (lineTemp[2].trim().length() > 0) {
					try {
						intValue = Integer.parseInt(lineTemp[2]);	
					} catch(NumberFormatException e) {	
						throw new TTK91CompileException(); 
					}	

					if (intValue < MININT || intValue > MAXINT) {
						throw new TTK91CompileException(); 
					}
				}
			}

			if (lineTemp[1].equalsIgnoreCase("equ")) {
				if (symbols.containsKey(lineTemp[0])) {
					symbolTableEntry[0] = lineTemp[0];
					symbolTableEntry[1] = lineTemp[2];
symbolTable.add(Integer.parseInt((String)symbols.get(lineTemp[0])), symbolTableEntry);
				} else {
					symbols.put(lineTemp[0], new Integer(symbolTable.size()));
			           	symbolTableEntry[0] = lineTemp[0];
                                        symbolTableEntry[1] = lineTemp[2];
                                        symbolTable.add(symbolTableEntry);
				}
				compileDebugger.foundEQU(lineTemp[0], intValue);
// TODO				compileDebugger.setComment();
			}
			if (lineTemp[1].equals("ds")) {
				if (symbols.containsKey(lineTemp[0])) {
					symbolTableEntry[0] = lineTemp[0];
					symbolTableEntry[1] = lineTemp[1] + " " + lineTemp[2];
symbolTable.add(Integer.parseInt((String)symbols.get(lineTemp[0])), symbolTableEntry);

				} else {
					symbolTableEntry[0] = lineTemp[0];
					symbolTableEntry[1] = lineTemp[1] + " " + lineTemp[2];
					symbolTable.add(symbolTableEntry);

				}
				compileDebugger.foundDS(lineTemp[0]);
// TODO				compileDebugger.setComment();
			}
			if (lineTemp[1].equals("dc")) {
				compileDebugger.foundDC(lineTemp[0]);
				if (symbols.containsKey(lineTemp[0])) {
                                       symbolTableEntry[0] = lineTemp[0];
                                        symbolTableEntry[1] = lineTemp[1] + " " + lineTemp[2];
symbolTable.add(Integer.parseInt((String)symbols.get(lineTemp[0])), symbolTableEntry);
                                } else {
                                        symbolTableEntry[0] = lineTemp[0];
                                        symbolTableEntry[1] = lineTemp[1] + " " + lineTemp[2];
                                        symbolTable.add(symbolTableEntry);
				}
				compileDebugger.foundDC(lineTemp[0]);
// TODO				compileDebugger.setComment();
			}
			if (lineTemp[1].equals("def")) {
				if (lineTemp[0].equals("stdin") || lineTemp[0].equals("stdout")) {
					if (lineTemp[0].equals("stdin")) { 
						defStdin = lineTemp[2];
					} else {
						defStdout = lineTemp[2];
					}		
					compileDebugger.foundDEF(lineTemp[0], lineTemp[2]);
// TODO					compileDebugger.setComment();
				} else {
					throw new TTK91CompileException(); 
				}			
			}
		}		
	}


//	generate a comment from the boolean variables! TODO
	return compileDebugger.lineCompiled();
    }

    /** This method initializes the code and data area arrays for the
	second round processing according to the dataAreaSize and 
	commandLineCount variables. It also resets the StringTokenizer by
	calling tokenize(). Before entering the second round we must clear all the empty lines
	like compiler-codes (pseudo-codes) and lines with nothing but comments.
     */
    private CompileInfo initializeSecondRound() {
/* TODO increase the values in symbol[] with the beginning of data area. */

	nextLine = 0;
	firstRound = false;

// copy the code.

	String[] newCode = new String[code.size()];
	for (int i= 0; i < newCode.length; ++i) newCode[i] = (String)code.get(i);

	String[] lineTemp;
	int dataAreaSize = 0;

// calculate the size of data-area

	for (int i = 0; i < symbolTable.size(); ++i) {
		lineTemp = (String[])symbolTable.get(i);
		if (lineTemp[1].trim().length() > 3) {
			if (lineTemp[1].substring(0,2).equalsIgnoreCase("ds")) {
				dataAreaSize += Integer.parseInt(lineTemp[1].substring(3));
			} else {
				if (lineTemp[1].substring(0,2).equalsIgnoreCase("dc"))
					++dataAreaSize;
			}
		}
	}
	
	if (!defStdin.equals("")) ++dataAreaSize;
	if (!defStdout.equals("")) ++dataAreaSize;

	String[] data = new String[dataAreaSize];
	String[] newSymbolTableLine = new String[2];
	newSymbolTableLine[0] = "";
	newSymbolTableLine[1] = "";
	int nextPosition = 0;
	int nextMemorySlot = newCode.length;
	int dsValue = 0;


// update variable values to symbolTable

	for (int i = 0; i < symbolTable.size(); ++i) {
                lineTemp = (String[])symbolTable.get(i);
                if (lineTemp[1].trim().length() > 2) {
                        if (lineTemp[1].substring(0,2).equalsIgnoreCase("ds")) {
				dsValue = Integer.parseInt(lineTemp[1].substring(3));
				newSymbolTableLine[0] = lineTemp[0];
				newSymbolTableLine[1] = "" + nextMemorySlot;
				symbolTable.add(i, newSymbolTableLine);
				++nextMemorySlot;
				for (int j = nextPosition; 
					j < nextPosition + dsValue; ++nextPosition) {
 					data[j] = "" + 0;		
				}		
                        } else {
                                if (lineTemp[1].substring(0,2).equalsIgnoreCase("dc")) {
					if (lineTemp[1].length() > 3) {
						data[nextPosition] = lineTemp[1].substring(3);
					} else { data[nextPosition] = "" + 0; }
	
					newSymbolTableLine[0] = lineTemp[0];
					newSymbolTableLine[1] = "" + nextMemorySlot;
					symbolTable.add(i, newSymbolTableLine);
					++nextMemorySlot;
					++nextPosition;
				}
                        }
                }
        }


	if (!defStdin.equals("")) {
		data[nextPosition] = "STDIN " + defStdin;
		++nextPosition;
	}
	if (!defStdout.equals("")) {
		data[nextPosition] = "STDOUT " + defStdout;
	}

	
// make new SymbolTable
	String[][] newSymbolTable = new String[symbolTable.size()][2];
	for (int i= 0; i < newSymbolTable.length; ++i) { 
		newSymbolTable[i] = (String[])symbolTable.get(i);
	}

	compileDebugger.finalFirstPhase(newCode, data, newSymbolTable);
	return compileDebugger.lineCompiled();	

    }

    /** This function transforms any commands to binary and stores both
	forms in the code array, or sets any initial data values in the
	data array. It uses CompileConstants as its aid. 
	@param line The line of code to process.
	@return A CompileInfo object describing what was done, or 
	null if the second round and thus the compilation has been 
	completed. */
    private CompileInfo secondRoundProcess(String line) throws TTK91CompileException { 

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
	boolean allOk = false;
	String[] parsedLine = parseLine(line);
	if (parsedLine == null) {
		throw new TTK91CompileException();
	} else {
		if (parsedLine[1].equalsIgnoreCase("nop")) {
			allOk = true;
		}

		if (parsedLine[1].equalsIgnoreCase("store") || 
					parsedLine[1].equalsIgnoreCase("load") ) {
			if (parsedLine[4].equals("")) {
// STORE must have an address (not just r2)
				throw new TTK91CompileException();
			}
			allOk = true;
		}
		
		if (parsedLine[1].equalsIgnoreCase("pop")) {
			if (!parsedLine[4].equals("") || parsedLine[5].equals("")) {
				 throw new TTK91CompileException();
			}
			allOk = true;
		}

		if (parsedLine[1].equalsIgnoreCase("jump")) {
			if (parsedLine[4].equals("") && parsedLine[5].equals("")) {
				throw new TTK91CompileException();
			} else { allOk = true; }
		}
	
		if ("jneg-jzer-jpos-jnneg-jnzer-jnpos".indexOf(parsedLine[1]) != -1) {
			 if (parsedLine[4].length() > 0 && parsedLine[5].equals("")) {
                                throw new TTK91CompileException();
                        } else { allOk = true; }
		}
		
		if ("jles-jequ-jgre-jnles-jnequ-jngre".indexOf(parsedLine[1]) != -1) {
			 if (parsedLine[4].equals("") && parsedLine[5].equals("")) {
                                throw new TTK91CompileException();
                        } else { allOk = true; }
		}
	
// other commands require first register, and address of second register
		if (!allOk) {
			if (parsedLine[2].equals("") || 
				(parsedLine[4].equals("") && parsedLine[5].equals(""))) {
				throw new TTK91CompileException();
			} else { allOk = true; }
                }
	
// check variables!
		

	}
	return compileDebugger.lineCompiled();

    }


    /**	This method parses a String and tries to find a label, opCode and all the 
	other parts of a Command line.
	@param symbolicOpCode Symbolic form of an operation code. */  
    public String[] parseLine(String symbolicOpcode) {

	String label = "";
	String opcode = "";		
	String firstRegister = "";
	String addressingMode = "";
	String secondRegister = "";
	String address = "";
	
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
	if (fieldEnd == -1) { 
		wordTemp = symbolicOpcode.substring(nextToCheck);
		fieldEnd = symbolicOpcode.length();  
	} else {
		wordTemp = symbolicOpcode.substring(nextToCheck, fieldEnd);
	}
	if (symbolicInterpreter.getOpcode(wordTemp) == -1) { 	// try to find a label (not a valid 
								//opCode)
				// label must have one non-number (valid chars A-÷, 0-9 and _)
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
			wordTemp = symbolicOpcode.substring(nextToCheck); 
			fieldEnd = symbolicOpcode.length();
		} else { 
			wordTemp = symbolicOpcode.substring(nextToCheck, fieldEnd); 
		}
		
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

	if (nextToCheck < symbolicOpcode.length()) {
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
	}

	if (symbolicInterpreter.getRegisterId(address) != -1) {
                secondRegister = address;
                address = "";
        }

	if (opcode.length() > 0) {
		if (opcode.charAt(0) == 'j' || opcode.charAt(0) == 'J') {
			if (addressingMode.equals("=") || address.equals("")) return null;	
		}
	}	

	if (addressingMode.equals("=") && address.equals("")) return null;
	if (opcode.equalsIgnoreCase("load") && address.equals("")) return null;
	if (opcode.equalsIgnoreCase("store") && address.equals("")) return null;
	if (opcode.equalsIgnoreCase("store") && addressingMode.equals("=")) return null;

	parsedLine = new String[6];
	parsedLine[0] = label.trim();
	parsedLine[1] = opcode.trim();
	parsedLine[2] = firstRegister.trim();
	parsedLine[3] = addressingMode.trim();
	parsedLine[4] = address.trim();
	parsedLine[5] = secondRegister.trim();
	return parsedLine;
    }

    /** This functions tries to find a compiler command from the line. Works much like parseLine
	but this time valid opcodes are DEF, EQU, DC and DS.
	@param line String representing one line from source code.
    	@return String array with label in position 0, command in the position 1 and position 2 
	contains the parameter.
      */	
    public String[] parseCompilerCommandLine(String line) {

	int fieldEnd = 0;
	int nextToCheck = 0;	
	String label = "";
	String opcode = "";
	String value = "";
	int intValue;
	String[] parsedLine;

/* preprosessing */

	line = line.trim();
	line = line.toLowerCase();
	line = line.replace('\t',' ');
	fieldEnd = line.indexOf(";");
	if (fieldEnd != -1) { line = line.substring(0, fieldEnd); }
	
/* LABEL opcode value */
	fieldEnd = line.indexOf(" ");
	if (fieldEnd == -1) {
		label = line.substring(nextToCheck);
		fieldEnd = line.length();
	} else {
		label = line.substring(nextToCheck, fieldEnd);		
	}

	nextToCheck = fieldEnd + 1;	

/* label OPCODE value */	
	if (nextToCheck < line.length()) {
		while (line.charAt(nextToCheck) == ' ') { ++nextToCheck; }
		fieldEnd = line.indexOf(' ', nextToCheck);
		if (fieldEnd == -1) {
			opcode = line.substring(nextToCheck);
			fieldEnd = line.length();
		} else {
			opcode = line.substring(nextToCheck, fieldEnd);
		}
		if ("dcdsequdef".indexOf(opcode) == -1) {
			return null;			
		}
		nextToCheck = fieldEnd;	
	}

/* label opcode VALUE */
	if (nextToCheck < line.length()) {
		while (line.charAt(nextToCheck) == ' ') { ++nextToCheck; }
		value = line.substring(nextToCheck);
		if (value.length() > 0) {
			try {
				intValue = Integer.parseInt(value);
				if (opcode.equalsIgnoreCase("ds") && intValue < 1) { 
					return null; 
				}
			} catch (NumberFormatException e) { return null; }
		}
	}	

	if (!opcode.equalsIgnoreCase("dc") && value.equals("")) return null;

	parsedLine = new String[3];
	parsedLine[0] = label;
	parsedLine[1] = opcode;
	parsedLine[2] = value;
	return parsedLine;
    }

    /** This method splits a line into a string array.
	@param line String to be split.
	@return Split line.
      */	
    public String[] splitALine(String line){
/* Line terminators 

A line terminator is a one- or two-character sequence that marks  the end of a line of the input 
character sequence.  The following are  recognized as line terminators:   
   
 A newline (line feed) character†('\n'),     
 A carriage-return character followed immediately by a newline    character†("\r\n"),     
 A standalone carriage-return character†('\r'),     
 A next-line character†('\u0085'),     
 A line-separator character†('\u2028'), or     
 A paragraph-separator character†('\u2029).   
*/	
        String temp = ""; 
        while (line.indexOf("\r\n") != -1) {
                temp = line.substring(0, line.indexOf("\r\n")) +
                                        line.substring(line.indexOf("\r\n") +1);
                line = temp;
        }
        line = line.replace('\r', '\n');
        line = line.replace('\u0085', '\n');
        line = line.replace('\u2028', '\n');
        line = line.replace('\u2029', '\n');
        return line.split("\n");
    }
}
