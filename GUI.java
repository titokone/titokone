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
  
  
        JPanel  upperRightPanel;
        JPanel  lowerRightPanel;
        
        JTableX codeTable;
        JTableX instructionsTable;
        JTableX dataTable;
        JScrollPane codeTableScrollPane;
        JScrollPane instructionsTableScrollPane;
        JScrollPane dataTableScrollPane;
        JSplitPane dataAndInstructionsTableSplitPane;   
        
        JTableX registersTable;
        JScrollPane registersTableScrollPane;
        
        JTableX symbolTable;
        
        JTextArea outputTextArea;
        JLabel enterNumberLabel;
        JTextField inputField;
        JButton enterNumberButton;
    
        
        JButton compileButton;
        JButton runButton;
        JButton continueButton;
        JButton continueToEndButton;
        JButton stopButton;
        
        JMenuItem compileMenuItem;
	      JMenuItem runMenuItem;
	      JMenuItem stopMenuItem;
	      
	      String[][] codeTableContents;
        
private JFileChooser openFileDialog;

private GUIRunSettingsDialog setRunningOptionsDialog;
private GUICompileSettingsDialog setCompileOptionsDialog;
        
private Font tableFont = new Font("Courier", java.awt.Font.PLAIN, 12);
        
private Border blacklined = BorderFactory.createLineBorder(Color.black);
  
       


/** This is called when ActionEvent of some kind is fired.
*/
public void actionPerformed(ActionEvent e) { }


