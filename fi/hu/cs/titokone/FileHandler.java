package fi.hu.cs.titokone;

import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import fi.hu.cs.ttk91.TTK91CompileSource;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

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
  
  testAccess(srcFile, READ_ACCESS);
  Source src = new Source(loadFileContentsToString(srcFile));
  return src;
}

    // (No saveSource is needed.)

    /** This function loads a settings file into a StringBuffer.
	@param filename The identifier of the file to read from.
	@return A StringBuffer which no longer depends on I/O.
	@throws IOException If an I/O error occurs. Eg. one of the possible
	IOExceptions is FileNotFoundException. */
public StringBuffer loadSettings(File settingsFile) throws IOException {
  
  testAccess(settingsFile, READ_ACCESS);
  StringBuffer srcbuf = new StringBuffer(loadFileContentsToString(settingsFile));
  return srcbuf;
}



/* This function is a private assistant method for FileHandler and
   it tests if given file can be accessed for reading, writing or 
   appending operations. It throws IOException in a negative case.
   It also throws FileNotFoundException if the file is not found.
*/
private boolean testAccess(File accessedFile, int accessType) throws IOException {

  if(accessedFile.exists() == false) {
    
    if (accessType == WRITE_ACCESS || accessType == APPEND_ACCESS) {
      accessedFile.createNewFile();
    }
    else {
      String msg = accessedFile.getName() + " not found.";
      //throw new FileNotFoundException(new Message(msg).toString());
      throw new FileNotFoundException();
    }
  }
  
  if(accessType == READ_ACCESS && accessedFile.canRead() == false) {
    String msg = "No read access to " + accessedFile.getName() + ".";
    throw new IOException(new Message(msg).toString());
  }
  else if(accessType == WRITE_ACCESS && accessedFile.canWrite() == false) {
    String msg = "No write access to " + accessedFile.getName() + ".";
    throw new IOException(new Message(msg).toString());
  }
  else if(accessType == APPEND_ACCESS && accessedFile.canWrite() == false) {
    String msg = "No write access to " + accessedFile.getName() + ".";
    throw new IOException(new Message(msg).toString());
  }
  
  return true;
  
}


/* This function is a private assistant method for FileHandler and
   it loads the contents of the given file into a string and returns
   that string. It may throw an IOException in case of a read error.
*/
private String loadFileContentsToString(File loadFile) throws IOException {

  /* !!!!  This is BAD CODE! length() returns long and it's casted   !!!!
     !!!!  to int here, so in case of a big file this code is buggy  !!!!
     TODO: cleanup.
  */
  int loadFileLength = (int)loadFile.length(); 
  
  BufferedReader loadFileContents = new BufferedReader(new FileReader(loadFile));
  char[] loadFileCharTable = new char[loadFileLength];
  //int nextByte;
  
  try {
    loadFileContents.read(loadFileCharTable, 0, loadFileLength);
  }
  catch (IOException e) {
    //throw IOException(new Message("Error while reading the file " + loadFile.getName() + ".").toString());
  }
  
  return new String(loadFileCharTable);
}


private void saveStringToFile(String str, File saveFile) throws IOException {
  
  BufferedWriter saveFileWriter = new BufferedWriter(new FileWriter(saveFile));
  try {
    saveFileWriter.write(str, 0, str.length());
  }
  catch (IOException e) {
    //throw IOException(new Message("Error while writing into file " + saveFile.getName() + ".").toString());
    //TODO: Perkele kun ei saa toimimaan tota IOException(String)-h‰ss‰kk‰‰
    throw new IOException();
  }
  saveFileWriter.close();
}




    /** This method saves settings data from a StringBuffer to a file.
	The line separator will be 
	System.getProperty("line.separator", "\n").
	@param settingsData The settings data in a StringBuffer in the 
	form it is to be saved in. The linebreaks in the file will be \ns.
	@param filename The identifier of the file to save to.
	@throws IOException If an I/O error occurs, eg. the directory 
	the file should be in does not exist or we cannot write to it. */
