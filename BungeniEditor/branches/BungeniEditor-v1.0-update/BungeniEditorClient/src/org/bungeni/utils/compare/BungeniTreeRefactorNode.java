package org.bungeni.utils.compare;

import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.bungeni.utils.BungeniBNode;
/**
 * Refactors a node in a tree
 * @author undesa
 */
public class BungeniTreeRefactorNode {
    private DefaultTreeModel treeModel;
    private BungeniBNode originalRootNode;
    private BungeniBNode mergeRootNode;
    private DefaultMutableTreeNode originalDMTreeRootNode;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniTreeRefactorNode.class.getName());
    
    public BungeniTreeRefactorNode (DefaultTreeModel model, BungeniBNode rootNodeOriginal, BungeniBNode rootNodeMerge) {
        treeModel = model;
        originalRootNode = rootNodeOriginal;
        mergeRootNode = rootNodeMerge;
        originalDMTreeRootNode = (DefaultMutableTreeNode) originalRootNode.getNodeObject();
    }
    
    /**
     * SwingWorker class that does the actual merge of the trees
     */
     class NodeMergeAgent extends SwingWorker<BungeniNodeComparator, Void>{
        /**
         * Runs the non-UI bits of the tree merge code
         * @return a BungeniNodeComparator object
         */
        @Override
        protected BungeniNodeComparator doInBackground()  {
                 //create a node comparator object
                 BungeniNodeComparator comp = new BungeniNodeComparator();
                 try {
                 //compare the nodes and generate the difference maps
                 comp.compareAndDiff(getOriginalRootNode(), getMergeRootNode());
                 } catch (Exception ex) {
                     log.error("BungeniNodeComparator.doInBackground : " + ex.getMessage());
                 } finally {
                 return comp;
                 }
        }
        
        /**
         * Is invoked after doInBackground() and runs the UI bits of the merge -
         * adding, deleting updating nodes from the tree models
         * The call to get() returns the output of doInBackground()s
         */
       @Override
        protected void done(){
            try {
                BungeniNodeComparator compObj = get();
                if (compObj.NULL_CONDITION) {
                    log.error("NodeMergeAgent : null condition was returned");
                    return;
                }
                boolean bDeletions = doMergeDeletions(compObj);
                //process the update difference chain
                //we process each chain as one atomic unit of change
                //for processing updates we dont use the update map, instead
                //we use the udpate difference chain since it provides a connected 
                //chain of changes i.e. if index 1 => 2 and 2 => 3 and 3 => 8 and so on...
                boolean bUpdates = doMergeUpdates(compObj);
                //process insert differences
                boolean bInserts = doMergeInserts(compObj);               
                
            } catch (InterruptedException ex) {
               log.error("NodeMergeAgent : done() : " + ex.getMessage());
            } catch (ExecutionException ex) {
               log.error("NodeMergeAgent : done() : " + ex.getMessage());
            }
        }

    
    
    
    }
    /**
     * Activates a merge for the node. The original root node is updated in place 
     * and merged with the merge root node.
     * This function runs on the EDT, and so we use a swingworker thread to run the non-gui bits.
     */
    public void doMerge(){
       try {
            NodeMergeAgent nodeMerger = new NodeMergeAgent();
            nodeMerger.execute();
        } catch (Exception ex) {
            log.error("doMerge : " + ex.getMessage());
        }
    }
    
    /**
     * Merges deletions between the original and the merge node
     * @param a BungeniNodeComparator object
     * @return
     */
    private boolean doMergeDeletions(BungeniNodeComparator comp){
        boolean bState = false;
        try {
        TreeMap<Integer,BungeniNodeDifference> diffDel = comp.getDiffMapDelete();
        for (Integer diffnode: diffDel.keySet()) {
                //get the vectors of the node to be deleted
              BungeniNodeDifference n = diffDel.get(diffnode);
              Integer origIndex = n.getOriginalIndex();
              String origName = n.getOriginalName();
              //get bnode to be removed
              BungeniBNode nNode = this.getOriginalRootNode().getChildNodeByName(origName);
              //the bnode stores the actual dmt within a node object
              //the below api synchronizes the node deletion between the bnode 
              //tree and the default tree model
              deleteNodeFromTree(nNode);
              bState = true;
        }
        } catch (Exception ex) {
            log.error ("doMergeDeletions for "+ originalRootNode + "  = " + ex.getMessage());
        } finally {
            return bState;
        }
    }
   
        private void deleteNodeFromTree(BungeniBNode nNode) {
            try {
               DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) nNode.getNodeObject();
                  dmt.setUserObject(new BungeniBNode ("deleting"));
                  nNode.setNodeObject(null);
                  this.getTreeModel().removeNodeFromParent(dmt);
                  this.getOriginalRootNode().removeChild(nNode);
            } catch(Exception ex) {
                log.error("deleteNodeFromTree : " + ex.getMessage());
            }
        }
  
    private boolean doMergeUpdates(BungeniNodeComparator comp) {
        boolean bState = false;
        boolean bMergeTree = false, bMergeModel = false;
        try {
           //step 1 process changes in BungeniBTree structures
            bState = doMergeUpdatesinBungeniTree(comp);
            bMergeTree = bState;
           //step 2 reflect updates into the tree model by updating the defaultmutabletree node structure
            bState = doMergeUpdatesinTreeModel(comp);
            bMergeModel = bState;
            //return success
            bState = true;
        } catch (Exception ex) {
            log.error ("doMergeUpdates for "+ originalRootNode + "  = " + ex.getMessage());            
        } finally {
            log.debug("doMergeUpdates : mergeTree = "+ bMergeTree + ", mergeModel = " + bMergeModel );
            if (bMergeTree && bMergeModel)
                return true;
            else
                return false;
           // return bState;
        }
    }

            public boolean doMergeUpdatesinBungeniTree(BungeniNodeComparator comp)  {
                boolean bState = false;
                try {

                    for (DifferenceChain dc : comp.getUpdateDifferenceChain()) {
                    DifferenceChain diffNext = dc;
                    BungeniBNode cachedNode = null;
                    while (diffNext != null ) {
                        //process the first chain
                        BungeniNodeDifference diff = diffNext.diff;
                        //cache the object at the next index ,
                        //replace the next index with the current index
                        BungeniBNode origNode = null;
                        if (cachedNode == null ) {
                            //previous difference was updated to a new index
                            //so we use original node
                           origNode = this.getOriginalRootNode().getChildrenByOrder().get(diff.getOriginalIndex());
                        } else {
                           origNode = cachedNode;  
                        }
                        //copy the original node to the new index, and return the cached node at 
                        //that position
                        //this is the code that moves the indexs in the BungeniBTree..
                        //after copying remove the node from the original index
                       //  cachedNode = rootNode.setNodeAtIndex(origNode, diff.getUpdateFromIndex());
                        if (cachedNode == null ) { //first time in we move node from x to y
                            cachedNode = getOriginalRootNode().moveNodeAtIndexFromTo(diff.getOriginalIndex(), diff.getUpdateFromIndex());
                        } else {
                            cachedNode = getOriginalRootNode().setNodeAtIndex(origNode, diff.getUpdateFromIndex());
                        }
                        diffNext = diffNext.nextDifference;
                    }
                    bState = true;
                }         

                } catch (Exception ex) {
                       log.error("doMergeUpdatesinBungeniTree exception :" + ex.getMessage());
                } finally {
                    return bState;
                }
            }
    
            public boolean doMergeUpdatesinTreeModel(BungeniNodeComparator comp)  {
                boolean bState = false;
                try {
                      for (DifferenceChain dc : comp.getUpdateDifferenceChain()) {
                        DifferenceChain dtmNext = dc;
                        while (dtmNext != null) {
                            //get new index that original has moved to
                            //Integer origIndex = dtmNext.diff.getOriginalIndex();
                            Integer newIndex = dtmNext.diff.getUpdateFromIndex();
                            BungeniBNode dmtHolder = getOriginalRootNode().getNodeAtIndex(newIndex);
                            DefaultMutableTreeNode nodeToMove = (DefaultMutableTreeNode)dmtHolder.getNodeObject();
                           //get current index of node in tree
                            int dmtIndex = this.getTreeModel().getIndexOfChild(this.getOriginalDMTreeRootNode(), nodeToMove);
                             //get current relative index of node in bnode
                            int bnodeIndex = this.getOriginalRootNode().relativeIndexOfChild(dmtHolder);
                            if (dmtIndex != bnodeIndex) {
                              //log.debug("moving dmt node from : " + dmtIndex + " to :" + bnodeIndex);
                              if (bnodeIndex > this.getOriginalDMTreeRootNode().getChildCount()-1) {
                                  getOriginalDMTreeRootNode().add(nodeToMove);
                              } else {
                               getTreeModel().removeNodeFromParent(nodeToMove);
                               getTreeModel().insertNodeInto(nodeToMove, this.getOriginalDMTreeRootNode(), bnodeIndex );
                              }
                            }

                            dtmNext = dtmNext.nextDifference;
                        }
                    }
                  bState = true;
                } catch (Exception ex) {
                    log.error("doMergeUpdatesinTreeModel : " + ex.getMessage());
                } finally {
                    return bState;
                }
            }
    
    public boolean doMergeInserts(BungeniNodeComparator comp) {
        boolean bState = false;
        try {
            TreeMap<Integer,BungeniNodeDifference> diffIns = comp.getDiffMapInsert();
            for (Integer diffnode: diffIns.keySet()) {
                  BungeniNodeDifference n =  diffIns.get(diffnode);
               //   System.out.println(n);
                  Integer newindex = n.getUpdateFromIndex();
                  BungeniBNode newnode = this.getMergeRootNode().getChildrenByOrder().get(newindex);
                  newnode.setParent(this.getOriginalRootNode());
                  //
                  newnode.setAndRunNamedCallback(getOriginalRootNode().getCallbackName());
                  //
                  getOriginalRootNode().setNodeAtIndex(newnode, newindex);
                  //create a dmt node for the newly added node
                  DefaultMutableTreeNode newDmt = new DefaultMutableTreeNode(newnode);
                  newnode.setNodeObject(newDmt);
                  int bnodeIndex = getOriginalRootNode().relativeIndexOfChild(newnode);
                  getTreeModel().insertNodeInto(newDmt, this.getOriginalDMTreeRootNode(), bnodeIndex);
            }
            bState = true;
        } catch (Exception ex) {
            log.error("doMergeInserts : " + ex.getMessage());
        } finally {
            return bState;
        }

    }
    
    
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }


    public BungeniBNode getOriginalRootNode() {
        return originalRootNode;
    }

    public BungeniBNode getMergeRootNode() {
        return mergeRootNode;
    }

    public DefaultMutableTreeNode getOriginalDMTreeRootNode() {
        return originalDMTreeRootNode;
    }

  
} 