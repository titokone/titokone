/* TODO:
    updateStatusLine(String str) updates the status line by replacing its contents with str
    
    updateReg(int reg, int value) updates reg with a new value
    
    disable(int)
    
    insertToCodeTable(int[], int[], String[])
    
    Lisäsin kentän HashMap symbolsHashMap
    
    Poistin metodin public void setInputFieldEnabled(boolean b)
*/

package fi.hu.cs.titokone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

/** Class GUI is namely the class that implements the Graphical User Interface.
    It uses methods in GUIBrain to fire certain actions, such as compiling and
    running the programs. If GUIBrain wants to inform GUI about something, like
    new register values or about a new symbol in symbol table, then an Event of
    some kind if fired. Various ActionListeners are added here to catch those 
    events.
    
    This GUI has three views hardcoded, one for the state when no file has been
    opened, one for the state when a symbolic code file has been opened and one
    one for the state when a binary code file has been opened. The view can be
    changed with setView(int) method and once it is launched, the GUI's view is
    changed into the selected view's basic state. A basic state for a view means
    which of the buttons are enabled and which of the menuselections are enabled
    for a certain view. 
*/

public class GUI extends JFrame implements ActionListener {
  
        GUI thisGUI;
        
        GUIBrain guibrain;
        
        /** This holds (@link rightSplitPane rightSplitPane) and 
            (@link leftPane leftPane).
        */
        JSplitPane mainSplitPane;;
        
        /** What this holds, depends on the view of this gui. If it's 1, then this
            is empty. If it's 2, then this holds (@link codeTableScrollPane codeTableScrollPane). 
            If it's 3, then this holds (@link dataAndInstructionsTableSplitPane 
            dataAndInstructionsTableSplitPane).
        */
        JPanel leftPanel;
        /** This holds (@link upperRightPanel upperRightPanel) and (@link commentListScrollPane
            commentListScrollPane).
        */
        JSplitPane rightSplitPane;
        /** This holds (@link registersTableScrollPane registersTableScrollPane), 
            (@link symbolTableScrollPane symbolTableScrollPane) and (@link ioPanel
            ioPanel).
        */
        JPanel  upperRightPanel;
        
        /** This holds (@link codeTable codeTable).
        */
        JScrollPane codeTableScrollPane;
        /** This table is used to visualize the K91 source code.
        */
        JTableX codeTable;
        Object[] codeTableIdentifiers = {""};
        
        /** This holds (@link instructionsTable instructionsTable)
        */
        JScrollPane instructionsTableScrollPane;
        /** This table is used to visualize the instructions side of Titokone's memory by showing
            its numeric contents and their symbolic equivalencies.
        */
        JTableX instructionsTable;
        Object[] instructionsTableIdentifiers = {"Line", "Binary command", "Symbolic command"};
        
        /** This holds (@link dataTable dataTable).
        */
        JScrollPane dataTableScrollPane;
        /** This table is used to visualize the data side of Titokone's memory by showing its numeric
            contents and their symbolic equivalencies.
        */
        JTableX dataTable;
        Object[] dataTableIdentifiers = {"Line", "Binary command", "Symbolic command"};
        
        /** This holds (@link instructionsTableScrollPane instructionsTableScrollPane) and 
            (@link dataTableScrollPane dataTableScrollPane).
        */
        JSplitPane dataAndInstructionsTableSplitPane;   
        
        /** This holds @link registersTable.
        */
        JScrollPane registersTableScrollPane;
        /** This table is used to visualize the contents of Titokone's registers. There's one row for
            each of there registers: R0, R1, R2, R3, R4, R5, SP, FP and PC.
        */
        JTableX registersTable;
        Object[] registersTableIdentifiers = {"", ""};
        
        /** This holds (@link symbolTable symbolTable).
        */
        JScrollPane symbolTableScrollPane;
        /** This table is used to visualize the symbols that are declared in source code, and their values.
            There's one row for each symbol.
        */
        JTableX symbolTable;
        Object[] symbolTableIdentifiers = {"", ""};
        
        
        /** This has symbol name as key and its row in the symbolTable as value. Thus it's easy to find
            out if a symbol is already included in symbolTable and a new row is not needed.
        */
        HashMap symbolsHashMap; 
        
        
        /** This holds (@link inputPanel) and (@link outputPanel).
        */
        JPanel ioPanel;
        
        /** This holds (@link outputScrollPane).
        */
        JPanel outputPanel;
        /** This holds (@link outputTextArea).
        */
        JScrollPane outputScrollPane;
        /** This text area is used to visualize the output data that Titokone sends to CRT.
        */
        JTextArea outputTextArea;
        
        /** This holds (@link enterNumberLabel enterNumberLabel), (@link inputField inputField) 
            and (@link enterNumberButton enterNumberButton).
        */
        JPanel inputPanel;
        /** This is used to show for example errors, when an invalid number is given etc.
        */
        JLabel enterNumberLabel;
        /** The number that will be sent to Titokone as KBD data is given here.
        */
        JTextField inputField;
        /** This sends the number to GUIBrain, which checks if it's valid and then sends it to Titokone.
        */
        JButton enterNumberButton;
    
        /** This holds (@link commentList commentList).
        */
        JScrollPane commentListScrollPane;
	      /** The comments are shown here.
        */
        JList commentList;
	      
	      
        
        JButton openFileButton;
        JButton compileButton;
        JButton runButton;
        JButton continueButton;
        JButton continueToEndButton;
        JButton stopButton;
        JToggleButton lineByLineToggleButton;
        JToggleButton showCommentsToggleButton;
        JToggleButton showAnimationToggleButton;
        
        JLabel statusBar;
        
        JMenu     fileMenu;
      	JMenuItem openFile;
	      JMenuItem compileMenuItem;
	      JMenuItem runMenuItem;
	      JMenuItem continueMenuItem;
        JMenuItem continueToEndMenuItem;
        JMenuItem stopMenuItem;
	      JMenuItem eraseMem;
        JMenuItem quit;
        
        JMenu optionsMenu;
        JMenu setMemSize;
        JMenu configureFileSystem;
        JMenuItem selectDefaultStdinFile;
        JMenuItem selectDefaultStdoutFile;
        JMenuItem setCompilingOptions;
        JMenuItem setRunningOptions;
        JMenuItem selectLanguageFromFile;
        
	      JMenu setLanguage;
	      
	      JMenu     helpMenu;
        JMenuItem manual;
	      JMenuItem about;
	
	      
	      public static final short R0 = 0,
        	                        R1 = 1,
        	                        R2 = 2,
        	                        R3 = 3,
        	                        R4 = 4,
        	                        R5 = 5,   
        	                        R6 = 6,   // r6 == sp
        	                        R7 = 7,   // r7 == fp
        	                        SP = 6,   // ^^
        	                        FP = 7,   // ^^
        	                        PC = 8;
        	                        

        public static final short COMPILE_COMMAND = 0,
                                  RUN_COMMAND = 1,
                                  STOP_COMMAND = 2,
                                  CONTINUE_COMMAND = 3,
                                  CONTINUE_WITHOUT_PAUSES_COMMAND = 4,
                                  INPUT_FIELD = 5,
                                  CODE_TABLE_EDITING = 6;

	      
        private int activeView = 0;
        
        
        private JFileChooser openFileDialog;
        private JFileChooser selectDefaultStdinFileDialog;
        private JFileChooser selectDefaultStdoutFileDialog;
        private JFileChooser selectLanguageFileDialog;
        
        private GUIRunSettingsDialog setRunningOptionsDialog;
        private GUICompileSettingsDialog setCompilingOptionsDialog;
        
        private GUIAboutDialog aboutDialog;
                
        private Font tableFont = new Font("Courier", java.awt.Font.PLAIN, 12);
                
