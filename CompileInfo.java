/** This class is used to tell GUIBrain what compiler has done at the moment.
    CompileDebugger creates objects from this class and passes them to the 
    GUIBrain.
*/

//Tdlld hetkelld melkein valmis, tiettyd toiminnallisuutta pitdd vield miettid
//ja javadoc puutteellinen
public class CompileInfo extends DebugInfo {
  // Variables common to both rounds.
  
  public static final short FIRST_ROUND = 0;
  public static final short SECOND_ROUND = 1;
  public static final short FINALIZING = 2; 
  private short phase;
  /** This field contains the number of the line being processed or 
      -1 during the finalizing phase. */
  private int lineNumber;
  private String lineContents;
  private String statusMessage;
  private String comments;
  // Variables for first round.

  /** This field is by default false, but if the compiled line was 
      empty (or consisted of whitespace only), the true value here says 
      that other checks can be skipped. */
  private boolean lineEmpty = false;
  /** This field contains the name of a symbol found on this line 
      from the parameter field (this also includes labels in the parameter
      field, but not ones defined). It will be set to "" if no symbol 
      was found on this line. See also symbolValue, symbolDefined and
      label variables. */
  private String symbolName = "";
  /** This field is true if the symbol's value was also defined on this
      line. In this case, the value is set in symbolValue. */
  private boolean symbolDefined = false;
  /** This field contains the value of the symbol found. If symbolDefined
      is false, the value in this field should be ignored. */
  private int symbolValue;

  // First or second round variable:
  /** This field contains the name of a label found from the beginning of
      this line. It will be set to "" if no label was found on this line. 
      The value can only be defined on the second round of compilation. */
  private String labelName = "";
  
  // Variables for the second round.

  private int lineBinary;

  private boolean labelDefined = false;
  private int labelValue;

  // Variables for finalization phase.

  /** This field contains true if we have gone through all DS and DC 
      commands and are setting FP and SP accordingly, or false if 
      we are still going through DS and DC commands. */
  private boolean finalFinal;

  public static final int SP_POS = 0;
  public static final int FP_POS = 1;
  /** This array contains the initial values of SP and FP, positioned
      according to SP_POS and FP_POS. */
  private int[] initPointers;

  private boolean definingDS; // or DC.
  private boolean definingDC;
  private int value; // DC/DS.

  public CompileInfo(String statusMessage, short phase, int lineNumber,
		     String lineContents) {}
  // (final phase constructor)
  public CompileInfo(String statusMessage, short phase, int lineNumber) {}
    
  /* Vielä metodit tueksi näille:
     all: set statusmessage (in parent), set phase, set linenumber
     and line contents if applicable (not final phase).
     first round:
     - set lineempty, symbolName, -defined and -value if applicable,
     labelname if applicable.
     second round:
     - set line binary, label name and value if applicable.
     final phase processing DC/DS:
     - set definingDS, value, symbolName, symbolValue
     final phase final: 
     - set SP, FP in initPointers. */

    
    
    /** This method sets lineEmpty value to true. */
    public void setLineEmpty(){}
    public void setComments(String comment){}
    public void setSymbolName(String name){}
    public void setSymbolName(String name, int value){}
    public void setLabelName(String name){}
    public void setLineBinary(int binary){}
    public void setLabelDefined(String name, boolean defined){}
    public void setLabelValue(String name, int value){}
    public void setDefiningDS(int ADDR, int size){}
    public void setFinal(){}
    public void setInitPointers(int SP, int FP){}
    public void setMemoryline(int lineNumber, int value){}

    public String returnStatusMessage(){}
    public String returnComments(){}
    public boolean returnLineEmpty(){}
    public short returnPhase(){}
    public String returnLineContents(){}
    public String returnSymbolName(){}
    public boolean returnSymbolDefined(){}
    public int returnSymbolValue(){}
    public int returnLineBinary(){}
    public String returnLabelName(){}
    public int returnLabelValue(){} // tarkasta
    public boolean returnDefiningDS(){} // samoin
    public boolean returnDefiningDC(){} //samoin
    public int returnValue(){}
    public boolean returnFinalPhase(){}
    public int[] returnInitPointers(){}


}

