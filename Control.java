/** Control class offers the extenal interface to titokone. Using the methods of this class
    one can compile and emulate an exection process of a ttk91-program from a text file
    or straight from a string object, which contain symbolic ttk91 machine code, A complete
    debugging and trackability of compilation, loading and execution cycles are provided with 
    CompileInfo, LoadInfo and RunInfo objects, which can be used to get information of what
    had just happened.
    
    This doesn't take a position on how to show the output, that's left for the GUI, maybe even for
    a piece of code between Control and the GUI, which prepares the output provided here to be shown
    in GUI. In this software that piece of code is the GUIBrain class.
*/

import java.text.ParseException;

public class Control implements TTK91Core {
    
 
    /** This has control to all the files this program has opened.
    */
    private FileHandler fileHandler; 
    
    
    
    private Processor processor;
    
    
    
    private Compiler cmplr;
    
    
    
    /** This constructor sets up the Control instance.
    */
    public Control() { }
    
    
    
    /** Compiles a symbolic TTK91-assembly language to binary executable
        application.
        @param source The source code to be compiled.
        @return The binary executable code.
    */
    public TTK91Application compile(CompileSource source) throws TTK91Exception, TTK91CompileException { }
 
 
    
    /** Runs a given app for <steps> steps at a time and updates its state.
        Problem: the CPU and Memory states might need to somehow be attached
        to this thing, unless there is some sort of a 'resume running current'
        method added.
        @param app Application to be run.
        @param steps Number of steps the application will be run.
    */
    public void run(TTK91Application app, int steps) throws TTK91Exception, TTK91RuntimeException { }
 
 
 
    /** Returns a reference to the RandomAccessMemory object which is
        attached to the current Processor object.
        @return The reference to the RandomAccessMemory object.
    */
    public TTK91Memory getMemory() { }
 
    
    
    /** Returns a reference to the Processor object.
        @return The reference to the Processor object.
    */
    public TTK91Cpu getCpu() { }
    
    
    
    /** Returns a string that contains the binary presentation of the application.
        @param app The application that is to be transformed into binary.
        @return Returns the application in the form a string, which contains the binary code.
    */
    public String getBinary(TTK91Application app) { }
    
    
    
    /** Returns a TTK91Application object of the given string, which should contain
        proper binary code. If it doesn't, however, a ParseException is thrown.
        @param binary The binary to be compiled.
        @return Returns a TTK91Application object of the binary string.
    */
    public TTK91Application loadBinary(String binary) throws ParseException { }
    
    
    /** Changes the size of memory measured in words.
        @param powerOfTwo Implies the total size of memory which is 2^powerOfTwo.
                          Legitimate values are 9,...,16.
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
        @Return Returns RunInfo object of the last line executed.
    */
    public RunInfo runLine() throws TTK91RuntimeException { }
    
    
    
    /** This loads a line of code into titokone's memory.
        @return Returns the LoadInfo object of the operation.
    */
    public LoadInfo loadLine() throws TTK91LoadtimeException { }



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
    public void setSTDIN(File stdinFile) { }
    
    
    
    /** This is called when the user has changed the file into which the stdout
        operations will be written.
        @param stdoutFile The stdout file..
    */
    public void setSTDOUT(File stdoutFile) { }
    
    
    
    /** This is called when a file is opened from GUI.
        This method just passes the file to FileHandler.
        @param openedFile The file.
    */
    public void openFile(File openedFile) { }
    
    
    
    /** This is called when GUIBrain want's to know the current settings.
        @return The Settings object, which includes all the current settings.
    */
    public Settings getSettings() { }
    
    
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