        private Border blacklined = BorderFactory.createLineBorder(Color.black);
          
        
        private static final int COMMENT_LIST_SIZE = 100;

        public static final String resourceHomeDir = "fi/hu/cs/titokone/";



/** This is called when ActionEvent of some kind is fired.
*/
public void actionPerformed(ActionEvent e) { 
  
  /* This next if-statement is true,when user has pushed apply-button in 
     'set running options' dialog.
  */
  if (e.getActionCommand().equals(GUIRunSettingsDialog.APPLY)) {
    guibrain.menuSetRunningOption(GUIBrain.LINE_BY_LINE, setRunningOptionsDialog.lineByLineCheckBox.isSelected());
    guibrain.menuSetRunningOption(GUIBrain.COMMENTED, setRunningOptionsDialog.showCommentsCheckBox.isSelected());
    guibrain.menuSetRunningOption(GUIBrain.ANIMATED, setRunningOptionsDialog.showAnimationCheckBox.isSelected());
  }
  
  /* And this if-statement is true,when user has pushed apply-button in 
     'set compiling options' dialog.
  */
  else if (e.getActionCommand().equals(GUICompileSettingsDialog.APPLY)) {
    guibrain.menuSetCompilingOption(GUIBrain.PAUSED, setCompilingOptionsDialog.lineByLineCheckBox.isSelected());
    guibrain.menuSetCompilingOption(GUIBrain.COMMENTED, setCompilingOptionsDialog.showCommentsCheckBox.isSelected());
  }
}



public GUI() {

  thisGUI = this;    
  System.out.println("Initializing setRunningOptionsDialog...");        
  setRunningOptionsDialog = new GUIRunSettingsDialog(this, false);
  setRunningOptionsDialog.addComponentListener( new ComponentListener() {
    public void componentShown(ComponentEvent e) {}
    public void componentHidden(ComponentEvent e) {
      guibrain.refreshRunningOptions();
    }
    public void componentMoved(ComponentEvent e) {}
    public void componentResized(ComponentEvent e) {}
  });
  
  System.out.println("Initializing setCompilingOptionsDialog...");        
  setCompilingOptionsDialog = new GUICompileSettingsDialog(this, false);
  setCompilingOptionsDialog.addComponentListener( new ComponentListener() {
    public void componentShown(ComponentEvent e) {}
    public void componentHidden(ComponentEvent e) {
      guibrain.refreshCompilingOptions();
    }
    public void componentMoved(ComponentEvent e) {}
    public void componentResized(ComponentEvent e) {}
  });
  
  System.out.println("Initializing symbolsHashMap...");        
  symbolsHashMap = new HashMap();
  
  System.out.println("Initializing GUI...");        
  initGUI();
  System.out.println("Initializing GUIBrain...");        
  guibrain = new GUIBrain(this);
  System.out.println("Inserting menubar...");        
  insertMenuBar(this);
  disable(GUI.COMPILE_COMMAND);
  disable(GUI.RUN_COMMAND);
  disable(GUI.CONTINUE_COMMAND);
  disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
  disable(GUI.STOP_COMMAND);
  
  
	System.out.println("Packing...");        
  this.pack();
	System.out.println("Setting visible...");        
  this.setVisible(true);
	
	
  System.out.println("Setting title...");        
  setTitle("Titokone");
  
  addWindowListener( new WindowAdapter () {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	);
	
	
  try {
    System.out.println("Initializing selectDefaultStdinFileDialog...");        
    selectDefaultStdinFileDialog = new JFileChooser();
    System.out.println("Initializing selectDefaultStdoutFileDialog...");        
    selectDefaultStdoutFileDialog = new JFileChooser();
    System.out.println("Initializing openFileDialog...");        
    openFileDialog = new JFileChooser();
    System.out.println("Initializing selectLanguageFileDialog...");        
    selectLanguageFileDialog = new JFileChooser();
	
  }
  catch (NullPointerException e) {
    System.out.println("Exiting due to a bug in recent Java version.\n"+
                       "Please start again.");
    System.exit(0);
  }
  
  openFileDialog.setFileFilter(B91FileFilter);
	openFileDialog.setFileFilter(K91FileFilter);
	openFileDialog.setAcceptAllFileFilterUsed(false);
  selectDefaultStdinFileDialog.setAcceptAllFileFilterUsed(true);
  selectDefaultStdoutFileDialog.setAcceptAllFileFilterUsed(true);
  selectLanguageFileDialog.setFileFilter(classFileFilter);
  selectLanguageFileDialog.setAcceptAllFileFilterUsed(false);
  
  aboutDialog = new GUIAboutDialog(this, false);
 
  
  System.out.println("Updating texts...");        
  updateAllTexts();
  
  
	System.out.println("Complete!");        
	 
}




public void setGUIView(int view) {
    
  leftPanel = new JPanel(new BorderLayout());
  
  if (view == 1) {
    mainSplitPane.setLeftComponent(leftPanel);
    validate();
  }
  else if (view == 2) {
    
    leftPanel.add(codeTableScrollPane);
    mainSplitPane.setLeftComponent(leftPanel);
    validate();
  }
  else if (view == 3) {
    leftPanel.add(dataAndInstructionsTableSplitPane);
    mainSplitPane.setLeftComponent(leftPanel);
    validate();
    int dividerLocation;
  
    /* Set the location of divider between data and instructions table */
    dividerLocation = instructionsTable.getHeight();
    dividerLocation += dataAndInstructionsTableSplitPane.getDividerSize();
    dividerLocation += instructionsTable.getTableHeader().getHeight();
    if (dividerLocation > leftPanel.getHeight() / 2) {
      dividerLocation = leftPanel.getHeight() / 2;
    }
    dataAndInstructionsTableSplitPane.setDividerLocation(dividerLocation);
    leftPanel.invalidate();
    
  }
  activeView = view;
  mainSplitPane.setDividerLocation(0.5);
}




/** GUIBrain can call this method to reset GUI, which means that all tables are
    emptied and all their rows are unselected.
*/
public void resetAll() {
 
  unselectAll();
  insertSymbolTable(null);
  outputTextArea.setText("");
}



/** Unselects all selected rows in every table.
*/
public void unselectAll() {
 
  codeTable.unselectAllRows();
  instructionsTable.unselectAllRows();
  dataTable.unselectAllRows();
  registersTable.unselectAllRows();
  symbolTable.unselectAllRows();
  this.repaint();
}



/** Inserts a text into status bar at the bottom of the screen.
    @param str The text to be inserted.
*/
public void updateStatusBar(String str) {
  statusBar.setText(str+" ");
  statusBar.validate();
}


/** Updates a value of a register in registersTable.
    @param reg The register to be updated. Valid values are R1, R2, R3, R3, R4,
               R5, R6, R7, SP and FP. (R6==SP and R7==FP).
    @param newValue The new value.
*/
public void updateReg(short reg, int newValue) {
	updateReg(reg, new Integer(newValue));
}


/** Updates a register value.
    @param reg The register to be updated.
    @param newValue The new value.
*/
public void updateReg(short reg, Integer newValue) {
  if (newValue == null) {
  return;
  }
  DefaultTableModel registersTableModel = (DefaultTableModel)registersTable.getModel(); 
  registersTable.setValueAt(""+newValue.intValue(), reg, 1);
}



/** Inserts data into code table. The data must be provided as a String-array, where
    one element corresponds to one line of original data. One element will be shown
    as one row in GUI.
    @param src The data to be shown and one element corresponds to one line.
*/
public void insertToCodeTable(String[] src) {
  
  int rows = src.length;
  Object[][] tableContents = new Object[rows][1];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = src[i];
  }
    
  DefaultTableModel codeTableModel = (DefaultTableModel)codeTable.getModel(); 
  codeTableModel.setDataVector(tableContents, codeTableIdentifiers);
  codeTable.getColumnModel().getColumn(0).setPreferredWidth(codeTable.getMaxTextLengthInColumn(0));   
}





/** Inserts data to instructionsTable. The data must be provided so that the dimension
    of both parameter is same. If they aren't, then false is returned and no insertion
    made. The second column will be filled with binaryCommand's contents and the third
    column with symbolicCommand's contents. The first column will contain line numbers
    which are 0...N, where N is size of the table.
    @param binaryCommand Contents of the second column.
    @param symbolicCommand Contents of the third column.
    @return True if operation was successful.
            False if the dimension of the parameters is not same.
*/
public boolean insertToInstructionsTable(String[] binaryCommand, String[] symbolicCommand) {
  
  if (binaryCommand.length != symbolicCommand.length) {
    return false;
  }
  int rows = binaryCommand.length;
  DefaultTableModel instructionsTableModel = (DefaultTableModel)instructionsTable.getModel(); 
  Object[][] tableContents = new Object[rows][3];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = ""+i;
    tableContents[i][1] = binaryCommand[i];
    tableContents[i][2] = symbolicCommand[i];
  }
  instructionsTableModel.setDataVector(tableContents, instructionsTableIdentifiers);
  instructionsTable.validate();
  return true;
}


