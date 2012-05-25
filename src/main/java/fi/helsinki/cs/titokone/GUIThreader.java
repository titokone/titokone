package fi.helsinki.cs.titokone;

import java.io.File;

public class GUIThreader implements Runnable {
    private short task;
    private GUIBrain brains;
    public static final short TASK_OPEN_FILE = 1;
    public static final short TASK_COMPILE = 2;
    public static final short TASK_RUN = 3;
    public static final short TASK_CONTINUE = 4;
    public static final short TASK_ENTER_NUMBER = 5;
    public static final short TASK_STOP = 6;

    private Object param1;

    public GUIThreader(short methodToRun, GUIBrain brains) {
        this.task = methodToRun;
        this.brains = brains;
    }

    public GUIThreader(short methodToRun, GUIBrain brains, Object param1) {
        this.task = methodToRun;
        this.brains = brains;
        this.param1 = param1;
    }


    public void run() throws ClassCastException {
        switch (task) {
            case TASK_OPEN_FILE:
                brains.menuOpenFile((File) param1);
                break;

            case TASK_COMPILE:
                brains.menuCompile();
                break;

            case TASK_RUN:
                brains.menuRun();
                break;

            case TASK_CONTINUE:
                brains.continueTask();
                break;

            case TASK_STOP:
                brains.menuInterrupt(false);
                break;

            case TASK_ENTER_NUMBER:
                brains.enterInput((String) param1);
                break;
        }
        /// case ...// etc
    }
}

// Käyttö: GUI:ssa new GUIThreader(GUIThreader.TASK_SHOW_HELP, myGuiBrain).run()
