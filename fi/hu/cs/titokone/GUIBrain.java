/* Huomautukset:
    Lisäsin COMMENTED, LINE_BY_LINE ja ANIMATED kentät.
    Poistin parametrin speed menuSetRunningOptions() ja menuSetCompilingOptions() -metodeista
    Lisäsin metodin public void waitForContinueTask()
    Lisäsin metodin public void continueTaskWithoutPauses()
    Lisäsin kentän noPauses

*/

package fi.hu.cs.titokone;

import java.util.Locale;
import java.io.File;
import java.util.Hashtable;
//import java.io.*;
import java.io.IOException;
import java.util.logging.Logger;
import fi.hu.cs.ttk91.TTK91OutOfMemory;
import fi.hu.cs.ttk91.*;
import java.text.ParseException;
import java.util.LinkedList;



/** This class contains the intellect the GUI class needs to provide 
    services to the Control class. It also hides the GUI and Control from
    each other, serving as the middle-man for all their communication. It
    prepares all the data from CompileInfo, LoadInfo and RunInfo objects
    to GUI, so that it doesn't have to be aware of what's happening behind
    GUIBrain. Moreover, this class for its part doesn't take any position on 
    how to show the data provided here on the screen. It may, however, tell 
    GUI to disable or enable some elements or objects, such as buttons, 
    menuitems or inputfields.
*/
public class GUIBrain {
    
    
    
/** This field contains the languages available, with the long,
    English names like "Finnish" or "English (GB)" as keys and 
    the locales corresponding to the languages as values. 
*/
private Hashtable availableLanguages;

private __stupid_Control control;
  
  
    
/** This field namely stores the current settings and everytime a setting 
    is changed, this is informed about it. When a GUIBrain object is created,
    it asks for Control to get settings from settings file (which is as well
    stored in this class, to settingsFile field) and GUIBrain saves
    those settings (if there's any) in this field. 
*/
private Settings currentSettings;

private GUI gui;  

private File settingsFile;

public static final int COMMENTED = 1;
public static final int LINE_BY_LINE = 2;
public static final int PAUSED = 2;
public static final int ANIMATED = 4;
  
/** This field is set when menuInterrupt is called, and all continuous
  	loops which do many things and wait for input in between should 
   	check if this is sent before continuing to their next step. An 
  	interrupt sent means they should stop doing whatever they were doing
  	as soon as is reasonable. (Usually when it would be the time to 
  	wait for input from the user. 
*/
private boolean interruptSent;


/** This field is set true, when continueTaskWithoutPauses is launched.
    Methods menuRun() and menuCompile() check this to see if they want
    to override the setting in currentSettings.
*/
private boolean noPauses;


/** Keeps track of the state of this program. It can be NONE, B91_NOT_RUNNING,
    B91_RUNNING, B91_PAUSED, B91_WAIT_FOR_KBD, K91_NOT_COMPILING, K91_COMPILING
    or K91_PAUSED.
*/
private short currentState;


/** These fields are used to set the current state of program. It's stored into
    currentState field.
*/
private static final short NONE = 0;
private static final short B91_NOT_RUNNING = 1;
private static final short B91_RUNNING = 2;
private static final short B91_PAUSED = 3;
private static final short B91_WAIT_FOR_KBD = 4;
private static final short K91_NOT_COMPILING = 5;
private static final short K91_COMPILING = 6;
private static final short K91_PAUSED = 7;



/** This constructor sets up the GUIBrain instance. It calls private
    initialization functions, including findAvailableLanguages(). 
*/
public GUIBrain(GUI gui) { 
  
  control = new __stupid_Control();
  this.gui = gui;
  settingsFile = new File("etc/settings.cfg");
  
  try {
    currentSettings = new Settings( control.loadSettingsFileContents(settingsFile) );
  }
  catch (ParseException e) {
    System.out.println("ParseException in settings");
  }
  
  int runmode = currentSettings.getIntValue(Settings.RUN_MODE);
  
  gui.setSelected(GUI.OPTION_RUNNING_COMMENTED, (runmode & COMMENTED) != 0);
  gui.setSelected(GUI.OPTION_RUNNING_PAUSED, (runmode & PAUSED) != 0);
  gui.setSelected(GUI.OPTION_RUNNING_ANIMATED, (runmode & ANIMATED) != 0);
  
  int compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);
  
