/* This class was used for testing GUIBrain */

package fi.hu.cs.titokone.GUIBrainStubs;


import fi.hu.cs.titokone.MemoryLine;

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