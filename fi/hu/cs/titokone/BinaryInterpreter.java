package fi.hu.cs.titokone;

import java.util.HashMap;

/** This class contains the information to translate a command in 
    binary form to its symbolic string form. Naturally, if a command
    has been transformed from its symbolic form to binary, the transformation
    back will not restore used symbols as we cannot tell, even with the help
    of the symbol table, what symbol has been used where. */
public class BinaryInterpreter extends Interpreter {
    public static final String GARBLE = "";
	
    /** This hashmap contains the symbolic commands as strings, with 
	Integer forms of their opcodes as keys. */
    private HashMap commands;

    private HashMap parameters;
    /** This String[] contains a string for each addressing mode. They can 
	be found by using the address mode number as index. */
    private String[] addressModes; //Mihin tätä tarvitaan?
    /** This String[] contains the register names. The names can be found
	by using the register id as index. */
    private String[] registerNames; //Mihin tätä tarvitaan?

    /** This constructor sets up a binaryinterpreter and initializes the 
	internal command information data structures. */
    //TODO
    public BinaryInterpreter() {
	commands = new HashMap(37);
	parameters = new HashMap(37);
	for (int i = 0; i<37; i++){
	    commands.put(commandData[i][1], commandData[i][0]);
	    parameters.put(commandData[i][1], commandData[i][2]);
	}

    }


    /** This function transforms a binary-form command to its symbolic
        representation. Binary is interpreted in two parts, firstly the 
        first 8 bits of the binary representation are extracted and if 
        it is a valid opcode then check the needed bits if they make 
        any sense. Like if the opcode is a NOP then all the bits can be 0, 
        or anything else. Also check what to return. Like in a case of NOP 
        there is no need to return other parameters. 
        http://www.cs.helsinki.fi/u/ahakkine/Tito/koksi.kaskyt Check 
        Compiler.java for more info on checking a binary. 
	@param binaryCommand The command's binary-form representation.
	@return The symbolic representation if it is valid enough.  If
	the opcode is unknown, the memory address mode faulty or the
	register ids do not point to real registers,
	BinaryInterpreter.GARBLE is returned. */
    //TODO parametrien määrä
    public String binaryToString(int binaryCommand) {
	int command = binaryCommand;
       	
	String s = getOpCodeFromBinary(command);
	if (s==null)
	    return GARBLE;
	Integer opcode = new Integer(s);
       	s=(String)commands.get(opcode);
	if (s==null)
	    return GARBLE;
	
	Integer param = (Integer)parameters.get(opcode);

	switch(param.intValue()){
	case 0:{
	    return s;
	}
	case 1:{ // Hmmh, rekisterit vai vain toinen?
	    return s;
	}
	case 2:{
	    s = s +" "+getFirstRegisterFromBinary(command);
	    s = s +", "+getSecondRegisterFromBinary(command);
	}
	case 4:{
	    s = s +" "+getAddressFromBinary(command); 
	    return s;   
	}
	case 5:{
	    s = s + " " + getFirstRegisterFromBinary(command);
	    s = s + ", " + getMemoryModeFromBinary(command);
	    s = s + getAddressFromBinary(command);
	    s = s + "(" + getSecondRegisterFromBinary(command);
	    s = s + ")";
	    s = s + " ";
	    return s;
	    }
	}
	return s;



    }

   /* Translates the opcode and checks if it is a valid one, then
   calls the getParameterString to sort out the rest of the binary.*/

    /**	This command returns the operation code from a binary
	@param binaryCommand The command's binary-form representation.
	@return Operation code in a String format.
      */
    public String getOpCodeFromBinary(int binaryCommand) {
	int command = binaryCommand;
	if (command == 0)
	    return "0";
	if (command > 0 && command < 16777216)
	    return null;

	Integer opcode = new Integer(command >> 24);
	if(commands.get(opcode)!=null){
	    String s = "" + opcode;
	    return s;
	}
	return null;
    }

    /** If a command has a first register value then this function returns
	it. (NOP has none, thus it would return "") Normally value would
	be a value from R0 to R7
	@param binaryCommand The command's binary-form representation.
	@return Possible register value in a String format.
      */
    public String getFirstRegisterFromBinary(int binaryCommand) {
	int command = binaryCommand;
	int i = command >> 21;
	i = i & 7;
	String s = (String)registerData[i][0];
	return s;
    }

    /** Function returns possible memory address mode from a binary command
	given as a parameter. Four possible values (non excistent, and
	three legal values)
	@param binaryCommand The command's binary-form representation.
	@return Memory address mode from binary command in a String format.
      */
    public String getMemoryModeFromBinary(int binaryCommand) {
	int command = binaryCommand;
	int i = command >> 19;
	i = i & 3;
	if (i==0)
	    return "=";
	if (i==1)
	    return "";
	if (i==2)
	    return "@";
	return null;

    }

    /** If a command has second register value, this function returns it
	"" or R0 to R7).
	@param binaryCommand The command's binary-form representation.
	@return Possible other register from binary command in a String format.
      */
    public String getSecondRegisterFromBinary(int binaryCommand) {
	int command = binaryCommand;
	int i = command >> 16;
	i = i & 7;
	String s = (String)registerData[i][0];
	return s;


    }

    /** If a given binary represents a valid command that has an address 
	then this function returns it.
	@param binaryCommand The command's binary-form representation.
	@return Address part of the binary command in a String format.
      */
    public String getAddressFromBinary(int binaryCommand) {
	int command = binaryCommand;
	int i = command & 65535;
	String s = "" + i;
	return s;

    }

    /** This method deals with the more complicated bit of extracting
	the parameter string needed for this command in translating
	a binary command to a string.
	@param commandParameters A binary command with its opcode 
	bits shifted out. 
	@return A string to add after the opcode with a space in between,
	or an empty string if no parameters are wanted. 
    */
    //TODO mutta mihin tätä tarvitaan? Saman hoitaa jo binaryToString helpommin
    private String getParameterString(int commandParameters) {
	return null;
    }
}
