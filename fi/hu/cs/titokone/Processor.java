package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.*;
import java.util.*;

/** This class represents the processor. It can be told to run for one
    command cycle at a time. */
public class Processor implements TTK91Cpu {


    /** When SVC call is made PC points to this place. */
    public static final int OS_CODE_AREA = -1;

    private static final String INVALID_OPCODE_MESSAGE = "Invalid operation code {0}";
    private static final String ADDRESS_OUT_OF_BOUNDS_MESSAGE = "Memory address out of bounds";
    private static final String BAD_ACCESS_MODE_MESSAGE = "Invalid memory addressing mode";
    private static final String BRANCH_BAD_ACCESS_MODE_MESSAGE = "Invalid memory access mode in branching command";
    private static final String STORE_BAD_ACCESS_MODE_MESSAGE = "Invalid memory access mode in STORE";
    private static final String NO_KDB_DATA_MESSAGE = "No keyboard data available";
    private static final String NO_STDIN_DATA_MESSAGE = "No standard input data available";
    private static final String INVALID_DEVICE_MESSAGE = "Invalid device number";
    private static final String INTEGER_OVERFLOW_MESSAGE = "Integer overflow";
    private static final String DIVISION_BY_ZERO_MESSAGE = "Division by zero";

    /** CRT-device */
    public static final int CRT = 0;
    /** KBD-device */
    public static final int KBD = 1;
    /** STDIN-device */
    public static final int STDIN = 6;
    /** STDOUT-device */
    public static final int STDOUT = 7;

    /** This field represents the memory of computer. */
    private RandomAccessMemory ram;

    /** This field represents the registers of computer. */
    private Registers regs;
 
    /** Is program running. */
    private int status = TTK91Cpu.STATUS_SVC_SD;
    
