// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.util.HashMap;

/**
 * This class contains the bulk data needed to translate commands to
 * opcodes etc. The main purpose for this class is to provide methods to convert
 * symbolic commands to binary form.
 */
public class SymbolicInterpreter extends Interpreter {
    /**
     * This hashtable contains the opcode values keyed to the symbolic commands.
     */
    private HashMap<Object, Object> opcodes;
    /**
     * This hashtable contains integer values for given addressingmodes.
     */
    private HashMap<Object, Object> addressModes;
    /**
     * This hashtable
     */
    private HashMap<Object, Object> registers;

// constructor

    /**
     * This constructor sets up a SymbolicInterpreter instance. It calls
     * the private method dataSetup() to set up its data structures.
     */
    public SymbolicInterpreter() {
        dataSetup();
    }

    /**
     * This method sets up the HashTables.
     */
    private void dataSetup() {
        opcodes = new HashMap<Object, Object>(commandData.length);
        for (int i = 0; i < commandData.length; i++) {
            opcodes.put(commandData[i][0], commandData[i][1]);
        }

        addressModes = new HashMap<Object, Object>(addressingData.length);
        for (int i = 0; i < addressingData.length; i++) {
            addressModes.put(addressingData[i][0], addressingData[i][1]);
        }

        registers = new HashMap<Object, Object>(registerData.length);
        for (int i = 0; i < registerData.length; i++) {
            registers.put(registerData[i][0], registerData[i][1]);
        }
    }

// public methods --> 

    /**
     * This method checks if a command is a valid opCode.
     *
     * @param command String form of a possible opCode.
     */
    public int getOpcode(String command) {
        command = command.trim();
        command = command.toUpperCase();
        Integer value = (Integer) opcodes.get(command);
        if (value != null) {
            return value.intValue();
        }
        return -1;
    }

    /**
     * This method transforms an addressing mode (=, @ or nothing) to
     * a number identifying it.
     *
     * @param identifier String form of an addressing mode.
     * @return Int value telling how many times memory must be accessed.
     */
    public int getAddressingMode(String identifier) {
        identifier = identifier.trim();
        identifier = identifier.toUpperCase();
        Integer value = (Integer) addressModes.get(identifier);
        if (value != null) {
            return value.intValue();
        }
        return -1;
    }

    /**
     * This method returns the binary form of a given register as an integer.
     *
     * @param registerName String form of a register (R0-R7, SP or FP).
     * @return Int value of a given register.
     */
    public int getRegisterId(String registerName) {
        registerName = registerName.trim();
        registerName = registerName.toUpperCase();
        Integer value = (Integer) registers.get(registerName);
        if (value != null) {
            return value.intValue();
        }
        return -1;
    }

    /**
     * This method coverts a complete command in a symbolic form to a binary form.
     * caller must split up the original command and give the parts as parameters
     *
     * @param opcode         String form of an operation code. (STORE)
     * @param firstRegister  String form of a first register. (R0-R7, SP or FP)
     * @param addressingMode = or @ or an empty string that representes the memory addressing
     *                       mode.
     * @param address        String form of an address, must be a valid int.
     * @param otherRegister  String form of an other register. (R0-R7, SP or FP)
     * @return Int format of a symbolic opCode. (etc 00000010 00101000 00000000 01100100 as int)
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

        if (address.equals("")) {
            address = "0";
        }

        try {
            addressAsInt = Integer.parseInt(address);
        } catch (NumberFormatException e) {
            allOk = false;
        }

        if (opcodeAsInt < 0) {
            allOk = false;
        }
        if (firstRegisterAsInt < 0) {
            firstRegisterAsInt = 0;
        }
        if (addressingModeAsInt < 0) {
            addressingModeAsInt = 1;
        }
        if (secondRegisterIdAsInt < 0) {
            secondRegisterIdAsInt = 0;
        }

        if (allOk) {

            // if store or jump then addressinmode as int -= 1;
            if (opcodeAsInt == 1 ||
                    (opcodeAsInt >= 32 && opcodeAsInt <= 44) ||
                    (addressEmpty && !otherRegister.equals("")) ||
                    opcodeAsInt == 49
                    ) {
                addressingModeAsInt = addressingModeAsInt - 1;
                if (addressingModeAsInt == -1) {
                    ++addressingModeAsInt;
                }
            }

            String binary = StringUtils.intToBinary(opcodeAsInt, 8) + StringUtils.intToBinary(firstRegisterAsInt, 3) +
                    StringUtils.intToBinary(addressingModeAsInt, 2) +
                    StringUtils.intToBinary(secondRegisterIdAsInt, 3) + StringUtils.intToBinary(addressAsInt, 16);
            return StringUtils.binaryToInt(binary, true);
        }

        return -1;
    }
}