/** Functionality of this method is exactly similar to insertToInstructionsTable(String[],String[]),
    but here the first parameter's type is int[]. That's for convenience, because otherwise
    it would require an additional for-loop to convert int[] to String[].
    @param binaryCommand Contents of the second column.
    @param symbolicCommand Contents of the third column.
    @return True if operation was successful.
            False if the dimension of the parameters is not same.
*/
public boolean insertToInstructionsTable(int[] binaryCommand, String[] symbolicCommand) {
  
  if (binaryCommand.length != symbolicCommand.length) {
    return false;
  }
  int rows = binaryCommand.length;
  DefaultTableModel instructionsTableModel = (DefaultTableModel)instructionsTable.getModel(); 
  Object[][] tableContents = new Object[rows][3];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = ""+i;
    tableContents[i][1] = ""+binaryCommand[i];
    tableContents[i][2] = symbolicCommand[i];
  }
  instructionsTableModel.setDataVector(tableContents, instructionsTableIdentifiers);
  return true;
}



/** Functionality of this method is exactly similar to insertToInstructionsTable(String[],String[]),
    but here the first parameter would be an array of empty Strings. Thus the second column
    will empty after calling this method.
    @param symbolicCommand Contents of the third column.
    @return True if operation was successful.
            False if the dimension of the parameters is not same.
*/
public boolean insertToInstructionsTable(String[] symbolicCommand) {
  String[] empty = new String[symbolicCommand.length];
  return insertToInstructionsTable(empty, symbolicCommand);
}



/** Updates contents of a line in either instructions table or data table. Parameter lineNumber
    decides which one to update - lines 0...N are rows in instructionsTable and lines N+1...N+P
    are rows in dataTable, where N is the row count of instructionsTable and P is the row count
    of dataTable.
    @param lineNumber The line number of the row to update. Lines 0...N are rows in instructionsTable 
                      and lines N+1...N+P are rows in dataTable, where N is the row count of 
                      instructionsTable and P is the row count of dataTable. 
    @param binaryCommand The content of the node in the second column.
    @param symbolicCommand The content of the node in the third column.
*/
public boolean updateInstructionsAndDataTableLine(int lineNumber, int binaryCommand, String symbolicCommand) {
  
  if (lineNumber < 0)
    return false;
  else if (lineNumber < instructionsTable.getRowCount()) {
    ((DefaultTableModel)instructionsTable.getModel()).setValueAt(""+binaryCommand, lineNumber, 1);
    ((DefaultTableModel)instructionsTable.getModel()).setValueAt(symbolicCommand, lineNumber, 2);
    return true;
  }
  else if (lineNumber < instructionsTable.getRowCount() + dataTable.getRowCount()) {
    lineNumber = lineNumber-instructionsTable.getRowCount();
    ((DefaultTableModel)dataTable.getModel()).setValueAt(""+binaryCommand, lineNumber, 1);
    ((DefaultTableModel)dataTable.getModel()).setValueAt(symbolicCommand, lineNumber, 2);
    return true;
  }
  else {
    return false;
  }
}



/** Functionality of this method is otherwise similar to updateInstructionsAndDataTableLine(int,int,String),
    but this one doesn't change the third column. This version is useful during compilation, where
    the contents of the third column are set, but the second column is empty at first and as the compilation
    goes on, it's contents are updated.
    @param lineNumber The line number of the row to update. Lines 0...N are rows in instructionsTable 
                      and lines N+1...N+P are rows in dataTable, where N is the row count of 
                      instructionsTable and P is the row count of dataTable. 
    @param binaryCommand The content of the node in the second column.
    @return True if the operation was successful.
            False is there's was no such line as lineNumber.
*/
public boolean updateInstructionsAndDataTableLine(int lineNumber, int binaryCommand) {
  
  if (lineNumber < 0) 
    return false;
  else if (lineNumber < instructionsTable.getRowCount()) {
    ((DefaultTableModel)instructionsTable.getModel()).setValueAt(""+binaryCommand, lineNumber, 1);
    return true;
  }
  else if (lineNumber < instructionsTable.getRowCount() + dataTable.getRowCount()) {
    lineNumber = lineNumber-instructionsTable.getRowCount();
    ((DefaultTableModel)dataTable.getModel()).setValueAt(""+binaryCommand, lineNumber, 1);
    return true;
  }
  else {
    return false;
  }
}



/** Inserts data into data table. Parameter dataContents is inserted into the 
    second column and the third column will be left empty. The first column holds
    numbers, which are line numbers. They will be calculated so that the first 
    element will be one greater than the last element of instructionsTable. Because
    of this, this method should only be called AFTER calling insertToInstructionsTable()
    or otherwise line numbers will not be correct. 
    @param dataContents Contents of the second column.
*/
public void insertToDataTable(String[] dataContents) {
  
  int rows = dataContents.length;
  int instructionsTableRowCount = instructionsTable.getRowCount();
  
  DefaultTableModel dataTableModel = (DefaultTableModel)dataTable.getModel(); 
  Object[][] tableContents = new Object[rows][3];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = ""+ (i + instructionsTableRowCount);
    tableContents[i][1] = ""+dataContents[i];
    tableContents[i][2] = "";
  }
  dataTableModel.setDataVector(tableContents, dataTableIdentifiers);
}



/** Functionally this is exactly similar to insertToDataTable(String[]), but this
    just takes int[] as parameter. This is here just for convenience, because
    converting a String-table to int-table would require an extra for-loop.
    @param data Contents of the second column.
*/
public void insertToDataTable(int[] data) {
  
  int rows = data.length;
  int instructionsTableRowCount = instructionsTable.getRowCount();
  
  DefaultTableModel dataTableModel = (DefaultTableModel)dataTable.getModel(); 
  Object[][] tableContents = new Object[rows][3];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = ""+ (i + instructionsTableRowCount);
    tableContents[i][1] = ""+data[i];
    tableContents[i][2] = "";
  }
  dataTableModel.setDataVector(tableContents, dataTableIdentifiers);
}



