package fi.hu.cs.titokone;

import java.text.ParseException;
import java.util.Vector;

/** This class represents the contents of a binary file. It can 
    interpret an Application instance of itself with the help of the 
    BinaryInterpreter class, as well as transforming an Application into
    a string in the binary file format. */
public class Binary {
    /** This field stores the application this binary represents, or 
	null if it has not yet been resolved. */
    private Application application;
    /** This field stores the binary contents of the application, or
	an empty string if the contents have not yet been resolved 
	(ie. the Application constructor has been used and toString() 
	has not been called). */
    private String contents;

    /** This constructor sets up a binary instance. 
	@param contents A linebreak-delimited string containing the 
	contents of a binary file. 
	@throws ParseException If the string does not represent a 
	syntactically correct binary. */ //TODO virheilmoitukset
    public Binary(String contents) throws ParseException {
	BinaryInterpreter bini = new BinaryInterpreter();
	this.contents=contents;

	//split contents into a b91 array where deliminiter is line separator
	String[] b91=contents.split(System.getProperty("line.separator",
						       "\n"));
	//Trim all spaces and whitespaces from aray
	for (int i=0;i<b91.length;i++)
	    b91[i]=b91[i].trim();
	
	/* From now on, i is used to tell which line in .b91 file method is
	   parsing.*/
	int i = 0;
	
	/* Check if reserved line ___b91___ is found.*/
	if(!b91[i].equalsIgnoreCase("___b91___"))
	   throw new ParseException(new Message("___B91___ is "+
						"missing.").toString(), i+1);
	i++;
	/* Check if reserved line ___code___ is found.*/
	if(!b91[i].equalsIgnoreCase("___code___"))
	    throw new ParseException(new Message("___code___ is "+
						 "missing.").toString(),i+1);
	i++;
	
	/* Calculating code area length.*/
	String[] codeArea=b91[i].split("\\s");
	Integer x,y;
	
	/* Try to get integers from values and check if they are valid*/
	try{
	    x=new Integer(codeArea[0]);
	    y=new Integer(codeArea[1]);
	}catch(Exception e){
	    throw new ParseException(new Message("Invalid code area value "+
						 "on line: {0}",
						 ""+(i+1)).toString(),i+1);
	}
	    

	if(x.intValue()!=0 || x.intValue()>y.intValue())
	    throw new ParseException(new Message("Invalid code area "+
						 "length on line: {0}",
						 ""+(i+1)).toString(),i+1);
	int areaLength=y.intValue()+1;

	i++;
	
	if(y.intValue()!=0){
	    while(!b91[i].startsWith("_")){
		Integer j;
		try{
		    j=new Integer(b91[i]);
		}catch(Exception e){
		    throw new ParseException(new Message("Invalid command on "+
							 "line: {0}",
							 ""+(i+1)).toString(),
					     i+1);
		}
		
		int k = j.intValue();
		String s = bini.binaryToString(k);

		if(s.equals(""))
		    throw new ParseException(new Message("Invalid command on"+
							 " line: {0}", 
							 ""+(i+1)).toString(),
					     i+1); 
		
		i++;	    
	    }
	
	    if(i-3!=areaLength)
		throw new ParseException(new Message("Invalid number of " + 
						 "code lines.").toString(), 
					 i+1);
	}
	/* Check if ___data___ is missing. */
	if(!b91[i].equalsIgnoreCase("___data___"))
	    throw new ParseException(new Message("___data___ is " + 
						 "missing.").toString(), i+1);
	i++;

	String[] dataArea=b91[i].split("\\s");
	/* check if data values are integers. */
	try{
	    x=new Integer(dataArea[0]);
	    y=new Integer(dataArea[1]);
	}catch(Exception e){
	    throw new ParseException(new Message("Invalid data area " + 
						 "value on line: {0}",
						 ""+(i+1)).toString(),i+1);
	}

	if(x.intValue()>y.intValue())
	    throw new ParseException(new Message("Invalid data area " + 
						 "length on line: {0}",
						 ""+(i+1)).toString(),i+1);
	
	areaLength=y.intValue()-x.intValue()+1;

	i++;
	areaLength +=i;

	for (int j = i;j<areaLength;j++){
	    try{
		Integer value = new Integer(b91[j]);
	    }catch(Exception e){
		throw new ParseException(new Message("Invalid data on line: "+
						     "{0}",
						     ""+(i+1)).toString(),i+1);
	    }
	    i++;
	}
       
	/* Check if ___symboltable___ is missing */
	if(!b91[i].equalsIgnoreCase("___symboltable___"))
	    throw new ParseException(new Message("___symboltable___ is "+
						 "missing.").toString(), i+1);
	i++;
	while(!b91[i].startsWith("_")){
	    String[] s=b91[i].split("\\s");
	    if(s.length!=2)
		throw new ParseException(new Message("Invalid symbol on "+
						     "line: {0}",
						     ""+(i+1)).toString(), 
					 i+1);

	    if(!s[0].equalsIgnoreCase("STDIN")&&
	       !s[0].equalsIgnoreCase("STDOUT")){
		
		try{
		    Integer value=new Integer(s[1]);
		}catch(Exception e){
		    throw new ParseException(new Message("Invalid symbol "+
							 "value on line: {0}"
							 ,""+(i+1)).toString(),
					     i+1);
		}
	    }
	    i++;
	}
	/* Check if ___end is missing. */
	if(!b91[i].equalsIgnoreCase("___end___"))
	    throw new ParseException(new Message("___end___ is "+
						 "missing.").toString(),
				     i+1);

	int EOF=i; //line containing ___end___, following lines should
	i++;       //contain only whitespaces 
	
	while(i<b91.length){
	    
	    if(!b91[i].equals(""))
		throw new ParseException(new Message("Lines after "+
						  "___end___").toString(),i+1);
	    i++;
	}

	this.contents="";
	//Assembling parsed b91 array to string
	for (int l=0;l<=EOF;l++){

	    this.contents= this.contents +b91[l];
	    this.contents+=System.getProperty("line.separator","\n");
	}


    }

