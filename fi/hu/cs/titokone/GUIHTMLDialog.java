package fi.hu.cs.titokone;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import java.net.URL;
import java.awt.Frame;
import java.io.IOException;


public class GUIHTMLDialog extends JDialog {

  JEditorPane contents;
  JScrollPane contentsScrollPane;
  
  String pathToURL;
  URL htmlURL;


       
public GUIHTMLDialog(Frame ownerFrame, boolean modal, String path) {
  super(ownerFrame, modal);
  
  

  
  pathToURL = path;
  
  contents = new JEditorPane();
  contents.setEditable(false);
  
  contents.addHyperlinkListener(new HyperlinkListener() {
    public void hyperlinkUpdate(HyperlinkEvent e) {
      if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        JEditorPane pane = (JEditorPane) e.getSource();
        if (e instanceof HTMLFrameHyperlinkEvent) {
          HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
          HTMLDocument doc = (HTMLDocument)pane.getDocument();
          doc.processHTMLFrameHyperlinkEvent(evt);
        } 
        else {
          try {
            pane.setPage(e.getURL());
          } 
          catch (Throwable t) {
            t.printStackTrace();
          }
        }
      }
    }
  });
  
  contentsScrollPane = new JScrollPane(contents);
  contentsScrollPane.setPreferredSize(new Dimension(400,300));
  
  getContentPane().add(contentsScrollPane);
  pack();
}

public void updateAllTexts() {
  String newPath = new Message(pathToURL).toString();
  
  try {
    htmlURL = new URL(newPath);
  }
  catch (Exception e) {
    try {
      htmlURL = new URL(getClass().getClassLoader().getResource("doc").toString()+"/"+newPath);
    }
    catch (Exception e2) {
      //System.out.println(e);
    }
  }
  
  if (htmlURL != null) {
    try {
      contents.setPage(htmlURL);
    } catch (IOException e) {
      System.err.println("Attempted to read a bad URL: " + htmlURL);
    }
  } 
  else {
    System.err.println("Requested URL was not found.");
  }
  
}

}
