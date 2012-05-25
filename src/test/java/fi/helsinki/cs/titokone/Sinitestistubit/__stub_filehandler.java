// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.Sinitestistubit;

import fi.helsinki.cs.titokone.Binary;

import java.io.File;

public class __stub_filehandler {
    String settingsreturn = "";

    public void saveSettings(StringBuffer foo, File bar) {

    }

    public Binary loadBinary(File foo) {
        return null;
    }

    public StringBuffer loadSettings(File foo) {
        return new StringBuffer(settingsreturn);
    }

    // Tweak functions.
    public void tweakLoadSettings(String newSettings) {
        settingsreturn = newSettings;
    }
}
