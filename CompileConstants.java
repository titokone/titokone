package fi.hu.cs.titokone;

import java.util.Hashtable;

/** This class contains the bulk data needed to translate commands to 
    opcodes and vice versa. 
    TODO: Anything else needed? */
public class CompileConstants {
    private Hashtable opcodes;
    private Hashtable commands;

    private static final Object[][] data = {
	{"NOP", new Integer(0)},
	{"STORE", new Integer(1)} // etc.
    };

    public CompileConstants() { // Set up the hashtables.
    }

    public String getCommand(int opcode) {  }

    public int getOpcode(String command) { }
}