    /** This constructor sets up a binary instance which has already
	been interpreted into an application. It delays interpreting
	the binary to string form until toString() is called. 
	@param application The application to form a binary of. */

    public Binary(Application application) {
	this.application=application;
    }

    /** This method transforms the binary into an application file. 
	It instantiates a BinaryInterpreter to help with the task and 
	stores the result internally to avoid needing to redo the 
	interpretation. If the class already knows what the application
	class corresponding to the binary is, it will just return that.
	@return An application instance corresponding to the binary. 
	@throws ParseException If the binary contents are not 
	syntatically correct. */
    //TODO Symboltableen muutokset uupuvat
    public Application toApplication() throws ParseException {
       
	BinaryInterpreter bini=new BinaryInterpreter();
	Application app;
	Integer command;
	MemoryLine line;
	SymbolTable symbolTable = new SymbolTable();

	Vector code = new Vector();
	Vector data = new Vector();
	String[] b91=contents.split(System.getProperty("line.separator","\n"));
	for (int i = 0 ; i<b91.length; i++)
	    b91[i].trim();
	int i=0;
	while(b91[i].startsWith("_"))
	    i++;
	i++;
	//	try{
	    while(!b91[i].startsWith("_")){
		command=new Integer(b91[i]);
		String symbolic = bini.binaryToString(command.intValue());
		//if (symbolic.equals(""))
		//    throw new ParseException(new Message("Invalid command "+
		//					 "on line: {0}"
		//					 ,""+(i+1)).toString(),
		//			     i+1);
		line = new MemoryLine(command.intValue(), symbolic);
		code.add(line);
		i++;
	    }
//}catch(Exception e){
//    throw new ParseException(new Message("Invalid command on line:"+
//						 " {0}",""+(i+1)).toString(),
//			     i+1);
//}
	i++;//Pass ___data___
	
	i++;//Pass data area pointers
	
//try{
	    while(!b91[i].startsWith("_")){
		command=new Integer(b91[i]);
		String symbolic = bini.binaryToString(command.intValue());
		line = new MemoryLine(command.intValue(), symbolic);
		data.add(line);
		i++;	    
	}
//}catch(Exception e){
//    throw new ParseException(new Message("Invalid data value on "+
//					 "line: {0}",
//					 ""+(i+1)).toString(),i+1);
//}
	
	i++; //pass __symboltable___
	while(!b91[i].startsWith("_")){
	    
	    String[] symbol=b91[i].split("\\s");
	    
	    /* If symbol is STDIN or STDOUT, add it to definitions */
	    if(symbol[0].equalsIgnoreCase("STDOUT")
	       ||symbol[0].equalsIgnoreCase("STDIN")){
		symbolTable.addDefinition(symbol[0], symbol[1]);
	    }
	    else{

		//	try {
		Integer j = new Integer(symbol[1]);
		symbolTable.addSymbol(symbol[0], j.intValue());
		//}catch(Exception e){
		//    throw new ParseException(new Message("Invalid symbol "+
		//				 "value on line: {0}",
		//			     ""+(i+1)).toString(),i+1);
		//}
	    }
	    i++;	    
	}	
	
	MemoryLine[] codeLines = new MemoryLine[code.size()];
	for (int j = 0;j<codeLines.length;j++){
	    codeLines[j]=(MemoryLine)code.get(j);
	}
	
	MemoryLine[] dataLines = new MemoryLine[data.size()];
	for (int j = 0;j<dataLines.length;j++){
	    dataLines[j]=(MemoryLine)data.get(j);
	}
	app = new Application(codeLines, dataLines, symbolTable);
	return app;
     }

