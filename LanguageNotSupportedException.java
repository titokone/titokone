/** This class represents an error condition where the string translation 
    system has been asked to use a language which it cannot offer any 
    support for. */
public class LanguageNotSupportedException extends RuntimeException {
    /** This constructor sets up a LanguageNotSupportedException. 
     @param errorMessage An error message to specify what went wrong. */
    public LanguageNotSupportedException(String errorMessage) {
	/* Pass the parameter to RuntimeException. */
    }
}