/** Inserts symbols and their values into symbol table. A symbol's name is given
    in symbolsAndValues[x][0] and its value in symbolsAndValues[x][1]. This method
    first makes the table and its HashMap empty and then enters the symbol and
    the values into both the table and its HashMap.
    @param symbolsAndValues A symbol's name is given in symbolsAndValues[x][0] and 
                            its value in symbolsAndValues[x][1]. 
                            This can be null, in which case symbol table will be 
                            set empty.
    @return Returns false if parameter is not of correct form. It MUST be of such 
            type where the size of the second dimension is precisely two. 
            It can however be null, which means an empty symbol table.
*/
public boolean insertSymbolTable(String[][] symbolsAndValues) {
  
  /* These are declared just to make the code clearer. They are used to get
     symbol names and values from symbolsAndValues[][].
  */
  final int NAME = 0;
  final int VALUE = 1;
  
  symbolsHashMap.clear();
  DefaultTableModel symbolTableModel = (DefaultTableModel)symbolTable.getModel(); 
  
  /* Null parameter equals to setting an empty symbol table. */
  if (symbolsAndValues == null || symbolsAndValues.length == 0) {
    symbolTableModel.setDataVector(null, symbolTableIdentifiers);
    return true;
  }
  if (symbolsAndValues[0].length != 2) {
    return false;
  }
  
  int numOfSymbols = symbolsAndValues.length;
  
  
  Object[][] tableContents = new Object[numOfSymbols][2];
  for (int row=0 ; row<numOfSymbols ; row++) {
    tableContents[row][0] = symbolsAndValues[row][NAME];
    tableContents[row][1] = symbolsAndValues[row][VALUE];
    symbolsHashMap.put(symbolsAndValues[row][NAME], new Integer(row));
  }
  symbolTableModel.setDataVector(tableContents, symbolTableIdentifiers);
  return true;
}




/** Updates the value of a symbol if it already exists, or adds a new row
    and inserts the symbol and its value there.
*/
public void updateRowInSymbolTable(String symbolName, Integer symbolValue) {
  
  /* Object row tells the row in GUI's symbol table, where symbolName has been
     saved or it's null if it hasn't been saved there yet.
  */  
  Integer row = (Integer)symbolsHashMap.get(symbolName);
  DefaultTableModel symbolTableModel = (DefaultTableModel)symbolTable.getModel(); 
 
  if (row != null) {
  /* symbolName is already in symbol table so a new row is not needed. The old
     value is just updated if there's any. Note that symbolValue may also be null,
     which means that the symbol was found, but not yet set. In that case the 
     value in GUI is set to an empty string.
  */
    if (symbolValue == null) {
      symbolTableModel.setValueAt("", row.intValue(), 1);
    }
    else {
      symbolTableModel.setValueAt(symbolValue, row.intValue(), 1);
    }
  }
  else {
    /* symbolName is not in symbol table so now a new is needed
    */
    DefaultTableModel tableModel = (DefaultTableModel)symbolTable.getModel();
    String[] data;
    if (symbolValue == null) {
      data = new String[]{symbolName, ""};
    }
    else {
      data = new String[]{symbolName, ""+symbolValue};
    }
    tableModel.addRow(data);
  
    row = new Integer(symbolTable.getRowCount() - 1);
    symbolsHashMap.put(symbolName, row);
  }
}




/** Adds a comment into the comment list.
*/
public void addComment(String comment) {
  if (comment == null) 
    return;
    
  DefaultListModel commentListModel = (DefaultListModel)commentList.getModel();
  int numberOfComponents = commentListModel.size();
  
  /* Comment list is not allowed to grow over COMMENT_LIST_SIZE */
  if (numberOfComponents == COMMENT_LIST_SIZE) {
    commentListModel.removeElementAt(numberOfComponents-1);
  }
  commentListModel.add(0, comment);
  commentList.setSelectedIndex(0);
}




/** Adds a number into the text area on the right side of the screen.
*/
public void addOutputData(int outputValue) {
  outputTextArea.insert(outputValue + "\n", 0);
 
  /*      // This commented code implements this method in a way that it adds a new row
          // at the end of the TextArea. It's not removed since it may be useful.
  
  outputTextArea.append(outputValue + "\n");
  int height = outputTextArea.getHeight();
  int viewHeight = (int)outputScrollPane.getHeight();
  int newY = 0;
  if (height-viewHeight > 0) {
    newY = height-viewHeight+3;
  }
  outputScrollPane.getViewport().setViewPosition(new Point(0, newY));
  */
}





/** Enables or disables a given command.
    @param command The command to be set. It can be RUN_COMMAND, STOP_COMMAND,
                   COMPILE_COMMAND, CONTINUE_COMMAND, CONTINUE_WITHOUT_PAUSES_COMMAND
                   INPUT_FIELD.
    @param b 'True' to enable the command. 'False' to disable it.
*/
public void setEnabled(short command, boolean b) {
  switch (command) {
    case RUN_COMMAND:
      runMenuItem.setEnabled(b);
      runButton.setEnabled(b);
      break;
    case STOP_COMMAND:
      stopMenuItem.setEnabled(b);
      stopButton.setEnabled(b);
      break;
    case COMPILE_COMMAND:
      compileMenuItem.setEnabled(b);
      compileButton.setEnabled(b);
      break;
    case CONTINUE_COMMAND:
      continueMenuItem.setEnabled(b);
      continueButton.setEnabled(b);
      break;
    case CONTINUE_WITHOUT_PAUSES_COMMAND:
      continueToEndMenuItem.setEnabled(b);
      continueToEndButton.setEnabled(b);
      break; 
    case INPUT_FIELD:
      enterNumberLabel.setEnabled(b);
      inputField.setEnabled(b);
      enterNumberButton.setEnabled(b);
      inputField.setText("");
      break;
    case CODE_TABLE_EDITING:
      codeTable.setEnabled(b);
      break;
  }
}




/** Enables a command.
    @param command The command to be enabled. It can be RUN_COMMAND, STOP_COMMAND,
                   COMPILE_COMMAND, CONTINUE_COMMAND, CONTINUE_WITHOUT_PAUSES_COMMAND
                   INPUT_FIELD.
*/
public void enable(short command) {
  setEnabled(command, true);
}
  



/** Disables a command.
    @param command The command to be Disabled. It can be RUN_COMMAND, STOP_COMMAND,
                   COMPILE_COMMAND, CONTINUE_COMMAND, CONTINUE_WITHOUT_PAUSES_COMMAND
                   INPUT_FIELD.
*/
public void disable(short command) {
  setEnabled(command, false);
}




/** paused compiling - used in setSelected() method */
public static final short OPTION_COMPILING_PAUSED = 0;
/** commented compiling - used in setSelected() method */
public static final short OPTION_COMPILING_COMMENTED = 1;
/** paused running - used in setSelected() method */
public static final short OPTION_RUNNING_PAUSED = 2;
/** commented running - used in setSelected() method */
public static final short OPTION_RUNNING_COMMENTED = 3;
/** animated running - used in setSelected() method */
public static final short OPTION_RUNNING_ANIMATED = 4;


/** Sets a certain option selected or unselected in GUI. Note that this doesn't
    take any position on what the options does. GUIBrain uses this method to
    to synchronize GUI for settings loaded from settings file.
*/
public void setSelected(short option, boolean b) {
  switch (option) {
    case OPTION_COMPILING_PAUSED:
      setCompilingOptionsDialog.lineByLineCheckBox.setSelected(b);
      break;
    case OPTION_COMPILING_COMMENTED:
      setCompilingOptionsDialog.showCommentsCheckBox.setSelected(b);
      break;
    case OPTION_RUNNING_PAUSED:
      lineByLineToggleButton.setSelected(b);
      setRunningOptionsDialog.lineByLineCheckBox.setSelected(b);
      break;
    case OPTION_RUNNING_COMMENTED:
      showCommentsToggleButton.setSelected(b);
      setRunningOptionsDialog.showCommentsCheckBox.setSelected(b);
      break;
    case OPTION_RUNNING_ANIMATED:
      showAnimationToggleButton.setSelected(b);
      setRunningOptionsDialog.showAnimationCheckBox.setSelected(b);
      break;  
  }
}
  


