package fi.hu.cs.titokone;

import javax.swing.JFrame;

/** This class takes care of the animation window. It digs the 
    needed information from a RunInfo instance. 

public class Animator extends JFrame {

    /** This method produces an animation of a command based on 
	the information in the RunInfo parameter.
	@param info A RunInfo to base the animation on. */
    public void animate(RunInfo info) {}
	/*
* Move PC to MAR
* Get a command to IR from MBR
* Upadte PC
* Convert it to a command
* Get operands 	(not all)
* Do ALU etc.
* Store result	(not all)
http://www.cs.helsinki.fi/u/kerola/tito/html/lu05_files/frame.html
	*/


    /** This method animates one event like "move 7 from R1 to In2 in ALU using
        the bus in between" The building block of a more complex operations like 
	"STORE R1, 100" where one needs to fetch an instruction, decipher it etc.
	Each time something changes wait for user input (space-bar) to continue.
        @param from From where does the value come from.
        @param to Where is the value going to be transported.
        @param value Value to be moved. tos new value.      */
    public void animateAnEvent(String from, String to, int value) {}
	/* Hilight the from value.
	   Hilight the bus in use (r-to-m, r-to-alu ... ).
	   Hilight and change the to value. 
	*/

}
