/*
 * generalEditorTreeModel.java
 *
 * Created on August 18, 2007, 12:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.panels;

import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.bungeni.editor.actions.toolbarAction;

/**
 *
 * @author Administrator
 */
public class generalEditorTreeModel implements TreeModel {
    toolbarAction rootNode;
    private boolean showAncestors;
      private Vector<TreeModelListener> treeModelListeners =
        new Vector<TreeModelListener>();
      
    /** Creates a new instance of generalEditorTreeModel */
    public generalEditorTreeModel(toolbarAction root) {
        rootNode = root;
        showAncestors = true;
        
    }

    public void showAncestor(boolean b, Object newRoot) {
        showAncestors = b;
        toolbarAction oldRoot = rootNode;
        if (newRoot != null) {
           rootNode = (toolbarAction)newRoot;
        }
        fireTreeStructureChanged(oldRoot);
    }
 
 
  //////////////// Fire events //////////////////////////////////////////////

    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    protected void fireTreeStructureChanged(toolbarAction oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, 
                                              new Object[] {oldRoot});
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeStructureChanged(e);
        }
    }
    
    public Object getRoot() {
        return rootNode;
    }

    public Object getChild(Object parent, int index) {
        toolbarAction action = (toolbarAction) parent;
        return action.getContainedActionAt(index);
    }
    

    public int getChildCount(Object parent) {
        toolbarAction action = (toolbarAction) parent;
        return action.getContainedActionsCount();
    }

    public boolean isLeaf(Object node) {
        toolbarAction action = (toolbarAction) node;
        return action.getContainedActionsCount() == 0;

    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("*** valueForPathChanged : "
                           + path + " --> " + newValue);
    }

    public int getIndexOfChild(Object parent, Object child) {
            toolbarAction tnParent = (toolbarAction) parent;
            toolbarAction tnChild = (toolbarAction) child;
            return tnParent.getIndexOfContainedAction(tnChild);
    }

    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }
    
}
