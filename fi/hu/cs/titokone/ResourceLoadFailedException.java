package fi.hu.cs.titokone;

/** This class represents a situation where loading a ResourceBundle
    from a file and instantiating it has failed. */
public class ResourceLoadFailedException extends Exception {
    public ResourceLoadFailedException(String message) {
	super(message);
    }
}
