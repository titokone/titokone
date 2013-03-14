// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;


/**
 * This class tells GUIBrain what the processor has done. RunDebugger
 * creates objects from this class and passes them to onwards.
 */

public class RunInfo extends DebugInfo {

    /**
     * This field contains the number of operation type.
     */
    private int operationType;
    /**
     * This field contains line number.
     */
    private int lineNumber;
    /**
     * This field contains contents of the line,
     */
    private String lineContents;
    /**
     * This field contains the command in binary format.
     */
    private int binary;
    /**
     * This array contains the current values of registers 0-7
     */
    private int[] registers = new int[8];
    /**
     * This String contains the colon-representation of current line
     */
    private String colonString;
    /**
     * This int represents the number of memoryfetches
     */
    private int numberOfMemoryfetches;
    /**
     * This field contains first operand of the command.
     */
    private int Rj;
    /**
     * This field contains value of new PC.
     */
    private int newPC;
    /**
     * This field contains index register.
     */
    private int Ri;
    /**
     * This field represents the address
     */
    private int addr;
    /**
     * This field contains the value of address field
     */
    private int valueAtADDR;
    /**
     * This field contains the value of second memory fetch
     */
    private int secondFetchValue;
    /**
     * This field contains the value of ALU-operation
     */
    private int aluResult;
    /**
     * This field contains the compare status of status register.
     * 0 - greater, 1 - equal, 2 - less.
     */
    private int compareStatus = -1;
    /**
     * This boolean value tells is the operation in or out -operation
     */
    private boolean externalOperation;
    /**
     * This boolean value is set true if operation is in operation, otherwise false
     */
    private boolean isIN;
    /**
     * This String value contains the name of the device
     */
    private String deviceName;
    /**
     * This value contains the value of the device
     */
    private int deviceNumber;
    /**
     * This value contains the value read or written from / to device
     */
    private int valueOfDevice;
    /**
     * This value contains the String representation of SVC-operation
     */
    private String svcOperation;
    /**
     * This list contains all changed memory lines. List contains object
     * arrays, whose first element is a Integer and second a MemoryLine.
     * Integer tells the row where MemoryLine is in memory.
     */
    ArrayList<SimpleEntry<Integer, MemoryLine>> changedMemoryLines;

    /**
     * This constructor initializes the RunInfo and sets its starting values.
     *
     * @param lineNumber   Line number of current line.
     * @param lineContents String containing symbolic command.
     */
    public RunInfo(int lineNumber, String lineContents) {
        this.lineNumber = lineNumber;
        this.lineContents = lineContents;
        externalOperation = false;
        setNewPC(lineNumber + 1);
    }


    public void setRegisters(int[] registers) {
        for (int i = 0; i < Math.min(registers.length, this.registers.length); i++) {
            this.registers[i] = registers[i];
        }
    }

    /**
     * This method sets the type of operation performed.
     *
     * @param type Type of operation.
     */
    public void setOperationType(int type) {
        this.operationType = type;
    }

    /**
     * This method sets the colon-presentation of the command.
     *
     * @param colonString The colon-presentation of the command,
     *                    eg. 0:1:0:2:3 (for NOP R1, =3(R2)).
     */
    public void setColonString(String colonString) {
        this.colonString = colonString;
    }

    /**
     * This method gets the colon-presentation of the command.
     *
     * @return The colon-presentation of the command.
     */
    public String getColonString() {
        return colonString;
    }

    /**
     * this method sets the index register.
     *
     * @param Ri Number of the register.
     */
    public void setIndexRegister(int Ri) {
        this.Ri = Ri;
    }

    /**
     * This method sets the first operand.
     *
     * @param Rj Number of the register.
     */
    public void setFirstOperand(int Rj) {
        this.Rj = Rj;
    }

    /**
     * This method sets the number of fetches.
     *
     * @param fetches Number of fetches.
     */
    public void setNumberOfFetches(int fetches) {
        this.numberOfMemoryfetches = fetches;
    }

    /**
     * This method sets the value of the ADDRess field.
     *
     * @param addr Int containing the ADDR.
     */
    public void setADDR(int addr) {
        this.addr = addr;
    }

    /**
     * This method sets the value found at ADDR.
     *
     * @param value Value found at the ADDR.
     */
    public void setValueAtADDR(int value) {
        this.valueAtADDR = value;
    }

    /** Sets changed memory lines.
     * @param changedMemoryLines List of changed memory lines. List contains
     * object arrays, whose first element is a Integer and second is a MemoryLine.
     * Integer tells the row where MemoryLine is in memory.
     */
    public void setChangedMemoryLines(ArrayList<SimpleEntry<Integer, MemoryLine>> changedMemoryLines) {
        this.changedMemoryLines = changedMemoryLines;
    }

    /**
     * This sets the result of performed ALU operation
     *
     * @param result Result of the operation.
     */
    public void setALUResult(int result) {
        this.aluResult = result;
    }

    /**
     * This method tells info that a compare operation was made and what SR
     * bit was changed to what value.
     *
     * @param whichBit Number of the bit.
     */
    public void setCompareOperation(int whichBit) {
        this.compareStatus = whichBit;
    }

    /**
     * This method tells is external operation executed
     *
     * @return boolean true if command is an external operation
     */
    public boolean isExternalOp() {
        return externalOperation;
    }

