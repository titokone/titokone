package fi.helsinki.cs.titokone;

import java.util.logging.Logger;
import java.util.logging.Level;

/** This class is just a launcher for the actual program. It creates an instance
    GUI and initiates a debugger, which is accessed all over the program in order
    to write logs for debugging the program.
*/
public class Titokone {
  /** This variable is a system.exit parameter for when an invalid
      parameter is given to the application. */
  public static final int INVALID_PARAMETER = -1;
  

  private static final String PACKAGE="fi.helsinki.cs.titokone";
  public static void main( String[] args ) {
    Logger myLogger = Logger.getLogger(PACKAGE);
    GUI gui;
    
    myLogger.setLevel(Level.WARNING); // By default we only show warnings.
    
    for(int i = 0; i < args.length; i++) 
      handleParameter(args[i]);
    gui = new GUI();
  }
  
  private static void handleParameter(String parameter) {
    Logger myLogger = Logger.getLogger(PACKAGE);
    if(parameter.equals("-v")) {
      myLogger.setLevel(Level.INFO);
    }
    else if(parameter.equals("-vv")) {
      myLogger.setLevel(Level.FINE);
    }
    else if(parameter.equals("-vvv")) {
      myLogger.setLevel(Level.FINER);
    }
    else {
      System.err.println("Sorry, parameter '" + parameter + "' is unknown.");
      showAvailableParameters();
      System.exit(INVALID_PARAMETER);
    }
    
  }

  private static void showAvailableParameters() {
    String lbrk = System.getProperty("line.separator");
    System.err.println("Available parameters are:" + lbrk +
		       "-v\tShows 'info' level logging messages. (Default " +
		       "level is 'warning'.)" + lbrk +
		       "-vv\tShows 'fine' level logging messages." + lbrk +
		       "-vvv\tShows 'finer' level logging messages." +lbrk);
  }
}




