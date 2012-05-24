package fi.hu.cs.titokone;

import junit.framework.*;

public class SiniTestit extends TestCase {
    public static Test suite() {
	TestSuite suite = new TestSuite("Sinin testijoukko");
	// Add your own tests here.
	// suite.addTestSuite(TranslatorTest.class);
	//suite.addTestSuite(ApplicationTest.class);
	//suite.addTestSuite(SettingsTest.class);
	suite.addTestSuite(ControlTest.class);
	return suite;
    }

    public static void main(String[] args) {
	if(args != null && args.length > 0 && args[0].equals("text"))
	    junit.textui.TestRunner.run(suite());
	else
	    junit.swingui.TestRunner.run(SiniTestit.class);
    }
}