  gui.setSelected(GUI.OPTION_COMPILING_COMMENTED, (compilemode & COMMENTED) != 0);
  gui.setSelected(GUI.OPTION_COMPILING_PAUSED, (compilemode & PAUSED) != 0);
  
  availableLanguages = new Hashtable();
  
  findAvailableLanguages();
  
  noPauses = false;
  interruptSent = false;
  
  currentState = NONE;
}

  
  
private void load() {
  
  __stupid_LoadInfo loadinfo;
  try {
    loadinfo = control.load();
  }
  catch (Exception  e) {  //Tää tullaan muuttamaan TTK91OutOfMemory:ksi
    // TODO: Mitä tehdään kun loppuu muisti
    return;
  }
  
  gui.updateStatusBar(loadinfo.getStatusMessage());
  gui.updateReg(GUI.SP, loadinfo.getSP());
  gui.updateReg(GUI.FP, loadinfo.getFP());
   
  String[][] symbolsAndValues = loadinfo.getSymbolTable();
  gui.insertSymbolTable(symbolsAndValues);
    
  int binaryCommands[] = loadinfo.getBinaryCommands();
  String symbolicCommands[] = loadinfo.getSymbolicCommands();
  int data[] = loadinfo.getData();
  
  gui.insertToInstructionsTable(binaryCommands, symbolicCommands);
  gui.insertToDataTable(data);
  
  gui.addComment(loadinfo.getComments());
  
  currentState = B91_NOT_RUNNING;
  setGUICommandsForCurrentState();
  gui.setGUIView(3);
}



/** This method corresponds to the menu option File -> Open... It 
    calls either openBinaryFile or openSourceFile correspondingly. 
*/
public void menuOpenFile(File openedFile) { 
  
  /* Opening a file is a command that interrupts all other tasks. */
  interruptCurrentTasks();
  
  String suffix = getExtension(openedFile);
  
  gui.resetAll();
  
  if (suffix.equals("b91")) {
    try {
      control.openBinary(openedFile);
    }
    catch (IOException e) {
      gui.showError(e.getMessage());
    }
    
    load();
  }
  else if (suffix.equals("k91")) {
    gui.resetAll();
    
    String k91Source = "";
    
    try {
      k91Source = control.openSource(openedFile);
    }
    catch (IOException e) {
      gui.showError(e.getMessage());
    }
    
    String[] src = k91Source.split("\n|\r|\r\n");
    gui.insertToCodeTable(src);
    gui.updateStatusBar("Opened a new k91 source file");
    gui.setGUIView(2);
    
    currentState = K91_NOT_COMPILING;
    setGUICommandsForCurrentState();
    
    
  }
  else { // if file extension isn't either b91 or k91
    gui.showError("File extension must be k91 or b91");
  } 
}


public static final int MIN_KBD_VALUE = -32766;
public static final int MAX_KBD_VALUE = 32767;

public boolean enterInput(String input) {
  int inputValue;
  try {
    inputValue = Integer.parseInt(input);
  }
  catch (NumberFormatException e) {
    gui.changeTextInEnterNumberLabel("Illegal input");
    gui.updateStatusBar("Illegal input. You must insert a number between " + MIN_KBD_VALUE + "..." + MAX_KBD_VALUE);
    return false;
  }
  
  if (inputValue > MAX_KBD_VALUE || inputValue < MIN_KBD_VALUE) {
    gui.changeTextInEnterNumberLabel("Illegal input");
    gui.updateStatusBar("Illegal input. You must insert a number between " + MIN_KBD_VALUE + "..." + MAX_KBD_VALUE);
    return false;
  }
  
  gui.changeTextInEnterNumberLabel("");
  gui.updateStatusBar("");
  
  control.keyboardInput(inputValue);
  return true;
}
  

