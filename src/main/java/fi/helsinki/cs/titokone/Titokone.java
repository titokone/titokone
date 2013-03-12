// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is just a launcher for the actual program. It creates an instance
 * GUI and initiates a debugger, which is accessed all over the program in order
 * to write logs for debugging the program.
 */
public class Titokone {
    /**
     * This variable is a system.exit parameter for when an invalid
     * parameter is given to the application.
     */
    public static final int INVALID_PARAMETER = -1;
    private static final String PACKAGE = "fi.helsinki.cs.titokone";

    public static void main(String[] args) {
        Logger myLogger = Logger.getLogger(PACKAGE);

        myLogger.setLevel(Level.WARNING); // By default we only show warnings.

        for (int i = 0; i < args.length; i++) {
            handleParameter(args[i]);
        }
        GUI gui = new GUI();
        try {
			gui.start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-2);
		}
    }

    private static void handleParameter(String parameter) {
        Logger myLogger = Logger.getLogger(PACKAGE);
        if (parameter.equals("-v")) {
            myLogger.setLevel(Level.INFO);
        } else if (parameter.equals("-vv")) {
            myLogger.setLevel(Level.FINE);
        } else if (parameter.equals("-vvv")) {
            myLogger.setLevel(Level.FINER);
        } else {
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
                "-vvv\tShows 'finer' level logging messages." + lbrk);
    }
}




