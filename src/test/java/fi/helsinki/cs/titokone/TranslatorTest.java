package fi.helsinki.cs.titokone;

import fi.helsinki.cs.titokone.Sinitestistubit.*;

import java.util.logging.LogRecord;
import java.util.*;

/** Unfortunately, this test requires some setup in the Translator class:
    the private static final boolean TESTING variable must be set to 
    true. */
public class TranslatorTest extends __LoguserTestCase {
    // Translator is a static class, no setup needed.
    protected void setUp() {}

    // Since this is the first test class to use logging, we run a few 
    // logging tests to start with.
    public void testBasicLogging() {
	logger.fine("This is a log message.");
	assertEquals(lastRecord.getMessage(), 
		     "This is a log message.");
	hushLogger(); // Make it stop making noise to stdout.
	logger.fine("This is a quiet log message.");
	assertEquals(lastRecord.getMessage(), "This is a quiet log message.");
	// And then look at the first message again...
	assertEquals(((LogRecord) 
		      allRecords.get(allRecords.size() - 2)).getMessage(), 
		     "This is a log message.");
    }

    public void testBasicTranslate() {
	String translateString;
	translateString = Translator.translate("UnknownString");
	assertEquals("Unknown message",
		     translateString, "UnknownString");
	// Log will show where the translation failed.
	assertEquals(lastRecord.getMessage(), 
		     "Translation for UnknownString not found in default " +
		     "set either.");
	translateString = Translator.translate("OnlyKnownTranslation");
	assertEquals("Known message",
		     translateString, 
		     "Translation {0} - {1} - {2}!");
	translateString = Translator.translate("SecondKnownTranslation");
	assertEquals("Another known message",
		     translateString,
		     "Translated {0} - {1} - {2}!");
	// Change the current translation turns out to be.
	Translator.setLocale(Locale.ENGLISH, new __stub_translations(true));
	// Then change it again so that it will only show up in the 
	// default translations, not the current ones.
	// Now it should be translated, but not from the first try.
	translateString = Translator.translate("SecondKnownTranslation");
	assertEquals("Now only default-known message",
		     translateString, 
		     "Translated {0} - {1} - {2}!");
	assertEquals(lastRecord.getMessage(),
		     "Translation for SecondKnownTranslation not found in " +
		     "current set.");
    }

    // This method is used twice in the test below. It generates a string
    // which isn't entirely short. This has been tested with 50 000,
    // but since it just takes time and does not seem to make a difference
    // for a class not in our power to change (MessageFormat), I've 
    // reduced the length of the string to generate.
    private String getManyLetters() {
	String result = "";
	for(int i = 0; i < 50  ; i++) {
	    result += result;
	}
	return result;
    }

    public void testReplacementTranslation() {
	String translateString;
	String[] replace0 = new String[0];
	String[] replace2 = { "foo", "bar" };
	String[] replace3 = { "", "bing", "bong" };
	String[] replace4 = { "fiba", getManyLetters(), 
			      "fdasf", "fob" };
	// Use the basic translations.
	Translator.setLocale(Locale.ENGLISH, new __stub_translations());
	// Nothing to replace: 0 replacements (unnecessary).
	translateString = Translator.translate("UnknownString", replace0);
	assertEquals("Unknown message with 0 vs. 0",
		     translateString, "UnknownString");
	assertEquals(lastRecord.getMessage(), 
		     "Translation for UnknownString not found in default " +
		     "set either.");
	// Nothing to replace: 2 replacements (too many).
	translateString = Translator.translate("UnknownString", replace2);
	assertEquals("Unknown message with 0 vs. 2",
		     translateString, "UnknownString");
	assertEquals(lastRecord.getMessage(), 
		     "Translation for UnknownString not found in default " +
		     "set either.");
	// 3 to replace, null replacement.
	translateString = Translator.translate("OnlyKnownTranslation",
					       null);
	assertEquals("3 to replace, 0 replacements",
		     translateString, 
		     "Translation {0} - {1} - {2}!");
	// 3 to replace: 0 replacements (none).
	translateString = Translator.translate("OnlyKnownTranslation",
					       replace0);
	assertEquals("3 to replace, 0 replacements",
		     translateString, 
		     "Translation {0} - {1} - {2}!");
	// 3 to replace: 2 replacements (too few).
	translateString = Translator.translate("OnlyKnownTranslation",
					       replace2);
	assertEquals("3 to replace, 2 replacements",
		     translateString, 
		     "Translation foo - bar - {2}!");
	// 3 to replace: 3 replacements (just right). Including empty
	// string replacement.
	translateString = Translator.translate("OnlyKnownTranslation",
					       replace3);
	assertEquals("3 to replace, 3 replacements",
		     translateString, 
		     "Translation  - bing - bong!");
	// 3 to replace: 4 replacements (too many). Including within
	// the usable replacements one longer string.
	translateString = Translator.translate("OnlyKnownTranslation",
					       replace4);
	assertEquals("3 to replace, 4 replacements",
		     translateString, 
		     "Translation fiba - " + getManyLetters() + 
		     " - fdasf!");
    }
    
    // The setLocale(Locale, ResourceBundle) has been tested before.
    public void testResourceLocating() {
	String translateString;
	Translator.setLocale(new Locale("fi", "FI", "MAC"));
	// The __stub_translations_fi should be in use now.
	translateString = Translator.translate("NewKnownTranslation");
	assertEquals("New translateable",
		     translateString, "New translation {0} - {1} - {2}!");
    }
    
}
