package fi.hu.cs.titokone;

/** This class can load a TTK91Application. It changes the processor state
    accordingly. Everything is loaded when loadApplication is called. Function returns the state of 
    memory after loading. If it runs out of memory it throws a TTK91OutOfMemory exception.
  */
public class Loader { 

  /**This variable holds the current application to be loaded.
    */
  private Application application;
  
/**You can set the file to load. Each time an application is set to load, the counter is set to 
one.
*/
public void setFileToLoad(Application application){}

/**Loads an application to memory. LoadInfo contains all the needed information about the process.
	@return Info from the load procedure.
*/
public LoadInfo loadApplication(){} 

}
