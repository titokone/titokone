/* This class was used for testing GUIBrain */

package fi.hu.cs.titokone;


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