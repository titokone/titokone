package fi.hu.cs.titokone;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.logging.Logger;

/** This class keeps track of the settings. It can parse and save settings
    file content. It provides support for a standard set of settings, but
    basically anything can be stored in it. Keys used in this file cannot
    contain KEY_VALUE_SEPARATOR. Whitespace around KEY_VALUE_SEPARATOR is 
    ignored. */
public class Settings { 
    private HashMap settings;
    
    /** This string separates keys from values in the settings file. 
	It cannot be included in the key strings. */
    public static final String KEY_VALUE_SEPARATOR = "=";
    /** This field stores the comment marker to put before any comment 
	lines. */
    private static final String COMMENT_MARKER = "#";
    
    /** This is one of the default settings keys of values that can be 
	stored here. */
    public static final String UI_LANGUAGE = "Language";
    public static final String RUN_MODE = "Running mode";
    public static final String COMPILE_MODE = "Compilation mode";
    public static final String DEFAULT_STDIN = "Stdin file";
    public static final String STDIN_PATH = "Stdin path"; 
    public static final String DEFAULT_STDOUT = "Stdout file";
    public static final String STDOUT_USE = "Stdout use";
    public static final String STDOUT_PATH = "Stdout path";
    public static final String MEMORY_SIZE = "Memory size";

    /** This constructor sets up a settings class with default values. 
	The settingsFileContent is parsed with the help fo the 
	parseSettingsFile() method.
	@param settingsFileContent A String containing what has been in 
	a settings file for parsing.
	@throws ParseException If the settings text was not syntactically
	correct. */
    public Settings(String settingsFileContent) throws ParseException {
	settings = new HashMap();
	if(settingsFileContent != null) 
	    parseSettingsFile(settingsFileContent);
    }

    /** This method sets a key to a certain string value. 
	@param key The key to point to the value.
	@param value The value to be stored. 
	@throws IllegalArgumentException If the given key contains 
	KEY_VALUE_SEPARATOR or the value contains a linebreak in the 
	middle (in the end it does not count), or if either are null. */
    public void setValue(String key, String value) {
	String expMessage;
	String[] parameters = new String[3];

	parameters[0] = new Message("value").toString();
	// Check that the value string does not contain linebreaks
	// in other positions than its end. To check that, match against
	// a pattern of 1 or more characters (there is a start of a line
	// in the start of each String, hence ignore the first character),
	// then 1 or more periods where there is at least one start of
	// line (\n\n would match ^^) and then 0 or more other characters.
	// We assume here that since Pattern.MULTILINE mode is on 
	// by default now, it will be on in other places as well.
	if(value.matches(".+[^+&&.*]+")) { 
	    parameters[1] = value;
	    parameters[2] = new Message("a linebreak");
	    expMessage = new Message("Illegal {0} \"{1}\", contains {2}.",
				     parameters).toString();
	    throw new IllegalArgumentException(expMessage);
	}
	if(value == null) {
	    expMessage = new Message("Illegal {0}: null. Try an empty " +
				     "string instead.", 
				     parameters).toString();
	    throw new IllegalArgumentException(expMessage);
	}

	doSetValue(key, value);
    }

    /** This method sets a key to a certain integer value. 
	@param key The key to point to the value.
	@param value The value to be stored. */
    public void setValue(String key, int value) {
	doSetValue(key, new Integer(value));
    }

    /** This method sets a key to a given object value. It checks the
	key's validity, but assumes that the Object is representable 
	as a valid string.
	@param key The possibly invalid key to point to the value.
	@param value The value to be stored. 
	@throws IllegalArgumentException If the key contains 
	KEY_VALUE_SEPARATOR or is null. */
    private void doSetValue(String key, Object value) {
	String[] parameters = new String[3];
	parameters[0] = new Message("key").toString();
    	// The regular expression below matches any character (.) 
	// 0 or more times (*) followed by a KEY_VALUE_SEPARATOR, 
	// followed by any character 0 or more times.
	if(key.matches(".*" + KEY_VALUE_SEPARATOR + ".*")) {
	    parameters[1] = key;
	    parameters[2] = "'" + KEY_VALUE_SEPARATOR + "'";
	    expMessage = new Message("Illegal {0} \"{1}\", contains {2}.",
				     parameters).toString();
	    throw new IllegalArgumentException(expMessage);
	}
	if(key == null) {
	    expMessage = new Message("Illegal {0}: null. Try an empty " +
				     "string instead.", parameters).toString();
	    throw new IllegalArgumentException(expMessage);
	}
	settings.put(key, value);
    }

