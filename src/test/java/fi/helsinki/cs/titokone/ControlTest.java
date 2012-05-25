// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import fi.helsinki.cs.titokone.Sinitestistubit.__LoguserTestCase;
import fi.helsinki.cs.ttk91.*;

import java.io.*;
import java.text.ParseException;

public class ControlTest extends __LoguserTestCase {
    Control control;
    Application application;

    protected void setUp() {
        control = new Control(new File("/root/teststdin"),
                new File("/root/teststdout"));
    }

    public void testConstructorAndChangeMemorySize() {
        // setUp has been called by now.
        assertEquals(control.getMemory().getSize(), 512);
    }

    // Testaa samalla compileLinen.
    public void testCompile() {
        TTK91Application app = null;
        try {
            app = control.compile(new Source("X DS 1\nNOP\nNOP\nNOP"));
        } catch (TTK91Exception x) {
            System.out.println("Poikkeus " + x.toString());
        }
        application = (Application) app;
        assertEquals(application.getCode()[0].getSymbolic(), "Foo bar baz");
    }

    public void testGetApplicationDefinitions() {
        testCompile(); // to make sure internal state is right.
        // The application going in has 'filename' as its stdin path.
        assertEquals(application.getSymbolTable().getDefinition("stdin"),
                "filename");
        // So the file array gotten from the app should have the same path
        // in its stdin position.
        assertEquals(control.getApplicationDefinitions()[0].toString(),
                "filename");
    }

    public void testLoad() throws TTK91RuntimeException {
        testCompile(); // to make sure internal state is right. This one works.
        control.load();
        try {
            control.writeToStdoutFile("text");
            fail("Should not have been able to write to default stdout " +
                    "file /root/teststdin.\n");
        } catch (TTK91FailedWrite err) {
            assertEquals(err.getMessage(),
                    "/root/teststdout (Permission denied)");
        }
        application.getSymbolTable().addDefinition("stdin", "filename");
        control.load();
        assertEquals(application.readNextFromStdIn(), 3); // filename has '3' in it.
        application.getSymbolTable().addDefinition("stdin", "nonexistingfile");
        try {
            control.load();
        } catch (TTK91NoStdInData foo) {
            assertEquals(foo.getMessage(), "STDIN data file unreadable: " +
                    "nonexistingfile (No such file or directory)");
        }
        application.getSymbolTable().addDefinition("stdin",
                "fiddlesticks");
        control.eraseMemory();
        try {
            control.load();
        } catch (TTK91NoStdInData foo) {
            assertEquals(foo.getMessage(), "STDIN data file contains " +
                    "invalid " +
                    "data: Stdin input string \"bah\n\" invalid, " +
                    "should be eg. \\n-separated list of integers.");
        }
        Processor cpu = (Processor) control.getCpu();
        TTK91Memory mem = cpu.getMemory();
        // Did it load?
        assertEquals(mem.getValue(0), 3); // first line of code matches.
        System.out.println("Curiosities:\n" +
                "First memory lines should be 3,4,5,0 -- are: " +
                mem.getValue(0) + ", " + mem.getValue(1) + ", " +
                mem.getValue(2) + ", " + mem.getValue(3));
        System.out.println("1 code line and 2 data lines -- FP (should be " +
                "what, 1?): " + cpu.getValueOf(TTK91Cpu.REG_FP) +
                ", SP (should be 2): " +
                cpu.getValueOf(TTK91Cpu.REG_SP));
        // Then let's test the stdout file.
        control.eraseMemory();
        application.getSymbolTable().addDefinition("stdout", "/root/foo1");
        application.getSymbolTable().addDefinition("stdin", "filename");
        control.load();
        try {
            control.writeToStdoutFile("text");
            fail("Should have not been able to write to /root/foo1.\n");
        } catch (TTK91FailedWrite err) {
            assertEquals(err.getMessage(), "/root/foo1 (Permission denied)");
        }
    }

    public void testChangeMemorySize() {
        try {
            control.changeMemorySize(8);
            fail("Should have not been able to change memory size to 8.");
        } catch (IllegalArgumentException err) {
            assertEquals(err.getMessage(), "Memory size must be between " +
                    "2^9 and 2^16, a change to 2^8 failed.");
        }
        try {
            control.changeMemorySize(17);
            fail("Should have not been able to change memory size to 17.");
        } catch (IllegalArgumentException err) {
            assertEquals(err.getMessage(), "Memory size must be between " +
                    "2^9 and 2^16, a change to 2^17 failed.");
        }
        control.changeMemorySize(15);
        assertEquals(control.getMemory().getSize(), 32768); // 2^15 = 32768
    }

    public void testSetDefaultStdin() throws IOException, ParseException {
        try {
            // Contains garbage.
            control.setDefaultStdIn(new File("fiddlesticks"));
            fail("Should not be able to set stdin default with invalid " +
                    "contents.");
        } catch (ParseException err) {
            assertEquals(err.getMessage(),
                    "StdIn file contents are invalid; the file should " +
                            "contain only integers and separators.");
        }
        try {
            control.setDefaultStdIn(new File("filename"));
        } catch (Exception err) {
            fail("This should have worked: setting default stdin to a file " +
                    "containing 3.");
        }

    }

    public void testOpenSource() throws IOException {
        assertEquals(control.openSource(new File("filename")),
                "3\n"); // The file contains "3".
        // Compiler state is not really interestingly accessible as long
        // as we're using a stub in the control.
    }

    public void testGetBinary() {
        assertEquals(control.getBinary(new Application(null, null, null)),
                "___b91___\n___code___\n0 0\n___data___\n0 0\n" +
                        "___symboltable___\n___end___\n");
    }

    public void testSaveBinaryFileName() throws IOException {
        testCompile(); // Ensure right state.
        // No exception.
        control.saveBinary(new File("testdata/examplebinary"));
        control = new Control(new File("/root/test"), new File("/root/test"));
        // Exception: no application loaded yet.
        try {
            control.saveBinary(new File("testdata/examplebinary"));
            fail("Should not have been able to save a null app to binary.");
        } catch (IllegalStateException err) {
            assertEquals(err.getMessage(),
                    "Cannot save binary to file; no application has " +
                            "been compiled or loaded.");
        }
    }

    public void testSaveBinaryGuessFilename() throws IOException {
        testCompile(); // Make sure there is an application.
        try {
            control.saveBinary();
            fail("Load file should be missing, why does this binary save " +
                    "with filename guessing work?");
        } catch (IllegalStateException err) {
            assertEquals(err.getMessage(),
                    "Cannot deduce the file to store the binary " +
                            "into; no source file has been loaded.");
        }
        // Now there is:
        control.openSource(new File("testdata/briefsource.k91"));
        testCompile(); // And magically, there is an app.
        control.saveBinary();
    }

    //public void testOpenBinary() {
    //}

    /** // integraatiotesti run()ille ja runLine()lle puuttuu. 
     Samoin puuttuvat testit openBinarylle. */

    /* // integraatiotesti.
    public void testLoadBinary() {
	Application app;
	try {
	    app = (Application) control.loadBinary("___b91___\n___code___\n0 1\nNOP\n__data__\n0 1\n___symboltable___\n___end___\n");
	}
	catch(Exception err) {
	    System.out.println("Kato poikkeus: " + err.toString());
	}
    }
    */


}