    /** Rundebugger */
    private RunDebugger runDebugger = new RunDebugger();
    
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
    
/** Creates new processor, memory and registers.
    Processor state, program counter get initial values
    @param memsize creates new computer with given size of memory. 
    Proper values are power of two (from 512 to 64k). */
    public Processor(int memsize){
        ram = new RandomAccessMemory (memsize);
        regs = new Registers();
    }


/** Returns the memory attached to the processor. */
    public TTK91Memory getMemory() {
       return ram;
    }
    
/** Returns the value of given registerID. The index numbers
    are available from the TTK91CPU interface.
    @param registerID Identifying number of the register.
    @return Value of given register. Inproper value returns -1. */
    public int getValueOf(int registerID) {
        return regs.getRegister (registerID);
    }
    
/** Returns queried memory line.
    @param row Number of the row in processor's memory.
    @return Queried memory line. */
    public MemoryLine getMemoryLine (int row) {
        return ram.getMemoryLine (row);
    }
    
/** Method returns the current value of Processor. Status values
    are available from the TTK91CPU interface.
    @return Current status of the Processor. */
    public int getStatus() {
        return status;
    } 

/** Method erases memorylines from memory. Memory will be filled
    with 0-lines. */
    public void eraseMemory() {
        ram = new RandomAccessMemory (ram.getSize());
    }

/** Method for loading MemoryLines to Processor, Loader classes uses
    this for loading application to processor.
    @throws IllegalArgumentException If inputLine is null.
    @throws TTK91AddressOutOfBounds If the rownumber is either below 0 or
    beyond the memory size.
*/
    public void memoryInput(int rowNumber, MemoryLine inputLine) 
	throws TTK91AddressOutOfBounds {
	String errorMessage;
        try {
            ram.setMemoryLine(rowNumber, inputLine);
        } catch (ArrayIndexOutOfBoundsException e) {
	    errorMessage = new Message("Row number {0} is beyond memory " +
				       "limits.", "" + rowNumber).toString();
	    throw new TTK91AddressOutOfBounds(errorMessage);

        }
    }

/** This method adds a line of keyboard data to a buffer the Processor
    can read it from during its next command cycle (or previous cycle 
    repeated). 
    @param kbdInput An int to be "read from the keyboard". */
    public void keyboardInput(int kbdInput) {
        kbdData = new Integer (kbdInput);
    }

/** This method adds a line of stdin data to a buffer the Processor
    can read it from during its next command cycle (or previous cycle 
    repeated). 
    @param stdinInput An int to be "read from STDIN (file)". */
    public void stdinInput(int stdinInput) {
        stdinData = new Integer (stdinInput);
    }

/** Initializes processor with new program
    set FP and SP, PC = 0  and return RunInfo
    @return RunInfo created by RunDebugger. */
    public void runInit(int initSP, int initFP) {
        status = TTK91Cpu.STATUS_STILL_RUNNING;
        regs.setRegister (TTK91Cpu.CU_PC, 0);
        regs.setRegister (TTK91Cpu.CU_PC_CURRENT, 0);
        
        regs.setRegister (TTK91Cpu.REG_SP, initSP);
        regs.setRegister (TTK91Cpu.REG_FP, initFP);
        
        ram.setCodeAreaLength (initFP+1);
        ram.setDataAreaLength (initSP -initFP);
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
        if (status == TTK91Cpu.STATUS_SVC_SD) return null;
        // if last command set status to ABNORMAL_EXIT repeat that command
        if (status == TTK91Cpu.STATUS_ABNORMAL_EXIT)
            regs.setRegister (TTK91Cpu.CU_PC, regs.getRegister (TTK91Cpu.CU_PC) -1);
        
        try {
            // get PC
            int PC = regs.getRegister (TTK91Cpu.CU_PC);
        
            // fetch the next command to IR from memory and increase PC
            MemoryLine IR = ram.getMemoryLine(PC);
        
            runDebugger.cycleStart (PC, IR.getSymbolic());
        
            regs.setRegister (TTK91Cpu.CU_IR, IR.getBinary());
            regs.setRegister (TTK91Cpu.CU_PC, PC+1);
        
            // cut up the command in IR
            int opcode = IR.getBinary() >>> 24;                             // operation code
            int Rj = ((IR.getBinary()&0xE00000) >>> 21) + TTK91Cpu.REG_R0;  // first operand (register 0..7)
            int M  = (IR.getBinary()&0x180000) >>> 19;                      // memory addressing mode
            int Ri = ((IR.getBinary()&0x070000) >>> 16) + TTK91Cpu.REG_R0;  // index register
            int ADDR = IR.getBinary()&0xFFFF;                               // address
        
            runDebugger.runCommand (IR.getBinary());

            // fetch parameter from memory
            if (Ri != TTK91Cpu.REG_R0) ADDR += regs.getRegister (Ri);   // add indexing register Ri
            int param = ADDR;                               // constant value        
            if (M == 1) {
                param = ram.getValue(ADDR);                 // one memory fetch
                runDebugger.setValueAtADDR (param);
            }
            if (M == 2) {
                param = ram.getValue(param);                // two memory fetches
                runDebugger.setValueAtADDR (param);
                param = ram.getValue (param);
                runDebugger.setSecondFetchValue (param);
            }
        
            // run the command
            if (M == 3) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91BadAccessMode(new Message(Processor.BAD_ACCESS_MODE_MESSAGE).toString());
            }
            
            if (opcode == 0) nop();
            else if (opcode >= 1 && opcode <= 4) transfer (opcode, Rj, M, ADDR, param);
            else if (opcode >= 17 && opcode <= 27) alu (opcode, Rj, param);
            else if (opcode == 31) comp (Rj, param);
            else if (opcode >= 32 && opcode <= 44) branch (opcode, Rj, M, ADDR, param);
            else if (opcode >= 49 && opcode <= 50) subr (opcode, Rj, ADDR, param);
            else if (opcode >= 51 && opcode <= 54) stack (opcode, Rj, Ri, param);
            else if (opcode == 112) svc (Rj, param);
            else {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
			throw new TTK91InvalidOpCode(new Message(Processor.INVALID_OPCODE_MESSAGE, ""+opcode).toString());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
            throw new TTK91AddressOutOfBounds(new Message(Processor.ADDRESS_OUT_OF_BOUNDS_MESSAGE).toString());
        }

        // update PC_CURRENT
        regs.setRegister (TTK91Cpu.CU_PC_CURRENT, regs.getRegister (TTK91Cpu.CU_PC));
        
        // give registers to runDebugger
        int[] registers = new int[8];
        for (int i=0; i < registers.length; i++) 
            registers[i] = regs.getRegister (i + TTK91Cpu.REG_R0);
        runDebugger.setRegisters (registers);
            
        return runDebugger.cycleEnd();
    }

/** Transfer-operations. */
    private void transfer(int opcode, int Rj, int M, int ADDR, int param) 
    throws TTK91BadAccessMode, TTK91AddressOutOfBounds, TTK91NoKbdData, TTK91NoStdInData, TTK91InvalidDevice {
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        switch (opcode) {
            case 1 : // STORE
            if (M == 2) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91BadAccessMode(new Message (Processor.STORE_BAD_ACCESS_MODE_MESSAGE).toString());
            }
            
            if (M == 0) writeToMemory (ADDR, regs.getRegister(Rj));
            if (M == 1) writeToMemory (ram.getValue (ADDR), regs.getRegister(Rj));
            break;
            
            case 2 : // LOAD
            regs.setRegister (Rj, param);
            break;
            
            case 3 : // IN
            switch (param) {
                case KBD : // Keyboard
                if (kbdData == null) {
                    status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                    throw new TTK91NoKbdData(new Message (Processor.NO_KDB_DATA_MESSAGE).toString());
                }
                regs.setRegister (Rj, kbdData.intValue());
                runDebugger.setIN (param, kbdData.intValue());
                kbdData = null;
                status = TTK91Cpu.STATUS_STILL_RUNNING;
                break;
                
                case STDIN : // Standard input file
                if (stdinData == null) {
                    status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                    throw new TTK91NoStdInData(new Message (Processor.NO_STDIN_DATA_MESSAGE).toString());
                }
                regs.setRegister (Rj, stdinData.intValue());
                runDebugger.setIN (param, stdinData.intValue());
                stdinData = null;
                status = TTK91Cpu.STATUS_STILL_RUNNING;
                break;
                
                default : 
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91InvalidDevice(new Message (Processor.INVALID_DEVICE_MESSAGE).toString());
            }
            break;
            
            case 4 : // OUT
            if (param != CRT && param != STDOUT) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91InvalidDevice(new Message (Processor.INVALID_DEVICE_MESSAGE).toString());
            }
            runDebugger.setOUT (param, regs.getRegister(Rj));
            break;
        }
    }


