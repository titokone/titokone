// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.*;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;


/**
 * This class contains the intellect the GUI class needs to provide
 * services to the Control class. It also hides the GUI and Control from
 * each other, serving as the middle-man for all their communication. It
 * prepares all the data from CompileInfo, LoadInfo and RunInfo objects
 * to GUI, so that it doesn't have to be aware of what's happening behind
 * GUIBrain. Moreover, this class for its part doesn't take any position on
 * how to show the data provided here on the screen. It may, however, tell
 * GUI to disable or enable some elements or objects, such as buttons,
 * menuitems or inputfields.
 */
public class GUIBrain {

    //lock object to avoid locking externally
    protected Object lock=new Object();
    /**
     * This variable can be set to e.g. 70 to slow down the GUI on
     * compilation and runtime for overly fast machines. It should
     * preferrably be completely replaced with a user-selectable option,
     * however. The value should be at minimum 0.
     */
    private final int SLOWDOWN = 0;


    /**
     * This field contains the languages available, with the long,
     * English names like "Finnish" or "English (GB)" as keys and
     * the locales corresponding to the languages as values.
     */
    private Hashtable<String, Locale> availableLanguages;

    private Control control;


    /**
     * This field namely stores the current settings and everytime a setting
     * is changed, this is informed about it. When a GUIBrain object is created,
     * it asks for Control to get settings from settings file (which is as well
     * stored in this class, to settingsFile field) and GUIBrain saves
     * those settings (if there's any) in this field.
     */
    private Settings currentSettings;


    private Animator animator;
    private Display display;


    private GUI gui;

    private String programPath = "fi/helsinki/cs/titokone/";
    private File settingsFile;

    private File currentlyOpenedFile;

    public static final int COMMENTED = 1;
    public static final int LINE_BY_LINE = 2;
    public static final int PAUSED = 2;
    public static final int ANIMATED = 4;

    /**
     * This field is set when menuInterrupt is called, and all continuous
     * loops which do many things and wait for input in between should
     * check if this is sent before continuing to their next step. An
     * interrupt sent means they should stop doing whatever they were doing
     * as soon as is reasonable. (Usually when it would be the time to
     * wait for input from the user.
     */
    private boolean interruptSent;


    /**
     * This field is set true, when continueTaskWithoutPauses is launched.
     * Methods menuRun() and menuCompile() check this to see if they want
     * to override the setting in currentSettings.
     */
    private boolean noPauses;


    private boolean threadRunning;


    /**
     * Keeps track of the state of this program. It can be NONE, B91_NOT_RUNNING,
     * B91_RUNNING, B91_PAUSED, B91_WAIT_FOR_KBD, K91_NOT_COMPILING, K91_COMPILING
     * or K91_PAUSED.
     */
    private short currentState;


    /**
     * These fields are used to set the current state of program. It's stored into
     * currentState field.
     */
    private static final short NONE = 0;
    private static final short B91_NOT_RUNNING = 1;
    private static final short B91_RUNNING = 2;
    private static final short B91_PAUSED = 3;
    private static final short B91_WAIT_FOR_KBD = 4;
    private static final short K91_NOT_COMPILING = 5;
    private static final short K91_COMPILING = 6;
    private static final short K91_PAUSED = 7;
    private static final short INTERRUPTED_WITHOUT_PAUSE = 10;
    private static final short INTERRUPTED_WITH_PAUSE = 11;

    /**
     * This field contains our logger.
     */
    private Logger logger;

    public static String DEFAULT_STDIN_FILENAME = "stdin";
    public static String DEFAULT_STDOUT_FILENAME = "stdout";

    /**
     * This variable sets whether the initial data area length (up to sp)
     * should be marked by hiding the symbolic versions of lines down from
     * it.
     */
    protected static boolean ENABLE_DATA_AREA_MARKUP = true;


