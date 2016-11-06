// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class transforms files into various sorts of buffer classes
 * depending on who needs them, and saves these buffer classes to
 * files when needed. The buffer classes will not be dependent on
 * files and I/O operations anymore, and therefore will not throw
 * eg. IOExceptions when read.
 */
public class FileHandler {
    /**
     * This class has its own logger.
     */
    public static String loggerName = "fi.helsinki.cs.titokone.filehandler";

    /**
     * Read only access to file
     */
    public static final int READ_ACCESS = 1;

    /**
     * Append access to file
     */
    public static final int APPEND_ACCESS = 2;

    /**
     * Write access to file
     */
    public static final int WRITE_ACCESS = 3;

    /**
     * This class sets up a new FileHandler and sets up its Logger.
     */
    public FileHandler() {
    }

    /**
     * This function loads up a Source file from a given file.
     *
     * @param srcFile The identifier of the file to read from.
     * @return A source instance which is no longer dependent on I/O.
     * @throws IOException If an I/O error occurs. Eg. one of the possible
     *                     IOExceptions is FileNotFoundException.
     */
    public Source loadSource(File srcFile) throws IOException {
        return new Source(loadFileContentsToString(srcFile).toString());
    }

    /**
     * This method is used to save a source that has been modified.
     *
     * @param src     The source object to save to file.
     * @param srcFile The file to save the source to.
     * @throws IOException If an I/O error occurds.
     */
    public void saveSource(Source src, File srcFile) throws IOException {
        saveStringToFile(src.getSource(), srcFile);
    }

    /**
     * This function loads a settings file into a StringBuffer.
     *
     * @param settingsFile The identifier of the file to read from.
     * @return A StringBuffer which no longer depends on I/O.
     * @throws IOException If an I/O error occurs. Eg. one of the possible
     *                     IOExceptions is FileNotFoundException.
     */
    public StringBuffer loadSettings(File settingsFile) throws IOException {
        return loadFileContentsToString(settingsFile);
    }

    /**
     * This function loads a settings input stream to a StringBuffer.
     *
     * @param settingsStream An input stream to read the contents
     *                       from.
     * @return A StringBuffer containing the stream's contents,
     *         linebreaks unmodified, or null if the settingsStream was null.
     * @throws IOException If an I/O error occurs while reading or
     *                     closing the stream.
     */
    public StringBuffer loadSettings(InputStream settingsStream)
    		throws IOException {
    	
        if (settingsStream == null) {
            return null;
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(settingsStream));
        return loadReaderContentsToString(reader);
    }

    /**
     * This method saves settings data from a StringBuffer to a file.
     * The line separator will be
     * System.getProperty("line.separator", "\n").
     *
     * @param settingsData The settings data in a StringBuffer in the
     *                     form it is to be saved in. The linebreaks in the file will be \ns.
     * @param settingsFile The identifier of the file to save to.
     * @throws IOException If an I/O error occurs, eg. the directory
     *                     the file should be in does not exist or we cannot write to it.
     */
    public void saveSettings(String settingsData, File settingsFile)
            throws IOException {
        saveStringToFile(settingsData, settingsFile);
    }

    /**
     * This function loads a Binary from a binary .b91 file and
     * returns the result. The Binary class checks itself upon creation
     * and throws a ParseException if it is not syntactically correct.
     *
     * @param binaryFile Identifier of the file to read from.
     * @return An Binary instance containing the contents of the
     *         .b91 file.
     * @throws IOException    If an I/O error occurs. Eg. one of the possible
     *                        IOExceptions is FileNotFoundException.
     * @throws ParseException If the file does not contain a valid
     *                        binary.
     */
    public Binary loadBinary(File binaryFile)
            throws IOException, ParseException {
        return new Binary(loadFileContentsToString(binaryFile).toString());
    }


    /**
     * This method saves a Binary to file in a .b91 binary format.
     *
     * @param bin            The binary to save to file.
     * @param binarySaveFile The identifier for the file to save to.
     * @throws IOException If an I/O error occurs, eg. the given file
     *                     cannot be written to.
     */
    public void saveBinary(Binary bin, File binarySaveFile)
            throws IOException {
        saveStringToFile(bin.toString(), binarySaveFile);
    }

    /**
     * This method loads a "stdin" file representing the disk into
     * a string. The contents should be integers delimited by \n,
     * \r or \r\n, but the loader does not check that this is the
     * case.
     *
     * @param stdinFile The identifier for the file to read from.
     * @return A stringbuffer containing the contents of the file.
     * @throws IOException If an I/O error occurs, eg. the given
     *                     file is not found.
     */
    public StringBuffer loadStdIn(File stdinFile) throws IOException {
        return loadFileContentsToString(stdinFile);
    }

    /**
     * This method appends data to a stdout file. If the file does
     * not exist, it is created.
     *
     * @param dataItem   The data to append to the file (a newline is
     *                   added automagically).
     * @param stdoutFile The file to append to.
     * @throws IOException If an I/O error occurs.
     */
    public void appendDataToStdOut(String dataItem, File stdoutFile)
            throws IOException {
    	
        if(!stdoutFile.exists())
        	throw new IOException();
        	
        // Open the file in append mode.
        BufferedWriter writer = new BufferedWriter(new FileWriter(stdoutFile,
                true));

        writer.write(dataItem, 0, dataItem.length());
        writer.newLine(); // Add newlines to end instead of beginning.
        writer.flush();
        writer.close();
    }

