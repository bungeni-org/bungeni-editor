package org.bungeni.utils.compare;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;

/**
 * Refactors two BungeniBTree objects by merging one tree into another
 * The class synchronizes deletions, inserts and updates from one tree into another.
 * Updates include movemement of a node in a tree from one position to another.
 * 
 * @author undesa
 */
public class BungeniTreeRefactorTree {
    /**
     * The UI tree model that is being updated
     */
    private DefaultTreeModel treeModel;
    /**
     * the root node of the tree that requires update
     */
    private BungeniBNode treeRootNode;
    /**
     * The root node of the tree that is updating the original tree
     * 
     */
    private BungeniBNode treeMergeRootNode;
    /**
     * Condition that determines whether dispay text is to be merged or not 
     */
    private boolean bMergeDisplayText = true;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniTreeRefactorTree.class.getName());
    /**
     * Constructor to instantiate a Refactoring object
     * @param model - input tree model
     * @param rootNode  - BungeniBNode root of tree model
     * @param newRootNode - Update root of a BungeniBTree that reflects the new tree structure
     */       
    public BungeniTreeRefactorTree(DefaultTreeModel model, BungeniBNode rootNode, BungeniBNode newRootNode){
        this.treeModel  = model;
        this.treeRootNode = rootNode;
        this.treeMergeRootNode = newRootNode;
    }

    public void setMergeDisplayText(boolean bState){
        bMergeDisplayText = bState;
    }
    
    public boolean getMergeDisplayText(){
        return this.bMergeDisplayText;
    }
    
    public void doMerge() {
        //do a recursive merge of the tree
        //step1 do a merge for the rootnodes
        //
        /*
        
         root      (1)          root          (2)
            |                      |
            |____child1            |____child3
            |____child2            |____child1
            |____child3            |____child7
            |____child4            |____child5
            |____child5            |____child4
            |____child6                    |
            |____child7                    |''''''child4.1
            |____child8                    |......child4.2
                                           |______child4.3

         */
        // the original root structure (1) is converted to structure (2)
        // once (2) has been transformed to (2) for the first iteration, 
        // it accquires some nodes and loses some nodes... in the above example,
        // child4 has been reordered and has accquired 3 children 4.1, 4,2 and 4.3
        // these child notes do not have defaultmutabletreenode objects, but exist
        // merely as BungeniBNode structures.
        // NOTE that the functionality presently doesnt handle node movement/updates across
        // hierarchies, movement and update is handled only within the same sibling hierarchy,
        // a node moving to a new hierarchy is treated as a deletion from the original, and 
        // a subsequent addition to a different hierarchy
       // log.debug("doMerge : starting for orig : " + getTreeRootNode() + " , merge : " + this.getTreeMergeRootNode());
        BungeniTreeRefactorNode nodeRefactor = new BungeniTreeRefactorNode(getDefaultTreeModel(),getTreeRootNode(),getTreeMergeRootNode());
        nodeRefactor.doMerge();
        //log.debug("doMerge : merging children ");
        doMergeChildren(this.getTreeRootNode(), this.getTreeMergeRootNode());
        log.debug("After doMergeChildren : ");
        //viewDmtNodes(getTreeRootNode());
        //at this point any new children now exist within the rootnode structure as BungeniNodes,
        //we need to create the corresponding UI defaultmutabletreenode structure for the 
        //empty bnode structures.
        //step2 recurse within the root node's children and iterate them...
        /*
        child4   (3)                        child4    (4)
           |                                   |
           |''''''child4.1 (without dmt)       |''''''child4.1 (with dmt)
           |......child4.2 (without dmt)       |......child4.2 (with dmt)
           |______child4.3 (without dmt)       |______child4.3 (with dmt)
       */
       //the children of any updated / new nodes are recursed and the NodeObject of the 
       //BNodes is examined (3), if it is null, a DetaultMutableTreeNode object
       //is  created and set into the NodeObject field of the BungeniBNode
       for (Integer nKey : getTreeRootNode().getChildrenByOrder().keySet()) {
           BungeniBNode foundNode = getTreeRootNode().getNodeAtIndex(nKey);

           seedTreeWithUITreeNodes(foundNode);         
       }

      
    }
