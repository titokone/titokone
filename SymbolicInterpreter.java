package fi.hu.cs.titokone;

import java.util.HashMap;

/** This class contains the bulk data needed to translate commands to 
    opcodes etc. The main purpose for this class is to provide methods to convert
    symbolic commands to binary form.  */
public class SymbolicInterpreter extends Interpreter {
    /** This hashtable contains the opcode values keyed to the symbolic
	commands. */
    private HashMap opcodes;
    private HashMap addressModes;
    private HashMap registers;
    private final int NOTVALID = -1;
    private final int EMPTY = -1;
    private final String validLabelChars = "0123456789abcdefghijklmnopqrstuvwxyzåäö_";

    /** This constructor sets up a SymbolicInterpreter instance. It calls
	the private method dataSetup() to set up its data structures. */
    public SymbolicInterpreter() {}

    /** This method sets up the HashTables. */
    private void dataSetup() {}

    /** This method checks if a command is a valid opCode.
	@param command String form of a possible opCode.*/
    public int getOpcode(String command) { }

    /** This method transforms an addressing mode (=, @ or nothing) to 
	a number identifying it. 
	@param identifier String form of an addressing mode.*/
    public int getAddressingMode(String identifier) { }

    /** This method returns the binary form of a given register as an integer.
	@param registerName String form of a register (R0-R7, SP or FP).
    public int getRegisterId(String registerName) { }

    /** Transforms a String to an int. Operates much like the compiler. First it
     	checks whether there is a label, then checks the opCode, then register, address mode, 
     	other register and finally the address. There can be extra spaces and tabs but nothing
     	more. All the rest must begin with a ;-sign that acts as a comment tag.
     	@param symbolicOpCode Symbolic operation code (LOAD R1, 100)
     	@return Int format of a symbolic opCode. (00000010 00101000 00000000 01100100 as int)
     */
    public int stringToBinary(String symbolicOpCode) { }
	int opCode = 0;		
	int firstRegister = 0;	// values if nothing else is found (like if String is "NOP")
	int addressingMode = 0;
	int secondRegister = 0;
	int address = 0;
	
	String wordTemp = "";
	int nextToCheck = 0;	// for looping out the spacing
	int fieldEnd = 0;	// searches the end of a field (' ', ',')

	symbolicOpCode = symbolicOpCode.toLowerCase();
	symbolicOpCode = symbolicOpCode.trim();
	if (symbolicOpCode.length() == 0) { return EMPTY; }

/*label or opCode*/
	fieldEnd = symbolicOpCode.indexOf(" ");
	if (fieldEnd == -1) {
		fieldEnd = symbolicOpCode.length();
	} 

	wordTemp = symbolicOpCode.substring(nextToCheck, fieldEnd);
	opCode = getOpCode(wordTemp);	
	if (opCode == -1) { 	// try to find a label (not a valid opCode)
				// label must have one non-number (valid chars A-Ö, 0-9 and _)
		boolean allCharsValid = true;
		boolean atLeastOneNonNumber = false;
		for (int i = 0; i < wordTemp.length(); ++i) {
			if (atLeastOneNonNumber == false) {
				if (validLabelChars.indexOf(wordTemp.charAt(i)) > 9) {
					atLeastOneNonNumber = true;
				}
			} 
			if (validLabelChars.indexOf(wordTemp.charAt(i)) < 0) {
				allCharsValid = false;
			}
		}
		if (atLeastOneNonNumber == false || allCharsValid == false) { return NOTVALID; }
		
				// opCode must follow the label
		fieldEnd = symbolicOpCode.indexOf(" ", nextToCheck);
        	if (fieldEnd == -1) {
                	fieldEnd = symbolicOpCode.length();
        	}
        	opCode = getOpCode(symbolicOpCode.substring(nextToCheck, fieldEnd));
		if (opCode < 0) { return NOTVALID; }
	}			// now opCode has integer value of an opcode.

	nextToCheck = fieldEnd + 1;

	/* TODO if jump or other not normal then do something.*/


	/*register*/
	if (nextToCheck < symbolicOpCode.length()) {
		if (symbolicOpCode.charAt(nextToCheck) != ' ') { return NOTVALID; }
		while (nextToCheck == ' ') { ++nextToCheck }	// needs a space, then R0-R7, SP or FP
		fieldEnd = symbolicOpCode.indexOf(" ", nextToCheck);
                if (fieldEnd == -1) {
                        fieldEnd = symbolicOpCode.length();
                }

		firstRegister = getRegisterId(symbolicOpCode.substring(nextToCheck, fieldEnd));
		if (firstRegister == NOTVALID) { return NOT VALID; }

		nextToCheck = fieldEnd + 1;
	}

	/*addressingMode*/
	if (nextToCheck < symbolicOpCode.length()) {
		while (nextToCheck == ' ') { ++nextToCheck; }
		if (symbolicOpCode.charAt(nextToCheck) == '=' || 
				symbolicOpCode.charAt(nextToCheck) == '@') {
			addressingMode = getAddressingMode(symbolicOpCode.charAt(nextToCheck));
			++nextToCheck;
		} else { addressingMode = getAddressingMode(""); }
	}

	/*otherRegister*/
	while (nextToCheck == ' ') { ++nextToCheck; }
	/*Check if r0-r7 sp or fp*/

	/*address*/

}
