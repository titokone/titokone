/*
 *
 * 
 */
package fi.hu.cs.ttk91;

import java.text.ParseException;

/**
 * @author Kalle Kärkkäinen
 *
 * 
 */
public interface TTK91Core {
	
	/*
	 * it should be possible to write:
	 * 
	 * //FileSource is not part of the specification,
	 * //mentioned here for an example.
	 * 
	 * TTK91CompileSource source = new Source("myfile.t91");
	 * TTK91Core core = new MalanCore(); 
	 * try{
	 *   TTK91Application app = core.compile(source);
	 *   TTK91Cpu cpu = null;
	 *   do{
	 *     try{
	 *       core.run(app, 1);
	 * 	   }catch(TTK91RuntimeException re){
	 *       .. handle the error ..
	 *       break;
	 *     }
	 *     cpu = core.getCpu();
	 *   }while(cpu.getStatus() == STATUS_STILL_RUNNING);
	 * }catch(TTK91CompileException e){
	 *   switch(e.getErrorCode()){
	 *    ... handle the errors ...
	 *   }
	 * }
	 */
	 
	
	/**
	 *   This method compiles given TTK91CompileSource into a 
	 *   TTK91Application.
	 * 
	 *   If source returned by TTK91CompileSource is null, then
	 *   a null TTK91Application is to be returned.
	 *   
	 *   @param source TTK91CompileSource describin
	 *   @throws TTK91CompileException thrown when encountered an invalid
	 *           source code.
	 *   @throws TTK91Exception place holder for extension.
	 *   @throws IllegalArgumentException thrown when TTK91CompileSource
	 *           is not valid (null).
	 *   @return Represents the application that is runnable
	 *           in this TTK91Core
	 */
	public TTK91Application compile(TTK91CompileSource source) 
					throws TTK91Exception, TTK91CompileException;

	/**
	 *   This method runs given TTK91Application in the  
	 *   TTK91 virtual machine.
	 *
	 *
	 *   @param app Application to be run, must be created using compile
	 *              of the same TTK91Core. May not be null.  
	 *   @param steps number of cpu cycle's to operate without stop.
	 *                Zero (0) means to the end. anything above zero means
	 *                this much or the end. anything less than zero is error.
	 *   @throws TTK91RuntimeException when encountered an invalid
	 *           operation by the TTK91Application.
	 *   @throws TTK91Exception place holder for extension.
	 *   @throws IllegalArgumentException thrown when TTK91Application
	 *           is not valid, or int steps is less than zero.
	 */
	public void run(TTK91Application app, int steps) 
					throws TTK91Exception, TTK91RuntimeException;
	
	/**
	 *   Returns a copy of the memory in the virtual machine
	 * 
	 * @return Copy of the memory
	 */
	public TTK91Memory getMemory();
	
	/**
	 * 
	 * Returns a copy of the CPU in the virtual machine
	 * 
	 * @return copy of the CPU of the virtual machine
	 */
	public TTK91Cpu getCpu();
	
	/**
	 * Loads TTK91 binary format into a TTK91Application
	 * 
	 * @param binary binary to load from
	 * @return application ready for running
	 * @throws ParseException if binary is not of TTK91 binary format
	 */
	public TTK91Application loadBinary(String binary)
					throws ParseException;

	/**
	 * This function return the program in 
	 * TTK91 binary file format
	 * @return String representing the binary format or null if failed
	 */
	public String getBinary(TTK91Application application);

}