/** This method corresponds to the menu option File -> Run. It does 
    its work by calling runInstruction(). 
*/
public void menuRun() { 
  
  interruptSent = false;
  noPauses = false;
  
  __stupid_RunInfo runinfo;
  int runmode = currentSettings.getIntValue(Settings.RUN_MODE);
  
  currentState = B91_RUNNING;
  setGUICommandsForCurrentState();
   
  do { 
    currentState = B91_RUNNING;
    setGUICommandsForCurrentState();
    
    runmode = currentSettings.getIntValue(Settings.RUN_MODE);
    
    try {
      runinfo = control.runLine();
      if (runinfo == null) {
        break;
      }
    }
    catch (TTK91NoKbdData needMoreData) {
      gui.addComment(new Message("Enter a number in the keyboard field above.").toString());
      currentState = B91_WAIT_FOR_KBD;
      setGUICommandsForCurrentState();
      gui.enable(GUI.INPUT_FIELD);
      /* Wait until continueTask() is run. In this case it's done by pressing
         enter-button below the text field where user enter kbd-data.
      */
      waitForContinueTask();
      gui.disable(GUI.INPUT_FIELD);
      continue; // And then go to beginning of the do-while-loop.
      
    }
    
    if ((runmode & COMMENTED) != 0) {
      gui.addComment(runinfo.getLineNumber() + ": " + runinfo.getComments());
    }
    gui.selectLine(runinfo.getLineNumber(), GUI.INSTRUCTIONS_AND_DATA_TABLE);
    if (runinfo.getCrtData() != null) {
      gui.addOutputData( runinfo.getCrtData().intValue() );
    }
    
    int[] newRegisterValues = runinfo.getRegisters();
    gui.updateReg(GUI.R0, newRegisterValues[TTK91Cpu.REG_R0]);
    gui.updateReg(GUI.R1, newRegisterValues[TTK91Cpu.REG_R1]);
    gui.updateReg(GUI.R2, newRegisterValues[TTK91Cpu.REG_R2]);
    gui.updateReg(GUI.R3, newRegisterValues[TTK91Cpu.REG_R3]);
    gui.updateReg(GUI.R4, newRegisterValues[TTK91Cpu.REG_R4]);
    gui.updateReg(GUI.R5, newRegisterValues[TTK91Cpu.REG_R5]);
    gui.updateReg(GUI.R6, newRegisterValues[TTK91Cpu.REG_R6]);
    gui.updateReg(GUI.R7, newRegisterValues[TTK91Cpu.REG_R7]);
    gui.updateReg(GUI.PC, newRegisterValues[TTK91Cpu.CU_PC]);
    
    LinkedList changedMemoryLines = runinfo.getChangedMemoryLines();
    while (changedMemoryLines.isEmpty() == false) {
      Object[] listItem = (Object[])changedMemoryLines.getFirst();
      int line = ((Integer)listItem[0]).intValue();
      MemoryLine contents = (MemoryLine)listItem[1];
      gui.updateInstructionsAndDataTableLine(line, contents.getBinary(), contents.getSymbolic());
      changedMemoryLines.removeFirst();
    }
      
    
    
    //gui.invalidate();
    gui.repaint();
    
    
    if ((runmode & LINE_BY_LINE) != 0 && noPauses == false) {
      currentState = B91_PAUSED;
      setGUICommandsForCurrentState();
      waitForContinueTask();
      
    }
    else {
      synchronized(this) {
        try {
          wait(70);
        }
        catch(InterruptedException e) {
          System.out.println("InterruptedException in menuRun()");
        }
      }
    }
    
  } while (interruptSent == false); // End of do-while -loop
  
  currentState = B91_NOT_RUNNING;
  setGUICommandsForCurrentState();
  
  gui.unselectAll();
}




