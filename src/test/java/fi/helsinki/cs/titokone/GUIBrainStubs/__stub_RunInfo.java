/* This class was used for testing GUIBrain */

package fi.helsinki.cs.titokone.GUIBrainStubs;

import fi.helsinki.cs.titokone.MemoryLine;

import java.util.LinkedList;

public class __stub_RunInfo {

    public static int line = -1;

    public __stub_RunInfo() {
        line++;
    }

    public int getLineNumber() {
        return line;
    }

    public String getComments() {
        return new String("Comment");
    }


    public Integer getCrtData() {
        return new Integer(line);

    }

    public int[] getRegisters() {
        int[] ret = new int[450];
        ret[401] = 213 + line;
        ret[402] = 21323 + line;
        ret[403] = 2135 + line;
        ret[404] = 2113 + line;
        ret[405] = 1213 + line;
        ret[406] = 2213 + line;
        ret[407] = 5213 + line;
        ret[408] = 7213 + line;
        ret[203] = 321 + line;

        return ret;
    }

    public LinkedList getChangedMemoryLines() {
        LinkedList ret = new LinkedList();

        for (int i = 0; i < 5; i++) {
            Object[] joo = {new Integer(i + 10), new MemoryLine(326487 + line, "JAAJOO" + line)};
            ret.add(joo);
        }
        return ret;
    }

}