/** ALU-operations.
    @return Result of the ALU-operation. */
    private void alu(int opcode, int Rj, int param) 
    throws TTK91IntegerOverflow, TTK91DivisionByZero {
    runDebugger.setOperationType (RunDebugger.ALU_OPERATION);
        long n;
        switch (opcode) {
            case 17 : // ADD
            n = (long)regs.getRegister (Rj) + (long)param;
            if (isOverflow (n)) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91IntegerOverflow(new Message (Processor.INTEGER_OVERFLOW_MESSAGE).toString());
            }
            regs.setRegister (Rj, (int)n);
            break;
            
            case 18 : // SUB
            n = (long)regs.getRegister (Rj) - (long)param;
            if (isOverflow (n)) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91IntegerOverflow(new Message (Processor.INTEGER_OVERFLOW_MESSAGE).toString());
            }
            regs.setRegister (Rj, (int)n);
            break;
            
            case 19 : // MUL
            n = (long)regs.getRegister (Rj) * (long)param;
            if (isOverflow (n)) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91IntegerOverflow(new Message (Processor.INTEGER_OVERFLOW_MESSAGE).toString());
            }
            regs.setRegister (Rj, (int)n);
            break;
            
            case 20 : // DIV
            if (param == 0) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91DivisionByZero(new Message (Processor.DIVISION_BY_ZERO_MESSAGE).toString());
            }
            regs.setRegister (Rj, regs.getRegister (Rj) / param);
            break;
            
            case 21 : // MOD
            if (param == 0) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91DivisionByZero(new Message (Processor.DIVISION_BY_ZERO_MESSAGE).toString());
            }
            regs.setRegister (Rj, regs.getRegister (Rj) % param);
            break;
            
            case 22 : // AND
            regs.setRegister (Rj, regs.getRegister (Rj) & param);
            break;
            
            case 23 : // OR
            regs.setRegister (Rj, regs.getRegister (Rj) | param);
            break;
            
            case 24 : // XOR
            regs.setRegister (Rj, regs.getRegister (Rj) ^ param);
            break;
            
            case 25 : // SHL
            regs.setRegister (Rj, regs.getRegister(Rj) << param);
            break;
            
            case 26 : // SHR
            regs.setRegister (Rj, regs.getRegister(Rj) >>> param);
            break;
            
            case 27 : // SHRA
            regs.setRegister (Rj, regs.getRegister(Rj) >> param);
            break;
        }
        
        runDebugger.setALUResult (regs.getRegister (Rj));
    }

