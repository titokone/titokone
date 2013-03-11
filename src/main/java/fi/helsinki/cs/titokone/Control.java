// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.*;

import java.io.*;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Control class offers the extenal interface to titokone. Using the
 * methods of this class one can compile and emulate the execution
 * process of a ttk91-program from a text file or straight from a
 * string object, which contain symbolic ttk91 machine code, A
 * complete debugging and trackability of compilation, loading and
 * execution cycles are provided with CompileInfo, LoadInfo and
 * RunInfo objects, which can be used to get information of what had
 * just happened.
 * <p/>
 * This doesn't take a position on how to show the output. That's
 * left for the GUI, actually a piece of code between Control
 * and the GUI, which prepares the output provided here to be shown
 * in GUI. In this software that piece of code is the GUIBrain class.
 */
public class Control implements TTK91Core {
    /**
     * This is the memory size that will be used by default, unless
     * a higher class (GUIBrain) chooses to change the size later.
     * The size is expressed as "the power of two which will give the
     * wanted memory size"; here 15 means that the actual memory will be
     * 2^15 = 32768 words long.
     */
    public static final int DEFAULT_MEMORY_SIZE = 15;

    /**
     * This field set directs handling of an array containing file
     * definitions set in the application. See getApplicationDefinitions().
     */
    public static final int DEF_STDIN_POS = 0;
    public static final int DEF_STDOUT_POS = 1;
    public static final int DEF_HOME_POS = 2;

    /**
     * This has control to all the files this program has opened.
     */
    private FileHandler fileHandler;

    private Processor processor;

    private Compiler compiler;

    private Application application;

    private File defaultStdInFile, defaultStdOutFile, currentStdOutFile,
            sourceFile;

    private LoadInfo pendingLoadInfo = null;
    private Display display; //reference to display so we can give it to processor for access

    /**
     * This constructor sets up the Control instance.
     */
    public Control(File defaultStdInFile, File defaultStdOutFile, Display d) {
        Logger logger = Logger.getLogger(getClass().getPackage().getName());

        if (defaultStdInFile == null) {
            logger.info(new Message("No default STDIN file set.").toString());
        }
        if (defaultStdOutFile == null) {
            logger.info(new Message("No default STDOUT file set.").toString());
        }
        this.display = d;
        fileHandler = new FileHandler();
        compiler = new Compiler();
        // Create a new processor with a memory size of 2^9.
        changeMemorySize(DEFAULT_MEMORY_SIZE);
        this.defaultStdInFile = defaultStdInFile;
        this.defaultStdOutFile = defaultStdOutFile;
        currentStdOutFile = defaultStdOutFile;
    }

    /**
     * Compiles a symbolic TTK91-assembly language to binary executable
     * application. Defined by TTK91Core. It throws exceptions if the
     * compilation (eg. Compiler) throws them.
     *
     * @param source The source code to be compiled.
     * @return The binary executable code.
     * @throws TTK91CompileException If the compiler throws one.
     * @throws TTK91Exception        Never in this implementation. (Except for
     *                               TTK91CompileExceptions, which inherit it.)
     */
    public TTK91Application compile(TTK91CompileSource source)
            throws TTK91Exception, TTK91CompileException {
        compiler.compile(source.getSource());
        // Compile lines, basically ignoring the output, until no more
        // CompileInfos are returned.
        while (compileLine() != null) {
            ; // All is done in compileLine().
        }
        application = compiler.getApplication();
        return application;
    }