    /**
     * This constructor sets up the GUIBrain instance. It calls private
     * initialization functions, including findAvailableLanguages().
     */
    public GUIBrain(GUI gui, Animator animator, Display display) {

        this.gui = gui;

        logger = Logger.getLogger(getClass().getPackage().getName());
        File defStdinFile = new File(System.getProperty("user.dir"), DEFAULT_STDIN_FILENAME);
        File defStdoutFile = new File(System.getProperty("user.dir"), DEFAULT_STDOUT_FILENAME);
        try {
            //defStdinFile.createNewFile(); // We don't need write access to stdin.
            if (!defStdoutFile.exists()) {
                defStdoutFile.createNewFile();
                defStdoutFile.delete();
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        control = new Control(defStdinFile, defStdoutFile,display);

        this.animator = animator;
        this.display = display;

        try {
            getCurrentSettings();
        } catch (IOException e) {
            System.out.println("Settings file cannot be accessed. ...exiting.");
            System.exit(0);
        }


        String filemode = currentSettings.getStrValue(Settings.STDIN_PATH);
        String path = currentSettings.getStrValue(Settings.DEFAULT_STDIN);

        if (path != null && !path.equals("")) {
            if (filemode.equals("absolute")) {
                defStdinFile = new File(path);
            } else if (filemode.equals("relative")) {
                defStdinFile = new File(System.getProperty("user.dir"), path);
            }
        }
        try {
            control.setDefaultStdIn(defStdinFile);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }


        filemode = currentSettings.getStrValue(Settings.STDOUT_PATH);
        path = currentSettings.getStrValue(Settings.DEFAULT_STDOUT);

        if (path != null && !path.equals("")) {
            if (filemode.equals("absolute")) {
                defStdoutFile = new File(path);
            } else if (filemode.equals("relative")) {
                defStdoutFile = new File(System.getProperty("user.dir"), path);
            }
        }
        try {
            control.setDefaultStdOut(defStdoutFile);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        int runmode = currentSettings.getIntValue(Settings.RUN_MODE);
        gui.setSelected(GUI.OPTION_RUNNING_COMMENTED, (runmode & COMMENTED) != 0);
        gui.setSelected(GUI.OPTION_RUNNING_PAUSED, (runmode & PAUSED) != 0);
        gui.setSelected(GUI.OPTION_RUNNING_ANIMATED, (runmode & ANIMATED) != 0);

        int compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);
        gui.setSelected(GUI.OPTION_COMPILING_COMMENTED, (compilemode & COMMENTED) != 0);
        gui.setSelected(GUI.OPTION_COMPILING_PAUSED, (compilemode & PAUSED) != 0);

        int memorysize = currentSettings.getIntValue(Settings.MEMORY_SIZE);
        if (memorysize != Control.DEFAULT_MEMORY_SIZE) {
            try {
                control.changeMemorySize(memorysize);
            } catch (IllegalArgumentException wrongsize) {
                control.changeMemorySize(Control.DEFAULT_MEMORY_SIZE);
            }
        }


        availableLanguages = new Hashtable<String, Locale>();
        findAvailableLanguages();

        String language = currentSettings.getStrValue(Settings.UI_LANGUAGE);

        if (availableLanguages.containsKey(language)) {
            Translator.setLocale((Locale) availableLanguages.get(language));
            //gui.updateAllTexts();
        }


        noPauses = false;
        interruptSent = false;
        // The settings have not really changed into anything
        // interesting at this point.
        //saveSettings();

        currentState = NONE;
    }


    /**
     * This method corresponds to the menu option File -> Open... It
     * calls either openBinaryFile or openSourceFile correspondingly.
     */
    public void menuOpenFile(File openedFile) {

        /* Opening a file is a command that interrupts all other tasks. */
        interruptCurrentTasks(true);

        String suffix = getExtension(openedFile);

        gui.resetAll();

        if (suffix.equals("b91")) {
            try {
                control.openBinary(openedFile);
            } catch (Exception e) {
                gui.showError(e.getMessage());
                return;
            }

            loadAndUpdateGUI();

        } else if (suffix.equals("k91")) {
            gui.resetAll();

            String k91Source = "";

            try {
                k91Source = control.openSource(openedFile);
            } catch (IOException e) {
                gui.showError(e.getMessage());
                return;
            }

            currentlyOpenedFile = openedFile;

            String[] src = k91Source.split("\n|\r|\r\n");
            gui.insertToCodeTable(src);
            gui.updateStatusBar(new Message("Opened a new k91 source " +
                    "file.").toString());
            gui.setGUIView(2);

            currentState = K91_NOT_COMPILING;
            setGUICommandsForCurrentState();


        } else { // if file extension isn't either b91 or k91
            gui.showError(new Message("File extension must be k91 or b91").toString());
        }
    }


    public static final int MIN_KBD_VALUE = Integer.MIN_VALUE; //,not -32767;
    public static final int MAX_KBD_VALUE = Integer.MAX_VALUE;//,not 32767;

    public boolean enterInput(String input) {
        int inputValue;
        String[] minAndMaxValues = {"" + MIN_KBD_VALUE, "" + MAX_KBD_VALUE};

        try {
            inputValue = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            gui.changeTextInEnterNumberLabel(new Message("Illegal input").toString());
            gui.updateStatusBar(new Message("Illegal input. You must insert a number between {0}...{1}", minAndMaxValues).toString());
            return false;
        }

        if (inputValue > MAX_KBD_VALUE || inputValue < MIN_KBD_VALUE) {
            gui.changeTextInEnterNumberLabel(new Message("Illegal input").toString());
            gui.updateStatusBar(new Message("Illegal input. You must insert a number between {0}...{1}", minAndMaxValues).toString());
            return false;
        }

        gui.changeTextInEnterNumberLabel("");
        gui.updateStatusBar("");

        control.keyboardInput(inputValue);
        return true;
    }


    /**
     * This method corresponds to the menu option File -> Run. It does
     * its work by calling runInstruction().
     */
    public  void menuRun() {
        synchronized(lock)
        {

        threadRunning = true;

        interruptSent = false;
        noPauses = false;

        /* If stdout file is set to be overwritten, then it must be emptied first.
           It's done here by deleting it first and then creating it again.
        */
        File stdoutFile = getCurrentDefaultStdoutFile();
        if (currentSettings.getStrValue(Settings.STDOUT_USE).equals("overwrite")) {
            try {
                if (stdoutFile.exists()) {
                    stdoutFile.delete();
                }
                stdoutFile.createNewFile();
            } catch (IOException e) {
                String[] filename = {stdoutFile.getName()};
                gui.showError(new Message("Error while emptying {0}", filename).toString());
                logger.warning(e.getMessage());
                //return; // Allow emptying to fail, since it may not mean anything.
            }
        }

        RunInfo runinfo;
        int runmode = currentSettings.getIntValue(Settings.RUN_MODE);

        int base = 0; //Register base is always 0
        int limit = currentSettings.getIntValue(Settings.MEMORY_SIZE); //Register limit is equal to memsize

        animator.init(control.getCpu(), base, limit);

        if ((runmode & ANIMATED) != 0) {
            gui.showAnimator();
        }

        do {
            currentState = B91_RUNNING;
            setGUICommandsForCurrentState();

            int nextLine = ((Processor) control.getCpu()).getValueOf(TTK91Cpu.CU_PC_CURRENT);
            gui.selectLine(nextLine, GUI.INSTRUCTIONS_AND_DATA_TABLE);

            runmode = currentSettings.getIntValue(Settings.RUN_MODE);

            try {
                runinfo = control.runLine();
                if (runinfo == null) {
                    break;
                }
            } catch (TTK91NoKbdData needMoreData) {
                /* GUI's KBD-frame is enabled and exectution cannot go on before user has entered
                   a number value there. GUI calls GUIBrain's enterInput()-method to send the number
                   ahead to Control.
                */
                gui.addComment(new Message("Enter a number in the text field above.").toString());
                gui.updateStatusBar(new Message("Waiting for KBD input...").toString());
                currentState = B91_WAIT_FOR_KBD;
                setGUICommandsForCurrentState();
                gui.enable(GUI.INPUT_FIELD);
                /* Wait until continueTask() is run. In this case it's done by pressing
                   enter-button below the text field where user enter kbd-data.
                */
                waitForContinueTask();
                gui.disable(GUI.INPUT_FIELD);
                continue; // And then go to beginning of the do-while-loop.

            } catch (TTK91RuntimeException e) {
                gui.updateStatusBar(new Message("Execution aborted due to an error").toString());
                gui.addComment(e.getMessage());
                currentState = INTERRUPTED_WITH_PAUSE;
                setGUICommandsForCurrentState();
                break;
            }

            if ((runmode & COMMENTED) != 0) {
                if (runinfo.getComments() != null) {
                    gui.addComment(runinfo.getLineNumber() + ": " + runinfo.getComments());
                }
            }

            animator.stopAnimation();
            animator.animate(runinfo);

            gui.updateStatusBar(runinfo.getComments());

            // If the command wrote something to screen, we'll deal with
            // the actual writing. STDOUT writes are dealt with in Control.
            if (runinfo.isExternalOp() && runinfo.whatOUT() != null) {
                // We use a Processor constant to check what device we are
                // writing to. CRT happens to be 0 now, but it might not be
                // "in the future".
                if (runinfo.whatOUT()[0] == Processor.CRT) {
                    gui.addOutputData(runinfo.whatOUT()[1]);
                }
            }

            int[] newRegisterValues = runinfo.getRegisters();
            gui.updateReg(GUI.R0, newRegisterValues[0]);
            gui.updateReg(GUI.R1, newRegisterValues[1]);
            gui.updateReg(GUI.R2, newRegisterValues[2]);
            gui.updateReg(GUI.R3, newRegisterValues[3]);
            gui.updateReg(GUI.R4, newRegisterValues[4]);
            gui.updateReg(GUI.R5, newRegisterValues[5]);
            gui.updateReg(GUI.R6, newRegisterValues[6]);
            gui.updateReg(GUI.R7, newRegisterValues[7]);
            gui.updateReg(GUI.PC, runinfo.getNewPC());

            LinkedList changedMemoryLines = runinfo.getChangedMemoryLines();
            Iterator changedMemoryLinesListIterator = changedMemoryLines.iterator();

            while (changedMemoryLinesListIterator.hasNext()) {
                Object[] listItem = (Object[]) changedMemoryLinesListIterator.next();
                int line = ((Integer) listItem[0]).intValue();
                MemoryLine contents = (MemoryLine) listItem[1];
                gui.updateInstructionsAndDataTableLine(line, contents.getBinary(), contents.getSymbolic());
            }

            gui.repaint();

            if ((runmode & LINE_BY_LINE) != 0 && noPauses == false) {
                currentState = B91_PAUSED;
                setGUICommandsForCurrentState();
                waitForContinueTask();

            } else {
                try {
                    if(SLOWDOWN>0)
                        lock.wait(SLOWDOWN); 
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException in menuRun()");
                }

            }

        } while (interruptSent == false); // End of do-while -loop

        if (currentState == INTERRUPTED_WITH_PAUSE) {
            setGUICommandsForCurrentState();
            waitForContinueTask();
        }

        load();
        currentState = B91_NOT_RUNNING;
        setGUICommandsForCurrentState();

        gui.unselectAll();

        gui.addComment(" ");
        gui.addComment(new Message("Execution finished").toString());
        gui.addComment(" ");

        continueTask();

        threadRunning = false;
        continueTask();
        }
    }

    /**
     * This method is used to save the source after it has been
     * modified in the code window.
     */
    public void saveSource() {
        try {
            control.modifySource(gui.getCodeTableContents());
        } catch (IOException e) {
            gui.showError(e.getMessage());
        }
    }


    /**
     * This method corresponds to the menu option File -> Compile. It
     * does its work by calling compileLine().
     */
    public void menuCompile() {
        synchronized(lock)
        {

        threadRunning = true;

        interruptSent = false;
        noPauses = false;

        currentState = K91_COMPILING;
        setGUICommandsForCurrentState();

        /* compileinfo is set to null now. Null as CompileInfo object means also that
           compilation has finished successfully. We may think that if no line was
           compiled then it would mean a successful compilation as well, so this isn't
           in contradiction to that anyway.
           Really this is set to null because we need compileinfo to be initialized somehow
           in compileLine() methods try-catch clause a few lines below.
        */
        CompileInfo compileinfo = null;

        int compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);
        int phase;

        /* This will be set to true once the compilation succeeds. The value
           of this variable will be used in case of an interrupted compilation
           or if an error occurs, when menuCompile() has to decide whether to
           change back to pre-compilation-started state or not.
        */
        boolean compilingCompleted = false;

        do {

            currentState = K91_COMPILING;
            setGUICommandsForCurrentState();

            try {
                compileinfo = control.compileLine();
            } catch (TTK91CompileException e) {
                /* This section is executed if an error occured during compilation. First
                   we preset errorLine and phase to what they would be, if there hasn't
                   been any compileinfo before.
                */
                int errorLine = 0;
                phase = CompileInfo.FIRST_ROUND;

                /* Then we check if there has been some compileinfos before and set errorLine
                   and phase accordingly if positive.
                */
                if (compileinfo != null) {
                    errorLine = compileinfo.getLineNumber() + 1;
                    phase = compileinfo.getPhase();
                }

                gui.addComment(errorLine + ": " + e.getMessage());
                gui.updateStatusBar(new Message("Compilation aborted due to an error").toString());

                if (phase == CompileInfo.FIRST_ROUND) {
                    gui.selectLine(errorLine, GUI.CODE_TABLE);
                } else if (phase == CompileInfo.SECOND_ROUND) {
                    gui.selectLine(errorLine, GUI.INSTRUCTIONS_AND_DATA_TABLE);
                }

                currentState = K91_PAUSED;
                setGUICommandsForCurrentState();
                waitForContinueTask();
                break;
            }

            /* Compilation is finished once compileLine() returns null */
            if (compileinfo == null) {
                compilingCompleted = true;
                break;
            } else {
                String comments = compileinfo.getComments();
                if (comments == null) {
                    comments = "";
                }

                if ((compilemode & COMMENTED) != 0 && !comments.equals("")) {
                    gui.addComment(compileinfo.getLineNumber() + ": " + comments);
                }

                gui.updateStatusBar(comments);

                compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);
                phase = compileinfo.getPhase();

                if (phase == CompileInfo.FIRST_ROUND) {
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
                } else if (phase == CompileInfo.FINALIZING_FIRST_ROUND) {
                    String[][] symbolTable = compileinfo.getSymbolTable();
                    if (symbolTable != null) {
                        for (int i = 0; i < symbolTable.length; i++) {
                            String symbolName = symbolTable[i][0];
                            Integer symbolValue = null;
                            try {
                                symbolValue = new Integer(symbolTable[i][1]);
                            } catch (NumberFormatException e) {
                            }
                            gui.updateRowInSymbolTable(symbolName, symbolValue);
                        }
                    }

                    String[] newInstructionsContents = compileinfo.getInstructions();
                    String[] newDataContents = compileinfo.getData();
                    gui.insertToInstructionsTable(newInstructionsContents);
                    gui.insertToDataTable(newDataContents);
                    gui.setGUIView(3);

                } else if (phase == CompileInfo.SECOND_ROUND) {
                    int line = compileinfo.getLineNumber();
                    int binary = compileinfo.getLineBinary();
                    gui.updateInstructionsAndDataTableLine(line, binary);
                    gui.selectLine(compileinfo.getLineNumber(), GUI.INSTRUCTIONS_AND_DATA_TABLE);
                }

                gui.repaint();

                if (((compilemode & PAUSED) != 0) && !comments.equals("") && noPauses == false) {
                    currentState = K91_PAUSED;
                    setGUICommandsForCurrentState();
                    waitForContinueTask();
                } else {
                    try {
                        lock.wait(SLOWDOWN + 1); // Add 1 to avoid the special meaning of 0.
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException in menuRun()");
                    }

                }
            }

        } while (interruptSent == false); // End of do-loop

        if (currentState == INTERRUPTED_WITH_PAUSE) {
            setGUICommandsForCurrentState();
            waitForContinueTask();
        }

        if (compilingCompleted == true) {
            try {
                control.saveBinary();
                System.out.println(new Message("Program saved to binary file!").toString());
            } catch (IOException e) {
                System.out.println(e);
            }
            gui.resetAll();
            gui.addComment(" ");
            gui.addComment(new Message("Compilation finished").toString());
            gui.addComment(" ");
            loadAndUpdateGUI();
        } else {
            /* Reload the source so that the compiling starts again from the beginning. */
            try {
                control.openSource(currentlyOpenedFile);
            } catch (IOException e) {
                gui.showError(e.getMessage());
                currentState = NONE;
                setGUICommandsForCurrentState();
                return;
            }
            currentState = K91_NOT_COMPILING;
            setGUICommandsForCurrentState();
            gui.setGUIView(2);
            gui.resetAll();
            gui.addComment(" ");
            gui.addComment(new Message("Compilation aborted.").toString());
            gui.addComment(" ");
            continueTask();
        }
        threadRunning = false;
        continueTask();
        }//lock
    }


