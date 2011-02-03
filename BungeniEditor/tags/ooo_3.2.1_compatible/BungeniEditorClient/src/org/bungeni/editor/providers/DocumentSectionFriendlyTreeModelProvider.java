/*
 * DocumentSectionTreeModelProvider.java
 *
 * Created on May 18, 2008, 1:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;

/**
 *
 * @author Administrator
 */
public class DocumentSectionFriendlyTreeModelProvider {
    
    /** Creates a new instance of DocumentSectionTreeModelProvider */
    public static DocumentSectionFriendlyAdapterDefaultTreeModel theSectionTreeModel = null;
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionFriendlyTreeModelProvider.class.getName());
           
    /**
     * returns a non-static instance of the section TreeModel which is updated directly by the documentsectionProvider
     */
     /*
    public static DocumentSectionFriendlyAdapterDefaultTreeModel create_without_subscription(){
        BungeniBNode bRootNode = DocumentSectionProvider.getTreeRoot();
        DefaultMutableTreeNode dmtRootNode = provideRootNode(bRootNode);
        DocumentSectionFriendlyAdapterDefaultTreeModel model = new DocumentSectionFriendlyAdapterDefaultTreeModel(dmtRootNode, false);
        return model;
    }*/
    
    public static DocumentSectionFriendlyAdapterDefaultTreeModel create(){
        DocumentSectionFriendlyAdapterDefaultTreeModel model = null;
        try {
            BungeniBNode bRootNode = DocumentSectionProvider.getNewFriendlyTree().getFirstRoot();
            if (bRootNode == null) log.error("create : bRootNode was null");
            DefaultMutableTreeNode dmtRootNode = provideRootNode(bRootNode);
            model = new DocumentSectionFriendlyAdapterDefaultTreeModel(dmtRootNode, false);
            DocumentSectionProvider.subscribeModel(model);
        }  catch (NullPointerException ex) {
          log.error("create : null pointer exception");
          log.error("create : "+ CommonExceptionUtils.getStackTrace(ex));
        } catch (Exception ex) {
            log.error("exception create() :" + ex.getMessage());
        } finally {
            return model;
        }
     
    }
    /*
    public static DocumentSectionFriendlyAdapterDefaultTreeModel create_static(){
       if (theSectionTreeModel == null ) {
            BungeniBNode bRootNode = DocumentSectionProvider.getTreeRoot();
            DefaultMutableTreeNode dmtRootNode = provideRootNode(bRootNode);
            //static model is always subscribed, so there is no internal timer required int the model
            theSectionTreeModel = new DocumentSectionFriendlyAdapterDefaultTreeModel(dmtRootNode, false);
            DocumentSectionProvider.subscribeModel(theSectionTreeModel);
            }
       return theSectionTreeModel;    
    }
    */

    /**
     * generates a newrootnode for the section model
     */
    public static DefaultMutableTreeNode newRootNode(){
        BungeniBNode bRootNode = DocumentSectionProvider.getTreeRoot();
        return provideRootNode(bRootNode);
    }
    
    /*
    private static String getSectionDisplayText(String sectionName, OOComponentHelper ooDoc){
        String retDisplayText = "";
        boolean bDispTextFound = false;
        log.debug("getSectionDisplayText : for "+ sectionName);
        XTextSection aSect = ooDoc.getSection(sectionName);
        String sectionText = aSect.getAnchor().getString();
        sectionText = sectionText.trim();
        String sectionType = ooDoc.getSectionType(aSect);
        if (sectionType != null ) {
            bDispTextFound = true;
            retDisplayText = sectionType + "-";
        }
        if (sectionText.length() > 0 ) {
            bDispTextFound = true;
            if (sectionText.length() > 15 ) 
                retDisplayText = retDisplayText + sectionText.substring(0, 14)+ "..";
            else
                retDisplayText = retDisplayText + sectionText+ "..";
        }
        if (!bDispTextFound ) {
            retDisplayText = sectionName;
        }
        return retDisplayText;
    }
    */
    
    private static DefaultMutableTreeNode provideRootNode(BungeniBNode rootNode) {
        //walk nodes and build tree
        DefaultMutableTreeNode theRootNode = null;
        try {
            theRootNode = new DefaultMutableTreeNode(rootNode);
            rootNode.setNodeObject(theRootNode);
//            DocumentSectionIterator docIterate = new DocumentSectionIterator(rootNode, new DefaultSectionIteratorListener());
//            docIterate.startIterator();
            recurseNodes(theRootNode);
            //return theRootNode;            
        } catch (Exception ex) {
           log.error("provideRootNode() : " + ex.getMessage()); 
        } finally {
        return theRootNode;
        }
    }
    
    
    private static boolean excludeNode(BungeniBNode aNode) {
        if (aNode.getName().startsWith("num_")) {
            return true;
        }
        return false;
    }
        private static void recurseNodes(DefaultMutableTreeNode theNode) {
        try {
        BungeniBNode theBNode = (BungeniBNode) theNode.getUserObject();
        if (excludeNode(theBNode)) { //ignore numbered sections
            return;
        }
        if (theBNode.hasChildren()) {
            TreeMap<Integer, BungeniBNode> children = theBNode.getChildrenByOrder();
            Iterator<Integer> childIterator = children.keySet().iterator();
            while (childIterator.hasNext()) {
                Integer nodeKey = childIterator.next();
                BungeniBNode childNode  = children.get(nodeKey);
                if (!excludeNode(childNode)) {
                    DefaultMutableTreeNode dmtChildNode = new DefaultMutableTreeNode( childNode);
                    childNode.setNodeObject(dmtChildNode);
                    recurseNodes(dmtChildNode);
                    theNode.add(dmtChildNode );
                }
            }
        }
        } catch (NullPointerException ex){
            log.error("recurseNodes : null pointer exception");
            log.error("recurseNodes:" + CommonExceptionUtils.getStackTrace(ex));
        }
    }
    /*
    private static void recurseNodes(DefaultMutableTreeNode theNode, OOComponentHelper ooDoc) {
        BungeniBNode theBNode = (BungeniBNode) theNode.getUserObject();
        if (theBNode.hasChildren()) {
            TreeMap<Integer, BungeniBNode> children = theBNode.getChildrenByOrder();
            Iterator<Integer> childIterator = children.keySet().iterator();
            while (childIterator.hasNext()) {
                Integer nodeKey = childIterator.next();
                BungeniBNode nodeChild  = children.get(nodeKey);
                nodeChild.setDisplayText(getSectionDisplayText(nodeChild.getName(), ooDoc));
                    DefaultMutableTreeNode dmtChildNode = new DefaultMutableTreeNode( nodeChild);
                    nodeChild.setNodeObject(dmtChildNode);
                recurseNodes(dmtChildNode, ooDoc);
                theNode.add(dmtChildNode );
            }
        }
    } */
}