    /**
     * This method loads an application that has been either compiled
     * or read from binary earlier. It calls Loader's
     * setApplicationToLoad and then loadApplication methods. It also
     * sets up the STDIN contents from the default file to the Application.
     * If this fails, a ParseException is thrown just before the method
     * should return. It can be ignored until it leads to a
     * TTK91NoStdInData exception when a program tries to read from STDIN.
     *
     * @throws TTK91OutOfMemory      If the memory cannot fit the
     *                               application.
     * @throws TTK91NoStdInData      If the current STDIN file contains invalid
     *                               STDIN input or the file cannot be opened.
     * @throws IllegalStateException If application is null.
     */
    public LoadInfo load()
            throws TTK91AddressOutOfBounds, TTK91NoStdInData {
        String errorMessage = "";
        boolean pendingException = false;
        File[] appDefinitions;
        LoadInfo result;
        pendingLoadInfo = null;
        if (application == null) {
            errorMessage = new Message("No application to load.").toString();
            throw new IllegalStateException(errorMessage);
        }
        // Fetch STDIN/STDOUT filenames from Application's symboltable.
        appDefinitions = getApplicationDefinitions();
        if (appDefinitions[DEF_STDOUT_POS] != null) {
            currentStdOutFile = appDefinitions[DEF_STDOUT_POS];
        }
        try {
            insertStdinToApplication(appDefinitions[DEF_STDIN_POS]);
        } catch (IOException fileProblem) {
            errorMessage = new Message("STDIN data file " +
                    "unreadable: {0}",
                    fileProblem.getMessage()).toString();
            // The exception will be thrown when everything else is 
            // finished; it can be ignored since it will be thrown later
            // again *if* someone tries to read the stdin data.
            pendingException = true;
        } catch (ParseException dataInvalid) {
            errorMessage = new Message("STDIN data file contains " +
                    "invalid data: {0}",
                    dataInvalid.getMessage()).toString();
            // The exception will be thrown when everything else is 
            // finished; it can be ignored since it will be thrown later
            // again *if* someone tries to read the stdin data.
            pendingException = true;
        }
        Loader loader = new Loader(processor);
        loader.setApplicationToLoad(application);
        result = loader.loadApplication();
        if (pendingException) {
            pendingLoadInfo = result;
            throw new TTK91NoStdInData(errorMessage);
        }
        return result;
    }

    /**
     * Sometimes load() throws an exception when the situation is not
     * really requiring an error message. This causes the caller to
     * not recieve their LoadInfo. To avoid causing unnecessary
     * suffering, the LoadInfo can be fetched by calling this
     * method. The LoadInfo will be null if the loading was really
     * not successful or if there has been no load.
     */
    public LoadInfo getPendingLoadInfo() {
        return pendingLoadInfo;
    }

    /**
     * This method does the actual inserting STDIN datat to an application.
     *
     * @param applicationStdin The application's suggestion for a file
     *                         to read the stdin data from. If null, the data is read from the
     *                         set default stdin file.
     * @throws IOException    If the current STDIN file cannot be opened.
     * @throws ParseException If the current STDIN file contains
     *                        invalid input.
     */
    private void insertStdinToApplication(File applicationStdin)
            throws ParseException, IOException {
        String contents;
        File stdinFile = defaultStdInFile;
        Logger logger;

        if (applicationStdin != null) {
            stdinFile = applicationStdin;
        }
        // If the stdin file is not set by default (in Control's constructor)
        // and not by the application, we assume it should not be used.
        if (stdinFile == null) {
            logger = Logger.getLogger(getClass().getPackage().getName());
            logger.info(new Message("STDIN files were null, data not inserted " +
                    "to the application.").toString());
            return;
        }
        contents = fileHandler.loadStdIn(stdinFile).toString();
        // We use a brutal way of testing the input in order to get 
        // more detailed information on *where* the input fails. 
        // Otherwise, should use Application's static boolean checkInput().
        try {
            application.setStdIn(contents);
        } catch (IllegalArgumentException syntaxErrorInStdinData) {
            throw new ParseException(syntaxErrorInStdinData.getMessage(), -1);
        }
    }

