// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.Sinitestistubit;

import fi.helsinki.cs.titokone.MemoryLine;

public class __stub_memoryline extends MemoryLine {
    public int number;
    public String str;

    public __stub_memoryline() {
        super(1, "foo");
    }

    public int getBinary() {
        return 60;
    }
}
