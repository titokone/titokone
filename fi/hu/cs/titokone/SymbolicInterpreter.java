//TODO hashtables, methods that check opcodes, registers etc.

package fi.hu.cs.titokone;

import java.util.HashMap;

/** This class contains the bulk data needed to translate commands to 
    opcodes etc. The main purpose for this class is to provide methods to convert
    symbolic commands to binary form.  */
public class SymbolicInterpreter extends Interpreter {
    /** This hashtable contains the opcode values keyed to the symbolic commands. */
    private HashMap opcodes;
    /** This hashtable contains integer values for given addressingmodes. */
    private HashMap addressModes;
    /** This hashtable  */
    private HashMap registers;
    
    /** This field holds the value to be returned if given value is invalid. */
    private final int NOTVALID = -1;
    /** This field holds the value to be returned if given value results in an empty line. */
    private final int EMPTY = 0;

// constructor

    /** This constructor sets up a SymbolicInterpreter instance. It calls
	the private method dataSetup() to set up its data structures. */
    public SymbolicInterpreter() {}

    /** This method sets up the HashTables. */
    private void dataSetup() {}

// public methods --> 

    /** This method checks if a command is a valid opCode.
	@param command String form of a possible opCode.*/
    public int getOpcode(String command) { }
	
    /** This method transforms an addressing mode (=, @ or nothing) to 
	a number identifying it. 
	@param identifier String form of an addressing mode.
	@return Int value telling how many times memory must be accessed. */
    public int getAddressingMode(String identifier) { 

	identifier = identifier.trim();
	if (identifier.equals(""))  { return 1; }
	if (identifier.equals("=")) { return 0; }
	if (identifier.equals("@")) { return 2; }
	return NOTVALID;
    }

    /** This method returns the binary form of a given register as an integer.
	@param registerName String form of a register (R0-R7, SP or FP).
	@return Int value of a given register. */
    public int getRegisterId(String registerName) { 
	registerName = registerName.trim();
	for (int i = 0; i < 8; ++i) {
		if (registerName.equalsIgnoreCase("r" + i)) { return i; }		
	}
	if (registerName.equalsIgnoreCase("sp")) { return 6; }
	if (registerName.equalsIgnoreCase("fp")) { return 7; }
	if (registerName.equals("")) { return EMPTY; } else { return NOTVALID; }
    }

    /** This method coverts a complete command in a symbolic form to a binary form.
        caller must split up the original command and give the parts as parameters
	@param opCode String form of an operation code. (STORE)
	@param firstRegister String form of a first register. (R0-R7, SP or FP)
	@param addressingMode = or @ or an empty string that representes the memory addressing 
	mode.
	@param address String form of an address, must be a valid int.
	@param otherRegister String form of an other register. (R0-R7, SP or FP)
     	@return Int format of a symbolic opCode. (etc 00000010 00101000 00000000 01100100 as int)
     */
    public int stringToBinary(String opCode, String firstRegister, String addressingMode, 
							String address, String otherRegister) { 
	
	

    }

}
