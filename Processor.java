package fi.hu.cs.titokone;


import fi.hu.cs.ttk91.TTK91Cpu;

/** This class represents the processor state. It can be told to run for one
    command cycle at a time. */
public class Processor implements TTK91Cpu {
    // has a pointer to memory and registers.

    // Implementations of CPU:

/** program counter
*/
   private int pc;
/** instruction register
*/
   private int ir;
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
  Memory address field of MMU
*/

  private int mar;


/**
  this field is temporary register of memory management unit,
  it has the current memoryline to be read from memory or to
  be written to memory.
 */ 
   private MemoryLine mbr;

/** Creates new processor, memory(with defaultsize) and registers.
    Processor state, program counter get initial values
 */
    public Processor(int memsize){}


/** Initializes processor with new program
    set fp and sp, pc = 0  and return RunInfo
*/

    public RunInfo runInit() {}

/** Process next instruction, returns RunInfo
   
    1. mar = pc
    2. mbr = RAM(pc)
    3. ir = mbr
    4. check  addressing mode 
    5. mar = address
    5. mbr = get value
    6. tr = mbr
    7. analyze binaryCommand

      112 SVC
      51-54 Stack operations: push, pop, pushr, popr
      50 EXIT
      49 CALL
      32-44 Branching operations (set GEL-bits)
      17-31 ALU operations
      1-4 Data transfer (LOAD, STORE, IN, OUT)
      0 NOP, do nothing
  if(112) --> SVC  
  if(51-54) --> Stack
  if(49-50) --> Subroutine
  if(32-44) --> Branch
  if(17-31) --> ALU
  if(1-4) --> Transfer


 */
    public RunInfo runLine() {}


/** Returns the current of given registerID
	*/
    public int getValueOf(int registerID) { }

/** Method returns the current value of Processor
	*/
    public int getStatus() {} // running status: still running, halted

/** Method erases memorylines from memory. Memory will be filled
    with 0-lines.
 	*/
    public boolean eraseMemory() {}

/**
    Method for loading MemoryLines to Processor
*/
    public void MemoryInput(MemoryLine inputLine) {}






/**
   Transfer-operations  
*/
    private void transfer(int command, int param1, int param2) {}


/**
   ALU-operations
   @returns result
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
