package fi.hu.cs.titokone;

import java.util.HashMap;

/** This class contains the information to translate a command in 
    binary form to its symbolic string form. Naturally, if a command
    has been transformed from its symbolic form to binary, the transformation
    back will not restore used symbols as we cannot tell, even with the help
    of the symbol table, what symbol has been used where. */
public class BinaryInterpreter extends Interpreter {
    public static final String GARBLE = "****";
	
    /** This hashmap contains the symbolic commands as strings, with 
	Integer forms of their opcodes as keys. */
    private HashMap commands;
    /** This hashmap contains a string for each addressing mode. They can 
	be found by using the address mode number as index. */
    private String[] addressModes;
    /** This hashmap contains the register names. The names can be found
	by using the register id as index. */
    private String[] registerNames;

    /** This constructor sets up a binaryinterpreter and initializes the 
	internal command information data structures. */
    public BinaryInterpreter() {}

    /** This function transforms a binary-form command to its symbolic
representation. Binary is interpreted in two parts, firstly the first 8 bits of the binary 
representation are extracted and if it is a valid opcode then check the needed bits if they make 
any sense. Like if the opcode is a NOP then all the bits can be 0, or anything else. Also check 
what to return. Like in a case of NOP there is no need to return other parameters.
http://www.cs.helsinki.fi/u/ahakkine/Tito/koksi.kaskyt Check Compiler.java for more info on 
checking a binary. 
	@param binaryCommand The command's binary-form representation.
	@return The symbolic representation if it is valid enough.  If
	the opcode is unknown, the memory address mode faulty or the
	register ids do not point to real registers,
	BinaryInterpreter.GARBLE is returned. */
    public String binaryToString(int binaryCommand) {}
/* Translates the opcode and checks if it is a valid one, then
calls the getParameterString to sort out the rest of the binary.*/

    /**	This command returns the operation code from a binary
	@param binaryCommand The command's binary-form representation.
	@return Operation code in a String format.
      */
    public String getOpCodeFromBinary(int binaryCommand) {}

    /** If a command has a first register value then this function returns
	it. (NOP has none, thus it would return "") Normally value would
	be a value from R0 to R7
	@param binaryCommand The command's binary-form representation.
	@return Possible register value in a String format.
      */
    public String getFirstRegisterFromBinary(int binaryCommand) {}

    /** Function returns possible memory address mode from a binary command
	given as a parameter. Four possible values (non excistent, and
	three legal values)
	@param binaryCommand The command's binary-form representation.
	@return Memory address mode from binary command in a String format.
      */
    public String getMemoryModeFromBinary(int binaryCommand) {}

    /** If a command has second register value, this function returns it
	"" or R0 to R7).
	@param binaryCommand The command's binary-form representation.
	@return Possible other register from binary command in a String format.
      */
    public String getSecondRegisterFromBinary(int binaryCommand) {}

    /** If a given binary represents a valid command that has an address 
	then this function returns it.
	@param binaryCommand The command's binary-form representation.
	@return Address part of the binary command in a String format.
      */
    public String getAddressFromBinary(int binaryCommand) {}

    /** This method deals with the more complicated bit of extracting
	the parameter string needed for this command in translating
	a binary command to a string.
	@param commandParameters A binary command with its opcode 
	bits shifted out. 
	@return A string to add after the opcode with a space in between,
	or an empty string if no parameters are wanted. 
*/
    private String getParameterString(int commandParameters) {}
}