    /**
     * This method corresponds to the menu option File -> Erase memory.
     */
    public void menuEraseMemory() {
        
        interruptCurrentTasks(true);
        synchronized(lock)
        {

        /* If there's no other thread running, then waiting for continueTask would
           be futile, since no one will ever notify this method to continue exectution.
           Actually, running waitForContinueTask() would mean dead lock then.
        */
        if (threadRunning == true) {
            waitForContinueTask();
        }

        control.eraseMemory();
        // The registers should be zeros now;
        gui.updateReg(GUI.R0, 0);
        gui.updateReg(GUI.R1, 0);
        gui.updateReg(GUI.R2, 0);
        gui.updateReg(GUI.R3, 0);
        gui.updateReg(GUI.R4, 0);
        gui.updateReg(GUI.R5, 0);
        gui.updateReg(GUI.R6, 0);
        gui.updateReg(GUI.R7, 0);
        gui.updateReg(GUI.PC, 0);
        gui.resetAll();

        gui.updateStatusBar(new Message("Memory contents erased").toString());

        gui.setGUIView(1);
        currentState = NONE;
        setGUICommandsForCurrentState();
        display.setMem(control.getPhysicalMemory());
        }//lock
    }


    /**
     * This method corresponds to the menu option File -> Exit.
     */
    public void menuExit() {
    }


