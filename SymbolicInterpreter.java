/** This class contains the bulk data needed to translate commands to 
    opcodes etc. */
public class SymbolicInterpreter extends Interpreter {
    /** This hashtable contains the opcode values keyed to the symbolic
	commands. */
    private HashTable opcodes;

    /** This constructor sets up a SymbolicInterpreter instance. It calls
	the private method dataSetup() to set up its data structures. */
    public SymbolicInterpreter() {}

    /** This method sets up the HashTables. */
    private void dataSetup() {}

    public int getOpcode(String command) { }

    /** This function transforms an addressing mode (=, @ or nothing) to 
	a number identifying it. */
    public int getAddressingMode(String identifier) {}

    public int getRegisterId(String registerName) {}
}
