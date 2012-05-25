// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

/*
 * Created on Feb 24, 2004
 */
package fi.helsinki.cs.ttk91;

/**
 * @author Kalle Kärkkäinen
 *         <p/>
 *         See separate documentation in yhteisapi.pdf in the javadoc root.
 */
public interface TTK91Application {
    /**
     * @return returns the program output in stdout and empties it
     */
    public String readStdOut();

    /**
     * @return returns the program output in crt and empties it
     */
    public String readCrt();

    /**
     * sets the keyboard input for the program.
     * This input survives the run, so if run again
     * program will use the same input again.
     * <p/>
     * input string must be feed of integers delimeted
     * with ' ', '\t', '\r', '\n', ',', '.', ':' or ';'.
     * if flawed no arguments contained are used.
     *
     * @throws IllegalArgumentException if input string is invalid.
     */
    public void setKbd(String input);

    /**
     * sets the stdin input for the program
     * This input survives the run, so if run again
     * program will use the same input again.
     * <p/>
     * input string must be feed of integers delimeted
     * with ' ', '\t', '\r', '\n', ',', '.', ':' or ';'.
     * if flawed no arguments contained are used.
     *
     * @throws IllegalArgumentException if input string is invalid.
     */
    public void setStdIn(String fileContent);
}
