package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.*;

/** This class represents the processor. It can be told to run for one
    command cycle at a time. */
public class Processor implements TTK91Cpu {


    /** This field represents the memory of computer. */
    private RandomAccessMemory ram;

    /** This field represents the registers of computer. */
    private Registers regs;
 
    /** Is program running. */
    private int status = TTK91Cpu.STATUS_SVC_SD;

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
    private boolean[] sr = new boolean[11];


/** The stdinData and kbdData fields stores buffer data to be read with 
    the IN operation. When the data has been read, the field should be 
    set to be null. */
    private Integer stdinData=null, kbdData=null;

/** This boolean stores whether the previous command should be run again
    instead of fetching a new command (eg. because it was an IN 
    operation and required more data, which has now been supplied). */
    private boolean rerun = false;

/** Creates new processor, memory and registers.
    Processor state, program counter get initial values
    @param memsize creates new computer with given size of memory. 
    Proper values are power of two (from 512 to 64k). */
    public Processor(int memsize){
        ram = new RandomAccessMemory (memsize);
        regs = new Registers();
    }


/** Initializes processor with new program
    set FP and SP, PC = 0  and return RunInfo
    @return RunInfo created by RunDebugger. */
    public RunInfo runInit(int initSP, int initFP) {
        status = TTK91Cpu.STATUS_STILL_RUNNING;
        regs.setRegister (TTK91Cpu.CU_PC, 0);
        PC_CURRENT?
        
        regs.setRegister (TTK91Cpu.REG_SP, initSP);
        regs.setRegister (TTK91Cpu.REG_FP, initFP);
        ASETA FP JA SP ram-olion avulla! (sieltä löytyy codeAreaLength ja dataAreaLength)
    }

/** Process next instruction, 

   Processing Cycle of an instruction:   
    1. MAR = PC
    2. MBR = RAM(PC)
    3. IR = MBR
    4. analyze binaryCommand, if ok, call method of the current operation type:
        112 SVC
        51-54 Stack operations: PUSH, POP, PUSHR, POPR
        50 EXIT
        49 CALL
        32-44 Branching operations (use GEL-bits)
        31 COMP (set GEL-bits)
        17-30 ALU operations
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

    @return RunInfo created by RunDebugger. */
    public RunInfo runLine() throws TTK91RuntimeException{
    
        if (status != TTK91Cpu.STATUS_STILL_RUNNING) return null;   // *** palauta sopiva RunInfo
            
        // if PC is out of bounds throw an exception
        int PC = regs.getRegister (TTK91Cpu.CU_PC);
        if (PC >= size) {
            status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
            throw new TTK91AddressOutOfBounds();    
        }
        
        // fetch the next command to IR from memory and increase PC
        int IR = ram.getMemoryLine(PC);     
        regs.setRegister (TTK91Cpu.CU_IR, IR);
        regs.setRegister (TTK91Cpu.CU_PC, PC+1);
        
        // cut up the command in IR
        int opcode = IR >>> 24;                             // operation code
        int Rj = ((IR&0xE00000) >>> 21) + TTK91Cpu.REG_R0;  // first operand (register 0..7)
        int M  = (IR&0x180000) >>> 19;                      // memory addressing mode
        int Ri = ((IR&0x070000) >>> 16) + TTK91Cpu.REG_R0;  // index register
        int ADDR = IR&0xFFFF;                               // address
        
        // fetch parameter from memory
        int param;
        if (Ri != TTK91Cpu.REG_R0) ADDR += regs.getRegister (Ri);   // add indexing register Ri
        if (M == 0) param2 = ADDR;                                  // constant parameter
        try {
            if (M == 1) param = ram.getValue(ADDR);                 // one memory fetch
            if (M == 2) param = ram.getValue (ram.getValue(ADDR));  // two memory fetches
        } catch (ArrayIndexOutOfBoundsException e) {
            status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
            throw new TTK91AddressOutOfBounds();
        }
            
        // run the command
        if (M == 3) {
            status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
            throw new TTK91BadAccessMode();
        }

        if (opcode == 0) nop();
        else if (opcode >= 1 && opcode <= 4) transfer (opcode, Rj, M, ADDR);
        else if (opcode >= 17 && opcode <= 27) alu (opcode, Rj, param);
        else if (opcode == 31) comp (Rj, param);
        else if (opcode >= 32 && opcode <= 44) branch (opcode, Rj, M, ADDR, param);
        else if (opcode >= 49 && opcode <= 50) subr (opcode, Rj, param);
        else if (opcode >= 51 && opcode <= 54) Stack (opcode, Rj, param);
        else if (opcode == 112) svc (opcode, Rj, param2);
        else {
            status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
            throw new TTK91InvalidOpCode();
        }
        
        // update PC_CURRENT
        regs.setRegister (TTK91Cpu.CU_PC_CURRENT, regs.getRegister (TTK91Cpu.CU_PC));
    }


/** Returns the value of given registerID. The index numbers
	are available from the TTK91CPU interface.
    @param registerID Identifying number of the register.
    @return Value of given register. Inproper value returns -1. */
    public int getValueOf(int registerID) {
        regs.getRegister (registerID);
    }

/** Method returns the current value of Processor. Status values
	are available from the TTK91CPU interface.
    @return Current status of the Processor. */
    public int getStatus() {
        return status;
    } 

/** Method erases memorylines from memory. Memory will be filled
    with 0-lines. */
    public boolean eraseMemory() {
        ram = new RandomAccessMemory (size);
    }

/** Method for loading MemoryLines to Processor, Loader classes uses
    this for loading application to processor.
    @return true if loading was ok, false otherwise (=memory full). */
    public void memoryInput(int rowNumber, MemoryLine inputLine) {
        ram.setMemoryLine (rowNumber, inputLine);
    }


/** This method adds a line of keyboard data to a buffer the Processor
    can read it from during its next command cycle (or previous cycle 
    repeated). 
    @param kbdInput An int to be "read from the keyboard". */
    public void keyboardInput(int kbdInput) {
        kbdData = new Integer (kndInput);
    }


/** This method adds a line of stdin data to a buffer the Processor
    can read it from during its next command cycle (or previous cycle 
    repeated). 
    @param stdinInput An int to be "read from STDIN (file)". */
    public void stdinInput(int stdinInput) {
        stdinData = new Integer (stdinInput);
    }

/** Transfer-operations. */
    private void transfer(int opcode, int Rj, int M, int ADDR, int param) {
        switch (opcode) {
            case 1 : // STORE
            if (M == 0) throw new TTK91BadAccessMode(); // in STORE parameter must be a pointer
            // find out if binary in Rj can be converted to symbolic command
            MemoryLine ml = new MemoryLine (binary, new BinaryInterpreter().binaryToString(binary));
            // write new memory line to memory
            try {
                if (M == 1) ram.setMemoryLine (ADDR, ml);
                if (M == 2) ram.setMemoryLine (ram.getValue (ADDR), ml);
            } catch (ArrayIndexOutOfBoundsException e) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91OutOfBounds();
            }
            break;
            
            case 2 : // LOAD
            regs.setRegiser (Rj, param);
            break;
            
            case 3 : // IN
            switch (param) {
                case 1 : // Keyboard
                if (kbdData == null) throw new TTK91NoKbdData();
                regs.setRegister (Rj, kbdData.int_value());
                kbdData = null;
                break;
                
                case 6 : // Standard input file
                if (stdinData == null) throw new TTK91NoFileData();
                regs.setRegister (Rj, stdinData.int_value());
                stdinData = null;
                break;
                
                default : throw new TTK91InvalidDevice();
            }
            break;
            
            case 4 : // OUT
            switch (param) {
                case 0 : // CRT
                break;
                
                case 7 : // Standard output file
                break;
                
                default : throw new TTK91InvalidDevice();
            }
            break;
        }
    }


