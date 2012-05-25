// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.TTK91CompileSource;

/**
 * This class represents source code. It contains the source as a delimited
 * string.
 */
public class Source implements TTK91CompileSource {
    /**
     * The source code as one long String, lines delimited with \n, \r\n or
     * \r.
     */
    private String sourceString;

    /**
     * This constructor initializes sourceString with its given string.
     *
     * @param source String containing source delimited with \n, \r\n or \r.
     */
    public Source(String source) {
        this.sourceString = source;
    }

    /**
     * @return The source code in one long String, lines delimited with
     *         \n, \r\n or \r.
     */
    public String getSource() {
        return sourceString;
    }
}