    /**
     * This method corresponds to the menu option Option -> Set language.
     * If the chosen language is one in the list, then this version is called.
     *
     * @param language Name of the language. This should be one of those get
     *                 from getAvailableLanguages() method.
     */
    public void menuSetLanguage(String language) {

        if (availableLanguages.containsKey(language)) {
            Translator.setLocale((Locale) availableLanguages.get(language));
            currentSettings.setValue(Settings.UI_LANGUAGE, language);
            saveSettings();
            gui.updateAllTexts();
        }
    }


    /**
     * This method correspods as well to the menu option Option -> Set language.
     * This version is called, if user has chosen an external language file.
     *
     * @param languageFile The language file. This must be class-file that
     *                     extends ListResourceBundle.
     */
    public void menuSetLanguage(File languageFile) {

        if (languageFile.exists()) {
            try {
                Translator.setLocale(Locale.CHINESE, control.loadLanguageFile(languageFile));
            } catch (ResourceLoadFailedException e) {
                gui.showError(new Message("Not a language file").toString());
                System.out.println(e.getMessage());
                return;
            }
            //currentSettings.setValue(Settings.UI_LANGUAGE, language);
            //control.saveSettings(currentSettings.toString(), settingsFile);
            gui.updateAllTexts();
        }
    }


    /**
     * This method corresponds to the menu option Option -> Set Default Stdin File.
     * It informs Control about the new default stdin file and saves it to
     * settingsFile.
     */
    public void menuSetStdin(File stdinFile) {
        try {
            control.setDefaultStdIn(stdinFile);
        } catch (Exception e) {
            gui.showError(e.getMessage());
            return;
        }
        String filename = stdinFile.getPath();
        currentSettings.setValue(Settings.DEFAULT_STDIN, filename);
        currentSettings.setValue(Settings.STDIN_PATH, "absolute");
        saveSettings();

        gui.addComment(new Message("Default stdin file set to {0}", filename).toString());
    }


