/** This control class Controls Everything(TM). */
public class ControlBridge implements TTK91Bridge {
    

    // TTK91Bridge implementations:
    public TTK91Application compile(CompileSource source) { }

    // (runs a given app for <steps> steps at a time and updates its state.)
    // Problem: the CPU and Memory states might need to somehow be attached
    // to this thing, unless there is some sort of a 'resume running current'
    // method added.
    public void run(TTK91Application app, int steps) { }

    public Memory getMemory() {  }

    public CPU getCPU() { }
}
