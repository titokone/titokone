/** This class represents the contents of a binary file. It can 
    interpret an Application instance of itself with the help of the 
    BinaryInterpreter class, as well as transforming an Application into
    a string in the binary file format. */
public class Binary {
    /** This field stores the application this binary represents, or 
	null if it has not yet been resolved. */
    private Application application;
    /** This field stores the binary contents of the application, or
	an empty string if the contents have not yet been resolved 
	(ie. the Application constructor has been used and toString() 
	has not been called). */
    private String contents;

    /** This constructor sets up a binary instance. 
	@param contents A linebreak-delimited string containing the 
	contents of a binary file. */
    public Binary(String contents) { }

    /** This constructor sets up a binary instance which has already
	been interpreted into an application. It delays interpreting
	the binary to string form until toString() is called. 
	@param application The application to form a binary of. */
    public Binary(Application application) { }

    /** This method transforms the binary into an application file. 
	It instantiates a BinaryInterpreter to help with the task and 
	stores the result internally to avoid needing to redo the 
	interpretation. If the class already knows what the application
	class corresponding to the binary is, it will just return that.
	@return An application instance corresponding to the binary. */
    public Application toApplication() { }

    /** This method determines (if it is not done already), stores and
	returns the String representation of this binary. The linebreaks
	used vary, as one of the constructors accepts a premade string; 
	if the Application constructor has been used, the linebreak will
	be System.getProperty(line.separator).
	@return The String representation of this binary. */
    public String toString() { }
}
