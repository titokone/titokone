package fi.hu.cs.titokone;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;



public class GUIRunSettingsDialog extends JDialog {
  
public JButton applyButton, closeButton;
public JCheckBox lineByLineCheckBox;
public JCheckBox showCommentsCheckBox;
public JCheckBox showAnimationCheckBox;

GUI ownerGUI;

public static String APPLY = "GUIRunSettingsDialog_Apply";

public GUIRunSettingsDialog(Frame ownerFrame, boolean modal) {
	
	super(ownerFrame, modal);
	
	ownerGUI = (GUI)ownerFrame;

	setTitle("Set running options");
	setSize(250,180);
	
	applyButton = new JButton("Apply");
	closeButton = new JButton("Close");
	
	applyButton.setActionCommand(APPLY);
	applyButton.addActionListener(ownerGUI);
	
	closeButton.addActionListener( new ActionListener() {
		public void actionPerformed( ActionEvent e ) {
			setVisible(false);
		} 
	} );
	
	
	lineByLineCheckBox = new JCheckBox("Execute line by line");
	showCommentsCheckBox = new JCheckBox("Show comments");
	showAnimationCheckBox = new JCheckBox("Show animation");
	
	
	getContentPane().setLayout( new BorderLayout() );
	
	JPanel checkBoxPanel = new JPanel(new GridLayout(3,1));
	
	checkBoxPanel.add(lineByLineCheckBox);
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


public void updateAllTexts() {
  setTitle(new Message("Set running options").toString());
  lineByLineCheckBox.setText(new Message("Execute code line by line").toString());
  showCommentsCheckBox.setText(new Message("Show extra comments while executing").toString());
  showAnimationCheckBox.setText(new Message("Show animation while executing").toString());
  applyButton.setText(new Message("Apply").toString());
  closeButton.setText(new Message("Close").toString());
  pack();
}

}