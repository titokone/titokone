package fi.hu.cs.titokone;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.util.*;
import fi.hu.cs.ttk91.TTK91Cpu;


/** This class takes care of the animation window. It digs the 
    needed information from a RunInfo instance. */
public class Animator extends JPanel implements Runnable {
    
    private final static Font textFont = new Font ("Arial", Font.BOLD, 16);

    private final static int R0 = 0;
    private final static int R1 = 1;
    private final static int R2 = 2;
    private final static int R3 = 3;
    private final static int R4 = 4;
    private final static int R5 = 5;
    private final static int R6 = 6;
    private final static int R7 = 7;
    private final static int TR = 8;
    private final static int PC = 9;
    private final static int IR = 10;
    private final static int SR = 11;
    private final static int BASE = 12;
    private final static int LIMIT = 13;
    private final static int MAR = 14;
    private final static int MBR = 15;
    private final static int ALU_IN1 = 16;
    private final static int ALU_IN2 = 17;
    private final static int ALU_OUT = 18;
    private final static int EXTERNAL_DEVICE = 19;
    private final static int MEMORY = 20;
    
    private final static int[][] routeToBus = {
        {182,97,  220,97},              // R0
        {182,119, 220,119},             // R1
        {182,141, 220,141},             // R2
        {182,163, 220,163},             // R3
        {182,185, 220,185},             // R4
        {182,207, 220,207},             // R5
        {182,229, 220,229},             // R6
        {182,251, 220,251},             // R7
        {257,110, 220,110},             // TR
        {257,132, 220,132},             // PC
        {257,154, 220,154},             // IR
        {257,176, 220,176},             // SR
        {257,295, 220,295},             // BASE
        {257,317, 220,317},             // LIMIT
        {257,339, 220,339},             // MAR
        {257,361, 220,361},             // MBR
        {182,327, 220,327},             // ALU_IN1
        {182,349, 220,349},             // ALU_IN2
        {182,393, 220,393},             // ALU_OUT
        {520,423, 520,491, 220,491},    // EXTERNAL_DEVICE
        {661,423, 661,491, 220,491},    // MEMORY
    };
    
    private final static int[][] whereWriteValueTo = {
        {73,105},                       // R0
        {73,127},                       // R1
        {73,149},                       // R2
        {73,171},                       // R3
        {73,193},                       // R4
        {73,215},                       // R5
        {73,237},                       // R6
        {73,259},                       // R7
        {264,117},                      // TR
        {264,139},                      // PC
        {264,161},                      // IR
        {264,183},                      // SR
        {264,303},                      // BASE
        {264,325},                      // LIMIT
        {264,347},                      // MAR
        {264,369},                      // MBR
        {74,334},                       // ALU_IN1
        {74,356},                       // ALU_IN2
        {74,401},                       // ALU_OUT
    };
    
    private final static int[][] whereWriteLabelTo = {
        {35,103},                       // R0
        {35,125},                       // R1
        {35,147},                       // R2
        {35,169},                       // R3
        {35,191},                       // R4
        {35,213},                       // R5
        {35,235},                       // R6
        {35,257},                       // R7
        {376,116},                      // TR
        {376,138},                      // PC
        {376,160},                      // IR
        {376,182},                      // SR
        {376,302},                      // BASE
        {376,324},                      // LIMIT
        {376,346},                      // MAR
        {376,368},                      // MBR
        {35,333},                       // ALU_IN1
        {35,355},                       // ALU_IN2
        {35,400},                       // ALU_OUT
        {35,80},                        // Registers
        {261,80},                       // Control unit
        {261,270},                      // MMU
        {35,305},                       // ALU
        {525,385},                      // External device
        {525,70},                       // Memory
    };

    private final static String[] labels = {
        "R0",
        "R1",
        "R2",
        "R3",
        "R4",
        "R5",
        "R6",
        "R7",
        "TR",
        "PC",
        "IR",
        "SR",
        "BASE",
        "LIMIT",
        "MAR",
        "MBR",
        "IN1",
        "IN2",
        "OUT",
        "Registers",
        "Control unit",
        "MMU",
        "ALU",
        "External device",
        "Memory"
    };        
    
    /** Contains values of registers, alu, memory and external device. */
    private int[] value = new int[routeToBus.length];
   
    /** Current command label */
    private String currentCommand = "";
    
    /** Comment label */
    private String comment1 = "";
    private String comment2 = "";
    
    /** String presentation of status register. */
    private String SR_String = "";
    
