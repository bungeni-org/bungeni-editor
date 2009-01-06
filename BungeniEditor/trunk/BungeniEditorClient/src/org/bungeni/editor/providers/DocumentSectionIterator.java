/*
 * DocumentSectionIterator.java
 *
 * Created on May 29, 2008, 3:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import java.util.Iterator;
import java.util.TreeMap;
import org.bungeni.utils.BungeniBNode;

/**
 * A recursive section iterator class that allows callbacks via an interface for every section found by the iterator. Makes use of the DocumentSectionProvider class, and the BungeniBNode tree classes to implement a callback interface via the IBungeniSectionIteratorListener class which has a single callback function
 * 
 * @author Administrator
 */
public  class DocumentSectionIterator {
    BungeniBNode rootNode;
    IBungeniSectionIteratorListener callback;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionIterator.class.getName());
         
    /** Creates a new instance of DocumentSectionIterator */
    public DocumentSectionIterator(IBungeniSectionIteratorListener callback) {
        this.rootNode = DocumentSectionProvider.getNewTree().getFirstRoot();
        this.callback = callback;
    }

    /** Creates a new instance of DocumentSectionIterator with custom root node*/
    public DocumentSectionIterator(BungeniBNode myRootNode, IBungeniSectionIteratorListener callback) {
        this.rootNode = myRootNode;
        this.callback = callback;
    }

    
    public void startIterator() {
        if (!callback.iteratorCallback(rootNode))
            return;
        recurseAllNodes(rootNode);
    }
    
    
    
    private void recurseAllNodes(BungeniBNode theBNode) {
       // BungeniBNode theBNode = (BungeniBNode) theNode.getUserObject();
        try {
            if (theBNode.hasChildren()) {
                log.debug("recurseAllNodes : iterating children = " + theBNode.getName());
                TreeMap<Integer, BungeniBNode> children = theBNode.getChildrenByOrder();
                Iterator<Integer> childIterator = children.keySet().iterator();
                while (childIterator.hasNext()) {
                    Integer nodeKey = childIterator.next();
                    BungeniBNode newBNode = children.get(nodeKey);
                    if (callback.iteratorCallback(newBNode) == false )
                        return;
                    recurseAllNodes(newBNode);
                }
            } else {
                log.debug("recurseAllNodes : "+ theBNode.getName() + " has no children ");
            }
        } catch (Exception ex) {
            log.error("recurseAllNodes : " + ex.getMessage());
        }
        }   
}
