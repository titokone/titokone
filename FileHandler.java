package fi.hu.cs.titokone;

import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;
import fi.hu.cs.ttk91.TTK91CompileSource;

/** This class transforms files into various sorts of buffer classes
    depending on who needs them, and saves these buffer classes to
    files when needed. The buffer classes will not be dependent on
    files and I/O operations anymore, and therefore will not throw 
    eg. IOExceptions when read. */
public class FileHandler {
    /** This logger will be used for logging the I/O activities. */
    private Logger logger;
    /** This class has its own logger. */
    public static String loggerName = "fi.hu.cs.titokone.filehandler";

    /** This class sets up a new FileHandler and sets up its Logger. */
    public FileHandler() {}

    /** This function is for when it is not clear whether the file would
	contain a source or binary file. It returns an object which is 
	either a Binary, or failing that, the more general Source. */
    public Object loadSourceOrBinary(File filename) throws IOException {}
    
    /** This function loads up a Source file from a given file.
	@param filename The identifier of the file to read from.
	@return A source instance which is no longer dependent on I/O. 
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
    public Source loadSource(File filename) throws IOException {}

    // (No saveSource is needed.)

    /** This function loads a settings file into a StringBuffer.
	@param filename The identifier of the file to read from.
	@return A StringBuffer which no longer depends on I/O.
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
    public StringBuffer loadSettings(File filename) throws IOException {}

    /** This method saves settings data from a StringBuffer to a file.
	The line separator will be 
	System.getProperty("line.separator", "\n").
	@param settingsData The settings data in a StringBuffer in the 
	form it is to be saved in. The linebreaks in the file will be \ns.
	@param filename The identifier of the file to save to.
	@throws IOException If an I/O error occurs, eg. the directory 
	the file should be in does not exist or we cannot write to it. */
    public void saveSettings(StringBuffer settingsData, File filename) 
	throws IOException {}

    /** This function loads a Binary from a binary .b91 file and
	returns the result. The Binary class checks itself upon creation
	and throws a ParseException if it is not syntactically correct. 
	@param filename Identifier of the file to read from.
	@return An Binary instance containing the contents of the
	.b91 file. 
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. 
	@throws ParseException If the file does not contain a valid 
	binary. */
    public Binary loadBinary(File filename) throws IOException, 
						   ParseException {}
   
    /** This method saves a Binary to file in a .b91 binary format.
	@param binary The binary to save to file. 
	@param filename The identifier for the file to save to. 
	@throws IOException If an I/O error occurs, eg. the given file 
	cannot be written to. */
    public void saveBinary(Binary binary, File filename)
	throws IOException {}

    /** This method loads a "stdin" file representing the disk into 
	a string. The contents should be integers delimited by \n, 
	\r or \r\n, but the loader does not check that this is the 
	case.
	@param filename The identifier for the file to read from.
	@return A stringbuffer containing the contents of the file.
	@throws IOException If an I/O error occurs, eg. the given
	file is not found. */
    public StringBuffer loadStdIn(File filename) throws IOException {}

    /** This method saves a "stdout" file representing the disk. 
	The contents to be saved to the file should be integers
	delimited by \n, \r or \r\n, but no checking is made.
	@param contents The string to save to the file.
	@param filename The file to save the given string to.
	@throws IOException If an I/O error occurs, eg. the given
	file cannot be written to. */
    public void saveStdOut(String contents, String filename) 
	throws IOException {}

    /** This method attempts to load a resource bundle from a file
	(with an URLClassLoader). It bundles up the various exceptions
	possibly created by this into ResourceLoadFailedException.
	@param filename The filename to load and instantiate the 
	ResourceBundle from. 
	@return A ResourceBundle found from the file.
	@throws ResourceLoadFailedException If the file load would cast
	an IOException, or the class loading would cast a 
	ClassNotFoundException or the instantiation would cast a
	InstantiationException or the cast a ClassCastException. */
    public ResourceBundle loadResourceBundle(File filename) 
	throws ResourceLoadFailedException {}

    /*
      How to find a config file:
      - include the file bar.txt in the jar/directory structure in 
      subdirectory foo
      - get eg. this class's ClassLoader, ask it for getResourceAsStream().
        - the classloader knows to look into subdirectories
        - the "name" for getResourceAsStream might be bar.txt or 
	  eg. fi/helsinki/cs/koski/foo/bar.txt.
    */
}