/** This method corresponds to the menu option File -> Compile. It
    does its work by calling compileLine(). 
*/
public void menuCompile() { 
  
  interruptSent = false;
  noPauses = false;

  currentState = K91_COMPILING;
  setGUICommandsForCurrentState();
  
  __stupid_CompileInfo compileinfo;
  int compilemode;
  int phase;
  
  /* This will be set to true once the compilation succeeds. The value
     of this variable will be used in case of an interrupted compilation
     or if an error occurs, when menuCompile() has to decide whether to
     change back to pre-compilation-started state.
  */
  boolean compilingCompleted = false;
  
  /* This is a variable for debugging */int n = 0; 
  do {
    
    /* This is for debugging */ //if(n++ > 20) interruptSent = true;
    currentState = K91_COMPILING;
    setGUICommandsForCurrentState();
    
    try {
      compileinfo = control.compileLine();
    }
    catch (Exception e) { // TODO: will be changed to TTK91CompileException
      gui.addComment(e.getMessage());
      currentState = K91_PAUSED;
      setGUICommandsForCurrentState();
      waitForContinueTask();
      break;
    }
    
    compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);    
    phase = compileinfo.getPhase();
    
    if (phase == __stupid_CompileInfo.FIRST_ROUND) {  
      if (compileinfo.getSymbolFound()) {      	
      	String symbolName = compileinfo.getSymbolName();
      	Integer symbolValue = null;
      	if (compileinfo.getSymbolDefined()) {
	        symbolValue = new Integer(compileinfo.getSymbolValue());
	      }
      	gui.updateRowInSymbolTable(symbolName, symbolValue);
      }
      if (compileinfo.getLabelFound()) {
        String symbolName = compileinfo.getLabelName();
      	Integer symbolValue = new Integer(compileinfo.getLabelValue());
        gui.updateRowInSymbolTable(symbolName, symbolValue);
      }
      
      gui.selectLine(compileinfo.getLineNumber(), GUI.CODE_TABLE);
    }

    else if (phase == __stupid_CompileInfo.FINALIZING_FIRST_ROUND) {
	    String[][] symbolTable = compileinfo.getSymbolTable();
	    if (symbolTable != null) {
  	    for (int i=0 ; i<symbolTable.length ; i++) {
          String symbolName = symbolTable[i][0];
          Integer symbolValue = new Integer(symbolTable[i][1]);
          gui.updateRowInSymbolTable(symbolName, symbolValue);  
      	}
      }
      
      String[] newInstructionsContents = compileinfo.getInstructions();
	    String[] newDataContents = compileinfo.getData();
	    gui.insertToInstructionsTable(newInstructionsContents);
	    gui.insertToDataTable(newDataContents);
	    gui.setGUIView(3);
    
    }
    else if (phase == __stupid_CompileInfo.SECOND_ROUND) {
      int line = compileinfo.getLineNumber();
      int binary = compileinfo.getLineBinary();
      gui.updateInstructionsAndDataTableLine(line, binary);
      gui.selectLine(compileinfo.getLineNumber(), GUI.INSTRUCTIONS_AND_DATA_TABLE);
    }
    else if (phase == __stupid_CompileInfo.FINALIZING) {
      if (compileinfo.getFinal() == true) {
        compilingCompleted = true;
        break;
      }
    }
    
    gui.repaint();
            
	  if ( ((compilemode & PAUSED) != 0) && !compileinfo.getComments().equals("")  && noPauses == false) {
      currentState = K91_PAUSED;
      setGUICommandsForCurrentState();
      waitForContinueTask();
    }
    else {
      synchronized(this) {
        try {
          wait(70);
        }
        catch(InterruptedException e) {
          System.out.println("InterruptedException in menuRun()");
        }
      }
    }
    
  } while ( interruptSent == false ); // End of do-loop
  
  
  if (compilingCompleted == true) {
    gui.resetAll();
    load();
  }
  else {
    currentState = K91_NOT_COMPILING;
    setGUICommandsForCurrentState();
    gui.setGUIView(2);
  }
}



/** This method corresponds to the menu option File -> Erase memory. 
*/
public void menuEraseMemory() { 
  interruptCurrentTasks();
  control.eraseMemory();
  gui.setGUIView(1);
  currentState = NONE;
  setGUICommandsForCurrentState();
  
}



/** This method corresponds to the menu option File -> Exit. 
*/
public void menuExit() { 
  interruptCurrentTasks();
}



private void interruptCurrentTasks() {
  interruptSent = true;
  synchronized(this) {
    notifyAll();
  }
}
/** This method corresponds to a request to interrupt whatever we 
    were doing once it becomes possible. 
*/
public void menuInterrupt() { 
  interruptCurrentTasks();
  
  switch (currentState) {
    case B91_NOT_RUNNING:
    case B91_RUNNING:
    case B91_PAUSED:
    case B91_WAIT_FOR_KBD:
      control.load();
      break;
  }
  
}



public void menuSetLanguage(String language) { 
  
 if (availableLanguages.containsKey(language)) {
    System.out.println((Locale)availableLanguages.get(language));
    Translator.setLocale((Locale)availableLanguages.get(language));  
    currentSettings.setValue(Settings.UI_LANGUAGE, language);
    control.saveSettings(currentSettings.toString(), settingsFile);
    gui.updateAllTexts();
  }
}



public void menuSetLanguage(File languageFile) { 
  
 if (languageFile.exists()) {
    try {
      Translator.setLocale(Locale.CHINESE, control.loadLanguageFile(languageFile));
    }
    catch (ResourceLoadFailedException e) {
      gui.showError(new Message("Not a language file").toString());
      System.out.println(e.getMessage());
      return;
    }
    //currentSettings.setValue(Settings.UI_LANGUAGE, language);
    //control.saveSettings(currentSettings.toString(), settingsFile);
    gui.updateAllTexts();
  }
}



