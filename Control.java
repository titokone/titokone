/** This control class Controls Everything(TM). */
public class ControlBridge implements TTK91Bridge {
    
 
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
        @return Returns true if the operation was successful. Otherwise false.
    */
    public boolean changeMemorySize(int powerOfTwo);
    
    
    
    /** Erases the memory ie. fills it with zeros.
    */
    public void eraseMemory();
    
    
    
    /** This is called when user has selected a file to be opened
        in FileDialog after selecting Open File from the menu.
        @param fileName The path of the file.
    */
    public void openFile(String filePath);
    
    
    
    /** This is called when user has selected Compile from the GUI menu.
        This compiles the t91 program that has been opened recently and
        hasn't yet been compiled to binary code.
        @return Returns true if the operation was successful. Otherwise
                it returns false (for example if there was no program
                loaded into memory).
    */
    public boolean compile() throws CompileTimeException;
    
    
    
    /** This is called when user has selected Run from the GUI menu.
        This runs the program that is currently loaded into the TTK91's
        memory. 
        @Return Returns true if the operation was successful. Otherwise
                it returns false (for example if there was no program
                loaded into memory)
    */
    public void run() throws RunningTimeException;
    
    
    
    /** This is called when user the has changed language to another one by
        choosing it from a dialog after selecting Set language from the
        GUI menu.
        @param language The language in which the messages will be shown.
    */
    public void setLanguage(String language);
    
    
    
    /** This is called when the user has changed the file from which stdin
        operations will be read in emulator.
        @param filePath The path of the file.
    */
    public void selectSTDIN(String filePath);
    
    
    
    /** This is called when the user has changed the file from which stdout
        operations will be written in emulator.
        @param filePath The path of the file.
    */
    public void selectSTDOUT(String filePath);
    
    
    
    /** This is called when the processor wants to write to an
        external device (CRT or STDOUT).
        @param id The identifier of the device.
                  CRT = CRT
                  STDOUT  = hard drive
        
    */
    public void writeToDevice(int id);
    
    /** This is called when the processor wants to read from
        an external device.(keyboard or stdin)
        @param id The identifier of the device
                  KBD = keyboard
                  STDIN  = hard drive
    */
    public void readFromDevice(int id);
    
    
    
    
    /** 
        @param 
    */
    public void ();
     
   
    
}