/** No table - used in setGUIView() method */
public static final short NONE = 1;
/** Code table - used in centerToLine(), selectRow() and setGUIView() methods */
public static final short CODE_TABLE = 2;
/** Instructions and data table - used in centerToLine(), selectRow() and setGUIView() methods */
public static final short INSTRUCTIONS_AND_DATA_TABLE = 3;




/** Sets the viewport of a certain table so that the given line is visible.
    @param line Number of the line, that is wanted to be visible.
    @param table The table. Valid values for this parameter are CODE_TABLE 
                 and INSTRUCTIONS_AND_DATA_TABLE
    @returns True if the operation was successful.
             False if the line number was not valid - ie there's no such line
             in the table or there's no such table.
*/
public boolean centerToLine(int line, short table) {
  
  switch (table) {
    case INSTRUCTIONS_AND_DATA_TABLE:
      if ( line >= (instructionsTable.getRowCount() + dataTable.getRowCount()) || line < 0) {
        return false;
      }
      
      JScrollPane activeScrollPane;
      JTableX activeTable;
      
      if (line < instructionsTable.getRowCount()) {
        activeScrollPane = instructionsTableScrollPane;
        activeTable = instructionsTable;
      }
      else {
        activeScrollPane = dataTableScrollPane;
        activeTable = dataTable;
        line -= instructionsTable.getRowCount();
      }
        
      int tableViewHeight = activeScrollPane.getHeight() - activeTable.getTableHeader().getHeight();
      int y;
      
      if (tableViewHeight > activeTable.getHeight()) {
        y = 0;
      }
      else {
        y = line * activeTable.getRowHeight() - tableViewHeight/2 + activeTable.getRowHeight()/2;
        y = (y<0)?0:y;
        if (y + tableViewHeight > activeTable.getHeight()) {
          y = activeTable.getHeight() - tableViewHeight + activeTable.getRowMargin() + 2; 
          /* I don't know where that number 2 comes from, but I included it there, because the viewport
             doesn't go to exactly right place without it. Otherwise it'd be misplaced by two pixels. :) */
        }          
      }
      activeScrollPane.getViewport().setViewPosition(new Point(0, y));
        
      return true;
    
    default:
      break;
  }
  return false;
}
      


/** Selects a row from code table or from instructions and data table. Instructions
    and data table, although two tables, are treated as one. Suppose that instructions
    table is n rows and data table is m rows. This is treated as one table of length
    n+m rows and its n first rows are same as instruction table's rows and m next
    rows are same as data table's rows.
    @param table The desired table. Proper values are CODE_TABLE and 
                 INSTRUCTIONS_AND_DATA_TABLE.
    @param line Line to be selected.
*/
public void selectLine(int line, short table) {
  
  if (line < 0) {
    return;
  }
  
  switch (table) {
    case CODE_TABLE:
      if (line > codeTable.getRowCount()) {
        return;
      }
      codeTable.selectRow(line);
      break;
    
    case INSTRUCTIONS_AND_DATA_TABLE:
      if ( line >= (instructionsTable.getRowCount() + dataTable.getRowCount()) ) {
        return;
      }
      
      if (line < instructionsTable.getRowCount()) {
        centerToLine(line, INSTRUCTIONS_AND_DATA_TABLE);       
        instructionsTable.selectRow(line);
      }
      else if (line == instructionsTable.getRowCount()) {
        instructionsTable.unselectAllRows();
        dataTable.selectRow(line - instructionsTable.getRowCount());
        dataTableScrollPane.getViewport().setViewPosition(new Point(0, 0));
      }
      else {
        int dataTableRow = line - instructionsTable.getRowCount();
        centerToLine(line, INSTRUCTIONS_AND_DATA_TABLE);
        dataTable.selectRow(dataTableRow);
        
      }
      break;
    
    default:
      break;
  }
  this.repaint();
}



/** Changes the text which is shown in KBD-frame above the text field. If
    the new text is an empty string, then the label will disappear.
    @para newText The new text to be shown. If this is empty, then the label
                  will disappear.
*/
public void changeTextInEnterNumberLabel(String newText) {
  enterNumberLabel.setText(newText);
}



/** Updates all texts that are shown in GUI to be shown in current language. If
    language hasn't changed since last call of this method, then nothing will
    happen, but if it has, then all the text will appear in it. GUIBrain calls
    this method in setLanguage() method.
*/
public void updateAllTexts() {
  fileMenu.setText( new Message("File").toString() );
  openFile.setText( new Message("Open").toString() );
	compileMenuItem.setText( new Message("Compile").toString() );
  runMenuItem.setText( new Message("Run").toString() );
	continueMenuItem.setText( new Message("Continue").toString() );
	continueToEndMenuItem.setText( new Message("Continue without pauses").toString() );
	stopMenuItem.setText( new Message("Stop").toString() );
	eraseMem.setText( new Message("Erase memory").toString() );
	quit.setText( new Message("Exit").toString() );
	optionsMenu.setText( new Message("Options").toString() );
  setMemSize.setText( new Message("Set memory size").toString() );
  helpMenu.setText( new Message("Help").toString() );
  manual.setText( new Message("Manual").toString() );
	about.setText( new Message("About").toString() );
	setCompilingOptions.setText( new Message("Set compiling options").toString() );
  setRunningOptions.setText( new Message("Set running options").toString() );
  configureFileSystem.setText( new Message("Configure file system").toString() );
  selectDefaultStdinFile.setText( new Message("Select default stdin file").toString() );
  selectDefaultStdoutFile.setText( new Message("Select default stdout file").toString() );
  setLanguage.setText( new Message("Set language").toString() );
  selectLanguageFromFile.setText( new Message("Select from a file...").toString() );
	
	instructionsTableIdentifiers = new Object[] { new Message("Line").toString(),
	                                              new Message("Numeric").toString(),
	                                              new Message("Symbolic").toString() };
  ((DefaultTableModel)instructionsTable.getModel()).setColumnIdentifiers(instructionsTableIdentifiers);
	
  dataTableIdentifiers = new Object[] { new Message("Line").toString(),
	                                      new Message("Numeric").toString(),
	                                      new Message("Symbolic").toString() };
  ((DefaultTableModel)dataTable.getModel()).setColumnIdentifiers(dataTableIdentifiers);
	
	openFileButton.setToolTipText(new Message("Open a new file").toString());
  compileButton.setToolTipText(new Message("Compile the opened file").toString());
  runButton.setToolTipText(new Message("Run the loaded program").toString());
  continueButton.setToolTipText(new Message("Continue current operation").toString());
  continueToEndButton.setToolTipText(new Message("Continue current operation without pauses").toString());
  stopButton.setToolTipText(new Message("Stop current operation").toString());
  
  enterNumberButton.setText(new Message("Enter").toString());
  
  ((TitledBorder)symbolTableScrollPane.getBorder()).setTitle( new Message("Symbol table").toString() );
  ((TitledBorder)registersTableScrollPane.getBorder()).setTitle( new Message("Registers").toString() );
  
  
  UIManager.put ("FileChooser.lookInLabelText", new Message("Look in:").toString());
  UIManager.put ("FileChooser.lookInLabelMnemonic", new Message("Look in:").toString());
  UIManager.put ("FileChooser.fileNameLabelText", new Message("File name:").toString());
  UIManager.put ("FileChooser.fileNameLabelMnemonic", new Message("File name:").toString());
  UIManager.put ("FileChooser.filesOfTypeLabelText", new Message("Files of type:").toString());
  UIManager.put ("FileChooser.filesOfTypeLabelMnemonic", new Message("Files of type:").toString());
  UIManager.put ("FileChooser.upFolderToolTipText", new Message("Up one level").toString());
  UIManager.put ("FileChooser.upFolderAccessibleName", new Message("Up").toString());
  UIManager.put ("FileChooser.homeFolderToolTipText", new Message("Desktop").toString());
  UIManager.put ("FileChooser.homeFolderAccessibleName", new Message("Desktop").toString());
  UIManager.put ("FileChooser.newFolderToolTipText", new Message("Create new folder").toString());
  UIManager.put ("FileChooser.newFolderAccessibleName", new Message("New folder").toString());
  UIManager.put ("FileChooser.listViewButtonToolTipText", new Message("List").toString());
  UIManager.put ("FileChooser.listViewButtonAccessibleName", new Message("List").toString());
  UIManager.put ("FileChooser.detailsViewButtonToolTipText", new Message("Details").toString());
  UIManager.put ("FileChooser.detailsViewButtonAccessibleName", new Message("Details").toString()); 
  UIManager.put ("FileChooser.cancelButtonText", new Message("Cancel").toString());
  UIManager.put ("FileChooser.cancelButtonMnemonic", new Message("Cancel").toString());
  UIManager.put ("FileChooser.openButtonText", new Message("Open").toString());
  UIManager.put ("FileChooser.openButtonMnemonic", new Message("Open").toString());
  UIManager.put ("FileChooser.acceptAllFileFilterText", new Message("All files").toString());
  UIManager.put ("FileChooser.FileChooser.openButtonText", new Message("Open").toString());
  UIManager.put ("FileChooser.openButtonToolTipText", new Message("Open the selected file").toString());
  
  selectDefaultStdinFileDialog.updateUI();
  selectDefaultStdoutFileDialog.updateUI();
  
  openFileDialog.setDialogTitle( new Message("Open a new file").toString() );
  selectDefaultStdinFileDialog.setDialogTitle( new Message("Select default stdin file").toString() );
  selectDefaultStdoutFileDialog.setDialogTitle( new Message("Select default stdout file").toString() );
  
  setRunningOptionsDialog.updateAllTexts();
  setCompilingOptionsDialog.updateAllTexts();
  aboutDialog.updateAllTexts();
  
  
  UIManager.put("OptionPane.yesButtonText", new Message("Yes").toString());
  UIManager.put("OptionPane.noButtonText", new Message("No").toString());
    
  invalidate();
  validate();
  repaint();
}



