/*
 * Created on Feb 24, 2004
 *
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
package fi.hu.cs.ttk91;

/**
 * @author Kalle Kärkkäinen
 *
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
	 * 
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
	 * 
	 * input string must be feed of integers delimeted 
	 * with ' ', '\t', '\r', '\n', ',', '.', ':' or ';'.
	 * if flawed no arguments contained are used.
	 * 
	 * @throws IllegalArgumentException if input string is invalid.
	 */
	public void setStdIn(String fileContent);
}
