package fi.hu.cs.titokone;
import fi.hu.cs.titokone.devices.*;
import fi.hu.cs.ttk91.*;
import java.util.*;

/** This class represents the processor. It can be told to run for one
    command cycle at a time. */
public class Processor 
implements TTK91Cpu,Interruptable {


    /** When SVC call is made PC points to this place. */
    public static final int OS_CODE_AREA = -1;

    public static final String INVALID_OPCODE_MESSAGE = "Invalid operation code {0}";
    public static final String ADDRESS_OUT_OF_BOUNDS_MESSAGE = "Memory address out of bounds";
    public static final String BAD_ACCESS_MODE_MESSAGE = "Invalid memory addressing mode";
    public static final String BRANCH_BAD_ACCESS_MODE_MESSAGE = "Invalid memory access mode in branching command";
    public static final String STORE_BAD_ACCESS_MODE_MESSAGE = "Invalid memory access mode in STORE";
    public static final String NO_KDB_DATA_MESSAGE = "No keyboard data available";
    public static final String NO_STDIN_DATA_MESSAGE = "No standard input data available";
    public static final String INVALID_DEVICE_MESSAGE = "Invalid device number";
    public static final String INTEGER_OVERFLOW_MESSAGE = "Integer overflow";
    public static final String DIVISION_BY_ZERO_MESSAGE = "Division by zero";

    /** CRT-device */
    public static final int CRT = 0;
    /** KBD-device */
    public static final int KBD = 1;
    /** STDIN-device */
    public static final int STDIN = 6;
    /** STDOUT-device */
    public static final int STDOUT = 7;
    
    protected ArrayList<IODevice> ioDevices=new ArrayList<IODevice>();

    /** This field represents the memory of computer. 
        this is actually the "virtual" ram*/
    private RandomAccessMemory ram;
    /**
     *  this ram is the "physical" ram, even though this might
     *  include some mmapped devices and actually be a 
     *  "combiningrandomaccessmemoryimpl" or something
     */
    private RandomAccessMemory physRam;

    /** This field represents the registers of computer. */
    private Registers regs;
 
    /** Is program running. */
    private int status = TTK91Cpu.STATUS_SVC_SD;
    
    /** Rundebugger */
    private RunDebugger runDebugger = new RunDebugger();
    private int memsize;
    
    /*  a programmable interrupt controller*/
    protected Pic pic=new Pic();
    
    /*  reference to the Display so we can control it*/
    protected Display display=null;
    
    
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
    
    /*  if an interrupt has been flagged
        this will be automatically cleared 
        when the processor does the jump to 
        the interrupt routine*/
    protected boolean interrupted=false;

    //Added by Harri Tuomikoski, 12.10.2004, Koskelo-project.
    private int stack_size = 0;
    private int stack_max_size = 0;
    private int commands_executed = 0;

/** The stdinData and kbdData fields stores buffer data to be read with 
    the IN operation. When the data has been read, the field should be 
    set to be null. */
    private Integer stdinData=null, kbdData=null;
    
/** Creates new processor, memory and registers.
    Processor state, program counter get initial values
    @param memsize creates new computer with given size of memory. 
    Proper values are power of two (from 512 to 64k). */
    public Processor(int memsize){
        this.memsize=memsize;
        reinitMemory(memsize);
        regs = new Registers();

        //Added by HT, 12.10.2004, Koskelo-project
        this.stack_size = 0;
        this.stack_max_size = 0;
        this.commands_executed = 0;
        initDevices();
    }
    /**
     *  flag that an interrupt has happened. this should only 
     *  be called on the "rising edge" of the interrupt. so if the
     *  interrupt line stays high, this will not be retriggered.
     */
    public void flagInterrupt(InterruptGenerator ig)
    {
        /*  set internal variable to flag interrupt
            dont know if we could use sr[7] for this*/
        interrupted=true;
    }
    public void clearInterrupt()
    {
        interrupted=false;
    }
    private void reinitMemory(int memsize)
    {
        physRam = new RandomAccessMemoryImpl(memsize);
        resetDevices();
    }
    /**
     *  this is used to set external devices which are somehow difficult
     *  and global. like the Display
     */
    public void setExternalDevice(Device d)
    {
        if(d instanceof Display)
        {
            display=(Display)display;
        }
    }
    private void initDevices()
    {   
        /*  link pic to report it's interrupt to processor*/
        pic.link(this);
        //!TBD combine below classes into a sensible stdout inner class
        // or two
        IODevice crt=new InvalidIODevice(1)
            {
                @Override
                public void setPort(int n,int value)
                {
                    if(n!=0)
                        throw new RuntimeException("shouldnt happen "+n);
                    runDebugger.setOUT (CRT, regs.getRegister(value));                
                }                
            };
        IODevice stdout=new InvalidIODevice(1)
            {
                @Override
                public void setPort(int n,int value)
                {
                    if(n!=0)
                        throw new RuntimeException("shouldnt happen "+n);
                    runDebugger.setOUT (STDOUT, regs.getRegister(value));                
                }                
            };
        registerDevice(crt);
        registerDevice(new InvalidIODevice(1)
            {
                @Override
                public int getPort(int n)
                throws TTK91RuntimeException
                {
                    if(n!=0)
                        throw new RuntimeException("shouldnt happen "+n);
                        
                    if (kbdData == null) {
                        throw new TTK91NoKbdData(new Message (Processor.NO_KDB_DATA_MESSAGE).toString());
                    }
                    runDebugger.setIN (KBD, kbdData.intValue());
                    int ret=kbdData.intValue();
                    kbdData = null;           
                    return ret;
                }
            });
        registerDevice(new InvalidIODevice(4));
        registerDevice(new InvalidIODevice(1)
            {
                @Override
                public int getPort(int n)
                throws TTK91RuntimeException
                {
                    if(n!=0)
                        throw new RuntimeException("shouldnt happen "+n);
                        
                    if (stdinData == null) {
                        throw new TTK91NoStdInData(new Message (Processor.NO_KDB_DATA_MESSAGE).toString());
                    }
                    runDebugger.setIN (STDIN, stdinData.intValue());
                    int ret=stdinData.intValue();
                    stdinData = null;           
                    return ret;
                }
            });
        registerDevice(stdout); //7
        registerDevice(new InvalidIODevice(3)); //reserve up to 10
        registerDevice(new ZeroIODevice()); //here you can read zeroes
        registerDevice(new RandomIODevice()); //random numbers
        MMU mmu=new MMU()
                {
                    protected RandomAccessMemory getMem()
                    {
                        return physRam;
                    }
                };
        ram=mmu;
        registerDevice(mmu);//dont register this if you want a passthrough
                            //stupid mmu
        registerDevice(pic);
        registerDevice(new UART(10)); //10 clocks per bit (fast!)
        registerDevice(new UART(10)); //another..
        registerDevice(new VIC()
                {
                    public Display getDisplay()
                    {
                        return Processor.this.display;
                    }
                }); //video
        registerDevice(new RTC());
    }
    /**
     *  register a new device which might either be an
     *  IODevice, a MMAPDevice or both. It will be allocated the
     *  next free ioports and memory addresses
     */
    public void registerDevice(Device d)
    {        
        if(d instanceof IODevice)
        {  
            int base=0;
            for(IODevice iod:ioDevices)
            {
                base+=iod.getPortCount();
            }
            /*  remap the device so it sees itself starting from 0*/
            ioDevices.add(new AddressMappingIODevice(base,(IODevice)d));
        }
        if(d instanceof InterruptGenerator &&
            ! (d instanceof Pic))
        {
            ((InterruptGenerator)d).link(pic);
            pic.add((InterruptGenerator)d);
        }
        //handle MMAPDevices
    }
    /**
     *  reset all devices to reboot state
     */
    public void resetDevices()
    {
        for(IODevice iod:ioDevices)
        {
            iod.reset();
        }
        //MMAP devices
    }
    /**
     *  give all devices a chance to update their state
     *  with PIC last.
     */
    public void updateDevices()
    {
        for(IODevice iod:ioDevices)
        {
            if(!(iod instanceof Pic))
            {
                iod.update();
            }
        }
        //MMAP devices
        pic.update();
    }
    /**
     *  get the IODevice which handles this port or 
     *  InvalidIODevice
     */
    protected IODevice getDevice(int port)
    {
        int base=0;
        for(IODevice iod:ioDevices)
        {
            base+=iod.getPortCount();
            if(base>port)
                return iod;
        }
        return new InvalidIODevice(65000);
    }

    /** Returns the memory attached to the processor. 
        will actually return the virtual ram how it is viewed by
        the processor*/
    public TTK91Memory getMemory() {
        return ram;
    }
    /**
     *  will return the underlying physical view of the ram
     */
    public TTK91Memory getPhysicalMemory()
    {
        return physRam;
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
        reinitMemory(memsize);
	regs = new Registers();
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

/** Process next instruction.
    @return RunInfo created by RunDebugger. */
    public RunInfo runLine() throws TTK91RuntimeException{
        if (status == TTK91Cpu.STATUS_SVC_SD) return null;
        // if last command set status to ABNORMAL_EXIT repeat that command
        if (status == TTK91Cpu.STATUS_ABNORMAL_EXIT)
            regs.setRegister (TTK91Cpu.CU_PC, regs.getRegister (TTK91Cpu.CU_PC) -1);
            
        /*  give peripherals some attention*/
        updateDevices();
        checkForInterrupt();
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
            int ADDR = (short) (IR.getBinary()&0xFFFF);                     // address
            
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
            //            else if (opcode >= 17 && opcode <= 27) alu (opcode, Rj, param); // Modified to support 'NOT' - Lauri 2004-09-23
            else if (opcode >= 17 && opcode <= 28) alu (opcode, Rj, param);
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

	//Added by HT, 12.10.2004, Koskelo-project
        ++this.commands_executed;

        // update PC_CURRENT
        regs.setRegister (TTK91Cpu.CU_PC_CURRENT, regs.getRegister (TTK91Cpu.CU_PC));
        
        // give registers to runDebugger
        int[] registers = new int[8];
        for (int i=0; i < registers.length; i++) 
            registers[i] = regs.getRegister (i + TTK91Cpu.REG_R0);
        runDebugger.setRegisters (registers);
            
        return runDebugger.cycleEnd();
    }

    //Added by HT, 12.10.2004, Koskelo-project
    public int giveCommAmount() {

        return this.commands_executed;

    }//giveCommAmount

    //Added by HT, 12.10.2004, Koskelo-project
    public int giveStackSize() {

        return this.stack_size;

    }//giveStackSize

    //Added by LL, 12.12.2004, Koskelo-project
    public int giveStackMaxSize() {
        return this.stack_max_size;
    }

/** Transfer-operations. */
    private void transfer(int oc, int Rj, int M, int ADDR, int param) 
    throws TTK91RuntimeException {
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        OpCode opcode=OpCode.getOpCode(oc);
        IODevice dev=null;
        switch (opcode) {
            case STORE : // STORE
            if (M == 2) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91BadAccessMode(new Message (Processor.STORE_BAD_ACCESS_MODE_MESSAGE).toString());
            }
            
            if (M == 0) writeToMemory (ADDR, regs.getRegister(Rj));
            if (M == 1) writeToMemory (ram.getValue (ADDR), regs.getRegister(Rj));
            break;
            
            case LOAD : // LOAD
            regs.setRegister (Rj, param);
            break;
            
            case IN : // IN
                try{
                    dev=getDevice(param);
                    regs.setRegister(Rj,dev.getPort(param));
                    status = TTK91Cpu.STATUS_STILL_RUNNING;
                }catch(TTK91RuntimeException id)
                {
                    status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                    throw id;
                }                                
            break;
            
            case OUT : // OUT     
                try{
                    dev=getDevice(param);                                
                    dev.setPort(param,Rj);
                }catch(TTK91RuntimeException id)
                {
                    status = TTK91Cpu.STATUS_ABNORMAL_EXIT;                    
                    throw id;
                }
            break;
        }
    }


/** ALU-operations.
    @return Result of the ALU-operation. */
    private void alu(int oc, int Rj, int param) 
    throws TTK91IntegerOverflow, TTK91DivisionByZero {
    runDebugger.setOperationType (RunDebugger.ALU_OPERATION);
        OpCode opcode=OpCode.getOpCode(oc);
        long n;
        switch (opcode) {
            case ADD : // ADD
            n = (long)regs.getRegister (Rj) + (long)param;
            if (isOverflow (n)) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91IntegerOverflow(new Message (Processor.INTEGER_OVERFLOW_MESSAGE).toString());
            }
            regs.setRegister (Rj, (int)n);
            break;
            
            case SUB : // SUB
            n = (long)regs.getRegister (Rj) - (long)param;
            if (isOverflow (n)) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91IntegerOverflow(new Message (Processor.INTEGER_OVERFLOW_MESSAGE).toString());
            }
            regs.setRegister (Rj, (int)n);
            break;
            
            case MUL : // MUL
            n = (long)regs.getRegister (Rj) * (long)param;
            if (isOverflow (n)) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91IntegerOverflow(new Message (Processor.INTEGER_OVERFLOW_MESSAGE).toString());
            }
            regs.setRegister (Rj, (int)n);
            break;
            
            case DIV : // DIV
            if (param == 0) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91DivisionByZero(new Message (Processor.DIVISION_BY_ZERO_MESSAGE).toString());
            }
            regs.setRegister (Rj, regs.getRegister (Rj) / param);
            break;
            
            case MOD : // MOD
            if (param == 0) {
                status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
                throw new TTK91DivisionByZero(new Message (Processor.DIVISION_BY_ZERO_MESSAGE).toString());
            }
            regs.setRegister (Rj, regs.getRegister (Rj) % param);
            break;
            
            case AND : // AND
            regs.setRegister (Rj, regs.getRegister (Rj) & param);
            break;
            
            case OR : // OR
            regs.setRegister (Rj, regs.getRegister (Rj) | param);
            break;
            
            case XOR : // XOR
            regs.setRegister (Rj, regs.getRegister (Rj) ^ param);
            break;
            
            case SHL : // SHL
            regs.setRegister (Rj, regs.getRegister(Rj) << param);
            break;
            
            case SHR : // SHR
            regs.setRegister (Rj, regs.getRegister(Rj) >>> param);
            break;
            //SHRA MOVED
            case NOT : // NOT
                regs.setRegister (Rj, ~(regs.getRegister(Rj)) ); // Command 'NOT' added 2004-09-23
                break;

            case SHRA : // SHRA
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
    private void branch(int oc, int Rj, int M, int ADDR, int param)
    throws TTK91BadAccessMode {
        runDebugger.setOperationType (RunDebugger.BRANCH_OPERATION);
        if (M == 2) {
            status = TTK91Cpu.STATUS_ABNORMAL_EXIT;
            throw new TTK91BadAccessMode(new Message (Processor.BRANCH_BAD_ACCESS_MODE_MESSAGE).toString());
        }
        OpCode opcode=OpCode.getOpCode(oc);
        switch (opcode) {
            case JUMP : // JUMP
            setNewPC (param);
            break;

            case JNEG : // JNEG
            if (regs.getRegister (Rj) < 0) setNewPC (param);
            break;

            case JZER : // JZER
            if (regs.getRegister (Rj) == 0) setNewPC (param);
            break;

            case JPOS : // JPOS
            if (regs.getRegister (Rj) > 0) setNewPC (param);
            break;

            case JNNEG : // JNNEG
            if (regs.getRegister (Rj) >= 0) setNewPC (param);
            break;

            case JNZER : // JNZER
            if (regs.getRegister (Rj) != 0) setNewPC (param);
            break;

            case JNPOS : // JNPOS
            if (regs.getRegister (Rj) <= 0) setNewPC (param);
            break;

            case JLES : // JLES
            if (sr[2]) setNewPC (param);
            break;

            case JEQU : // JEQU
            if (sr[1]) setNewPC (param);
            break;

            case JGRE : // JGRE
            if (sr[0]) setNewPC (param);
            break;

            case JNLES : // JNLES
            if (sr[1] || sr[0]) setNewPC (param);
            break;

            case JNEQU : // JNEQU
            if (sr[2] || sr[0]) setNewPC (param);
            break;

            case JNGRE : // JNGRE
            if (sr[2] || sr[1]) setNewPC (param);
            break;
        }
    }
