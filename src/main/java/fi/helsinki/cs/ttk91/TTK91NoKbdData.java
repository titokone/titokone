// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.ttk91;

/*
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
public class TTK91NoKbdData extends TTK91RuntimeException {
    public TTK91NoKbdData(String s) {
        super(s);
    }
}