    /** This method returns the value of a certain key. It will try to 
	cast it to an integer before returning. 
	@param key The key pointing to the value to be returned. 
	@return The value the key points to, cast to an int. 
	@throws ClassCastException If the value was not an int. 
	@throws NullPointerException If there was no value stored by that
	key (or if the value stored was null for some reason). The 
	problem is that int cannot be null, while String in getStrValue 
	can. */
    public int getIntValue(String key) {
	return ((Integer) settings.get(key)).toInt();
    }
    
    /** This method returns the value of a certain key. It will try to 
	cast it to a string before returning. 
	@param key The key pointing to the value to be returned. 
	@return The value the key points to, cast to a String. 
	@throws ClassCastException If the value was not a String. */
    public String getStrValue(String key) {
	return (String) settings.get(key);
    }

    /** This method transforms this settings class into a format which
	can be parsed by parseSettingsFile. 
	@return String containing the non-fixed data in this class. */
    public String toString() {
	String keyString, valueString;
	StringBuffer result;
	Object value;
	Iterator keyIterator = settings.keySet().iterator();
	result = "";
	while(iterator.hasNext()) {
	    keyString = (String) iterator.next();
	    value = settings.get(key);
            try {
		valueString = (String) settings.get(key);
	    }
	    catch(ClassCastException mustBeAnIntegerThen) {
		valueString = "" + ((Integer) value).intValue();
	    }
	    result.append(keyString + " " + KEY_VALUE_SEPARATOR + " " + 
			  valueString + System.getProperty("line.separator",
							   "\n"));
	}
	return result.toString();
    }

    /** This method parses the fileContent string and sets up the settings
	HashMap values accordingly. It will try to cast number strings to
	integers.
	@param fileContent A String containing what has been in 
	a settings file for parsing.
	@throws ParseException If the settings text was not syntactically
	correct. */
    private void parseSettingsFile(String fileContent) 
	throws ParseException {
	StringTokenizer rowenizer;
	String[] parameters = new String[2], parts;
	String line, errorMessage, key, valueString;
	int lineCounter;
	Logger logger;

	// We accept \n,\r and whatever this system uses as a line separator.
	// StringTokenizer will not mind matching against eg. \r\r; it will
	// be considered the same as \r.
	rowenizer = new StringTokenizer(fileContent, "\n\r" + 
					System.getProperty("line.separator",
							   "\n"));
	int lineCounter = 0;
	while(rowenizer.hasMoreTokens()) {
	    lineCounter++;
	    line = rowenizer.nextToken();
	    // First, check if it is an empty line or a comment line. 
	    // Ignore those.
	    // (Trimming does not modify the string.)
	    if(line.trim() != "" && !line.startsWith(COMMENT_MARKER)) {
		parts = line.split(KEY_VALUE_SEPARATOR);
		// parts.length can be > 2, because KEY_VALUE_SEPARATOR
		// is allowed in the value string, even if not in the key
		// string.
		if(parts.length < 2) {
		    // Log where we failed and on what.
		    parameters[0] = "" + lineCounter;
		    parameters[1] = line;
		    errorMessage = new Message("Syntax error on line {0}, " +
					       "which was: \"{1}\".",  
					       parameters).toString();
		    throw new ParseException(errorMessage);
		}
		else { // Line has 2 parts as it should.
		    key = parts[0].trim();
		    valueString = parts[1].trim();
		    try {
			setValue(key, Integer.parseInt(valueString));
		    }
		    catch(NumberFormatException notAnIntegerThen) {
			setValue(key, valueString);
		    }
		}
	    }
	}
	parameters[0] = "" + lineCounter;
	parameters[1] = "" + settings.size();
	logger = Logger.getLogger(this.getClass().getPackage());
	logger.info(new Message("Settings successfully parsed, lines: " +
				"{0}, unique keys found: {1}.", 
				parameters));
		    
    }

}