/** Shows an error in a message dialog.
    @ param errorMsg The message to be shown.
*/
public void showError(String errorMsg) {
  JOptionPane.showMessageDialog(null, errorMsg, new Message("Error").toString(), JOptionPane.ERROR_MESSAGE);
}



public String[] getCodeTableContents() {

  Vector codeTableContents = ((DefaultTableModel)codeTable.getModel()).getDataVector();
  String[] codeTableContentsString = new String[codeTableContents.size()];
  
  int i = 0;
  for (Enumeration e = codeTableContents.elements(); e.hasMoreElements() ; i++) {
    codeTableContentsString[i] = (String)((Vector)e.nextElement()).get(0);
  }
  return codeTableContentsString;
}
  

// Private methods. ---------------------------------------------------



/** This is just a private assistance method for the creator. This prepares
    all the graphical tables,lists and such as well as puts them into the
    main frame. This won't be accessed but once in the creator method.
*/
private void initGUI() {
  
  codeTable = new JTableX(new DefaultTableModel(codeTableIdentifiers ,0));
  codeTable.setFont(tableFont);
  codeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
  codeTable.setEnabled(false);
  codeTable.setShowGrid(false);
  ((DefaultCellEditor)codeTable.getDefaultEditor(codeTable.getColumnClass(0))).addCellEditorListener( new CellEditorListener() {
     public void editingCanceled(ChangeEvent e) {}
     
     public void editingStopped(ChangeEvent e) {
       codeTable.getColumnModel().getColumn(0).setPreferredWidth(codeTable.getMaxTextLengthInColumn(0));   
     }
   });
  
  ((DefaultCellEditor)codeTable.getDefaultEditor(codeTable.getColumnClass(0))).getComponent().setFont(tableFont);
  codeTableScrollPane = new JScrollPane(codeTable);
  codeTableScrollPane.setForeground(Color.black);
    
  instructionsTable = new JTableX(new DefaultTableModel(instructionsTableIdentifiers,0));
  instructionsTable.setFont(tableFont);
  instructionsTable.setEnabled(false);
  instructionsTable.getColumnModel().getColumn(0).setMinWidth(20);
  instructionsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
  instructionsTableScrollPane = new JScrollPane(instructionsTable);
  
  dataTable = new JTableX(new DefaultTableModel(dataTableIdentifiers,0));
  dataTable.setEnabled(false);
  dataTable.setFont(tableFont);
  dataTable.setRowSelectionAllowed(false);
  dataTableScrollPane = new JScrollPane(dataTable);
  
  dataTable.setDoubleBuffered(true);
  dataTableScrollPane.setDoubleBuffered(true);
  
  dataAndInstructionsTableSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
  dataAndInstructionsTableSplitPane.setTopComponent(instructionsTableScrollPane);
  dataAndInstructionsTableSplitPane.setBottomComponent(dataTableScrollPane);
  dataAndInstructionsTableSplitPane.setOneTouchExpandable(true);
  
  statusBar = new JLabel();
  commentList = new JList(new DefaultListModel());
  ((DefaultListModel)commentList.getModel()).ensureCapacity(COMMENT_LIST_SIZE);
  
  symbolTable = new JTableX(new DefaultTableModel(new String[] {"",""}, 0));
  symbolTable.setFont(tableFont);
  symbolTable.setRowSelectionAllowed(false);
  symbolTableScrollPane = new JScrollPane(symbolTable);
  symbolTableScrollPane.setBorder(BorderFactory.createTitledBorder(blacklined, "Symbol table"));
  symbolTableScrollPane.setMinimumSize(new Dimension(200, 150));
 
  String[][] regTableContents = new String[][] 
    { {"R0",""}, {"R1",""}, {"R2",""}, {"R3",""}, {"R4",""}, 
      {"R5",""}, {"SP",""}, {"FP",""}, {"PC",""}};
  
  registersTable = new JTableX(new DefaultTableModel(regTableContents, registersTableIdentifiers));
  registersTable.setEnabled(false);
  registersTable.setFont(tableFont);
  registersTable.setRowSelectionAllowed(false);
  (registersTable.getColumnModel().getColumn(0)).setPreferredWidth(15);
  registersTableScrollPane = new JScrollPane(registersTable);
  registersTableScrollPane.setPreferredSize(new Dimension(150, 150));
  registersTableScrollPane.setBorder(BorderFactory.createTitledBorder(blacklined, "Registers"));
  
  ioPanel = new JPanel(new BorderLayout());
  inputPanel = new JPanel(new BorderLayout());
  outputPanel = new JPanel(new BorderLayout());
 
  outputTextArea = new JTextArea(1,7);
  outputTextArea.setLineWrap(true);
  outputTextArea.setWrapStyleWord(true);
  outputTextArea.setEditable(false);
  
  enterNumberLabel = new JLabel("");
  enterNumberLabel.setSize(new Dimension(100,100));
  
  inputField = new JTextField(11);
  inputField.addKeyListener( new KeyListener() {
    public void keyPressed(KeyEvent e) {}
    
    public void keyReleased(KeyEvent e) {
      if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
        enterInput();
      }
    }
    
    public void keyTyped(KeyEvent e) {}
  });
  
  enterNumberButton = new JButton("Enter");
  enterNumberButton.addActionListener(enterNumberButtonActionListener);
  
  outputScrollPane = new JScrollPane(outputTextArea);
  outputScrollPane.setPreferredSize(new Dimension(30,300));
  outputScrollPane.setBorder(BorderFactory.createTitledBorder(blacklined, "CRT"));
  outputPanel.add(outputScrollPane, BorderLayout.CENTER);
  
  inputPanel.add(enterNumberLabel, BorderLayout.NORTH);
  inputPanel.add(inputField, BorderLayout.CENTER);
  inputPanel.add(enterNumberButton, BorderLayout.SOUTH);
  inputPanel.setBorder(BorderFactory.createTitledBorder(blacklined, "KBD"));
  
  ioPanel.add(outputPanel, BorderLayout.CENTER);
  ioPanel.add(inputPanel, BorderLayout.SOUTH);
  
  upperRightPanel = new JPanel(new BorderLayout());
  upperRightPanel.add(registersTableScrollPane, BorderLayout.EAST);
  upperRightPanel.add(symbolTableScrollPane, BorderLayout.CENTER);
  upperRightPanel.add(ioPanel, BorderLayout.WEST);
  
  commentListScrollPane = new JScrollPane(commentList);
  commentListScrollPane.setPreferredSize(new Dimension(1,50));
  commentList.setDoubleBuffered(true);
  commentListScrollPane.setDoubleBuffered(true);
  
  commentList.addListSelectionListener( new ListSelectionListener() {
    public void valueChanged( ListSelectionEvent e ) {
      JList src = (JList)(e.getSource());
      String str = (String)src.getSelectedValue();
      
      Integer line = null;
      int i = 1;
      while (true) {
        try {
          line = new Integer(str.substring(0,i));
          
        }
        catch (NumberFormatException excp) {
          break;
        }
        catch (IndexOutOfBoundsException excp2) {
          break;
        }
        i++;
      }
      
      if (line != null) {
        if (activeView == 3) {
          centerToLine(line.intValue(),INSTRUCTIONS_AND_DATA_TABLE);
        }
      }
    }
  } );
  
  rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperRightPanel, commentListScrollPane);
  rightSplitPane.setResizeWeight(0.5);
  
  
  leftPanel = new JPanel(new BorderLayout());
  mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplitPane);
  mainSplitPane.setResizeWeight(0.5);
  
  getContentPane().setLayout( new BorderLayout() );
  getContentPane().add(mainSplitPane, BorderLayout.CENTER);
  getContentPane().add( makeToolBar() , BorderLayout.NORTH);
  
  statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
  getContentPane().add(statusBar, BorderLayout.SOUTH);
  
  disable(INPUT_FIELD);
  setGUIView(1);
  
}



