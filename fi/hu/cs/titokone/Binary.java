package fi.hu.cs.titokone;

import java.text.ParseException;

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
	contents of a binary file. 
	@throws ParseException If the string does not represent a 
	syntactically correct binary. */
    public Binary(String contents) throws ParseException { }

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
	@return An application instance corresponding to the binary. 
	@throws ParseException If the binary contents are not 
	syntatically correct. */
    public Application toApplication() throws ParseException { }

    /** This method determines (if it is not done already), stores and
	returns the String representation in .b91 format of this
	binary.  The linebreaks used vary, as one of the constructors
	accepts a premade string; if the Application constructor has
	been used, the linebreak will be
	System.getProperty(line.separator, "\n"). An example program in .b91
	format might look like this (comments in parentheses are not 
	a part of the file format):<br>
	<pre>
        ___b91___
        ___code___
        0 9          (numbers of the first and the last line of code,
        52428801      the latter also being the initial value of FP)
	18874378
	572522503
	36175883
	287834122
	18874379
	538968064
	36175883
	69206016
	1891631115
	___data___
	10 11         (numbers the first and the last line of the data 
	0              area, the latter also being the initial value of SP;
	0              then follow the contents of the data area in order)
	___symboltable___
	luku 10        (only local symbols are included, eg. HALT is 
	summa 11        considered built-in)
	___end___
	</pre>
	@return The String representation of this binary. */
    public String toString() { }
}