    /** This method determines (if it is not done already), stores and
	returns the String representation in .b91 format of this
	binary.  The linebreaks used vary, as one of the constructors
	accepts a premade string; if the Application constructor has
	been used, the linebreak will be
	System.getProperty(line.separator, "\n"). An example program in .b91
	format might look like this (comments in parentheses are not 
	a part of the file format):<br>
	<pre>
        ___b91___
        ___code___
        0 9          (numbers of the first and the last line of code,
        52428801      the latter also being the initial value of FP)
	18874378
	572522503
	36175883
	287834122
	18874379
	538968064
	36175883
	69206016
	1891631115
	___data___
	10 11         (numbers the first and the last line of the data 
	0              area, the latter also being the initial value of SP;
	0              then follow the contents of the data area in order)
	___symboltable___
	luku 10        (only local symbols are included, eg. HALT is 
	summa 11        considered built-in)
	___end___
	</pre>
       
	@return The String representation of this binary. */
    public String toString() {  //TODO Symboltablen muutokset uupuvat
	//System.out.println(contents);
	if (contents!=null)
	    return contents;
	
	
	MemoryLine[] code=application.getCode();
	MemoryLine[] data=application.getInitialData();
	SymbolTable symbol=application.getSymbolTable();
	String[] symNames = symbol.getAllSymbols();
	String[] definitions = symbol.getAllDefinitions();
	int length = code.length;
	
	String s="___b91___";
	s+=System.getProperty("line.separator", "\n");

	s+="___code___";
	s+=System.getProperty("line.separator", "\n");
	
	if(length>0)
	    s+=+0+" "+(length-1);
	else 
	    s+="0 0";

	s+=System.getProperty("line.separator", "\n");

	for(int i =0; i<length;i++)
	    s+=code[i].getBinary()+System.getProperty("line.separator", "\n");
	
	length=code.length+data.length;
	s+="___data___";
	s+=System.getProperty("line.separator", "\n");

	if(data.length>0)
	    s+=""+code.length+" "+(length-1);
	else
	    s+=""+code.length+" "+code.length;
	s+=System.getProperty("line.separator", "\n");
	
	if (data!=null){
	    for(int i = 0; i<data.length;i++){
		//System.out.println(data[i].getBinary());
		s+=""+data[i].getBinary();
		s+=System.getProperty("line.separator", "\n");
	    }
	}
	s+="___symboltable___";
	s+=System.getProperty("line.separator", "\n");
       
	if(symNames!=null){
	    for(int i=0;i<symNames.length;i++){
		/*if(symNames[0].equalsIgnoreCase("STDOUT")
		  ||symNames[0].equalsIgnoreCase("STDIN")){ 
		  //TODO tarkasta
		  s+=""+symNames[i]+" "+symbol.getDefinition(symNames[i]);
		  }
		  else{*/
		
		s+=""+symNames[i]+" "+symbol.getSymbol(symNames[i]);
		s+=System.getProperty("line.separator", "\n");
	    }   
	}
	if(definitions!=null){
	    for(int i=0;i<definitions.length;i++){
		s+=definitions[i]+" "+symbol.getDefinition(definitions[i]);
		s+=System.getProperty("line.separator", "\n");
	    }
	}
    
	s+="___end___";
	s+=System.getProperty("line.separator", "\n");
	return s;
	  
	   
    }
}
