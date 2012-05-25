// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.Sinitestistubit;

import java.util.logging.*;

// Levels: severe, warning, info, fine

/**
 * Log handlers are given records to publish whenever a Logger with
 * this particular handler assigned to it is told to log something
 * at a level which should be published. This implementation of a log
 * handler prints the log data out to system.err.
 */
public class LogHandler extends Handler {
    /**
     * These methods are uninteresting.
     */
    public void flush() {
    }

    public void close() {
    }

    /**
     * This method writes out the data from a given log record.
     */
    public void publish(LogRecord record) {
        // As a default, there is no filtering.
        System.err.println(record.getMessage());
        //System.out.println(record.getSourceClassName() + ": " +
        //		   record.getSourceMethodName() + " - " +
        //		   record.getMessage() + " (level was " +
        //		   record.getLevel() + ")");
    }
}
