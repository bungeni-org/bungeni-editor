/*
 * BungeniToolbarXMLNodeAdapter.java
 *
 * Created on January 10, 2008, 10:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;
import org.jdom.Element;

/**
 * In JDOM, an XML file is referred to as a Document. Every Document contains a root Element, 
 * which in turn contains other Elements. To make things easier, we will also create a 
 * "node adapter" as a kind of helper class for the model adapter. The node adapter will wrap 
 * a JDOM Element object, since this is the main class used to model data in JDOM.
 * @author Administrator
 */
public class DocumentSectionAdapterTreeNode extends DefaultMutableTreeNode {
   // public BungeniBNode node;
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionAdapterTreeNode.class.getName());
 
    /**
     * Creates a new instance of the DocumentSectionAdapterTreeNode class
     * 
     * @param Element node
     */
    public DocumentSectionAdapterTreeNode(BungeniBNode node) {
        if (node == null) {
            log.error("constructor node is null");
        }
        setUserObject(node);
    }
    
    public static DocumentSectionAdapterTreeNode create(){
       return new DocumentSectionAdapterTreeNode(DocumentSectionProvider.getTreeRoot());
    }
    
    public BungeniBNode getSectionNode() {
        return (BungeniBNode) getUserObject();
    }
    /**
     * Finds index of child in this node.
     * 
     * @param child The child we are looking for
     * @return index of child, -1 if not present (error)
     */
    public int index(DocumentSectionAdapterTreeNode child) {
        int count = childCount();
        for (int i = 0; i < count; i++) {
            DocumentSectionAdapterTreeNode n = this.child(i);
            if (child.getSectionNode() == n.getSectionNode()) {
                return i;
            }
        }
        return -1; // Should never get here.
    }

    /**
     * Returns an adapter node given a valid index found through
     * the method: public int index(JDOMAdapterNode child)
     * 
     * @param searchIndex find this by calling index(JDOMAdapterNode)
     * @return the desired child
     */
    public DocumentSectionAdapterTreeNode child(int searchIndex) {
        BungeniBNode child = null;
  
        try {
        child = getSectionNode().getChildrenByOrder().get(searchIndex);
  
        } catch (Exception ex) {
            log.error("child = " + ex.getMessage());
            log.error("child = " + CommonExceptionUtils.getStackTrace(ex));
        }
                     return new DocumentSectionAdapterTreeNode(child);
 
    }

    /**
     * Return the number of children for this element/node
     * 
     * @return int number of children
     */
    public int childCount() {
        try {
        log.debug("childCount = " + getSectionNode().getChildCount());
        } catch (Exception ex) {
            log.error("childCount = " + ex.getMessage());
            log.error("childCount = " + CommonExceptionUtils.getStackTrace(ex));
        }
        return getSectionNode().getChildCount();
    }

  
    
/**
     * Tricky toString which allows for copying entire elements and their children
     * from the xml viewer.
     * 
     * @return String
     */
   // @Override
    @Override
    public String toString(){
       return getSectionNode().getName();
    }
    
    /*
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(childCount() > 0) {
            sb.append(node.getName() + lf);
            for (int i = 0; i < childCount(); i++) {
                DocumentSectionAdapterTreeNode child = child(i);
                sb.append(child.toString(1));
            }

        } else {
            sb.append(tab + node.getName() +"["+node.getTextTrim()+"]"+lf);
        }
        
        return sb.toString();
    }
    */

    
}
