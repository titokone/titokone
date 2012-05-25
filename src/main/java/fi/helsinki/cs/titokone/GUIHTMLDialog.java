package fi.helsinki.cs.titokone;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


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
                        HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                        HTMLDocument doc = (HTMLDocument) pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent(evt);
                    } else {
                        try {
                            pane.setPage(e.getURL());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        });

        contentsScrollPane = new JScrollPane(contents);
        contentsScrollPane.setPreferredSize(new Dimension(400, 300));

        getContentPane().add(contentsScrollPane);
        pack();
    }

    public void updateAllTexts() {
        String newPath = new Message(pathToURL).toString();

        try {
            htmlURL = new URL(newPath);
        } catch (Exception e) {
            try {
                htmlURL = new URL(getClass().getClassLoader().getResource("doc").toString() + "/" + newPath);
            } catch (Exception e2) {
                //System.out.println(e);
            }
        }

        if (htmlURL != null) {
            try {
                contents.setPage(htmlURL);
            } catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + htmlURL);
            }
        } else {
            System.err.println("Requested URL was not found.");
        }

    }

}
