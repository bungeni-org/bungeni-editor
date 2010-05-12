/*
 * DocStructureTreeNode.java
 *
 * Created on July 5, 2007, 12:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class DocStructureTreeNode {
    private String name;
    private int level;
    private int count;
    DocStructureTreeNode parent;
    Vector<DocStructureTreeNode> children;
    
    /** Creates a new instance of DocStructureTreeNode */
    public DocStructureTreeNode(String nodeName) {
        name = nodeName;
        parent = null;
        children = new Vector<DocStructureTreeNode>();
        
    }
    
    public DocStructureTreeNode(String nodeName, int nLevel, int nCount) {
        this.name = nodeName;
        this.level = nLevel;
        this.count = nCount;
        this.parent = null;
        this.children = new Vector<DocStructureTreeNode>();
        
    }
    public static void makeRelation (DocStructureTreeNode newParent, DocStructureTreeNode[] newChildrens){
        
        for (DocStructureTreeNode newChildren: newChildrens){
            newParent.children.addElement(newChildren);
            newChildren.parent = newParent;
        }
    }
    
    public DocStructureTreeNode getChildAt(int i){
        return (DocStructureTreeNode) children.elementAt(i);
    }
    
    public DocStructureTreeNode getParent(){
        return parent;
    }
    
    public int getLevel(){
        return this.level;
    }
    public String toString(){
        return name;
        
    }
    public int getChildCount(){
        return children.size();
    }
    
    
    public int getIndexOfChild(DocStructureTreeNode child){
        //returns -1 if not found
        return children.indexOf(child);
    }
    
}
