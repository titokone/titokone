/** This class takes care of language control and gathering the translation
    data from various files. It is used in a static manner by Translatable
    to ask for replacements to strings. While Translatable does not know what
    language the system should at this very moment speak, Translator has
    setLanguage() and getLanguage() methods for controlling this. It 
    reads the translateables from a file and does not replace anything within
    them as it returns the translated strings. */
public class Translator {
    private statuc String languageCode;
    // There is also a private mapping of strings to their <languageCode>
    // replacements, implemented in some suitable class. Java ought to have 
    // a hashtable or something such for the purpose.

    /** This method sets the language the Translator outputs to 
	<languageCode>.
	@param languageCode The language for the translator to
	use. The code should be the ISO 639 two- or in some rarer
	cases three-letter language code, eg. "fi" for Finnish, "sv"
	for Swedish, "en" for English, but the validity of the code is
	not checked as such. If no language is ever successfully set, the 
	default is the program's native language (English). If this set
	fails, the language remains unchanged.
	@throws LanguageNotSupportedException if there is no file 
	<languageCode>.po in the translation directory or if it cannot
	be parsed. */
    public static void setLanguage(String languageCode) 
	throws LanguageNotSupportedException {
	/* Set internal language variable to languageCode and load the
	   new set of translated strings from file <languageCode.po> into
	   the mapping variable. Only replace it as a last step to keep 
	   the current language until we are certain that the file read
	   and parse worked. */
    }

    /** This function returns the language currently used as a code. 
	@return The language code currently in use. See setLanguage(). */
    public static String getLanguage() {
	/* Return languageCode. */
    }

    /** This function replaces a given idString with a string of the 
	language currently in use. If no replacement is found for the
	current language, idString is returned unchanged. 
	@param idString The native-language string which has or has not
	a replacement in the translation *.po files. 
	@return The translated string or idString if no translation 
	exists. */
    public static String translate(String idString) {
	/* Fetch the translated replacement of idString from the 
	   mapping described above and return it. If languageCode is set
	   to nothing, just return idString. */
    }
}