    /** If this is true, animation will be executed without repaitings and delays. */
    private boolean executeAnimationImmediately = false;
    
    /** Is animation currently running. */
    private boolean isAnimating = false;
    
    /** Thread, that runs animation. */
    private Thread animationThread;
    
    /** RunInfo object of currently running animation. */
    private RunInfo info;

    private BufferedImage backgroundImage, doubleBuffer;
    private int pointX=-1, pointY=-1;
    private int delay = 40;
    
    /** Creats new animator. 
        @throws IOException If there are problems reading background image throw IOException. */
    public Animator () throws IOException {
        // read the background image
        ClassLoader foo = getClass ().getClassLoader();
        BufferedImage bi = ImageIO.read (foo.getResourceAsStream ("fi/hu/cs/titokone/resources/animator.gif"));
		backgroundImage = new BufferedImage (bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        doubleBuffer = new BufferedImage (bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
		backgroundImage.createGraphics().drawImage (bi, 0,0, null);
    }
    
    public void paint (Graphics g) {
        // copy backround image to dublebuffer
        backgroundImage.copyData (doubleBuffer.getRaster());
        
        Graphics g2 = doubleBuffer.createGraphics();
        g2.setColor (Color.BLACK);
    	g2.setFont (textFont);
        
        // write labels
        for (int i=0; i < labels.length; i++)
            g2.drawString (new Message(labels[i]).toString(), whereWriteLabelTo[i][0], whereWriteLabelTo[i][1]);
        
        // write values (registers, alu, control unit, mmu)
        for (int i=0; i < whereWriteValueTo.length; i++)
            if (i != SR)
                g2.drawString ("" + value[i], whereWriteValueTo[i][0], whereWriteValueTo[i][1]);
            else
                g2.drawString (SR_String, whereWriteValueTo[i][0], whereWriteValueTo[i][1]);
            
        // write current command and comments
        g2.drawString (new Message ("Current command: ").toString() + currentCommand, 217, 23);
        g2.drawString (comment1, 29, 548);
        g2.drawString (comment2, 29, 581);
        
        // draw red animation spot
        if (pointX != -1) {
            g2.setColor (Color.RED);
            g2.fillOval (pointX -5, pointY -5, 10, 10);
        }
        
        // draw double buffer to frame
        g.drawImage (doubleBuffer, 0,0, getWidth(), getHeight(), null);
    }
    
    /** Animation is done in this run method. animate method awakes new thread
        with this run method.
    */
    public void run() {
        // Information about command cycle:
        // http://www.cs.helsinki.fi/u/kerola/tito/html/lu05_files/frame.html
        currentCommand = "";        
        
        // animate instruction fetch
        comment1 = new Message ("Fetch the next instruction from memory slot {0} to IR and increase PC by one.", ""+value[PC]).toString();
        animateAnEvent (PC, MAR);
        animateAnEvent (PC, PC, value[PC]+1);
        animateAnEvent (MAR, MEMORY);
        animateAnEvent (MEMORY, MBR, info.getBinary());
        animateAnEvent (MBR, IR);
        currentCommand = info.getLineContents(); //+ "   (" + info.getColonString() + ")";
        pause();
        
        int opcode = info.getBinary() >>> 24;
        int Rj = info.getFirstOperand();
        int Ri = info.getIndexRegister();
        int ADDR = info.getADDR();
        int memoryFetches = info.getMemoryfetches();
        int[] regs = info.getRegisters();
        int Rj_value = value[Rj];
        int Ri_value = value[Ri];
        
        // if NOP interrupt immediately
        if (opcode == 0) {
            comment1 = new Message ("No-operation command completed.").toString();
            pause();
            isAnimating = executeAnimationImmediately = false;
            return;
        }
        
        if (Ri != 0) ADDR += Ri_value;
        int param = ADDR;
        int whereIsSecondOperand = IR;
        if (memoryFetches == 1) {
            param = info.getValueAtADDR();
            whereIsSecondOperand = MBR;
            comment1 = new Message ("Fetch second operand from memory slot {0}.", ""+ADDR).toString();
            animateAnEvent (IR, MAR, ADDR);
            animateAnEvent (MAR, MEMORY);
            animateAnEvent (MEMORY, MBR, param);
        }
        else if (memoryFetches == 2) {
            param = info.getSecondFetchValue();
            whereIsSecondOperand = MBR;
            comment1 = new Message ("Indirect memory accessing mode.").toString();
            comment2 = new Message ("1: Fetch indexing value from memory slot {0}.", ""+ADDR).toString();
            animateAnEvent (IR, MAR, ADDR);
            animateAnEvent (MAR, MEMORY);
            animateAnEvent (MEMORY, MBR, info.getValueAtADDR());
            comment1 = new Message ("Indirect memory accessing mode.").toString();
            comment2 = new Message ("2: Fetch second operand from memory slot {0}.", ""+value[MBR]).toString();
            pause();
            animateAnEvent (MBR, MAR);
            animateAnEvent (MAR, MEMORY);
            animateAnEvent (MEMORY, MBR, param);
            comment2 = "";
        }
  
        
        switch (info.getOperationtype()) {
            case RunDebugger.DATA_TRANSFER_OPERATION :
            switch (opcode) {
                case 1 : // STORE
                comment1 = new Message ("Write value {0} from register R{1} to memory slot {2}.", new String[] {""+Rj_value, ""+Rj, ""+param}).toString();
                animateAnEvent (whereIsSecondOperand, MAR, param);
                animateAnEvent (Rj, MBR);
                animateAnEvent (MBR, MEMORY);
                break;
                
                case 2 : // LOAD
                comment1 = new Message ("Load value {0} to register R{1}.", new String[] {""+param, ""+Rj}).toString();
                animateAnEvent (whereIsSecondOperand, Rj, param);
                break;
                
                case 3 : // IN
                int inValue = info.whatIN()[1];
                comment1 = new Message ("Read value {0} from {1} to register R{2}.", new String[] {""+inValue, info.whatDevice(), ""+Rj}).toString();
        	animateAnEvent (whereIsSecondOperand, MAR);
		animateAnEvent (MAR, EXTERNAL_DEVICE);
                animateAnEvent (EXTERNAL_DEVICE, MBR, inValue);
                animateAnEvent (MBR, Rj);
                break;
                
                case 4 : // OUT
                int outValue = info.whatOUT()[1];
                comment1 = new Message ("Write value {0} from register R{1} to {2}.", new String[] {""+value[Rj], ""+Rj, info.whatDevice()}).toString();
                animateAnEvent (Rj, MBR);
		animateAnEvent (MBR, EXTERNAL_DEVICE);
		
                break;
            }
            pause();
            break;

            case RunDebugger.ALU_OPERATION :
            comment1 = new Message ("Copy register R{0} to ALU IN1.", ""+Rj).toString();
            animateAnEvent (Rj, ALU_IN1);
            comment1 = new Message ("Copy second operand to ALU IN2.").toString();
            animateAnEvent (whereIsSecondOperand, ALU_IN2, param);
            comment1 = new Message ("ALU computes the result.").toString();
            pause();
            comment1 = new Message ("Copy ALU result to register R{0}", ""+Rj).toString();
            value[ALU_OUT] = info.getALUResult();
            animateAnEvent (ALU_OUT, Rj);
            pause();
            break;

            case RunDebugger.COMP_OPERATION :
            comment1 = new Message ("Copy register R{0} to ALU IN1.", ""+Rj).toString();
            animateAnEvent (Rj, ALU_IN1);
            comment1 = new Message ("Copy second operand to ALU IN2.").toString();
            animateAnEvent (whereIsSecondOperand, ALU_IN2, param);
            comment1 = new Message ("ALU computes the comparison result.").toString();
            comment2 = new Message ("0=greater, 1=equals, 2=less").toString();
            pause();
            value[ALU_OUT] = info.getCompareStatus();
            comment1 = new Message ("Set comparison result to SR").toString();
            animateAnEvent (ALU_OUT, SR);
            switch (info.getCompareStatus()) {
                case  0 : SR_String = "1   0   0...";break;
                case  1 : SR_String = "0   1   0...";break;
                case  2 : SR_String = "0   0   1...";break;
                default : SR_String = "0   0   0...";break;
            }
            pause();
            comment2 = "";
            break;

            case RunDebugger.BRANCH_OPERATION :
            if (value[PC] == info.getNewPC()) {
                comment1 = new Message ("Branching command - branching condition is false, so do nothing.").toString();
            }
            else {
                comment1 = new Message ("Branching command - branching condition is true, so update PC.").toString();
                animateAnEvent (whereIsSecondOperand, PC, param);
            }
            pause();
            break;

            case RunDebugger.SUB_OPERATION :
            if (opcode == 49) { // CALL
                comment1 = new Message ("Save new PC to TR").toString();
                animateAnEvent (whereIsSecondOperand, TR, param);
                
                comment1 = new Message ("Increase stack pointer R{0} by one and push PC to stack.", ""+Rj).toString();
                animateAnEvent (Rj, Rj, value[Rj]+1);
                animateAnEvent (Rj, MAR);
                animateAnEvent (PC, MBR);
                animateAnEvent (MBR, MEMORY);

                comment1 = new Message ("Increase stack pointer R{0} by one and push FP to stack.", ""+Rj).toString();
                animateAnEvent (Rj, Rj, value[Rj]+1);
                animateAnEvent (Rj, MAR);
                animateAnEvent (R7, MBR);
                animateAnEvent (MBR, MEMORY);
                
                comment1 = new Message ("Copy stack pointer to FP.").toString();
                animateAnEvent (Rj, R7);
                
                comment1 = new Message ("Update PC.").toString();
                animateAnEvent (TR, PC);
            }
            else if (opcode == 50) {
                comment1 = new Message ("Pop PC from stack and decrease stack pointer R{0} by one.", ""+Rj).toString();
                animateAnEvent (Rj, MAR);
                animateAnEvent (Rj, Rj, value[Rj]-1);
                animateAnEvent (MAR, MEMORY);
                animateAnEvent (MEMORY, MBR, info.getNewPC());
                animateAnEvent (MBR, PC);
                
                comment1 = new Message ("Pop FP from stack and decrease stack pointer R{0} by one.", ""+Rj).toString();
                animateAnEvent (Rj, MAR);
                animateAnEvent (Rj, Rj, value[Rj]-1);
                animateAnEvent (MAR, MEMORY);
                animateAnEvent (MEMORY, MBR, regs[7]);
                animateAnEvent (MBR, R7);
                
                comment1 = new Message ("Decrease {0} parameters from stack pointer R{1}.", new String[] {""+param, ""+Rj}).toString();
                animateAnEvent (Rj, Rj, regs[Rj]);
            }
            pause();
            break;

            case RunDebugger.STACK_OPERATION :
            int popValue;
            switch (opcode) {
                case 51 : // PUSH
                comment1 = new Message ("Increase stack pointer R{0} by one then write second operand to stack", ""+Rj).toString();
                animateAnEvent (Rj, Rj, value[Rj]+1);
                animateAnEvent (Rj, MAR);
                animateAnEvent (whereIsSecondOperand, MBR);
                animateAnEvent (MBR, MEMORY);
                break;
                
                case 52 : // POP
                comment1 = new Message ("Read value from stack to R{0} then decrease stack pointer R{1} by one.", new String[] {""+Ri,""+Rj}).toString();
                popValue = regs[Ri];    // popped value founds at destination register
                if (Ri == Rj) popValue++;
                animateAnEvent (Rj, MAR);
                animateAnEvent (MAR, MEMORY);
                animateAnEvent (MEMORY, MBR, popValue);
                animateAnEvent (MBR, Ri);
                animateAnEvent (Rj, Rj, value[Rj]-1);
                break;
                
                case 53 : // PUSHR
                for (int i=0; i <= 6; i++) {
                    comment1 = new Message ("Increase stack pointer R{0} by one then write R{1} to stack.", new String[] {""+Rj, ""+i}).toString();
                    animateAnEvent (Rj, Rj, value[Rj]+1);
                    animateAnEvent (Rj, MAR);
                    animateAnEvent (i, MBR);
                    animateAnEvent (MBR, MEMORY);
                }
                break;
                
                case 54 : // POPR
                for (int i=6; i >= 0; i--) {
                    comment1 = new Message ("Read value from stack then decrease stack pointer R{0} by one.", ""+Rj).toString();
                    popValue = regs[i];    // popped value founds at destination register
                    if (i == Rj) popValue += Rj+1;
                    
                    animateAnEvent (Rj, MAR);
                    animateAnEvent (MAR, MEMORY);
                    animateAnEvent (MEMORY, MBR, popValue);
                    animateAnEvent (MBR, i);
                    animateAnEvent (Rj, Rj, value[Rj]-1);
                }
                break;
            }
            pause();
            break;

            case RunDebugger.SVC_OPERATION :
            comment1 = new Message ("Supervisor call to operating system's services.").toString();
            pause();
            // update possibly changed registers
            for (int i=0; i < 8; i++)
                value[i] = regs[i];
            break;
        }
        isAnimating = executeAnimationImmediately = false;
    }
    
    /** Stops currently running animation if there is one. The rest of the animation is done
        without delays and this method returns after current animation is done and new values
        are updated.
    */
    public void stopAnimation() {
        if (!isAnimating) return;
        
        executeAnimationImmediately = true;
        animationThread.interrupt();
        while (isAnimating) {
            try {
                Thread.sleep (10);
            }
            catch (InterruptedException e) {}
        }
    }
    
    public boolean isAnimationRunning() {
        return isAnimating;
    }

    /** This method produces an animation of a command based on 
    the information in the RunInfo parameter. Animation is set to run only if 
    there is no animation currently running. 
    @param info A RunInfo to base the animation on. 
    @return Returns false, if there is already an animation running. */
    public boolean animate(RunInfo info) {
        if (isAnimating) return false;  // cannot do two animations at the same time
        isAnimating = true;
        this.info = info;
        animationThread = new Thread(this);
        animationThread.start();
        return true;
    }
    
    /** Initalizes animation.
    @param cpu A TTK91Cpu, giving the start values of the registers.
    @param baseValue Value of the BASE register in MMU.
    @param limitValue Value of the LIMIT register in MMU. */
    public void init (TTK91Cpu cpu, int baseValue, int limitValue) {
        value[R0] = cpu.getValueOf (TTK91Cpu.REG_R0);
        value[R1] = cpu.getValueOf (TTK91Cpu.REG_R1);
        value[R2] = cpu.getValueOf (TTK91Cpu.REG_R2);
        value[R3] = cpu.getValueOf (TTK91Cpu.REG_R3);
        value[R4] = cpu.getValueOf (TTK91Cpu.REG_R4);
        value[R5] = cpu.getValueOf (TTK91Cpu.REG_R5);
        value[R6] = cpu.getValueOf (TTK91Cpu.REG_R6);
        value[R7] = cpu.getValueOf (TTK91Cpu.REG_R7);
        value[PC] = cpu.getValueOf (TTK91Cpu.CU_PC);
        value[IR] = cpu.getValueOf (TTK91Cpu.CU_IR);
        value[TR] = cpu.getValueOf (TTK91Cpu.CU_TR);
        value[SR] = -1;
        SR_String = "0   0   0...";
        value[BASE] = baseValue;
        value[LIMIT] = 1<<limitValue;
    }
    
    /** Sets animation delay.
        @param delay Delay between frames in milliseconds. */
    public void setAnimationDelay (int delay) {
        this.delay = delay;
    }

    /** This method animates one event like "move 7 from R1 to In2 in ALU using
        the bus in between" The building block of a more complex operations like 
        "STORE R1, 100" where one needs to fetch an instruction, decipher it etc.
        @param from From where does the value come from.
        @param to Where is the value going to be transported.
        @param newValue New value replaces the old value in destination. */
    private void animateAnEvent(int from, int to, int newValue) {
        if (executeAnimationImmediately) {
            value[to] = newValue;
            return;
        }
            
        // form the route
        int routeLength = routeToBus[from].length +routeToBus[to].length;
        int[] route = new int[routeLength];
        for (int i=0; i < routeToBus[from].length; i++)
            route[i] = routeToBus[from][i];
        for (int i=0; i < routeToBus[to].length; i+=2) {
            route[i +routeToBus[from].length] =    routeToBus[to][routeToBus[to].length -i -2];
            route[i +1 +routeToBus[from].length] = routeToBus[to][routeToBus[to].length -i -1];
        }
	
	// fixes...
	if (from == MAR && to == MEMORY) route = new int[] {378,340, 470,340, 470,90, 516,90};
	if (from == MBR && to == MEMORY) route = new int[] {378,362, 470,362, 470,90, 516,90};
	if (from == MEMORY && to == MBR) route = new int[] {516,90, 470,90, 470,362, 378,362};
	
	if (from == MAR && to == EXTERNAL_DEVICE) route = new int[] {378,340, 470,340, 470,405, 516,405};
	if (from == MBR && to == EXTERNAL_DEVICE) route = new int[] {378,362, 470,362, 470,405, 516,405};
	if (from == EXTERNAL_DEVICE && to == MBR) route = new int[] {516,405, 470,405, 470,362, 378,362};
	
        
        int x1 = route[0];
        int y1 = route[1];
        int x2, y2;
        for (int i=2; i < route.length; i+=2) {
            x2 = route[i];
            y2 = route[i+1];
            while (x1 != x2 || y1 != y2) {
                if (x1 < x2) x1 = Math.min (x2, x1+8);
                if (x1 > x2) x1 = Math.max (x2, x1-8);
                if (y1 < y2) y1 = Math.min (y2, y1+8);
                if (y1 > y2) y1 = Math.max (y2, y1-8);
                
                pointX = x1;
                pointY = y1;
                repaint();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    value[to] = newValue;
                    return;
                }
            }
        }
        pointX = pointY = -1;
        value[to] = newValue;
        repaint();
    }
    
    private void animateAnEvent(int from, int to) {
        animateAnEvent (from, to, value[from]);
    }
    
    private void pause() {
        if (executeAnimationImmediately) return;
        
        /*
	pause is disabled, because there is no need for pausing(?)
        repaint();
        try {Thread.sleep(3000);} catch (Exception e) {}
	*/
    }
    
    public static void main (String[] args) throws IOException {
        Animator a = new Animator();
        a.setAnimationDelay (80);
        a.init (new Processor(512), 0, 512);

        JFrame f = new JFrame();
        f.setSize (810, 636);
        f.setTitle ("Animator");
        f.getContentPane().add (a);
        f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        f.show();
        
        RunDebugger runDebugger = new RunDebugger();
	
        runDebugger.cycleStart (0, "IN R7, 10(R1)");
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.setIN (1, 75);
        runDebugger.runCommand (65601546);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}

        runDebugger.cycleStart (0, "OUT R3, =0");
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.setOUT (0, -1);
        runDebugger.runCommand (73400320);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}
	
        
        runDebugger.cycleStart (1, "STORE R1, @100");
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.setValueAtADDR (666);
        runDebugger.runCommand (27787365);
        a.animate (runDebugger.cycleEnd());
        
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}
        

        runDebugger.cycleStart (0, "NOP");
        runDebugger.setOperationType (RunDebugger.NO_OPERATION);
        runDebugger.runCommand (0);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}
        
        runDebugger.cycleStart (0, "LOAD R2, @10(R1)");
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.setValueAtADDR (100);
        runDebugger.setSecondFetchValue (1000);
        runDebugger.runCommand (38862858);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}

        runDebugger.cycleStart (0, "COMP R0, @30");
        runDebugger.setOperationType (RunDebugger.COMP_OPERATION);
        runDebugger.runCommand (521142302);
        runDebugger.setCompareResult (0);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}
        
        runDebugger.cycleStart (0, "JZER R2, 100");
        runDebugger.setOperationType (RunDebugger.BRANCH_OPERATION);
        runDebugger.setNewPC (32);
        runDebugger.runCommand (574619748);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}
        
        runDebugger.cycleStart (0, "ADD R6, =20");
        runDebugger.setOperationType (RunDebugger.ALU_OPERATION);
        runDebugger.setALUResult (20);
        runDebugger.runCommand (297795604);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}

        runDebugger.cycleStart (0, "LOAD R1, =100");
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.runCommand (35651684);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}
        
        runDebugger.cycleStart (0, "CALL R3, 40(R1)");
        runDebugger.setOperationType (RunDebugger.SUB_OPERATION);
        runDebugger.runCommand (828440616);
        runDebugger.setNewPC (140);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}
        
        runDebugger.cycleStart (0, "EXIT SP, =4");
        runDebugger.setOperationType (RunDebugger.SUB_OPERATION);
        runDebugger.runCommand (851443716);
        runDebugger.setNewPC (5);
        runDebugger.setRegisters (new int[] {0,0,0,0,0,0,-6,100});
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}

        runDebugger.cycleStart (0, "COMP R0, @30");
        runDebugger.setOperationType (RunDebugger.COMP_OPERATION);
        runDebugger.runCommand (521142302);
        runDebugger.setCompareResult (2);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}

        runDebugger.cycleStart (0, "PUSH R6, 100(R4)");
        runDebugger.setOperationType (RunDebugger.STACK_OPERATION);
        runDebugger.runCommand (869007460);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}

        runDebugger.cycleStart (0, "POP R6, R6");
        runDebugger.setOperationType (RunDebugger.STACK_OPERATION);
        runDebugger.runCommand (885391360);
        runDebugger.setRegisters (new int[] {1, 2,3,4,5,6,99,8});
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}


        runDebugger.cycleStart (0, "POPR R0");
        runDebugger.setOperationType (RunDebugger.STACK_OPERATION);
        runDebugger.setRegisters (new int[] {107,106,105,104,103,102,101,100});
        runDebugger.runCommand (905969664);
        a.animate (runDebugger.cycleEnd());
        while (a.isAnimationRunning());
        try {Thread.sleep(3000);} catch (Exception e) {}
     }
}
