/*
 * DocumentSectionAdapterDefaultTreeModel.java
 *
 * Created on May 18, 2008, 1:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.utils.compare.BungeniTreeRefactorTree;

/**
 *
 * @author Administrator
 */
public class DocumentSectionAdapterDefaultTreeModel extends DefaultTreeModel implements IRefreshableSectionTreeModel{
    Timer treeModelTimer ;
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionAdapterDefaultTreeModel.class.getName());
   
    /** Creates a new instance of DocumentSectionAdapterDefaultTreeModel */
    public DocumentSectionAdapterDefaultTreeModel(DefaultMutableTreeNode root ) {
        super(root);
          treeModelTimer = new Timer(DocumentSectionProvider.TIMER_DELAY, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                setRoot(DocumentSectionTreeModelProvider.newRootNode());
                
                } catch (Exception ex) {
                    log.error("timer:actionperformed :" + ex.getMessage());
                    log.error("timer:actionperformed :" + CommonExceptionUtils.getStackTrace(ex));
                }
            }
       });
       treeModelTimer.setInitialDelay(1000);
       treeModelTimer.start();
    }
    
    public DocumentSectionAdapterDefaultTreeModel(DefaultMutableTreeNode root , boolean bTimer ) {
        super(root);
        if (bTimer) {
          treeModelTimer = new Timer(DocumentSectionProvider.TIMER_DELAY, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                setRoot(DocumentSectionTreeModelProvider.newRootNode());
                
                } catch (Exception ex) {
                    log.error("timer:actionperformed :" + ex.getMessage());
                    log.error("timer:actionperformed :" + CommonExceptionUtils.getStackTrace(ex));
                }
            }
       });
       treeModelTimer.setInitialDelay(1000);
       treeModelTimer.start();
        }
    }

    public void newRootNode() {
       //setRoot(DocumentSectionTreeModelProvider.newRootNode());
    }

    public void updateTreeModel(BungeniBNode refreshNode) {
       log.debug("updateTreeModel : " + refreshNode);
       DefaultMutableTreeNode dmtRoot = (DefaultMutableTreeNode) this.getRoot();
       if (dmtRoot != null ) {
           Object dmtObj =  dmtRoot.getUserObject();
           if (dmtObj != null) {
               BungeniBNode nodeRoot = (BungeniBNode) dmtObj;
               BungeniTreeRefactorTree refactorTree = new BungeniTreeRefactorTree (this, nodeRoot, refreshNode);
               refactorTree.doMerge();
           }
       }
    }
    
    
}