    /**
     * This method corresponds to the menu option Option -> Set Default Stdout File.
     * It informs Control about the new default stdout file and saves it to
     * settingsFile.
     */
    public void menuSetStdout(File stdoutFile, boolean append) {
        String filename = stdoutFile.getPath();

        if (append == true) {
            currentSettings.setValue(Settings.STDOUT_USE, "append");
        } else {
            currentSettings.setValue(Settings.STDOUT_USE, "overwrite");
        }
        try {
            control.setDefaultStdOut(stdoutFile);
        } catch (Exception e) {
            gui.showError(e.getMessage());
            //return; // Allow unwriteable stdout file; the exception'll hit later anyway.
        }
        currentSettings.setValue(Settings.DEFAULT_STDOUT, filename);
        currentSettings.setValue(Settings.STDOUT_PATH, "absolute");
        saveSettings();

        gui.addComment(new Message("Default stdout file set to {0}", filename).toString());
    }


    /**
     * This method corresponds to the menu option Option -> Set Memory Size.
     */
    public void menuSetMemorySize(int newSize) {
        try {
            control.changeMemorySize(newSize);
        } catch (IllegalArgumentException e) {
            return;
        }
        currentSettings.setValue(Settings.MEMORY_SIZE, newSize);
        saveSettings();
        gui.setGUIView(1);
        currentState = NONE;
        setGUICommandsForCurrentState();
    }


    /**
     * This methods refreshes GUI so that it shows running options as they
     * are declared currently.
     */
    public void refreshRunningOptions() {
        int runmode = currentSettings.getIntValue(Settings.RUN_MODE);
        gui.setSelected(GUI.OPTION_RUNNING_PAUSED, (runmode & PAUSED) != 0);
        gui.setSelected(GUI.OPTION_RUNNING_COMMENTED, (runmode & COMMENTED) != 0);
        gui.setSelected(GUI.OPTION_RUNNING_ANIMATED, (runmode & ANIMATED) != 0);
    }

    public void menuSetRunningOption(int option, boolean b) {
        int runmode = currentSettings.getIntValue(Settings.RUN_MODE);
        boolean adjustAnimationOff = false, adjustLineByLineOn = false;

        // First, update running mode. If the option LINE_BY_LINE was turned
        // off, turn ANIMATED off. If the option ANIMATED was turned on,
        // turn LINE_BY_LINE on.
        if (((runmode & option) != 0) == b) {
            // do nothing
        } else if ((runmode & option) != 0) {
            runmode -= option; // Something was turned off.
            // Was it LINE_BY_LINE and is animation on?
            if (option == LINE_BY_LINE && (runmode & ANIMATED) != 0) {
                adjustAnimationOff = true;
            }
        } else if ((runmode & option) == 0) {
            runmode += option; // Something was turned on.
            // Was it ANIMATED and is line-by-line off?
            if (option == ANIMATED && (runmode & LINE_BY_LINE) == 0) {
                adjustLineByLineOn = true;
            }
        }

        currentSettings.setValue(Settings.RUN_MODE, runmode);
        saveSettings();

        switch (option) {
            case LINE_BY_LINE: // Synonym for case PAUSED:
                gui.setSelected(GUI.OPTION_RUNNING_PAUSED, b);
                break;
            case COMMENTED:
                gui.setSelected(GUI.OPTION_RUNNING_COMMENTED, b);
                break;
            case ANIMATED:
                gui.setSelected(GUI.OPTION_RUNNING_ANIMATED, b);
                if (b == true && (currentState == B91_RUNNING
                        || currentState == B91_PAUSED
                        || currentState == B91_WAIT_FOR_KBD)) {
                    gui.showAnimator();
                } else {
                    gui.hideAnimator();
                }
                break;
        }
        // Finally, we repeat the process to a) turn animation off if line-by-line
        // running was turned off, and b) turn line-by-line running on if animation
        // was turned on. Note the lack of infinite looping which we are proud of.
        if (adjustAnimationOff) {
            menuSetRunningOption(ANIMATED, false);
        }
        if (adjustLineByLineOn) {
            menuSetRunningOption(LINE_BY_LINE, true);
        }

    }


