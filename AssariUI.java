package fi.hu.cs.titokone;

import javax.servlet.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;

/** This class is a servlet providing a HTML interface to the TTK91
    simulator's core functions. It provides a method for compiling a
    program (the output is printed out, but not stored as such), for
    compiling and running a program either in whole or for a fixed
    number of commands (the program's output to screen and STDOUT is
    printed out, as is the memory up to the stack pointer and the
    registers' values and computer state). The TTK91 simulatee computer
    is recreated for each request. */
public class AssariUI extends HttpServlet {
    private Settings settings;
    /** This field defines the value to input in the command counter to
	execute commands until the program stops. In practice, the value 
	is replaced by MAX_CYCLES. See also MAX_CYCLES. */
    public static final int EXECUTE_ALL = 0;
    /** This field contains the maximum number of TTK91 cpu cycles the 
	given TTK91 code is allowed to execute until it is assumed to be 
	in an infinite loop and is stopped. */
    public static final int MAX_CYCLES = 2000;

    /** This method sets up a new AssariUI servlet. It initializes a new
	TTK91Core for its use, and sets up the necessary settings for it. */
    public AssariUI() {}

    /** This method responds to a GET command. It produces a new HTML
	page containing a form gotten from makeForm() according to the
	user input, possibly after creating a computer and running a
	posted program on it. Any exceptions from the TTK91Core are
	caught and error messages are written to the user. The
	parameters and exceptions thrown are defined in HttpServlet's
	corresponding method. */
    public void doGet(HttpServletRequest request, 
		      HttpServletResponse response) 
	throws ServletException, IOException {
	// use "request" to read incoming HTTP headers (eg. cookies)
	// and HTML form data. Use "response" to specify the HTTP
	// response line and headers, eg. specifying the content type,
	// setting cookies. 
	// Use PrintWriter out = response.getWriter() to send content
	// to the browser.
	// response.setContentType("text/html")
	// out.println("<!DOCTYPE HTML PUBLIC ...")
    } 

    /** This method respons to a POST command. It calls doGet with its 
	parameters and throws what it would throw. See doGet. */
    public void doPost(HttpServletRequest request, 
		       HttpServletResponse response) 
	throws ServletException, IOException {
	// Tämän voi pistää kutsumaan doGettiä.
	// request.getParameter("paramname") 
	// dogettiä.
	// Enumeration paramNames = request.getParameterNames();
	// while paramnames.hasMoreElements()...
	// String[] paramValues = request.getParameterValues(paramName).
	// <form action="polkumeidanservlettiin" method="post" (isoa dataa)>
	// <textarea name="foo" rows="3" cols="40"></textarea>
	// <input type="submit" value="run">
	// <input type="submit" value="compile"> jne.
	// <input type="text" name="bar" /> 
	// </form>
    }

    /** This method produces a string containing an HTML form that 
	contains the input fields. 
	@param code The code string to insert in the TTK91 code text field. 
	@param kbdInput The input string to insert in the KBD input text 
	field. 
	@param fileInput The input string to insert in the STDIN input 
	text field. 
	@param printout The output string to insert in the Program Output 
	text field. 
	@param commandCounter The number to insert in the counter for how 
	many commands should be executed, or 0 if the field has either not
	been changed or has been changed to an invalid value (nonnumeric). 
	@return A string containing the form. */
    private String makeForm(String code, String kbdInput, String fileInput,
	String printout, int commandCounter) {}

    /** This method gathers a printout of the computer's state after
	running an application and returns it. 
	@param computer A TTK91Core to draw the state information from.
	@return A string describing the computer's state, including things
	it printed out. */
    private String getPrintOut(TTK91Core computer) {}
}
