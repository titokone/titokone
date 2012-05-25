// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;


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

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
        }
    };


    protected ActionListener applyButtonActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            applyButton.setEnabled(false);
        }
    };


    protected ActionListener closeButtonActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    };

    protected void checkCorrespondance(javax.swing.JCheckBox changee) {
    }

}
 
