import fi.hu.cs.ttk91.TTK91OutOfMemory;

package fi.hu.cs.titokone;

/** This class can load a TTK91Application. It changes the processor state
    accordingly. Everything is loaded when loadApplication is called. Function returns the state of 
    memory after loading. If it runs out of memory it throws a TTK91OutOfMemory exception.
  */
public class Loader { 

  /**This variable holds the current application to be loaded.
    */
  private Application application;
  
  
  private Processor processor; 

public Loader(Processor processor) {
  this.processor = processor;
}

/**You can set the file to load. Each time an application is set to load, the counter is set to 
one.
*/
public void setApplicationToLoad(Application application){
  this.application = application;
}

/**Loads an application to memory. LoadInfo contains all the needed information about the process.
	@return Info from the load procedure.
*/
public LoadInfo loadApplication() throws throw TTK91OutOfMemoryException {
  
  if (application == null) {
    return null;
  }
  
  MemoryLine[] code = application.getCode();
  MemoryLine[] data = application.getInitialData();
  
  int i;
  for (i=0 ; i<code.length ; i++) {
    if( processor.MemoryInput(code[i]) == false) {
      throw TTK91OutOfMemoryException;
    }
  }
  
  for (int j=0 ; j<data.length ; j++) {
    if( processor.memoryInput(i+j, data[i]) == false) {
      throw TTK91OutOfMemoryException;
    }
  }
  
  LoadInfo retValue = new LoadInfo( code, 
                                    data,
                                    application.getSymbolTable(),
                                    processor.getValueOf(6),        // Tarkista onko R6 SP
                                    processor.getValueOf(7),        // samoin onko R7 FP
                                    new Message("Loads program") ); // TODO: Messagen kirjoitus!!!

  return retValue;
} 

}
