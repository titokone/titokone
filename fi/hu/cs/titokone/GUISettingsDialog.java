package fi.hu.cs.titokone;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JDialog;
import javax.swing.JButton;
import java.awt.Frame;


public class GUISettingsDialog extends JDialog {

protected JButton applyButton, closeButton;

protected GUISettingsDialog(Frame ownerFrame, boolean modal) {
  super(ownerFrame, modal);
}


protected ChangeListener stateChangeListener = new ChangeListener() {
  public void stateChanged(ChangeEvent e) {
    applyButton.setEnabled(true);
    checkCorrespondance((javax.swing.JCheckBox) e.getSource());
  }
};


protected ComponentListener settingsDialogListener = new ComponentListener() {
  public void componentShown(ComponentEvent e) {
    applyButton.setEnabled(false);
  }
  
  public void componentHidden(ComponentEvent e) {}
  public void componentMoved(ComponentEvent e) {}
  public void componentResized(ComponentEvent e) {}
};


protected ActionListener applyButtonActionListener = new ActionListener() {
  public void actionPerformed(ActionEvent e) {
    applyButton.setEnabled(false);
  }
};


protected ActionListener closeButtonActionListener =new ActionListener() {
	public void actionPerformed( ActionEvent e ) {
		setVisible(false);
	} 
};

protected void checkCorrespondance(javax.swing.JCheckBox changee) {}

}
 
