/** This class represents one slot in TTK-91 computer's memory. There is optional possibility
    for TTK-91 commands also save the original symbolic command (like "STORE R1, =100").
*/    
public class MemoryLine {

	/** Creates memory line without symbolic information.
	    @param binary Value of this memory slot.
	*/
	public MemoryLine (int binary);

	/** Creates memory line with symbolic information.
	    @param binary Value of this memory slot.
	    @param symbolicCommand String that contains the corresponding symbolic command.
	*/
	public MemoryLine (int binary, String symbolicCommand);
	
	/** Returns symbolic presentation of the command or if it is not defined empty string
	    is returned.
	    @return Symbolic presentation of command. If symbolic presentation of
	    command is not defined an empty string is returned. */
	public String getSymbolic();
	
	/** Returns the value of this memory slot.
	    @return Integer value of this memory slot.
	*/
	public int getBinary();
}
