package fi.hu.cs.titokone;

import fi.hu.cs.ttk91.TTK91CompileSource;

/** This class represents source code. It contains the source as a delimited
    string. */
public class Source implements TTK91CompileSource {
    /** The source code as one long String, lines delimited with \n, \r\n or 
	\r. */
    private String sourceString;

    /** @return The source code in one long String, lines delimited with 
	\n, \r\n or \r. */
    public String getSource() {}
}
