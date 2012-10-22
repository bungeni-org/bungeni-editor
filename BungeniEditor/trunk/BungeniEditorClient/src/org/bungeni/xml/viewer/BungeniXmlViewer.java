package org.bungeni.xml.viewer;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.bungeni.utils.BungeniFrame;
import org.bungeni.extutils.FrameLauncher;

/**
 * Description: ...
 *
 * Copyright (c) March 2001 Kyle Gabhart
 * @author Kyle Gabhart
 * @version 1.0
 */
public  class BungeniXmlViewer extends BungeniFrame {
   private static org.apache.log4j.Logger log = Logger.getLogger(BungeniXmlViewer.class.getName());

    private static BungeniXmlViewer xmlViewer = null;
    // This is the BungeniXmlTree object which displays the XML in a JTree
    XmlViewerPanel xmlPanel = null;

    public static synchronized BungeniXmlViewer getInstance(String title, ArrayList<String> xmlText) throws ParserConfigurationException {
        if (xmlViewer == null) {
            synchronized(BungeniXmlViewer.class) {
                if (xmlViewer == null) {
                    xmlViewer = new BungeniXmlViewer(title, xmlText);
                }
            }
           
        } else {
            xmlViewer.updateContent(title, xmlText);
            xmlViewer.setVisible(true);
        }
        return xmlViewer;
    }

    public static void launchXmlViewer(String title, File fFile) throws ParserConfigurationException {
        BufferedReader reader = null;
        try {
            // The file will have to be re-read when the Document object is parsed
            reader = new BufferedReader(new FileReader(fFile));
            ArrayList<String> xmlText = new ArrayList<String>();
            String line = "";
            while ((line = reader.readLine()) != null) {
                xmlText.add(line);
            } //end while ( ( line = reader.readLine() ) != null )
            // The file will have to be re-read when the Document object is parsed
            reader.close();
            BungeniXmlViewer frmViewer = BungeniXmlViewer.getInstance(title, xmlText);
            FrameLauncher.CenterFrame(frmViewer);
        } catch (IOException ex) {
            log.error("launchXmlViewer" , ex);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ex) {
                    log.error("launchXmlViewer" , ex);
            }
        }

    }

    private BungeniXmlViewer updateContent(String title, ArrayList<String> xmlText) {
        setTitle(title);
        xmlPanel.updateComponent(xmlText);
        return this;
    }

    /**
     * This constructor passes the graphical construction off to the overloaded constructor
     * and then handles the processing of the XML text
     */
    protected BungeniXmlViewer(String title, ArrayList<String> xmlText) throws ParserConfigurationException {
        this(title);
        xmlPanel = new XmlViewerPanel(this, xmlText);
        updateContent(title, xmlText);
        // Add the split pane to the frame
        getContentPane().add(xmlPanel, BorderLayout.CENTER);
        //Put the final touches to the JFrame object
        validate();
        setAlwaysOnTop(true);
        pack();
        toFront();
        setVisible(true);
    } //end BungeniXmlViewer( String title, String xml )

    /**
     * This constructor builds a frame containing a JSplitPane, which in turn contains two JScrollPanes.
     * One of the panes contains an BungeniXmlTree object and the other contains a JTextArea object.
     */
    protected BungeniXmlViewer(String title) {
        // This builds the JFrame portion of the object
        super(title);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    } //end BungeniXmlViewer()

} //end class BungeniXmlViewer