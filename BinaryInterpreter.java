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
	representation. 
	@param binaryCommand The command's binary-form representation.
	@return The symbolic representation if it is valid enough.  If
	the opcode is unknown, the memory address mode faulty or the
	register ids do not point to real registers,
	BinaryInterpreter.GARBLE is returned. */
    public String binaryToString(int binaryCommand) {}

    /** This method deals with the more complicated bit of extracting
	the parameter string needed for this command in translating
	a binary command to a string.
	@param commandParameters A binary command with its opcode 
	bits shifted out. 
	@return A string to add after the opcode with a space in between,
	or an empty string if no parameters are wanted. */
    private String getParameterString(int commandParameters) {}
}
