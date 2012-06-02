// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.util.Locale;
import java.util.logging.LogRecord;

/**
 * Unfortunately, this test requires some setup in the Translator class:
 * the private static final boolean TESTING variable must be set to
 * true.
 */
public class TranslatorTest extends LoguserTestCase {

    private String originalResourceFamilyName;

    protected void setUp() throws Exception {
        originalResourceFamilyName = Translator.resourceFamilyName;
        Translator.resourceFamilyName = StubTranslations.class.getName();
        Translator.setLocale(Locale.ENGLISH);
    }

    protected void tearDown() throws Exception {
        Translator.resourceFamilyName = originalResourceFamilyName;
    }

    // Since this is the first test class to use logging, we run a few
    // logging tests to start with.
    public void testBasicLogging() {
        logger.fine("This is a log message.");
        assertEquals("This is a log message.", lastRecord.getMessage());
        hushLogger(); // Make it stop making noise to stdout.
        logger.fine("This is a quiet log message.");
        assertEquals("This is a quiet log message.", lastRecord.getMessage());
        // And then look at the first message again...
        assertEquals("This is a log message.", ((LogRecord) allRecords.get(allRecords.size() - 2)).getMessage());
    }

    public void testBasicTranslate() {
        String translateString;

        translateString = Translator.translate("UnknownString");
        assertEquals("Unknown message", "UnknownString", translateString);
        // Log will show where the translation failed.
        assertEquals("Translation for UnknownString not found in default set either.", lastRecord.getMessage());

        translateString = Translator.translate("OnlyKnownTranslation");
        assertEquals("Known message", "Translation {0} - {1} - {2}!", translateString);
        translateString = Translator.translate("SecondKnownTranslation");
        assertEquals("Another known message", "Translated {0} - {1} - {2}!", translateString);

        // Change the current translation turns out to be.
        Translator.setLocale(Locale.ENGLISH, new StubTranslations(StubTranslations.OTHER_TRANSLATION));
        // Then change it again so that it will only show up in the
        // default translations, not the current ones.
        // Now it should be translated, but not from the first try.
        translateString = Translator.translate("SecondKnownTranslation");
        assertEquals("Now only default-known message", "Translated {0} - {1} - {2}!", translateString);
        assertEquals("Translation for SecondKnownTranslation not found in current set.", lastRecord.getMessage());
    }

    // This method is used twice in the test below. It generates a string
    // which isn't entirely short. This has been tested with 50 000,
    // but since it just takes time and does not seem to make a difference
    // for a class not in our power to change (MessageFormat), I've 
    // reduced the length of the string to generate.
    private String getManyLetters() {
        String result = "";
        for (int i = 0; i < 50; i++) {
            result += result;
        }
        return result;
    }

    public void testReplacementTranslation() {
        String translateString;
        String[] replace0 = new String[0];
        String[] replace2 = {"foo", "bar"};
        String[] replace3 = {"", "bing", "bong"};
        String[] replace4 = {"fiba", getManyLetters(),
                "fdasf", "fob"};
        // Use the basic translations.
        Translator.setLocale(Locale.ENGLISH, new StubTranslations(StubTranslations.DEFAULT_TRANSLATION));
        // Nothing to replace: 0 replacements (unnecessary).
        translateString = Translator.translate("UnknownString", replace0);
        assertEquals("Unknown message with 0 vs. 0", "UnknownString", translateString);
        assertEquals("Translation for UnknownString not found in default set either.", lastRecord.getMessage());
        // Nothing to replace: 2 replacements (too many).
        translateString = Translator.translate("UnknownString", replace2);
        assertEquals("Unknown message with 0 vs. 2", "UnknownString", translateString);
        assertEquals("Translation for UnknownString not found in default set either.", lastRecord.getMessage());
        // 3 to replace, null replacement.
        translateString = Translator.translate("OnlyKnownTranslation", null);
        assertEquals("3 to replace, 0 replacements", "Translation {0} - {1} - {2}!", translateString);
        // 3 to replace: 0 replacements (none).
        translateString = Translator.translate("OnlyKnownTranslation", replace0);
        assertEquals("3 to replace, 0 replacements", "Translation {0} - {1} - {2}!", translateString);
        // 3 to replace: 2 replacements (too few).
        translateString = Translator.translate("OnlyKnownTranslation", replace2);
        assertEquals("3 to replace, 2 replacements", "Translation foo - bar - {2}!", translateString);
        // 3 to replace: 3 replacements (just right). Including empty
        // string replacement.
        translateString = Translator.translate("OnlyKnownTranslation", replace3);
        assertEquals("3 to replace, 3 replacements", "Translation  - bing - bong!", translateString);
        // 3 to replace: 4 replacements (too many). Including within
        // the usable replacements one longer string.
        translateString = Translator.translate("OnlyKnownTranslation", replace4);
        assertEquals("3 to replace, 4 replacements", "Translation fiba - " + getManyLetters() + " - fdasf!", translateString);
    }

    // The setLocale(Locale, ResourceBundle) has been tested before.
    public void testResourceLocating() {
        String translateString;
        Translator.setLocale(new Locale("fi", "FI", "MAC"));
        // The StubTranslations_fi should be in use now.
        translateString = Translator.translate("NewKnownTranslation");
        assertEquals("New translateable", "New translation {0} - {1} - {2}!", translateString);
    }
}