public GUI() {
  
  setTitle("Kälidemo");
  openFileDialog = new JFileChooser();
  this.setRunningOptionsDialog = new GUIRunSettingsDialog(this, false);
  this.setCompileOptionsDialog = new GUICompileSettingsDialog(this, false);
  
  initGUI();
  
  addWindowListener( new WindowAdapter () {
		public void windowClosing(WindowEvent tapahtuma) {
			
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
       
  setGUIView(1);
  setGUIView(2);
  /*addRowToCodeTable("", "Jaahas");
  addRowToCodeTable("", "Jaahah56h4hu4u5nej5j53j65s");
  addRowToCodeTable("", "Jaaha321515555432523gewrs");
  addRowToCodeTable("", "Jaaha543gj429 tg95nt53hyu6h386uhu3985hg09hg53m9s");
  addRowToCodeTable("", "Jaahas");
  addRowToCodeTable("", "Jaaha543s");*/
  setGUIView(3);
  
  addRowToInstructionsTable("0","24372932","LOAD R1, =100");
  addRowToInstructionsTable("1","65463464","Alku: STORE R1, R2");
  
  //instructionsTable.selectRow(1,true);
}


private void setGUIView(int view) {
  
  JPanel leftPanel = new JPanel(new BorderLayout());
  JPanel upperRightPanel = new JPanel(new BorderLayout());
  JPanel lowerRightPanel = new JPanel(new BorderLayout());
     
  getContentPane().removeAll();
  
  if (view == 1) {
    // nothing is done 
  }
  else if (view == 2) {
    leftPanel.add(codeTableScrollPane);
  }
  else if (view == 3) {
    leftPanel.add(dataAndInstructionsTableSplitPane);
  }
  
  
    
    registersTableScrollPane = prepareRegistersTableScrollPane();
    
    upperRightPanel.add(registersTableScrollPane, BorderLayout.WEST);
    
    
    
    
    JPanel symbolTablePanel = new JPanel();
    //JTable symbolTable = new JTable(new SymbolTableModel());
    symbolTable = new JTableX(new DefaultTableModel(new String[] {"",""}, 0));
    
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
    
    JList commentList = new JList();
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
    
    
    JLabel statusBar = new JLabel("Status bar");
    
    statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    getContentPane().add(statusBar, BorderLayout.SOUTH);
    
    
    setInputFieldEnabled(false);
    
    if (view == 1) {
      compileMenuItem.setEnabled(false);
      runMenuItem.setEnabled(false);
      stopMenuItem.setEnabled(false);
      compileButton.setEnabled(false);
      runButton.setEnabled(false);
    }
    else if (view == 2) {
      compileMenuItem.setEnabled(true);
      runMenuItem.setEnabled(false);
      stopMenuItem.setEnabled(false);
      registersTable.setEnabled(false);
      compileButton.setEnabled(true);
      runButton.setEnabled(false);
    }
    else if (view == 3) {
      compileMenuItem.setEnabled(false);
      runMenuItem.setEnabled(true);
      stopMenuItem.setEnabled(false);
      compileButton.setEnabled(false);
      runButton.setEnabled(true);
    }
    
    continueButton.setEnabled(false);
    continueToEndButton.setEnabled(false);
    stopButton.setEnabled(false);
  
  
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
 
  String[] columnNames = {"",""};
  JTableX codeTable = new JTableX(new DefaultTableModel(columnNames,0));
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



public void addRowToCodeTable(String rowNum, String str) {
  
  DefaultTableModel tableModel = (DefaultTableModel)codeTable.getModel();
  String[] data = {""+rowNum, ""+str};
  tableModel.addRow(data);
    
  int nowAddedRowNumber = codeTable.getRowCount() - 1;
  int column0Width = codeTable.getColumnModel().getColumn(0).getWidth();
  int column1Width = codeTable.getColumnModel().getColumn(1).getWidth();
  
  if (column0Width < codeTable.getTextLength(nowAddedRowNumber, 0)) {
    codeTable.getColumnModel().getColumn(0).setPreferredWidth(codeTable.getTextLength(nowAddedRowNumber, 0));   
  }
  
  if (column1Width < codeTable.getTextLength(nowAddedRowNumber, 1)) {
    codeTable.getColumnModel().getColumn(1).setPreferredWidth(codeTable.getTextLength(nowAddedRowNumber, 1));
  }
  codeTable.getRootPane().validate();
}



public void addRowToInstructionsTable(String rowNum, String code, String symbolic) {
  
  DefaultTableModel tableModel = (DefaultTableModel)instructionsTable.getModel();
  String[] data = {""+rowNum, ""+code, ""+symbolic};
  tableModel.addRow(data);
    
  /*int nowAddedRowNumber = codeTable.getRowCount() - 1;
  int column0Width = codeTable.getColumnModel().getColumn(0).getWidth();
  int column1Width = codeTable.getColumnModel().getColumn(1).getWidth();
  
  if (column0Width < codeTable.getTextLength(nowAddedRowNumber, 0)) {
    codeTable.getColumnModel().getColumn(0).setPreferredWidth(codeTable.getTextLength(nowAddedRowNumber, 0));   
  }
  
  if (column1Width < codeTable.getTextLength(nowAddedRowNumber, 1)) {
    codeTable.getColumnModel().getColumn(1).setPreferredWidth(codeTable.getTextLength(nowAddedRowNumber, 1));
  }*/
  instructionsTable.getRootPane().validate();
} 



public void addRowToDataTable(String rowNum, String code, String symbolic) {
  
  DefaultTableModel tableModel = (DefaultTableModel)dataTable.getModel();
  String[] data = {""+rowNum, ""+code, ""+symbolic};
  tableModel.addRow(data);
    
  /*int nowAddedRowNumber = codeTable.getRowCount() - 1;
  int column0Width = codeTable.getColumnModel().getColumn(0).getWidth();
  int column1Width = codeTable.getColumnModel().getColumn(1).getWidth();
  
  if (column0Width < codeTable.getTextLength(nowAddedRowNumber, 0)) {
    codeTable.getColumnModel().getColumn(0).setPreferredWidth(codeTable.getTextLength(nowAddedRowNumber, 0));   
  }
  
  if (column1Width < codeTable.getTextLength(nowAddedRowNumber, 1)) {
    codeTable.getColumnModel().getColumn(1).setPreferredWidth(codeTable.getTextLength(nowAddedRowNumber, 1));
  }*/
  dataTable.getRootPane().validate();
} 

    

private JTableX prepareInstructionsTable() {
  
  //JTableX instructionsTable = new JTableX(new InstructionsTableModel());
  
  String[] columnNames = {"Line", "Code", "Symbolic instruction"};
  JTableX instructionsTable = new JTableX(new DefaultTableModel(columnNames,0));
  
  instructionsTable.setFont(tableFont);
  
  instructionsTable.setEnabled(false);
  instructionsTable.getColumnModel().getColumn(0).setMinWidth(20);
  instructionsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
   
  return instructionsTable; 
}



private JTableX prepareDataTable() {
 
  //JTable dataTable = new JTable(new DataTableModel());
  
  String[] columnNames = {"Line", "Code", "Symbolic instruction"};
  JTableX dataTable = new JTableX(new DefaultTableModel(columnNames,0));
  
  dataTable.setEnabled(false);
  dataTable.setFont(tableFont);
  dataTable.setRowSelectionAllowed(false);
  
  return dataTable;
}



private JScrollPane prepareRegistersTableScrollPane() {
  JPanel registersPanel = new JPanel();
    
  String[][] regTableContents = new String[][] 
    { {"R0",""}, {"R1",""}, {"R2",""}, {"R3",""}, {"R4",""}, 
      {"SP",""}, {"FP",""}, {"PC",""}, {"IR",""}};
  
  registersTable = new JTableX(new DefaultTableModel(regTableContents, new String[] {"",""}));
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
  		  System.out.println(openFileDialog.getSelectedFile().getName());
  		  System.out.println(openFileDialog.getSelectedFile().exists());
  		}
  	} } );

  setCompileOptions.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {						

			setCompileOptionsDialog.setVisible(true);
		} } );

  setRunningOptions.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {						

			setRunningOptionsDialog.setVisible(true);
		} } );

}



    


  

public static void main( String[] args ) {
  GUI gui = new GUI();
}
  
  
  
  

  
  
  
}