private void enterInput() {
  String input;
  input = inputField.getText();
  
	if(guibrain.enterInput(input) == false) {  
	  return;
	}
	
	guibrain.continueTask();
}


/** Inserts the menu into main window.
    @param destFrame The frame the menu bar will be inserted.
*/
private void insertMenuBar(JFrame destFrame) {

  JMenuBar  mainMenuBar = new JMenuBar();
	fileMenu              = new JMenu("File");
	openFile              = fileMenu.add("Open");
	compileMenuItem       = fileMenu.add("Compile");
	runMenuItem           = fileMenu.add("Run");
	continueMenuItem      = fileMenu.add("Continue");
	continueToEndMenuItem = fileMenu.add("Continue without interruptions");
	stopMenuItem          = fileMenu.add("Stop");
	eraseMem              = fileMenu.add("Erase memory");
	quit                  = fileMenu.add("Exit");
  
  optionsMenu           = new JMenu("Options");
  
  setMemSize                      = new JMenu("Set memory size");
  JMenuItem setMemTo512           = setMemSize.add("512");
  JMenuItem setMemTo1024          = setMemSize.add("1024");
  JMenuItem setMemTo2048          = setMemSize.add("2048");
  JMenuItem setMemTo4096          = setMemSize.add("4096");
  JMenuItem setMemTo8192          = setMemSize.add("8192");
  JMenuItem setMemTo16384         = setMemSize.add("16384");
  JMenuItem setMemTo32768         = setMemSize.add("32768");
  JMenuItem setMemTo65536         = setMemSize.add("65536");
  optionsMenu.add(setMemSize);
  
  configureFileSystem             = new JMenu("Configure file system");
  selectDefaultStdinFile     = configureFileSystem.add("Select default stdin file");
  selectDefaultStdoutFile    = configureFileSystem.add("Select default stdout file");
  optionsMenu.add(configureFileSystem);
  
  setCompilingOptions     = optionsMenu.add("Set compiling options");
  setRunningOptions     = optionsMenu.add("Set running options");
  
  setLanguage                     = new JMenu("Set language");
  String[] languages = guibrain.getAvailableLanguages();
  for (int i=0 ; i<languages.length ; i++) {
    JMenuItem newLanguage = setLanguage.add(languages[i]);
    newLanguage.addActionListener( setLanguageActionListener );
      
  }
  selectLanguageFromFile = setLanguage.add("Select from file...");
  optionsMenu.add(setLanguage);
  
  
  helpMenu    = new JMenu("Help");
  manual      = helpMenu.add("Manual");
	about       = helpMenu.add("About");
	
	mainMenuBar.add(fileMenu);
	mainMenuBar.add(optionsMenu);
	mainMenuBar.add(helpMenu);
	destFrame.setJMenuBar(mainMenuBar);
	
	openFile.addActionListener(openCommandActionListener);
  compileMenuItem.addActionListener(compileCommandActionListener);
  runMenuItem.addActionListener(runCommandActionListener);
  continueMenuItem.addActionListener(continueCommandActionListener);
  continueToEndMenuItem.addActionListener(continueToEndCommandActionListener);
  setCompilingOptions.addActionListener(setCompilingOptionsCommandActionListener);
	setRunningOptions.addActionListener(setRunningOptionsCommandActionListener);
	stopMenuItem.addActionListener(stopCommandActionListener);
	selectDefaultStdinFile.addActionListener(selectStdinFileActionListener);
	selectDefaultStdoutFile.addActionListener(selectStdoutFileActionListener);
	eraseMem.addActionListener(eraseMemoryActionListener);
	setMemTo512.addActionListener(new SetMemSizeActionListener(9));
	setMemTo1024.addActionListener(new SetMemSizeActionListener(10));
	setMemTo2048.addActionListener(new SetMemSizeActionListener(11));
	setMemTo4096.addActionListener(new SetMemSizeActionListener(12));
	setMemTo8192.addActionListener(new SetMemSizeActionListener(13));
	setMemTo16384.addActionListener(new SetMemSizeActionListener(14));
	setMemTo32768.addActionListener(new SetMemSizeActionListener(15));
	setMemTo65536.addActionListener(new SetMemSizeActionListener(16));
	selectLanguageFromFile.addActionListener(selectLanguageFromFileActionListener);
	about.addActionListener(aboutActionListener);
	quit.addActionListener(quitActionListener);
	
  
}



