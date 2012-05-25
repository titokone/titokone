package fi.helsinki.cs.titokone;

/*
Sampo Yrjänäinen, 7.4. 2004
Program name: DmpEqualsB91

Compares Koksi dump file (.dmp) and compiled binary file (.b91). Returns 1, 
if files are equal. In other cases program returns zero (0).
*/


import java.io.*;
import java.util.*;

public class DmpEqualsB91 {
    
    
    public static void main (String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println ("Useage: DmpEqualsB91 input.k91 input.b91");
            System.exit(0);
        }
        
        BufferedReader dmpReader=null, b91Reader=null;
        try {
            dmpReader = new BufferedReader (new FileReader (new File (args[0])));
        } catch (FileNotFoundException e) {
            System.out.println ("File \"" + args[0] + "\" not found.");
            System.exit (0);
        }
        
        try {
            b91Reader = new BufferedReader (new FileReader (new File (args[1])));
        } catch (FileNotFoundException e) {
            System.out.println ("File \"" + args[1] + "\" not found.");
            System.exit (0);
        }
        
        StringTokenizer dmpTokenizer, b91Tokenizer;
        String dmpLine, b91Line;
        
        // kelataan b91-tiedosto koodialueen alkuun
        do {
            b91Line = b91Reader.readLine();
            if (b91Line == null) {
                System.out.println ("Could not found \"___code___\" tag from b91 file \"" + args[1] + "\".");
                System.exit(0);
            }
            b91Tokenizer = new StringTokenizer (b91Line);
        } while (!b91Tokenizer.nextToken().equals ("___code___"));
        
        
        // luetaan koodialueen pituus b91 tiedostosta
        int codeAreaSize=0;
        b91Tokenizer = new StringTokenizer (b91Reader.readLine());
        try {
            codeAreaSize = -( (new Integer (b91Tokenizer.nextToken())).intValue() -(new Integer (b91Tokenizer.nextToken())).intValue()) +1;
        }
        catch (NumberFormatException e) {
            System.out.println ("Error occured while reading code area size from b91 file.");
            System.exit(0);
        }
        
        boolean equals = true;
        int operationNumber = 0;
        // vertaillaan koodialueiden sisältöjä
        for (;codeAreaSize > 0; codeAreaSize--) {
            dmpLine = dmpReader.readLine();
            if (dmpLine == null) {
                System.out.println ("dmp file ended before code area end.");
                System.exit(0);
            }
            b91Line = b91Reader.readLine();
            if (b91Line==null) {
                System.out.println ("b91 file ended before code area end.");
                System.exit(0);
            }
            
            b91Tokenizer = new StringTokenizer (b91Line);
            dmpTokenizer = new StringTokenizer (dmpLine);
            
            dmpTokenizer.nextToken();
            String b91Command = b91Tokenizer.nextToken();
            String dmpCommand = dmpTokenizer.nextToken();
            
            if (!b91Command.equals (dmpCommand)) {
                System.out.println ("Operation " + operationNumber +" differs: dmp: \"" + dmpCommand + "\" b91: \"" + b91Command + "\".");
                equals = false;
            }
            ++operationNumber;
        }
        
        b91Tokenizer = new StringTokenizer (b91Reader.readLine());
        if (!b91Tokenizer.nextToken().equals ("___data___")) {
            System.out.println ("Could not found \"___data___\" tag after code area in b91 file.");
            System.exit(0);
        }

        int dataAreaSize=0;
        b91Tokenizer = new StringTokenizer (b91Reader.readLine());
        try {
            dataAreaSize = -( (new Integer (b91Tokenizer.nextToken())).intValue() -(new Integer (b91Tokenizer.nextToken())).intValue()) +1;
        }
        catch (NumberFormatException e) {
            System.out.println ("Error occured while reading data area size from b91 file.");
            System.exit(0);
        }
        
        // vertaillaan data-alueiden sisältöjä
        for (;dataAreaSize > 0; dataAreaSize--) {
            dmpLine = dmpReader.readLine();
            if (dmpLine == null) {
                System.out.println ("dmp file ended before data area end.");
                System.exit(0);
            }
            b91Line = b91Reader.readLine();
            if (b91Line==null) {
                System.out.println ("b91 file ended before data area end.");
                System.exit(0);
            }
            
            b91Tokenizer = new StringTokenizer (b91Line);
            dmpTokenizer = new StringTokenizer (dmpLine);
            
            dmpTokenizer.nextToken();
            String b91Command = b91Tokenizer.nextToken();
            String dmpCommand = dmpTokenizer.nextToken();
            
            if (!b91Command.equals (dmpCommand)) {
                System.out.println ("Operation " + operationNumber +" differs: dmp: \"" + dmpCommand + "\" b91: \"" + b91Command + "\".");
                equals = false;
            }
            ++operationNumber;
        }
        
        if (equals) {
            System.out.println ("Files are equal.");
            System.exit(1);
        }
        else System.exit(0);
    }
}