    /**
     * This helper method gets the definitions in the currently loaded
     * application. It assumes the definitions are file paths.
     *
     * @return A File-array containing STDIN, STDOUT and HOME definitions
     *         or nulls if the values have not been defined in the application.
     *         The positions of the values in the array are determined by the
     *         constants DEF_STDIN_POS, DEF_STDOUT_POS and DEF_HOME_POS
     *         respectively.
     */
    public File[] getApplicationDefinitions() {
        SymbolTable symbols;
        String[] definitions;
        File[] result = new File[3];
        Logger logger;

        symbols = application.getSymbolTable();
        definitions = symbols.getAllDefinitions();
        for (int i = 0; i < definitions.length; i++) {
            if (definitions[i].equalsIgnoreCase("STDIN")) {
                result[DEF_STDIN_POS] =
                        new File(symbols.getDefinition(definitions[i]));
            } else if (definitions[i].equalsIgnoreCase("STDOUT")) {
                result[DEF_STDOUT_POS] =
                        new File(symbols.getDefinition(definitions[i]));
            } else if (definitions[i].equalsIgnoreCase("HOME")) {
                result[DEF_HOME_POS] =
                        new File(symbols.getDefinition(definitions[i]));
            } else {
                logger = Logger.getLogger(getClass().getPackage().getName());
                logger.warning(new Message("Application contained an odd " +
                        "definition key '{0}'.",
                        definitions[i]).toString());
            }
        }
        return result;
    }

    /**
     * Runs a given app for <steps> steps at a time and updates its state.
     * Problem: the CPU and Memory states might need to somehow be attached
     * to this thing, unless there is some sort of a 'resume running current'
     * method added. Defined by TTK91Core.
     *
     * @param app   Application to be run.
     * @param steps Number of steps the application will be run.
     * @throws TTK91NoStdInData      If the STDIN data file is unreadable or
     *                               syntactically incorrect even if the file is not really needed.
     * @throws TTK91ExecutionOverrun if program overruns the limit set in steps
     */
    public void run(TTK91Application app, int steps)
            throws TTK91Exception, TTK91RuntimeException {
        String errorMessage = "";
        RunInfo info = null;
        int counter;
        boolean pendingException = false, gotException = false;
        try {
            this.application = (Application) app;
        } catch (ClassCastException unsupportedTypeOfApplication) {
            errorMessage = new Message("Trying to run an unsupported " +
                    "type of application. (The " +
                    "application must be created " +
                    "using the same program.)").toString();
            throw new IllegalArgumentException(errorMessage);
        }
        try {
            load();
        } catch (TTK91NoStdInData stdinDataWillNotBeAvailable) {
            // Ignore it; we'll just see how it goes to run it.
            // (On the other hand, TTK91AddressOutOfBounds thrown by 
            // load() will be thrown upwards from here.)
        }
        // Run the program a line at a time until the counter becomes
        // equal to the step count. (The counter is incremented before
        // comparing to the steps variable.) If the steps was 0, run
        // infinitely. 
        if (steps < 0) {
            return;
        }
        counter = 1;
        // We then always run at least one step; 0 steps would mean "run 
        // infinitely".
        do {
            try {
                info = runLine(); // See runLine(). It does various things.
            } catch (TTK91FailedWrite err) {
                // If the write fails, it fails after all else, so we just
                // inform of it happening afterwards.
                errorMessage = err.getMessage();
                pendingException = true;
                gotException = true;
            }
        }
        while ((info != null || gotException) &&
                (steps == 0 || ++counter <= steps));

        // Added by Kohahdus project 2006-11-23
        if ((steps != 0) && (counter > steps)) {
            throw new TTK91ExecutionOverrun("Program execution killed after "
                    + steps + " instructions. Likely an "
                    + "infinite loop");
        }


        if (pendingException) {
            throw new TTK91FailedWrite(errorMessage);
        }
    }

    /**
     * Returns a reference to the RandomAccessMemory object which is
     * attached to the current Processor object. Defined by TTK91Core.
     *
     * @return The reference to the RandomAccessMemory object.
     */
    public TTK91Memory getMemory() {
        return processor.getMemory();
    }

    public TTK91Memory getPhysicalMemory() {
        return processor.getPhysicalMemory();
    }

    /**
     * Returns a reference to the Processor object. Defined by TTK91Core.
     *
     * @return The reference to the Processor object.
     */
    public TTK91Cpu getCpu() {
        return processor;
    }

