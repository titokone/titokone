import java.util.ResourceBundle;
import java.util.Locale;
import java.text.MessageFormat;
import java.util.MissingResourceException;

import java.net.*;

/** This class deals with translating strings. It also remembers which
    language is currently set and what languages are
    available. (...) */
public class Translator {
    /** This name identifies the resource files containing translations 
	for this software. */
    public static final String resourceFamilyName = "Translations";
    public static final Locale defaultLocale = Locale.ENGLISH;

    public static void main(String[] args) {
       ResourceBundle resources =
       ResourceBundle.getBundle(resourceFamilyName, new Locale("fi", "FI"));
       MessageFormat formatter = new MessageFormat(""); 
       Object[] strings = { "foo", "bar" };
       Object[] tooshortstrings = { "soup" };
       formatter.setLocale(new Locale("fi", "FI"));
       formatter.applyPattern(resources.getString("Example"));
       System.out.println(formatter.format(strings));
       System.out.println(formatter.format(tooshortstrings)); // jää {1}.
       System.out.println(resources.getString("MenuFile"));
       System.out.println(resources.getString("Foo"));
       try {
	   System.out.println(resources.getString("Foo 2 demonstrating " +
						  "spaces in keys"));
       }
       catch(MissingResourceException spaces_not_allowed_in_keys) {
	   System.out.println("Spaces do not work with keys.");
       }
       try {
	   System.out.println(resources.getString("Bar 3 \\= not working?"));
       }
       catch(MissingResourceException spaces_not_allowed_in_keys) {
	   System.out.println("Quoting does not help with =.");
       }
       try {
	   System.out.println(resources.getString("Zvip Zvap"));
       }
       catch(MissingResourceException spaces_not_allowed_in_keys) {
	   System.out.println("Quoting does not help at all.");
       }
       try {
	   System.out.println("X" + resources.getString("foo") + "X");
       }
       catch(MissingResourceException err) {
	   System.out.println("Translation for \"foo\" was not found.");
       }
       System.out.println("X" + resources.getString("Drop") + "X");
       System.out.println(resources.getString("\"Zvip"));
       System.out.println(resources.getString("Drep,drip"));

       System.out.println();


       URL[] url = new URL[1];
       try {
	   url[0] = new URL("file:///home/sini/Ohtu/src/po/");
	   java.net.URLClassLoader loader = new java.net.URLClassLoader(url);
	   Object lang = loader.loadClass("Translations_fi").newInstance();
	   resources = (ResourceBundle) lang;
       }
       catch(Exception err) {
	   System.out.println(err + "FOO");
       }
       if(resources == null)
	   System.out.println("Resources are null.");
       else {
	   System.out.println(resources.getString("Drep,drip"));
       }
    }

    // Käännöstiedostojen haku yhdestä hakemistosta: File: 
    // listFiles(FileFilter filter)
    // See:
    // http://java.sun.com/docs/books/tutorial/i18n/format/messageFormat.html
    // http://java.sun.com/j2se/1.4.2/docs/api/java/text/MessageFormat.html

    /*
      From ResourceBundle documentation: Otherwise, getBundle attempts
      to locate a property resource file. It generates a path name
      from the candidate bundle name [basename given as parameter + _ +
      language + _ + country + _ + variant] by replacing all "."
      characters with "/" and appending the string ".properties". It
      attempts to find a "resource" with this name using
      ClassLoader.getResource. (Note that a "resource" in the sense of
      getResource has nothing to do with the contents of a resource
      bundle, it is just a container of data, such as a file.) If it
      finds a "resource", it attempts to create a new
      PropertyResourceBundle instance from its contents. If
      successful, this instance becomes the result resource bundle.

    */
}