/** ALU-operations.
    @return Result of the ALU-operation. */
    private int alu(int opcode, int Rj, int param2) {
        long n;
        switch (opcode) {
            case 17 : // ADD
            n = (long)regs.getRegister (Rj) - (long)param2;
            if (isOverflow (n)) throw new TTK91IntegerOverflow();
            regs.setRegister (Rj, (int)n);
            break;
            
            case 18 : // SUB
            n = (long)regs.getRegister (Rj) - (long)param2;
            if (isOverflow (n)) throw new TTK91IntegerOverflow();
            regs.setRegister (Rj, (int)n);
            break;
            
            case 19 : // MUL
            n = (long)regs.getRegister (Rj) * (long)param2;
            if (isOverflow (n)) throw new TTK91IntegerOverflow();
            regs.setRegister (Rj, (int)n);
            break;
            
            case 20 : // DIV
            if (param2 == 0) throw new TTK91DivisionByZero();
            regs.setRegister (Rj, regs.getRegister (Rj) / param2);
            break;
            
            case 21 : // MOD
            if (param2 == 0) throw new TTK91DivisionByZero();
            regs.setRegister (Rj, regs.getRegister (Rj) % param2);
            break;
            
            case 22 : // AND
            regs.setRegister (Rj, regs.getRegister (Rj) & param2);
            break;
            
            case 23 : // OR
            regs.setRegister (Rj, regs.getRegister (Rj) | param2);
            break;
            
            case 24 : // XOR
            regs.setRegister (Rj, regs.getRegister (Rj) ^ param2);
            break;
            
            case 25 : // SHL
            regs.setRegister (Rj, regs.getRegister(Rj) << param2);
            break;
            
            case 26 : // SHR
            regs.setRegister (Rj, regs.getRegister(Rj) >>> param2);
            break;
            
            case 27 : // SHRA
            regs.setRegister (Rj, regs.getRegister(Rj) >> param2);
            break;
        }
    }

