// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

/* This class was used for testing GUIBrain */

package fi.helsinki.cs.titokone.GUIBrainStubs;


public class __stub_SymbolTable {

    public __stub_SymbolTable() {
    }

    public String[] getAllSymbols() {
        String[] ret = {"joo", "jaa", "juu"};
        return ret;
    }

    public int getSymbolValue(String joo) {
        return 100;
    }

}

