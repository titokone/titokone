/** This data class contains register information. */
public class Registers { 
	
	/** Returns value of indexed register.
	    @param index Index to register.
	    @return Value of register.
	*/
	public int getRegister (int index);

	/** Returns value of FP (frame pointer).
	    @return Value of FP.
	*/
	public int getFP();

	/** Returns value of IR (instruction register).
	    @return Value of IR.
	*/
	public int getIR();

	/** Returns value of PC (program counter).
	    @return Value of PC.
	*/
	public int getPC();

	/** Returns value of SP (stack pointer).
	    @return Value of SP.
	*/
	public int getSP();

	/** Returns value of SR (status register).
	    @return Value of SR.
	*/
	public int getSR();

	/** Returns value of TR (temporary register).
	    @return Value of TR.
	*/
	public int getTR();
	
	/** Sets new value to indexed register.
	    @param index Index to register.
	    @param value New value to set.
	*/
	public int setRegister (int index, int value);

	/** Sets new value to FP (frame pointer).
	    @param value New value to set.
	*/
	public int setFP(int value);

	/** Sets new value to IR (instruction register).
	    @param value New value to set.
	*/
	public int setIR(int value);

	/** Sets new value to PC (program counter).
	    @param value New value to set.
	*/
	public int setPC(int value);

	/** Sets new value to SP (stack pointer).
	    @param value New value to set.
	*/
	public int setSP(int value);

	/** Sets new value to SR (status register).
	    @param value New value to set.
	*/
	public int setSR(int value);

	/** Sets new value to TR (temporary register).
	    @param value New value to set.
	*/
	public int setTR(int value);
}
