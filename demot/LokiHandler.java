import java.util.logging.*;

public class LokiHandler extends Handler {
    public void flush() {}
    public void close() {}
    public void publish(LogRecord record) {
	// (Oletusfiltteriä ei ole.)
	System.out.println("<" + record.getSourceClassName() + ": " + 
			   record.getSourceMethodName() + " - " +
			   record.getMessage() + " (level oli " + 
			   record.getLevel() + ")" + ">");
    }
}