public void saveSettings(String settingsData, File settingsFile) throws IOException {
  
  testAccess(settingsFile, WRITE_ACCESS);
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
public Binary loadBinary(File binaryFile) throws IOException, ParseException {
  
  testAccess(binaryFile, READ_ACCESS);
  String str = loadFileContentsToString(binaryFile);
  Binary bin = new Binary(str);
  return bin;
}
  
   
    /** This method saves a Binary to file in a .b91 binary format.
	@param binary The binary to save to file. 
	@param filename The identifier for the file to save to. 
	@throws IOException If an I/O error occurs, eg. the given file 
	cannot be written to. */
public void saveBinary(Binary bin, File binarySaveFile) throws IOException {
  
  testAccess(binarySaveFile, WRITE_ACCESS);
  String str = bin.toString();
  saveStringToFile(str, binarySaveFile);
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
  
  testAccess(stdinFile, READ_ACCESS);
  String str = loadFileContentsToString(stdinFile);
  return new StringBuffer(str);
}

    /** This method saves a "stdout" file representing the disk. 
	The contents to be saved to the file should be integers
	delimited by \n, \r or \r\n, but no checking is made.
	@param contents The string to save to the file.
	@param filename The file to save the given string to.
	@throws IOException If an I/O error occurs, eg. the given
	file cannot be written to. */
public void saveStdOut(String contents, File stdoutFile) throws IOException {
  
  testAccess(stdoutFile, WRITE_ACCESS);
  saveStringToFile(contents, stdoutFile);
}

public void appendDataToStdOut(String dataItem, File stdoutFile) throws IOException {
  testAccess(stdoutFile, APPEND_ACCESS);
  testAccess(stdoutFile, READ_ACCESS);
  String newFileContents = loadFileContentsToString(stdoutFile) + "\n" + dataItem;
  saveStringToFile(newFileContents, stdoutFile);
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
public ResourceBundle loadResourceBundle(File rbFile) throws ResourceLoadFailedException {
  
  Class theClass;
  Object translations;
  
  logger = Logger.getLogger(this.getClass().getPackage().toString());
     
  String className = rbFile.getName();
  // Poistetaan .class. Oletan ett‰ se on jotain hassua Windowsissa ja voi
  // olla isolla tai pienell‰, joten otan yleens‰ ottaen tiedostop‰‰tteen 
  // pois. Jos tied‰t paremman tavan, korvaa ihmeess‰. T‰m‰ on purkkaa.
  className = className.substring(0, className.lastIndexOf("."));
  
  URL[] url = new URL[1];
  
  try {
    url[0] = rbFile.toURL();
  }
  catch (java.net.MalformedURLException e) {
    logger.fine(new Message("MalformedURLException in loadResourceBundle()").toString());
    throw new ResourceLoadFailedException("MalformedURLException in loadResourceBundle()");
  }
  
  URLClassLoader loader = new URLClassLoader(url); // voi heitt‰‰ poikkeuksen.
  
  try {
    theClass = loader.loadClass(className); // Voi heitt‰‰ poikkeuksen.
  }
  catch (java.lang.ClassNotFoundException e) {
    logger.fine(new Message("ClassNotFoundException in loadResourceBundle()").toString());
    throw new ResourceLoadFailedException("ClassNotFoundException in loadResourceBundle()");
  }
  
  try {
    translations = theClass.newInstance(); // Voi heitt‰‰ poikkeuksen.
  }
  catch (java.lang.InstantiationException e) {
    logger.fine(new Message("InstantiationException in loadResourceBundle()").toString());
    throw new ResourceLoadFailedException("InstantiationException in loadResourceBundle()");
  }
  catch (IllegalAccessException e) {
    logger.fine(new Message("IllegalAccessException in loadResourceBundle()").toString());
    throw new ResourceLoadFailedException("IllegalAccessException in loadResourceBundle()");
  }
  
  try {
    return (ResourceBundle)translations; // Voi heitt‰‰ ClassCastExceptionin.
  }
  catch (ClassCastException e) {
    logger.fine(new Message("ClassCastExceptionin in loadResourceBundle()").toString());
    throw new ResourceLoadFailedException("ClassCastExceptionin in loadResourceBundle()");
  }

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
private String changeExtensionStr(String filename, String newExtension)
{
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


    /** This method checks whether the given file can be a) written
        to, b) appended to, or c) read.
        @param accessedFile The file to check.
        @param accessMode The permission to check against.
        @return True if actually going through with the operation
        should go fine and not throw an IOException. (This also
        means testing whether the file exists if it should be read.) */
public boolean checkAccess(File accessedFile, int accessMode) {
  
  switch (accessMode) {
    case READ_ACCESS:
      return accessedFile.canRead();
    case WRITE_ACCESS:
    case APPEND_ACCESS:
      return accessedFile.canWrite();
  }
  return false;
}

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
