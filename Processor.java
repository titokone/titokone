package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.TTK91Cpu;

/** This class represents the processor. It can be told to run for one
    command cycle at a time. */
public class Processor implements TTK91Cpu {


/**
This field represents the memory of computer
*/
private RandomAccessMemory ram;

/** 
This field represents the registers of computer
*/
private Registers regs;


/** 
This field is the program counter
*/
private int pc;

/** 
This field has the current instruction being processed
*/
   private MemoryLine ir;
/**
  state register array.
  index: 
  0 - greater
  1 - equal
  2 - less
  3 - arithmetic overflow
  4 - divide by zero 
  5 - unknown instruction
  6 - forbidden memory access
  7 - device interrupt
  8 - supervisor call
  9 - priviledged mode
 10 - interrupts disabled
*/
   private boolean[] sr;


/**
  Memory address fields of MMU; Memory address register
*/

  private int mar;

/**
  this field is temporary register of memory management unit,
  it has the current memoryline to be read from memory or to
  be written to memory.
 */ 
   private MemoryLine mbr;

/** The stdinData and kbdData fields stores buffer data to be read with 
    the IN operation. When the data has been read, the field should be 
    set to be null. */
   private Integer stdinData, kbdData;

/** This boolean stores whether the previous command should be run again
    instead of fetching a new command (eg. because it was an IN 
    operation and required more data, which has now been supplied). */
   private boolean rerun = false;

/** 
  Creates new processor, memory(with defaultsize) and registers.
  Processor state, program counter get initial values
  @param memsize creates new computer with given size of memory. Proper values are power of two (from 512 to 64k)
 */
    public Processor(int memsize){}


/** Initializes processor with new program
    set fp and sp, pc = 0  and return RunInfo
    @return RunInfo created by RunDebugger
*/
    public RunInfo runInit() {}

/** Process next instruction, 

   Processing Cycle of an instruction:   
    1. mar = pc
    2. mbr = RAM(pc)
    3. ir = mbr
    4. analyze binaryCommand, if ok, call method of the current operation type:
  
   112 SVC
   51-54 Stack operations: push, pop, pushr, popr
   50 EXIT
   49 CALL
   32-44 Branching operations (set GEL-bits)
   17-31 ALU operations
   1-4 Data transfer (LOAD, STORE, IN, OUT)
   0 NOP, do nothing

    5. check  addressing mode 
         -->mar = address
         -->mbr = get value
    6. tr = mbr

   
  if(112) --> SVC  
  if(51-54) --> Stack
  if(49-50) --> Subroutine
  if(32-44) --> Branch
  if(17-31) --> ALU
  if(1-4) --> Transfer


  @return RunInfo created by RunDebugger


 */
    public RunInfo runLine() {}


/** Returns the value of given registerID
    @param registerIDindex of register (0-7). 
    @return Value of given register. Inproper value returns -1

	*/
    public int getValueOf(int registerID) { }

/** Method returns the current value of Processor
    @return 1 running, 0 halted

*/
    public int getStatus() {} // running status: still running, halted

/** Method erases memorylines from memory. Memory will be filled
    with 0-lines.
 	*/
    public boolean eraseMemory() {}

/**
    Method for loading MemoryLines to Processor, Loader classes uses
    this for loading application to processor.
    @return true if loading was ok, false otherwise (=memory full)

*/
    public boolean MemoryInput(MemoryLine inputLine) {}


/** 
    This method adds a line of keyboard data to a buffer the Processor
    can read it from during its next command cycle (or previous cycle 
    repeated). 
    @param kbdInput An int to be "read from the keyboard".
*/
    public void keyboardInput(int kbdInput) {}


/** 
    This method adds a line of stdin data to a buffer the Processor
    can read it from during its next command cycle (or previous cycle 
    repeated). 
    @param stdinInput An int to be "read from STDIN (file)".
*/
    public void stdinInput(int stdinInput) {}

/**
   Transfer-operations  
*/
    private void transfer(int command, int param1, int param2) {}


/**
   ALU-operations
   @return result of the ALU-operation
*/

    private int alu(int command, int param1, int param2) {}

/** 
   Branching
*/
    private int branch(int command, int param) {}

/**
   Stack
*/
    private int stack(int command, int param1, int param2) {}


/**
  Subroutine
*/
    private int subr(int command, int param1, int param2) {}

/**
  Supervisor call
*/
    private int svc(int command, int param1, int param2) {}

}
