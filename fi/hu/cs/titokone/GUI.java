/* TODO:
    updateStatusLine(String str) updates the status line by replacing its contents with str
    
    updateReg(int reg, int value) updates reg with a new value
    
    disable(int)
    
    insertToCodeTable(int[], int[], String[])
    
    Lisäsin kentän HashMap symbolsHashMap
    
    Poistin metodin public void setInputFieldEnabled(boolean b)
*/

package fi.hu.cs.titokone;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

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
  
        GUIBrain guibrain;
        
        JSplitPane mainSplitPane;;
        
        JPanel leftPanel;
        JPanel  upperRightPanel;
        JPanel  lowerRightPanel;
        
        JTableX codeTable;
        Object[] codeTableIdentifiers = {""};
        
        JTableX instructionsTable;
        Object[] instructionsTableIdentifiers = {"Line", "Binary command", "Symbolic command"};
        JTableX dataTable;
        Object[] dataTableIdentifiers = {"Line", "Binary command", "Symbolic command"};
        JScrollPane codeTableScrollPane;
        JScrollPane instructionsTableScrollPane;
        JScrollPane dataTableScrollPane;
        JSplitPane dataAndInstructionsTableSplitPane;   
        
        JTableX registersTable;
        Object[] registersTableIdentifiers = {"", ""};
        JScrollPane registersTableScrollPane;
        
        JTableX symbolTable;
        Object[] symbolTableIdentifiers = {"", ""};
        JScrollPane symbolTableScrollPane;
        
        HashMap symbolsHashMap; // This has symbol name as key and its row in the symbol table as value
        
        JTextArea outputTextArea;
        JScrollPane outputScrollPane;
        JLabel enterNumberLabel;
        JTextField inputField;
        JButton enterNumberButton;
    
        
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
        
	      JMenu setLanguage;
	      
	      JMenu     helpMenu;
        JMenuItem manual;
	      JMenuItem about;
	
	      
	      JList commentList;
	      JScrollPane commentListScrollPane;
	      
	      String[][] codeTableContents;
	      
	      public static final short R0 = 0,
        	                        R1 = 1,
        	                        R2 = 2,
        	                        R3 = 3,
        	                        R4 = 4,
        	                        R5 = 5,   // r5 == sp
        	                        R6 = 6,   // r6 == fp
        	                        SP = 5,   // ^^
        	                        FP = 6;   // ^^

        public static final short COMPILE_COMMAND = 0,
                                  RUN_COMMAND = 1,
                                  STOP_COMMAND = 2,
                                  CONTINUE_COMMAND = 3,
                                  CONTINUE_WITHOUT_PAUSES_COMMAND = 4,
                                  INPUT_FIELD = 5;

	      
        private int activeView = 0;
        
        
        private JFileChooser openFileDialog;
        
        private GUIRunSettingsDialog setRunningOptionsDialog;
        private GUICompileSettingsDialog setCompilingOptionsDialog;
                
        private Font tableFont = new Font("Courier", java.awt.Font.PLAIN, 12);
                
        private Border blacklined = BorderFactory.createLineBorder(Color.black);
          
        
        private static final int COMMENT_LIST_SIZE = 100;


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
  
  setTitle("Titokone");
  openFileDialog = new JFileChooser();
  this.setRunningOptionsDialog = new GUIRunSettingsDialog(this, false);
  this.setCompilingOptionsDialog = new GUICompileSettingsDialog(this, false);
  
  symbolsHashMap = new HashMap();
          
  initGUI();
  guibrain = new GUIBrain(this);
  insertMenuBar(this);
  disable(GUI.COMPILE_COMMAND);
  disable(GUI.RUN_COMMAND);
  disable(GUI.CONTINUE_COMMAND);
  disable(GUI.CONTINUE_WITHOUT_PAUSES_COMMAND);
  disable(GUI.STOP_COMMAND);
  
  addWindowListener( new WindowAdapter () {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	);
	
	updateAllTexts();
  
	this.setVisible(true);
	this.pack(); 
}




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
  codeTableScrollPane = insertTableToScrollPane(codeTable);
  
  instructionsTable = new JTableX(new DefaultTableModel(instructionsTableIdentifiers,0));
  instructionsTable.setFont(tableFont);
  instructionsTable.setEnabled(false);
  instructionsTable.getColumnModel().getColumn(0).setMinWidth(20);
  instructionsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
  instructionsTableScrollPane = insertTableToScrollPane(instructionsTable);
  
  dataTable = new JTableX(new DefaultTableModel(dataTableIdentifiers,0));
  dataTable.setEnabled(false);
  dataTable.setFont(tableFont);
  dataTable.setRowSelectionAllowed(false);
  dataTableScrollPane = insertTableToScrollPane(dataTable);
  
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
      {"SP",""}, {"FP",""}, {"PC",""}, {"IR",""}};
  
  registersTable = new JTableX(new DefaultTableModel(regTableContents, registersTableIdentifiers));
  registersTable.setEnabled(false);
  registersTable.setFont(tableFont);
  registersTable.setRowSelectionAllowed(false);
  (registersTable.getColumnModel().getColumn(0)).setPreferredWidth(15);
  registersTableScrollPane = new JScrollPane(registersTable);
  registersTableScrollPane.setPreferredSize(new Dimension(150, 150));
  registersTableScrollPane.setBorder(BorderFactory.createTitledBorder(blacklined, "Registers"));
  
  JPanel ioPanel = new JPanel(new BorderLayout());
  JPanel inputPanel = new JPanel(new BorderLayout());
  JPanel outputPanel = new JPanel(new BorderLayout());
 
  outputTextArea = new JTextArea(1,7);
  outputTextArea.setLineWrap(true);
  outputTextArea.setWrapStyleWord(true);
  outputTextArea.setEditable(false);
  
  enterNumberLabel = new JLabel("");
  enterNumberLabel.setSize(new Dimension(100,100));
  
  inputField = new JTextField(11);
  
  enterNumberButton = new JButton("Enter");

  outputScrollPane = new JScrollPane(outputTextArea);
  outputScrollPane.setPreferredSize(new Dimension(30,300));
  outputPanel.add(outputScrollPane, BorderLayout.CENTER);
  
  inputPanel.add(enterNumberLabel, BorderLayout.NORTH);
  inputPanel.add(inputField, BorderLayout.CENTER);
  inputPanel.add(enterNumberButton, BorderLayout.SOUTH);
  
  ioPanel.add(outputPanel, BorderLayout.CENTER);
  ioPanel.add(inputPanel, BorderLayout.SOUTH);
  
  upperRightPanel = new JPanel(new BorderLayout());
  upperRightPanel.add(registersTableScrollPane, BorderLayout.WEST);
  upperRightPanel.add(symbolTableScrollPane, BorderLayout.CENTER);
  upperRightPanel.add(ioPanel, BorderLayout.EAST);
  
  commentListScrollPane = new JScrollPane(commentList);
  commentListScrollPane.setPreferredSize(new Dimension(1,50));
  
  JPanel southeastPanel = new JPanel(new BorderLayout());
  southeastPanel.add(commentListScrollPane, BorderLayout.CENTER);
  
  lowerRightPanel = new JPanel(new BorderLayout());
  lowerRightPanel.add(southeastPanel, BorderLayout.CENTER);
  
  JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperRightPanel, lowerRightPanel);
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
  enterNumberButton.addActionListener(enterNumberButtonActionListener);
  mainSplitPane.setDividerLocation(0.5);
}




