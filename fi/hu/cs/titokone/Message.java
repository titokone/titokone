package fi.hu.cs.titokone;

/** This class represents a translatable message to the user. It uses
    the Translator class statically to deal with the actual translation. */
public class Message {
    private String messageKey;
    private String[] parameters;

    /** This constructor sets up a static message. 
    @param messageKey The identifying key, possibly equal to the default
    English message. The value is used to find the translation. */
    public Message(String messageKey) {
        this.messageKey = messageKey;
        parameters = null;
    }

    /** This constructor sets up a message with modifiable parts, and sets
    what they will be replaced with. 
    @param templateKey The identifying key, possibly equal to the default
    English message. The value is used to find the translation. 
    @param parameter The value to replace a {0} marker in the template
    message. */
    public Message(String templateKey, String parameter) {
        this (templateKey);
        parameters = new String[1];
        parameters[0] = parameter;
    }

    /** This constructor sets up a message with modifiable parts, and sets
    what they will be replaced with. 
    @param templateKey The identifying key, possibly equal to the default
    English message. The value is used to find the translation. 
    @param parameters The values to replace {i} markers in the template
    message in order. If there are less strings in the array than there
    are markers, the remaining markers will show as such in the 
    resulting string. If there are more strings in the array than are 
    needed, the final ones are ignored. */
    public Message(String templateKey, String[] parameters) {
        this (templateKey);
        this.parameters = parameters;
    }

    /** This method translates the message and does any necessary
    replacement of parameters into the string itself. The translated
    message is formed over for each time toString() is called;
    if the locale is changed between two calls to the toString() of 
    the same instance of Message, the results may be different. See
    Translator for details.
    @return The translated string. */
    public String toString() {
        if (parameters == null)
            return Translator.translate (messageKey);
        else
            return Translator.translate (messageKey, parameters);
    }
}
