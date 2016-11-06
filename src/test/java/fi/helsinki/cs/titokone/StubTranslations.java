// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.util.ListResourceBundle;

/**
 * This class is the stub tester for the Translator class.
 */
public class StubTranslations extends ListResourceBundle {

    public static final String[][] DEFAULT_TRANSLATION = {
            {"SecondKnownTranslation", "Translated {0} - {1} - {2}!"},
            {"OnlyKnownTranslation", "Translation {0} - {1} - {2}!"}
    };

    public static final String[][] OTHER_TRANSLATION = {
            {"OnlyKnownTranslation", "Other translation {0} - {1} - {2}!"}
    };

    private final String[][] contents;

    public StubTranslations() {
        this(DEFAULT_TRANSLATION);
    }

    public StubTranslations(String[][] contents) {
        this.contents = contents;
    }

    public Object[][] getContents() {
        return contents;
    }
}
