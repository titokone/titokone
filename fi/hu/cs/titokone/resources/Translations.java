package fi.hu.cs.titokone.resources;

import java.util.ListResourceBundle;

/** This translation is what all translations should be based on. It 
    translates keys to plain English. Note that not all keys are equal 
    to their values; the key may contain a specification part which
    should not be translated. The specification part ought to be 
    put between ? and :, eg. "?Menu:File" would mean "translate the 
    word 'File' as if you would see it on the common menu name" as 
    opposed to "?Utility item:File", which might mean "translate the 
    word 'File' as you would if it meant a nail file". */
class Translations extends ListResourceBundle {
   public Object[][] getContents() { 
     // To be simultaneously compatible with the general ListResourceBundle
     // functionality and provide a base template array for other 
     // translations, we do not repeat every line in the emptyContents 
     // array, but instead arrange the duplication when asked for
     // the real contents. (Only necessary the first time.)
     if(contents == null) {
       contents = new String[emptyContents.length][];
       for(int i = 0; i < emptyContents.length; i++) {
         contents[i] = new String[2];
         contents[i][0] = emptyContents[i][0];
         contents[i][1] = emptyContents[i][0];
       }
     }
     return contents;
   }
     
   protected static Object[][] contents;

   private static final Object[][] emptyContents = {
      // Localize below, pairs of key-value (what key is in your language)...
      // Remove lines which you do not wish to translate completely. Leaving
      // in a value of "" will translate the message to "" as well.

      // Class: Application.
      // General messages: (none)
      // Exception messages:
      { "No more keyboard data stored on application." , 
        null },
      { "No more stdin data stored on application.", 
	null },
      {	"Keyboard input string \"{0}\" invalid, should be eg. \n-separated " +
	"list of integers." , 
	null },
      { "Stdin input string \"{0}\" invalid, should be eg. \n-separated " +
	"list of integers.", 
	null },
      // Log messages:
      { "Application has no more keyboard data, read: {0}, buffer length " +
	"{1}.", 
	null },
      { "Application has no more stdin data, read: {0}, buffer length {1}.", 
	null },
      { "Accepted \"{0}\" as keyboard input, tokens found: {1}.", 
	null },
      { "Accepted \"{0}\" as stdin input, tokens found: {1}.", 
	null }, 

      // Class: Settings.
      // General messages: (none)
      // Exception messages: 
      { "value", null },
      { "a linebreak", null },
      { "Illegal {0} \"{1}\", contains {2}.", null },
      { "Illegal {0}: null. Try an empty string instead.", null },
      { "Syntax error on line {0}, which was: \"{1}\".", null },
      // Log messages: 
      { "Settings successfully parsed, lines: {0}, unique keys found: {1}.", 
	null },

      // Class: 
      // General messages: 
      // Exception messages: 
      // { , ""}, 
      // ... 

      // Localizable bit ends. 
      // TODO: poista rivi alta, viim. ylläolevan parin perästä pilkku pois. 
      { "Eliminator of comma-problems.", "Remove when ready!" } 
  };
}
