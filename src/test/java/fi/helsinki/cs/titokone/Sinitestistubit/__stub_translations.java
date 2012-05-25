package fi.helsinki.cs.titokone.Sinitestistubit;

import java.util.ListResourceBundle;

/**
 * This class is the stub tester for the Translator class.
 */
public class __stub_translations extends ListResourceBundle {
    private boolean overrideswap = false;

    String[][] result = {{"OnlyKnownTranslation",
            "Other translation {0} - {1} - {2}!"}};

    String[][] result2 = {{"SecondKnownTranslation",
            "Translated {0} - {1} - {2}!"},
            {"OnlyKnownTranslation",
                    "Translation {0} - {1} - {2}!"}};
    String[][] result3;

    public __stub_translations() {
        result3 = result2;
    }

    public __stub_translations(boolean change) {
        if (change) {
            result3 = result;
        } else {
            result3 = result2;
        }
    }

    public Object[][] getContents() {
        return result3;
    }
}
