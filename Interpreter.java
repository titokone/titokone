/** This class contains information common to various interpreters. */
public class Interpreter {
    /** This field specifies the opcode field length in number of bits. */
    public static final int opcodeLength = 8; // ?
    public static final int addressModeLength = 2; // ?
    public static final int registerFieldLength = 3; // ?
    public static final int addressFieldLength = 16; // ?
    
    /** This field contains a two-dimensional array of translations 
	between opcodes as integers and as symbolic command names. 
	The command names are in all capital letters. */
    protected static final Object[][] commandData = {
	{"NOP", new Integer(0)},
	{"STORE", new Integer(1)} // etc.
    };
    
    /** This field contains a two-dimensional array of translations
	between memory addressing types as integers and as symbols
	repressenting them. */
    protected static final Object[][] addressingData = {
	{"", new Integer(0) }, // TARKISTA ARVOT!
	{"=", new Integer(1) },
	{"@", new Integer(2) }
    };
    
    /** This field contains a two-dimensional array of translations
	between register symbolic names and the integers used to 
	represent them in numeric commands. */
    protected static final Object[][] registerData = {
	{"R0", new Integer(0) } // etc; tarkista arvot!
    };
}
