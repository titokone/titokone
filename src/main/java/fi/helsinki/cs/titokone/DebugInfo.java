package fi.helsinki.cs.titokone;

/**
 * This class is parent class for LoadInfo, CompileInfo and RunInfo. It
 * contains two fields for storing messages.
 */


public class DebugInfo {

    public DebugInfo() {
    }

    /**
     * This field contains message to the GUI's statusbar.
     */
    private String statusMessage;

    /**
     * This field contains comments made by debugger.
     */
    private String comments;

    /**
     * This method sets statusMessage field to given string.
     *
     * @param statusMessage String containing the message.
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * This method sets comments field to given string.
     *
     * @param comments String containing comments.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }


    /**
     * This method returns statusmessage.
     *
     * @return String containing the message.
     */
    public String getStatusMessage() {
        return this.statusMessage;
    }

    /**
     * This method returns comments.
     *
     * @return String containing comments..
     */
    public String getComments() {
        return this.comments;
    }
}