/** This creates the toolbar of this program and returns it.
*/
private JToolBar makeToolBar() {
  
  JToolBar toolbar;
  
  toolbar = new JToolBar("Toolbar");
  
  System.out.println(getClass().getClassLoader().getResource(resourceHomeDir+"etc/Open24.gif"));
  
  openFileButton = new JButton();
  try {
    openFileButton.setIcon(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/Open24.gif"), "Open file")
    );
  }
  catch (Exception e) {
  }
  openFileButton.setToolTipText("Open a file");
  openFileButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(openFileButton);
  
  toolbar.addSeparator();
  
  compileButton = new JButton();
  try {  
    compileButton.setIcon(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/Compile24.gif"), "Compile")
    );
  }
  catch (Exception e) {
  }
  compileButton.setToolTipText("Compile the program");
  compileButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(compileButton);
  
  runButton = new JButton();
  try {  
    runButton.setIcon(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/Run24.gif"), "Run")
    );
  }
  catch (Exception e) {
  }
  runButton.setToolTipText("Run the program");
  runButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(runButton);
  
  continueButton = new JButton();
  try {  
    continueButton.setIcon(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/StepForward24.gif"), "Continue")
    );
  }
  catch (Exception e) {
  }
  continueButton.setToolTipText("Continue operation");
  continueButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(continueButton);
  
  continueToEndButton = new JButton();
  try {  
    continueToEndButton.setIcon(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/FastForward24.gif"), "Continue w/o pauses")
    );
  }
  catch (Exception e) {
  }
  continueToEndButton.setToolTipText("Continue operation without pauses");
  continueToEndButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(continueToEndButton);
  
  stopButton = new JButton();
  try {  
    stopButton.setIcon(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/Stop24.gif"), "Stop")
    );
  }
  catch (Exception e) {
  }
  stopButton.setToolTipText("Stop the current operation");
  stopButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(stopButton);
  
  toolbar.addSeparator();
  
  
  lineByLineToggleButton = new JToggleButton(); 
  try {  
    lineByLineToggleButton = new JToggleButton(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/RowInsertAfter24.gif"), "Run line by line")
    );
  }
  catch (Exception e) {
  }
  lineByLineToggleButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(lineByLineToggleButton);
  
  showCommentsToggleButton = new JToggleButton(); 
  try {  
    showCommentsToggleButton = new JToggleButton(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/History24.gif"), "Show comments")
    );
  }
  catch (Exception e) {
  }
  showCommentsToggleButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(showCommentsToggleButton);
   
  showAnimationToggleButton = new JToggleButton(); 
  try {  
    showAnimationToggleButton = new JToggleButton(
      new ImageIcon(getClass().getClassLoader().getResource(resourceHomeDir+"etc/Movie24.gif"),"Show comments")
    );
  }
  catch (Exception e) {
  }
  showAnimationToggleButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(showAnimationToggleButton);
  
  toolbar.setFloatable(false);
  
  openFileButton.addActionListener(openCommandActionListener);
  compileButton.addActionListener(compileCommandActionListener);
  runButton.addActionListener(runCommandActionListener);
  continueButton.addActionListener(continueCommandActionListener);
  continueToEndButton.addActionListener(continueToEndCommandActionListener);
  stopButton.addActionListener(stopCommandActionListener);
  lineByLineToggleButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      boolean b = ((JToggleButton)e.getSource()).isSelected();
      guibrain.menuSetRunningOption(GUIBrain.LINE_BY_LINE, b);
    }
  });
  showCommentsToggleButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      boolean b = ((JToggleButton)e.getSource()).isSelected();
      guibrain.menuSetRunningOption(GUIBrain.COMMENTED, b);
    }
  });
  showAnimationToggleButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      boolean b = ((JToggleButton)e.getSource()).isSelected();
      guibrain.menuSetRunningOption(GUIBrain.ANIMATED, b);
    }
  });
 
  return toolbar;
}



/* Next comes ActionListeners that are used with buttons and menuitems in this
   program. Their names are informative enough to tell which objects they are 
   intended to listen.
*/

private ActionListener openCommandActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	 
		int rv = openFileDialog.showOpenDialog(thisGUI);
		if (rv == JFileChooser.APPROVE_OPTION) {
		   new GUIThreader(GUIThreader.TASK_OPEN_FILE, guibrain, openFileDialog.getSelectedFile()).run();
		}
	} 
};

private ActionListener selectStdinFileActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						

		int rv = selectDefaultStdinFileDialog.showOpenDialog(thisGUI);
		if (rv == JFileChooser.APPROVE_OPTION) {
		  guibrain.menuSetStdin(selectDefaultStdinFileDialog.getSelectedFile());
		}
	} 
};

private ActionListener selectStdoutFileActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						
    
		int rv = selectDefaultStdoutFileDialog.showOpenDialog(thisGUI);
		if (rv == JFileChooser.APPROVE_OPTION) {
		  JOptionPane confirmDialog = new JOptionPane();
		  String[] param = { (String)UIManager.get("OptionPane.yesButtonText"), 
		                     (String)UIManager.get("OptionPane.noButtonText") };
  
		  int rv2 = confirmDialog.showConfirmDialog(
		    selectDefaultStdoutFileDialog,	    
		    new Message("Do you want to overwrite the file? Select {1} to append or {0} to overwrite.", param).toString(), 
		    new Message("Overwrite?").toString(),
		    JOptionPane.YES_NO_OPTION
		  );
		  if (rv2 == JOptionPane.YES_OPTION) {
		    guibrain.menuSetStdout(selectDefaultStdoutFileDialog.getSelectedFile(), false);
		  }
		  else if (rv2 == JOptionPane.NO_OPTION) {
		    guibrain.menuSetStdout(selectDefaultStdoutFileDialog.getSelectedFile(), true);
		  }
		}
	} 
};

private ActionListener compileCommandActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						
    new Thread(new GUIThreader(GUIThreader.TASK_COMPILE, guibrain)).start();
  }
};

private ActionListener runCommandActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						
    new Thread(new GUIThreader(GUIThreader.TASK_RUN, guibrain)).start();
  }
};

private ActionListener continueCommandActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {						
    new Thread(new GUIThreader(GUIThreader.TASK_CONTINUE, guibrain)).start();
  }
};


private ActionListener continueToEndCommandActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						

		guibrain.continueTaskWithoutPauses();
	} 
};

private ActionListener setRunningOptionsCommandActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						

		setRunningOptionsDialog.setVisible(true);
	} 
};

private ActionListener setCompilingOptionsCommandActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						

		setCompilingOptionsDialog.setVisible(true);
	} 
};

private ActionListener stopCommandActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {
    guibrain.menuInterrupt(false);
    //new Thread(new GUIThreader(GUIThreader.TASK_STOP, guibrain)).start();
  }
};

private ActionListener eraseMemoryActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {
    guibrain.menuEraseMemory();
  }
};

private ActionListener setLanguageActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {
    String language = ((JMenuItem)e.getSource()).getText();
    guibrain.menuSetLanguage(language);
  }
};

private ActionListener selectLanguageFromFileActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {
    //openFileDialog.resetChoosableFileFilters();
	  //openFileDialog.setAcceptAllFileFilterUsed(true);

		int rv = selectLanguageFileDialog.showOpenDialog(thisGUI);
		if (rv == JFileChooser.APPROVE_OPTION) {
		   guibrain.menuSetLanguage(selectLanguageFileDialog.getSelectedFile());
		}
	} 
};

private ActionListener aboutActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {
    aboutDialog.setVisible(true);
	} 
};

private ActionListener enterNumberButtonActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {						
    enterInput();
    
     
  }
};

private ActionListener quitActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {
    guibrain.menuExit();
    System.exit(0);
  }
};

/* Instances of this class will be created for Options -> Set Memory Size
   menu's menuitems.
*/
private class SetMemSizeActionListener implements ActionListener {
  private int memsize;
  public SetMemSizeActionListener(int memsize) {
    this.memsize = memsize;
  }
  public void actionPerformed(ActionEvent e) {
    guibrain.menuSetMemorySize(memsize);
  }
};
  
    


private FileFilter B91FileFilter = new FileFilter() {
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = GUIBrain.getExtension(f);
    if (extension != null) {
      if (extension.equals("b91")) {
        return true;
      } 
      else {
        return false;
      }
    }
    return false;
  }
  
  public String getDescription() {
    return new Message("B91 binary").toString();
  }
};


private FileFilter K91FileFilter = new FileFilter() {
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = GUIBrain.getExtension(f);
    if (extension != null) {
      if (extension.equals("k91")) {
        return true;
      } 
      else {
        return false;
      }
    }
    return false;
  }
  
  public String getDescription() {
    return new Message("K91 source").toString();
  }
};

private FileFilter classFileFilter = new FileFilter() {
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = GUIBrain.getExtension(f);
    if (extension != null) {
      if (extension.equals("class")) {
        return true;
      } 
      else {
        return false;
      }
    }
    return false;
  }
  
  public String getDescription() {
    return new Message("Class file").toString();
  }
};

} 
