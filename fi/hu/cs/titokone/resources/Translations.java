package fi.hu.cs.titokone.resources;

import java.util.ListResourceBundle;

/** This translation is what all translations should be based on. It 
    translates keys to plain English. Note that not all keys are equal 
    to their values; the key may contain a specification part which
    should not be translated. The specification part ought to be 
    put between ? and :, eg. "?Menu:File" would mean "translate the 
    word 'File' as if you would see it on the common menu name" as 
    opposed to "?Utility item:File", which might mean "translate the 
    word 'File' as you would if it meant a nail file". 
    Use */
class Translations extends ListResourceBundle {
   public Object[][] getContents() { 
     return contents;
   }

  protected static final Object[][] contents = {
  // Localize below, pairs of key-value (what key is in your language)...
      // Class: Application.
      // General messages: (none)
      // Exception messages:
      { "No more keyboard data stored on application." , 
        "No more keyboard data stored on application." },
      { "No more stdin data stored on application.", 
	"No more stdin data stored on application." },
      {	"Keyboard input string \"{0}\" invalid, should be eg. \n-separated " +
	"list of integers." , 
	"Keyboard input string \"{0}\" invalid, should be eg. \n-separated " +
	"list of integers." },
      { "Stdin input string \"{0}\" invalid, should be eg. \n-separated " +
	"list of integers.", 
	"Stdin input string \"{0}\" invalid, should be eg. \n-separated " +
	"list of integers." },
      { , },
      { , },
      // Log messages:
      { "Application has no more keyboard data, read: {0}, buffer length " +
	"{1}.", 
	"Application has no more keyboard data, read: {0}, buffer length " +
	"{1}." },
      { "Application has no more stdin data, read: {0}, buffer length {1}." , 
	"Application has no more stdin data, read: {0}, buffer length {1}." },
      { "Accepted \"{0}\" as keyboard input, tokens found: {1}." , 
	"Accepted \"{0}\" as keyboard input, tokens found: {1}." },
      { "Accepted \"{0}\" as stdin input, tokens found: {1}.", 
	"Accepted \"{0}\" as stdin input, tokens found: {1}." }      
      // Class: 
      // General messages:
      // Exception messages:
      // Log messages:
      // ...
  // Localizable bit ends.
  }
}