    /**
     * This methods refreshes GUI so that it shows compiling options as they
     * are declared currently.
     */
    public void refreshCompilingOptions() {
        int compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);
        gui.setSelected(GUI.OPTION_COMPILING_PAUSED, (compilemode & PAUSED) != 0);
        gui.setSelected(GUI.OPTION_COMPILING_COMMENTED, (compilemode & COMMENTED) != 0);
    }


    /**
     * This method
     */
    public void menuSetCompilingOption(int option, boolean b) {
        int compilemode = currentSettings.getIntValue(Settings.COMPILE_MODE);

        if (((compilemode & option) != 0) == b) {
            // do nothing
        } else if ((compilemode & option) != 0) {
            compilemode -= option;
        } else if ((compilemode & option) == 0) {
            compilemode += option;
        }

        currentSettings.setValue(Settings.COMPILE_MODE, compilemode);
        saveSettings();

        switch (option) {
            case PAUSED:
                gui.setSelected(GUI.OPTION_COMPILING_PAUSED, b);
                break;
            case COMMENTED:
                gui.setSelected(GUI.OPTION_COMPILING_COMMENTED, b);
                break;
        }
    }


    public void menuAbout() {
    }


    public void menuManual() {
    }


    /**
     * This method corresponds to a request to interrupt whatever we
     * were doing once it becomes possible.
     *
     * @param immediate If this is true, then continueTask is being waited before
     *                  the previous job ends.
     *                  If this is false, then it stops immediately and next job
     *                  can start right after calling this.
     */
    public void menuInterrupt(boolean immediate) {
        gui.updateStatusBar(new Message("Current operation aborted").toString());
        interruptCurrentTasks(immediate);
    }


    /**
     * Notifies all currents tasks to be interrupted once they are able to read the
     * new value of interruptSent. Immediate interruption means that all tasks should
     * end without any further activities, while non-immediate interruption means
     * that some tasks may pause to wait for continueTask() to notify them before
     * ending completely.
     *
     * @param immediate If this is true, then continueTask is being waited before
     *                  the previous job ends.
     *                  If this is false, then it stops immediately and next job
     *                  can start right after calling this.
     */
    private void interruptCurrentTasks(boolean immediate) {
        if (immediate == true) {
            currentState = INTERRUPTED_WITHOUT_PAUSE;
        } else {
            currentState = INTERRUPTED_WITH_PAUSE;
        }

        interruptSent = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }


    /**
     * Notifies all methods,that have called waitForContinueTask() to continue
     * their operation.
     */
    public void continueTask() {
        synchronized (lock) {
            lock.notify();
        }
        return;
    }


    /**
     * Notifies all methods, that have called waitForContinueTask() to continue
     * their operation plus informs them that waitForContinueTask() should no
     * longer be called during current operation.
     */
    public void continueTaskWithoutPauses() {
        noPauses = true;
        synchronized (lock) {
            lock.notify();
        }
        return;
    }


    /**
     * A method can call this, if it wants enter into pause mode and wait for someone
     * to call continueTask() or continueTaskWithoutPauses() methods. This method cannot
     * however be used, unless the method which is calling this hasn't been set to run
     * in a thread of its own. eg. by calling new GUIThreader()
     */
    public void waitForContinueTask() {

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            }
        }
        return;
    }


    /**
     * Returns all available languages. These (and only these) can be used as parameter
     * for setLanguge(String) method.
     */
    public String[] getAvailableLanguages() {
        Object[] keys = availableLanguages.keySet().toArray();
        String[] str = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            str[i] = (String) keys[i];
        }
        return str;
    }


