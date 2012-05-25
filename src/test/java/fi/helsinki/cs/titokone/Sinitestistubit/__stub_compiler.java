package fi.helsinki.cs.titokone.Sinitestistubit;

import fi.helsinki.cs.titokone.*;
import fi.helsinki.cs.titokone.Compiler;
import fi.helsinki.cs.ttk91.*;
import java.util.logging.Logger;

public class __stub_compiler extends Compiler {
    int counter = 0;

    // OVERRIDES -----------------------------------------------------

    public CompileInfo compileLine() throws TTK91CompileException {
	short foo = 1;
	counter++;
	Logger logger = Logger.getLogger("fi.helsinki.cs.titokone");
	if(counter <= 3) 
	    return new CompileInfo(foo, 1, "Foo bar");
	else 
	    return null;
	
    }

    public Application getApplication() {
	MemoryLine[] code = new MemoryLine[1], data = new MemoryLine[2];
	SymbolTable symbols = new SymbolTable();
	code[0] = new MemoryLine(3, "Foo bar baz");
	data[0] = new MemoryLine(4, "");
	data[1] = new MemoryLine(5, "jau");
	symbols.addSymbol("foo", 8);
	//symbols.addDefinition("stdin", "filename");
	return new Application(code, data, symbols);
    }


    // DEBUG AIDES ----------------------------------------------------

    public int compileLineCallCount() { return counter; }

}
