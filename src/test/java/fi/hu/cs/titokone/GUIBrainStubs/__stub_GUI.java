/* This class was used for testing GUIBrain */

package fi.hu.cs.titokone.GUIBrainStubs;

public class __stub_GUI {

public static int R0 = 0,
                  R1 = 1,
                  R2 = 2,
                  R3 = 3,
                  R4 = 4,
                  R5 = 5,   // r5 == sp
                  R6 = 6,   // r6 == fp
                  SP = 5,   // ^^
                  FP = 6;   // ^^

public static short COMPILE_COMMAND = 0,
                    RUN_COMMAND = 1,
                    STOP_COMMAND = 2,
                    CONTINUE_COMMAND = 3,
                    CONTINUE_WITHOUT_PAUSES_COMMAND = 4;

public __stub_GUI() {
}
  
public void updateStatusLine(String str) {
}
    
public void updateReg(int reg, int value) {
}

public void insertToCodeTable(int[] line, int[] binaryCode, String[] symbolicCode) {
}    

public void setGUIView(int view) {
}

public void enable(int item) {
}

public void disable(int item) {
}


public void updateRowInSymbolTable(String symbolName, int symbolValue) {
}

public void updateInstructionsTable(int[] instructionsLineNumber, String[] newInstructionsContents) {
}

public void updateDataTable(int[] dataLineNumber, String[] newDataContents) {
}

}
