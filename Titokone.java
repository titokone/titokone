/** This class is just a launcher for the actual program. It creates an instance
    GUI and initiates a debugger, which is accessed all over the program in order
    to write logs for debugging the program.
*/
public class Titokone {

public static void main( String[] args ) {
  
  GUI gui = new GUI();
    
  if(args.length >= 1) {
    if(args[0].equals("-v")) {
      //set debuglevel 1
    }
    else if(args[0].equals("-vv")) {
      //set debuglevel 2
    }
    else if(args[0].equals("-vvv")) {
      //set debuglevel 3
    }
  } 
}

}