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
    processor.memoryInput(i, code[i]);
      /*throw new TTK91AddressOutOfBounds(new Message("Loading to memory failed " +
						"on line {0}.", 
						"" + i).toString());
    }*/
  }
  
  for (int j=0 ; j<data.length ; j++) {
    processor.memoryInput(i+j, data[i]);
      /*throw new TTK91AddressOutOfBounds(new Message("Loading to memory failed " +
						"on line {0}.", 
						"" + (i + j)).toString());
    }*/
  }
  
  LoadInfo retValue = new LoadInfo( code, 
                                    data,
                                    application.getSymbolTable(),
                                    FP,
                                    SP,
                                    new Message("Loads program").toString() ); // TODO: Messagen kirjoitus!!!
 
  processor.runInit(SP, FP);

  return retValue;
} 

}
