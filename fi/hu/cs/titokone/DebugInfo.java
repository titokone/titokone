package fi.hu.cs.titokone;

/** This class is parent class for LoadInfo, CompileInfo and RunInfo. It
    contains two fields for storing messages.
*/



public class DebugInfo{
    
    public DebugInfo(){}

    /** This field contains message to the GUI's statusbar.*/
    private Message statusMessage;
    
    /** This field contains comments made by debugger. */
    private Message comments;

    /** This method sets statusMessage field to given string.
	@param statusMessage String containing the message.
    */
    public void setStatusMessge(String statusMessage){ 
	this.statusMessage = new Message(statusMessage);
    }

    /** This method sets comments field to given string.
	@param comments String containing comments.
    */
    public void setComments(String comments){
	this.comments = new Message(comments);
    }


    /** This method returns statusmessage.
	@return String containing the message.
    */
    public String getStatusMessage(){
	return this.statusMessage.toString();
    }

    /** This method returns comments.
	@return String containing comments..
    */
    public String getComments(){
	return this.comments.toString();
    }
}
