package fi.hu.cs.titokone;

/** This class represents one slot in the TTK-91 computer's memory. It 
    contains both the integer value stored in the memory as well as 
    its symbolic code representation. As such, the class is not 
    strictly to be used in a computer's memory, but can also bridge the
    gap between applications' binary and symbolic form. */
public class MemoryLine {
    /** This field contains the symbolic form of the command. It is 
	empty if there is no symbolic representation for the binary. */
    private String symbolic;
    /** This field contains the binary form of the data stored in this
	line. It may be a valid command, or just a random number. */
    private int binary;

    /** Creates a memory line with symbolic information. 
	@param binary Value of this memory slot.
	@param symbolicCommand String that contains the corresponding
	symbolic command, or an empty string ("") if there is no
	representation. */
    public MemoryLine (int binary, String symbolicCommand);
	
    /** Returns the symbolic representation of the command. If the command
	has no symbolic representation, an empty string is returned.
	@return Symbolic presentation of command. If symbolic 
	representation of the command is not defined, an empty string is 
	returned. */
    public String getSymbolic();
	
    /** Returns the value of this memory slot. 
	@return Integer value of this memory slot. */
    public int getBinary();
}
