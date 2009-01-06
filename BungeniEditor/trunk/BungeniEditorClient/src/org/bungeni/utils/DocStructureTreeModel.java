/*
 * DocStructureTreeModel.java
 *
 * Created on July 5, 2007, 12:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

/**
 *
 * @author Administrator
 */


import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Vector;

public class DocStructureTreeModel  implements TreeModel {
    private DocStructureTreeNode rootNode;
 
    private boolean showAncestors;
    
    //listeners
      private Vector<TreeModelListener> treeModelListeners =
        new Vector<TreeModelListener>();
    
    
    /** Creates a new instance of DocStructureTreeModel */
    public DocStructureTreeModel(DocStructureTreeNode root) {
        showAncestors = true;
        rootNode = root;
    }

  public void showAncestor(boolean b, Object newRoot) {
        showAncestors = b;
        DocStructureTreeNode oldRoot = rootNode;
        if (newRoot != null) {
           rootNode = (DocStructureTreeNode)newRoot;
        }
        fireTreeStructureChanged(oldRoot);
    }
  //////////////// Fire events //////////////////////////////////////////////

    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    protected void fireTreeStructureChanged(DocStructureTreeNode oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, 
                                              new Object[] {oldRoot});
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeStructureChanged(e);
        }
    }
  
    
    //////////////// TreeModel interface implementation ///////////////////////

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.addElement(l);
    }
    
       /**
     * Removes a listener previously added with addTreeModelListener().
     */
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.removeElement(l);
    }

  
    public Object getRoot() {
        return rootNode;
    }

    public Object getChild(Object parent, int index) {
        DocStructureTreeNode tn = (DocStructureTreeNode) parent;
        /*
        if (showAncestors) {
            int count = 0;
            if (tn.getParent() != null) { 
                count++;
            }
            return count;
        }*/
        return tn.getChildAt(index);
    }

    public int getChildCount(Object parent) {
        DocStructureTreeNode tn = (DocStructureTreeNode)parent;
        /*
         if (showAncestors) {
            int count = 0;
            if (tn.getParent() != null) { 
                count++;
            }
           return count;
        }*/
        return tn.getChildCount();
    }

    public boolean isLeaf(Object node) {
        DocStructureTreeNode tn = (DocStructureTreeNode)node;
        
        return tn.getChildCount() == 0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
         System.out.println("*** valueForPathChanged : "
                           + path + " --> " + newValue);

    }

    public int getIndexOfChild(Object parent, Object child) {
        DocStructureTreeNode tnParent = (DocStructureTreeNode) parent;
        DocStructureTreeNode tnChild = (DocStructureTreeNode) child;
        /*
         if (showAncestors) {
            int count = 0;
            DocStructrueTreeNode pParent = tnParent.getParent();
            if (pParent != null) {
                count++;
                if (pParent == tnChild) {
                    return 0;
                }
            }
            }
            return -1;
        } */
        return tnParent.getIndexOfChild(tnChild);
    }

}
