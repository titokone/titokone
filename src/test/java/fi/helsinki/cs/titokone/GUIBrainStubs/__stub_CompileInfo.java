package fi.helsinki.cs.titokone.GUIBrainStubs;

/* This class was used for testing GUIBrain */
public class __stub_CompileInfo {

public static final short FIRST_ROUND = 0;
public static final short FINALIZING_FIRST_ROUND =1;
public static final short SECOND_ROUND = 2;
public static final short FINALIZING = 3; 
private static int i = -1;



public __stub_CompileInfo() {
  i++;

}

public boolean getSymbolFound() {
  return true;
}

public String getSymbolName() {
  if ( i == 5 )
    return new String("joo2");
  if ( i == 6 ) 
    return new String("joo3");
  else   
    return new String("joo"+i%5);
}

public boolean getSymbolDefined() {
  if (i==3 || i==2)
    return false;
  else
    return true;
}

public int getSymbolValue() {
  return 10+i;
}

public boolean getLabelFound() {
  return true;
}

public int getLabelValue() {
  return 2*i;
}

public String getLabelName() {
  return "labeli"+i;
}


public String[][] getSymbolTable() {
  return new String[][] {{ "joo2", "1"}, {"hjadsa", "3"}};
}

public int getLineBinary() {
  return 31432432;
}


public short getPhase() {
  if ( i == 4 ) {
    return FINALIZING_FIRST_ROUND;
  }
  if (i > 4 && i < 9)
    return SECOND_ROUND;
  if (i >= 9) {
    i=-1;
    return FINALIZING;
    
  }
  
  else   
    return FIRST_ROUND;
    
}


public boolean getFinal() {
  return true;
}


public int getLineNumber() {
  if ( i < 5 )
    return i;
  else
    return i - 5;
}

public String[] getInstructions() {
  return new String[] { "haa", "hee", "hii" } ;
}

public String[] getData() {
  return new String[] { "532151", "53252", "1412" } ;
}

public String getComments() {
  return new String("This is compile comment for line" + i);
}

}