    /**
     * This method attempts to load a resource bundle from a file
     * (with an URLClassLoader). It bundles up the various exceptions
     * possibly created by this into ResourceLoadFailedException.
     *
     * @param rbFile The filename to load and instantiate the
     *               ResourceBundle from.
     * @return A ResourceBundle found from the file.
     * @throws ResourceLoadFailedException If the file load would cast
     *                                     an IOException, or the class loading would cast a
     *                                     ClassNotFoundException or the instantiation would cast a
     *                                     InstantiationException or the cast a ClassCastException.
     */
    public ResourceBundle loadResourceBundle(File rbFile)
            throws ResourceLoadFailedException {

        Class<?> theClass;
        Object translations;
        String className, errorMessage;
        String errorParams[] = new String[2];
        Logger logger = Logger.getLogger(getClass().getPackage().getName());
        URL[] url = new URL[1];

        // Remove .class from the file. Note that package names will not be added.
        // We can't determine them sensibly from the file name.
        className = changeExtension(rbFile, "").getName();

        try {
            url[0] = rbFile.getParentFile().toURI().toURL(); // MalformedURLException, anyone?
            URLClassLoader loader = new URLClassLoader(url); // SecurityExcp..
            theClass = loader.loadClass(className); // ClassNotFoundExcp..?
            // InstantiationException or IllegalAccessException, anyone?
            translations = theClass.newInstance();
            return (ResourceBundle) translations;  // ClassCastException?
        } catch (Exception originalException) {
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

    /**
     * This function is a private assistant method for FileHandler and
     * it loads the contents of the given file into a string and returns
     * that string. It may throw an IOException in case of a read error.
     */
    private StringBuffer loadFileContentsToString(File loadFile)
            throws IOException {
        BufferedReader loadFileContents =
                new BufferedReader(new FileReader(loadFile));

        return loadReaderContentsToString(loadFileContents);
    }

    /**
     * This function is a private assistant method, which loads the
     * contents of a given reader into a string and returns that string.
     * The lines will be read using .readLine() and recombined with
     * \ns.
     *
     * @throws IOException If an I/O error occurs while reading the file.
     */
    private StringBuffer loadReaderContentsToString(BufferedReader reader)
            throws IOException {
        StringBuffer result;
        String line = "";

        result = new StringBuffer("");
        // readLine() returns null when the end of the stream has been
        // reached.
        while (line != null) {
            result.append(line);
            line = reader.readLine();
            if (line != null) {
                line += "\n"; // (Result-str is internally used.)
            }
        }
        reader.close();
        return result;
    }

    /**
     * This method is a private helper method which handles saving strings
     * to files.
     */
    private void saveStringToFile(String str, File saveFile)
            throws IOException {
        if (!saveFile.exists()) {
            saveFile.createNewFile();
        }

        BufferedWriter saveFileWriter =
                new BufferedWriter(new FileWriter(saveFile));
        saveFileWriter.write(str, 0, str.length());
        saveFileWriter.flush();
        saveFileWriter.close();
    }

    /**
     * This method changes the extension of the given filename to
     * newExtension and returns the new filename as a File object.
     * File extensions are considered to be the part after the last period
     * in the File.getName(). If there are no periods in that part,
     * the file is considered to be without an extension and newExtension
     * is added.
     */
    public File changeExtension(File f, String newExtension) {
        String filenamestr;
        File parent;
        filenamestr = f.getName();
        parent = f.getParentFile();

        return new File(parent, changeExtensionStr(filenamestr,
                newExtension));
    }

    /**
     * This method returns the first string modified so that the part of
     * it following the last period is removed, including the period,
     * and the result is this modified followed by newExtension. If
     * newExtension is not an empty string, the two are separated with a
     * ".".
     */
    private String changeExtensionStr(String filename, String newExtension) {
        String result;
        int lastPeriod;
        lastPeriod = filename.lastIndexOf(".");
        if (lastPeriod == -1) {
            lastPeriod = filename.length();
        }
        result = filename.substring(0, lastPeriod);
        if (!newExtension.equals("")) {
            result += "." + newExtension;
        }
        return result;
    }

    /* This function tests if a given file can be accessed for
       reading, writing or appending operations. It throws a home-made
       IOException accordingly. 
       @throws FileNotFoundException If a file checked for read 
       access does not exist. 
       @throws IOException (Excluding the above.) If a file does not 
       allow the sort of access which is asked for. */
    public void testAccess(File accessedFile, int accessType)
            throws IOException {
        String msg;
        boolean couldWrite = true;
        if (accessedFile.exists() == false) {
            if (accessType == READ_ACCESS) {
                throw new FileNotFoundException(accessedFile.getName());
            } else {
                try {
                    // Check if the file can be created. This may throw
                    // a FileNotFoundException.
                    accessedFile.createNewFile();
                    couldWrite = accessedFile.canWrite();
                    accessedFile.delete();
                } catch (IOException errorOccurred) {
                    msg = new Message("No write access to {0}; " +
                            "file cannot be created.",
                            accessedFile.getName()).toString();
                    throw new IOException(msg);
                }
                if (!couldWrite) {
                    msg = new Message("No write access to {0}.",
                            accessedFile.getName()).toString();
                    throw new IOException(msg);
                }
                return; // Write access and file can be created, assume ok.
            }
        }
        if (accessType == READ_ACCESS && accessedFile.canRead() == false) {
            msg = new Message("No read access to {0}.",
                    accessedFile.getName()).toString();
            throw new IOException(msg);
        } else if ((accessType == WRITE_ACCESS || accessType == APPEND_ACCESS) &&
                accessedFile.canWrite() == false) {
            msg = new Message("No write access to {0}.",
                    accessedFile.getName()).toString();
            throw new IOException(msg);
        }
    }
}





