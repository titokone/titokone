package fi.hu.cs.titokone;

import java.text.ParseException;
import java.util.HashMap;

/** This class keeps track of the settings. It can parse and save settings
    file content. It provides support for a standard set of settings, but
    basically anything can be stored in it. Keys used in this file cannot
    contain KEY_VALUE_SEPARATOR. */
public class Settings { 
    private HashMap settings;
    
    /** This string separates keys from values in the settings file. 
	It cannot be included in the key strings. */
    public static final String KEY_VALUE_SEPARATOR = ":=";
    
    /** This is one of the default settings keys of values that can be 
	stored here. */
    public static final String UI_LANGUAGE = "Language";
    public static final String RUN_MODE = "Running mode";
    public static final String DEFAULT_STDIN = "Stdin file";
    public static final String DEFAULT_STDOUT = "Stdout file";
    public static final String MEMORY_SIZE = "Memory size";

    /** This constructor sets up a settings class with default values. 
	The settingsFileContent is parsed with the help fo the 
	parseSettingsFile() method.
	@param settingsFileContent A String containing what has been in 
	a settings file for parsing.
	@throws ParseException If the settings text was not syntactically
	correct. */
    public Settings(String settingsFileContent) throws ParseException {}

    /** This method sets a key to a certain value. 
	@param key The key to point to the value.
	@param value The value to be stored. 
	@throws IllegalArgumentException If the given key contains 
	KEY_VALUE_SEPARATOR. */
    public void setValue(String key, Object value) {}

    /** This method sets a key to a certain value. 
	@param key The key to point to the value.
	@param value The value to be stored. 
	@throws IllegalArgumentException If the given key contains 
	KEY_VALUE_SEPARATOR. */
    public void setValue(String key, int value) {}

    /** This method returns the value of a certain key. It will try to 
	cast it to an integer before returning. 
	@param key The key pointing to the value to be returned. 
	@return The value the key points to, cast to an int. 
	@throws ClassCastException If the value was not an int. */
    public int getIntValue(String key) {}
    
    /** This method returns the value of a certain key. It will try to 
	cast it to an integer before returning. 
	@param key The key pointing to the value to be returned. 
	@return The value the key points to, cast to a String. 
	@throws ClassCastException If the value was not a String. */
    public String getStrValue(String key) {}

    /** This method returns the value of a certain key. It will try to 
	cast it to an integer before returning. 
	@param key The key pointing to the value to be returned. 
	@return The value the key points to. */
    public Object getValue(String key) {}

    /** This method transforms this settings class into a format which
	can be parsed by parseSettingsFile. 
	@return String containing the non-fixed data in this class. */
    public String toString() {}

    /** This method parses the fileContent string and sets up the settings
	HashMap values accordingly. 
	@param fileContent A String containing what has been in 
	a settings file for parsing.
	@throws ParseException If the settings text was not syntactically
	correct. */
    private parseSettingsFile(String fileContent) throws ParseException {}

}
