// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.Sinitestistubit;

import java.util.logging.Logger;

public class __stub_processor {
    public __stub_processor(int size) {
        Logger.getLogger("fi.helsinki.cs.titokone").fine("StubP got size " + size);
    }

    public void keyboardInput(int input) {
        Logger.getLogger("fi.helsinki.cs.titokone").fine("StubP got kbdinput " +
                input);

    }
}
