/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import org.jdom.Element;

/**
 *
 * @author ashok
 */
public class OdfDomNode {

    /** the Element encapsulated by this node */
    public Element node;

    /**
     * Creates a new instance of the OdfDomNode class
     * @param Element node
     */
    public OdfDomNode(Element node) {
        this.node = node;
    }

    /**
     * Finds index of child in this node.
     *
     * @param child The child to look for
     * @return index of child, -1 if not present (error)
     */
    public int index(OdfDomNode child) {

        int count = childCount();
        for (int i = 0; i < count; i++) {
            OdfDomNode n = this.child(i);
            if (child.node == n.node) {
                return i;
            }
        }
        return -1; // Should never get here.
    }

    /**
     * Returns an adapter node given a valid index found through
     * the method: public int index(OdfDomNode child)
     *
     * @param searchIndex find this by calling index(OdfDomNode)
     * @return the desired child
     */
    public OdfDomNode child(int searchIndex) {
        Element child = (Element)node.getChildren().get(searchIndex);
        return new OdfDomNode(child);
    }

    /**
     * Return the number of children for this element/node
     *
     * @return int number of children
     */
    public int childCount() {
        return node.getChildren().size();
    }
}
