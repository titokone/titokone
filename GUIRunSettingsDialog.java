import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;



public class GUIRunSettingsDialog extends JDialog {
  
JButton applyButton, closeButton;
  
public GUIRunSettingsDialog(Frame ownerFrame, boolean modal) {
	
	super(ownerFrame, modal);
	setTitle("Set running options");
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
	
	
	JRadioButton lineByLineRadioButton = new JRadioButton("Execute line by line");
	JRadioButton runAtCertainSpeedRadioButton = new JRadioButton("Execute at the specified speed");
	JTextField speedTextField = new JTextField();
	JCheckBox showCommentsCheckBox = new JCheckBox("Show comments");
	JCheckBox showAnimationCheckBox = new JCheckBox("Show animation");
	
	ButtonGroup bgroup = new ButtonGroup();
	bgroup.add(lineByLineRadioButton);
	bgroup.add(runAtCertainSpeedRadioButton);
	
	
	getContentPane().setLayout( new BorderLayout() );
	
	JPanel checkBoxPanel = new JPanel(new GridLayout(5,1));
	
	checkBoxPanel.add(lineByLineRadioButton);
	checkBoxPanel.add(runAtCertainSpeedRadioButton);
	checkBoxPanel.add(speedTextField);
	checkBoxPanel.add(showCommentsCheckBox);
	checkBoxPanel.add(showAnimationCheckBox);
	
	JPanel buttonPanel = new JPanel(new FlowLayout());
	buttonPanel.add(applyButton);
	buttonPanel.add(closeButton);
	
	getContentPane().add(checkBoxPanel, BorderLayout.CENTER);
	getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	
	pack();
	
	setDefaultCloseOperation( DISPOSE_ON_CLOSE );
	
}

}