/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.ui;

/**
 *
 * @author ashok
 */


import javax.swing.tree.DefaultMutableTreeNode;
import org.bungeni.editor.section.refactor.xml.OdfJDomElement;


/**
 * Wraps an XML element. Will return find children of this element if any.
 *
 * @see http://java.sun.com/webservices/jaxp/dist/1.1/docs/tutorial/index.html
 */
public class OdfJDomTreeNode extends DefaultMutableTreeNode{

    /** the Element encapsulated by this node */
    public OdfJDomElement node;

    /** used for toString() */
    private final static String tab = "  ";
    private final static String lf = "\n";

    /**
     * Creates a new instance of the JDOMAdapterNode class
     * @param Element node
     */
    public OdfJDomTreeNode(OdfJDomElement node) {
        this.node = node;
    }

    /**
     * Finds index of child in this node.
     *
     * @param child The child to look for
     * @return index of child, -1 if not present (error)
     */
    public int index(OdfJDomTreeNode child) {

        int count = childCount();
        for (int i = 0; i < count; i++) {
            OdfJDomTreeNode n = this.child(i);
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
    public OdfJDomTreeNode child(int searchIndex) {
        OdfJDomElement child = (OdfJDomElement)node.getChildren().get(searchIndex);
        return new OdfJDomTreeNode(child);
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
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        String secName = node.getAttribute(OdfJDomElement.NAME_ATTR).getValue();
        String typeName = node.getAttribute(OdfJDomElement.TYPE_ATTR).getValue();
        sb.append(typeName + "("+ secName + ")");

        /*
        if(childCount() > 0) {
            sb.append(node.getName() + lf);
            for (int i = 0; i < childCount(); i++) {
                OdfJDomTreeNode child = child(i);
                sb.append(child.toString(1));
            }

        } else {
            sb.append(tab + node.getName() +"["+node.getTextTrim()+"]"+lf);
        }
        */
        return sb.toString();
    }

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
                OdfJDomTreeNode child = child(i);
                sb.append(child.toString(r+1));
            }

        } else {
            sb.append(space + node.getName() +"["+node.getTextTrim()+"]"+lf);
        }

        return sb.toString();
    }
}