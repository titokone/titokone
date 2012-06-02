// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.text.ParseException;

public class SettingsTest extends LoguserTestCase {
    protected void setUp() {
    }

    public void testConstructor() {
        assertTrue(true);
    }

    public void testSetAndGetValue() throws java.text.ParseException {
        Settings setup = new Settings("");
        setup.setValue("key1", "bar");
        assertEquals(setup.getStrValue("key1"), "bar");
        setup.setValue("key2", 3);
        assertEquals(setup.getIntValue("key2"), 3);
        setup.setValue("key1", 4); // Changing key1's value.
        assertEquals(setup.getIntValue("key1"), 4);
        try {
            setup.setValue("key3", "3");
            setup.getIntValue("key3");
            fail("Should have thrown an exception: string of a number " +
                    "cannot be cast to int.");
        } catch (ClassCastException err) {
        }
        try {
            setup.setValue("key4", null);
            fail("Should have thrown an exception: null value.");
        } catch (IllegalArgumentException err) {
            assertEquals(err.getMessage(),
                    "Illegal value: null. Try an empty string instead.");
        }
        try {
            setup.setValue(null, "foo");
            fail("Should have thrown an exception: null key.");
        } catch (IllegalArgumentException err) {
            assertEquals(err.getMessage(),
                    "Illegal key: null. Try an empty string instead.");
        }
        try {
            setup.setValue("fo=o", "bar");
            fail("Should have thrown an exception: KEY_VALUE_SEPARATOR in " +
                    "key.");
        } catch (IllegalArgumentException err) {
            assertEquals(err.getMessage(),
                    "Illegal key \"fo=o\", contains '='.");
        }
        try {
            setup.setValue("fo" + "\r" + "fdas", "bar");
            fail("Should have thrown an exception: linebreak in " +
                    "key.");
        } catch (IllegalArgumentException err) {
            assertEquals(err.getMessage(),
                    "Illegal key \"fo\rfdas\", contains a linebreak.");
        }
        try {
            setup.setValue("foo", "bar\nrd");
            fail("Should have thrown an exception: linebreak in " +
                    "value.");
        } catch (IllegalArgumentException err) {
            assertEquals(err.getMessage(),
                    "Illegal value \"bar\nrd\", contains a linebreak.");
        }
        try {
            setup.setValue("\nfoo", "bar\n");
        } catch (IllegalArgumentException err) {
            fail("Linebreaks in ends of values and beginnings of keys " +
                    "should not matter: " + err.getMessage());
        }
    }

    public void testParse() throws ParseException {
        Settings setup = new Settings("# comment line\n" +
                "\n" + // empty line
                "key1=bar\n" +
                "key2 = 4 \n");
        System.out.println(lastRecord.getMessage());
        assertEquals(setup.getKeys().length, 2);
        assertEquals(setup.getStrValue("key1"), "bar");
        assertEquals(setup.getIntValue("key2"), 4);
        try {
            setup = new Settings("a line without any keyvalueseparator");
            fail("Should have thrown ParseException: syntax error by now.");
        } catch (ParseException err) {
            assertEquals(err.getMessage(),
                    "Syntax error on line 1, which was: \"a line " +
                            "without any keyvalueseparator\".");
            assertEquals(err.getErrorOffset(), 1);
        }
    }

    public void testExtract() throws ParseException {
        Settings setup = new Settings("");
        assertEquals(setup.toString(), "");
        setup.setValue("key1", "foo");
        setup.setValue("key2", 3);

        String str = setup.toString();
        String nl = System.getProperty("line.separator", "\n");

        assertEquals(2, str.split(nl).length);
        assertTrue(str.contains("key1 = foo" + nl));
        assertTrue(str.contains("key2 = 3" + nl));
    }
}