/*
    private void viewDmtNodes(BungeniBNode nodeRoot ) {
        DefaultMutableTreeNode anode = (DefaultMutableTreeNode) nodeRoot.getNodeObject();
        log.debug("dmt = " + anode.toString() + ", bbnode = " + nodeRoot.toString());
        log.debug(" anode dmt count = " + anode.getChildCount() + " bnode count = " + nodeRoot.getChildCount());
    } 
  */  
 private void seedTreeWithUITreeNodes(BungeniBNode nodeDMTnodes){
     //recurse children of rootnodes
     try {
     DefaultMutableTreeNode dmtofNodeDMT = (DefaultMutableTreeNode) nodeDMTnodes.getNodeObject();
      dmtofNodeDMT.setAllowsChildren(true);
     boolean structureChanged = false;
     for (Integer nKey : nodeDMTnodes.getChildrenByOrder().keySet()){
         BungeniBNode childofNodeDmtNode = nodeDMTnodes.getNodeAtIndex(nKey);
         Object nodeObject = childofNodeDmtNode.getNodeObject();
         if (nodeObject == null){
             DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(childofNodeDmtNode);
             childofNodeDmtNode.setNodeObject(dmt);
             dmtofNodeDMT.add(dmt);
             structureChanged = true;
         }
         seedTreeWithUITreeNodes(childofNodeDmtNode);
     }
     if (structureChanged) {
         this.getDefaultTreeModel().nodeStructureChanged(dmtofNodeDMT);
     }
     } catch (Exception ex) {
         log.error("seedTreeWithUITreeNodes : " + ex.getMessage());
         log.error("seedTreeWithUITreeNodes : " + CommonExceptionUtils.getStackTrace(ex));
     }
 }   
    
 
 
 private void doMergeChildren(BungeniBNode origNode, BungeniBNode mergeNode) {
     try {
         if (origNode == null )
             log.error("doMergeChildren : origNode was null");
         else
             log.debug("doMergeChildren : origNode.getName() = " + origNode.getName());
         if (mergeNode == null ) {
             log.error("doMergeChildren : mergeNode was null");
             return;
         }
         else
             log.debug("doMergeChildren : mergeNode.getName() = " + mergeNode.getName());
         
        //check if node's display text has changed .. if it has changed
        //set the new display text from the merged node.
         /*
        if (getMergeDisplayText())  {
            String root1DispText  = origNode.getDisplayText();
            if (mergeNode != null) {
                String root2DispText = mergeNode.getDisplayText();
                //added null check to avoid null pointer crash
                if (root2DispText != null)
                    if (!root1DispText.equalsIgnoreCase(root2DispText)) {
                        origNode.setDisplayText(root2DispText);
                    }
            } else
                log.debug("doMergeChildren : mergeNode was null");
        }*/
                                      
        for (String nodeName : origNode.getChildrenByName().keySet()) {
           //this root node has an updated UI, but its children dont
           BungeniBNode childOfOriginal = origNode.getChildNodeByName(nodeName);
           BungeniBNode childOfMergeNode = mergeNode.getChildNodeByName(nodeName);
           //check if nodeNewChild has children, if it doesnt we wipe out the children of the orignal node
           BungeniTreeRefactorNode childnodesRefactor = new BungeniTreeRefactorNode(getDefaultTreeModel(), childOfOriginal, childOfMergeNode);
           //the child nodes are merged inside a swingworker thread for better UI performances
           childnodesRefactor.doMerge();
           doMergeChildren(childOfOriginal, childOfMergeNode);
       }
       } catch (NullPointerException ex) {
           log.error("doMergeChildren : "+ex.getMessage());
           log.error("doMergeChildren : " + CommonExceptionUtils.getStackTrace(ex));
           
       } catch (Exception ex) {
           log.error("doMergeChildren : "+ex.getMessage());
           log.error("doMergeChildren : " + CommonExceptionUtils.getStackTrace(ex));
       }
 }
 


    public DefaultTreeModel getDefaultTreeModel() {
        return treeModel;
    }

    public BungeniBNode getTreeRootNode() {
        return treeRootNode;
    }

    public BungeniBNode getTreeMergeRootNode() {
        return treeMergeRootNode;
    }
}