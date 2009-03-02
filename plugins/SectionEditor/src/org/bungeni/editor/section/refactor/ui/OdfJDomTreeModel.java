/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.ui;

/**
 *
 * @author ashok
 */

import org.bungeni.editor.section.refactor.xml.OdfJDomElement;

import javax.swing.tree.DefaultTreeModel;

import org.jdom.Document;

/**
 * Converts a JDOM document into a TreeModel. Can be used for viewing XML
 * documents in a JTree.
 *
 * @see http://java.sun.com/webservices/jaxp/dist/1.1/docs/tutorial/index.html
 */
public class OdfJDomTreeModel extends DefaultTreeModel {

    //JDOM Document to view as a tree
    private Document document;

    //constructor used to set the document to view
    //public OdfJDomTreeModel(Document doc) {
    ///    document = doc;
    //}

    public OdfJDomTreeModel(OdfJDomTreeNode node) {
        super(node);
        document = node.node.getDocument();
    }
    //override from TreeModel
    @Override
    public Object getRoot() {
        if(document == null) return null;
        return new OdfJDomTreeNode((OdfJDomElement)document.getRootElement());
    }

    
    //override from TreeModel
    @Override
    public Object getChild(Object parent, int index) {
        OdfJDomTreeNode node = (OdfJDomTreeNode) parent;
        return node.child(index);
    }

    //override from TreeModel
    public int getIndexOfChild(Object parent, Object child) {
        OdfJDomTreeNode node = (OdfJDomTreeNode) parent;
        return node.index((OdfJDomTreeNode) child);
    }

    //override from TreeModel
    @Override
    public int getChildCount(Object parent) {
        OdfJDomTreeNode jdomNode = (OdfJDomTreeNode)parent;
        return jdomNode.childCount();
    }

    //override from TreeModel
    public boolean isLeaf(Object node) {
        OdfJDomTreeNode jdomNode = (OdfJDomTreeNode)node;
        return (jdomNode.node.getTextTrim().length() > 0);
    }

 
    /*
     * Use these methods to add and remove event listeners.
     * (Needed to satisfy TreeModel interface, but not used.)
     */



}

