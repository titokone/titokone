/** This class represents the processor state. It can be told to run for one
    command cycle at a time. */
public class Processor implements CPU {
    // has a pointer to memory and registers.

    // Implementations of CPU:

    public int getValueOf(int registerID) {  }

    public int getStatus() {  } // running status: still running, halted
}
