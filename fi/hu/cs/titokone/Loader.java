package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.TTK91AddressOutOfBounds;

/** This class can load a TTK91Application. It changes the processor
    state accordingly. Everything is loaded when loadApplication is
    called. Function returns the state of memory after loading. If it
    runs out of memory it throws a TTK91AddressOutOfBounds exception.
*/
public class Loader { 

  /**This variable holds the current application to be loaded.
    */
  private Application application;
  
  
  private Processor processor; 

public Loader(Processor processor) {
  if (processor == null) {
    throw new IllegalArgumentException("Null is an invalid parameter," +
                                       " instance of " + Processor.class.getName() +
                                       " required.");
  }
  this.processor = processor;
}

/**You can set the file to load. Each time an application is set to load, the counter is set to 
one.
*/
public void setApplicationToLoad(Application application){
  if (application == null) {
    throw new IllegalArgumentException("Null is an invalid parameter, " +
                                       "instance of " + Application.class.getName() +
                                       " required.");
  }
  this.application = application;
}

/**Loads an application to memory. LoadInfo contains all the needed information about the process.
	@return Info from the load procedure, null if no application has been set for loading.
*/
public LoadInfo loadApplication() throws TTK91AddressOutOfBounds {
  
  if (application == null) {
    return null;
  }
  
  MemoryLine[] code = application.getCode();
  MemoryLine[] data = application.getInitialData();
  
  int FP = code.length - 1;
  int SP = code.length + data.length - 1;
  
  int i;
  for (i=0 ; i<code.length ; i++) {
    processor.memoryInput(i, code[i]);   //May throw TTK91AddressOutOfBounds, but it's just thrown
  }                                      //backwards
  
  for (int j=0 ; j<data.length ; j++) {
    processor.memoryInput(i+j, data[i]); //May throw TTK91AddressOutOfBounds, but it's just thrown
  }                                      //backwards
  
  LoadInfo retValue = new LoadInfo( code, 
                                    data,
                                    application.getSymbolTable(),
                                    FP,
                                    SP,
                                    new Message("Program loaded into memory. FP set to "+FP+
                                                " and SP to "+SP+".").toString() );
 
  processor.runInit(SP, FP);

  return retValue;
} 

}
