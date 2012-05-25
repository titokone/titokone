// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

/* This class was used for testing GUIBrain */

package fi.helsinki.cs.titokone.GUIBrainStubs;


import fi.helsinki.cs.titokone.MemoryLine;

public class __stub_Processor {

    public MemoryLine[] mem;

    public __stub_Processor() {
        mem = new MemoryLine[100];

    }

    public void memoryInput(int line, MemoryLine contents) {
        mem[line] = contents;
    }

    public void runInit(int fp, int sp) {
    }

}