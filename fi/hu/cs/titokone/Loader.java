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
  String errorMessage;
  if (processor == null) {
      errorMessage = new Message("Null is an invalid parameter," +
				 " instance of {0} required.", 
				 Processor.class.getName()).toString();
      throw new IllegalArgumentException(errorMessage);
  }
  this.processor = processor;
}

/**You can set the file to load. Each time an application is set to load, the counter is set to 
one.
*/
public void setApplicationToLoad(Application application){
  String errorMessage;
  if (application == null) {
      errorMessage = new Message("Null is an invalid parameter," +
				 " instance of {0} required.", 
				 Application.class.getName()).toString();
      throw new IllegalArgumentException(errorMessage);
  }
  this.application = application;
}

/**Loads an application to memory. LoadInfo contains all the needed information about the process.
	@return Info from the load procedure, null if no application has been set for loading.
*/
public LoadInfo loadApplication() throws TTK91AddressOutOfBounds {
  String[] messageParameters = new String[2];  
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
    processor.memoryInput(i+j, data[j]); //May throw TTK91AddressOutOfBounds, but it's just thrown
  }                                      //backwards
  
  messageParameters[0] = "" + FP;
  messageParameters[1] = "" + SP;
  LoadInfo retValue = new LoadInfo( code, 
                                    data,
                                    application.getSymbolTable(),
                                    FP,
                                    SP,
                                    new Message("Program loaded into memory. FP set to {0}" +
                                                " and SP to {1}.", 
						messageParameters).toString() );
 
  processor.runInit(SP, FP);

  return retValue;
} 

}
