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
    
// constructor

    /** This constructor sets up a SymbolicInterpreter instance. It calls
	the private method dataSetup() to set up its data structures. */
    public SymbolicInterpreter() {
	dataSetup();
    }

    /** This method sets up the HashTables. */
    private void dataSetup() {
        opcodes = new HashMap(commandData.length);
        for (int i = 0; i < commandData.length; i++) 
		opcodes.put(commandData[i][0], commandData[i][1]);

	addressModes = new HashMap(addressingData.length);
	for (int i = 0; i < addressingData.length; i++) 
		addressModes.put(addressingData[i][0], addressingData[i][1]);

	registers = new HashMap(registerData.length);
	for (int i = 0; i < registerData.length; i++) 
		registers.put(registerData[i][0], registerData[i][1]);
    }

// public methods --> 

    /** This method checks if a command is a valid opCode.
	@param command String form of a possible opCode.*/
    public int getOpcode(String command) {
	command = command.trim();
	command = command.toUpperCase();
	Integer value = (Integer)opcodes.get(command);
	if (value != null) { return value.intValue(); }
	return -1;
    }
	
    /** This method transforms an addressing mode (=, @ or nothing) to 
	a number identifying it. 
	@param identifier String form of an addressing mode.
	@return Int value telling how many times memory must be accessed. */
    public int getAddressingMode(String identifier) { 
	identifier = identifier.trim();
	identifier = identifier.toUpperCase();
	Integer value = (Integer)addressModes.get(identifier);
	if (value != null) { return value.intValue(); }
	return -1;
    }

    /** This method returns the binary form of a given register as an integer.
	@param registerName String form of a register (R0-R7, SP or FP).
	@return Int value of a given register. */
    public int getRegisterId(String registerName) {
	registerName = registerName.trim();
	registerName = registerName.toUpperCase(); 
	Integer value = (Integer)registers.get(registerName);
        if (value != null) { return value.intValue(); }
    	return -1;
    }

    /** This method coverts a complete command in a symbolic form to a binary form.
        caller must split up the original command and give the parts as parameters
	@param opcode String form of an operation code. (STORE)
	@param firstRegister String form of a first register. (R0-R7, SP or FP)
	@param addressingMode = or @ or an empty string that representes the memory addressing 
	mode.
	@param address String form of an address, must be a valid int.
	@param otherRegister String form of an other register. (R0-R7, SP or FP)
     	@return Int format of a symbolic opCode. (etc 00000010 00101000 00000000 01100100 as int)
     */
    public int stringToBinary(String opcode, String firstRegister, String addressingMode, 
							String address, String otherRegister) { 
	boolean allOk = true;
	boolean addressEmpty;

	if (address.equals("")) { 
		addressEmpty = true; 
	} else { 
		addressEmpty = false;
	}

	int opcodeAsInt = getOpcode(opcode);
	int firstRegisterAsInt = getRegisterId(firstRegister);
	int addressingModeAsInt = getAddressingMode(addressingMode);
	int addressAsInt = 0;
	int secondRegisterIdAsInt = getRegisterId(otherRegister);

	if (address.equals("")) address = "0";

	try { addressAsInt = Integer.parseInt(address); } catch(NumberFormatException e) {
		allOk = false;
	}

	if (opcodeAsInt < 0) { allOk = false; }
	if (firstRegisterAsInt < 0) { firstRegisterAsInt = 0; }
	if (addressingModeAsInt < 0) { addressingModeAsInt = 1; }
	if (secondRegisterIdAsInt < 0) { secondRegisterIdAsInt = 0; }

	if (allOk) {

		// if store or jump then addressinmode as int -= 1;
		if (opcodeAsInt == 1 || 
		    (opcodeAsInt >= 32 && opcodeAsInt <= 44) ||
		    (addressEmpty && !otherRegister.equals(""))  ||
		    opcodeAsInt == 49
 		) {
			addressingModeAsInt = addressingModeAsInt - 1;
			if (addressingModeAsInt == -1) { ++addressingModeAsInt; }
		}

		String binary = intToBinary(opcodeAsInt, 8) + intToBinary(firstRegisterAsInt, 3) + 
			intToBinary(addressingModeAsInt, 2) + 
			intToBinary(secondRegisterIdAsInt, 3) + intToBinary(addressAsInt, 16);
		return binaryToInt(binary, true);		
	}


	return -1;
    }

    /**	This method converts int values to binary-string. intToBinary(1,2) --> "01"
	@param value Int value to be converted.
	@param bits How many bits can be used .
	@return String representation of a said Int.
      */
    public String intToBinary(long value, int bits) {
/* if bits too few, i.e. 10,2 then result is "11" */
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
/*  returns 0 when error! exception perhaps? */
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
