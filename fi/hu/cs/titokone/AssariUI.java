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
   
   res.setContentType("text/html");
   ServletOutputStream out= res.getOutputStream();        
   out.println("<html><head><title>Assari User Interface</title></head>");
   out.println("<body><form method=\"POST\" action=\"AssariUI\" method=\"get\">");
   out.println("<table border=\"0\" width=\"100%\">");
   out.println("<tr><td width=\"20%\">TTK-91 code</td><td width=\"20%\"></td>");
   out.println("<td width=\"20%\"></td><td width=\"20%\"></td><td width=\"20%\"></td></tr>");
   out.println("<tr><td width=\"20%\" rowspan=\"4\"><textarea rows=\"11\" name=\"code\" cols=\"33\">");
   
   //tähän seuraavaan printn-kenttään tulee sama lähdekoodi
   out.println(req.getParameter("code") + "</textarea></td><td width=\"20%\"></td><td width=\"20%\" rowspan=\"4\"></td>");
   
   out.println("<td width=\"20%\" rowspan=\"4\"></td><td width=\"20%\" rowspan=\"4\"></td></tr><tr>");  
   out.println("<td width=\"20%\"><input type=\"submit\" value=\"Käännä ohjelma\" name=\"B1\"></td>");
   out.println("</tr><tr><td width=\"20%\"><input type=\"submit\" value=\"Compile and run\" name=\"B2\"></td>");
   out.println("</tr><tr><td width=\"20%\"><input type=\"submit\" value=\"Run\" name=\"B3\">");
   out.println("<input type=\"text\" name=\"lines\" size=\"3\" value=\"" +req.getParameter("lines") + "\"> lines </td></tr>");
   out.println("<tr><td width=\"20%\">KBD input</td><td width=\"20%\"></td>");
   out.println("<td width=\"20%\">Program output</td><td width=\"20%\"></td>");
   out.println("<td width=\"20%\"></td></tr><tr>");

String error = "";
String kbd = "";
kbd = req.getParameter("kbd");


//tähän samat myös
   out.println("<td width=\"20%\"><textarea rows=\"8\" name=\"kbd\" cols=\"33\">"+ kbd + "</textarea></td>");
   out.println("<td width=\"20%\" rowspan=\"3\"></td>");
   out.println("<td width=\"20%\" rowspan=\"3\"><textarea rows=\"17\" name=\"output\" cols=\"33\">");

source = new Source(req.getParameter("code"));

core = new Control(null, null);

boolean runningException = false;

String kbdInput = "";
output = "";
String status = "";

   if(req.getParameter("B1")!=null) {
      try {
	    app = core.compile(source);
	    app.setKbd(req.getParameter("kbd"));
	    app.setStdIn(req.getParameter("stdin"));
	    status = "Compilation ok!";
      } catch(TTK91Exception e) {
	   runningException = false;	
	   error = e.toString();   
      } catch(IllegalArgumentException e) {
	   runningException = false;
 	   error = e.toString();
      }
    } else if(req.getParameter("B2")!=null) {
      try {
         app = core.compile(source);
	     app.setKbd(req.getParameter("kbd"));
         app.setStdIn(req.getParameter("stdin"));
         
         core.run(app, 0);
	 memory = core.getMemory();
         cpu = core.getCpu();
         status = "Execution ok!";
         output = createOutput();
      } catch(TTK91Exception e) {
          runningException=true;
	      error = e.toString();
      } catch(IllegalArgumentException e) {
	    runningException = false;
            error = e.toString();
      }
   }
   else if(req.getParameter("B3")!=null) {
	   try {
         app = core.compile(source);
	 app.setKbd(req.getParameter("kbd"));
         app.setStdIn(req.getParameter("stdin"));
         
         if(req.getParameter("lines").equals("") || req.getParameter("lines").matches("[^0123456789]"))
	        error = "Invalid number of lines.";
         else {
          	 int lines = Integer.parseInt(req.getParameter("lines"));
         	core.run(app, lines);
         	memory = core.getMemory();
		cpu = core.getCpu();
         	status = lines + " lines executed.";
         	output = createOutput();
		}
        } catch(TTK91Exception e) {
          runningException=true;
	      error = e.toString();
      } catch(IllegalArgumentException e) {
	     runningException = true;
             error = e.toString();
	}
 

   }
       
   
   //tämä kenttä on keskeinen: tulosteet painetun napin mukaan
   out.println(output + "</textarea></td>");
   out.println("<td width=\"20%\" rowspan=\"3\"></td><td width=\"20%\" rowspan=\"3\"></td>");
   out.println("</tr><tr><td width=\"20%\">STDIN input</td></tr><tr>");

   //tarkistetaan parametri

   String stdin = "1";
   if(!req.getParameter("stdin").equals(""))
     stdin = req.getParameter("stdin");



   out.println("<td width=\"20%\"><textarea rows=\"7\" name=\"stdin\" cols=\"33\">" + stdin + "</textarea></td>");
   out.println("</tr></table><p>&nbsp;</p><p>&nbsp;</p>");
   if(error.equals(""))
      out.println("<font face=\"sans-serif\" size=4 color=\"blue\">" + status + "</font>");
   else {
       if(runningException)
           error = "Execution failed:" + error.substring(error.indexOf(':')+1);
       else
	    error = error.substring(error.indexOf(':')+1);
       out.println("<font face=\"sans-serif\" size=4 color=\"red\">" + error + "</font>");
   }
       out.println("</body></html>");


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
	            "\nCRT: " + app.readCrt() +
	            "\n---------------------" + 
                    "\nSTDOUT: " + app.readStdOut() +
                    "\n---------------------" +
		    "\nMemorylines 0-9: " + 
                    "\n0: " + memory.getValue(0) +
		    "\n1: " + memory.getValue(1) +
                    "\n2: " + memory.getValue(2) +
                    "\n3: " + memory.getValue(3) +
                    "\n4: " + memory.getValue(4) +
                    "\n5: " + memory.getValue(5) +
                    "\n6: " + memory.getValue(6) +
                    "\n7: " + memory.getValue(7) +
                    "\n8: " + memory.getValue(8) +
                    "\n9: " + memory.getValue(9);
	 return output;
	
}



}


