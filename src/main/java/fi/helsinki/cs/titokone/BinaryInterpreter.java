// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import fi.helsinki.cs.titokone.devices.DeviceNames;

import java.util.HashMap;

/**
 * This class contains the information to translate a command in
 * binary form to its symbolic string form. Naturally, if a command
 * has been transformed from its symbolic form to binary, the transformation
 * back will not restore used symbols as we cannot tell, even with the help
 * of the symbol table, what symbol has been used where.
 */
public class BinaryInterpreter extends Interpreter {
    public static final String GARBLE = "";

    /**
     * This hashmap contains the symbolic commands as strings, with
     * Integer forms of their opcodes as keys.
     */
    private HashMap<Object, Object> commands;

    /**
     * This hashmap contains parameters for each command with Integer forms
     * of their opcodes as keys.
     */
    private HashMap<Object, Object> parameters;


    /**
     * This constructor sets up a binaryinterpreter and initializes the
     * internal command information data structures.
     */

    public BinaryInterpreter() {
        commands = new HashMap<Object, Object>(38); // 38 was 37, increased to 38 (added NOT-command) - Lauri 2004-12-09
        parameters = new HashMap<Object, Object>(38);

        for (int i = 0; i < 38; i++) {
            commands.put(commandData[i][1], commandData[i][0]);
            parameters.put(commandData[i][1], commandData[i][2]);
        }

    }


    /**
     * This function transforms a binary-form command to its symbolic
     * representation. Binary is interpreted in two parts, firstly the
     * first 8 bits of the binary representation are extracted and if
     * it is a valid opcode then check the needed bits if they make
     * any sense. Like if the opcode is a NOP then all the bits can be 0,
     * or anything else. Also check what to return. Like in a case of NOP
     * there is no need to return other parameters.
     * http://www.cs.helsinki.fi/u/ahakkine/Tito/koksi.kaskyt Check
     * Compiler.java for more info on checking a binary.
     *
     * @param binaryCommand The command's binary-form representation.
     * @return The symbolic representation if it is valid enough.  If
     *         the opcode is unknown, the memory address mode faulty or the
     *         register ids do not point to real registers,
     *         BinaryInterpreter.GARBLE is returned.
     */

    public String binaryToString(int binaryCommand) {
        int command = binaryCommand;

        String s = getOpCodeFromBinary(command);
        if (s == null) {
            return GARBLE;
        }
        Integer opcode = new Integer(s);
        s = (String) commands.get(opcode);
        if (s == null) {
            return GARBLE;
        }
        if (getMemoryModeFromBinary(command) == null) {
            return GARBLE;
        }

        Integer param = (Integer) parameters.get(opcode);

        switch (param.intValue()) {
            case 0: { // No parameters
                return s;
            }
            case 1: { // not used
                return s;
            }
            case 2: { //SP and register
                s = s + " " + getFirstRegisterFromBinary(command);
                s = s + ", " + getSecondRegisterFromBinary(command);
                return s;
            }
            case 3: { //only SP
                s += " " + getFirstRegisterFromBinary(command);
                return s;
            }
            case 4: { //address only
                String mem = getMemoryModeFromBinary(command);

                if (mem.equals(null)) {
                    return GARBLE;
                }
                s += " " + mem;
                s += getAddressFromBinary(command);
                s = s + "(" + getSecondRegisterFromBinary(command);
                s = s + ")";
                return s;
            }
            case 5: { //Full parameters
                s = s + " " + getFirstRegisterFromBinary(command);
                s = s + ", " + getMemoryModeFromBinary(command);
                s = s + getAddressFromBinary(command);
                s = s + "(" + getSecondRegisterFromBinary(command);
                s = s + ")";

                return s;
            }
            case 6: {//Full with less fetches

                String mem = getMemoryModeFromBinary(command);
                if (mem == null) {
                    return GARBLE;
                }

                s = s + " " + getFirstRegisterFromBinary(command);
                s = s + ", " + mem;
                s = s + getAddressFromBinary(command);
                s = s + "(" + getSecondRegisterFromBinary(command);
                s = s + ")";

                return s;
            }

            case 7: {//Register and device
                // FIXME: this should really allow any addressing mode and no restrictions on the constant value
                s += " " + getFirstRegisterFromBinary(command);

                int device = Integer.parseInt(getAddressFromBinary(command));
                if (!getMemoryModeFromBinary(command).equals("=")) {
                    return GARBLE;
                }

                String devName = DeviceNames.lookupByValue(device);
                if (devName != null) {
                    return s + ", =" + devName.toUpperCase();
                }
                return GARBLE;
            }
            case 8: { //Address with less fetches
                String mem = getMemoryModeFromBinary(command);
                if (mem == null) {
                    return GARBLE;
                }

                s = s + " " + mem;
                s = s + getAddressFromBinary(command);
                s = s + "(" + getSecondRegisterFromBinary(command);
                s = s + ")";

                return s;
            }
            case 9: {//SVC SP and operation
                // FIXME: this should really allow any addressing mode and no restrictions on the constant value
                s += " " + getFirstRegisterFromBinary(command);
                s += ", =";
                int service = Integer.parseInt(getAddressFromBinary(command));

                String servName = SvcNames.lookupByValue(service);
                if (servName != null) {
                    return s + servName.toUpperCase();
                }

                return GARBLE;
            }
        }

        return s;

    }