/** Compare-method manipulates status register.
    @param Rj First value to compare (register index).
    @param param Second value. */
    private void comp(int Rj, int param) {
        // COMP
        runDebugger.setOperationType (RunDebugger.COMP_OPERATION);
        if (regs.getRegister (Rj) > param) {
            sr[0] = true;
            sr[1] = false;
            sr[2] = false;
            runDebugger.setCompareResult (0);
            regs.setRegister (TTK91Cpu.CU_SR, 1);
        }
        else if (regs.getRegister (Rj) < param) {
            sr[0] = false;
            sr[1] = false;
            sr[2] = true;
            runDebugger.setCompareResult (2);
            regs.setRegister (TTK91Cpu.CU_SR, 4);
        }
        else { 
            sr[0] = false;
            sr[1] = true;
            sr[2] = false;
            runDebugger.setCompareResult (1);
            regs.setRegister (TTK91Cpu.CU_SR, 2);
        }
    }

/** Branching. */
    private void branch(int opcode, int Rj, int M, int ADDR, int param)
    throws TTK91BadAccessMode {
        runDebugger.setOperationType (RunDebugger.BRANCH_OPERATION);
        if (M == 2) {
            status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
            throw new TTK91BadAccessMode(new Message (Processor.BRANCH_BAD_ACCESS_MODE_MESSAGE).toString());
        }
        
        switch (opcode) {
            case 32 : // JUMP
            setNewPC (param);
            break;

            case 33 : // JNEG
            if (regs.getRegister (Rj) < 0) setNewPC (param);
            break;

            case 34 : // JZER
            if (regs.getRegister (Rj) == 0) setNewPC (param);
            break;

            case 35 : // JPOS
            if (regs.getRegister (Rj) > 0) setNewPC (param);
            break;

            case 36 : // JNNEG
            if (regs.getRegister (Rj) >= 0) setNewPC (param);
            break;

            case 37 : // JNZER
            if (regs.getRegister (Rj) != 0) setNewPC (param);
            break;

            case 38 : // JNPOS
            if (regs.getRegister (Rj) <= 0) setNewPC (param);
            break;

            case 39 : // JLES
            if (sr[2]) setNewPC (param);
            break;

            case 40 : // JEQU
            if (sr[1]) setNewPC (param);
            break;

            case 41 : // JGRE
            if (sr[0]) setNewPC (param);
            break;

            case 42 : // JNLES
            if (sr[1] || sr[0]) setNewPC (param);
            break;

            case 43 : // JNEQU
            if (sr[2] || sr[0]) setNewPC (param);
            break;

            case 44 : // JNGRE
            if (sr[2] || sr[1]) setNewPC (param);
            break;
        }
    }

/** Stack. */
    private void stack(int opcode, int Rj, int Ri, int param) 
    throws TTK91AddressOutOfBounds {
        runDebugger.setOperationType (RunDebugger.STACK_OPERATION);
        switch (opcode) {
            case 51 : // PUSH
            regs.setRegister (Rj, regs.getRegister(Rj) +1);
            writeToMemory (regs.getRegister(Rj), param);
            break;
            
            case 52 : // POP
            regs.setRegister (Ri, ram.getValue (regs.getRegister(Rj)));
            regs.setRegister (Rj, regs.getRegister(Rj) -1);
            break;
            
            case 53 : // PUSHR
            for (int i=0; i < 7; i++) {
                regs.setRegister (Rj, regs.getRegister(Rj) +1);
                writeToMemory (regs.getRegister (Rj), regs.getRegister (TTK91Cpu.REG_R0 +i));
            }
            break;
            
            case 54 : // POPR
            for (int i=0; i < 7; i++) {
                regs.setRegister (TTK91Cpu.REG_R6 -i, ram.getValue (regs.getRegister (Rj)));
                regs.setRegister (Rj, regs.getRegister(Rj) -1);
            }
            break;
        }
    }

