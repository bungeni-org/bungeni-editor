package org.bungeni.utils.compare;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.BungeniBNode;

public class BungeniNodeComparator {
    
    private  TreeMap<Integer, BungeniNodeDifference> diffMapInsert = new TreeMap<Integer, BungeniNodeDifference>();
    private  TreeMap<Integer, BungeniNodeDifference> diffMapUpdate = new TreeMap<Integer, BungeniNodeDifference>();
    private  TreeMap<Integer, BungeniNodeDifference> diffMapDelete = new TreeMap<Integer, BungeniNodeDifference>();
    private  ArrayList<DifferenceChain> updateDifferenceChain = new ArrayList<DifferenceChain>(0);
    
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniNodeComparator.class.getName());
     public boolean NULL_CONDITION = false;
        
    private void clearMaps(){
        diffMapInsert.clear();
        diffMapDelete.clear();
        diffMapUpdate.clear();
    }
   
    private void diffAdd(Integer nKey, BungeniNodeDifference diff) {
        switch (diff.getDiffState()) {
        case INSERT:
            update (getDiffMapInsert(),nKey, diff);
            break;
        case UPDATE:
            update (getDiffMapUpdate(),nKey, diff);
            break;
        case DELETE:
            update (getDiffMapDelete(),nKey, diff);
            break;
        default:
            break;
        }
        
    }
    private void update (TreeMap<Integer, BungeniNodeDifference> map, Integer nKey, BungeniNodeDifference diff) {
        map.put(nKey, diff);        
    }
        
    /*
    private void processUpdateChains() {
        ArrayList<DifferenceChain> dcList = new ArrayList<DifferenceChain>();
        Iterator<Integer> iterKey = getDiffMapUpdate().keySet().iterator();
        while (iterKey.hasNext()) {
            
           Integer nKey = iterKey.next();
           //get the node difference objects sequentially
           BungeniNodeDifference n = getDiffMapUpdate().get(nKey);
           log.debug("processUpdateChain = DIFFS + " + n);
           //create a chain object for it.
           DifferenceChain dc = new DifferenceChain();
           Integer changeTo = n.getUpdateFromIndex();
           String changeToName = n.getUpdateFromName();
           dc.diff = n;
           //check if the chain exists in the list for the original index,
           boolean ifaddedToExistingChain = this.addToExistingChain(dcList, n);
           
           //if it doesnt we check if the original exists as a target index,
           //   and we chain it at the next position of the matching object
           //if the chain exists with the original we skip it and move to the 
           //   next difference object
           
           if (!ifaddedToExistingChain) {
               dcList.add(dc);
               DifferenceChain dcPrev = dc;
               for  (;; ) {
                   if (getDiffMapUpdate().containsKey(changeTo)){
                        BungeniNodeDifference nchain = getDiffMapUpdate().get(changeTo);
                        DifferenceChain dcChain = new DifferenceChain();
                        dcChain.diff = nchain;
                        dcPrev.nextDifference = dcChain;
                        dcChain.prevDifference = dcPrev;
                        changeTo = nchain.getUpdateFromIndex();
                   } else
                       break;
               } 
           }    
        }
        
        this.updateDifferenceChain = dcList;
    }
    */
    
    private ArrayList<DifferenceChain> buildDifferenceChain () {
        ArrayList<DifferenceChain> diffChain = new ArrayList<DifferenceChain>(0);
        try {
            //get the first element in the list

            int sizeOfList = getDiffMapUpdate().size();
            ArrayList<String> runningKeys = new ArrayList<String>(0);
            int runningSize = 0;
            for (Integer nKey : getDiffMapUpdate().keySet()) {

                BungeniNodeDifference ndiff = getDiffMapUpdate().get(nKey);
                //if already exists as a difference chain
                if (!runningKeys.contains(ndiff.getDiffKey())) {

                    DifferenceChain dStart = new DifferenceChain();
                    dStart.diff = ndiff;
                    runningKeys.add(ndiff.getDiffKey());
                    diffChain.add(dStart);
                    Integer startTarget = dStart.diff.getUpdateFromIndex();
                    DifferenceChain running = dStart;
                    Integer runningindice = startTarget;
                    //search for target as original  in list
                    for (;running != null ;) { //until chains are built
                         running = findNextTarget (runningKeys, running, running.diff.getUpdateFromIndex());
                    }
                   runningSize = dStart.chainSize();
            }
           }
        } catch (Exception ex) {
            log.error("buildDifferenceChain : " + ex.getMessage());
        } finally {
            return diffChain; 
        }
    }
    
    private DifferenceChain findNextTarget (ArrayList<String> runningKeys, DifferenceChain dRun, Integer runIndice) {
        try {
        for (Integer nKey :  getDiffMapUpdate().keySet()) {
            //get the differnece key
            BungeniNodeDifference ndiff = getDiffMapUpdate().get(nKey);
            //check if diff object has been trapped
            if (!runningKeys.contains(ndiff.getDiffKey())){
                //check if it mataches current target
                Integer searchOrigIndice = ndiff.getOriginalIndex();
                if (runIndice == searchOrigIndice ) {
                    //chain the two together... 
                    DifferenceChain dnewDiff = new DifferenceChain();
                    dnewDiff.diff = ndiff;
                    dRun.nextDifference = dnewDiff;
                    dnewDiff.prevDifference = dRun;
                    runningKeys.add(ndiff.getDiffKey());
                    return dnewDiff; //return the new next element in the difference chain
                }
            }
        }
        } catch (Exception ex) {
            log.error("findNextTarget : " + ex.getMessage());
        }
        return null;
    }
    /*
     * -1 - original exists in chain list
     * index number of target - original exists as target index
     * -2 - original does not exist as original or target index
     */
    private boolean addToExistingChain(ArrayList<DifferenceChain> dclist, BungeniNodeDifference nchain) {
        try {
            for (DifferenceChain d : dclist) {
                DifferenceChain drunning = d;
                    while (drunning != null) { 
                         BungeniNodeDifference ncomp = drunning.diff;
                         //original index from node diff exists in difference chain
                         if (ncomp.getOriginalIndex().equals(nchain.getOriginalIndex())) {
                            break; //exit from loop - earch further
                         }
                         //check if replacement index = the original of the nchain node... 
                         //if equal we chain the nchain node to th stored node
                         if (ncomp.getUpdateFromIndex().equals(nchain.getOriginalIndex())) {
                             //original exists as a target index... we chain this difference object
                             // to the current difference chain
                             if (drunning.nextDifference != null ) {
                                 DifferenceChain newd = new DifferenceChain();
                                 newd.diff = nchain;
                                 drunning.nextDifference = newd;
                                 newd.prevDifference = drunning;
                                 return true;
                             }
                         }
                         drunning = drunning.nextDifference; 
                    }
            }
            //does not exist in reference chain as original
        } catch (Exception ex) {
            log.error("addToExistingChain : " + ex.getMessage());
        }
            return false;
            
    }
    private boolean existsInChainList(ArrayList<DifferenceChain> dclist, BungeniNodeDifference nchain) {
        try {
        for (DifferenceChain d : dclist) {
            DifferenceChain drunning = d;
                while (drunning != null) { 
                     BungeniNodeDifference ncomp = drunning.diff;
                     if (ncomp.getDiffKey().equals(nchain.getDiffKey())) {
                        return true;
                     }
                     drunning = drunning.nextDifference; 
                }
        }
        } catch (Exception ex) {
            log.error("existsInChainList:" + ex.getMessage());
        }
        return false;
    }
    public void compareAndDiff(BungeniBNode root1, BungeniBNode root2){
        //build comparative difference maps
        NULL_CONDITION = false;
        if (root1 == null || root2 == null) {
            log.error("compareAndDiff : either root1 or root2 was null");
            NULL_CONDITION = true;
            return ;
        }
        compare(root1, root2);
     //   for (Integer n : this.getDiffMapUpdate().keySet()) {
     //     log.debug("compareAndDiff  updateMap = " + getDiffMapUpdate().get(n));
     // }
        //build update chain
        this.updateDifferenceChain = this.buildDifferenceChain();
    }
     private void compare (BungeniBNode root1, BungeniBNode root2){
         try {
          TreeMap<Integer, BungeniBNode> root2children = root2.getChildrenByOrder();
            for (Integer root2child : root2children.keySet()) {
                BungeniBNode aNode = root2children.get(root2child);
                if (root1.containsNodeByName(aNode.getName())) { //root1 contains the child
                       //check if index of node in root1 == index of node in root2
                       BungeniBNode nNode = root1.getChildNodeByName(aNode.getName());
                       Integer indexinroot1 = root1.indexOfChild(nNode);
                       //BungeniBNode nNodeInRoot2 = root2children.get(root2child);
                       //Integer indexinroot2 = root2.indexOfChild(nNodeInRoot2);
                       if (indexinroot1 != root2child) {
                         //  System.out.println("root1 contains " + aNode.getName() + " at unequal index (move " + aNode.getName() + " from " + indexinroot1 + " to "+ root2child+") or (insert "+aNode.getName() + " at " + root2child + ")");
                           BungeniNodeDifference nodeDiff = new BungeniNodeDifference ();
                           nodeDiff.diffUpdate(aNode.getName(), indexinroot1, aNode.getName(), root2child);                       
                           this.diffAdd(indexinroot1, nodeDiff);
                       }
                   }  else {

                   // System.out.println("root1 does not contain  " + aNode.getName() + " (add child to root1 at index : "+ root2child + " )");
                           BungeniNodeDifference nodeDiff = new BungeniNodeDifference ();
                           nodeDiff.diffInsert(aNode.getName(), root2child);
                           this.diffAdd(root2child, nodeDiff);
                   }
               }

            //now look for deletions in the original map...
            final HashMap<String, BungeniBNode>root1children = root1.getChildrenByName();
            //final HashMap<String, BungeniBNode>root2childrenbyname = root2.getChildrenByName();
            for (String root1child: root1children.keySet()){
                if (!root2.containsNodeByName(root1child)) {
                    BungeniBNode rChild = root1children.get(root1child);
                    Integer indexofrChild = root1.indexOfChild(rChild);
                    BungeniNodeDifference diff = new BungeniNodeDifference();
                    diff.diffDelete(rChild.getName(), indexofrChild);
                    this.diffAdd(indexofrChild, diff);
                }
            }
         } catch (NullPointerException ex) {
          log.error("compare : "+ ex.getMessage());
          log.error("compare : "+ CommonExceptionUtils.getStackTrace(ex));
         } catch (Exception ex){
            log.error("compare : " + ex.getMessage());
         }
    }

    public TreeMap<Integer, BungeniNodeDifference> getDiffMapInsert() {
        return diffMapInsert;
    }

    public TreeMap<Integer, BungeniNodeDifference> getDiffMapUpdate() {
        return diffMapUpdate;
    }

    public TreeMap<Integer, BungeniNodeDifference> getDiffMapDelete() {
        return diffMapDelete;
    }
    
    public ArrayList<DifferenceChain> getUpdateDifferenceChain(){
        return this.updateDifferenceChain;
    }
    
}