    /**
     * This method tells is external operation in or out
     *
     * @return true if external operation is in operation, otherwise false.
     */
    public boolean isInOp() {
        return isIN;
    }


    /**
     * This method tells info what was read from given device and what was
     * the value.
     *
     * @param deviceName Name of the device.
     * @param device     Number of the device.
     * @param value      Value read.
     */
    public void setIN(String deviceName, int device, int value) {
        this.externalOperation = true;
        this.isIN = true;
        this.deviceName = deviceName;
        this.deviceNumber = device;
        this.valueOfDevice = value;
    }


    /**
     * This method tells info what was written to the  given device and what
     * was the value.
     *
     * @param deviceName Name of the device.
     * @param device     Number of the device.
     * @param value      Value written.
     */
    public void setOUT(String deviceName, int device, int value) {
        this.externalOperation = true;
        this.isIN = false;
        this.deviceName = deviceName;
        this.deviceNumber = device;
        this.valueOfDevice = value;
    }


    /**
     * This method sets what kind of SVC operation was made.
     */
    public void setSVCOperation(String operation) {
        this.svcOperation = operation;
    }

    /**
     * Sets the value of new PC.
     *
     * @param newPC Value of the new PC.
     */
    public void setNewPC(int newPC) {
        this.newPC = newPC;
    }

    /**
     * Gets the value of new PC.
     *
     * @return Value of the new PC.
     */
    public int getNewPC() {
        return newPC;
    }

    /**
     * Gets compare status of status register.
     *
     * @return Compare status of status register. 0 = grater, 1 = equal, 2 = less.
     */
    public int getCompareStatus() {
        return compareStatus;
    }

    /**
     * This method tells GUIBrain what kind of operation happened.
     *
     * @return int value which represents operation type.
     */
    public int getOperationtype() {
        return operationType;
    }

    /**
     * This methot tells GUIBrain how many memoryfetches were made.
     *
     * @return int How many fetches were made.
     */
    public int getMemoryfetches() {
        return numberOfMemoryfetches;
    }

    /**
     * This method returns the number of the line.
     *
     * @return int containing the line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * This method returns the symbolic command found on the line..
     *
     * @return String String containing the symbolic command.
     */
    public String getLineContents() {
        return lineContents;
    }

    /**
     * This method returns the binary command.
     *
     * @return int Integer containing the binary command.
     */
    public int getBinary() {
        return binary;
    }

    /**
     * This method sets the binary command.
     *
     * @param binary Contains the binary command.
     */
    public void setBinary(int binary) {
        this.binary = binary;
    }

    /**
     * Returns register array.
     *
     * @return int[] int array containing values of the registers.
     */
    public int[] getRegisters() {
        return registers;
    }


    /* Returns changed memory lines.
@return List of changed memory lines. List contains object arrays, whose
first element is a Integer and second is a MemoryLine. Integer tells the
row where MemoryLine is in memory. */
    public ArrayList<SimpleEntry<Integer, MemoryLine>> getChangedMemoryLines() {
        return changedMemoryLines;
    }

    /**
     * This method tells GUIBrain what was result of an OUT command (device
     * and value).
     *
     * @return int[] Integer array containing device number and new value.
     */
    public int[] whatOUT() {
        int[] outD = new int[2];
        outD[0] = deviceNumber;
        outD[1] = valueOfDevice;

        return outD;
    }

    /**
     * This method tells GUIBrain what was result of an IN command (device and
     * value.
     *
     * @return int[] Integer array containing device number and new value.
     */
    public int[] whatIN() {
        int[] inD = new int[2];
        inD[0] = deviceNumber;
        inD[1] = valueOfDevice;

        return inD;
    }

    /**
     * This method returns name of the used device.
     *
     * @return String devicename.
     */
    public String whatDevice() {
        return deviceName;
    }

    /**
     * This method returns register number of the first operand.
     *
     * @return Register number of the first operand.
     */
    public int getFirstOperand() {
        return Rj;
    }

    /**
     * This method returns number of the index register.
     *
     * @return Number of the index register.
     */
    public int getIndexRegister() {
        return Ri;
    }

    /**
     * This method returns value of the ADDR part of the command.
     *
     * @return int Integer containing the value of the ADDR part of command.
     */
    public int getADDR() {
        return addr;
    }

    /**
     * This method returns value found at the ADDR.
     *
     * @return int Integer containing the value found at ADDR..
     */
    public int getValueAtADDR() {
        return valueAtADDR;
    }

    /**
     * This method sets value of second memory fetch. Indirect memory
     * accessing mode needs two memory fetches.
     *
     * @param secondFetchValue Value which have got at second memory fetch.
     */
    public void setSecondFetchValue(int secondFetchValue) {
        this.secondFetchValue = secondFetchValue;
    }

    /**
     * This method gets value of second memory fetch. Indirect memory
     * accessing mode needs two memory fetches.
     *
     * @return Value which have got at second memory fetch.
     */
    public int getSecondFetchValue() {
        return secondFetchValue;
    }


    /**
     * This method returns the result of the ALU operation.
     *
     * @return int Integer containing the result.
     */
    public int getALUResult() {
        return aluResult;
    }


    /**
     * This method returns type of the SVC operation.
     *
     * @return int Integer containing the operation type.
     */
    public String getSVC() {
        return svcOperation;
    }


}
