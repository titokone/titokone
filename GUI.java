import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;


public class GUI extends JFrame {
  
  
private JPanel  leftPanel, 
                upperRightPanel,
                lowerRightPanel;
        JTableX upperTable;
        JTable lowerTable;
  
        JScrollPane upperScrollPane;
        JScrollPane lowerScrollPane;
        JSplitPane splitPane;
        
        JFileChooser openFileDialog;
        GUIRunSettingsDialog setRunningOptionsDialog;


public GUI() {
  
  setTitle("Kälidemo");
  openFileDialog = new JFileChooser();
  this.setRunningOptionsDialog = new GUIRunSettingsDialog(this, false);
  
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




public void initGUI() {
  
  insertMenuBar();
  
  
  leftPanel = new JPanel(new BorderLayout());
  Font tableFont = new Font("Courier", java.awt.Font.PLAIN, 12);
  
  upperTable = new JTableX(new CodeTableModel());
  lowerTable = new JTable(new CodeTableModel());
  
  upperTable.setFont(tableFont);
  lowerTable.setFont(tableFont);
  //upperTable.setRowSelectionAllowed(false);
  lowerTable.setRowSelectionAllowed(false);
  
  
  upperScrollPane = new JScrollPane(upperTable);
  lowerScrollPane = new JScrollPane(lowerTable);
  upperScrollPane.setPreferredSize(new Dimension(200, 200));
  lowerScrollPane.setPreferredSize(new Dimension(200, 200));
  upperScrollPane.setMinimumSize(new Dimension(200, 60));
  lowerScrollPane.setMinimumSize(new Dimension(200, 60));
  
  System.out.println((upperTable.getColumnModel().getColumn(0)).getMaxWidth());
  
  upperTable.getColumnModel().getColumn(0).setMinWidth(20);
  upperTable.getColumnModel().getColumn(0).setPreferredWidth(20);
  //upperTable.getColumnModel().getColumn(1).setPreferredWidth(100);
   
  splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperScrollPane, lowerScrollPane);
  splitPane.setOneTouchExpandable(true);
  
  leftPanel.add(splitPane);
  
  
  
  
  
  upperRightPanel = new JPanel(new BorderLayout());
  lowerRightPanel = new JPanel(new BorderLayout());
  
  
  JPanel registersPanel = new JPanel();
  JTable registersTable = new JTable(new RegistersTableModel());
  JScrollPane registersScrollPane = new JScrollPane(registersTable);
  
  registersScrollPane.setPreferredSize(new Dimension(150, 150));
 
  Border blacklined = BorderFactory.createLineBorder(Color.black);
  
  registersScrollPane.setBorder(BorderFactory.createTitledBorder(blacklined, "Registers"));
  
  registersTable.setFont(tableFont);
  registersTable.setRowSelectionAllowed(false);
  (registersTable.getColumnModel().getColumn(0)).setPreferredWidth(15);
  
  
  upperRightPanel.add(registersScrollPane, BorderLayout.WEST);
  
  
  
  
  JPanel symbolTablePanel = new JPanel();
  JTable symbolTable = new JTable(new SymbolTableModel());
  JScrollPane symbolTableScrollPane = new JScrollPane(symbolTable);
  
  symbolTableScrollPane.setBorder(BorderFactory.createTitledBorder(blacklined, "Symbol table"));
  symbolTableScrollPane.setMinimumSize(new Dimension(200, 150));
  
  symbolTable.setFont(tableFont);
  symbolTable.setRowSelectionAllowed(false);
  
  upperRightPanel.add(symbolTableScrollPane, BorderLayout.CENTER);
  
  
  
  JPanel ioPanel = new JPanel(new BorderLayout());
  JPanel inputPanel = new JPanel(new BorderLayout());
  JPanel outputPanel = new JPanel(new BorderLayout());
  
  JTextArea outputTextArea = new JTextArea(1,7);
  JLabel enterNumberLabel = new JLabel("Enter a number");
  JTextField inputField = new JTextField(11);
  JButton enterNumberButton = new JButton("Enter");
  
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
  
  String[] joo = {"KJ odottaa syötettä laitteelta KBD","...","...","...","...","...","...","...","...","...","...","jne"};
  JList commentList = new JList(joo);
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
  
  
  JToolBar toolbar = new JToolBar("Toolbar");
  
  JButton openFileButton = new JButton();
  openFileButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/general/open24.gif", "Open file"));
  openFileButton.setToolTipText("Open a file");
  openFileButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(openFileButton);
  
  toolbar.addSeparator();
  
  JButton compileButton = new JButton();
  compileButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/media/compile24.gif", "Compile"));
  compileButton.setToolTipText("Compile the program");
  compileButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(compileButton);
  
  JButton runButton = new JButton();
  runButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/media/StepForward24.gif", "Advance one line"));
  runButton.setToolTipText("Run the next line of the program");
  runButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(runButton);
  
  JButton runToEndButton = new JButton();
  runToEndButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/media/FastForward24.gif", "Advance to end"));
  runToEndButton.setToolTipText("Run the rest of the program at once");
  runToEndButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(runToEndButton);
  
  JButton stopButton = new JButton();
  stopButton.setIcon(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/general/Stop24.gif", "Stop"));
  stopButton.setToolTipText("Stop the current operation");
  stopButton.setMargin(new Insets(0,0,0,0));
  toolbar.add(stopButton);
  
  toolbar.addSeparator();
  
  /*JToggleButton setRunSettingsToggleButton = new JToggleButton(new ImageIcon("jlfgr-1_0/toolbarButtonGraphics/general/properties24.gif", "Set running options"));
  toolbar.add(setRunSettingsToggleButton);
  */
  
  getContentPane().add(toolbar, BorderLayout.NORTH);
   
  validate();
  
  pack();
  
  splitPane.setDividerLocation(0.5);
  mainSplitter.setDividerLocation(0.5);
  
  upperTable.selectRow(1,true);
}



private void insertMenuBar() {
  JMenuBar  mainMenuBar = new JMenuBar();
	JMenu     fileMenu    = new JMenu("File");
	JMenuItem openFile    = fileMenu.add("Open");
	JMenuItem compile     = fileMenu.add("Compile");
	JMenuItem run         = fileMenu.add("Run");
	JMenuItem stop        = fileMenu.add("Stop");
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
  JMenuItem chooseSTDIN           = configureFileSystem.add("Choose stdin");
  JMenuItem chooseSTDOUT          = configureFileSystem.add("Choose stout");
  optionsMenu.add(configureFileSystem);
  
  JMenuItem setRunningOptions     = optionsMenu.add("Set running options");
  
  JMenuItem setLanguage           = optionsMenu.add("Set language");
  
  
  JMenu     helpMenu    = new JMenu("Help");
  JMenuItem manual      = helpMenu.add("Manual");
	JMenuItem about       = helpMenu.add("About");
	
	mainMenuBar.add(fileMenu);
	mainMenuBar.add(optionsMenu);
	mainMenuBar.add(helpMenu);
	setJMenuBar(mainMenuBar);
	
	
	openFile.addActionListener( new ActionListener() {
  	public void actionPerformed(ActionEvent e) {						
  
  		int rv = openFileDialog.showOpenDialog(null);
  		if (rv == JFileChooser.APPROVE_OPTION) {
  		  System.out.println(openFileDialog.getSelectedFile().getName());
  		  System.out.println(openFileDialog.getSelectedFile().exists());
  		}
  	} } );
  	
  setRunningOptions.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {						

			setRunningOptionsDialog.setVisible(true);
		} } );

}