    /**
     * Returns a string that contains the binary presentation of the
     * application. Defined by TTK91Core.
     *
     * @param app The application that is to be transformed into
     *            binary.
     * @return Returns the application in the form a string, which
     *         contains the binary code.
     */
    public String getBinary(TTK91Application app) {
        String errorMessage;
        try {
            return new Binary((Application) app).toString();
        } catch (ClassCastException invalidApplicationFormat) {
            errorMessage = new Message("Cannot form a binary out of an " +
                    "unsupported type of an application. " +
                    "(The application must be created " +
                    "using the same program.)").toString();
            throw new IllegalArgumentException(errorMessage);
        }
    }


    /**
     * Returns a TTK91Application object of the given string, which
     * should contain proper binary code. If it doesn't, however, a
     * ParseException is thrown.  Defined by TTK91Core.
     *
     * @param binary The binary to be compiled.
     * @return Returns a TTK91Application object of the binary string.
     */
    public TTK91Application loadBinary(String binary)
            throws ParseException {
        // (Binary's methods may throw the ParseException.)
        return new Binary(binary).toApplication();
    }

    /**
     * Changes the size of memory measured in words.
     *
     * @param powerOfTwo Implies the total size of memory which is
     *                   2^powerOfTwo.  Legitimate values are
     *                   9,...,16.
     * @throws IllegalArgumentException if powerOfTwo is not between
     *                                  9 and 16.
     */
    public void changeMemorySize(int powerOfTwo) {
        String errorMessage;
        if (powerOfTwo < 9 || powerOfTwo > 16) {
            // Translate the error message and store in the same variable.
            errorMessage = new Message("Memory size must be between 2^9 and " +
                    "2^16, a change to 2^{0} failed.",
                    "" + powerOfTwo).toString();
            throw new IllegalArgumentException(errorMessage);
        }
        processor = new Processor((int) Math.pow(2, powerOfTwo));
        processor.setExternalDevice(display);
    }

    /**
     * Erases the memory ie. fills it with zeros.
     */
    public void eraseMemory() {
        processor.eraseMemory();
    }

    /**
     * This compiles one next line of the t91 program that has been
     * opened recently and hasn't yet been compiled to binary code.
     *
     * @return Returns CompileInfo object of the last line compiled.
     */
    public CompileInfo compileLine()
            throws TTK91CompileException {
        CompileInfo info = compiler.compileLine();
        // If the compilation is finished, let's store the application.
        if (info == null) {
            application = compiler.getApplication();
        }
        return info;
    }

    /**
     * This runs one next line of the program that is currently
     * loaded into the TTK91's memory.
     *
     * @return Returns RunInfo object of the last line executed.
     */
    public RunInfo runLine()
            throws TTK91RuntimeException {
        RunInfo info;
        int data;
        int[] outData;
        String errorMessage;

        if (application == null) {
            errorMessage = new Message("There is no application available " +
                    "to run from!").toString();
            throw new IllegalStateException(errorMessage);
        }
        try {
            info = processor.runLine();
            if (info != null) {
                // OutData becomes an array where slot 0 is the device 
                // number and slot 1 the value written there.
                outData = info.whatOUT();
                if (info.isExternalOp() && outData != null) {
                    if (outData[0] == Processor.CRT) {
                        application.writeToCrt(outData[1]);
                    }
                    if (outData[0] == Processor.STDOUT) {
                        application.writeToStdOut(outData[1]);
                        writeToStdoutFile("" + outData[1]);
                    }
                }
            }
            return info;
        } catch (TTK91NoKbdData needMoreData) {
            // Application may throw a new TTK91NoKbdData exception,
            // in which case it will just be thrown upwards.
            data = application.readNextFromKbd();
            processor.keyboardInput(data);
            return runLine(); // Try again with the CPU buffer filled.
        } catch (TTK91NoStdInData needMoreData) {
            // Application may throw a new TTK91NoStdinData exception,
            // in which case it will just be thrown upwards.
            data = application.readNextFromStdIn();
            processor.stdinInput(data);
            return runLine(); // Try again with the CPU buffer filled.
        }
    }