public void menuSetStdin(File stdinFile) {
  try {
    control.setDefaultStdIn(stdinFile);
  }
  catch (ParseException e) {
    gui.showError(e.getMessage());
    return;
  }
  currentSettings.setValue(Settings.STDIN_PATH, stdinFile.getPath());
  control.saveSettings(currentSettings.toString(), settingsFile);
}



public void menuSetStdout(File stdoutFile) {
  try {
    control.setDefaultStdOut(stdoutFile);
  }
  catch (IOException e) {
    gui.showError(e.getMessage());
    return;
  }
  currentSettings.setValue(Settings.STDOUT_PATH, stdoutFile.getPath());
  control.saveSettings(currentSettings.toString(), settingsFile);
}



public void menuSetMemorySize(int newSize) {
  try { 
    control.changeMemorySize(newSize);
  }
  catch (IllegalArgumentException e) {
    return;
  }
  currentSettings.setValue(Settings.MEMORY_SIZE, newSize);
  control.saveSettings(currentSettings.toString(), settingsFile);
  gui.setGUIView(1);
}



public void menuSetRunningOption(int option,boolean b) {
  int runmode = currentSettings.getIntValue(Settings.RUN_MODE);
  
  if (((runmode & option) != 0) == b) {
    // do nothing
  }
  else if ((runmode & option) != 0) {
    runmode -= option;
  }
  else if ((runmode & option) == 0) {
    runmode += option;
  }
  
  currentSettings.setValue(Settings.RUN_MODE, runmode);
  System.out.println(runmode);
  
  switch (option) {
    case LINE_BY_LINE: // Synonym for case PAUSED:
      gui.setSelected(GUI.OPTION_RUNNING_PAUSED, b);
      break;    
    case COMMENTED:
      gui.setSelected(GUI.OPTION_RUNNING_COMMENTED, b);
      break;    
    case ANIMATED:
      gui.setSelected(GUI.OPTION_RUNNING_ANIMATED, b);
      break;
  }    
}


public void menuSetCompilingOption(int option,boolean b) {
  int compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);
  
  if (((compilemode & option) != 0) == b) {
    // do nothing
  }
  else if ((compilemode & option) != 0) {
    compilemode -= option;
  }
  else if ((compilemode & option) == 0) {
    compilemode += option;
  }
  
  currentSettings.setValue(Settings.COMPILE_MODE, compilemode);
  System.out.println(compilemode);
  
  switch (option) {
    case PAUSED:
      gui.setSelected(GUI.OPTION_COMPILING_PAUSED, b);
      break;    
    case COMMENTED:
      gui.setSelected(GUI.OPTION_COMPILING_COMMENTED, b);
      break;    
    
  }    
}
 

public void menuAbout() {}



public void menuManual() {}



/** Notifies all methods,that have called waitForContinueTask() to continue 
    their operation.
*/
public void continueTask() {
  synchronized(this) {
    notify();
  }
  return;
}



/** Notifies all methods, that have called waitForContinueTask() to continue
    their operation plus informs them that waitForContinueTask() should no 
    longer be called during current operation.
*/
public void continueTaskWithoutPauses() {
  noPauses = true;
  synchronized(this) {
    notify();
  }
  return;
}




/** A method can call this, if it wants enter into pause mode and wait for someone
    to call continueTask() or continueTaskWithoutPauses() methods. This method cannot
    however be used, unless the method which is calling this hasn't been set to run
    in a thread of its own. eg. by calling new GUIThreader()
*/
public void waitForContinueTask() {
  
  synchronized(this) {
    try {
      wait();
    }
    catch (InterruptedException e) {
      System.out.println("InterruptedException");
    }
  }
  return;
}




public String[] getAvailableLanguages() {
  Object[] keys = availableLanguages.keySet().toArray();
  String[] str = new String[keys.length];
  for (int i=0 ; i<keys.length ; i++) {
    str[i] = (String)keys[i];
  }
  return str;
}
  

/* In addition: Some EventListeners which would eg. call continueTask
   instead of GUI trying to determine which means that we should go on? */

// Services for Control. ----------------------------------------------

// Sini 17.3.: Control throws an exception to get keyboard input. It
// does not call a method of a "higher" class.
//public String getUserKeyboardInput() {}



// Private methods. ---------------------------------------------------

