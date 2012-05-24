package fi.hu.cs.titokone;

import junit.framework.*;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.*;
import fi.hu.cs.ttk91.*;

public class ApplicationTest extends __LoguserTestCase {

    protected void setUp() {}

    public void testConstructor() {
	Application app;
	// The stub_memorylines always return 60 for their getBinary().
	__stub_memoryline[] foo = { new __stub_memoryline() };
	// Null variables should be replaced by empty actual variables
	// by the application.
	app = new Application(null, null, null);
	assertEquals(app.getCode().length, 0);
	assertEquals(app.getInitialData().length, 0);
	assertTrue(app.getSymbolTable() != null);
	// Then try one which has content.
	app = new Application(foo, foo, new __stub_symboltable());
	assertEquals("One-length code area", app.getCode().length, 1);
	assertEquals(app.getCode()[0].getBinary(), 60);
	assertEquals("One-length data area", app.getInitialData().length, 1);
	assertEquals(app.getInitialData()[0].getBinary(), 60);
	assertTrue(app.getSymbolTable() != null);
    }

    public void testWrites() {
	Application app;
	app = new Application(null, null, null);
	// STDOUT writing.
	assertEquals(app.readStdOut(), "");
	app.writeToStdOut(30);
	app.writeToStdOut(3);
	assertEquals("App read", app.readStdOut(), 
		     "30" + System.getProperty("line.separator", "\n") + "3" +
		     System.getProperty("line.separator", "\n"));
	// The read should also have emptied the buffer.
	assertEquals(app.readStdOut(), "");
	// CRT writing.
	assertEquals(app.readCrt(), "");
	app.writeToCrt(42);
	app.writeToCrt(4);
	assertEquals("Crt read", app.readCrt(), 
		     "42" + System.getProperty("line.separator", "\n") + 
		     "4" + System.getProperty("line.separator", "\n"));
	assertEquals(app.readCrt(), "");
    }
    
    public void testInputCheck() {
	assertTrue(Application.checkInput("3 45 67"));
	assertTrue(Application.checkInput("-3 ;: 6;:\n\r\n7"));
	assertTrue(!Application.checkInput("-3 5 a"));
    }

    public void testKbdInputBuffer() throws Exception {
	Application app = new Application(null, null, null);
	app.setKbd("3 45 67");
	assertEquals(app.readNextFromKbd(), 3);
	assertEquals(app.readNextFromKbd(), 45);
	assertEquals(app.readNextFromKbd(), 67);
	try {
	    app.readNextFromKbd();
	    fail("Should have thrown an exception now: no more data.");
	}
	catch(TTK91NoKbdData err) {}
	app.setKbd("-3 ;: 6;:\n\r\n7");
	assertEquals(app.readNextFromKbd(), -3);
	assertEquals(app.readNextFromKbd(), 6);
	assertEquals(app.readNextFromKbd(), 7);
	try {
	    app.readNextFromKbd();
	    fail("Should have thrown an exception now: no more data.");
	}
	catch(TTK91NoKbdData err) {}
	try {
	    app.setKbd("-3 5 a");
	    fail("Should have thrown an exception now: invalid input.");
	}
	catch(IllegalArgumentException err) {}
	
    }

    public void testStdInputBuffer() throws Exception {
	Application app = new Application(null, null, null);
	app.setStdIn("3 45 67");
	assertEquals(app.readNextFromStdIn(), 3);
	assertEquals(app.readNextFromStdIn(), 45);
	assertEquals(app.readNextFromStdIn(), 67);
	try {
	    app.readNextFromStdIn();
	    fail("Should have thrown an exception now: no more data.");
	}
	catch(TTK91NoStdInData err) {}
	app.setStdIn("-3 ;: 6;:\n\r\n7");
	assertEquals(app.readNextFromStdIn(), -3);
	assertEquals(app.readNextFromStdIn(), 6);
	assertEquals(app.readNextFromStdIn(), 7);
	try {
	    app.readNextFromStdIn();
	    fail("Should have thrown an exception now: no more data.");
	}
	catch(TTK91NoStdInData err) {}
	try {
	    app.setStdIn("-3 5 a");
	    fail("Should have thrown an exception now: invalid input.");
	}
	catch(IllegalArgumentException err) {}
	
    }
}