    /* Translates the opcode and checks if it is a valid one, then
calls the getParameterString to sort out the rest of the binary.*/

    /**
     * This command returns the operation code from a binary
     *
     * @param binaryCommand The command's binary-form representation.
     * @return Operation code in a String format.
     */
    public String getOpCodeFromBinary(int binaryCommand) {
        int command = binaryCommand;
        //get opcode and get its name and return it
        Integer opcode = new Integer(command >> 24);
        if (commands.get(opcode) != null) {
            String s = "" + opcode;
            return s;
        }
        return null;
    }

    /**
     * If a command has a first register value then this function returns
     * it. (NOP has none, thus it would return "") Normally value would
     * be a value from R0 to R7
     *
     * @param binaryCommand The command's binary-form representation.
     * @return Possible register value in a String format.
     */
    public String getFirstRegisterFromBinary(int binaryCommand) {
        int command = binaryCommand;
        int i = command >> 21; //remove addr, 2. register, memorymode
        i = i & 7;             //get 1. register and check its name
        String s = (String) registerData[i][0];
        return s;
    }

    /**
     * Function returns possible memory address mode from a binary command
     * given as a parameter. Four possible values (non excistent, and
     * three legal values)
     *
     * @param binaryCommand The command's binary-form representation.
     * @return Memory address mode from binary command in a String format.
     */
    public String getMemoryModeFromBinary(int binaryCommand) {
        int command = binaryCommand;
        int i = command >> 19; //remove addr and second register
        i = i & 3;             //get memorymode
        String operationCode = getOpCodeFromBinary(binaryCommand);
        Integer opcode = new Integer(operationCode);
        Integer params = (Integer) parameters.get(opcode);

        /* Store and jumps use less memoryfetches so we need to return
            different symbols for them.*/

        if (params.intValue() == 6 || params.intValue() == 8) {
            if (i == 0) {
                return "";
            }
            if (i == 1) {
                return "@";
            }
            if (i == 2 || i == 3) {
                return null;
            }
        } else {
            if (i == 0) {
                return "=";
            }
            if (i == 1) {
                return "";
            }

            if (i == 2) {
                return "@";
            }
        }
        return null;

    }

    /**
     * If a command has second register value, this function returns it
     * "" or R0 to R7).
     *
     * @param binaryCommand The command's binary-form representation.
     * @return Possible other register from binary command in a String format.
     */
    public String getSecondRegisterFromBinary(int binaryCommand) {
        int command = binaryCommand;
        int i = command >> 16; //remove address and get first three bits
        i = i & 7;
        String s = (String) registerData[i][0];

        return s;


    }

    /**
     * If a given binary represents a valid command that has an address
     * then this function returns it.
     *
     * @param binaryCommand The command's binary-form representation.
     * @return Address part of the binary command in a String format.
     */

    public String getAddressFromBinary(int binaryCommand) {
        int command = binaryCommand;
        int i = command & 65535; //AND first 16 bits

        String binaryString = StringUtils.intToBinary(i, 16);

        i = StringUtils.binaryToInt(binaryString, true);

        String s = "" + i;
        return s;

    }
}
