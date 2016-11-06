// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.util.ListResourceBundle;

/**
 * This class is the stub tester for the Translator class.
 */
public class StubTranslations_fi extends ListResourceBundle {

    private final String[][] contents = {
            {"NewKnownTranslation", "New translation {0} - {1} - {2}!"}
    };

    public Object[][] getContents() {
        return contents;
    }
}
