package fi.hu.cs.titokone;

/** Control class offers the extenal interface to titokone. Using the
    methods of this class one can compile and emulate the execution
    process of a ttk91-program from a text file or straight from a
    string object, which contain symbolic ttk91 machine code, A
    complete debugging and trackability of compilation, loading and
    execution cycles are provided with CompileInfo, LoadInfo and
    RunInfo objects, which can be used to get information of what had
    just happened.
    
    This doesn't take a position on how to show the output. That's
    left for the GUI, actually a piece of code between Control
    and the GUI, which prepares the output provided here to be shown
    in GUI. In this software that piece of code is the GUIBrain class.
*/

import java.text.ParseException;
import java.io.File;
import java.io.IOException;
import fi.hu.cs.ttk91.*;

public class Control implements TTK91Core {
    public static final int DEFAULT_MEMORY_SIZE = 512;
 
    /** This has control to all the files this program has opened.
    */
    private FileHandler fileHandler; 
    
    private Processor processor;
    
    private Compiler compiler;

    private TTK91Application application;

    /** This constructor sets up the Control instance.
    */
    public Control() { 
      fileHandler = new FileHandler();
      compiler = new Compiler();
      processor = new Processor(DEFAULT_MEMORY_SIZE);
      // TODO: Tarvitseeko tänne jotain muuta?
    }
    
    /** Compiles a symbolic TTK91-assembly language to binary executable
        application. Defined by TTK91Core.
        @param source The source code to be compiled.
        @return The binary executable code.
    */
    public TTK91Application compile(TTK91Source source) 
      throws TTK91Exception, TTK91CompileException { 
      compiler.compile(source.getSource());
      // Compile lines, basically ignoring the output, until no more
      // CompileInfos are returned.
      while(compileLine() != null)
	; // All is done in compileLine().
      // TODO: Check that returned application is not null?
      application = compiler.getApplication(); 
      return application;
    }
 
    /** This method loads an application that has been either compiled 
        or read from binary earlier. It calls Loader's 
        setApplicationToLoad and then loadApplication methods.
        @throws TTK91OutOfMemory If the memory cannot fit the 
        application. */
    public void load() throws TTK91OutOfMemory {
      Loader loader = new Loader();
      loader.setApplicationToLoad(application);
      loader.loadApplication();
    }    

    /** Runs a given app for <steps> steps at a time and updates its state.
        Problem: the CPU and Memory states might need to somehow be attached
        to this thing, unless there is some sort of a 'resume running current'
        method added. Defined by TTK91Core.
        @param app Application to be run.
        @param steps Number of steps the application will be run.
    */
    public void run(TTK91Application app, int steps) 
      throws TTK91Exception, TTK91RuntimeException { 
      Application myApp;
      String errorMessage;
      try {
	myApp = (Application) TTK91Application;
      }
      catch(ClassCastException unsupportedTypeOfApplication) {
	errorMessage = new Message(
				   throw new IllegalArgumentException(...)); 
	// TODO: jatka.
						       
      }
    }
 
 
 
    /** Returns a reference to the RandomAccessMemory object which is
        attached to the current Processor object. Defined by TTK91Core.
        @return The reference to the RandomAccessMemory object.
    */
    public TTK91Memory getMemory() { }
 
    
    
    /** Returns a reference to the Processor object. Defined by TTK91Core.
        @return The reference to the Processor object.
    */
    public TTK91Cpu getCpu() { }
    
    
    
    /** Returns a string that contains the binary presentation of the 
        application. Defined by TTK91Core.
        @param app The application that is to be transformed into
        binary.
        @return Returns the application in the form a string, which
        contains the binary code.
    */
    public String getBinary(TTK91Application app) { }
    
    
    
