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
     return contents;
   }

  protected static final Object[][] contents = {
  // Localize below, pairs of key-value (what key is in your language)...
    { "Menu:File", "File" },
    { "Menu:Options", "Options" }
  // Localizable bit ends.
  }
}
