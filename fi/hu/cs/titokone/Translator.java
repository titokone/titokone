/* Copyright 2004 University of Helsinki, Department of Computer
   Science. See license.txt for details. */
package fi.hu.cs.titokone;

import java.util.ResourceBundle;
import java.util.Locale;
import java.util.logging.Logger;
import java.text.MessageFormat;
import java.util.MissingResourceException;

/** This class deals with translating strings. It also remembers which
    language is currently set, but does not know specifically what 
    languages are currently available. */
public class Translator {
    /** This field should be false during normal operation. It can be 
	set to true to enable some testability quirks. */
    private static final boolean TESTING = false; 

    /** This name identifies the resource files containing translations 
	for this software. */
    public static final String resourceFamilyName = "fi.hu.cs.titokone.translations.Translations";

    /** This field contains the default locale. */
    public static final Locale defaultLocale = Locale.ENGLISH;

    /** This field stores the current locale. It defaults to 
	defaultLocale. */
    private static Locale currentLocale = defaultLocale;
    /** This field stores the default ResourceBundle. */
    private static ResourceBundle defaultTranslations =  ResourceBundle.getBundle(resourceFamilyName, defaultLocale);
	/*(TESTING ? new __stub_translations() : 
	 ResourceBundle.getBundle(resourceFamilyName, defaultLocale));*/
    /** This field stores the current ResourceBundle in use. */
    private static ResourceBundle translations = defaultTranslations; 

    /** This function translates a fixed string to the currently used 
	language. If the current language has no string corresponding to
	the key, a default translation is used from Translations.class. 
	If that class does not have the translation either, the keystring
	itself is returned. The translation therefore fails silently.
	@param keyString A string key that identifies the translation in a
	Translations*class file. 
	@return The translated string, if available, or something otherwise
	usable. */
    public static String translate(String keyString) { 
	String result = null;
	Logger logger = 
	    Logger.getLogger(Translator.class.getPackage().getName());
	try {
	    result = translations.getString(keyString);
	}
	catch(MissingResourceException untranslatedKey) {
	    logger.fine("Translation for " + keyString + " not found in " +
			"current set."); // No Message used here.
	    result = null;
	}
	catch(NullPointerException oddity) {
	  logger.fine("ResourceBundle getString would not just return " +
		      "null, it insists on casting. Trasnslating string " +
		      "'" + keyString + "' failed.");
	  result = null;
	}
	if(result == null) { // If there was no luck, try the untranslated.
	    try {
		result = defaultTranslations.getString(keyString);
	    }
	    catch(MissingResourceException totallyUnknownKey) {
		logger.fine("Translation for " + keyString + " not found " +
			    "in default set either.");
		result = null;
	    }
	    catch(NullPointerException oddity) {
   	        logger.fine("Null pointer exception repeated when trying " +
		   	    "default translations.");
		result = null;
	    } 
	}
	if(result == null) // If there was still no luck, go for the keystr.
	    result = keyString;
	return result;
    }

    /** This function translates a template string to the currently
	used language and replaces any {i} markers in it with strings
	from the parameters array. If the current language has no
	string corresponding to the key, a default translation is used
	from Translations.class.  If that class does not have the
	translation either, the keystring itself is returned. The
	translation therefore fails silently. The translation string is 
	fetched from the current ResourceBundle in use, and the replacement
	is done with the help of java.text.MessageFormat.
	@param keyString A string key that identifies the translation in a
	Translations*class file. 
	@param parameters A string array containing strings to replace {i}
	markers in the string in order - that is, parameters[0] replaces {0},
	parameters[1] replaces {1} etc.
	@return The translated string, if available, or something otherwise
	usable, with {i} markers replaced from the parameters array as 
	far as there are available replacements. */
    public static String translate(String keyString, String[] parameters) { 
	MessageFormat formatter;
	// First, we translate the basic string as if it had no parameters,
	// then we replace any {i} fields in it by using MessageFormat's
	// format method, with the replacements as its parameter. The
	// replacement is done according to locale, although this is
	// unlikely to affect anything in our current implementation.
	formatter = new MessageFormat(translate(keyString));
	formatter.setLocale(currentLocale);
	return formatter.format(parameters).toString();
    }

    /** This method sets the current locale in use and fetches a 
	corresponding ResourceBundle that contains the translations most
	suitable for this locale. This may mean the default English 
	translation if there is nothing better available. 
	@param newLocale The locale to switch to, eg. new Locale("fi", 
	"FI"). */
    public static void setLocale(Locale newLocale) { 
	Logger logger = 
	    Logger.getLogger(Translator.class.getPackage().getName());
	if(newLocale == null)
	    throw new IllegalArgumentException("Trying to set locale to " +
					       "null.");
	currentLocale = newLocale;
	translations = ResourceBundle.getBundle(resourceFamilyName,
						newLocale);
	logger.fine("Locale changed, new locale " + newLocale.toString() + 
		    ", translations from class " + 
		    translations.getClass().getName() + ".");
    }

    /** This method sets the current locale in use and tries to fetch the
	translation from translationPath. Use of setLocale(Locale) is 
	recommended, but this method enables the user to have very exact
	control over where the translations are found.
	@param newLocale The locale to switch to, eg. new Locale("fi", 
	"FI"). 
	@param newTranslations A class containing the translations for this 
	locale. If the translations are located in the standard place, 
	setLocale(Locale) can be used. */
    public static void setLocale(Locale newLocale, 
				 ResourceBundle newTranslations) {
	Logger logger = 
	    Logger.getLogger(Translator.class.getPackage().getName());
	if(newLocale == null)
	    throw new IllegalArgumentException("Trying to set locale to " +
					       "null.");
	if(newTranslations == null)
	    throw new IllegalArgumentException("Trying to set translations " +
					       "to null.");
	currentLocale = newLocale;
	translations = newTranslations;
	logger.fine("Locale changed with set translations, new locale " + 
		    newLocale.toString() + 
		    ", translations from class " + 
		    translations.getClass().getName() + ".");
    }

    /** This method returns the resource bundle in use.
	@return The resource bundle set to correspond to the current 
	locale. */
    public static ResourceBundle getResourceBundle() {
	return translations;
    }
}