    /**
     * This method appends the given data to the current stdout file.
     *
     * @param data The data to append to the stdout file.
     * @throws TTK91FailedWrite If there was an I/O error.
     */
    void writeToStdoutFile(String data)
            throws TTK91FailedWrite {
        Logger logger = Logger.getLogger(getClass().getPackage().getName());
        // The default stdout file is set at the constructor and may
        // be null. If the application does not set it either, we 
        // interpret it as the stdout file being disabled.
        if (currentStdOutFile == null) {
            logger.info(new Message("No STDOUT file set, not writing to " +
                    "it.").toString());
            return;
        }
        try {
            fileHandler.appendDataToStdOut(data, currentStdOutFile);
        } catch (IOException writeFailed) {
            throw new TTK91FailedWrite(writeFailed.getMessage());
        }
    }

    /**
     * This is called when user the has changed language to another one by
     * choosing it from a dialog after selecting Set language from the
     * GUI menu, and has chosen a special file to load.
     *
     * @param languageFile The language file to load into a
     *                     ResourceBundle.
     * @return The ResourceBundle represented by the file.
     * @throws ResourceLoadFailedException If opening the resourcebundle
     *                                     from the file failed.
     */
    public ResourceBundle loadLanguageFile(File languageFile)
            throws ResourceLoadFailedException {
        return fileHandler.loadResourceBundle(languageFile);
    }

    /**
     * This is called when the user has changed the file from which the stdin
     * operations will be read.
     *
     * @param stdinFile The stdin file.
     * @throws IOException    If an I/O error occurs.
     * @throws ParseException *After* everything else is done, if the
     *                        contents of the file are invalid. This exception can be ignored
     *                        until it leads into TTK91NoStdInData when an application tries
     *                        to read from STDIN.
     */
    public void setDefaultStdIn(File stdinFile)
            throws IOException,
            ParseException {
        String contents;
        defaultStdInFile = stdinFile;
        contents = fileHandler.loadStdIn(stdinFile).toString();
        if (!Application.checkInput(contents)) {
            throw new ParseException(new Message("StdIn file contents are " +
                    "invalid; the file should " +
                    "contain only integers and " +
                    "separators.").toString(),
                    -1);
        }
    }

    /**
     * This is called when the user has changed the file into which the stdout
     * operations will be written.
     *
     * @param stdoutFile The stdout file.
     */
    public void setDefaultStdOut(File stdoutFile)
            throws IOException {
    	
        defaultStdOutFile = stdoutFile;
        // Check whether we can really write to the file without writing
        // to it. (We just append, really.) TestAccess throws exceptions.
        fileHandler.testAccess(stdoutFile, FileHandler.APPEND_ACCESS);
    }

    /**
     * This is called when a source file is opened from GUI.
     * This method passes the file to FileHandler and prepares to
     * compile the contents.
     *
     * @param openedFile The file.
     * @return The source string.
     */
    public String openSource(File openedFile)
            throws IOException {
        String sourceString = fileHandler.loadSource(openedFile).getSource();
        compiler.compile(sourceString); // Prepare the compiler.
        sourceFile = openedFile;
        application = null;
        currentStdOutFile = defaultStdOutFile;
        return sourceString;
    }

    /**
     * This method makes it possible to re-open the same source file,
     * only modified. The opened file stays the same, the new string is
     * passed to the compiler and the modifications are saved in
     * the source file.
     *
     * @param modifiedSource The modified source as a string array, one
     *                       line per cell. The format is optimized for GUI/GUIBrain use. The
     *                       source must not be null.
     * @return The source string.
     * @throws IOException           Just before the last return if the saving of
     *                               the modified source failed.
     * @throws IllegalStateException If there is no source file set.
     *                               The method openSource() must be called before this method.
     */
    public String modifySource(String[] modifiedSource)
            throws IOException {
        StringBuffer result = new StringBuffer();
        String sourceString;
        File targetFile = sourceFile;
        if (modifiedSource == null) {
            throw new IllegalArgumentException(new Message("Modified source " +
                    "was null.").toString());
        }
        if (sourceFile == null) {
            throw new IllegalStateException(new Message("No source file set, " +
                    "use openSource " +
                    "first.").toString());
        }
        for (int i = 0; i < modifiedSource.length; i++) {
            result.append(modifiedSource[i] + "\n");
        }
        sourceString = result.toString();
        compiler.compile(sourceString); // Prepare the compiler.
        application = null;
        currentStdOutFile = defaultStdOutFile;
        fileHandler.saveSource(new Source(sourceString), targetFile);
        return sourceString;
    }

