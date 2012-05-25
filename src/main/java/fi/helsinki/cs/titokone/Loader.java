package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.TTK91AddressOutOfBounds;

/**
 * This class can load a TTK91Application. It changes the processor
 * state accordingly. Everything is loaded when loadApplication is
 * called. Function returns the state of memory after loading. If it
 * runs out of memory it throws a TTK91AddressOutOfBounds exception.
 */
public class Loader {

    /**
     * This variable holds the current application to be loaded.
     */
    private Application application;


    private Processor processor;

    private BinaryInterpreter binInterpreter;

    public Loader(Processor processor) {
        String errorMessage;
        if (processor == null) {
            errorMessage = new Message("Null is an invalid parameter," +
                    " instance of {0} required.",
                    Processor.class.getName()).toString();
            throw new IllegalArgumentException(errorMessage);
        }
        this.processor = processor;
        binInterpreter = new BinaryInterpreter();
    }

    /**
     * You can set the file to load. Each time an application is set to load, the counter is set to
     * one.
     */
    public void setApplicationToLoad(Application application) {
        String errorMessage;
        if (application == null) {
            errorMessage = new Message("Null is an invalid parameter," +
                    " instance of {0} required.",
                    Application.class.getName()).toString();
            throw new IllegalArgumentException(errorMessage);
        }
        this.application = application;
    }

    /**
     * Loads an application to memory. LoadInfo contains all the needed information about the process.
     *
     * @return Info from the load procedure, null if no application has been set for loading.
     */
    public LoadInfo loadApplication() throws TTK91AddressOutOfBounds {
        int bin;
        String[] messageParameters = new String[2];
        if (application == null) {
            return null;
        }

        MemoryLine[] code = application.getCode();
        MemoryLine[] data = application.getInitialData();
        // Add the symbolic values for the initial data area.
        for (int i = 0; i < data.length; i++) {
            if (data[i].getSymbolic().equals("")) {
                bin = data[i].getBinary();
                data[i] = new MemoryLine(bin, binInterpreter.binaryToString(bin));
            }
        }

        int FP = code.length - 1;
        int SP = code.length + data.length - 1;

        int i;
        for (i = 0; i < code.length; i++) {
            processor.memoryInput(i, code[i]);   //May throw TTK91AddressOutOfBounds, but it's just thrown
        }                                      //backwards

        for (int j = 0; j < data.length; j++) {
            processor.memoryInput(i + j, data[j]); //May throw TTK91AddressOutOfBounds, but it's just thrown
        }                                      //backwards

        messageParameters[0] = "" + FP;
        messageParameters[1] = "" + SP;

        MemoryLine[] wholeMemoryDump = ((RandomAccessMemory) processor.getPhysicalMemory()).getMemoryLines();
        int wholeDataAreaLength = wholeMemoryDump.length - code.length;
        MemoryLine[] wholeDataArea = new MemoryLine[wholeDataAreaLength];

        for (int k = 0; k < wholeDataAreaLength; k++) {
            wholeDataArea[k] = wholeMemoryDump[code.length + k];
        }


        LoadInfo retValue = new LoadInfo(code,
                wholeDataArea,
                application.getSymbolTable(),
                SP,
                FP,
                new Message("Program loaded into memory. FP set to {0}" +
                        " and SP to {1}.",
                        messageParameters).toString());

        processor.runInit(SP, FP);

        return retValue;
    }

}
