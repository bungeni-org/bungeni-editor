/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.xml.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;

/**
 * Description: ...
 *
 * Copyright (c) March 2001 Kyle Gabhart
 * @author Kyle Gabhart
 * @version 1.0
 */
public class BungeniXmlViewer2 extends JFrame implements ActionListener {
   private static org.apache.log4j.Logger log = Logger.getLogger(BungeniXmlViewer2.class.getName());

    private static BungeniXmlViewer2 xmlViewer = null;
    // This is the BungeniXmlTree object which displays the XML in a JTree
    private BungeniXmlTree BungeniXmlTree;
    // This is the textArea object that will display the raw XML text
    private JTextArea textArea;
    // One JScrollPane is the container for the JTree, the other is for the textArea
    private JScrollPane jScroll,  jScrollRt;
    // This JSplitPane is the container for the two JScrollPanes
    private JSplitPane splitPane;
    // This JButton handles the tree Refresh feature
    private JButton refreshButton;
    // This Listener allows the frame's close button to work properly
    private WindowListener winClosing;

    // These two constants set the width and height of the frame
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 450;

    public static BungeniXmlViewer2 getInstance(String title, ArrayList<String> xmlText) throws ParserConfigurationException {
        if (xmlViewer == null) {
            return new BungeniXmlViewer2(title, xmlText);
        } else {
            return xmlViewer.updateContent(title, xmlText);
        }
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
            BungeniXmlViewer2.getInstance(title, xmlText);
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

    private BungeniXmlViewer2 updateContent(String title, ArrayList<String> xmlText) {
        setTitle(title);
        textArea.setText((String) xmlText.get(0) + "\n");
        for (int i = 1; i < xmlText.size(); i++) {
            textArea.append((String) xmlText.get(i) + "\n");
        }
        BungeniXmlTree.refresh(textArea.getText());
        return this;
    }

    /**
     * This constructor passes the graphical construction off to the overloaded constructor
     * and then handles the processing of the XML text
     */
    public BungeniXmlViewer2(String title, ArrayList<String> xmlText) throws ParserConfigurationException {
        this(title);
        updateContent(title, xmlText);
    } //end BungeniXmlViewer( String title, String xml )

    /**
     * This constructor builds a frame containing a JSplitPane, which in turn contains two JScrollPanes.
     * One of the panes contains an BungeniXmlTree object and the other contains a JTextArea object.
     */
    public BungeniXmlViewer2(String title) throws ParserConfigurationException {
        // This builds the JFrame portion of the object
        super(title);

        Toolkit toolkit;
        Dimension dim, minimumSize;
        int screenHeight, screenWidth;

        // Initialize basic layout properties
        setBackground(Color.lightGray);
        getContentPane().setLayout(new BorderLayout());

        // Set the frame's display to be WIDTH x HEIGHT in the middle of the screen
        toolkit = Toolkit.getDefaultToolkit();
        dim = toolkit.getScreenSize();
        screenHeight = dim.height;
        screenWidth = dim.width;
        setBounds((screenWidth - FRAME_WIDTH) / 2, (screenHeight - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);

        // Create the refresh button object
        refreshButton = new JButton("Refresh");
        refreshButton.setBorder(BorderFactory.createRaisedBevelBorder());
        refreshButton.addActionListener(this);

        // Add the button to the frame
        getContentPane().add(refreshButton, BorderLayout.NORTH);

        // Create two JScrollPane objects
        jScroll = new JScrollPane();
        jScrollRt = new JScrollPane();

        // First, create the JTextArea:
        // Create the JTextArea and add it to the right hand JScroll
        textArea = new JTextArea(150, 150);
        jScrollRt.getViewport().add(textArea);

        // Next, create the BungeniXmlTree
        BungeniXmlTree = new BungeniXmlTree();
        BungeniXmlTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        BungeniXmlTree.setShowsRootHandles(true);

        // A more advanced version of this tool would allow the JTree to be editable
        BungeniXmlTree.setEditable(false);

        // Wrap the JTree in a JScroll so that we can scroll it in the JSplitPane.
        jScroll.getViewport().add(BungeniXmlTree);

        // Create the JSplitPane and add the two JScroll objects
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jScroll, jScrollRt);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);

        // Provide minimum sizes for the two components in the split pane
        minimumSize = new Dimension(200, 150);
        jScroll.setMinimumSize(minimumSize);
        jScrollRt.setMinimumSize(minimumSize);

        // Provide a preferred size for the split pane
        splitPane.setPreferredSize(new Dimension(400, 300));

        // Add the split pane to the frame
        getContentPane().add(splitPane, BorderLayout.CENTER);

        //Put the final touches to the JFrame object
        validate();
        setAlwaysOnTop(true);
        toFront();
        setVisible(true);

    // Add a WindowListener so that we can close the window
    //  winClosing = new WindowAdapter()
    //  {
    //     public void windowClosing(WindowEvent e)
    //     {
    //        exit();
    //     }
    //  };
    //  addWindowListener(winClosing);
    } //end BungeniXmlViewer()

    /**
     * When a user event occurs, this method is called.  If the action performed was a click
     * of the "Refresh" button, then the BungeniXmlTree object is updated using the current XML text
     * contained in the JTextArea
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Refresh")) {
            BungeniXmlTree.refresh(textArea.getText());
        }
    } //end actionPerformed()

    // Program execution begins here.  An XML file (*.xml) must be passed into the method
    public static void main(String[] args) {
        String fileName = "";
        BufferedReader reader;
        String line;
        ArrayList<String> xmlText = null;
        BungeniXmlViewer2 BungeniXmlViewer;

        // Build a Document object based on the specified XML file
        try {
            if (args.length > 0) {
                fileName = args[0];

                if (fileName.substring(fileName.indexOf('.')).equals(".xml")) {
                    reader = new BufferedReader(new FileReader(fileName));
                    xmlText = new ArrayList<String>();

                    while ((line = reader.readLine()) != null) {
                        xmlText.add(line);
                    } //end while ( ( line = reader.readLine() ) != null )

                    // The file will have to be re-read when the Document object is parsed
                    reader.close();

                    // Construct the GUI components and pass a reference to the XML root node
                    BungeniXmlViewer = new BungeniXmlViewer2("BungeniXmlViewer 1.0", xmlText);
                } else {
                    help();
                } //end if ( fileName.substring( fileName.indexOf( '.' ) ).equals( ".xml" ) )
            } else {
                BungeniXmlViewer = new BungeniXmlViewer2("BungeniXmlViewer 1.0");
            } //end if( args.length > 0 )
        } catch (FileNotFoundException fnfEx) {
            System.out.println(fileName + " was not found.");
            exit();
        } catch (Exception ex) {
            ex.printStackTrace();
            exit();
        }// end try/catch
    }// end main()

    // A common source of operating instructions
    private static void help() {
        System.out.println("\nUsage: java BungeniXmlViewer filename.xml");
        System.exit(0);
    } //end help()

    // A common point of exit
    private static void exit() {
        System.out.println("\nThank you for using BungeniXmlViewer 1.0");
        System.exit(0);
    } //end exit()
} //end class BungeniXmlViewer