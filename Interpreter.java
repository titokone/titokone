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
        {"IN", new Integer(3)},
        {"OUT", new Integer(4)},
        {"ADD", new Integer(17)},
        {"SUB", new Integer(18)},
        {"MUL", new Integer(19)},
        {"DIV", new Integer(20)},
        {"MOD", new Integer(21)},
        {"AND", new Integer(22)},
        {"OR", new Integer(23)},
        {"XOR", new Integer(24)},
        {"SHL", new Integer(25)},
        {"SHR", new Integer(26)},
	{"SHRA", new Integer(27)},
        {"COMP", new Integer(31)},
        {"JUMP", new Integer(32)},
        {"JNEG", new Integer(33)},
        {"JZER", new Integer(34)},
        {"JPOS", new Integer(35)},
        {"JNNEG", new Integer(36)},
        {"JNZER", new Integer(37)},
        {"JNPOS", new Integer(38)},
        {"JLES", new Integer(39)},
        {"JEQU", new Integer(40)},
        {"JGRE", new Integer(41)},
        {"JNLES", new Integer(42)},
        {"JNEQU", new Integer(43)},
        {"JNGRE", new Integer(44)},
        {"CALL", new Integer(49)},
        {"EXIT", new Integer(50)},
        {"PUSH", new Integer(51)},
        {"POP",  new Integer(52)},
        {"PUSHR", new Integer(53)},
        {"POPR",  new Integer(54)},
        {"SVC", new Integer(112)}
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
