/** This class keeps track of the settings. It can parse and save settings
    files. */
public class Settings { 

	/**You can set the language of user interface. 
	*/
public void setLanguage(String newLanguage){}

	/**
            @return Current language of the user interface.
        */
public String getLanguage(){}


	/**You can set the running mode (one command at a time, with comments etc.)
	*/
public void setRunMode(String newMode){}

	/** Returns the current language.
            @return Current language.
        */
public String getRunMode(){}


	/**You can set the file that the simulator uses to emulate disc input with.
	*/
public void setInputFile(String newInputFileName){}

	/** 
            @return Name of the file that emulates disc input.
        */
public String getInputFile(){}


	/**You can set the file that the simulator uses to emulate disc output with.
	*/
public void setOutputFile(String newOutputFileName){}

	/**	
	    @return Name of the file that emulates disc output.
	*/
public String getOutputFile(){}


	/**You can set the file where the settings are saved to.
        */
public void setSettingsFile(String newSettingsFile){}

	/** 
            @return The name of the file where settings are saved to.
        */
public String getSettingsFile(){}


	/**You can set the new size for the memory.
	*/
public void setMemorysize(int newMemorySize){}

	/** 
            @return Current memory size.
        */
public int getMemorysize(){}

}
