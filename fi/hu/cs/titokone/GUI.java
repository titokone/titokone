/* TODO:
    updateStatusLine(String str) updates the status line by replacing its contents with str
    
    updateReg(int reg, int value) updates reg with a new value
    
    disable(int)
    
    insertToCodeTable(int[], int[], String[])
*/

package fi.hu.cs.titokone;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.table.*;


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
        
        JPanel leftPanel;
        JPanel  upperRightPanel;
        JPanel  lowerRightPanel;
        
        JTableX codeTable;
        Object[] codeTableIdentifiers = {"", ""};
        
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
        
        HashMap symbolsHashMap; // This has symbol name as key and its row in the symbol table as value
        
        JTextArea outputTextArea;
        JLabel enterNumberLabel;
        JTextField inputField;
        JButton enterNumberButton;
    
        
        JButton compileButton;
        JButton runButton;
        JButton continueButton;
        JButton continueToEndButton;
        JButton stopButton;
        
        JLabel statusBar;
        
        JMenuItem compileMenuItem;
	      JMenuItem runMenuItem;
	      JMenuItem continueMenuItem;
        JMenuItem continueToEndMenuItem;
        JMenuItem stopMenuItem;
	      
	      JList commentList;
	      
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
                                  CONTINUE_WITHOUT_PAUSES_COMMAND = 4;

	      
        
private JFileChooser openFileDialog;

private GUIRunSettingsDialog setRunningOptionsDialog;
private GUICompileSettingsDialog setCompileOptionsDialog;
        
private Font tableFont = new Font("Courier", java.awt.Font.PLAIN, 12);
        
private Border blacklined = BorderFactory.createLineBorder(Color.black);
  
       


/** This is called when ActionEvent of some kind is fired.
*/
public void actionPerformed(ActionEvent e) { }


public GUI() {
  
  setTitle("Titokone");
  openFileDialog = new JFileChooser();
  this.setRunningOptionsDialog = new GUIRunSettingsDialog(this, false);
  this.setCompileOptionsDialog = new GUICompileSettingsDialog(this, false);
  guibrain = new GUIBrain(this);
  symbolsHashMap = new HashMap();
          
  initGUI();
  
  addWindowListener( new WindowAdapter () {
		public void windowClosing(WindowEvent e) {
			
			System.exit(0);
		}
	}
	);
	
	addComponentListener( new ComponentAdapter () {
    public void componentResized(ComponentEvent e) {
      
    }
  }
  );
  
  
	this.setVisible(true);
	this.pack();
	 
}



/** This is just a private assistance method for the creator. This takes
    the responsibility to prepare GUI so that it looks right when the
    program starts. This won't be accessed but once in the creator method.
*/
private void initGUI() {
  
  insertMenuBar(this);
  
  
  codeTable = prepareCodeTable();
  codeTableScrollPane = insertTableToScrollPane(codeTable);
  
  instructionsTable = prepareInstructionsTable();
  instructionsTableScrollPane = insertTableToScrollPane(instructionsTable);
  
  dataTable = prepareDataTable();
  dataTableScrollPane = insertTableToScrollPane(dataTable);
  
  dataAndInstructionsTableSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, instructionsTableScrollPane, dataTableScrollPane);
  dataAndInstructionsTableSplitPane.setOneTouchExpandable(true);
  
  statusBar = new JLabel();
  commentList = new JList(new DefaultListModel());
  symbolTable = new JTableX(new DefaultTableModel(new String[] {"",""}, 0));
  registersTableScrollPane = prepareRegistersTableScrollPane();
        
         
  setGUIView(1);
  
}