public void setGUIView(int view) {
  
  if (activeView == view)  {
    return;
  }
  else {
    activeView = view;
  }
     
  leftPanel = new JPanel(new BorderLayout());
  
  if (view == 1) {
    mainSplitPane.setLeftComponent(leftPanel);
    validate();
    pack();
  }
  else if (view == 2) {
    
    leftPanel.add(codeTableScrollPane);
    mainSplitPane.setLeftComponent(leftPanel);
    validate();
    pack();
  }
  else if (view == 3) {
    leftPanel.add(dataAndInstructionsTableSplitPane);
    mainSplitPane.setLeftComponent(leftPanel);
    validate();
    pack();
    int dividerLocation;
    
    /* Set the location of divider between data and instructions table */
    dividerLocation = instructionsTable.getRowHeight() * instructionsTable.getRowCount();
    dividerLocation += dataAndInstructionsTableSplitPane.getDividerSize()*2;
    
    leftPanel.invalidate();
    System.out.println(leftPanel.getSize());
    if (dividerLocation > leftPanel.getHeight() / 2) {
      dividerLocation = leftPanel.getHeight() / 2;
    }
    
    dataAndInstructionsTableSplitPane.setDividerLocation(dividerLocation);
  }
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



/** This creates the toolbar of this program and returns it.
*/
private JToolBar makeToolBar() {
  
  JToolBar toolbar;
  JButton openFileButton;
 
  toolbar = new JToolBar("Toolbar");
  
  openFileButton = new JButton();
  openFileButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/general/open24.gif", "Open file"));
  openFileButton.setToolTipText("Open a file");
  openFileButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(openFileButton);
  
  toolbar.addSeparator();
  
  compileButton = new JButton();
  compileButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/media/compile24.gif", "Compile"));
  compileButton.setToolTipText("Compile the program");
  compileButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(compileButton);
  
  runButton = new JButton();
  runButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/media/run24.gif", "Run"));
  runButton.setToolTipText("Run the program");
  runButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(runButton);
  
  continueButton = new JButton();
  continueButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/media/StepForward24.gif", "Continue"));
  continueButton.setToolTipText("Continue operation");
  continueButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(continueButton);
  
  continueToEndButton = new JButton();
  continueToEndButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/media/FastForward24.gif", "Continue w/o pauses"));
  continueToEndButton.setToolTipText("Continue operation without pauses");
  continueToEndButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(continueToEndButton);
  
  stopButton = new JButton();
  stopButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/general/Stop24.gif", "Stop"));
  stopButton.setToolTipText("Stop the current operation");
  stopButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(stopButton);
  
  toolbar.addSeparator();
  
  lineByLineToggleButton = new JToggleButton(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/table/RowInsertAfter24.gif", "Run line by line"));
  lineByLineToggleButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(lineByLineToggleButton);
  
  showCommentsToggleButton = new JToggleButton(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/general/History24.gif", "Show comments"));
  showCommentsToggleButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(showCommentsToggleButton);
  
  showAnimationToggleButton = new JToggleButton(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/media/Movie24.gif", "Show comments"));
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




/** Inserts a text into status bar at the bottom of the screen.
    @param str The text to be inserted.
*/
public void updateStatusBar(String str) {
  statusBar.setText(str+" ");
  statusBar.validate();
}




/** Updates a register value.
    @param reg The register to be updated.
    @param newValue The new value.
*/
public void updateReg(int reg, int newValue) {
  DefaultTableModel registersTableModel = (DefaultTableModel)registersTable.getModel(); 
  registersTable.setValueAt(""+newValue, reg, 1);
}



/** Inserts data into code table. The data must be provided as a String-array, where
    one element corresponds to one line of original data. One elements will be shown
    as one row in GUI.
    @param src The data to be shown and one element corresponds to one line.
*/
public boolean insertToCodeTable(String[] src) {
  
  int rows = src.length;
  Object[][] tableContents = new Object[rows][1];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = src[i];
  }
    
  DefaultTableModel codeTableModel = (DefaultTableModel)codeTable.getModel(); 
  codeTableModel.setDataVector(tableContents, codeTableIdentifiers);
  codeTable.getColumnModel().getColumn(0).setPreferredWidth(codeTable.getMaxTextLengthInColumn(0));   
  return true;
}






public boolean insertToInstructionsTable(int[] lineNum, String[] binaryCommand, String[] symbolicCommand) {
  
  if (lineNum.length != binaryCommand.length || lineNum.length != symbolicCommand.length) {
    return false;
  }
  int rows = lineNum.length;
  DefaultTableModel instructionsTableModel = (DefaultTableModel)instructionsTable.getModel(); 
  Object[][] tableContents = new Object[rows][3];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = ""+lineNum[i];
    tableContents[i][1] = binaryCommand[i];
    tableContents[i][2] = symbolicCommand[i];
  }
  instructionsTableModel.setDataVector(tableContents, instructionsTableIdentifiers);
  return true;
}


public boolean insertToInstructionsTable(int[] lineNum, int[] binaryCommand, String[] symbolicCommand) {
  
  if (lineNum.length != binaryCommand.length || lineNum.length != symbolicCommand.length) {
    return false;
  }
  int rows = lineNum.length;
  DefaultTableModel instructionsTableModel = (DefaultTableModel)instructionsTable.getModel(); 
  Object[][] tableContents = new Object[rows][3];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = ""+lineNum[i];
    tableContents[i][1] = ""+binaryCommand[i];
    tableContents[i][2] = symbolicCommand[i];
  }
  instructionsTableModel.setDataVector(tableContents, instructionsTableIdentifiers);
  return true;
}



public boolean insertToInstructionsTable(int[] lineNum, String[] symbolicCommand) {
  String[] empty = new String[lineNum.length];
  return insertToInstructionsTable(lineNum, empty, symbolicCommand);
}







//public boolean insertToDataTable(int[] lineNum, int[] binaryCommand, String[] symbolicCommand) {
public boolean insertToDataTable(int[] lineNum, int[] data) {
  
  if (lineNum.length != data.length) {
    return false;
  }
  int rows = lineNum.length;
  DefaultTableModel dataTableModel = (DefaultTableModel)dataTable.getModel(); 
  Object[][] tableContents = new Object[rows][3];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = ""+lineNum[i];
    tableContents[i][1] = ""+data[i];
    tableContents[i][2] = "";
    //tableContents[i][2] = symbolicCommand[i];
  }
  dataTableModel.setDataVector(tableContents, dataTableIdentifiers);
  return true;
}



/** Inserts data into data table. Parameter lineNum is inserted into the first
    column and dataContents into the second. Note that the dimension of both
    parameters must be same. Otherwise false is returned and no update done.
    The third column will be empty.
    @param lineNum Contents of the first column, preferrably the line numbers.
    @param dataContents Contents of the second column.
    @return True if operation was successful.
*/
public boolean insertToDataTable(int[] lineNum, String[] dataContents) {
  if (lineNum.length != dataContents.length) {
    return false;
  }
  int rows = lineNum.length;
  DefaultTableModel dataTableModel = (DefaultTableModel)dataTable.getModel(); 
  Object[][] tableContents = new Object[rows][3];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = ""+lineNum[i];
    tableContents[i][1] = ""+dataContents[i];
    tableContents[i][2] = "";
  }
  dataTableModel.setDataVector(tableContents, dataTableIdentifiers);

  return true;
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
            Returns true if operation was successful.
*/
public boolean insertSymbolTable(String[][] symbolsAndValues) {
  
  /* These are declared just to make the code clearer. They are used to get
     symbol names and values from symbolsAndValues[][].
  */
  final int NAME = 0;
  final int VALUE = 1;
  
  symbolsHashMap.clear();
  DefaultTableModel symbolTableModel = (DefaultTableModel)symbolTable.getModel(); 
  
  
  if (symbolsAndValues == null) {
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
  DefaultListModel commentListModel = (DefaultListModel)commentList.getModel();
  int numberOfComponents = commentListModel.size();
  
  /* Comment list is not allowed to grow over COMMENT_LIST_SIZE */
  if (numberOfComponents == COMMENT_LIST_SIZE) {
    commentListModel.removeElementAt(numberOfComponents-1);
  }
  commentListModel.add(0, comment);
  commentListScrollPane.getViewport().setViewPosition(new Point(0,0));
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
/** Code table - used in selectRow() and setGUIView() methods */
public static final short CODE_TABLE = 2;
/** Instructions and data table - used in selectRow() and setGUIView() methods */
public static final short INSTRUCTIONS_AND_DATA_TABLE = 3;


/** Selects a row from code table or from instructions and data table. Instructions
    and data table, although two tables, are treated as one. Suppose that instructions
    table is n rows and data table is m rows. This is treated as one table of length
    n+m rows and its n first rows are same as instruction table's rows and m next
    rows are same as data table's rows.
    @param table The desired table. Proper values are CODE_TABLE and 
                 INSTRUCTIONS_AND_DATA_TABLE.
    @param row Row to be selected.
*/
public void selectRow(short table, int row) {
    
  if (row < 0) {
    return;
  }
  
  switch (table) {
    case CODE_TABLE:
      if (row > codeTable.getRowCount()) {
        return;
      }
      codeTable.selectRow(row);
      break;
    
    case INSTRUCTIONS_AND_DATA_TABLE:
      if ( row > (instructionsTable.getRowCount() + dataTable.getRowCount()) ) {
        return;
      }
      if (row < instructionsTable.getRowCount()) {
        instructionsTable.selectRow(row);
      }
      else if (row == instructionsTable.getRowCount()) {
        instructionsTable.unselectAllRows();
        dataTable.selectRow(row - instructionsTable.getRowCount());
      }
      else {
        dataTable.selectRow(row - instructionsTable.getRowCount());
      }
      break;
    default:
      break;
  }
  this.repaint();
}

    
    
private JScrollPane insertTableToScrollPane(JTable tbl) {
  
  JScrollPane tableScrollPane = new JScrollPane(tbl);
  tableScrollPane.setPreferredSize(new Dimension(200, 200));
  tableScrollPane.setMinimumSize(new Dimension(200, 60));
  
  return tableScrollPane;
}


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
}




/** Updates all texts that are shown in GUI to be shown in current language. If
    language hasn't changed since last call of this method, then nothing will
    happen, but if it has, then all the text will appear in it. GUIBrain calls
    this method in setLanguage() method.
*/
public void updateAllTexts() {
  openFile.setText( new Message("Open").toString() );
	compileMenuItem.setText( new Message("Compile").toString() );
  runMenuItem.setText( new Message("Run").toString() );
	continueMenuItem.setText( new Message("Continue").toString() );
	continueToEndMenuItem.setText( new Message("Continue without pauses").toString() );
	stopMenuItem.setText( new Message("Stop").toString() );
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
  
	
  compileButton.setToolTipText(new Message("Compile the opened file").toString());
  runButton.setToolTipText(new Message("Run the loaded program").toString());
  continueButton.setToolTipText(new Message("Continue current operation").toString());
  continueToEndButton.setToolTipText(new Message("Continue current operation without pauses").toString());
  stopButton.setToolTipText(new Message("Stop current operation").toString());

  ((TitledBorder)symbolTableScrollPane.getBorder()).setTitle( new Message("Symbol table").toString() );
  ((TitledBorder)registersTableScrollPane.getBorder()).setTitle( new Message("Registers").toString() );
  
}




private ActionListener openCommandActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	  
	  openFileDialog.setFileFilter(B91FileFilter);
	  openFileDialog.setFileFilter(K91FileFilter);
	  openFileDialog.setAcceptAllFileFilterUsed(false);

		int rv = openFileDialog.showOpenDialog(null);
		if (rv == JFileChooser.APPROVE_OPTION) {
		   new GUIThreader(GUIThreader.TASK_OPEN_FILE, guibrain, openFileDialog.getSelectedFile()).run();
		}
	} 
};

private ActionListener selectStdinFileActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						

		int rv = openFileDialog.showOpenDialog(null);
		if (rv == JFileChooser.APPROVE_OPTION) {
		  guibrain.menuSetStdin(openFileDialog.getSelectedFile());
		}
	} 
};

private ActionListener selectStdoutFileActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {						

		int rv = openFileDialog.showOpenDialog(null);
		if (rv == JFileChooser.APPROVE_OPTION) {
		  guibrain.menuSetStdout(openFileDialog.getSelectedFile());
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
    guibrain.menuInterrupt();
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

private ActionListener enterNumberButtonActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {						
  
    String input;
    input = inputField.getText();
    
  	if(guibrain.enterInput(input) == false) {  
  	  return;
  	}
  	
  	guibrain.continueTask();
     
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
    return "B91 binary";
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
    return "K91 source";
  }
};



public void changeTextInEnterNumberLabel(String newText) {
  enterNumberLabel.setText(newText);
}





}
