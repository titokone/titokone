package fi.hu.cs.titokone;

import java.awt.*;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.util.*;
import fi.hu.cs.ttk91.TTK91Cpu;


/** This class takes care of the animation window. It digs the 
    needed information from a RunInfo instance. */
public class Animator extends JFrame {
    
  	private final static File backgroundImageFile = new File("animator.gif");
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
        {228,131, 269,131},             // R0
        {228,151, 269,151},             // R1
        {228,171, 269,171},             // R2
        {228,191, 269,191},             // R3
        {228,211, 269,211},             // R4
        {228,231, 269,231},             // R5
        {228,251, 269,251},             // R6
        {228,271, 269,271},             // R7
        {292,131, 269,131},             // TR
        {292,151, 269,151},             // PC
        {292,171, 269,171},             // IR
        {292,191, 269,191},             // SR
        {292,321, 269,321},             // BASE
        {292,341, 269,341},             // LIMIT
        {292,361, 269,361},             // MAR
        {292,381, 269,381},             // MBR
        {140,345, 140,330, 269,330},    // ALU_IN1
        {210,345, 210,330, 269,330},    // ALU_IN2
        {180,420, 180,432, 269,432},    // ALU_OUT
        {540,415, 540,487, 269,487},    // EXTERNAL_DEVICE
        {680,415, 680,487, 269,487},    // MEMORY
    };
    
    private final static int[][] whereWriteValueTo = {
        {165,138},                      // R0
        {165,158},                      // R1
        {165,178},                      // R2
        {165,198},                      // R3
        {165,218},                      // R4
        {165,238},                      // R5
        {165,258},                      // R6
        {165,278},                      // R7
        {305,137},                      // TR
        {305,157},                      // PC
        {305,177},                      // IR
        {305,197},                      // SR
        {305,328},                      // BASE
        {305,348},                      // LIMIT
        {305,368},                      // MAR
        {305,388},                      // MBR
        {114,368},                      // ALU_IN1
        {182,368},                      // ALU_IN2
        {154,409},                      // ALU_OUT
    };
    
    /** Contains values of registers, alu, memory and external device. */
    private int[] value = new int[routeToBus.length];
   
    /** Current command label */
    private String currentCommand = "";
    
    /** Comment label */
    private String comment = "";

    private BufferedImage backgroundImage, doubleBuffer;
    private int pointX=-1, pointY=-1;
    
    public Animator (int wide, int height, String title)
    throws IOException {
        // read the background image
        BufferedImage bi = ImageIO.read (backgroundImageFile);
		backgroundImage = new BufferedImage (bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        doubleBuffer = new BufferedImage (bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
		backgroundImage.createGraphics().drawImage (bi, 0,0, null);
        
        setSize (wide, height);
        setTitle (title);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        show();
    }
    
    public void paint (Graphics g) {
        // copy backround image to dublebuffer
        backgroundImage.copyData (doubleBuffer.getRaster());
        
        Graphics g2 = doubleBuffer.createGraphics();
        g2.setColor (Color.BLACK);
        
        // write values (registers, alu, control unit)
    	g2.setFont (textFont);
        
        for (int i=0; i < whereWriteValueTo.length; i++)
            g2.drawString ("" + value[i], whereWriteValueTo[i][0], whereWriteValueTo[i][1]);
            
        // write current command and comments
        g2.drawString (currentCommand, 355, 48);
        g2.drawString (comment, 106, 559);
        
        // draw red animation spot
        if (pointX != -1) {
            g2.setColor (Color.RED);
            g2.fillOval (pointX -5, pointY -5, 10, 10);
        }
        
        // draw double buffer to frame
        g.drawImage (doubleBuffer, 0,0, getWidth(), getHeight(), null);
    }

    /** This method produces an animation of a command based on 
    the information in the RunInfo parameter.
    @param info A RunInfo to base the animation on. */
    public void animate(RunInfo info) {
    /*
* Move PC to MAR
* Get a command to IR from MBR
* Upadte PC
* Convert it to a command
* Get operands  (not all)
* Do ALU etc.
* Store result  (not all)
http://www.cs.helsinki.fi/u/kerola/tito/html/lu05_files/frame.html
    */
        currentCommand = "";        
        // animate instruction fetch
        comment = new Message ("Fetch the next instruction from memory slot {0} to IR.", ""+value[PC]).toString();
        value[PC]++;
        animateAnEvent (PC, MAR, value[PC]-1);
        animateAnEvent (MAR, MEMORY);
        animateAnEvent (MEMORY, MBR, info.getBinary());
        animateAnEvent (MBR, IR);
        currentCommand = info.getLineContents();
        pause();
        
        int opcode = info.getBinary() >>> 24;
        int Rj = info.getFirstOperand()[0];
        int Rj_value = info.getFirstOperand()[1];
        int Ri = info.getIndexRegister()[0];
        int Ri_value = info.getIndexRegister()[1];
        int ADDR = info.getADDR();
        int memoryFetches = info.getMemoryfetches();
        
        if (Ri != 0) ADDR += Ri_value;
        int param = ADDR;
        int whereIsSecondOperand = IR;
        if (memoryFetches == 1) {
            param = info.getValueAtADDR();
            whereIsSecondOperand = MBR;
            comment = new Message ("Fetch second operand from memory slot {0}.", ""+ADDR).toString();
            animateAnEvent (IR, MAR, ADDR);
            animateAnEvent (MAR, MEMORY);
            animateAnEvent (MEMORY, MBR, param);
        }
        else if (memoryFetches == 2) {
            param = info.getSecondFetchValue();
            whereIsSecondOperand = MBR;
            comment = new Message ("Indirect memory accessing mode. 1: Fetch indexing value from memory slot {0}.", ""+ADDR).toString();
            animateAnEvent (IR, MAR, ADDR);
            animateAnEvent (MAR, MEMORY);
            animateAnEvent (MEMORY, MBR, info.getValueAtADDR());
            comment = new Message ("Indirect memory accessing mode. 2: Fetch second operand from memory slot {0}.", ""+value[MBR]).toString();
            pause();
            animateAnEvent (MBR, MAR);
            animateAnEvent (MAR, MEMORY);
            animateAnEvent (MEMORY, MBR, param);
        }
  
        
        switch (info.getOperationtype()) {
            case RunDebugger.NO_OPERATION :
            comment = new Message ("No-operation command compleated.").toString();
            pause();
            break;
            
            case RunDebugger.DATA_TRANSFER_OPERATION :
            switch (opcode) {
                case 1 : // STORE
                comment = new Message ("Write value {0} from register R{1} to memory slot {2}.", new String[] {""+Rj_value, ""+Rj, ""+param}).toString();
                animateAnEvent (whereIsSecondOperand, MAR, param);
                animateAnEvent (Rj, MBR);
                animateAnEvent (MBR, MEMORY);
                pause();
                break;
                
                case 2 : // LOAD
                comment = new Message ("Load value {0} to register R{1}.", new String[] {""+param, ""+Rj}).toString();
                animateAnEvent (whereIsSecondOperand, Rj, param);
                pause();
                break;
                
                case 3 : // IN
                int inValue = info.whatIN()[1];
                comment = new Message ("Read value {0} from device {1} to register R{2}.", new String[] {""+inValue, info.whatDevice(), ""+Rj}).toString();
                animateAnEvent (whereIsSecondOperand, EXTERNAL_DEVICE);
                animateAnEvent (EXTERNAL_DEVICE, Rj, inValue);
                pause();
                
                case 4 : // OUT
                int outValue = info.whatOUT()[1];
                comment = new Message ("Write value {0} from register R{1} to device {2}.", new String[] {""+value[Rj], ""+Rj, info.whatDevice()}).toString();
                animateAnEvent (whereIsSecondOperand, EXTERNAL_DEVICE);
                animateAnEvent (Rj, EXTERNAL_DEVICE);
                pause();
            }
            break;

            case RunDebugger.ALU_OPERATION :
            comment = new Message ("Copy register R{0} to ALU IN1.", ""+Rj).toString();
            animateAnEvent (Rj, ALU_IN1);
            comment = new Message ("Copy second operand to ALU IN2.").toString();
            animateAnEvent (whereIsSecondOperand, ALU_IN2, param);
            comment = new Message ("ALU computes the result.").toString();
            pause();
            comment = new Message ("Copy ALU result to register R{0}", ""+Rj).toString();
            value[ALU_OUT] = info.getALUResult();
            animateAnEvent (ALU_OUT, Rj);
            pause();
            break;

            case RunDebugger.COMP_OPERATION :
            
            break;

            case RunDebugger.BRANCH_OPERATION :
            break;

            case RunDebugger.SUB_OPERATION :
            break;

            case RunDebugger.STACK_OPERATION :
            break;

            case RunDebugger.SVC_OPERATION :
            break;
        }
/*
        currentCommand = "LOAD R0, 2";
        value[PC] = 5;
        repaint();
        pause();
        comment = "Fetch next instruction from memory to IR";
        value[PC] = 6;
        animateAnEvent (PC, MAR, 5);
        animateAnEvent (MAR, MEMORY);
        animateAnEvent (MEMORY, MBR, 34078722);
        animateAnEvent (MBR, IR);
        comment = "Fetch second parameter from memory";
        repaint();
        pause();
        animateAnEvent (IR, MAR, 2);
        animateAnEvent (MAR, MEMORY);
        animateAnEvent (MEMORY, MBR, 101);
        comment = "Store result to R0";
        repaint();
        pause();
        animateAnEvent (MBR, R0);
        
        currentCommand = "MUL R0, =10";
        comment = "Fetch next instruction from memory to IR";
        repaint();
        pause();
        value[PC] = 7;
        animateAnEvent (PC, MAR, 6);
        animateAnEvent (MAR, MEMORY);
        animateAnEvent (MEMORY, MBR, 318767114);
        animateAnEvent (MBR, IR);
        comment = "Transfer first parameter to ALU IN1";
        repaint();
        pause();
        animateAnEvent (R0, ALU_IN1);
        comment = "Transfer second parameter to ALU IN2";
        animateAnEvent (IR, ALU_IN2, 10);
        value[ALU_OUT] = 1010;
        comment = "Transfer ALU result to R0";
        try {Thread.sleep(500);} catch (Exception e) {}
        repaint();
        pause();
        animateAnEvent (ALU_OUT, R0);
*/     
    }

    /** This method animates one event like "move 7 from R1 to In2 in ALU using
        the bus in between" The building block of a more complex operations like 
    "STORE R1, 100" where one needs to fetch an instruction, decipher it etc.
    Each time something changes wait for user input (space-bar) to continue.
        @param from From where does the value come from.
        @param to Where is the value going to be transported.
        @param value Value to be moved. tos new value.      */
    private void animateAnEvent(int from, int to, int newValue) {
        
        // form the route
        int routeLength = routeToBus[from].length +routeToBus[to].length;
        int[] route = new int[routeLength];
        for (int i=0; i < routeToBus[from].length; i++)
            route[i] = routeToBus[from][i];
        for (int i=0; i < routeToBus[to].length; i+=2) {
            route[i +routeToBus[from].length] =    routeToBus[to][routeToBus[to].length -i -2];
            route[i +1 +routeToBus[from].length] = routeToBus[to][routeToBus[to].length -i -1];
        }
        
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
                    Thread.sleep(40*2);
                } catch (Exception e) {}
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
        repaint();
        try {Thread.sleep(3000);} catch (Exception e) {}
    }
    
    public static void main (String[] args) throws IOException {
        Animator a = new Animator(800, 600, "Animator");
        RunDebugger runDebugger = new RunDebugger();
/*
        
        runDebugger.cycleStart (0, "NOP", 1, 2,2);
        runDebugger.setOperationType (RunDebugger.NO_OPERATION);
        runDebugger.runCommand (0, 0, 0);
        a.animate (runDebugger.cycleEnd());
        
        runDebugger.cycleStart (1, "STORE R1, @100", 2, 2,2);
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.setValueAtADDR (666);
        runDebugger.runCommand (27787365, 0,0);
        a.animate (runDebugger.cycleEnd());
        
        runDebugger.cycleStart (0, "LOAD R2, @10(R1)", 1, 2,2);
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.setValueAtADDR (100);
        runDebugger.setSecondFetchValue (1000);
        runDebugger.runCommand (38862858, 0,0);
        a.animate (runDebugger.cycleEnd());
        
        runDebugger.cycleStart (0, "IN R7, 10(R1)", 1, 2,2);
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.setIN (1, 75);
        runDebugger.runCommand (65601546, 0,0);
        a.animate (runDebugger.cycleEnd());

        runDebugger.cycleStart (0, "OUT R3, =0", 1, 2,2);
        runDebugger.setOperationType (RunDebugger.DATA_TRANSFER_OPERATION);
        runDebugger.setOUT (0, -1);
        runDebugger.runCommand (73400320, 0,0);
        a.animate (runDebugger.cycleEnd());
*/
        runDebugger.cycleStart (0, "ADD R6, =20", 1, 2,2);
        runDebugger.setOperationType (RunDebugger.ALU_OPERATION);
        runDebugger.setALUResult (20);
        runDebugger.runCommand (297795604, 0,0);
        a.animate (runDebugger.cycleEnd());
        
        
        
     }
}
