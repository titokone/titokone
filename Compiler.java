/** This class knows everything about the relation between symbolic
    code and binary code. It can transform a full source to binary or
    one symbolic command to binary or vice versa. */
public class Compiler {
    // Problem: translating back to symbolic from binary will lose 
    // any symbol data, eg. LOAD R1, X compiled to binary and back will 
    // become LOAD R1, 100 if X=100. The effects of this can be avoided
    // in the GUI by getting the symbol data directly from source. 
    
    /** This function transforms a symbolic source code into an 
	application class. 
	@param source The symbolic source code to be compiled.
	@return The compiled application. */
    public Application compile(CompileSource source) { }

    // - get binary from symbolic command
    // - get symbolic command from binary
}
