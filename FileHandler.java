/** This class transforms files into various sorts of buffer classes
    depending on who needs them, and saves these buffer classes to
    files when needed. */
public class FileHandler {
    
    /** This function loads up a Source file from a given file.
	@param filename The full path to the file to read from.
	@return A source instance which is no longer dependent on I/O. 
	@throws IOException If an I/O error occurs. One of the possible
	IOExceptions is FileNotFoundException. */
    public Source loadSource(String filename) throws IOException { }

    // (no saveSource is needed, presumably?)

    // - load settings from file
    // - save settings to file
    // - load b91 from file (into a TTK91Application-compatible thing?)
    // - save b91 to file (from a TTK91Application.)
}