/** Compare-method manipulates status register.
    @param Rj First value to compare (register index).
    @param param Second value. */
    private void comp(int Rj, int param) {
        // COMP
        if (regs.getRegister (Rj) > param) {
            sr[0] = true;
            sr[1] = false;
            sr[2] = false;
        }
        else if (regs.getRegister (Rj) < param) {
            sr[0] = false;
            sr[1] = false;
            sr[2] = true;
        else { 
            sr[0] = false;
            sr[1] = true;
            sr[2] = false;
        }
    }

/** Branching. */
    private int branch(int opcode, int Rj, int M, int ADDR, int param) {
        if (M == 0) throw new TTK91BadAccessMode(); // in jump-commands parameter must be a pointer
        
        switch (opcode) {
            case 32 : // JUMP
            regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 33 : // JNEG
            if (regs.getRegister (Rj) < 0) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 34 : // JZER
            if (regs.getRegister (Rj) == 0) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 35 : // JPOS
            if (regs.getRegister (Rj) > 0) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 36 : // JNNEG
            if (regs.getRegister (Rj) >= 0) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 37 : // JNZER
            if (regs.getRegister (Rj) != 0) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 38 : // JNPOS
            if (regs.getRegister (Rj) <= 0) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 39 : // JLES
            if (sr[2]) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 40 : // JEQU
            if (sr[1]) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 41 : // JGRE
            if (sr[0]) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 42 : // JNLES
            if (sr[1] || sr[0]) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 43 : // JNEQU
            if (sr[2] || sr[0]) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;

            case 44 : // JNGRE
            if (sr[2] || sr[1]) regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;
        }
    }

/** Stack. */
    private int stack(int opcode, int Rj, int param) {
        switch (opcode) {
            case 51 : // PUSH
            regs.setRegister (Rj, regs.getRegister(Rj) +1);
            try {
                ram.setMemoryLine (regs.getRegister (Rj), new MemoryLine (param, null));
            }
            catch (ArrayIndexOutOfBoundsException e) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91OutOfBounds();
            }
            break;
            
            case 52 : // POP
            
            break;
            
            case 53 : // PUSHR
            break;
            
            case 54 : // POPR
            break;
        }
    }

/** Subroutine. */
    private int subr(int opcode, int Rj, int ADDR, int param) {
        switch (opcode) {
            case 49 : // CALL
            // push PC and FP to stack (Rj is stack pointer)
            int sp = regs.getRegister (Rj);
            try {
                ram.setMemoryLine (++sp, new MemoryLine (regs.getRegister (TTK91Cpu.CU_PC), null));
                ram.setMemoryLine (++sp, new MemoryLine (regs.getRegister (TTK91Cpu.REG_FP), null));
            }
            catch (ArrayIndexOutOfBoundsException e) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91OutOfBounds();
            }
            
            // update stack and frame pointers
            regs.setRegister (Rj, sp);
            regs.setRegister (TTK91Cpu.REG_FP, sp);
            
            // set PC
            regs.setRegister (TTK91Cpu.CU_PC, ADDR);
            break;
            
            case 50 : // EXIT
            // pop FP and PC from stack (Rj is stack pointer)
            int sp = regs.getRegister (Rj);
            try {
                regs.setRegister (TTK91Cpu.REG_FP, ram.getValue (sp--));
                regs.setRegister (TTK91Cpu.CU_PC, ram.getValue (sp--));
            }
            catch (ArrayIndexOutOfBoundsException e) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91OutOfBounds();
            }
            
            // decrease number of parameters from stack
            regs.setRegister (Rj, sp -param);
            break;
        }
    }

/** Supervisor call. */
    private int svc(int Rj, int param) {
        
        // make CALL operation
        subr(49, Rj, regs.getRegister (TTK91Cpu.CU_PC), param);
        
        switch (param) {
            case 11 : // HALT
            status = TTK91Cpu.STATUS_SVC_SD;
            break;
            
            case 12 : // READ
            if (kbdData == null) throw new TTK91NoKbdData();
            ram.setMemoryLine (ram.getValue (sp -2), new MemoryLine (kbdData.intValue(), null));
            kbdData = null;
            subr (50, Rj, 0, 1);    // EXIT from SVC(READ)
            break;
            
            case 13 : // WRITE
            subr (50, Rj, 0, 1);    // EXIT from SVC(WRITE)
            break;

            case 14 : // TIME
            TODO
            subr (50, Rj, 0, 3);    // EXIT from SVC(TIME)
            break;
            
            case 15 : // DATE
            TODO
            subr (50, Rj, 0, 3);    // EXIT from SVC(DATE)
            break;
            
            
        
    }
    
/** Tests if given long value is acceptable int value. */
    private boolean isOverflow (long value) {
        return (value > (long)Integer.MAX_VALUE || value < (long)Integer.MIN_VALUE);
    }
}
