/* This class was used for testing GUIBrain */

package fi.helsinki.cs.titokone.GUIBrainStubs;

import fi.helsinki.cs.titokone.MemoryLine;

public class __stub_LoadInfo {

    public __stub_LoadInfo() {
        __stub_RunInfo.line = -1;
    }

    public __stub_LoadInfo(MemoryLine[] codeArea, MemoryLine[] dataArea, int symbolTable,
                           int initSP, int initFP, String str) {
    }


    public int[] getBinaryCommands() {
        int[] ret = {1, 2, 3, 4, 5, 64, 7, 8, 9, 1, 2, 3, 4, 5, 64, 7, 8, 9};
        return ret;
    }


    public String[] getSymbolicCommands() {
        String[] ret = {"_1_", "_2_", "_3_", "_4_", "_5_", "_6_", "_7_", "_8_", "_9_",
                "_1_", "_2_", "_3_", "_4_", "_5_", "_6_", "_7_", "_8_", "_9_"};
        return ret;
    }

    public int[] getData() {
        int[] ret = new int[60000];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = 0;
        }
        return ret;
    }


    public int getSP() {
        return 12;
    }

    public int getFP() {
        return 11;
    }

    public String[][] getSymbolTable() {
        String[][] ret = {{"Alku", "0"}, {"gnrkej", "21"}};
        return ret;
    }

    public String getStatusMessage() {
        return new String("This is status message");
    }

    public String getComments() {
        return new String("Loaded the program into Titokone's memory.\nFP set to 11 and SP set to 12.");
    }

}