/* Huomautukset:
    Lis‰sin COMMENTED, LINE_BY_LINE ja ANIMATED kent‰t.
    Poistin parametrin speed, menuSetRunningOptions() ja menuSetCompilingOptions() -metodeista
*/

package fi.hu.cs.titokone;

import java.util.Locale;
import java.io.File;
import java.util.Hashtable;
import java.io.*;
import java.util.logging.Logger;
import fi.hu.cs.ttk91.TTK91OutOfMemory;
import fi.hu.cs.titokone.__stupid_GUI;




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
  
  
    
private __stupid_Settings currentSettings;
private __stupid_GUI gui;  

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



/** This constructor sets up the GUIBrain instance. It calls private
    initialization functions, including findAvailableLanguages(). 
*/
public GUIBrain(__stupid_GUI gui) { 
  
  control = new __stupid_Control();
  this.gui = gui;
  settingsFile = new File("Titokone.cfg");
  currentSettings = new __stupid_Settings( control.loadSettingsFileContents(settingsFile) );
  availableLanguages = new Hashtable();
  
  findAvailableLanguages();
  
  interruptSent = false;
  
}

  
  

private String parseSuffix(File f) {
  
  String filename = f.getName();
  int i;
  for (i=0; i<filename.length() ; i++) {
    if (filename.charAt(i) == '.') {
      break;
    }
  }
  
  return filename.substring(i);
}



/** This method corresponds to the menu option File -> Open... It 
    calls either openBinaryFile or openSourceFile correspondingly. 
*/
public void menuOpenFile(File openedFile) { 
  
  String suffix = parseSuffix(openedFile);
  __stupid_LoadInfo loadinfo;
  
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
    
    gui.updateStatusLine(loadinfo.getStatusMessage());
    gui.updateReg(__stupid_GUI.SP, loadinfo.getSP());
    gui.updateReg(__stupid_GUI.FP, loadinfo.getFP());
    
    int binaryCommands[] = loadinfo.getBinaryCommands();
    String symbolicCommands[] = loadinfo.getSymbolicCommands();
    int line[] = new int[binaryCommands.length];
    for (int i=0 ; i<line.length ; i++) {
      line[i] = i;
    }
    
    gui.insertToCodeTable(line, binaryCommands, symbolicCommands);
    
    gui.setGUIView(3);
  
  
  
  
  
  }
  else if (suffix.equals("k91")) {
    String k91Source;
    try {
      k91Source = control.openSource(openedFile);
    }
    catch (IOException e) {
      //TODO: Mit‰ tehd‰‰n kun tulee IOException
    }
    
    //gui. //TODO: Tee loppuun
  }
  else {
    // TODO: Mit‰ tehd‰‰n kun tiedoston p‰‰te ei ole "k91" tai "b91"
  }
  
  
  
}



/** This method corresponds to the menu option File -> Run. It does 
    its work by calling runInstruction(). 
*/
public void menuRun() { 
  
  __stupid_RunInfo runinfo;
  int runmode = currentSettings.getIntValue(__stupid_Settings.RUN_MODE);
  
  gui.disable(__stupid_GUI.GUI_RUN_COMMAND);
  gui.enable(__stupid_GUI.GUI_STOP_COMMAND);
  
  runLine();
  
  while (interruptSent == false) {
    if ((runmode & LINE_BY_LINE) != 0) {   // If program is set to run line by line then
      try {
        wait();                             // the thread pauses to wait for a call of notify()
      }
      catch (InterruptedException e) {
      //jaajaa
      }
    }
    runLine();
  }
 
  
  
}




