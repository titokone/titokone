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
    public Source loadSource(String filename) throws IOException { }

    // (No saveSource is needed.)

    /** This function loads a settings file into a StringBuffer.
	@param filename The full or relative path to the file to read from. 
	@return A StringBuffer which no longer depends on I/O.
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
    public StringBuffer loadSettings(String filename) throws IOException { }

    /** This method saves settings data from a StringBuffer to a file.
	The line separator will be 
	System.getProperty("line.separator", "\n").
	@param settingsData The settings data in a StringBuffer in the 
	form it is to be saved in. The linebreaks in the file will be \ns.
	@param filename The full or relative path to the file to save to. 
	@throws IOException If an I/O error occurs, eg. the directory 
	the file should be in does not exist or we cannot write to it. */
    public void saveSettings(StringBuffer settingsData, String filename) 
	throws IOException { }

    /** This function loads an Application from a binary .b91 file and
	returns the result. The Application's source will consist of
	integers only, even though integers + symbolic code is supported
	by the class as well. It calls a private method parseApplication() 
	to do the actual interpretation work.
	@param filename The full or relative path to the file to read the 
	application from. 
	@return An Application instance containing the data gotten from the
	application file. 
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. 
	@throws InvalidBinaryException If the binary file is not syntactically
	correct. */
    public Application loadApplication(String filename) 
	throws IOException, InvalidBinaryException { }
   
    /** This method saves an Application to file in a .b91 binary format 
	which can be read by loadApplication. It calls a private method
	transformApplication() to take care of translating the application
	to a StringBuffer. The standard binary format is 
	described in more detail in the project's design documentation. The
	line delimiter will be System.getProperty("line.separator", "\n").
	An example program in .b91 format might look like this (comments 
	in parentheses are not a part of the file format):<br>
	<pre>
        ___b91___
        ___code___
        0 9          (numbers of the first and the last line of code,
        52428801      the latter also being the initial value of FP)
	18874378
	572522503
	36175883
	287834122
	18874379
	538968064
	36175883
	69206016
	1891631115
	___data___
	10 11         (numbers the first and the last line of the data 
	0              area, the latter also being the initial value of SP;
	0              then follow the contents of the data area in order)
	___symboltable___
	luku 10        (only local symbols are included, eg. HALT is 
	summa 11        considered built-in)
	___end___
	</pre>
	@param binary The application to save to file. (The TTK91Application 
	interface is insufficient for the saving, since it does not reveal 
	the application's symbol table.)
	@param filename The full or relative path of the file to save the 
	binary to. */
    public void saveApplication(Application binary, String filename)
	throws IOException {}

    /** This method parses a binary in text form given to it and creates
	an instance of Application from it. 
	@param binaryCode A StringBuffer containing the text contained in
	the binary. 
	@return An Application constructed from the parsed code. 
	@throws InvalidBinaryException If the binary string is not 
	syntactically correct. */
    private Application parseApplication(StringBuffer binaryCode) 
	throws InvalidBinaryException { }

    /** This method transforms an instance of an Application class to 
	a StringBuffer containing the code from it, in the standard .b91
	format described in saveApplication(). 
	@param binary The application to transform to text form.
	@return A StringBuffer containing the binary in the aforementioned
	format. The line delimiter will be 
	System.getProperty("line.separator", "\n"). */
    private StringBuffer transformApplication(Application binary) { }
}
