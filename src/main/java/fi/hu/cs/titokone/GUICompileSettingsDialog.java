package fi.hu.cs.titokone;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;




public class GUICompileSettingsDialog extends GUISettingsDialog {
  
public JCheckBox lineByLineCheckBox;
public JCheckBox showCommentsCheckBox;
	  
public static String APPLY = "GUICompileSettingsDialog_Apply";


 
public GUICompileSettingsDialog(Frame ownerFrame, boolean modal) {
	
	super(ownerFrame, modal);
	setTitle("Set compiling options");
	//setSize(250,180);
	
	applyButton = new JButton("Apply");
	closeButton = new JButton("Close");
	
	applyButton.setActionCommand(APPLY);
	applyButton.addActionListener((GUI)ownerFrame);
	applyButton.addActionListener(applyButtonActionListener);
	 
	closeButton.addActionListener(closeButtonActionListener);
	
	this.addComponentListener(settingsDialogListener);
	
	lineByLineCheckBox = new JCheckBox("Compile line by line");
	lineByLineCheckBox.addChangeListener( stateChangeListener );
	showCommentsCheckBox = new JCheckBox("Show comments");
	showCommentsCheckBox.addChangeListener( stateChangeListener );
	
	getContentPane().setLayout( new BorderLayout() );
	
	JPanel checkBoxPanel = new JPanel(new GridLayout(2,1));
	
	checkBoxPanel.add(lineByLineCheckBox);
	checkBoxPanel.add(showCommentsCheckBox);
	
	JPanel buttonPanel = new JPanel(new FlowLayout());
	buttonPanel.add(applyButton);
	buttonPanel.add(closeButton);
	
	getContentPane().add(checkBoxPanel, BorderLayout.CENTER);
	getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	
	pack();
	
	setDefaultCloseOperation( DISPOSE_ON_CLOSE );
	
}


public void updateAllTexts() {
  setTitle(new Message("Set compiling options").toString());
  lineByLineCheckBox.setText(new Message("Pause whenever a comment occurs").toString());
  showCommentsCheckBox.setText(new Message("Show extra comments while compiling").toString());
  applyButton.setText(new Message("Apply").toString());
  closeButton.setText(new Message("Close").toString());
  pack();
}




}

