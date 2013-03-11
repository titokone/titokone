// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import javax.swing.*;
import java.awt.*;


public class GUIRunSettingsDialog extends GUISettingsDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 914680842790217627L;
	public JCheckBox lineByLineCheckBox;
    public JCheckBox showCommentsCheckBox;
    public JCheckBox showAnimationCheckBox;

    GUI ownerGUI;

    public static String APPLY = "GUIRunSettingsDialog_Apply";

    public GUIRunSettingsDialog(Frame ownerFrame, boolean modal) {

        super(ownerFrame, modal);

        ownerGUI = (GUI) ownerFrame;

        setTitle("Set running options");
        setSize(250, 180);

        applyButton = new JButton("Apply");
        closeButton = new JButton("Close");

        applyButton.setActionCommand(APPLY);
        applyButton.addActionListener(ownerGUI);
        applyButton.addActionListener(applyButtonActionListener);

        closeButton.addActionListener(closeButtonActionListener);

        this.addComponentListener(settingsDialogListener);


        lineByLineCheckBox = new JCheckBox("Execute line by line");
        lineByLineCheckBox.addChangeListener(stateChangeListener);
        showCommentsCheckBox = new JCheckBox("Show comments");
        showCommentsCheckBox.addChangeListener(stateChangeListener);
        showAnimationCheckBox = new JCheckBox("Show animation");
        showAnimationCheckBox.addChangeListener(stateChangeListener);

        getContentPane().setLayout(new BorderLayout());

        JPanel checkBoxPanel = new JPanel(new GridLayout(3, 1));

        checkBoxPanel.add(lineByLineCheckBox);
        checkBoxPanel.add(showCommentsCheckBox);
        checkBoxPanel.add(showAnimationCheckBox);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(applyButton);
        buttonPanel.add(closeButton);

        getContentPane().add(checkBoxPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    protected void checkCorrespondance(JCheckBox changee) {
        if (changee.isSelected()) {// Something was selected: animator -> line-by-line?
            if (showAnimationCheckBox.isSelected() && !lineByLineCheckBox.isSelected()) {
                lineByLineCheckBox.setSelected(true);
            }
        } else // Something was unselected: !line-by-line -> !animator?
            if (showAnimationCheckBox.isSelected() && !lineByLineCheckBox.isSelected()) {
                showAnimationCheckBox.setSelected(false);
            }
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
