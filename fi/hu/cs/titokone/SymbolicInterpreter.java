package fi.hu.cs.titokone;

import java.util.HashMap;

/** This class contains the bulk data needed to translate commands to 
    opcodes etc. */
public class SymbolicInterpreter extends Interpreter {
    /** This hashtable contains the opcode values keyed to the symbolic
	commands. */
    private HashMap opcodes;
    private HashMap addressModes;
    private HashMap registers;

    /** This constructor sets up a SymbolicInterpreter instance. It calls
	the private method dataSetup() to set up its data structures. */
    public SymbolicInterpreter() {}

    /** This method sets up the HashTables. */
    private void dataSetup() {}

    public int getOpcode(String command) { }

    /** This function transforms an addressing mode (=, @ or nothing) to 
	a number identifying it. */
    public int getAddressingMode(String identifier) { }

    public int getRegisterId(String registerName) { }

    /**Transforms a String to an int. Operates much like the compiler. First it
     checks whether there is a label, then checks the opCode, then register, address mode, 
     other register and finally the address. There can be extra spaces and tabs but nothing
     more. All the rest must begin with a ;-sign that acts as a comment tag.
     Someone else should 1) replace any symbols in the address portion 
     with numbers and 2) check whether the command makes sense; this method
     will happily transform something like LOAD R1, =R2 into numbers, no 
     matter that the command does not make sense.
     @param symbolicCommand Symbolic command (LOAD R1, 100)
     @return Int format of a symbolic command. (00000010 00101000 00000000 
       01100100 as int presented in base2).
     @throws IllegalArgumentException if the syntax of symbolicCommand is 
     not parseable. 
     */
    public int stringToBinary(String symbolicCommand) { }

}