/** Stack. */
    private void stack(int oc, int Rj, int Ri, int param) 
    throws TTK91AddressOutOfBounds {
        runDebugger.setOperationType (RunDebugger.STACK_OPERATION);
        OpCode opcode=OpCode.getOpCode(oc);
        switch (opcode) {
            case PUSH : // PUSH
            regs.setRegister (Rj, regs.getRegister(Rj) +1);
            writeToMemory (regs.getRegister(Rj), param);
            //Added by HT, 12.10.2004, Koskelo-project, modified by LL, 12.12.2004
            addToStack();
            break;
            
            case POP : // POP
            regs.setRegister (Ri, ram.getValue (regs.getRegister(Rj)));
            regs.setRegister (Rj, regs.getRegister(Rj) -1);
            //Added by HT, 12.10.2004, Koskelo-project
            --this.stack_size;
            break;
            
            case PUSHR : // PUSHR
            for (int i=0; i < 7; i++) {
                regs.setRegister (Rj, regs.getRegister(Rj) +1);
                writeToMemory (regs.getRegister (Rj), regs.getRegister (TTK91Cpu.REG_R0 +i));
                //Added by HT, 12.10.2004, Koskelo-project, modified by LL, 12.12.2004
                addToStack();
            }
            break;
            
            case POPR : // POPR
            for (int i=0; i < 7; i++) {
                regs.setRegister (TTK91Cpu.REG_R6 -i, ram.getValue (regs.getRegister (Rj)));
                regs.setRegister (Rj, regs.getRegister(Rj) -1);
                //Added by HT, 12.10.2004, Koskelo-project
                --this.stack_size;
            }
            break;
        }
    }
    /**
     *  see if we have been interrupted and execute a jump
     *  if so.
     */
    protected void checkForInterrupt()
    throws TTK91AddressOutOfBounds
    {
        if(interrupted)
        {
            /*  fake a CALL*/
            clearInterrupt();
            subr(OpCode.CALL.code(),TTK91Cpu.REG_R0,ram.getValue(0),0);
        }
    }
    
    /** Subroutine. */
    private void subr(int oc, int Rj, int ADDR, int param)
    throws TTK91AddressOutOfBounds {
        runDebugger.setOperationType (RunDebugger.SUB_OPERATION);
        int sp;
        OpCode opcode=OpCode.getOpCode(oc);
        switch (opcode) {
            case CALL : // CALL
            // push PC and FP to stack (Rj is stack pointer)
            sp = regs.getRegister (Rj);
            writeToMemory (++sp, regs.getRegister (TTK91Cpu.CU_PC));
            addToStack(); // <-- by Kohahdus 2006-11-23
            writeToMemory (++sp, regs.getRegister (TTK91Cpu.REG_FP));
            addToStack(); // <-- by Kohahdus 2006-11-23
            
            // update stack and frame pointers
            regs.setRegister (Rj, sp);
            regs.setRegister (TTK91Cpu.REG_FP, sp);
            
            // set PC
            setNewPC (ADDR);
            break;
            
            case EXIT : // EXIT
            // pop FP and PC from stack (Rj is stack pointer)
            sp = regs.getRegister (Rj);
            regs.setRegister (TTK91Cpu.REG_FP, ram.getValue (sp--));
            stack_size--; // <-- by Kohahdus 2006-11-23
            setNewPC (ram.getValue (sp--));
            stack_size--; // <-- by Kohahdus 2006-11-23
            
            // decrease number of parameters from stack
            regs.setRegister (Rj, sp -param);
            stack_size = stack_size - param; // <-- by Kohahdus 2006-11-23
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
            runDebugger.setIN (KBD, kbdData.intValue());
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

    
    //Added by LL, 12.12.2004, Koskelo-project
    private void addToStack() {
	this.stack_size++;
	if (this.stack_size > this.stack_max_size) {
	    ++stack_max_size;
	}
    }
}
