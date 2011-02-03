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
public class DocumentSectionFriendlyAdapterDefaultTreeModel extends DefaultTreeModel implements IRefreshableSectionTreeModel  {
    Timer treeModelTimer ;
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionFriendlyAdapterDefaultTreeModel.class.getName());
  
    /** Creates a new instance of DocumentSectionAdapterDefaultTreeModel */
  
    
    public DocumentSectionFriendlyAdapterDefaultTreeModel(DefaultMutableTreeNode root , boolean bTimer ) {
        super(root);
        if (bTimer) {
          treeModelTimer = new Timer(DocumentSectionProvider.TIMER_DELAY, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                DefaultMutableTreeNode nnode = DocumentSectionFriendlyTreeModelProvider.newRootNode();
                
                setRoot(DocumentSectionFriendlyTreeModelProvider.newRootNode());
                
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


/*
    private void viewDmtNodes(BungeniBNode nodeRoot, DefaultMutableTreeNode dmtRoot ) {
        DefaultMutableTreeNode anode = (DefaultMutableTreeNode) nodeRoot.getNodeObject();
        log.debug("dmt = " + anode.toString() + ", bbnode = " + nodeRoot.toString());
        log.debug("dmt count = " + dmtRoot.getChildCount() + " , anode dmt count = " + anode.getChildCount() + " bnode count = " + nodeRoot.getChildCount());
    } 
 */ 
    public void updateTreeModel(BungeniBNode refreshNode) {
      //  throw new UnsupportedOperationException("Not supported yet.");
       log.debug("updateTreeModel for : " + refreshNode);
  
       DefaultMutableTreeNode dmtRoot = (DefaultMutableTreeNode) this.getRoot();
       
       if (dmtRoot != null ) {
           Object dmtObj =  dmtRoot.getUserObject();
           if (dmtObj != null) {
               log.debug("updateTreeModel refactoring tree");
               BungeniBNode nodeRoot = (BungeniBNode) dmtObj;
               log.debug("updateTreeModel : before state");
               //viewDmtNodes(nodeRoot, dmtRoot);
               DefaultMutableTreeNode newdmt = (DefaultMutableTreeNode) nodeRoot.getNodeObject();
            //   if (newdmt.getChildCount() != dmtRoot.getChildCount()) {
            //       this.setRoot(newdmt);
            //   }
               BungeniTreeRefactorTree refactorTree = new BungeniTreeRefactorTree (this, nodeRoot, refreshNode);
               refactorTree.doMerge();
               log.debug("updateTreeModel : after state");
               //viewDmtNodes(nodeRoot, dmtRoot);
               
           } else {
               log.debug("updateTreeModel userObject of dmt node was null");
           }
       } else {
           log.debug("updateTreeModel, dmtRoot was null");
       }
    }

    public void newRootNode() {
       // throw new UnsupportedOperationException("Not supported yet.");
    }
}
