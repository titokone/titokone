package fi.hu.cs.titokone;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import java.net.URL;
import java.awt.Frame;
import java.io.IOException;


public class GUIAboutDialog extends JDialog {

  JEditorPane contents;
  JScrollPane contentsScrollPane;
  
  URL aboutURL;

public GUIAboutDialog(Frame ownerFrame, boolean modal) {
  super(ownerFrame, modal);
  
  setTitle("About");
  
  try {
    aboutURL = new URL(getClass().getClassLoader().getResource(GUI.resourceHomeDir).toString()+"resources/about.html");
  }
  catch (Exception e) {
    System.out.println(e);
  }
  contents = new JEditorPane();
  contents.setEditable(false);
  
  if (aboutURL != null) {
    try {
      contents.setPage(aboutURL);
    } catch (IOException e) {
      System.err.println("Attempted to read a bad URL: " + aboutURL);
    }
  } 
  else {
    System.err.println("Bad error!");
  }
  
  contentsScrollPane = new JScrollPane(contents);
  contentsScrollPane.setPreferredSize(new Dimension(400,300));
  
  getContentPane().add(contentsScrollPane);
  pack();
}

public void updateAllTexts() {
  setTitle(new Message("About").toString());
}

}