/** This method corresponds to the menu option File -> Compile. It
    does its work by calling compileLine(). 
*/
public void menuCompile() { 
  
  __stupid_CompileInfo compileinfo;
  int compilemode = currentSettings.getIntValue(__stupid_Settings.COMPILE_MODE);
  
  gui.disable(__stupid_GUI.GUI_COMPILE_COMMAND);
  gui.enable(__stupid_GUI.GUI_STOP_COMMAND);
  
  compileinfo = control.compileLine();

  while ( interruptSent == false ) {
      
    int phase = compileinfo.getPhase();

    if (phase == __stupid_CompileInfo.FIRST_ROUND) {
      if (compileinfo.symbolFound()) {
      	String symbolName = compileinfo.getSymbolName();
      	int symbolValue = 0;
      	if (compileinfo.getSymbolDefined()) {
	        symbolValue = compileinfo.getSymbolValue();
	      }
      	gui.updateRowInSymbolTable(symbolName, symbolValue);
      }
    }

    if (phase == __stupid_CompileInfo.FINALIZING_FIRST_ROUND) {
	    String symbolName;
	    int symbolValue;
	    if (compileinfo.definingDS()) {
  	    symbolName = compileinfo.getSymbolName();
  	    symbolValue = compileinfo.getDSAddress();
  	    gui.updateRowInSymbolTable(symbolName, symbolValue);
  	  }
	    else if (compileinfo.definingDC()) {
  	    symbolName = compileinfo.getSymbolName();
  	    symbolValue = compileinfo.getDCAddress();
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

  	    gui.updateInstructionsTable(instructionsLineNumber, newInstructionsContents);
  	    gui.updateDataTable(dataLineNumber, newDataContents);
  	    gui.setGUIView(3);
	    }
    }
	    

    

	
    if ( ((compilemode & PAUSED) != 0) && compileinfo.getComments().equals("") ) {
      gui.enable(__stupid_GUI.GUI_CONTINUE_COMMAND);
      gui.enable(__stupid_GUI.GUI_CONTINUE_WITHOUT_PAUSES_COMMAND);
      try {
        wait();                             // the thread pauses to wait for a call of notify()
      }
      catch (InterruptedException e) {
      //jaajaa
      }
      gui.disable(__stupid_GUI.GUI_CONTINUE_COMMAND);
      gui.disable(__stupid_GUI.GUI_CONTINUE_WITHOUT_PAUSES_COMMAND);
    }
    compileinfo = control.compileLine();
  }




}



/** This method corresponds to the menu option File -> Erase memory. 
*/
public void eraseMemory() { 
  
  control.eraseMemory();
  gui.setGUIView(1);
  
}



/** This method corresponds to the menu option File -> Exit. 
*/
public void menuExit() { }



/** This method corresponds to a request to interrupt whatever we 
    were doing once it becomes possible. 
*/
public void menuInterrupt() { }



public void menuSetLanguage(String language) { 
  control.setLanguage(language);
}



public void menuSetStdIn(File stdinFile) {
  control.setDefaultStdIn(stdinFile);
}



public void menuSetStdOut(File stdoutFile) {
  control.setDefaultStdOut(stdoutFile);
}



public void menuSetMemorySize(int newSize) {
  control.changeMemorySize(newSize);
  gui.setGUIView(1);
}



public void menuSetRunningOptions(boolean isCommentedExecution, 
                                  boolean isPausedExecution, 
                                  boolean isAnimatedExecution) {
  int runmode = 0;
  if (isCommentedExecution) {
    runmode += COMMENTED;
  }
  if (isPausedExecution) {
    runmode += LINE_BY_LINE;
  }
  if (isAnimatedExecution) {
    runmode += ANIMATED;
  }
  
  
  currentSettings.setValue(__stupid_Settings.RUN_MODE, runmode);
  control.saveSettings(currentSettings.toString(), settingsFile);
}



public void menuSetCompilingOptions(boolean isCommentedExecution, 
                                    boolean isPausedExecution) {
  int compilemode = 0;
  if (isCommentedExecution) {
    compilemode += COMMENTED;
  }
  if (isPausedExecution) {
    compilemode += PAUSED;
  }
  
  currentSettings.setValue(__stupid_Settings.COMPILE_MODE, compilemode);
  control.saveSettings(currentSettings.toString(), settingsFile);
}
                                    


public void menuAbout() {}



public void menuManual() {}



/** This method corresponds to some user input saying that a long,
    commented task can continue. 
*/
public void continueTask() {}



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
  File languageFile = new File("languages.cfg");
  String language, country, variant;
  
  if (languageFile.exists()) {
    String languageFileContents = control.loadSettingsFileContents(languageFile);
    String[] languageFileRow = languageFileContents.split("\n");
    
    /* Split each row of language.cfg into separate strings and 
       tokenize these strings by a colon. If there are two or three
       tokens on each row, then everything goes well. Otherwise
       the language.cfg is not a proper language file for this program.
    */
    for (int i=0 ; i<languageFileRow.length ; i++) {
      String[] token = languageFileRow[i].split(",");
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
        logger = Logger.getLogger(this.getClass().getPackage().toString());
	      logger.fine(new Message("Parse error in language file").toString());
	    }
    }
  }
}



/** This method calls the Control class service for running one 
    instruction and deals with the possible animation according to the 
    returned RunInfos. 
*/
private void runLine() { }



/** This method calls the Control class service for compiling a line
    and deals with the possible animation according to the returned
    CompileInfos. 
*/
private void compileLine() { }


}
