package fi.hu.cs.titokone;

/** This class contains information common to various interpreters. */
public class Interpreter {
    /** This field specifies the opcode field length in number of bits. */
    public static final int opcodeLength = 8; 
    public static final int addressModeLength = 2; 
    public static final int registerFieldLength = 3; 
    public static final int addressFieldLength = 16; 
    

    public static final Integer NONE = new Integer(0);
    public static final Integer REG = new Integer(1);
    public static final Integer SP_REG = new Integer(2);
    public static final Integer FULL = new Integer(3);

    /** This field contains a two-dimensional array of translations 
	between opcodes as integers, as symbolic command names and the
	parameters they accept. The command names are in all 
	capital letters. */
    protected static final Object[][] commandData = {
	{"NOP", new Integer(0), NONE},
        {"STORE", new Integer(1), FULL},
        {"LOAD", new Integer(2), FULL},
        {"IN", new Integer(3), FULL},	//I guess you could have KBD stored in some weird way
        {"OUT", new Integer(4), FULL},
        {"ADD", new Integer(17), FULL},
        {"SUB", new Integer(18), FULL},
        {"MUL", new Integer(19), FULL},
        {"DIV", new Integer(20), FULL},
        {"MOD", new Integer(21), FULL},
        {"AND", new Integer(22), FULL},		//not sure if compare needs to be between
        {"OR", new Integer(23), FULL},		//two registers.
        {"XOR", new Integer(24), FULL},		//(AND, OR and XOR
        {"SHL", new Integer(25), FULL},
        {"SHR", new Integer(26), FULL},
	{"SHRA", new Integer(27), FULL},	//same int as NOT!,
        {"COMP", new Integer(31), FULL},
        {"JUMP", new Integer(32), FULL},	//Jump has only one param. Jump Address
        {"JNEG", new Integer(33), FULL},	//JNEG Rj, Address
        {"JZER", new Integer(34), FULL},
        {"JPOS", new Integer(35), FULL},
        {"JNNEG", new Integer(36), FULL},
        {"JNZER", new Integer(37), FULL},
        {"JNPOS", new Integer(38), FULL},
        {"JLES", new Integer(39), FULL},
        {"JEQU", new Integer(40), FULL},
        {"JGRE", new Integer(41), FULL},
        {"JNLES", new Integer(42), FULL},
        {"JNEQU", new Integer(43), FULL},
        {"JNGRE", new Integer(44), FULL},
        {"CALL", new Integer(49), FULL},
        {"EXIT", new Integer(50), FULL},
        {"PUSH", new Integer(51), FULL},
        {"POP",  new Integer(52), SP_REG},	
        {"PUSHR", new Integer(53), NONE},	//not sure, would be odd tho
        {"POPR",  new Integer(54), NONE},	//same with this one.
        {"SVC", new Integer(112), FULL}
    };
    
    /** This field contains a two-dimensional array of translations
	between memory addressing types as integers and as symbols
	repressenting them. */
    protected static final Object[][] addressingData = {
        {"=", new Integer(0)},
        {"", new Integer(1)},
        {"@", new Integer(2)}
    };
    
    /** This field contains a two-dimensional array of translations
	between register symbolic names and the integers used to 
	represent them in numeric commands. */
    protected static final Object[][] registerData = {
        {"R0", new Integer(0)},
        {"R1", new Integer(1)},
        {"R2", new Integer(2)},
        {"R3", new Integer(3)},
        {"R4", new Integer(4)},
        {"R5", new Integer(5)},
        {"R6", new Integer(6)},
        {"R7", new Integer(7)},
        {"SP", new Integer(6)},
        {"FP", new Integer(7)},
    };
}
