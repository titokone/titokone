/* Huomautukset:
    Lis‰sin COMMENTED, LINE_BY_LINE ja ANIMATED kent‰t.
    Poistin parametrin speed menuSetRunningOptions() ja menuSetCompilingOptions() -metodeista
    Lis‰sin metodin public void waitForContinueTask()
    Lis‰sin metodin public void continueTaskWithoutPauses()
    Lis‰sin kent‰n noPauses

*/

package fi.hu.cs.titokone;

import java.util.Locale;
import java.io.File;
import java.util.Hashtable;
import java.io.*;
import java.util.logging.Logger;
import fi.hu.cs.ttk91.TTK91OutOfMemory;
import fi.hu.cs.ttk91.*;
import java.text.ParseException;




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
  
  availableLanguages = new Hashtable();
  
  findAvailableLanguages();
  
  noPauses = false;
  interruptSent = false;
  
  currentState = NONE;
}

  
  


/** This method corresponds to the menu option File -> Open... It 
    calls either openBinaryFile or openSourceFile correspondingly. 
*/
public void menuOpenFile(File openedFile) { 
  
  /* Opening a file is a command that interrupts all other tasks. */
  interruptCurrentTasks();
  
  String suffix = getExtension(openedFile);
  __stupid_LoadInfo loadinfo;
  
  gui.resetAll();
  
  if (suffix.equals("b91")) {
    try {
      control.openBinary(openedFile);
    }
    catch (IOException e) {
      //TODO: Mit‰ tehd‰‰n kun tulee IOException
    }
    
    try {
      loadinfo = control.load();
    }
    catch (Exception  e) {  //T‰‰ tullaan muuttamaan TTK91OutOfMemory:ksi
      // TODO: Mit‰ tehd‰‰n kun loppuu muisti
      return;
    }
    
    
    gui.updateStatusBar(loadinfo.getStatusMessage());
    gui.updateReg(__stupid_GUI.SP, loadinfo.getSP());
    gui.updateReg(__stupid_GUI.FP, loadinfo.getFP());
     
    currentState = B91_NOT_RUNNING;
    setGUICommandsForCurrentState();
    
    String[][] symbolsAndValues = loadinfo.getSymbolTable();
    gui.insertSymbolTable(symbolsAndValues);
    
    
    int binaryCommands[] = loadinfo.getBinaryCommands();
    String symbolicCommands[] = loadinfo.getSymbolicCommands();
    int line[] = new int[binaryCommands.length];
    for (int i=0 ; i<line.length ; i++) {
      line[i] = i;
    }
    
    gui.insertToInstructionsTable(line, binaryCommands, symbolicCommands);
    
    int data[] = loadinfo.getData();
    line = new int[data.length];
    for (int i=0 ; i<line.length ; i++) {
      line[i] = binaryCommands.length + i;
    }
    
    gui.insertToDataTable(line, data);
    
    gui.addComment(loadinfo.getComments());
    
    
    gui.setGUIView(3);
  
  
  
  
  
  }
  else if (suffix.equals("k91")) {
    gui.resetAll();
    
    String k91Source = "";
    
    try {
      k91Source = control.openSource(openedFile);
    }
    catch (IOException e) {
      //TODO: Mit‰ tehd‰‰n kun tulee IOException
    }
    
    String[] src = k91Source.split("\n|\r|\r\n");
    gui.insertToCodeTable(src);
    gui.updateStatusBar("Opened a new k91 source file");
    gui.setGUIView(2);
    
    currentState = K91_NOT_COMPILING;
    setGUICommandsForCurrentState();
    
    
  }
  else {
    // TODO: Mit‰ tehd‰‰n kun tiedoston p‰‰te ei ole "k91" tai "b91"
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
  
  
  /* This is a variable for debugging */int n = 0; 
  do {
    
    runmode = currentSettings.getIntValue(Settings.RUN_MODE);
    /* This is for debugging */ //if(n++ > 20) interruptSent = true;
    
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
      /* Wait while continueTask() is run. In this case it's done by pressing
         enter-button below the text field where user enter kbd-data.
      */
      waitForContinueTask();
      gui.disable(GUI.INPUT_FIELD);
      continue; // And then go to beginning of the do-while-loop.
      
    }
    
    if ((runmode & COMMENTED) != 0) {
      gui.addComment(runinfo.getLineNumber() + ": " + runinfo.getComments());
    }
    gui.selectRow(GUI.INSTRUCTIONS_AND_DATA_TABLE, runinfo.getLineNumber());
    if (runinfo.getCrtData() != null) {
      gui.addOutputData( runinfo.getCrtData().intValue() );
    }
    gui.invalidate();
    gui.repaint();
    
    if ((runmode & LINE_BY_LINE) != 0 && noPauses == false) {   // If program is set to run line by line then
      currentState = B91_PAUSED;
      setGUICommandsForCurrentState();
      waitForContinueTask();
      currentState = B91_RUNNING;
      setGUICommandsForCurrentState();
    }
    
    synchronized(this) {
      try {
        wait(50);
      }
      catch(InterruptedException e) {
        System.out.println("InterruptedException in menuRun()");
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
    
    /* This is for debugging */ if(n++ > 20) interruptSent = true;
    
    compileinfo = control.compileLine();
    compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);    
    phase = compileinfo.getPhase();
    
    if (phase == __stupid_CompileInfo.FIRST_ROUND) {  
      if (compileinfo.symbolFound()) {      	
      	String symbolName = compileinfo.getSymbolName();
      	Integer symbolValue = null;
      	if (compileinfo.getSymbolDefined()) {
	        symbolValue = new Integer(compileinfo.getSymbolValue());
	      }
      	gui.updateRowInSymbolTable(symbolName, symbolValue);
      }
    }

    else if (phase == __stupid_CompileInfo.FINALIZING_FIRST_ROUND) {
	    String symbolName;
	    Integer symbolValue;
	    if (compileinfo.definingDS()) {
  	    symbolName = compileinfo.getSymbolName();
  	    symbolValue = new Integer(compileinfo.getDSAddress());
  	    gui.updateRowInSymbolTable(symbolName, symbolValue);
  	  }
	    else if (compileinfo.definingDC()) {
  	    symbolName = compileinfo.getSymbolName();
  	    symbolValue = new Integer(compileinfo.getDCAddress());
  	    gui.updateRowInSymbolTable(symbolName, symbolValue);
  	  }
	    else {
  	    String[] newInstructionsContents = compileinfo.getInstructions();
  	    String[] newDataContents = compileinfo.getData();
  	    int[] instructionsLineNumber = new int[newInstructionsContents.length];
  	    int[] dataLineNumber = new int[newDataContents.length];
  	    int i;
  
  	    for (i = 0 ; i < instructionsLineNumber.length ; i++) {
		      instructionsLineNumber[i] = i;
	      }
	      for (int j = 0 ; j < dataLineNumber.length ; j++) {
		      dataLineNumber[j] = j+i;
	      }

  	    gui.insertToInstructionsTable(instructionsLineNumber, newInstructionsContents);
  	    gui.insertToDataTable(dataLineNumber, newDataContents);
  	    gui.setGUIView(3);
	    }
    }
    else if (phase == __stupid_CompileInfo.SECOND_ROUND) {
    
    }
    
    gui.selectRow(GUI.CODE_TABLE, compileinfo.getLineNumber());
    gui.repaint();
            
	  if ( ((compilemode & PAUSED) != 0) && !compileinfo.getComments().equals("")  && noPauses == false) {
      currentState = K91_PAUSED;
      setGUICommandsForCurrentState();
      waitForContinueTask();
      currentState = K91_COMPILING;
      setGUICommandsForCurrentState(); 
    }
  } while ( interruptSent == false ); // End of do-loop
  
  
  if (compilingCompleted == true) {
    currentState = B91_NOT_RUNNING;
    setGUICommandsForCurrentState();
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
public void menuExit() { }



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
    
    Translator.setLocale((Locale)availableLanguages.get(language));  
    currentSettings.setValue(Settings.UI_LANGUAGE, language);
    control.saveSettings(currentSettings.toString(), settingsFile);
  }
}



public void menuSetStdin(File stdinFile) {
  control.setDefaultStdIn(stdinFile);
  currentSettings.setValue(Settings.STDIN_PATH, stdinFile.getPath());
  control.saveSettings(currentSettings.toString(), settingsFile);
}



public void menuSetStdout(File stdoutFile) {
  control.setDefaultStdOut(stdoutFile);
  currentSettings.setValue(Settings.STDOUT_PATH, stdoutFile.getPath());
  control.saveSettings(currentSettings.toString(), settingsFile);
}



public void menuSetMemorySize(int newSize) {
  control.changeMemorySize(newSize);
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
  String language, country, variant;
  
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
      String[] token = languageFileRow[i].split(",|=");
      if (token.length == 2) {
        language = token[0];
        country = token[1];
        availableLanguages.put(language, new Locale(country));
      }
      else if (token.length == 3) {
        language = token[0];
        country = token[1];
        variant = token[2];
        availableLanguages.put(language, new Locale(country, variant));
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
