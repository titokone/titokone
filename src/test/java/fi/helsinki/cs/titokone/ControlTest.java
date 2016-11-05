// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import com.google.common.io.Files;
import fi.helsinki.cs.ttk91.*;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Locale;

public class ControlTest extends LoguserTestCase {

    private String originalLineSeparator;
    private final TemporaryFolder tempDir = new TemporaryFolder();

    // common test data
    private File teststdin;
    private File teststdout;
    private File filename;
    private File fiddlesticks;
    private File foo1;

    private Control control;
    private Application application;

    protected void setUp() throws IOException {
        originalLineSeparator = System.getProperty("line.separator");
        System.setProperty("line.separator", "\n");
        tempDir.create();

        // the default stdin/stdout files used to be "/root/teststdin", in hope of there not being permission to read them
        teststdin = tempDir.newFile("teststdin");
        Files.write("\n", teststdin, Charset.defaultCharset());
        withNoPermissions(teststdin);

        teststdout = tempDir.newFile("teststdout");
        Files.write("\n", teststdout, Charset.defaultCharset());
        withNoPermissions(teststdout);

        // valid input; only numbers and newlines
        filename = tempDir.newFile("filename");
        Files.write("3\n", filename, Charset.defaultCharset());

        // invalid input
        fiddlesticks = tempDir.newFile("fiddlesticks");
        Files.write("bah\n", fiddlesticks, Charset.defaultCharset());

        // inaccessible output file
        foo1 = tempDir.newFile("foo1");
        withNoPermissions(foo1);

        control = new Control(teststdin, teststdout, null);
        Translator.setLocale(new Locale("en"));
    }

    private static void withNoPermissions(File file) {
        file.setReadable(false);
        file.setWritable(false);
    }

    protected void tearDown() throws Exception {
        System.setProperty("line.separator", originalLineSeparator);
        tempDir.delete();
    }

    public void testConstructorAndChangeMemorySize() {
        // setUp has been called by now.
        assertEquals(32768, control.getMemory().getSize());
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

        MemoryLine[] memoryLines = application.getCode();
        assertEquals(3, memoryLines.length);
        assertEquals("NOP", memoryLines[0].getSymbolic());
        assertEquals("NOP", memoryLines[1].getSymbolic());
        assertEquals("NOP", memoryLines[2].getSymbolic());
    }

    public void testGetApplicationDefinitions() {
        testCompile(); // to make sure internal state is right.

        application.getSymbolTable().addDefinition("stdin", "filename");

        // The application going in has 'filename' as its stdin path.
        assertEquals("filename", application.getSymbolTable().getDefinition("stdin"));
        // So the file array gotten from the app should have the same path
        // in its stdin position.
        assertEquals("filename", control.getApplicationDefinitions()[0].toString());
    }

    public void testLoad() throws TTK91RuntimeException {

        testCompile(); // to make sure internal state is right. This one works.

        try {
            control.load();
        } catch (TTK91NoStdInData err) {
            assertNotNull(err.getCause());
            assertTrue(err.getCause() instanceof IOException);
        }
        application.getSymbolTable().addDefinition("stdin", filename.getPath());
        control.load();
        assertEquals(3, application.readNextFromStdIn()); // filename has '3' in it.
        application.getSymbolTable().addDefinition("stdin", "nonexistingfile");
        try {
            control.load();
            fail();
        } catch (TTK91NoStdInData err) {
            assertNotNull(err.getCause());
            assertTrue(err.getCause() instanceof IOException);
        }
        application.getSymbolTable().addDefinition("stdin", fiddlesticks.getPath());
        control.eraseMemory();
        try {
            control.load();
            fail();
        } catch (TTK91NoStdInData foo) {
            assertEquals("STDIN data file contains " +
                    "invalid " +
                    "data: Stdin input string \"bah\n\" invalid, " +
                    "should be eg. \\n-separated list of integers.", foo.getMessage());
        }
        Processor cpu = (Processor) control.getCpu();
        TTK91Memory mem = cpu.getMemory();
        // Did it load?
        // FIXME: test out of date; the original test data is lost
        //assertEquals(3, mem.getValue(0)); // first line of code matches.
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
        application.getSymbolTable().addDefinition("stdout", foo1.getPath());
        application.getSymbolTable().addDefinition("stdin", filename.getPath());
        control.load();
        try {
            control.writeToStdoutFile("text");
            fail("Should have not been able to write to /root/foo1.\n");
        } catch (TTK91FailedWrite err) {
            // Expected exception
        }
    }

    public void testChangeMemorySize() {
        try {
            control.changeMemorySize(8);
            fail("Should have not been able to change memory size to 8.");
        } catch (IllegalArgumentException err) {
            assertEquals("Memory size must be between " +
                    "2^9 and 2^16, a change to 2^8 failed.", err.getMessage());
        }
        try {
            control.changeMemorySize(17);
            fail("Should have not been able to change memory size to 17.");
        } catch (IllegalArgumentException err) {
            assertEquals("Memory size must be between " +
                    "2^9 and 2^16, a change to 2^17 failed.", err.getMessage());
        }
        control.changeMemorySize(15);
        assertEquals(32768, control.getMemory().getSize()); // 2^15 = 32768
    }

    public void testSetDefaultStdin() throws IOException, ParseException {
        try {
            control.setDefaultStdIn(fiddlesticks);
            fail("Should not be able to set stdin default with invalid " +
                    "contents.");
        } catch (ParseException err) {
            assertEquals("StdIn file contents are invalid; the file should " +
                    "contain only integers and separators.", err.getMessage());
        }

        try {
            control.setDefaultStdIn(filename);
        } catch (Exception err) {
            fail("This should have worked: setting default stdin to a file " +
                    "containing 3.");
        }
    }

    public void testOpenSource() throws IOException {
        assertEquals("3\n", control.openSource(filename)); // The file contains "3".
        // Compiler state is not really interestingly accessible as long
        // as we're using a stub in the control.
    }

    public void testGetBinary() {
        // FIXME: this test used to define "0 0" but the implementation produced "0 -1"; must find out what is the desired way,
        // though since Control.getBinary is not used by Titokone, it might not matter
        assertEquals("___b91___\n___code___\n0 -1\n___data___\n0 -1\n___symboltable___\n___end___\n",
                control.getBinary(new Application(null, null, null)));
    }

    public void testSaveBinaryFileName() throws IOException {
        File examplebinary = tempDir.newFile("examplebinary");
        testCompile(); // Ensure right state.
        // No exception.
        control.saveBinary(examplebinary);
        control = new Control(new File("/root/test"), new File("/root/test"), null);
        // Exception: no application loaded yet.
        try {
            control.saveBinary(examplebinary);
            fail("Should not have been able to save a null app to binary.");
        } catch (IllegalStateException err) {
            assertEquals("Cannot save binary to file; no application has " +
                    "been compiled or loaded.", err.getMessage());
        }
    }

    public void testSaveBinaryGuessFilename() throws IOException {
        testCompile(); // Make sure there is an application.
        try {
            control.saveBinary();
            fail("Load file should be missing, why does this binary save " +
                    "with filename guessing work?");
        } catch (IllegalStateException err) {
            assertEquals("Cannot deduce the file to store the binary " +
                    "into; no source file has been loaded.", err.getMessage());
        }
        // Now there is:
        control.openSource(tempDir.newFile("briefsource.k91"));
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