    /** Returns a TTK91Application object of the given string, which
        should contain proper binary code. If it doesn't, however, a
        ParseException is thrown.  Defined by TTK91Core.
        @param binary The binary to be compiled. 
        @return Returns a TTK91Application object of the binary string.
    */
    public TTK91Application loadBinary(String binary) throws ParseException { }
    
    
    /** Changes the size of memory measured in words.
        @param powerOfTwo Implies the total size of memory which is
                          2^powerOfTwo.  Legitimate values are
                          9,...,16.
        @throws IllegalArgumentException if powerOfTwo is not between
        9 and 16.
    */
    public void changeMemorySize(int powerOfTwo) { }
    

    
    /** Erases the memory ie. fills it with zeros.
    */
    public void eraseMemory() { }
    
    
    
    /** This compiles one next line of the t91 program that has been opened recently and
        hasn't yet been compiled to binary code.
        @return Returns CompileInfo object of the last line compiled.
    */
    public CompileInfo compileLine() throws TTK91CompileException { }
    
    
    
    /** This runs one next line of the program that is currently loaded into the TTK91's
        memory. 
        @return Returns RunInfo object of the last line executed.
    */
    public RunInfo runLine() throws TTK91RuntimeException { }
    
    
    
    /** This is called when user the has changed language to another one by
        choosing it from a dialog after selecting Set language from the
        GUI menu.
        @param language The language in which the messages will be shown.
    */
    public void setLanguage(String language) { }
    
    
    
    /** This is called when the user has changed the file from which the stdin
        operations will be read.
        @param stdinFile The stdin file.
    */
    public void setDefaultStdIn(File stdinFile) { }
    
    
    
    /** This is called when the user has changed the file into which the stdout
        operations will be written.
        @param stdoutFile The stdout file..
    */
    public void setDefaultStdOut(File stdoutFile) { }
    
    
    
    /** This is called when a source file is opened from GUI.
        This method passes the file to FileHandler and prepares to 
        compile the contents.
        @param openedFile The file.
    */
    public void openSource(File openedFile) throws IOException { }

    /** This is called when a binary file is opened from GUI.
        This method passes the file to FileHandler, has the contents
        interpreted by Binary and prepares to load the result Application.
        @param openedFile The file.
    */
    public void openBinary(File openedFile) throws IOException { }
    
    
    
    /** This is called when GUIBrain wants to load the settings.
        @return A string representation of the Settings object stored in 
        the file.
    */
    public String loadSettingsFileContents(File settingsFile) { }
    
    /** This method stores the settings string to the given file. 
        It passes the parameters to FileHandler.
        @param currentSettings A string representation of the current 
        settings. 
        @param settingsFile The file to store the settings to.
        @throws IOException if FileHandler throws one to indicate an I/O 
        error. */
    public void saveSettings(String currentSettings, File settingsFile) {}  
  

    // (Metodi lisanappaimistodatan lukemiseen lisatty 15.3. --Sini)    
    /** GUIBrain calls this when it has recieved the TTK91NoKbdData
        exception. The input is passed on to the processor. Note that 
        primarily input is searched for in the Application instance.
        @param inputValue The input to pass on. */
    public void keyboardInput(int inputValue) {}

    // Control ei tarvitse naita metodeja, koska tiedot kasitellaan
    // joka tapauksessa GUIBrainissa vasta. --Sini 15.3.
    //public void setRunningOptions(boolean isCommentedExecution, 
    //                              boolean isPausedExecution, 
    //                              boolean isAnimatedExecution, 
    //                              int     speed) {}

    

    //public void setCompilingOptions(boolean isCommentedExecution, 
    //                                boolean isPausedExecutionm,
    //                                int     speed) {}


    /** RunLine() calls this, when the processor wants to write a value to CRT.
        @param inputValue The input to CRT.  
    */
    private void writeToCRT(int inputValue) { }
    
    
    
    /** RunLine() calls this, when the processor wants to write a value to StdOut.
        @param inputValue The inpuit to StdOut.
    */
    private void writeToStdOut(int inputValue) { }
    
    
}
