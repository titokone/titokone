package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.TTK91CompileSource;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.net.URL;
import java.net.URLClassLoader;

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

    /** Read only access to file */
    public static final int READ_ACCESS = 1;

    /** Append access to file */
    public static final int APPEND_ACCESS = 2;

    /** Write access to file */
    public static final int WRITE_ACCESS = 3;
    
    /** This class sets up a new FileHandler and sets up its Logger. */
    public FileHandler() {}

    /** This function loads up a Source file from a given file.
	@param filename The identifier of the file to read from.
	@return A source instance which is no longer dependent on I/O. 
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
    public Source loadSource(File srcFile) throws IOException {
	return new Source(loadFileContentsToString(srcFile).toString());
    }

    // (No saveSource is needed.)

    /** This function loads a settings file into a StringBuffer.
	@param filename The identifier of the file to read from.
	@return A StringBuffer which no longer depends on I/O.
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
    public StringBuffer loadSettings(File settingsFile) throws IOException {
	return loadFileContentsToString(settingsFile);
    }

    /** This method saves settings data from a StringBuffer to a file.
	The line separator will be 
	System.getProperty("line.separator", "\n").
	@param settingsData The settings data in a StringBuffer in the 
	form it is to be saved in. The linebreaks in the file will be \ns.
	@param filename The identifier of the file to save to.
	@throws IOException If an I/O error occurs, eg. the directory 
	the file should be in does not exist or we cannot write to it. */
    public void saveSettings(String settingsData, File settingsFile) 
	throws IOException {
	saveStringToFile(settingsData, settingsFile);
    }

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
    public Binary loadBinary(File binaryFile) 
	throws IOException, ParseException {
	return new Binary(loadFileContentsToString(binaryFile).toString());
    }
  
   
    /** This method saves a Binary to file in a .b91 binary format.
	@param binary The binary to save to file. 
	@param filename The identifier for the file to save to. 
	@throws IOException If an I/O error occurs, eg. the given file 
	cannot be written to. */
    public void saveBinary(Binary bin, File binarySaveFile) 
	throws IOException {
	saveStringToFile(bin.toString(), binarySaveFile);
    }

    /** This method loads a "stdin" file representing the disk into 
	a string. The contents should be integers delimited by \n, 
	\r or \r\n, but the loader does not check that this is the 
	case.
	@param filename The identifier for the file to read from.
	@return A stringbuffer containing the contents of the file.
	@throws IOException If an I/O error occurs, eg. the given
	file is not found. */
    public StringBuffer loadStdIn(File stdinFile) throws IOException {
	return loadFileContentsToString(stdinFile);
    }

    /** This method saves a "stdout" file representing the disk. 
	The contents to be saved to the file should be integers
	delimited by \n, \r or \r\n, but no checking is made.
	@param contents The string to save to the file.
	@param filename The file to save the given string to.
	@throws IOException If an I/O error occurs, eg. the given
	file cannot be written to. 
	@deprecated This method is not used in practice, as appending the
	data one line at a time has been found to be more convenient. */
    //public void saveStdOut(String contents, File stdoutFile) 
    //	throws IOException {
    //	saveStringToFile(contents, stdoutFile);
    //}

    /** This method appends data to a stdout file. If the file does
	not exist, it is created. 
	@param dataItem The data to append to the file (a newline is
	added automagically). 
	@param stdoutFile The file to append to. 
	@throws IOException If an I/O error occurs. */
    public void appendDataToStdOut(String dataItem, File stdoutFile) 
	throws IOException {
	boolean fileExisted = stdoutFile.exists();
	// Open the file in append mode.
	BufferedWriter writer =  new BufferedWriter(new FileWriter(stdoutFile, 
								   true));
	if(fileExisted)
	    writer.newLine();
	writer.write(dataItem, 0, dataItem.length());
	writer.flush();
	writer.close();
    }
  
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
    public ResourceBundle loadResourceBundle(File rbFile) 
	throws ResourceLoadFailedException {
  
	Class theClass;
	Object translations;
	String className, errorMessage;
	String errorParams[] = new String[2];
	Logger logger = Logger.getLogger(getClass().getPackage().getName());
	URL[] url = new URL[1];

	// Poistetaan .class tiedostosta. 
	className = changeExtension(rbFile, "").getName();
 
	try {
	    url[0] = rbFile.toURL(); // MalformedURLException, anyone?
	    URLClassLoader loader = new URLClassLoader(url); // SecurityExcp..
	    theClass = loader.loadClass(className); // ClassNotFoundExcp..?
	    // InstantiationException or IllegalAccessException, anyone?
	    translations = theClass.newInstance(); 
	    return (ResourceBundle) translations;  // ClassCastException?
	}
	catch(Exception originalException) {
	    // Throw an exception with a message like "<exception name> in
	    // loadResourceBundle(): <original exception message>".
	    errorParams[0] = originalException.getClass().getName();
	    errorParams[1] = originalException.getMessage();
	    errorMessage = new Message("{0} in loadResourceBundle(): {1}",
				       errorParams).toString();
	    logger.fine(errorMessage);
	    throw new ResourceLoadFailedException(errorMessage);
	}
    }

    /* This function is a private assistant method for FileHandler and
       it loads the contents of the given file into a string and returns
       that string. It may throw an IOException in case of a read error.
    */
    private StringBuffer loadFileContentsToString(File loadFile) 
	throws IOException {

	//long loadFileLength = loadFile.length(); 
	BufferedReader loadFileContents = 
	    new BufferedReader(new FileReader(loadFile));
	StringBuffer result;
	String line = "";
	
	result = new StringBuffer("");
	// readLine() returns null when the end of the stream has been
	// reached.
	while(line != null) {
	    result.append(line);
	    line = loadFileContents.readLine();
	}
	loadFileContents.close();
	return result;
    }

    /** This method is a private helper method which handles saving strings
	to files. */
    private void saveStringToFile(String str, File saveFile) 
	throws IOException {
	if(!saveFile.exists())
	    saveFile.createNewFile();
  
	BufferedWriter saveFileWriter = 
	    new BufferedWriter(new FileWriter(saveFile));
	saveFileWriter.write(str, 0, str.length());
	saveFileWriter.flush();
	saveFileWriter.close();
    }

    /** This method changes the extension of the given filename to  
	newExtension and returns the new filename as a File object.
	File extensions are considered to be the part after the last period
	in the File.getName(). If there are no periods in that part,
	the file is considered to be without an extension and newExtension
	is added. */
    public File changeExtension(File f, String newExtension) {
	String filenamestr;
	File parent;
	filenamestr = f.getName();
	parent = f.getParentFile();
	
	return new File(parent, changeExtensionStr(filenamestr, 
						   newExtension));
    }

    /** This method returns the first string modified so that the part of 
	it following the last period is removed, including the period,
	and the result is this modified followed by newExtension. If
	newExtension is not an empty string, the two are separated with a
	".". */
    private String changeExtensionStr(String filename, String newExtension) {
	String result;
	int lastPeriod;
	lastPeriod = filename.lastIndexOf(".");
	if(lastPeriod == -1)
	    lastPeriod = filename.length();
	result = filename.substring(0, lastPeriod);
	if(!newExtension.equals(""))
	    result += "." + newExtension;
	return result;
    }

    /* This function tests if a given file can be accessed for
       reading, writing or appending operations. It throws a home-made
       IOException accordingly. 
       @throws FileNotFoundException If a file checked for read access 
       does not exist.
       @throws IOException (Excluding the above.) If a file does not 
       allow the sort of access which is asked for. */
    public void testAccess(File accessedFile, int accessType) 
	throws IOException {
	if(accessedFile.exists() == false && accessType == READ_ACCESS) {
	    throw new FileNotFoundException(accessedFile.getName());
	}
	if(accessType == READ_ACCESS && accessedFile.canRead() == false) {
	    String msg = new Message("No read access to {0}.", 
				     accessedFile.getName()).toString();
	    throw new IOException(msg);
	}
	else if((accessType == WRITE_ACCESS || accessType == APPEND_ACCESS) && 
		accessedFile.canWrite() == false) {
	    String msg = new Message("No write access to {0}.",
				     accessedFile.getName()).toString();
	    throw new IOException(msg);
	}
    }
}
