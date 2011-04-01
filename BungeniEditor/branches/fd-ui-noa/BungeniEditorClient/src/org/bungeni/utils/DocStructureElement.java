/*
 * DocStructureElement.java
 *
 * Created on July 19, 2007, 1:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import com.sun.star.text.XTextRange;

/**
 *
 * @author Administrator
 */
public class DocStructureElement {
   private String name="";
    private int level=0;
    private int count=0;
    boolean hasChild=false;
    private XTextRange textRange;
    private String __padding = "-";
    
    /** Creates a new instance of DocStructureElement */
    public DocStructureElement() {
    }

    public DocStructureElement(String name, int level, int count, XTextRange range) {
        this.name = name;
        this.level = level;
        this.count = count;
        this.textRange = range;
    }

    public void hasChildren(boolean value){
        this.hasChild = value;
    }
    public XTextRange getRange(){
        return this.textRange;
    }
    public boolean hasChildren(){
        return hasChild;
    }
    
    public int getLevel(){
        return this.level;
    }
    
    public String toString(){
        String paddedElement = name;
        for (int i=0; i <= level; i++){
            paddedElement = __padding + paddedElement;
        }
        if (hasChild) paddedElement = "-"+paddedElement;
        return paddedElement;
    }
    
}