    /**
     * This method saves the opened source file to a binary of the
     * same filename as the previous source file was loaded from,
     * with the extension changed (probably from .k91) to .b91.
     * If you wish to select the filename yourself, use saveBinary(File).
     *
     * @throws IllegalStateException If the method is called before
     *                               a source file has been opened or if there is no application
     *                               to save.
     */
    public void saveBinary()
            throws IOException {
        String errorMessage;
        if (sourceFile == null) {
            errorMessage = new Message("Cannot deduce the file to " +
                    "store the binary into; no source " +
                    "file has been loaded.").toString();
            throw new IllegalStateException(errorMessage);
        }
        saveBinary(fileHandler.changeExtension(sourceFile, "b91"));
    }

    /**
     * This method saves the current application to a given file. It
     * throws any IOExceptions upwards. Often saveBinary() with no
     * parameters is sufficient; it guesses the file to save to from
     * the source filename.
     *
     * @param specialFile A filename to save the binary to.
     * @throws IllegalStateException If there is no application to save.
     */
    public void saveBinary(File specialFile)
            throws IOException {
        String errorMessage;
        if (application == null) {
            errorMessage = new Message("Cannot save binary to file; no " +
                    "application has been compiled or " +
                    "loaded.").toString();
            throw new IllegalStateException(errorMessage);
        }
        fileHandler.saveBinary(new Binary(application), specialFile);
    }

    /**
     * This is called when a binary file is opened from GUI.
     * This method passes the file to FileHandler, has the contents
     * interpreted by Binary and prepares to load the result Application.
     *
     * @param openedFile The file.
     */
    public void openBinary(File openedFile)
            throws IOException, ParseException {
        sourceFile = null;
        application = fileHandler.loadBinary(openedFile).toApplication();
    }

    /**
     * This is called when GUIBrain wants to load the settings.
     * It uses FileHandler for reading them.
     *
     * @return A string representation of the Settings object stored in
     *         the file.
     */
    public String loadSettingsFileContents(File settingsFile)
            throws IOException {
        return fileHandler.loadSettings(settingsFile).toString();
    }

    /**
     * This is called when GUIBrain wants to load the settings from
     * a stream. It uses FileHandler for reading them.
     *
     * @return A string representation of the Settings object stored in
     *         the input stream, or null if the stream was null.
     */
    public String loadSettingsStreamContents(InputStream settingsStream)
            throws IOException {
        if (settingsStream == null) {
            return null;
        } else {
            return fileHandler.loadSettings(settingsStream).toString();
        }
    }

    /**
     * This method stores the settings string to the given file.
     * It passes the parameters to FileHandler.
     *
     * @param currentSettings A string representation of the current
     *                        settings.
     * @param settingsFile    The file to store the settings to.
     * @throws IOException if FileHandler throws one to indicate an I/O
     *                     error.
     */
    public void saveSettings(String currentSettings, File settingsFile)
            throws IOException {
        fileHandler.saveSettings(currentSettings,
                settingsFile);
    }

    /**
     * GUIBrain calls this when it has received the TTK91NoKbdData
     * exception. The input is passed on to the processor. Note that
     * primarily input is searched for in the Application instance.
     *
     * @param inputValue The input to pass on.
     */
    public void keyboardInput(int inputValue) {
        processor.keyboardInput(inputValue);
    }
}
