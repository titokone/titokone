package fi.hu.cs.titokone;

/** 	This class representes the situation where getApplication was called when compile
	was not done yet. */
public class InvalidStateException extends RuntimeException {
    /** This constructor sets up an instance of the class.
        @param message The message to describe the problem more
        verbosely, for the user's eyes. */
    public InvalidStateException(String message) {}
}

