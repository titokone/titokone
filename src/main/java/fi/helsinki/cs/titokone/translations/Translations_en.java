// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

// NOTE: This file is merely a copy of Translations.java. Do your 
// modifications there and, if you like, copy the file over here (and 
// change the public class Translations .. to Translations_en). We added this
// file here as a hack to get around the VM not checking for a default
// translation file. This is fixed in Java VM's SE build 1.4.2-b28, but
// not in the current b06 build used in CSL. The file is not needed for
// anything else than being able to change languages back, and as long
// as the translations equal the base messages, as they do, this file
// could contain just about anything.

package fi.helsinki.cs.titokone.translations;

import java.util.ListResourceBundle;

public class Translations_en extends ListResourceBundle {
    public Object[][] getContents() {
        // To be simultaneously compatible with the general ListResourceBundle
        // functionality and provide a base template array for other
        // translations, we do not repeat every line in the emptyContents
        // array, but instead arrange the duplication when asked for
        // the real contents. (Only necessary the first time.)
        if (contents == null) {
            contents = new String[emptyContents.length][];
            for (int i = 0; i < emptyContents.length; i++) {
                contents[i] = new String[2];
                contents[i][0] = emptyContents[i][0];
                if (emptyContents[i][1] == null) {
                    contents[i][1] = emptyContents[i][0];
                } else {
                    contents[i][1] = emptyContents[i][1];
                }
            }
        }
        return contents;
    }

    protected static Object[][] contents;

