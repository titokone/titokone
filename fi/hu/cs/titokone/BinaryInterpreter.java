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

    /** This hashmap contains parameters for each command with Integer forms
	of their opcodes as keys.*/
    private HashMap parameters;


    /** This constructor sets up a binaryinterpreter and initializes the 
	internal command information data structures. */
    
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
    //TODO siivousta ja tarkistuksia
    public String binaryToString(int binaryCommand) {
	int command = binaryCommand;
       	
	String s = getOpCodeFromBinary(command);
	if (s==null)
	    return GARBLE;
	Integer opcode = new Integer(s);
       	s=(String)commands.get(opcode);
	if (s==null)
	    return GARBLE;
	if(getMemoryModeFromBinary(command)==null)
	    return GARBLE;
	//System.out.println("mm "+getMemoryModeFromBinary(command));
	Integer param = (Integer)parameters.get(opcode);
	
	switch(param.intValue()){
	case 0:{ // No parameters
	    return s;
	}
	case 1:{ // TODO Hmmh, rekisterit vai vain toinen?
	    return s;
	}
	case 2:{ //SP and register
	    s = s +" "+getFirstRegisterFromBinary(command);
	    s = s +", "+getSecondRegisterFromBinary(command);
	    return s;
	}
	case 3:{ //only SP
	    s+=" "+getFirstRegisterFromBinary(command);
	    return s;
	}
	case 4:{ //address only TODO Not used?
	    String mem = getMemoryModeFromBinary(command);

	    if (mem.equals(null))
		return GARBLE;

	    s+=" "+mem;
	    s+= getAddressFromBinary(command); 
	    s = s + "(" + getSecondRegisterFromBinary(command);
	    s = s + ")";
	    return s;   
	}
	case 5:{ //Full parameters
	    s = s + " " + getFirstRegisterFromBinary(command);
	    s = s + ", " + getMemoryModeFromBinary(command);
	    s = s + getAddressFromBinary(command);
	    s = s + "(" + getSecondRegisterFromBinary(command);
	    s = s + ")";
	   
	    return s;
	    }
	case 6:{//Full with less fetches
	    
	    String mem = getMemoryModeFromBinary(command);
	    if (mem==null)
		return GARBLE;
	   

	    s = s + " " + getFirstRegisterFromBinary(command);
	    s = s + ", " + mem;
	    s = s + getAddressFromBinary(command);
	    s = s + "(" + getSecondRegisterFromBinary(command);
	    s = s + ")";

	    return s;
	}

	 
	case 7:{//Register and device
	    s+=" "+getFirstRegisterFromBinary(command);
	    
	    Integer device = new Integer(getAddressFromBinary(command));
	    if(!getMemoryModeFromBinary(command).equals("="))
	       return GARBLE;

	    if (device.intValue()==0){
		s+=", =CRT";
		return s;
	    }
	    if (device.intValue()==1){
		s+=", =KBD";
		return s;
	    }
	    if (device.intValue()==6){
		s+=", =STDIN";
		return s;
	    }
	    if (device.intValue()==7){
		s+=", =STDOUT";
		return s;
	    }
	    
	    return GARBLE;
	}
	case 8:{ //Address with less fetches
	    String mem = getMemoryModeFromBinary(command);
	    if (mem==null)
		return GARBLE;

	    s = s + " " + mem;
	    s = s + getAddressFromBinary(command);
	    s = s + "(" + getSecondRegisterFromBinary(command);
	    s = s + ")";
	   
	    return s;
	}
	case 9:{//SVC SP and operation
	    s+=" "+getFirstRegisterFromBinary(command);
	    s+=", =";
	    Integer service = new Integer(getAddressFromBinary(command));
	    if (service.intValue()==11){
		s+="HALT";
		return s;
	    }
	    if (service.intValue()==12){
		s+="READ";
		return s;
	    }
	    if (service.intValue()==13){
		s+="WRITE";
		return s;
	    }
	    if (service.intValue()==14){
		s+="TIME";
		return s;
	    }
	    if (service.intValue()==15){
		s+="DATE";
		return s;
	    }
	    
	    return GARBLE;
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
	/*if (command == 0)  //if command is zero, then return nop
	    return "0";
	//then check if command has no operation code
	if (command > 0 && command < 16777216)
	return null;*/
	
	//get opcode and get its name and return it
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
	int i = command >> 21; //remove addr, 2. register, memorymode
	i = i & 7;             //get 1. register and check its name
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
	int i = command >> 19; //remove addr and second register
	i = i & 3;             //get memorymode
	String operationCode = getOpCodeFromBinary(binaryCommand);
	Integer opcode = new Integer(operationCode);
	Integer params = (Integer)parameters.get(opcode);
	
	/* Store and jumps use less memoryfetches so we need to return
	   different symbols for them.*/
	
	if(params.intValue()==6||params.intValue()==8){
	    if (i==0)
		return "";
	    if (i==1)
		return "@";
	    if(i==2||i==3)
		return null;
	}	
	else{
	    if (i==0)
		return "=";
	    if (i==1)
		return "";
	    
	    if (i==2)
		return "@";
	}    
	return null;
	
    }

    /** If a command has second register value, this function returns it
	"" or R0 to R7).
	@param binaryCommand The command's binary-form representation.
	@return Possible other register from binary command in a String format.
      */
    public String getSecondRegisterFromBinary(int binaryCommand) {
	int command = binaryCommand;
	int i = command >> 16; //remove address and get first three bits
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
	int i = command & 65535; //AND first 16 bits
	
	String binaryString = intToBinary(i,16);
	
	i=binaryToInt(binaryString,true);



	/*	
	if (i>=32768){
	    i=i-32768;
	    i=i*(-1);
		
	}
	*/
	String s = "" + i;
	return s;

    }

    /**	This method converts int values to binary-string. intToBinary(1,2) --> "01"
	@param value Int value to be converted.
	@param bits How many bits can be used .
	@return String representation of a said Int.
      */
    public String intToBinary(long value, int bits) {
/* TODO if bits too few, i.e. 10,2 then result is "11" */
	char[] returnValue = new char[bits];
	boolean wasNegative = false;

	if (value < 0) { 
		wasNegative = true; 
		++value;
		value = (value * -1);
	}

	
	for (int i = 0; i < bits; ++i) returnValue[i] = '0';

	for (int i = returnValue.length - 1; i > -1; --i) {
		if (value >= (int)Math.pow(2.0, i * 1.0)) {
			returnValue[returnValue.length - 1 - i] = '1';
			value = value - (int)Math.pow(2.0, i * 1.0);
		}
	}

	if (wasNegative) { 
		for (int i = 0; i < returnValue.length; ++i)
			if (returnValue[i] == '0') returnValue[i] = '1'; 
			else returnValue[i] = '0';
	}

	return new String(returnValue);
    }

    /**	This method converts String that contains a binary to int. binaryToInt("01") --> 1
	@param binaryValue String representing the binary, if other than {0,1} then null.
	@param signIncluded Boolean value telling whether 11 is -1 or 3 i.e. will the leading
	one be interpreted as sign-bit.
	@return Int value of a Binary.
	*/
    public int binaryToInt(String binaryValue, boolean signIncluded) {
/* TODO ! returns 0 when error! exception perhaps? */
	boolean isNegative = false;
	int value = 0;

	if (signIncluded) { 
		if (binaryValue.charAt(0) == '1') { 
			isNegative = true; 
			binaryValue = binaryValue.replace('1', '2');
			binaryValue = binaryValue.replace('0', '1');
			binaryValue = binaryValue.replace('2', '0');
		}
	}
		
	for (int i = 0; i < binaryValue.length(); ++i) {
		if (binaryValue.charAt(binaryValue.length() - 1 -i) == '1') {
			value = value + (int)Math.pow(2.0, i * 1.0);	
		} else {
			if (binaryValue.charAt(binaryValue.length() - 1 -i) != '0') {
				return 0;
			}
		}		
	}

	if (isNegative) {
		value = (value + 1) * -1;
	}
	return value;
    }


}
