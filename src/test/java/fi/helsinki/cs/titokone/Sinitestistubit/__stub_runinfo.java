// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.Sinitestistubit;

public class __stub_runinfo {
    private String dev;

    public __stub_runinfo(String device) {
        dev = device;
    }

    public int[] whatOUT() {
        int[] result = {2, 3};
        return result;
    }

    public String whatDevice() {
        return dev;
    }
}