public void setGUIView(int view) {
  
  
     
  getContentPane().removeAll();
  
  leftPanel = new JPanel(new BorderLayout());
  upperRightPanel = new JPanel(new BorderLayout());
  lowerRightPanel = new JPanel(new BorderLayout());
  
  if (view == 1) {
    // nothing is done 
  }
  else if (view == 2) {
    leftPanel.add(codeTableScrollPane);
  }
  else if (view == 3) {
    leftPanel.add(dataAndInstructionsTableSplitPane);
  }
  
  
    
    
    upperRightPanel.add(registersTableScrollPane, BorderLayout.WEST);
    
    
    
    
    JPanel symbolTablePanel = new JPanel();
  
    JScrollPane symbolTableScrollPane = new JScrollPane(symbolTable);
    
    symbolTableScrollPane.setBorder(BorderFactory.createTitledBorder(blacklined, "Symbol table"));
    symbolTableScrollPane.setMinimumSize(new Dimension(200, 150));
    
    symbolTable.setFont(tableFont);
    symbolTable.setRowSelectionAllowed(false);
    
    upperRightPanel.add(symbolTableScrollPane, BorderLayout.CENTER);
    
    
    
    JPanel ioPanel = new JPanel(new BorderLayout());
    JPanel inputPanel = new JPanel(new BorderLayout());
    JPanel outputPanel = new JPanel(new BorderLayout());
    
    
    outputTextArea = new JTextArea(1,7);
    enterNumberLabel = new JLabel("Enter a number");
    inputField = new JTextField(11);
    enterNumberButton = new JButton("Enter");
    
    outputTextArea.setLineWrap(true);
    outputTextArea.setWrapStyleWord(true);
    outputTextArea.setEditable(false);
    
    
    enterNumberLabel.setSize(new Dimension(100,100));
    JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
    outputScrollPane.setPreferredSize(new Dimension(30,300));
    outputPanel.add(outputScrollPane, BorderLayout.CENTER);
    
    inputPanel.add(enterNumberLabel, BorderLayout.NORTH);
    inputPanel.add(inputField, BorderLayout.CENTER);
    inputPanel.add(enterNumberButton, BorderLayout.SOUTH);
    
    ioPanel.add(outputPanel, BorderLayout.CENTER);
    ioPanel.add(inputPanel, BorderLayout.SOUTH);
    
    upperRightPanel.add(ioPanel, BorderLayout.EAST);
      
      
      
      
      
    JPanel southeastPanel = new JPanel(new BorderLayout());
    
    //String[] joo = {"KJ odottaa syötettä laitteelta KBD","...","...","...","...","...","...","...","...","...","...","jne"};
    
    JScrollPane commentListScrollPane = new JScrollPane(commentList);
    commentListScrollPane.setPreferredSize(new Dimension(1,50));
    
    
    southeastPanel.add(commentListScrollPane, BorderLayout.CENTER);
    
    lowerRightPanel.add(southeastPanel, BorderLayout.CENTER);
    
    
    
    getContentPane().setLayout( new BorderLayout() );
    
    JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperRightPanel, lowerRightPanel);
    rightSplitPane.setResizeWeight(0.5);
    
    JSplitPane mainSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplitPane);
    mainSplitter.setResizeWeight(0.5);
    
    getContentPane().add(mainSplitter, BorderLayout.CENTER);
    
    
    
    
    getContentPane().add( makeToolBar() , BorderLayout.NORTH);
    
    
    
    statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    getContentPane().add(statusBar, BorderLayout.SOUTH);
    
    
    setInputFieldEnabled(false);
    
    if (view == 1) {
      disable(COMPILE_COMMAND);
      disable(RUN_COMMAND);
      disable(CONTINUE_COMMAND);
      disable(CONTINUE_WITHOUT_PAUSES_COMMAND);
    }
    else if (view == 2) {
      enable(COMPILE_COMMAND);
      disable(RUN_COMMAND);
      disable(CONTINUE_COMMAND);
      disable(CONTINUE_WITHOUT_PAUSES_COMMAND);
    }
    else if (view == 3) {
      disable(COMPILE_COMMAND);
      enable(RUN_COMMAND);
      disable(CONTINUE_COMMAND);
      disable(CONTINUE_WITHOUT_PAUSES_COMMAND);
    }
    
    
  
    validate();
  
    pack();
  
    //dataAndInstructionsTableSplitPane.setDividerLocation(0.5);
    mainSplitter.setDividerLocation(0.5);
  
  
}



/** This sets the input field enabled or disabled. Input field is the field
    where kbd-inputs are entered.
*/
public void setInputFieldEnabled(boolean b) {
  enterNumberLabel.setEnabled(b);
  inputField.setEnabled(b);
  enterNumberButton.setEnabled(b);
}



/** This creates the toolbar and returns it.
*/
private JToolBar makeToolBar() {
  
  JToolBar toolbar;
  JButton openFileButton;
  JToggleButton lineByLineToggleButton;
  JToggleButton showCommentsToggleButton;
  JToggleButton showAnimationToggleButton;
  
  
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
  
  return toolbar;
}



private JTableX prepareCodeTable() {
 
  JTableX codeTable = new JTableX(new DefaultTableModel(codeTableIdentifiers ,0));
  codeTable.setFont(tableFont);
  codeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
  codeTable.setEnabled(false);
  codeTable.setShowGrid(false);
  codeTable.getColumnModel().getColumn(0).setMinWidth(0);
  codeTable.getColumnModel().getColumn(1).setMinWidth(0);
  codeTable.getColumnModel().getColumn(0).setPreferredWidth(0);
  codeTable.getColumnModel().getColumn(1).setPreferredWidth(0);
  
  return codeTable;
}


