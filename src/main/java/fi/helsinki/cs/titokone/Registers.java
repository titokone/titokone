package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.TTK91Cpu;

/**
 * This data class contains all the register information. The registers
 * are accessed via index numbers which are defined in the TTK91CPU
 * interface.
 */
public class Registers {
    /**
     * This field contains the register values. 0..7 are general-purpose
     * registers, 8..12 are CU registers.
     */
    private int[] registerValues = new int[13];

    /**
     * Returns the value of a register. The index numbers
     * are available from the TTK91CPU interface.
     *
     * @param registerId Identifying number of the register.
     * @return Value stored in the register.
     */
    public int getRegister(int registerId) {
        int index = getIndex(registerId);
        if (index != -1) {
            return registerValues[index];
        } else {
            throw new IllegalArgumentException(new Message("Unknown registerId: {0}",
                    "" + registerId).toString());
        }
    }

    /**
     * Returns the value of a register.
     *
     * @param registerName The name of the register.
     * @return Value stored in the register.
     */
    public int getRegister(String registerName) {
        int index = getIndex(registerName);
        if (index != -1) {
            return registerValues[index];
        } else {
            throw new IllegalArgumentException(new Message("Unknown registerName: {0}",
                    "" + registerName).toString());
        }
    }

    /**
     * Sets a new value to a register.
     *
     * @param registerId The identifying number of the register.
     * @param value      New value to set.
     */
    public void setRegister(int registerId, int value) {
        int index = getIndex(registerId);
        if (index != -1) {
            registerValues[index] = value;
        } else {
            throw new IllegalArgumentException(new Message("Unknown registerId: {0}",
                    "" + registerId).toString());
        }
    }

    /**
     * Sets a new value to a register.
     *
     * @param registerName The name of the register.
     * @param value        New value to set.
     */
    public void setRegister(String registerName, int value) {
        int index = getIndex(registerName);
        if (index != -1) {
            registerValues[index] = value;
        } else {
            throw new IllegalArgumentException(new Message("Unknown registerName: {0}",
                    "" + registerName).toString());
        }
    }

    /**
     * Converts TTK91Cpu register to the corresponding index to registerValues array.
     *
     * @param TTK91Cpu_index Register index in TTK91Cpu notation
     * @return Index to registerValues array. Returns -1 if register
     *         does not exists in TTK91Cpu.
     */
    private int getIndex(int TTK91Cpu_index) {
        switch (TTK91Cpu_index) {
            case TTK91Cpu.REG_R0:
                return 0;
            case TTK91Cpu.REG_R1:
                return 1;
            case TTK91Cpu.REG_R2:
                return 2;
            case TTK91Cpu.REG_R3:
                return 3;
            case TTK91Cpu.REG_R4:
                return 4;
            case TTK91Cpu.REG_R5:
                return 5;
            case TTK91Cpu.REG_R6:
                return 6;
            case TTK91Cpu.REG_R7:
                return 7;
            case TTK91Cpu.CU_TR:
                return 8;
            case TTK91Cpu.CU_IR:
                return 9;
            case TTK91Cpu.CU_PC:
                return 10;
            case TTK91Cpu.CU_PC_CURRENT:
                return 11;
            case TTK91Cpu.CU_SR:
                return 12;
            default:
                return -1;
        }
    }

    /**
     * Converts given String to the corresponding index to registerValues array.
     *
     * @param registerName The name of the register.
     * @return Index to registerValues array. Returns -1 if register
     *         does not exists.
     */
    private int getIndex(String registerName) {
        if (registerName == null) {
            return -1;
        }
        String str = registerName.toUpperCase().trim();
        if (str.equals("R0")) {
            return 0;
        }
        if (str.equals("R1")) {
            return 1;
        }
        if (str.equals("R2")) {
            return 2;
        }
        if (str.equals("R3")) {
            return 3;
        }
        if (str.equals("R4")) {
            return 4;
        }
        if (str.equals("R5")) {
            return 5;
        }
        if (str.equals("R6")) {
            return 6;
        }
        if (str.equals("R7")) {
            return 7;
        }
        if (str.equals("SP")) {
            return 6;
        }
        if (str.equals("FP")) {
            return 7;
        }
        if (str.equals("TR")) {
            return 8;
        }
        if (str.equals("IR")) {
            return 9;
        }
        if (str.equals("PC")) {
            return 10;
        }
        if (str.equals("PC_CURRENT")) {
            return 11;
        }
        if (str.equals("SR")) {
            return 12;
        }
        return -1;
    }
}