// Private methods. ---------------------------------------------------


    /**
     * Makes sure that currentSettings contains at least the default values for each
     * key, if they cannot be obtained from settingsFile.
     */
    private void getCurrentSettings() throws IOException {
        String defaultStdinFile = System.getProperty("user.dir") + "/stdin";
        String defaultStdinPath = "absolute";
        String defaultStdoutFile = System.getProperty("user.dir") + "/stdout";
        String defaultStdoutPath = "absolute";
        String defaultStdoutUse = "overwrite";
        int defaultMemorySize = Control.DEFAULT_MEMORY_SIZE;
        String defaultUILanguage = "English";
        int defaultRunningMode = 0;
        int defaultCompilingMode = 0;
        boolean useDefaultSettings = false;
        InputStream defaultSettingsStream = null;

        //URI fileURI;

        //try {
        //  fileURI = new URI( getClass().getClassLoader().getResource(programPath).toString() + "etc/settings.cfg" );
        //  settingsFile = new File(fileURI);
        settingsFile = new File(System.getProperty("user.home"), "titokone.cfg");
        //}
        //catch (Exception e) {
        //  System.out.println("Main path not found!...exiting");
        //  System.exit(0);
        //}

        // First, check if the user has a settings file. Otherwise we'll use the default
        // file in <the class directory>etc/settings.cfg.
        if (settingsFile.exists() == false) {
            useDefaultSettings = true;
            // (Creating a new settings file will not be necessary; the settings saving
            // will take care of it. In fact, we do not want to uselessly create an empty
            // settings file to confuse us on the next run. But we will want to check
            // if we are allowed to.)
            settingsFile.createNewFile(); // May throw an IOException.
            settingsFile.delete(); // May throw an IOException, hardly if createNewFile didn't.
        }

        String settingsFileContents = "";
        try {
            if (!useDefaultSettings) { // Use user's own settings file.
                settingsFileContents = control.loadSettingsFileContents(settingsFile);
                logger.info("Used user's own settings file.");
            } else { // Use default settings file.
                ClassLoader loader = getClass().getClassLoader();
                defaultSettingsStream = loader.getResourceAsStream(programPath +
                        "etc/settings.cfg");
                settingsFileContents = control.loadSettingsStreamContents(defaultSettingsStream);
                logger.info("Used default settings file.");
            }
        } catch (IOException e) {
            logger.warning("Error while reading " +
                    (useDefaultSettings ? "default" : "user's") +
                    " settings file.");
            throw e;
        }

        try {
            currentSettings = new Settings(settingsFileContents);
        } catch (Exception e) {
            logger.warning("Parse error in settings file, using hard-coded defaults.");
            currentSettings = new Settings(); // Create an empty settings instance.
        }


        try {
            if (currentSettings.getStrValue(Settings.STDIN_PATH) == null) {
                currentSettings.setValue(Settings.STDIN_PATH, defaultStdinPath);
            }
        } catch (Exception e) {
            currentSettings.setValue(Settings.STDIN_PATH, defaultStdinPath);
        }

        try {
            if (currentSettings.getStrValue(Settings.DEFAULT_STDIN) == null) {
                currentSettings.setValue(Settings.DEFAULT_STDIN, defaultStdinFile);
            }
        } catch (Exception e) {
            currentSettings.setValue(Settings.DEFAULT_STDIN, defaultStdinFile);
        }

        try {
            if (currentSettings.getStrValue(Settings.STDOUT_PATH) == null) {
                currentSettings.setValue(Settings.STDOUT_PATH, defaultStdoutPath);
            }
        } catch (Exception e) {
            currentSettings.setValue(Settings.STDOUT_PATH, defaultStdoutPath);
        }

        try {
            if (currentSettings.getStrValue(Settings.DEFAULT_STDOUT) == null) {
                currentSettings.setValue(Settings.DEFAULT_STDOUT, defaultStdoutFile);
            }
        } catch (Exception e) {
            currentSettings.setValue(Settings.DEFAULT_STDOUT, defaultStdoutFile);
        }

        try {
            if (currentSettings.getStrValue(Settings.STDOUT_USE) == null) {
                currentSettings.setValue(Settings.STDOUT_USE, defaultStdoutUse);
            }
        } catch (Exception e) {
            currentSettings.setValue(Settings.STDOUT_USE, defaultStdoutUse);
        }

        try {
            currentSettings.getIntValue(Settings.MEMORY_SIZE);
        } catch (Exception e) {
            currentSettings.setValue(Settings.MEMORY_SIZE, defaultMemorySize);
        }

        try {
            if (currentSettings.getStrValue(Settings.UI_LANGUAGE) == null) {
                currentSettings.setValue(Settings.UI_LANGUAGE, defaultUILanguage);
            }
        } catch (Exception e) {
            currentSettings.setValue(Settings.UI_LANGUAGE, defaultUILanguage);
        }

        try {
            currentSettings.getIntValue(Settings.RUN_MODE);
        } catch (Exception e) {
            currentSettings.setValue(Settings.RUN_MODE, defaultRunningMode);
        }

        try {
            currentSettings.getIntValue(Settings.COMPILE_MODE);
        } catch (Exception e) {
            currentSettings.setValue(Settings.COMPILE_MODE, defaultCompilingMode);
        }
    }


    /**
     * This just loads the opened b91-program into Titokone's memory without updating
     * GUI anyway. However, GUI is told to show an error message, if the loaded program
     * is too large and thus Titokone is out of memory.
     *
     * @return LoadInfo object, which contains information about the loading process.
     */
    private LoadInfo load() {

        LoadInfo loadinfo;
        try {
            loadinfo = control.load();
        } catch (TTK91AddressOutOfBounds e) {
            gui.showError(new Message("Titokone out of memory").toString());
            return control.getPendingLoadInfo(); // This is null; load really failed.
        } catch (TTK91NoStdInData e) {
            File[] appDefs = control.getApplicationDefinitions();
            String[] stdinFilePath = {getCurrentDefaultStdinFile().getPath()};
            if (appDefs[Control.DEF_STDIN_POS] != null) {
                stdinFilePath[0] = appDefs[Control.DEF_STDIN_POS].getPath();
            }

            //gui.showError(new Message("Stdin file {0} is not in valid format or it doesn't exist", stdinFilePath).toString());
            //gui.addComment(e.getMessage()); // The message is already translated.
            return control.getPendingLoadInfo(); // This is != null; load succeeded.
        }

        return loadinfo;

    }


    /**
     * Load the program into Titokone's memory and update's GUI to show the new memory contents
     * and register values and such.
     */
    private void loadAndUpdateGUI() {
        LoadInfo loadinfo = load();

        if (loadinfo != null) {
            gui.updateStatusBar(loadinfo.getStatusMessage());
            gui.updateReg(GUI.SP, loadinfo.getSP());
            gui.updateReg(GUI.FP, loadinfo.getFP());

            String[][] symbolsAndValues = loadinfo.getSymbolTable();
            gui.insertSymbolTable(symbolsAndValues);

            int instructionsBinary[] = loadinfo.getBinaryCommands();
            String instructionsSymbolic[] = loadinfo.getSymbolicCommands();

            int dataBinary[] = loadinfo.getData(); // Full memory excepting code area.
            // The symbolic data depends on two things: The Loader should add
            // symbolic versions of the initial data area, and the RandomAccessMemory
            // should add NOPs on 0 lines. The latter does not currently happen,
            // but can be changed if it is deemed better.
            String dataSymbolic[];
            // Only show symbolic forms of the initial data area, not below it?
            if (GUIBrain.ENABLE_DATA_AREA_MARKUP) {
                dataSymbolic = loadinfo.getDataAreaSymbolic();
            } else {
                dataSymbolic = loadinfo.getDataSymbolic();
            }
            gui.insertToInstructionsTable(instructionsBinary, instructionsSymbolic);
            gui.insertToDataTable(dataBinary, dataSymbolic);

            gui.addComment(loadinfo.getComments());

            currentState = B91_NOT_RUNNING;
            setGUICommandsForCurrentState();
            gui.setGUIView(3);
            display.setMem(control.getPhysicalMemory());
            
        }
    }


    /**
     * Saves currentSettings to settingsFile.
     */
    private void saveSettings() {
        try {
            control.saveSettings(currentSettings.toString(), settingsFile);
        } catch (IOException e) {
            String[] name = {settingsFile.getName()};
            gui.showError(new Message("{0} is inaccessible", name).toString());
        }
    }


    /**
     * This method returns the default stdout file, which is the one declared in currentSettings.
     */
    private File getCurrentDefaultStdoutFile() {

        File currentStdoutFile = new File(System.getProperty("user.dir") + DEFAULT_STDOUT_FILENAME);

        String filemode = currentSettings.getStrValue(Settings.STDOUT_PATH);
        String path = currentSettings.getStrValue(Settings.DEFAULT_STDOUT);

        if (path != null && !path.equals("")) {
            if (filemode.equals("absolute")) {
                currentStdoutFile = new File(path);
            } else if (filemode.equals("relative")) {
                currentStdoutFile = new File(System.getProperty("user.dir") + path);
            }
        }

        return currentStdoutFile;
    }


    /**
     * This method returns the default stdin file, which is the one declared in currentSettings.
     */
    private File getCurrentDefaultStdinFile() {

        File currentStdinFile = new File(System.getProperty("user.dir"), DEFAULT_STDIN_FILENAME);

        String filemode = currentSettings.getStrValue(Settings.STDIN_PATH);
        String path = currentSettings.getStrValue(Settings.DEFAULT_STDIN);

        if (path != null && !path.equals("")) {
            if (filemode.equals("absolute")) {
                currentStdinFile = new File(path);
            } else if (filemode.equals("relative")) {
                currentStdinFile = new File(System.getProperty("user.dir"), path);
            }
        }

        return currentStdinFile;
    }


    /**
     * This method determines the available languages. It reads them from
     * a setup file languages.cfg, which contains lineseparator-delimited
     * sets of language-name, language-id, (country), eg.
     * "Finnish, fi", or "English (GB), en, GB".
     */
    private void findAvailableLanguages() {
        InputStream fileStream = null;
        //URI fileURI;
        //File languageFile = null;

        //try {
        //fileURI = new URI( getClass().getClassLoader().getResource(programPath).toString() + "etc/languages.cfg" );
        //languageFile = new File(fileURI);
        ClassLoader loader = getClass().getClassLoader();
        fileStream = loader.getResourceAsStream(programPath + "etc/languages.cfg");
        //}
        //catch (Exception e) {
        //  System.out.println("Main path not found!...exiting");
        //  System.exit(0);
        //}

        String languageName, language, country, variant;

        if (fileStream != null) {
            String languageFileContents;
            try {
                languageFileContents = control.loadSettingsStreamContents(fileStream);
            } catch (IOException e) {
                logger.warning("IOException in language settings file.");
                return;
            }

            String[] languageFileRow = languageFileContents.split("\n|\r|\r\n");

            /* Split each row of language.cfg into separate strings and
               tokenize these strings by a colon. If there are two or three
               tokens on each row, then everything goes well. Otherwise
               the language.cfg is not a proper language file for this program.
            */
            for (int i = 0; i < languageFileRow.length; i++) {
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
                } else if (token.length == 2) {
                    language = token[0].trim();
                    country = token[1].trim();
                    availableLanguages.put(languageName, new Locale(language, country));
                } else if (token.length == 3) {
                    language = token[0].trim();
                    country = token[1].trim();
                    variant = token[2].trim();
                    availableLanguages.put(languageName, new Locale(language, country, variant));
                } else {
                    System.out.println("Parse error in language file");
                }
            }
        }
    }


    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }


    /**
     * Sets GUI to correspond the current state of program, which means that some
     * buttons should be enables while others not.
     */
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
                gui.enable(GUI.CODE_TABLE_EDITING);
                break;

            case K91_COMPILING:
                gui.disable(GUI.COMPILE_COMMAND);
                gui.disable(GUI.RUN_COMMAND);
                gui.disable(GUI.CONTINUE_COMMAND);
                gui.disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
                gui.enable(GUI.STOP_COMMAND);
                gui.disable(GUI.CODE_TABLE_EDITING);
                break;

            case K91_PAUSED:
                gui.disable(GUI.COMPILE_COMMAND);
                gui.disable(GUI.RUN_COMMAND);
                gui.enable(GUI.CONTINUE_COMMAND);
                gui.enable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
                gui.enable(GUI.STOP_COMMAND);
                gui.disable(GUI.CODE_TABLE_EDITING);
                break;

            case INTERRUPTED_WITH_PAUSE:
                gui.disable(GUI.COMPILE_COMMAND);
                gui.disable(GUI.RUN_COMMAND);
                gui.enable(GUI.CONTINUE_COMMAND);
                gui.enable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
                gui.disable(GUI.STOP_COMMAND);
                break;

        }
    }
}
