/* Copyright 2004 University of Helsinki, Department of Computer
   Science. See license.txt for details. */
    
package fi.hu.cs.titokone.resources;

import java.util.ListResourceBundle;

/** This translation is what all translations should be based on. It 
    translates keys to plain English. Note that not all keys are equal 
    to their values; the key may contain a specification part which
    should not be translated. The specification part ought to be 
    put between ? and :, eg. "?Menu:File" would mean "translate the 
    word 'File' as if you would see it on the common menu name" as 
    opposed to "?Utility item:File", which might mean "translate the 
    word 'File' as you would if it meant a nail file". */
public class Translations extends ListResourceBundle {
   public Object[][] getContents() { 
     // To be simultaneously compatible with the general ListResourceBundle
     // functionality and provide a base template array for other 
     // translations, we do not repeat every line in the emptyContents 
     // array, but instead arrange the duplication when asked for
     // the real contents. (Only necessary the first time.)
     if(contents == null) {
       contents = new String[emptyContents.length][];
       for(int i = 0; i < emptyContents.length; i++) {
         contents[i] = new String[2];
         contents[i][0] = emptyContents[i][0];
         contents[i][1] = emptyContents[i][0];
       }
     }
     return contents;
   }
     
   protected static Object[][] contents;

   private static final Object[][] emptyContents = {
      // Localize below, pairs of key-value (what key is in your language)...
      // Remove lines which you do not wish to translate completely. Leaving
      // in a value of "" will translate the message to "" as well.

      // Class: Animator, not processed yet - TODO.

      // Class: Application.
      // General messages: (none)
      // Exception messages:
      { "No more keyboard data stored on application." , 
        null },
      { "No more stdin data stored on application.", 
	null },
      {	"Keyboard input string \"{0}\" invalid, should be eg. \\n-separated " +
	"list of integers." , 
	null },
      { "Stdin input string \"{0}\" invalid, should be eg. \\n-separated " +
	"list of integers.", 
	null },
      // Log messages:
      { "Application has no more keyboard data, read: {0}, buffer length " +
	"{1}.", 
	null },
      { "Application has no more stdin data, read: {0}, buffer length {1}.", 
	null },
      { "Accepted \"{0}\" as keyboard input, tokens found: {1}.", 
	null },
      { "Accepted \"{0}\" as stdin input, tokens found: {1}.", 
	null }, 

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
      {"Compilation is not finished yet.", null},	// 203
      {"Not a valid command.", null},			// 255
      {"Invalid label.", null},				// 271, 328, 345 and 349
      {"Found label {0} and variable {1}.", null},	// 323
      {"Variable {0} used.", null},			// 328
      {"Label {0} found.", null},			// 333
      {"Invalid size for a DS.", null},			// 358 and 362
      {"Invalid value for a DC.", null},		// 373
      {"Variable {0} defined as {1}.", null},		// 401
      {"Found variable {0}.", null},			// 419 and 436
      {"{0} defined as {1}.", null},			// 449
      {"Invalid DEF operation.", null},			// 454
      {"{0} --> {1} ({2}) ", null}, // symb --> bin (:-sep. bin); 650

      // Class: Control
      {"No application to load.", null},
      {"STDIN data file unreadable: {0}", null}, 
      {"STDIN data file contains invalid data: {0}", null},
      {"Application contained an odd definition key '{0}'.", null},
      {"Trying to run an unsupported type of application. (The application " +
       "must be created using the same program.)", null},
      {"Cannot form a binary out of an unsupported type of an application. " +
       "(The application must be created using the same program.)", null},
      {"There is no application available to run from!", null},
      {"Memory size must be between 2^9 and 2^16, a change to 2^{0} failed.", 
      null},
      {"StdIn file contents are invalid; the file should contain only " +
       "integers and separators.", null},
      {"Cannot deduce the file to store the binary into; no source " +
       "file has been loaded.", null},
      {"Cannot save binary to file; no application has been compiled or " +
       "loaded.", null},

      // Class: DebugInfo, no messages.

      // Class: FileHandler
      {"{0} in loadResourceBundle(): {1}", null},
      {"No read access to {0}.", null},
      {"No write access to {0}.", null},
      
      // Class: GUIBrain (not fully processed) - TODO
      {"Enter a number in the keyboard field above.", null},

      // Class: GUICompileSettingsDialog - not processed yet - TODO

      // Class: GUI (not fully processed) - TODO
      {"Open", null},
      {"Compile", null},
      {"Run", null},
	  {"Continue", null},
	  {"Continue without pauses", null},
	  {"Stop", null},
	  {"Options", null},
      {"Set memory size", null},
      {"Help", null},
      {"Manual", null},
	  {"About", null},
	  {"Set compiling options", null},
      {"Set running options", null},
      {"Configure file system", null},
      {"Select default stdin file", null},
      {"Select default stdout file", null},
      {"Set language", null},
      {"Compile the opened file", null},
      {"Run the loaded program", null},
      {"Continue current operation", null},
      {"Continue current operation without pauses", null},
      {"Stop current operation", null},
      {"Symbol table", null},
      {"Registers", null},
      
      // Class: GUIRunSettingsDialog - not processed yet - TODO
      // Class: GUIThreader, no messages.
      // Class: Interpreter, no messages.
      // Class: InvalidDefinitionException, no messages.
      // Class: InvalidSymbolException, no messages.
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
      // Class: RunDebugger, not processed yet - TODO.
      // Class: RunInfo, no messages.
      
      // Class: Settings.
      // General messages: (none)
      // Exception messages: 
      { "value", null },
      { "a linebreak", null },
      { "Illegal {0} \"{1}\", contains {2}.", null },
      { "Illegal {0}: null. Try an empty string instead.", null },
      { "Syntax error on line {0}, which was: \"{1}\".", null },
      // Log messages: 
      { "Settings successfully parsed, lines: {0}, unique keys found: {1}.", 
	null },

      // Class: Source, no messages.
      // Class: SymbolicInterpreter, no messages.
      
      // Class: SymbolTable
      {"SymbolName was null.", null},
      {"Definition key was null.", null},
      {"Definition {0} not found.", null}

      // Class: Translator: no translateable strings set to avoid 
      // looping bugs.

      // Localizable bit ends. 
  };
}
