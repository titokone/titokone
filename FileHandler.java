/** This class transforms files into various sorts of buffer classes
    depending on who needs them, and saves these buffer classes to
    files when needed. The buffer classes will not be dependent on
    files and I/O operations anymore, and therefore will not throw 
    eg. IOExceptions when read. */
public class FileHandler {
    /** This function loads up a Source file from a given file.
	@param filename The full or relative path to the file to read from.
	@return A source instance which is no longer dependent on I/O. 
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
    public Source loadSource(String filename) throws IOException {}

    // (No saveSource is needed.)

    /** This function loads a settings file into a StringBuffer.
	@param filename The full or relative path to the file to read from. 
	@return A StringBuffer which no longer depends on I/O.
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
    public StringBuffer loadSettings(String filename) throws IOException {}

    /** This method saves settings data from a StringBuffer to a file.
	The line separator will be 
	System.getProperty("line.separator", "\n").
	@param settingsData The settings data in a StringBuffer in the 
	form it is to be saved in. The linebreaks in the file will be \ns.
	@param filename The full or relative path to the file to save to. 
	@throws IOException If an I/O error occurs, eg. the directory 
	the file should be in does not exist or we cannot write to it. */
    public void saveSettings(StringBuffer settingsData, String filename) 
	throws IOException {}

    /** This function loads a Binary from a binary .b91 file and
	returns the result. 
	@param filename The full or relative path to the file to read the 
	binary from. 
	@return An Binary instance containing the contents of the
	.b91 file. 
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
    public Binary loadBinary(String filename) throws IOException {}
   
    /** This method saves a Binary to file in a .b91 binary format.
	@param binary The binary to save to file. 
	@param filename The full or relative path of the file to save the 
	binary to. 
	@throws IOException If an I/O error occurs, eg. the given file 
	cannot be written to. */
    public void saveBinary(Binary binary, String filename)
	throws IOException {}

    /** This method loads a "stdin" file representing the disk into 
	a string. The contents should be integers delimited by \n, 
	\r or \r\n, but the loader does not check that this is the 
	case.
	@param filename The name of the file to load the data from.
	@return A string containing the contents of the file.
	@throws IOException If an I/O error occurs, eg. the given
	file is not found. */
    public String loadStdIn(String filename) throws IOException {}

    /** This method saves a "stdout" file representing the disk. 
	The contents to be saved to the file should be integers
	delimited by \n, \r or \r\n, but no checking is made.
	@param contents The string to save to the file.
	@param filename The file to save the given string to.
	@throws IOException If an I/O error occurs, eg. the given
	file cannot be written to. */
    public void saveStdOut(String contents, String filename) 
	throws IOException {}

}
