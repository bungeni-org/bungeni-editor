/*
 * DocumentSectionAdapterTreeModel.java
 *
 * Created on January 10, 2008, 10:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Timer;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.utils.BungeniBTree;


/**
 *
 * @author Administrator
 */
public class DocumentSectionAdapterTreeModel2 implements TreeModel {
   
    private boolean showAncestors ;
    private Timer treeModelTimer ;  
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionAdapterTreeModel2.class.getName());
    private Vector treeModelListeners = new Vector();
    private BungeniBNode rootElement;

    /**
     * Creates a new instance of DocumentSectionAdapterTreeModel
     */
    public DocumentSectionAdapterTreeModel2(BungeniBNode rootNode) {
     this.rootElement = rootNode;
     //  this.rootNode = rootNode;
       
       treeModelTimer = new Timer(DocumentSectionProvider.TIMER_DELAY, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                setRoot(DocumentSectionProvider.getTreeRoot());
                
                } catch (Exception ex) {
                    log.error("timer:actionperformed :" + ex.getMessage());
                    log.error("timer:actionperformed :" + CommonExceptionUtils.getStackTrace(ex));
                }
                //reload();
            }
       });
       treeModelTimer.setInitialDelay(1000);
       treeModelTimer.start();
    }
 
    public static DocumentSectionAdapterTreeModel2 create(){
        return new DocumentSectionAdapterTreeModel2(DocumentSectionProvider.getTreeRoot());
    }
    
    
    /*
    public Object getRoot() {
        return this.rootNode;
        
    }
*/
  /*  
    public void setRoot(DocumentSectionAdapterTreeNode root) {
       this.rootNode = root;
    }
   */
    /*
   public Object getChild(Object parent, int index) {
        DocumentSectionAdapterTreeNode node = (DocumentSectionAdapterTreeNode) parent;
         log.debug("getChild : " + node.child(index));
        return node.child(index);
   } */

    /*
   public int getIndexOfChild(Object parent, Object child) {
        DocumentSectionAdapterTreeNode node = (DocumentSectionAdapterTreeNode) parent;
          log.debug("getIndexofChild = " +  node.index((DocumentSectionAdapterTreeNode) child));
        return node.index((DocumentSectionAdapterTreeNode) child);
    }
   
    public int getChildCount(Object parent) {
        DocumentSectionAdapterTreeNode sectionNode = (DocumentSectionAdapterTreeNode)parent;
        log.debug("getChildCount = " + sectionNode.childCount());
        return sectionNode.childCount();
    }

   //override from TreeModel
    public boolean isLeaf(Object node) {
        DocumentSectionAdapterTreeNode sectionNode = (DocumentSectionAdapterTreeNode)node;
        return ((sectionNode.childCount() == 0) ? true: false);
    } */

    public Object getRoot() {
        return this.rootElement;
    }

    public void setRoot(BungeniBNode node) {
        //this.rootElement = node;
        BungeniBNode oldElement = this.rootElement;
    }
    public Object getChild(Object parent, int index) {
      BungeniBNode parentElement = (BungeniBNode) parent;
      return parentElement.getChildrenByOrder().get(index);
    }

    public int getChildCount(Object parent) {
        BungeniBNode parentElement = (BungeniBNode)parent;
        return parentElement.getChildCount();
    }

    public boolean isLeaf(Object node) {
        return !((BungeniBNode)node).hasChildren();
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
         System.out.println("*** valueForPathChanged : "
                + path + " --> " + newValue);
    }

    public int getIndexOfChild(Object parent, Object child) {
        BungeniBNode parentElement = (BungeniBNode)parent;
        BungeniBNode childElement = (BungeniBNode) child;
        Iterator<Integer> orderKeys= parentElement.getChildrenByOrder().keySet().iterator();
        while (orderKeys.hasNext()) {
            Integer iKey = orderKeys.next();
            BungeniBNode matchChild = parentElement.getChildrenByOrder().get(iKey);
            if (matchChild.equals(childElement)) {
                return iKey;
            }
        }
        return -1;
    }

    protected void fireTreeStructureChanged(BungeniBNode oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this,
                new Object[] {oldRoot});
                for (int i = 0; i < len; i++) {
                    ((TreeModelListener)treeModelListeners.elementAt(i)).
                            treeStructureChanged(e);
                }
    }
        
    public void addTreeModelListener(TreeModelListener l) {
       treeModelListeners.addElement(l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
          treeModelListeners.removeElement(l);
    }
 }
