/** This control class Controls Everything(TM). */
public class Control implements TTK91Bridge {
    
 
    /** This has control to all the files this program has opened.
    */
    private FileHandler fileHandler; 
    
    
    private Compiler cmpler;
    
    
    
    /** Compiles a symbolic TTK91-assembly language to binary executable
        application.
        @param source The source code to be compiled.
        @return The binary executable code.
    */
    public TTK91Application compile(CompileSource source);
 
 
    
    /** Runs a given app for <steps> steps at a time and updates its state.
        Problem: the CPU and Memory states might need to somehow be attached
        to this thing, unless there is some sort of a 'resume running current'
        method added.
        @param app Application to be run.
        @param steps Number of steps the application will be run.
    */
    public void run(TTK91Application app, int steps);
 
 
 
    /** Returns a reference to the RandomAccessMemory object which is
        attached to the current Processor object.
        @return The reference to the RandomAccessMemory object.
    */
    public Memory getMemory();
 
    
    
    /** Returns a reference to the Processor object.
        @return The reference to the Processor object.
    */
    public CPU getCPU();
    
    
    
    /** Changes the size of memory measured in words.
        @param powerOfTwo Implies the total size of memory which is 2^powerOfTwo.
                          Legitimate values are 9,...,16.
    */
    public void changeMemorySize(int powerOfTwo);
    
    
    
    /** Erases the memory ie. fills it with zeros.
    */
    public void eraseMemory();
    
    
    
    /** This compiles one next line of the t91 program that has been opened recently and
        hasn't yet been compiled to binary code.
        @return Returns CompileInfo object of the last line compiled.
    */
    public CompileInfo compileLine() throws CompileTimeException;
    
    
    
    /** This runs one next line of the program that is currently loaded into the TTK91's
        memory. 
        @Return Returns RunInfo object of the last line executed.
    */
    public RunInfo runLine() throws RunningTimeException;
    
    
    
    /** This is called when user the has changed language to another one by
        choosing it from a dialog after selecting Set language from the
        GUI menu.
        @param language The language in which the messages will be shown.
    */
    public void setLanguage(String language);
    
    
    
    /** This is called when the user has changed the file from which stdin
        operations will be read in emulator.
        @param stdinFile The stdin file.
    */
    public void setSTDIN(File stdinFile);
    
    
    
    /** This is called when the user has changed the file from which stdout
        operations will be written in emulator.
        @param stdoutFile The stdout file..
    */
    public void setSTDOUT(File stdoutFile);
    
    
    
    /** This is called when the processor wants to write to an
        external device (CRT or STDOUT).
        @param id The identifier of the device.
                  CRT = CRT
                  STDOUT  = hard drive
        @param input The input to given device.
        
    */
    public void writeToDevice(int id, String input);
    
    
    
    /** This is called when the processor wants to read from
        an external device.(keyboard or stdin)
        @param id The identifier of the device
                  KBD = keyboard
                  STDIN  = hard drive
        @return The string representation of the output from the wanted device.
                Lines are separated by '\n'...
    */
    public String readFromDevice(int id);
    
    
    
    
    /** This is called when a b91-file is opened from GUI.
        This method just passes the file to FileHandler.
        @param b91File The binary b91-file.
    */
    public void openBinaryFile(File b91File);
    
    
    
    /** This is called when a k91-file is opened from GUI.
        This method just passes the file to FileHandler.
        @param kt91File The symbolic sourcecode file (k91).
    */
    public void openSourceFile(File k91File);
     
   

    /** 
        @return 
    */
    public LoadInfo Load();



    /** 
        @return
    */
    public Settings LoadSettings();



    /** 
        @param 
    */
    public void ();



    /** 
        @param 
    */
    public void ();



    /** 
        @param 
    */
    public void ();     
   
    
}
