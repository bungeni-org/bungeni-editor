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
import javax.swing.Timer;
import javax.swing.tree.DefaultTreeModel;
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author Administrator
 */
public class DocumentSectionAdapterTreeModel extends DefaultTreeModel {
   private Timer treeModelTimer ;  
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionAdapterTreeModel.class.getName());
 

    /**
     * Creates a new instance of DocumentSectionAdapterTreeModel
     */
    public DocumentSectionAdapterTreeModel(DocumentSectionAdapterTreeNode rootNode) {
      super(rootNode);
     //  this.rootNode = rootNode;
       
       treeModelTimer = new Timer(DocumentSectionProvider.TIMER_DELAY, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                setRoot(DocumentSectionAdapterTreeNode.create());
                
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
 
    public static DocumentSectionAdapterTreeModel create(){
        return new DocumentSectionAdapterTreeModel(new DocumentSectionAdapterTreeNode(DocumentSectionProvider.getTreeRoot()));
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
 }
