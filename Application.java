/** This class represents a compiled TTK-91-application. It also contains 
    information about what has gotten printed during its running and 
    what it should be reading as various inputs during its run. */
public class Application implements TTK91Application {

    /** @return The binary code in an array. */
    public int[] getSerialized() { }

    /** @return What the application printed to a file during its last run,
	delimited with \n. */
    public String getStdOut() {  }

    /** @return What the application printed to screen during its last 
	run, delimited with \n. */
    public String getCrt() {  }

    /** @param input What the application should read from the keyboard
	during its run. */ // (when it runs out, throw runtime exception)
    public void setKbd(String input) { }

    /** @param input What the application should read from a file
	during its run. */ // (when it runs out, throw runtime exception)
    public void setStdIn(String input) { }
}
