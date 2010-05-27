/*
 * BungeniToolbarXMLNodeAdapter.java
 *
 * Created on January 10, 2008, 10:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar;

import javax.swing.tree.DefaultMutableTreeNode;
import org.jdom.Element;

/**
 * In JDOM, an XML file is referred to as a Document. Every Document contains a root Element, 
 * which in turn contains other Elements. To make things easier, we will also create a 
 * "node adapter" as a kind of helper class for the model adapter. The node adapter will wrap 
 * a JDOM Element object, since this is the main class used to model data in JDOM.
 * @author Administrator
 */
public class BungeniToolbarXMLAdapterNode extends DefaultMutableTreeNode {
    public Element node;
        /** used for toString() */
    private final static String tab = "  ";
    private final static String lf = "\n";

    /**
     * Creates a new instance of the BungeniToolbarXMLAdapterNode class
     * @param Element node
     */
    public BungeniToolbarXMLAdapterNode(Element node) {
        this.node = node;
    }
    
    /**
     * Finds index of child in this node.
     * 
     * @param child The child we are looking for
     * @return index of child, -1 if not present (error)
     */
    public int index(BungeniToolbarXMLAdapterNode child) {
        int count = childCount();
        for (int i = 0; i < count; i++) {
            BungeniToolbarXMLAdapterNode n = this.child(i);
            if (child.node == n.node) {
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
    public BungeniToolbarXMLAdapterNode child(int searchIndex) {
        Element child = (Element)node.getChildren().get(searchIndex);
        return new BungeniToolbarXMLAdapterNode(child);
    }

    /**
     * Return the number of children for this element/node
     * 
     * @return int number of children
     */
    public int childCount() {
        return node.getChildren().size();
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
        org.jdom.Attribute nodeAttrib = node.getAttribute("title");
        if (nodeAttrib == null )
            return node.getName();
        else
            return (String) nodeAttrib.getValue();
    }
    
    /*
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(childCount() > 0) {
            sb.append(node.getName() + lf);
            for (int i = 0; i < childCount(); i++) {
                BungeniToolbarXMLAdapterNode child = child(i);
                sb.append(child.toString(1));
            }

        } else {
            sb.append(tab + node.getName() +"["+node.getTextTrim()+"]"+lf);
        }
        
        return sb.toString();
    }
    */
    /** used recursively to space the xml */
    public String toString(int r) {
        //tab to appropriate level
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < r; i++) {
            tabs.append(tab);
        }
        String space = tabs.toString();
        
        StringBuilder sb = new StringBuilder();
        if(childCount() > 0) {
            sb.append(space + node.getName() + lf);
            for (int i = 0; i < childCount(); i++) {
                BungeniToolbarXMLAdapterNode child = child(i);
                sb.append(child.toString(r+1));
            }

        } else {
            sb.append(space + node.getName() +"["+node.getTextTrim()+"]"+lf);
        }
        
        return sb.toString();
    }
    
}