class MyTableModel extends javax.swing.table.AbstractTableModel {
  
  protected String[] columnNames;
  protected Object[][] data;
  
  public int getColumnCount() {
      return columnNames.length;
  }

  public int getRowCount() {
      return data.length;
  }

  public String getColumnName(int col) {
      return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
      return data[row][col];
  }

  public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
  }

  public void setValueAt(Object value, int row, int col) {
      data[row][col] = value;
      fireTableCellUpdated(row, col);
  }
}



class CodeTableModel extends MyTableModel {
  protected String[] columnNames = {"Rivi", "Koodi", "Symbolinen käsky"};
  protected Object[][] data = {{"0", "12378912","     STORE R1,=100"},
                            {"1", "32131234","Alku: LOAD R2,=R1"},
                            };
  
  public int getColumnCount() {
      return columnNames.length;
  }

  public int getRowCount() {
      return data.length;
  }

  public String getColumnName(int col) {
      return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
      return data[row][col];
  }

  public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
  }

  public void setValueAt(Object value, int row, int col) {
      data[row][col] = value;
      fireTableCellUpdated(row, col);
  }                          
}



class SymbolTableModel extends MyTableModel {
  protected String[] columnNames = {"Symboli", "Arvo"};
  protected Object[][] data = {{"Sum", "12"},
                            {"KBD", "1"},
                            {"Alku","1"}
                            };
  public int getColumnCount() {
      return columnNames.length;
  }

  public int getRowCount() {
      return data.length;
  }

  public String getColumnName(int col) {
      return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
      return data[row][col];
  }

  public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
  }

  public void setValueAt(Object value, int row, int col) {
      data[row][col] = value;
      fireTableCellUpdated(row, col);
  }
} 




class RegistersTableModel extends MyTableModel {
  protected String[] columnNames = {"Register", "Value"};
  protected Object[][] data = {{"R0", "0"},
                            {"R2", "1"},
                            {"R3", "1"},
                            {"R4", "1"},
                            {"SP", "1"},
                            {"FP", "1"},
                            {"PC", "1"},
                            {"IR", "1"},
                            };
  public int getColumnCount() {
      return columnNames.length;
  }

  public int getRowCount() {
      return data.length;
  }

  public String getColumnName(int col) {
      return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
      return data[row][col];
  }

  public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
  }

  public void setValueAt(Object value, int row, int col) {
      data[row][col] = value;
      fireTableCellUpdated(row, col);
  }
}

  

public static void main( String[] args ) {
  GUI gui = new GUI();
}
  
  
  
  

  
  
  
}
