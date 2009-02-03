/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jdom.Document;

/**
 *
 * @author ashok
 */
public class OdfDomTreeModel  implements TreeModel {
   //JDOM Document to view as a tree
    private Document document;

    //listeners for changes, not used in this example
    private ArrayList listenerList = new ArrayList();
    public OdfDomTreeModel(Document doc) {
        document = doc;
    }

    //override from TreeModel
    public Object getRoot() {
        if(document == null) return null;
        return new OdfDomNode(document.getRootElement());
    }

    //override from TreeModel
    public Object getChild(Object parent, int index) {
        OdfDomNode node = (OdfDomNode) parent;
        return node.child(index);
    }

    //override from TreeModel
    public int getIndexOfChild(Object parent, Object child) {
        OdfDomNode node = (OdfDomNode) parent;
        return node.index((OdfDomNode) child);
    }

    //override from TreeModel
    public int getChildCount(Object parent) {
        OdfDomNode jdomNode = (OdfDomNode)parent;
        return jdomNode.childCount();
    }

    //override from TreeModel
    public boolean isLeaf(Object node) {
        OdfDomNode jdomNode = (OdfDomNode)node;
        return (jdomNode.node.getTextTrim().length() > 0);
    }

    //override from TreeModel
    public void valueForPathChanged(TreePath path, Object newValue) {
        // Null. We won't be making changes in the GUI
        // If we did, we would ensure the new value was really new,
        // adjust the model, and then fire a TreeNodesChanged event.
    }


    /*
     * Use these methods to add and remove event listeners.
     * (Needed to satisfy TreeModel interface, but not used.)
     */

    // override from TreeModel
	public void addTreeModelListener(TreeModelListener listener) {
		if (listener != null && !listenerList.contains(listener)) {
			listenerList.add(listener);
		}
	}
    // override from TreeModel
	public void removeTreeModelListener(TreeModelListener listener) {
		if (listener != null) {
			listenerList.remove(listener);
		}
	}

    /*
	 * Invoke these methods to inform listeners of changes. (Not needed for this
	 * example.) Methods taken from TreeModelSupport class described at
	 * http://java.sun.com/products/jfc/tsc/articles/jtree/index.html That
	 * architecture (produced by Tom Santos and Steve Wilson) is more elegant. I
	 * just hacked 'em in here so they are immediately at hand.
	 */
    public void fireTreeNodesChanged(TreeModelEvent e) {
		Iterator listeners = listenerList.iterator();
		while (listeners.hasNext()) {
			TreeModelListener listener = (TreeModelListener) listeners.next();
			listener.treeNodesChanged(e);
		}
	}
    public void fireTreeNodesInserted(TreeModelEvent e) {
		Iterator listeners = listenerList.iterator();
		while (listeners.hasNext()) {
			TreeModelListener listener = (TreeModelListener) listeners.next();
			listener.treeNodesInserted(e);
		}
	}
    public void fireTreeNodesRemoved(TreeModelEvent e) {
		Iterator listeners = listenerList.iterator();
		while (listeners.hasNext()) {
			TreeModelListener listener = (TreeModelListener) listeners.next();
			listener.treeNodesRemoved(e);
		}
	}
    public void fireTreeStructureChanged(TreeModelEvent e) {
		Iterator listeners = listenerList.iterator();
		while (listeners.hasNext()) {
			TreeModelListener listener = (TreeModelListener) listeners.next();
			listener.treeStructureChanged(e);
		}
	}

}
