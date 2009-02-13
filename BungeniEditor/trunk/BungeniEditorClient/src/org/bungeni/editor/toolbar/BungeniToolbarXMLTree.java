/*
 * BungeniToolbarXMLTree.java
 *
 * Created on January 10, 2008, 11:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.utils.BungeniEditorProperties;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Class to display an XML file in a JTree. Uses the JDOM (Java Document Object
 * Model) to back the XML.
 * 
 * @see http://java.sun.com/webservices/jaxp/dist/1.1/docs/tutorial/index.html
 */
public class BungeniToolbarXMLTree {
    private static Document document;
    private static SAXBuilder saxBuilder;
    private static boolean validate = false;
    private static BufferedReader reader;
    private static byte[] xml = new byte[] {};
    private String TOOLBAR_XML_FILE = "";
    //tree to be displayed
    private JTree theTree;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniToolbarXMLTree.class.getName());
 
    /** Creates a new instance of BungeniToolbarXMLTree */
    public BungeniToolbarXMLTree(JTree tree) {
        saxBuilder = new SAXBuilder(validate);
        String activeDocumentMode = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
        String toolbarquery = SettingsQueryFactory.Q_FETCH_TOOLBAR_CONFIG_FILE(activeDocumentMode);
        BungeniClientDB instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        instance.Connect();
        QueryResults qr = instance.QueryResults(toolbarquery);
        instance.EndConnect();
        String[] toolbarXml = qr.getSingleColumnResult("TOOLBAR_XML");
        String xmlConfigRelativePath =         toolbarXml[0];
        xmlConfigRelativePath = xmlConfigRelativePath.replace('/', File.separatorChar);
        this.TOOLBAR_XML_FILE = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + xmlConfigRelativePath;       
        this.theTree = tree;
    }

    /**
     * Returns the JTree with xml inside.
     * 
     * @return JTree is present, or null.
     */
    public void setTree(JTree tree) {
        theTree = tree;
    }

    private String getToolbarXMLFile(String pathToXmlFile){
        String strResult = null;
        try {
        FileReader fr;
        
        fr = new FileReader(pathToXmlFile);
        
        BufferedReader xmlReader = new BufferedReader(fr);
        String line = "";
        
        StringBuilder result = new StringBuilder();
        while ((line = xmlReader.readLine()) != null){
            result.append(line);
        }
        
        strResult = result.toString();
        
        } catch (FileNotFoundException ex) {
            log.error("getToolbarXMLFile: toolbar.xml not found in path : "+ ex.getMessage());
        } finally {
            return strResult;
        }
    }
      /**
     * Read in an XML file to display in the tree
     * 
     * @param xmlFile Path to an XML file.
     */
    public void loadToolbar() throws Exception {
        try {
            StringReader stringReader=new StringReader(getToolbarXMLFile(TOOLBAR_XML_FILE));
            document = saxBuilder.build(stringReader);
            //convert the document object into a JTree
            BungeniToolbarXMLModelAdapter model = new BungeniToolbarXMLModelAdapter(document);
            model.addTreeModelListener(new BungeniToolbarXMLModelAdapterListener() );
            theTree.setModel(model);
            //tree.setCellRenderer(new XMLTreeCellRenderer());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } catch (JDOMException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
    
    class BungeniToolbarXMLModelAdapterListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
             //   System.out.println("The node has changed");
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)e.getTreePath().getLastPathComponent();
            try {
                int nIndex = e.getChildIndices()[0];
                node= (DefaultMutableTreeNode)(node.getChildAt(nIndex));
            } catch (NullPointerException ex) {
              log.error("treeNodesChanged = "+ex.getMessage());  
            }
           
        }

        public void treeNodesInserted(TreeModelEvent e) {
        }

        public void treeNodesRemoved(TreeModelEvent e) {
        }

        public void treeStructureChanged(TreeModelEvent e) {
        }
        
    }
}