    protected static final Object[][] emptyContents = {
            // Localize below, pairs of key-value (what key is in your  .

            // Class: Animator
            // General messages:
            {"R0", "R0"},
            {"R1", "R1"},
            {"R2", "R2"},
            {"R3", "R3"},
            {"R4", "R4"},
            {"R5", "R5"},
            {"R6", "R6"},
            {"R7", "R7"},
            {"TR", "TR"},
            {"PC", "PC"},
            {"IR", "IR"},
            {"SR", "SR"},
            {"BASE", "BASE"},
            {"LIMIT", "LIMIT"},
            {"MAR", "MAR"},
            {"MBR", "MBR"},
            {"IN1", "IN1"},
            {"IN2", "IN2"},
            {"OUT", "OUT"},

            {"Registers", null},
            {"Control unit", null},
            {"MMU", null},
            {"ALU", null},
            {"External device", null},
            {"Memory", null},
            {"Current command: ", null},
            {"Fetch the next instruction from memory slot {0} to IR and " +
                    "increase PC by one.", null},
            {"No-operation command completed.", null},
            {"Fetch second operand from memory slot {0}.", null},
            {"Indirect memory addressing mode.", null},
            {"1: Fetch indexing value from memory slot {0}.", null},
            {"2: Fetch second operand from memory slot {0}.", null},
            {"Write value {0} from register R{1} to memory slot {2}.", null},
            {"Load value {0} to register R{1}.", null},
            {"Read value {0} from device {1} to register R{2}.", null},
            {"Write value {0} from register R{1} to device {2}.", null},
            {"Copy register R{0} to ALU IN1.", null},
            {"Copy second operand to ALU IN2.", null},
            {"ALU computes the result.", null},
            {"Copy ALU result to register R{0}", null},
            {"ALU computes the comparison result.", null},
            {"0=greater, 1=equal, 2=less", null},
            {"Set comparison result to SR", null},
            {"Branching command - branching condition is false, so do nothing.",
                    null},
            {"Branching command - branching condition is true, so update PC.",
                    null},
            {"Save new PC to TR", null},
            {"Increase stack pointer R{0} by one and push PC to stack.", null},
            {"Increase stack pointer R{0} by one and push FP to stack.", null},
            {"Copy stack pointer to FP.", null},
            {"Update PC.", null},
            {"Pop PC from stack and decrease stack pointer R{0} by one.", null},
            {"Pop FP from stack and decrease stack pointer R{0} by one.", null},
            {"Decrease {0} parameters from stack pointer R{1}.", null},
            {"Increase stack pointer R{0} by one, then write second operand to " +
                    "stack", null},
            {"Read value from stack to R{0}, then decrease stack pointer R{1} " +
                    "by one.", null},
            {"Read value from stack to R{0} then decrease stack pointer R{1} by one.", null},
            {"Supervisor call to operating system's services.", null},

            // Class: Application.
            // General messages: (none)
            // Exception messages:
            {"No more keyboard data stored on application.",
                    null},
            {"No more stdin data stored on application.",
                    null},
            {"Keyboard input string \"{0}\" invalid, should be eg. \\n-separated " +
                    "list of integers.",
                    null},
            {"Stdin input string \"{0}\" invalid, should be eg. \\n-separated " +
                    "list of integers.",
                    null},
            // Log messages:
            {"Application has no more keyboard data, read: {0}, buffer length " +
                    "{1}.",
                    null},
            {"Application has no more stdin data, read: {0}, buffer length {1}.",
                    null},
            {"Accepted \"{0}\" as keyboard input, tokens found: {1}.",
                    null},
            {"Accepted \"{0}\" as stdin input, tokens found: {1}.",
                    null},

            // Class: BinaryInterpreter, no messages.

            // Class: Binary
            {"___b91___ is missing.", null},
            {"___code___ is missing.", null},
            {"Invalid code area value on line: {0}", null},
            {"Invalid code area length on line: {0}", null},
            {"Invalid command on line: {0}", null},
            {"Invalid number of code lines.", null},
            {"___data___ is missing.", null},
            {"Invalid data area value on line: {0}", null},
            {"Invalid data area length on line: {0}", null},
            {"Invalid data on line: {0}", null},
            {"___symboltable___ is missing.", null},
            {"Invalid symbol on line: {0}", null},
            {"Invalid symbol value on line: {0}", null},
            {"___end___ is missing.", null},
            {"Lines after ___end___", null},

            // Class: CompileDebugger, no messages.
            // Class: CompileInfo, no messages.

            // Class: Compiler				// line (approx)
            {"Compilation is not finished yet.", null},        // 203
            {"Invalid command.", null},                        // 255
            {"Invalid label.", null},                                // 271, 328, 345 and 349
            {"Found label {0} and variable {1}.", null},        // 323
            {"Variable {0} used.", null},                        // 328
            {"Label {0} found.", null},                        // 333
            {"Invalid size for a DS.", null},                        // 358 and 362
            {"Invalid value for a DC.", null},                // 373
            {"Variable {0} defined as {1}.", null},                // 401
            {"Found variable {0}.", null},                        // 419 and 436
            {"{0} defined as {1}.", null},                        // 449
            {"Invalid DEF operation.", null},                        // 454
            {"{0} --> {1} ({2}) ", null}, // symb --> bin (:-sep. bin); 650
            //ADDED 26.4. OLLI
            {"Missing referred label {0}.", null},                //662

            {"Compilation failed: {0}", null},         //  767
            {"invalid label {0}.", null},             //  768
            {"invalid opcode {0}.", null},             //  781, 787, 1057
            {"second register expected.", null},       //  836, 877, 886
            {"invalid register {0}.", null},           //  859
            {"end of line expected.", null},           //  908
            {"first register expected.", null},        //  920, 948
            {"address expected.", null},               //  927, 968, 974
            {"invalid argument.", null},               //  939
            {"address or register expected.", null},   //  956
            {"invalid addressing mode {0}.", null},    //  980
            {"opcode missing.", null},                 //  991
            {"invalid value {0}.", null},              // 1072, 1088
            {"invalid value for a DS {0}.", null},     // 1082
            {"value expected.", null},                // 1097, 1104


            // Class: Control
            {"No default STDIN file set.", null},
            {"No default STDOUT file set.", null},
            {"No application to load.", null},
            {"STDIN data file unreadable: {0}", null},
            {"STDIN data file contains invalid data: {0}", null},
            {"STDIN files were null, data not inserted to the application.", null},
            {"Application contained an odd definition key '{0}'.", null},
            {"Trying to run an unsupported type of application. (The application " +
                    "must be created using the same program.)", null},
            {"Cannot form a binary out of an unsupported type of an application. " +
                    "(The application must be created using the same program.)", null},
            {"There is no application available to run from!", null},
            {"No STDOUT file set, not writing to it.", null},
            {"Memory size must be between 2^9 and 2^16, a change to 2^{0} failed.",
                    null},
            {"StdIn file contents are invalid; the file should contain only " +
                    "integers and separators.", null},
            {"Modified source was null.", null},
            {"No source file set, use openSource first.", null},
            {"Cannot deduce the file to store the binary into; no source " +
                    "file has been loaded.", null},
            {"Cannot save binary to file; no application has been compiled or " +
                    "loaded.", null},

            // Class: DebugInfo, no messages.

            // Class: FileHandler
            {"{0} in loadResourceBundle(): {1}", null},
            {"No read access to {0}.", null},
            {"No write access to {0}.", null},

            // Class: GUI
            {"File", null},
            {"Open", null},
            {"Compile", null},
            {"Run", null},
            {"Continue", null},
            {"Continue without pauses", null},
            {"Stop", null},
            {"Erase memory", null},
            {"Exit", null},
            {"Options", null},
            {"Set memory size", null},
            {"Set base number", null},
            {"Help", null},
            {"Manual", null},
            {"About", null},
            {"Set compiling options", null},
            {"Set running options", null},
            {"Configure file system", null},
            {"Select default stdin file", null},
            {"Select default stdout file", null},
            {"Set language", null},
            {"Select from a file...", null},
            {"en/manual.html", null},
            {"en/about.html", null},

            {"Binary (2)", null},
            {"Decimal (10)", null},
            {"Hexadecimal (16)", null},

            {"Line", null},
            {"Numeric value", null},
            {"Symbolic content", null},

            {"Open a new file", null},
            {"Compile the opened file", null},
            {"Run the loaded program", null},
            {"Continue current operation", null},
            {"Continue current operation without pauses", null},
            {"Stop current operation", null},
            {"Enable/disable animated execution", null},
            {"Show/hide video graphics display", null},
            {"Enable/disable extra comments while execution", null},
            {"Enable/disable line by line execution", null},
            {"Enter", null}, // button used to enter a number to the KBD device.
            {"Symbol table", null},
            {"Registers", null},

            // Class: GUIBrain
            {"Main path not found! (Trying to locate etc/settings.cfg.) " +
                    "...exiting.", null},
            {"I/O error while reading settings file: {0}", null},
            {"Parse error in settings file.", null},
            {"Titokone out of memory: {0}", null},
            {"Opened a new k91 source file.", null},
            {"File extension must be k91 or b91", null},
            {"Illegal input", null},
            {"Illegal input. You must insert a number between {0}...{1}", null},
            {"Error", null},
            {"Enter a number in the text field above.", null},
            {"Not a language file", null},
            {"Cannot overwrite {0}", null},
            {"Default stdin file set to {0}", null},
            {"Default stdout file set to {0}", null},
            {"Error while emptying {0}", null},
            {"Overwrite?", null},
            {"Do you want to overwrite the file? Select {1} to append or {0} " +
                    "to overwrite.", null},
            {"Waiting for KBD input...", null},
            {"Execution aborted due to an error", null},
            {"Compilation aborted due to an error", null},
            {"Current operation aborted", null},
            {"B91 binary", null},
            {"K91 source", null},
            {"Class file", null},
            {"Memory contents erased", null},
            // Class: GUICompileSettingsDialog
            // (Set compiling options already defined in GUI.)
            {"Apply", null},
            {"Close", null},
            {"Compile line by line", null},
            {"Show comments", null},
            {"Set compiling options", null},
            // Pause whenever a comment occurs & show extra comments while compiling
            // represented in JOptionPane.

            // Class: GUIRunSettingsDialog
            // (Set running options already defined in GUI, as are Apply and Close.)
            {"Execute line by line", null},
            {"Show comments", null},
            {"Show animation", null},
            // (execute code line by line, show extra comments while executing
            // and show animation while executing are represented in JOptionPane.

            // Class: GUIThreader, no messages.
            // Class: Interpreter, no messages.
            // Class: InvalidDefinitionException, no messages.
            // Class: InvalidSymbolException, no messages.

            // Class: JFileChooser
            {"Open", null},
            {"Cancel", null},
            {"Look in:", null},
            {"File name:", null},
            {"Files of type:", null},
            {"Up one level", null},
            {"Up", null},
            {"Desktop", null},
            {"Create new folder", null},
            {"New folder", null},
            {"List", null},
            {"Details", null},
            {"All files", null},
            {"Abort file selection", null},
            {"Open the selected file", null},

            // Class: JOptionPane
            {"Yes", null},
            {"No", null},
            {"Pause whenever a comment occurs", null},
            {"Show extra comments while compiling", null},
            {"Execute code line by line", null},
            {"Show extra comments while executing", null},
            {"Show animation while executing", null},
            {"Apply", null},
            {"Close", null},

            // Class: JTableX, no messages.

            // Class: Loader
            {"Null is an invalid parameter, instance of {0} required.", null},
            {"Program loaded into memory. FP set to {0} and SP to {1}.", null},

            // Class: LoadInfo, no messages.
            // Class: MemoryLine, no messages.
            // Class: Message, no messages. (Surprising, huh?)


            // Class: Processor
            {"Invalid operation code {0}", null},
            {"Memory address out of bounds", null},
            {"Invalid memory addressing mode", null},
            {"Invalid memory access mode in branching command", null},
            {"Invalid memory access mode in STORE", null},
            {"No keyboard data available", null},
            {"No standard input data available", null},
            {"Invalid device number", null},
            {"Integer overflow", null},
            {"Division by zero", null},
            {"Row number {0} is beyond memory limits.", null}, // in memoryInput

            // Class: RandomAccessMemory
            {"Memory size cannot be negative.", null},
            {"Tried to set symbol table to null.", null},
            {"Trying to load a null memory line.", null},
            {"Address {0} too large, memory size {1} (indexing starts at 0).",
                    null},
            {"Address {0} below zero.", null},
            {"Code area size cannot be negative.", null},
            {"Code area size cannot be bigger than the size of the whole memory.",
                    null},
            {"Data area size cannot be negative.", null},
            {"Data area size cannot be bigger than the size of the whole memory.",
                    null},

            // Class: Registers
            {"Unknown registerId: {0}", null},
            {"Unknown registerName: {0}", null},

            // Class: ResourceLoadFailedException, no messages.
            // Class: RunDebugger, comments
            {"{0}{1} Indexing {2}, direct.", null},
            {"{0}{1} Indexing {2}, direct addressing.", null},
            {"{0}{1} Indexing {2}, indirect addressing.", null},

            // Comment with value (=KBD, =CRT =STDIN =STDOUT)
            {"{0}{1} Indexing {2}, direct, value {3}.", null},
            // Are these two needed?
            {"{0}{1} Indexing {2}, direct addressing, value {3}.", null},
            {"{0}{1} Indexing {2}, indirect addressing {3}.", null},

            // External devices
            {"keyboard", null},
            {"stdin", null},
            {"stdout", null},
            {"display", null},


            // Class: RunInfo, no messages.

            // Class: Settings.
            // General messages: (none)
            // Exception messages:
            {"value", null},
            {"a linebreak", null},
            {"Illegal {0} \"{1}\", contains {2}.", null},
            {"Illegal {0}: null. Try an empty string instead.", null},
            {"Syntax error on line {0}, which was: \"{1}\".", null},
            // Log messages:
            {"Settings successfully parsed, lines: {0}, unique keys found: {1}.",
                    null},

            // Class: Source, no messages.
            // Class: SymbolicInterpreter, no messages.

            // Class: SymbolTable
            {"SymbolName was null.", null},
            {"Definition key was null.", null},
            {"Definition {0} not found.", null},

            // Class: Translator: no translateable strings set to avoid
            // looping bugs.


            {"__ABOUT_FILENAME__", "en/about.html"},
            {"__MANUAL_FILENAME__", "en/manual.html"}
            // Localizable bit ends.
    };
}
