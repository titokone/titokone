import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;



public class GUICompileSettingsDialog extends JDialog {
  
JButton applyButton, closeButton;
  
public GUICompileSettingsDialog(Frame ownerFrame, boolean modal) {
	
	super(ownerFrame, modal);
	setTitle("Set compiling options");
	setSize(250,180);
	
	applyButton = new JButton("Apply");
	closeButton = new JButton("Close");
	
	applyButton.addActionListener( new ActionListener() {
		public void actionPerformed( ActionEvent e ) {
			
		} 
	} );
	
	closeButton.addActionListener( new ActionListener() {
		public void actionPerformed( ActionEvent e ) {
			setVisible(false);
		} 
	} );
	
	
	//JRadioButton lineByLineRadioButton = new JRadioButton("Pause when a comment appears");
	//JRadioButton runAtCertainSpeedRadioButton = new JRadioButton("Execute at the specified speed");
	JLabel speedLabel= new JLabel("Execute at specified speed:");
	//JTextField speedTextField = new JTextField();
	JSpinner speedSpinner = new JSpinner( new SpinnerNumberModel(0,0,10,1) );
	JCheckBox showCommentsCheckBox = new JCheckBox("Show comments");
	JCheckBox pauseCheckBox = new JCheckBox("Pause whenever a comment appears");
  
	/*ButtonGroup bgroup = new ButtonGroup();
	bgroup.add(lineByLineRadioButton);
	bgroup.add(runAtCertainSpeedRadioButton);
	*/
	
	getContentPane().setLayout( new BorderLayout() );
	
	JPanel checkBoxPanel = new JPanel(new GridLayout(5,1));
	
	checkBoxPanel.add(speedLabel);
	//checkBoxPanel.add(runAtCertainSpeedRadioButton);
	checkBoxPanel.add(speedSpinner);
	checkBoxPanel.add(showCommentsCheckBox);
	checkBoxPanel.add(pauseCheckBox);
	
	JPanel buttonPanel = new JPanel(new FlowLayout());
	buttonPanel.add(applyButton);
	buttonPanel.add(closeButton);
	
	getContentPane().add(checkBoxPanel, BorderLayout.CENTER);
	getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	
	pack();
	
	setDefaultCloseOperation( DISPOSE_ON_CLOSE );
	
}

}