// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

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

		Namespace ns = null;

		ArgumentParser parser = ArgumentParsers.newArgumentParser("Titokone")
				.defaultHelp(true)
				.description("A TTK-91 machine language simulator.");
		parser.addArgument("-v", "--verbosity")
				.choices("info", "fine", "finer").setDefault("info")
				.help("Specify verbosity level");

		try {
			ns = parser.parseArgs(args);
		} catch (ArgumentParserException e) {
			parser.handleError(e);
			System.exit(1);
		}

		handleParameters(ns);

        GUI gui = new GUI();
        try {
			gui.start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-2);
		}
    }

    private static void handleParameters(Namespace ns) {
		String verbosity = ns.getString("verbosity");
        Logger myLogger = Logger.getLogger(PACKAGE);

        if (verbosity.equals("info")) {
            myLogger.setLevel(Level.INFO);
        } else if (verbosity.equals("fine")) {
            myLogger.setLevel(Level.FINE);
        } else if (verbosity.equals("finer")) {
            myLogger.setLevel(Level.FINER);
        }
    }
}