public void updateStatusBar(String str) {
  System.out.println(statusBar.getText());
  statusBar.setText(str);
  System.out.println(statusBar.getText());
  statusBar.validate();
}

public void updateReg(int reg, int newValue) {
  DefaultTableModel registersTableModel = (DefaultTableModel)registersTable.getModel(); 
  registersTable.setValueAt(""+newValue, reg, 1);
  registersTable.selectRow(reg);
}


public boolean insertToCodeTable(String[] src) {
  
  int rows = src.length;
  Object[][] tableContents = new Object[rows][2];
  for (int i=0 ; i<rows ; i++) {
    tableContents[i][0] = "";
    tableContents[i][1] = src[i];
  }
    
  DefaultTableModel codeTableModel = (DefaultTableModel)codeTable.getModel(); 
  codeTableModel.setDataVector(tableContents, codeTableIdentifiers);
  

  codeTable.getColumnModel().getColumn(0).setPreferredWidth(codeTable.getMaxTextLengthInColumn(0));   
  codeTable.getColumnModel().getColumn(1).setPreferredWidth(codeTable.getMaxTextLengthInColumn(1));   
  
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
  int[] empty = new int[lineNum.length];
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
    //tableContents[i][2] = symbolicCommand[i];
  }
  dataTableModel.setDataVector(tableContents, dataTableIdentifiers);
  return true;
}



public boolean insertToDataTable(int[] lineNum, String[] symbolicCommand) {
  int[] empty = new int[lineNum.length];
  //return insertToDataTable(lineNum, empty, symbolicCommand);
  return true;
}



public boolean insertSymbolTable(String[][] symbolsAndValues) {
  
  if (symbolsAndValues[0].length != 2) {
    return false;
  }
  
  int numOfSymbols = symbolsAndValues.length;
  
  DefaultTableModel symbolTableModel = (DefaultTableModel)symbolTable.getModel(); 
  Object[][] tableContents = new Object[numOfSymbols][2];
  for (int i=0 ; i<numOfSymbols ; i++) {
    tableContents[i][0] = symbolsAndValues[i][0];
    tableContents[i][1] = symbolsAndValues[i][1];
  }
  symbolTableModel.setDataVector(tableContents, symbolTableIdentifiers);
  return true;
}


public boolean updateRowInSymbolTable(String symbolName, int symbolValue) {
  Integer row = (Integer)symbolsHashMap.get(symbolName);
  DefaultTableModel symbolTableModel = (DefaultTableModel)symbolTable.getModel(); 
  if (row != null) {
    row = (Integer)symbolsHashMap.get(symbolName);
    symbolTableModel.setValueAt(""+symbolValue, row.intValue(), 1);
  
  }
  else {
    DefaultTableModel tableModel = (DefaultTableModel)symbolTable.getModel();
    String[] data = {symbolName, ""+symbolValue};
    tableModel.addRow(data);
  
    row = new Integer(symbolTable.getRowCount());
    symbolsHashMap.put(symbolName, row);
  }
  
  symbolTable.selectRow(row.intValue());
  return true;
}


public void addComment(String comment) {
  DefaultListModel commentListModel = (DefaultListModel)commentList.getModel();
  commentListModel.addElement(comment);
}


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
  }
}

      


public void enable(short command) {
  setEnabled(command, true);
}
  
public void disable(short command) {
  setEnabled(command, false);
}
  
  

private JTableX prepareInstructionsTable() {
  
  /* This instructionsTable_TEMP is just a temporary JTableX that is used for
     preparing the return value.
  */
  JTableX instructionsTable_TEMP = new JTableX(new DefaultTableModel(instructionsTableIdentifiers,0));
  
  instructionsTable_TEMP.setFont(tableFont);
  
  instructionsTable_TEMP.setEnabled(false);
  instructionsTable_TEMP.getColumnModel().getColumn(0).setMinWidth(20);
  instructionsTable_TEMP.getColumnModel().getColumn(0).setPreferredWidth(20);
   
  return instructionsTable_TEMP; 
}



private JTableX prepareDataTable() {
 
  JTableX dataTable_TEMP = new JTableX(new DefaultTableModel(dataTableIdentifiers,0));
  
  dataTable_TEMP.setEnabled(false);
  dataTable_TEMP.setFont(tableFont);
  dataTable_TEMP.setRowSelectionAllowed(false);
  
  return dataTable_TEMP;
}



