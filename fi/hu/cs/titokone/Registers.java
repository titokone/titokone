package fi.hu.cs.titokone;

/** This data class contains all the register information. The registers
    are accessed via index numbers which are defined in the TTK91CPU 
    interface. */
public class Registers { 
    /** This field contains the register values. 0..4 are CU registers, 
	5..12 are general-purpose registers. */
    private int[] registerValues;
	
    /** Returns the value of a register. The index numbers
	are available from the TTK91CPU interface.
	@param registerId Identifying number of the register.
	@return Value stored in the register.  */
    public int getRegister(int registerId) {}

    /** Returns the value of a register. 
	@param registerName The name of the register.
	@return Value stored in the register.  */
    public int getRegister(String registerName) {}

    /** Sets a new value to a register.
	@param registerId The identifying number of the register.
	@param value New value to set.  */
    public void setRegister(int registerId, int value) {}
    
    /** Sets a new value to a register.
	@param registerName The name of the register.
	@param value New value to set.  */
    public void setRegister(String registerName, int value) {}
}
