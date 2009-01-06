/*
 * BungeniBNode.java
 *
 * Created on October 21, 2007, 5:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author Administrator
 */
    
 public class BungeniBNode {
            private String Name;
            private String displayText = "";
            private Object nodeObject = null;
            private BungeniBNode nodeParent = null;
            /*Stores child nodes by order*/
            private TreeMap<Integer, BungeniBNode> childNodes = new TreeMap<Integer,BungeniBNode>();
            /*Stores child nodes by name*/
            private HashMap<String, BungeniBNode> childNodeNames = new HashMap<String,BungeniBNode>();
            private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniBNode.class.getName());
            private static HashMap<String,INodeSetterCallback> nodeSetterCallbackList = new HashMap<String,INodeSetterCallback>(0);
            private String runThisCallback;
            
            /**
             * Creates a BungeniBNode object with a name
             * @param n - name of the new BungeniBNode object
             */
            public BungeniBNode(String n) {
               Name = n;
            }
            
            /**
             * Creates a BungeniBNode object with name, and a parent assignment
             * @param n - name of the new node
             * @param parent - parent BungeniBNode object
             */
            public BungeniBNode (String n, BungeniBNode parent) {
                Name = n;
                nodeParent = parent;
            }
            
            /**
             * Creates a BungeniBNode object with name, and a node object
             * @param n - name of the node
             * @param obj - node object
             */
            public BungeniBNode(String n, Object obj) {
                Name = n;
                nodeObject = obj;
            }
            
            public void setCallbackName(String name) {
                this.runThisCallback = name;
            }
            
            public String getCallbackName(){
                return runThisCallback;
            }
            
            public void setAndRunNamedCallback(String name) {
                if (name != null ) {
                    this.runThisCallback = name;
                    log.debug("setAndRunNamedCallback running for :" + this.Name);
                    invokeCallback();
                    
                } else {
                    log.debug("setAndRunNamedCallback not running for :" + this.Name);
                }
            }
            
            public static synchronized void setINodeSetterCallback(INodeSetterCallback icallBack) {
                nodeSetterCallbackList.put(icallBack.getName(), icallBack);
            }

            public static synchronized void removeAllCallbacks(){
                nodeSetterCallbackList.clear();
            }
            
            public static synchronized void removeCallback(String name) {
                if (nodeSetterCallbackList.containsKey(name))
                    nodeSetterCallbackList.remove(name);
            }
            
            private void invokeCallback(){
                INodeSetterCallback icall = nodeSetterCallbackList.get(runThisCallback);
                if (icall != null)
                    icall.nodeSetter(this);
            }
            
            public String nodeSignature(){
                //unique signature to idenfiy if node has changed.
                //name + childCount + parentName + order_in_parent.
                String orderInParent = "";
                String parentName = "";
                String sig = "";
                if (hasParent()) {
                    parentName = getParent().getName();
                    orderInParent = Integer.toString(getParent().indexOfChild(this));
                }
                sig =  getName()+"-"+Integer.toString(getChildCount())+"-"+parentName+"-"+orderInParent;
                return sig;
            }
            
            public String getName() {
                return Name;
            }
            
            public BungeniBNode getParent(){
                return nodeParent;
            }
            
            public void setParent(BungeniBNode parent) {
                nodeParent = parent;
            }
            public Object getNodeObject(){
                return nodeObject;
            }
            
            public void setNodeObject(Object obj){
                this.nodeObject = obj;
            }
            
            public boolean hasParent(){
                return (nodeParent == null ) ? false: true;
            }
            
            public boolean hasNodeObject(){
                return ((nodeObject == null) ? false: true);
            }
    @Override
            public String toString(){
               //return getName();
               log.debug("toString : displayText = " + displayText);
               if (displayText.length() == 0)
                    return getName();
                else
                    return displayText;
            }
    
            public HashMap<String,BungeniBNode> getChildrenByName() {
                return childNodeNames;
            }
            
            public BungeniBNode getChildNodeByName(String aName) {
                if (this.containsNodeByName(aName))  {
                    return getChildrenByName().get(aName);
                } else
                    return null;
            }
            
            public TreeMap<Integer,BungeniBNode> getChildrenByOrder(){
                return childNodes;
            }
            
            public int indexOfChild(BungeniBNode node) {
                if (containsNodeByName(node.getName())) {
                    Iterator<Integer> orderedNodeIterator = this.getChildrenByOrder().keySet().iterator();
                    while (orderedNodeIterator.hasNext()) {
                        Integer iKey = orderedNodeIterator.next();
                        BungeniBNode matchedNode = getChildrenByOrder().get(iKey);
                        if (matchedNode.equals(node)){
                            return iKey;
                        }
                    }
                }
                return -1;
            }
            
            public void addChild(BungeniBNode node) {
                childNodes.put(childNodes.size()+1, node);
                childNodeNames.put(node.getName(), node);
            }
            
            public void removeChild(BungeniBNode node) {
                //remove from ordered map
                Integer foundKey = null;
                if (childNodeNames.containsKey(node.getName())) {
                    childNodeNames.remove(node.getName());
                    Iterator<Integer> orderedNodeIterator = childNodes.keySet().iterator();
                    while (orderedNodeIterator.hasNext()) {
                        Integer iKey = orderedNodeIterator.next();
                        BungeniBNode foundNode = childNodes.get(iKey);
                        if (foundNode == node) {
                            foundKey = iKey;
                            break;
                        }
                    }
                }
                if (foundKey != null) {
                    childNodes.remove(foundKey);
                }
            }
            
            public void removeNodeOnlyByIndex(Integer nIndex) {
                if (this.childNodes.containsKey(nIndex)) {
                    childNodes.remove(nIndex);
                }
            }
           
            public BungeniBNode moveNodeAtIndexFromTo(Integer fromIndex, Integer toIndex) {
                BungeniBNode fromNode = null;
                BungeniBNode toNode = null;
                if (this.childNodes.containsKey(fromIndex)) {
                    fromNode = childNodes.get(fromIndex);
                }
                if (fromNode != null) {
                    if (childNodes.containsKey(toIndex)) {
                        toNode = childNodes.get(toIndex);
                    }
                    childNodes.put(toIndex, fromNode);
                    //free the from Index
                    this.childNodes.remove(fromIndex);
                }
                return toNode;
            }
            
            public BungeniBNode setNodeAtIndex(BungeniBNode anode, Integer iIndex){
                BungeniBNode oldNode = null;
                if (childNodes.containsKey(iIndex)) {
                    oldNode = childNodes.get(iIndex);
                }
                childNodes.put(iIndex, anode);
                if (!childNodeNames.containsKey(anode.getName())) {
                    childNodeNames.put(anode.getName(), anode);
                }
                return oldNode;
            }
            
            public BungeniBNode getNodeAtIndex(Integer iIndex) {
                BungeniBNode ret = null;
                if (childNodes.containsKey(iIndex)){
                    ret = childNodes.get(iIndex);
                }
                return ret;
            }
            
            public int getChildCount(){
                return childNodes.size();
            }
            
            public boolean hasChildren(){
                if (childNodes.size() > 0) 
                    return true;
                else
                    return false;
            }
            
            public boolean containsNodeByName(String name) {
                return childNodeNames.containsKey(name);
            }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String dText) {
        this.displayText = dText;
    }

    public int relativeIndexOfChild(BungeniBNode origNode) {
       Integer n= this.indexOfChild(origNode);
       int nIndex = -1;
       for (Integer nKey : childNodes.keySet()) {
           nIndex++; 
           if (nKey == n) {
               return nIndex;
           }
       }
       return -1;
    }
            
   }
      
