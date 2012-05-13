import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import fi.hu.cs.titokone.*;
import fi.hu.cs.ttk91.*;
import java.text.*;

public class AssariUI extends HttpServlet { 

TTK91CompileSource source;
TTK91Core core;
TTK91Application app;
TTK91Cpu cpu;
TTK91Memory memory;
String output;

public void doGet(HttpServletRequest req, HttpServletResponse res)
   throws ServletException, IOException {

   String code = req.getParameter("code");
   String noOflines = req.getParameter("lines");
   String kbd = req.getParameter("kbd");
   String stdin = req.getParameter("stdin");
   String error = "";
   output = "";
   String status = "";   
      
   source = new Source(req.getParameter("code"));
   core = new Control(null, null);
   boolean runningException = false;
            
   //COMPILE
   if(req.getParameter("B1")!=null) {
      try {
	    app = core.compile(source);
	    status = "Compilation ok!";
      } catch(TTK91Exception e) {
	   runningException = false;	
	   error = e.toString();   
	
      } //COMPILE AND RUN
    } else if(req.getParameter("B2")!=null) {
      try {
         app = core.compile(source);
	 runningException = true;
         app.setKbd(req.getParameter("kbd"));
         app.setStdIn(req.getParameter("stdin"));
         
         core.run(app, 0);
	 memory = core.getMemory();
         cpu = core.getCpu();
         status = "Execution ok!";
         output = createOutput();
      } catch(TTK91Exception e) {
	      error = e.toString();
      } catch(IllegalArgumentException e) {
	    runningException = false;
            error = e.toString();
      }
   } // RUN
   else if(req.getParameter("B3")!=null) {
	   try {
         app = core.compile(source);
	 runningException=true;
	 app.setKbd(req.getParameter("kbd"));
         app.setStdIn(req.getParameter("stdin"));
         
          	int lines = Integer.parseInt(req.getParameter("lines"));
         	core.run(app, lines);
         	memory = core.getMemory();
		cpu = core.getCpu();
         	status = lines + " lines executed.";
         	output = createOutput();
        } catch(TTK91Exception e) {
	      error = e.toString();
      } catch(IllegalArgumentException e) {
             error = e.toString();
	}
   }
   String printstate;
   if(error.equals(""))
      printstate = "<p><font face=\"sans-serif\" size=4 color=\"blue\">" + status + "</font></p>";
   else {
       if(runningException)
           error = "Execution failed:" + error.substring(error.indexOf(':')+1);
       else
	   error = error.substring(error.indexOf(':')+1);
       printstate = "<p><font face=\"sans-serif\" size=4 color=\"red\">" + error + "</font></p>";
   }
               
   res.setContentType("text/html");
   ServletOutputStream out= res.getOutputStream();        
   out.println("<html><head><title>Assari User Interface</title></head>");
   out.println("<body bgcolor=\"#CCCCCC\">");
   
   out.println(printstate);
   
   out.println("<form method=\"POST\" action=\"AssariUI\" method=\"get\"><table border=\"0\" width=\"100%\">");
   out.println("<tr><td width=\"20%\">TTK-91 code</td><td width=\"20%\"></td>");
   out.println("<td width=\"20%\"></td><td width=\"20%\"></td><td width=\"20%\"></td></tr>");
   out.println("<tr><td width=\"20%\" rowspan=\"4\"><textarea rows=\"11\" name=\"code\" cols=\"33\">");
   
   out.println(code);
   
   out.println("</textarea></td><td width=\"20%\"></td><td width=\"20%\" rowspan=\"4\"></td>");
   out.println("<td width=\"20%\" rowspan=\"4\"></td><td width=\"20%\" rowspan=\"4\"></td></tr><tr>");  
   out.println("<td width=\"20%\"><input type=\"submit\" value=\"Compile\" name=\"B1\"></td>");
   out.println("</tr><tr><td width=\"20%\"><input type=\"submit\" value=\"Compile and run\" name=\"B2\"></td>");
   out.println("</tr><tr><td width=\"20%\"><input type=\"submit\" value=\"Compile and then run\" name=\"B3\">");
   out.println("<input type=\"text\" name=\"lines\" size=\"3\" value=\"");
   
   out.println(noOflines);
   
   out.println("\"> lines </td></tr>");
   out.println("<tr><td width=\"20%\">KBD input</td><td width=\"20%\"></td>");
   out.println("<td width=\"20%\">Program output</td><td width=\"20%\"></td>");
   out.println("<td width=\"20%\"></td></tr><tr>");
   out.println("<td width=\"20%\"><textarea rows=\"8\" name=\"kbd\" cols=\"33\">");
   
   out.println(kbd);
   
   out.println("</textarea></td>");
   out.println("<td width=\"20%\" rowspan=\"3\"></td>");
   out.println("<td width=\"20%\" rowspan=\"3\"><textarea rows=\"17\" name=\"output\" cols=\"33\">");
   out.println(output + "</textarea></td>");
   out.println("<td width=\"20%\" rowspan=\"3\"></td><td width=\"20%\" rowspan=\"3\"></td>");
   out.println("</tr><tr><td width=\"20%\">STDIN input</td></tr><tr>");
   out.println("<td width=\"20%\"><textarea rows=\"7\" name=\"stdin\" cols=\"33\">");
   
   out.println(stdin);
   
   out.println("</textarea></td></tr></table></body></html>");
   
       


}
public void doPost(HttpServletRequest req, HttpServletResponse res)
   throws ServletException, IOException {
    // do the same as doget
   doGet(req,res);

}

private String createOutput() {
	output = "Registers: \nR0: " + cpu.getValueOf(TTK91Cpu.REG_R0) +
	            "\nR1: " + cpu.getValueOf(TTK91Cpu.REG_R1) +
	            "\nR2: " + cpu.getValueOf(TTK91Cpu.REG_R2) +
	            "\nR3: " + cpu.getValueOf(TTK91Cpu.REG_R3) +
	            "\nR4: " + cpu.getValueOf(TTK91Cpu.REG_R4) +
	            "\nR5: " + cpu.getValueOf(TTK91Cpu.REG_R5) +
	            "\nSP: " + cpu.getValueOf(TTK91Cpu.REG_R6) +
	            "\nFP: " + cpu.getValueOf(TTK91Cpu.REG_R7) +
	            "\n---------------------" +
	            "\nCRT:\n" + app.readCrt() +
	            "\n---------------------" + 
                    "\nSTDOUT:\n" + app.readStdOut() +
                    "\n---------------------" +
		    "\nMemorylines 0-49: ";
        //muistirivien täyttäminen output-ikkunaan, oletus: 50 riviä
	for(int i=0; i < 50; i++) 
                    output += "\n"+i+": " + memory.getValue(i);
	 return output;
	
}



}


