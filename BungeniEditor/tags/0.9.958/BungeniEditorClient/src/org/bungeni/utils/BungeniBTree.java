/*
 * BungeniBTree.java
 *
 * Created on October 21, 2007, 5:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

/**
 *
 * @author Administrator
 */
/*
 * Main.java
 *
 * Created on October 21, 2007, 2:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
/**
 * <B>Class that implements a binary tree, used for tracking hierarchies within a Openoffice document
 * . Currently the section hierarchy is created by enumerating the document, and tracking the sections and their parents.
 * The tree class also implements lookups for nodes by name, or by order of enumeration</B>
 * @author ashok
 */
public final class BungeniBTree {
        private TreeMap<Integer,BungeniBNode> roots = new TreeMap<Integer,BungeniBNode> ();
        private HashMap<String, BungeniBNode> rootsByName = new HashMap<String,BungeniBNode>();
        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniBTree.class.getName());
        
    /**
     * Default constructor for the class
     */
        public BungeniBTree() {
            super();
        }
        
    /**
     * <B>The class supports multiple root nodes. However, current usage within the document is for a single root node.</B>
     * @param name Name for the new root section to be added
     */
        public void addRootNode(String name) {
           //System.out.println("roots.size+1 is "+ new Integer(roots.size()+1));
           Integer nKey = roots.size()+1;
           BungeniBNode xRoot = new BungeniBNode(name);
           roots.put(nKey, xRoot );
           rootsByName.put(name, xRoot);
           //System.out.println("roots.size = key , " + newKey.toString() +", "+ roots.get(new Integer(2)));
        }
        
        public void addRootNode(BungeniBNode aNode) {
           Integer nKey = roots.size()+1;
           roots.put(nKey, aNode );
           rootsByName.put(aNode.getName(), aNode);
        }
    /**
     * <B>Adds a child node, to an existing node identified by name</B>
     * @param parent Name of Node to which the node is to be added
     * @param child Name of node to be added to parent.
     */
        public void addNodeToNamedNode(String parent, String child) {
            BungeniBNode node = getNodeByName(parent);
            if (node == null ) {
                log.debug("unable to add node because parent:" + parent + " was null");
                return;
            }
           if (node.containsNodeByName(child))
                log.debug("Already contains child named "+ child);
            else  {
                //check if child exists in tree... if so then add 
                node.addChild(new BungeniBNode(child));
            }
                
              
        }
        
    /**
     * <B>Adds a child node, to an existing node identified by name</B>
     * @param parent Name of Node to which the node is to be added
     * @param child BungeniBNode object to be added to parent.
     */
        public void addNodeToNamedNode(String parent, BungeniBNode child) {
            BungeniBNode node = getNodeByName(parent);
            if (node == null ) {
                log.debug("unable to add node because parent:" + parent + " was null");
                return;
            }
           if (node.containsNodeByName(child.getName()))
                log.debug("Already contains child named "+ child);
            else  {
                //check if child exists in tree... if so then add 
                node.addChild(child);
            }
        }
                
        
    /**
     * Retrieves a node from the tree by name
     * @param name Name of node to be retrieved
     * @return Returns a BungeniBNode object.  If the node is not found, returns null.
     */
        public BungeniBNode getNodeByName(String name) {
            if (this.rootsByName.containsKey(name)) {
                return rootsByName.get(name);
            }
            Iterator<Integer> rootIter = roots.keySet().iterator();
            while (rootIter.hasNext()) {
                Integer key = (Integer) rootIter.next();
                BungeniBNode n = roots.get(key);
                //System.out.println("root name = key ="+ key + " is = "+ n.getName());
                //if (n.getName().equals(name)) {
                //    return n;
                //} else {
                return walkNodeByName(n, name);
                //}
            }
          return null;
        }
   
        
        private BungeniBNode walkNodeByName(BungeniBNode n, String name) {
            if (n.getChildrenByName().size() == 0) {
                return null;
            }
            if (n.getChildrenByName().containsKey(name)) {
               return n.getChildrenByName().get(name);
            } else {
               Iterator<String> names = n.getChildrenByName().keySet().iterator();
               while (names.hasNext()) {
                   String nodeName =  (String) names.next();
                   BungeniBNode n_child = n.getChildrenByName().get(nodeName);
                   BungeniBNode walkNode = walkNodeByName(n_child, name);
                   if (walkNode != null )
                       return walkNode;
               }
            }
           return null;
        }
        
        private void walkNodeByOrder(BungeniBNode n, int depth) {
            if (n.getChildrenByOrder().size() == 0) {
                return;
            }
            depth++;
            Iterator<Integer> nodesByOrder = n.getChildrenByOrder().keySet().iterator();
            while (nodesByOrder.hasNext()) {
                Integer key = (Integer) nodesByOrder.next();
                BungeniBNode n_child  = n.getChildrenByOrder().get(key);
                sbOut.append(padding(depth)+n_child.getName()+"\n");
                walkNodeByOrder(n_child, depth);
            }
            
        }
        
        private String padding (int depth) {
            String pad = "";
            for (int i = 0; i < depth; i++) {
                pad+=" ";
            }
            return pad;
        }
        private StringBuffer sbOut = new StringBuffer();
       
    /**
     * Returns a indented string representation of the tree structure
     * @return Returns a String
     */
    @Override
        public String toString() {
           sbOut = new StringBuffer();
           Iterator<Integer> rootIter = roots.keySet().iterator();
           int depth = 0;
           while (rootIter.hasNext()) {
                Integer key = (Integer) rootIter.next();
                BungeniBNode n = roots.get(key);
                sbOut.append(padding(depth) + n.getName()+ "\n");
                walkNodeByOrder(n, depth);
            }
           return sbOut.toString(); 
        }
        
      
        
    /**
     * Returns the BTree root(s)
     */
        public TreeMap<Integer, BungeniBNode> getTree(){
            return this.roots;
        }
        
        public int getRootCount(){
            return roots.size();
        }
        public BungeniBNode getFirstRoot(){
            /*Iterator<Integer> itr = roots.keySet().iterator();
            Integer intKey = itr.next();
            
            return roots.get(intKey);*/
            
            Entry<Integer,BungeniBNode> fEntry =roots.firstEntry();
            if (fEntry != null) {
                return fEntry.getValue();    
            } else {
                log.error("getFirstRoot : returned null");
                return null;
            }
        }
        
        public static BungeniBNode add(BungeniBNode p, String childname){
                BungeniBNode newChild = new BungeniBNode (childname, p);
                p.addChild(newChild);
                return newChild;
        }

       public static void main(String[] args){
            BungeniBTree aTree= new BungeniBTree();
            aTree.addRootNode(new String("root"));
            BungeniBNode nRoot = aTree.getTree().get(1) ;
            add(nRoot, "child1");
            add(nRoot, "child2");
            
            BungeniBTree aTree2= new BungeniBTree();
            aTree2.addRootNode(new String("root"));
            BungeniBNode nRoot2 = aTree2.getTree().get(1) ;
           // add(nRoot2, "child1");
            add(nRoot2, "child1.1");
            add(nRoot2, "child4");
           // add(nRoot2, "child2");
            add(nRoot2, "child3");
            
            
            System.out.println(aTree.toString());
            
            System.out.println(aTree2.toString());
    }
       

    }
    
