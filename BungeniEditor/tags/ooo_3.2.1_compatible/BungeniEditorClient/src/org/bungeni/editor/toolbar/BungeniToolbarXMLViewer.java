/*
 * BungeniToolbarXMLViewer.java
 *
 * Created on January 10, 2008, 1:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar;

/**
 *Tester class for BungeniToolbar
 * @author Administrator
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;


/**
 * Simple gui to display an xml tree.
 */
public class BungeniToolbarXMLViewer extends JFrame {
    
    private final String title = "JDOM XML Tree";
    private final MenuBar menuBar = new MenuBar();
    private final Menu fileMenu = new Menu();
    private final MenuItem open = new MenuItem();
    private final JFileChooser fileChooser = new JFileChooser();
  
    private final BungeniToolbarXMLTree xmlTree;
    private File file;
    private JTree tree = new JTree();
    private Exception exception;
    
    private final int windowHeight = 600;
    private final int leftWidth = 380;
    private final int rightWidth = 600;
    private final int windowWidth = leftWidth + rightWidth;
    private final Font treeFont = new Font("Lucida Console", Font.BOLD, 14);
    private final Font textFont = new Font("Lucida Console", Font.PLAIN, 13);
    
    
    /**
     * Creates a simple gui for viewing xml in a tree.
     */
    public BungeniToolbarXMLViewer() {        
        
        setTitle(getClass().getSimpleName());
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setFocusable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);        
        
        xmlTree = new BungeniToolbarXMLTree(tree);
        try {
            
      //  fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            //fileChooser.setFileFilter(new XMLFileFilter());
      //  fileChooser.setCurrentDirectory(new File("E:/"));
            
            
       // fileMenu.setLabel("File");
            
       // open.setLabel("Browse");
       // open.addActionListener(new MyActionListener());
            xmlTree.loadToolbar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        display();
        //open.dispatchEvent(new ActionEvent(open,1001,open.getActionCommand()));
    }
    
    /**
     * Construct a frame of the most recently read-in document.
     */
    private void makeFrame() {
        
        getContentPane().removeAll();
                
        fileMenu.add(open);
        menuBar.add(fileMenu);
        setMenuBar(menuBar);
        
        pack();
        setVisible(true);
    }
    
    /**
     * Displays the tree.
     * 
     * @param tree JTree to display
     */
    public void display() {
        try {
            makeFrame();
            
            JScrollPane treeScrollPane = null;
            JScrollPane textScrollPane = null;
            
            // Build left-side view
            if(tree != null) {
                tree.setFont(treeFont);        
                treeScrollPane = new JScrollPane(tree);
                treeScrollPane.setPreferredSize(new Dimension(leftWidth, windowHeight));
            } else {
                JEditorPane errorMessagePane = new JEditorPane();
                errorMessagePane.setEditable(false);
                errorMessagePane.setContentType("text/plain");
                errorMessagePane.setText("Error: unable to build tree from xml:\n"+ exception.toString());
                errorMessagePane.setCaretPosition(0);
                treeScrollPane = new JScrollPane(errorMessagePane);
            }
            
            // Build right-side view
            if(file != null) {
                StringBuilder sb = new StringBuilder();
                
                //TODO show validation

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line = "";
                    while((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    reader.close();
                } catch (Exception e) {
                    System.err.println("exception when reading file for display");
                    e.printStackTrace();
                } 
                
                JEditorPane textPane = new JEditorPane();
                textPane.setEditable(false);
                textPane.setContentType("text/plain");
                textPane.setText(sb.toString());
                textPane.setCaretPosition(0);
                textPane.setFont(textFont);
                textScrollPane = new JScrollPane(textPane);
                textScrollPane.setPreferredSize(new Dimension(rightWidth, windowHeight));
            }
    
            // Build split-pane view
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    treeScrollPane, textScrollPane);
            
            splitPane.setContinuousLayout(true);
            splitPane.setDividerLocation(leftWidth);
            splitPane.setPreferredSize(new Dimension(windowWidth + 10,
                    windowHeight + 10));
    
            // Add GUI components
            setLayout(new BorderLayout());
            add("Center", splitPane);
            pack();
            setVisible(true);
        } catch (Exception e) {
            System.err.println("error when updating xml viewer");
            e.printStackTrace();
        }
    }
    
    /** listener for when user selects a file to view */
   
    private class MyActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == open) {

                int returnVal = fileChooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    //reset for currently selected message
                    exception = null;
                    
                    file = fileChooser.getSelectedFile();
                    
                    // update the gui for this file
                    setTitle(title + " | " + (file != null ? file.getAbsolutePath() : "Select A File"));
                    
                    // remember last directory used
                    fileChooser.setCurrentDirectory(file);
                    
                    try {
                    } catch (Exception e) {
                        exception = e;
                    }
                    //stree = xmlTree.getTree();
                    display();
                }
            } 
        } 
    }
    
    public static void main(String[] argv) {
        new BungeniToolbarXMLViewer();
    }
}