/** Subroutine. */
    private void subr(int opcode, int Rj, int ADDR, int param)
    throws TTK91AddressOutOfBounds {
        runDebugger.setOperationType (RunDebugger.SUB_OPERATION);
        int sp;
        switch (opcode) {
            case 49 : // CALL
            // push PC and FP to stack (Rj is stack pointer)
            sp = regs.getRegister (Rj);
            writeToMemory (++sp, regs.getRegister (TTK91Cpu.CU_PC));
            writeToMemory (++sp, regs.getRegister (TTK91Cpu.REG_FP));
            
            // update stack and frame pointers
            regs.setRegister (Rj, sp);
            regs.setRegister (TTK91Cpu.REG_FP, sp);
            
            // set PC
            setNewPC (ADDR);
            break;
            
            case 50 : // EXIT
            // pop FP and PC from stack (Rj is stack pointer)
            sp = regs.getRegister (Rj);
            regs.setRegister (TTK91Cpu.REG_FP, ram.getValue (sp--));
            setNewPC (ram.getValue (sp--));
            
            // decrease number of parameters from stack
            regs.setRegister (Rj, sp -param);
            break;
        }
    }

/** Supervisor call. */
    private void svc(int Rj, int param)
    throws TTK91AddressOutOfBounds, TTK91NoKbdData {
        runDebugger.setSVCOperation (param);
        Calendar calendar;
        
        // make CALL operation to supervisor
        subr(49, Rj, OS_CODE_AREA, param);
        
        switch (param) {
            case 11 : // HALT
            runDebugger.setOperationType (RunDebugger.SVC_OPERATION);
            status = TTK91Cpu.STATUS_SVC_SD;
            break;
            
            case 12 : // READ
            if (kbdData == null) {
                subr (50, Rj, 0, 1);    // EXIT from SVC(READ) with one parameter
                runDebugger.setOperationType (RunDebugger.SVC_OPERATION);
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91NoKbdData(new Message (Processor.NO_KDB_DATA_MESSAGE).toString());
            }
            writeToMemory (ram.getValue (regs.getRegister(TTK91Cpu.REG_FP) -2), kbdData.intValue());
            kbdData = null;
            status = TTK91Cpu.STATUS_STILL_RUNNING;
            subr (50, Rj, 0, 1);    // EXIT from SVC(READ) with one parameter
            break;
            
            case 13 : // WRITE
            runDebugger.setOUT (CRT, ram.getValue (regs.getRegister(TTK91Cpu.REG_FP) -2));
            subr (50, Rj, 0, 1);    // EXIT from SVC(WRITE) with 1 parameter
            break;

            case 14 : // TIME
            calendar = new GregorianCalendar();

            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            
            // Write second, minute and hour to given places. Right places are found from stack.
            writeToMemory (ram.getValue (regs.getRegister(TTK91Cpu.REG_FP) -2), second);    
            writeToMemory (ram.getValue (regs.getRegister(TTK91Cpu.REG_FP) -3), minute);
            writeToMemory (ram.getValue (regs.getRegister(TTK91Cpu.REG_FP) -4), hour);
            subr (50, Rj, 0, 3);    // EXIT from SVC(TIME) with 3 parameters
            break;
            
            case 15 : // DATE
            calendar = new GregorianCalendar();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DATE);

            // Write date, month and year to given places. Right places are found from stack.
            writeToMemory (ram.getValue (regs.getRegister(TTK91Cpu.REG_FP) -2), date);
            writeToMemory (ram.getValue (regs.getRegister(TTK91Cpu.REG_FP) -3), month);
            writeToMemory (ram.getValue (regs.getRegister(TTK91Cpu.REG_FP) -4), year);
            subr (50, Rj, 0, 3);    // EXIT from SVC(DATE) with 3 parameters
            break;
        }
        runDebugger.setOperationType (RunDebugger.SVC_OPERATION);
    }
    
    private void nop() {
        runDebugger.setOperationType (RunDebugger.NO_OPERATION);
    }
    
    private void writeToMemory (int row, int value) throws TTK91AddressOutOfBounds {
        MemoryLine memoryLine = new MemoryLine (value, new BinaryInterpreter().binaryToString(value));
        ram.setMemoryLine (row, memoryLine);
        runDebugger.addChangedMemoryLine (row, memoryLine);
    }
    
    private void setNewPC (int newPC) {
        regs.setRegister (TTK91Cpu.CU_PC, newPC);
        runDebugger.setNewPC (newPC);
    }
    
/** Tests if given long value is acceptable int value. */
    private boolean isOverflow (long value) {
        return (value > (long)Integer.MAX_VALUE || value < (long)Integer.MIN_VALUE);
    }
}
