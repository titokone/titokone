/** This control class Controls Everything(TM). */
public class ControlBridge implements TTK91Bridge {
    
 
    /** Compiles a symbolic TTK91-assembly language to binary executable
        application.
        @param source The source code to be compiled.
        @return The binary executable code.
    */
    public TTK91Application compile(CompileSource source) { }
 
 
    
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
    
    
    
    /** Sets the running mode.
        @param mode This is the running mode to be set. It can be
                    0 = pauseless execution
                    1 = line by line without comments
                    2 = line by line with comments
    */
    public void setRunningMode(int mode);
    
    
    
    /** This function transmits a message from debugger (etc) to GUI which
        shows it to the user. Note that ControlBrigde is only a transmitter
        here.
        @param msg The message to be transmitted.
        @param id This is used identify the type of message. For example
                  1 = Explanation of latest operation.
                  2 = Debug information
                  3 = Error message
                  This information can be used by GUI to decide on how
                  to show the message.
    */
    public void sendMessageToGUI(String msg, int id);
}
