/** This class is to be inherited by all classes which produce strings
    which the user can see. The correct way of calling it is by 
    eg. System.err.println(TL("Error: Cannot start anything!"); 
    The strings may contain %s which are to be replaced with parameters
    given to the TL function, so that translatable strings like 
    "Error on line 5: Bad opcode" and "Error on line 10: Another error" 
    do not require a translatable string for each possibility. The 
    strings for these would be "Error on line %s: %s", "Bad opcode" and 
    "Another error", and the first would be called by something like
    System.err.println(TL("Error on line %s: %s", {"5", TL("Bad opcode")})); 
    If there should be a % in the text, possibly even followed by an 's', 
    write it as %%. If there are more %s-markers given than there are 
    parameters to replace them with, the remaining markers are replaced with 
    empty strings. %%s will become "%s" in the result string, %%%s will
    become "%" + a parameter given. */
public class Translatable {
    /** This function takes a translatable string and returns the string
	translated. %% is replaced with %, %s is replaced with 
	empty strings. 
	@param idString The native-language string used as a string ID. 
	@return The translated string. */
    protected String TL(String idString) {
	/* Ask Translator for a translated version of this string. 
	   Replace %% with % and any %s with an empty string. */
    }

    /** This function takes a translatable string and returns the string
	translated. %% is replaced with %, %s is replaced with 
	empty strings. 
	@param idString The native-language string used as a string ID. 
	@param parameter A single string to replace the first occurrence
	of %s.
	@return The translated string. */
    protected String TL(String idString, String parameter) {
	/* Ask Translator for a translated version of this string and
	   replace %% with % and the first %s with the parameter. 
	   Replace each remaining %s with an empty string. */
    }

    /** This function takes a translatable string and returns the string
	translated. %% is replaced with %, %s is replaced with 
	empty strings. 
	@param idString The native-language string used as a string ID. 
	@param parameters An array of strings to replace as many 
	occurrences of %s as possible. If there are too many parameters,
	the remaining ones are ignored.
	@return The translated string. */
    protected String TL(String idString, String[] parameters) {
	/* Ask Translator for a translated version of this string
	   and replace %% with % and each %s with values from parameters, 
	   0 first etc. If there are too few parameters given, replace
	   the remaining slots with empty strings. */
    }
}