private JScrollPane prepareRegistersTableScrollPane() {
  JPanel registersPanel = new JPanel();
    
  String[][] regTableContents = new String[][] 
    { {"R0",""}, {"R1",""}, {"R2",""}, {"R3",""}, {"R4",""}, 
      {"SP",""}, {"FP",""}, {"PC",""}, {"IR",""}};
  
  registersTable = new JTableX(new DefaultTableModel(regTableContents, registersTableIdentifiers));
  JScrollPane registersScrollPane = new JScrollPane(registersTable);
  
  registersScrollPane.setPreferredSize(new Dimension(150, 150));
 
  
  registersScrollPane.setBorder(BorderFactory.createTitledBorder(blacklined, "Registers"));
  
  registersTable.setEnabled(false);
  registersTable.setFont(tableFont);
  registersTable.setRowSelectionAllowed(false);
  (registersTable.getColumnModel().getColumn(0)).setPreferredWidth(15);
  
  return registersScrollPane;
}
 
 
    
private JScrollPane insertTableToScrollPane(JTable tbl) {
  
  JScrollPane tableScrollPane = new JScrollPane(tbl);
  tableScrollPane.setPreferredSize(new Dimension(200, 200));
  tableScrollPane.setMinimumSize(new Dimension(200, 60));
  
  return tableScrollPane;
}


private void insertMenuBar(JFrame destFrame) {

  JMenuBar  mainMenuBar = new JMenuBar();
	JMenu     fileMenu    = new JMenu("File");
	JMenuItem openFile    = fileMenu.add("Open");
	compileMenuItem       = fileMenu.add("Compile");
	runMenuItem           = fileMenu.add("Run");
	continueMenuItem      = fileMenu.add("Continue");
	continueToEndMenuItem = fileMenu.add("Continue without interruptions");
	stopMenuItem          = fileMenu.add("Stop");
	JMenuItem eraseMem    = fileMenu.add("Erase memory");
	JMenuItem quit        = fileMenu.add("Exit");


  JMenu     optionsMenu           = new JMenu("Options");
  
  JMenu setMemSize                = new JMenu("Set memory size");
  JMenuItem setMemTo512           = setMemSize.add("512");
  JMenuItem setMemTo1024          = setMemSize.add("1024");
  JMenuItem setMemTo2048          = setMemSize.add("2048");
  JMenuItem setMemTo4096          = setMemSize.add("4096");
  JMenuItem setMemTo8192          = setMemSize.add("8192");
  JMenuItem setMemTo16384         = setMemSize.add("16384");
  JMenuItem setMemTo32768         = setMemSize.add("32768");
  JMenuItem setMemTo65536         = setMemSize.add("65536");
  optionsMenu.add(setMemSize);
  
  JMenu configureFileSystem       = new JMenu("Configure file system");
  JMenuItem chooseSTDIN           = configureFileSystem.add("Set default stdin");
  JMenuItem chooseSTDOUT          = configureFileSystem.add("Set default stdout");
  optionsMenu.add(configureFileSystem);
  
  JMenuItem setCompileOptions     = optionsMenu.add("Set compiling options");
  JMenuItem setRunningOptions     = optionsMenu.add("Set running options");
  
  JMenuItem setLanguage           = optionsMenu.add("Set language");
  
  
  JMenu     helpMenu    = new JMenu("Help");
  JMenuItem manual      = helpMenu.add("Manual");
	JMenuItem about       = helpMenu.add("About");
	
	mainMenuBar.add(fileMenu);
	mainMenuBar.add(optionsMenu);
	mainMenuBar.add(helpMenu);
	destFrame.setJMenuBar(mainMenuBar);
	
	
	openFile.addActionListener( new ActionListener() {
  	public void actionPerformed(ActionEvent e) {						
  
  		int rv = openFileDialog.showOpenDialog(null);
  		if (rv == JFileChooser.APPROVE_OPTION) {
  		  //System.out.println(openFileDialog.getSelectedFile().getName());
  		  //System.out.println(openFileDialog.getSelectedFile().exists());
  		  new GUIThreader(GUIThreader.TASK_OPEN_FILE, guibrain, openFileDialog.getSelectedFile()).run();
  		}
  	} 
  });
  
  compileMenuItem.addActionListener( new ActionListener() {
  	public void actionPerformed(ActionEvent e) {						
      new GUIThreader(GUIThreader.TASK_COMPILE, guibrain).run();
    }
  });
  
  setCompileOptions.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {						

			setCompileOptionsDialog.setVisible(true);
		} 
  });

  setRunningOptions.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {						

			setRunningOptionsDialog.setVisible(true);
		} 
  });

}
}