/** This method determines the available languages. It reads them from 
    a setup file languages.cfg, which contains lineseparator-delimited
    sets of language-name, language-id, (country), eg.
    "Finnish, fi", or "English (GB), en, GB". 
*/
private void findAvailableLanguages() {
  Logger logger;
  File languageFile = new File("etc/languages.cfg");
  String languageName, language, country, variant;
  
  if (languageFile.exists()) {
    String languageFileContents = control.loadSettingsFileContents(languageFile);
    String[] languageFileRow = languageFileContents.split("\n|\r|\r\n");
    
    System.out.println(languageFileContents);
    /* Split each row of language.cfg into separate strings and 
       tokenize these strings by a colon. If there are two or three
       tokens on each row, then everything goes well. Otherwise
       the language.cfg is not a proper language file for this program.
    */
    for (int i=0 ; i<languageFileRow.length ; i++) {
      String[] token = languageFileRow[i].split("=");
      if (token.length != 2) {
        System.out.println("Parse error in language file");
        return;
      }
      languageName = token[0].trim();
      
      token = token[1].split("\\.");
      if (token.length == 1) {
        language = token[0].trim();
        availableLanguages.put(languageName, new Locale(language));
      }
      else if (token.length == 2) {
        language = token[0].trim();
        country = token[1].trim();
        availableLanguages.put(languageName, new Locale(language, country));
      }
      else if (token.length == 3) {
        language = token[0].trim();
        country = token[1].trim();
        variant = token[2].trim();
        availableLanguages.put(languageName, new Locale(language, country, variant));
      }
      
      else {
        //logger = Logger.getLogger(this.getClass().getPackage().toString());
	      //logger.fine(new Message("Parse error in language file").toString());
	      System.out.println("Parse error in language file");
	    }
    }
  }
}




public static String getExtension(File f) {
  String ext = null;
  String s = f.getName();
  int i = s.lastIndexOf('.');

  if (i > 0 &&  i < s.length() - 1) {
      ext = s.substring(i+1).toLowerCase();
  }
  return ext;
}




private void setGUICommandsForCurrentState() {
  switch (currentState) {
    case NONE:
      gui.disable(GUI.COMPILE_COMMAND);
      gui.disable(GUI.RUN_COMMAND);
      gui.disable(GUI.CONTINUE_COMMAND);
      gui.disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
      gui.disable(GUI.STOP_COMMAND);
      break;
      
    case B91_NOT_RUNNING:
      gui.disable(GUI.COMPILE_COMMAND);
      gui.enable(GUI.RUN_COMMAND);
      gui.disable(GUI.CONTINUE_COMMAND);
      gui.disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND); 
      gui.disable(GUI.STOP_COMMAND);
      break;
    
    case B91_RUNNING:
      gui.disable(GUI.COMPILE_COMMAND);
      gui.disable(GUI.RUN_COMMAND);
      gui.disable(GUI.CONTINUE_COMMAND);
      gui.disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
      gui.enable(GUI.STOP_COMMAND);
      break;
      
    case B91_PAUSED:
      gui.disable(GUI.COMPILE_COMMAND);
      gui.disable(GUI.RUN_COMMAND);
      gui.enable(GUI.CONTINUE_COMMAND);
      gui.enable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
      gui.enable(GUI.STOP_COMMAND);
      break;
    
    case B91_WAIT_FOR_KBD:
      gui.disable(GUI.COMPILE_COMMAND);
      gui.disable(GUI.RUN_COMMAND);
      gui.disable(GUI.CONTINUE_COMMAND);
      gui.disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
      gui.enable(GUI.STOP_COMMAND);
      break;
      
    case K91_NOT_COMPILING:
      gui.enable(GUI.COMPILE_COMMAND);
      gui.disable(GUI.RUN_COMMAND);
      gui.disable(GUI.CONTINUE_COMMAND);
      gui.disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
      gui.disable(GUI.STOP_COMMAND);
      break;
    
    case K91_COMPILING:
      gui.disable(GUI.COMPILE_COMMAND);
      gui.disable(GUI.RUN_COMMAND);
      gui.disable(GUI.CONTINUE_COMMAND);
      gui.disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
      gui.enable(GUI.STOP_COMMAND);
      break;
     
    case K91_PAUSED:
      gui.disable(GUI.COMPILE_COMMAND);
      gui.disable(GUI.RUN_COMMAND);
      gui.enable(GUI.CONTINUE_COMMAND);
      gui.enable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
      gui.enable(GUI.STOP_COMMAND);
      break;
  }
}
}
