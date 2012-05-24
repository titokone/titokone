
package fi.hu.cs.titokone;
import junit.framework.*;
import java.text.ParseException;


public class BinaryTest extends TestCase {


    private Binary bin;

    protected void setUp() {
    }
   
    public void testBinary() {
	String contents;
	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.fail("virheellinen syöte");
	    //Assert.assertEquals(e.toString(),");
	}	    
	Assert.assertEquals(bin.toString(), contents); 

	contents= "___ab91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "
				+"___b91___ is missing.");
	}	    
	
	contents= "___b91___\n___caode___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;
	
	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "
				+"___code___ is missing.");
	}	    
	
	contents= "___b91___\n___code___\n0 1\n123\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"Invalid command on line: 4");
	}	    


	
	contents= "___b91___\n___code___\n0 1\na\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"Invalid command on line: 4");
	}	    


	contents= "___b91___\n___code___\n0 1\n0\n0\n___daata___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "
				+"___data___ is missing.");
	}	    
	
	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___asymboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "
				+"___symboltable___ is missing.");
	}	    
	
	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___aend___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException:"
				+" ___end___ is missing.");
	}	    
	
	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.fail(e.toString());
	}	    

	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"___data___ is missing.");
	}	    

	
	contents= "___b91___\n___code___\n0 2\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    // System.out.println(e);
	    Assert.assertEquals(e.toString(), "java.text.ParseException: "
				+"Invalid number of code lines.");
	}	    

	contents= "___b91___\n___code___\n0 -1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"___data___ is missing.");
	}	    
	
	contents= "___b91___\n___code___\n0 a\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"Invalid code area value on line: 3");
	}	    



	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 -1\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "
				+"Invalid data area length on line: 7");
	}	    

	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 2\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"___symboltable___ is missing.");
	}	    
	
	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 4\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"Invalid data on line: 10");
	}	    

	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\na11\n___symboltable___\nSum 10\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "
				+"Invalid data on line: 9");
	}	    

	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum a\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"Invalid symbol value on line: 11");
	}	    

	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum \n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"Invalid symbol on line: 11");
	}	    

	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSTDIN a\nSTDOUT asd\n___end___\n" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.fail(e.toString());
	}	    

	contents= "___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n \na" ;

	try{
	    bin = new Binary(contents);
	}catch (ParseException e){
	    Assert.assertEquals(e.toString(),"java.text.ParseException: "+
				"Lines after ___end___");
	}	    


    }

    public void testBinaryToApplication(){
	String content= "___b91___\n___code___\n0 1\n0\n 0\n___data___\n2 3\n"
	    +"10\n11\n___symboltable___\nSum 10\n___end___\n";
	try{
	    bin = new Binary(content);
	}catch(Exception e){
	    Assert.fail(e.toString());
	}
	Application app = null;	
	try{
	    app = bin.toApplication();
	}
	catch(Exception e){
	    Assert.fail(e.toString());
	}
	Assert.assertNotNull(app);
	MemoryLine[] code = app.getCode();
	MemoryLine[] data = app.getInitialData();
	SymbolTable symtable = app.getSymbolTable();
	
	int binary;
	String sym;
	for (int i = 0; i<code.length;i++){
	    Assert.assertEquals(code[i].getBinary(),0);
	    Assert.assertEquals(code[i].getSymbolic(),"NOP");
	}
	for (int i = 0; i<data.length;i++){
	    System.out.println(i+" "+data[i].getBinary() +" "
			       +data[i].getSymbolic());
	}

	content="       ___b91___\n        ___code___\n        0 9 \n"+
	     " 52428801\n	18874378\n	572522503\n	36175883\n"+
	    "287834122\n	18874379\n	538968064\n	36175883\n"+
	    "69206016\n	1891631115\n"+ 
	    "___data___\n	 10 12    \n12 \n	12 \n0\n"+
	    "___symboltable___\nstdout c:\\koksi\\  \n	summa 11 \n___end___";

	try{
	    bin = new Binary(content);
	    app= bin.toApplication();
	}catch(ParseException e){
	    System.out.println(e.toString());
	    Assert.fail(e.toString());
	}
	
	
	code = app.getCode();
	data = app.getInitialData();
	
	Assert.assertEquals(code[0].getBinary(),52428801);
	Assert.assertEquals(code[0].getSymbolic(),"IN R1, =KBD");

	Assert.assertEquals(data[0].getBinary(),12);
	Assert.assertEquals(data[0].getSymbolic(),"NOP");



	symtable = app.getSymbolTable();
	String[] symbols = symtable.getAllSymbols();
	String[] defs = symtable.getAllDefinitions();
	
	int sval = symtable.getSymbol(symbols[0]);
	
	Assert.assertEquals(symbols[0],"summa");
	Assert.assertEquals(sval,11);
	
	Assert.assertEquals(defs[0],"stdout");
	Assert.assertEquals(symtable.getDefinition(defs[0]),"c:\\koksi\\");
	System.out.println("printti alkaa");
	for (int i = 0;i<code.length;i++){
	    System.out.println(code[i].getBinary()+" "+code[i].getSymbolic());
	}
	for (int i = 0;i<data.length;i++){
	    System.out.println(data[i].getBinary()+" "+data[i].getSymbolic());
	}
	for (int i = 0;i<symbols.length;i++){
	    System.out.println(symbols[i]+" "+symtable.getSymbol(symbols[i]));
	}
	for (int i = 0;i<defs.length;i++){
	    System.out.println(defs[i]+" "+symtable.getDefinition(defs[i]));
	}
	System.out.println("printti loppuu");

    }

    public void testToString(){
	//Application app = new Application();
	//bin = new Binary(app);
	String result="___b91___\n___code___\n0 1\n0\n0\n___data___\n2 3\n"
	    +"2\n3\n___symboltable___\nstdin asdhj\n___end___\n";
	try {
	bin = new Binary(result);
	}catch (Exception e){
	    System.out.println(e);
	    Assert.fail();
	}
	try{
	Application app = bin.toApplication();
	
	bin = new Binary(app);
	}catch (Exception e){
	}	
	//System.out.println("applicationista takaisin\n"+bin.toString());
	Assert.assertEquals(bin.toString(), result);
    }

    public static Test suite() {
	return new TestSuite(BinaryTest.class);
    }
    
    public static void main (String[] args) {

	junit.textui.TestRunner.run (suite());
    }
    
}
