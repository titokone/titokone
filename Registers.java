package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.IllegalArgumentException;

/** This data class contains all the register information. The registers
    are accessed via index numbers which are defined in the TTK91CPU 
    interface. */
public class Registers { 
    /** This field contains the register values. 0..4 are CU registers, 
	5..12 are general-purpose registers. */
    private int[] registerValues;
	
    /** This function returns the value of a register. The index numbers
	are available from the TTK91CPU interface.
	@param registerId Identifying number of the register.
	@return Value stored in the register. 
	@throws IllegalArgumentException If the registerId does not 
	represent a register. */
    public int getRegisterValue(int registerId) 
	throws IllegalArgumentException {}

    /** This method sets a new value to a register.
	@param registerId The identifying number of the register.
	@param value New value to set.  */
    public int setRegister(int registerId, int value) {}

    /** -- Tarvitaanko allaolevia? Tuki yleiseen numeroilla tunnistamiseen 
	on suht vaatimus, sillä muuten CPU joutuu tulkkaamaan ne kaikki
	toteuttaakseen TTK91CPU-interfacen. --------------------------- */

    /** Returns value of FP (frame pointer).
	@return Value of FP. */
    public int getFP();
    
    /** Returns value of IR (instruction register).
	@return Value of IR. */
    public int getIR();
    
    /** Returns value of PC (program counter).
	@return Value of PC. */
    public int getPC();
    
    /** Returns value of SP (stack pointer).
	@return Value of SP. */
    public int getSP();
    
    /** Returns value of SR (status register).
	@return Value of SR. */
    public int getSR();
    
    /** Returns value of TR (temporary register).
	@return Value of TR.  */
    public int getTR();
    
    /** Sets new value to FP (frame pointer).
	@param value New value to set.  */
    public int setFP(int value);
    
    /** Sets new value to IR (instruction register).
	@param value New value to set.  */
    public int setIR(int value);
    
    /** Sets new value to PC (program counter).
	@param value New value to set.  */
    public int setPC(int value);
    
    /** Sets new value to SP (stack pointer).
	@param value New value to set.  */
    public int setSP(int value);
    
    /** Sets new value to SR (status register).
	@param value New value to set.  */
    public int setSR(int value);
    
    /** Sets new value to TR (temporary register).
	@param value New value to set.  */
    public int setTR(int value);
}
