/*
 * sectionHive.java
 *
 * Created on February 25, 2008, 12:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.dialogs.treetable;

/**
 *
 * @author undesa
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * 
 * sectionHive is a container class used by the TreeTableNode implementation 
 * (sectionHiveNode) to capture information stored at treetablenode level.
 * @author undesa
 */
public class sectionHive {
    
    public String sectionName;
    public String key ;
    public String value;
    
    public HashMap<String, String> keyValueMap = new HashMap<String,String>();
    public ArrayList<sectionHive> children = new ArrayList<sectionHive>();
    public sectionHive parentHive;
    /** Creates a new instance of sectionHive */
    public sectionHive(String sectionName) {
        this.sectionName = sectionName;
        this.key = "";
        this.value = "";
    }
    public sectionHive(String key, String value) {
        this.sectionName="";
        this.key = key;
        this.value = value;
    }
        
    public void setParent(sectionHive p) {
        parentHive = p;
    }
    
    public sectionHive getParent(){
        return parentHive;
    }
    public String getName() {
        if (this.sectionName.length() == 0 )  
            return key;
        else
            return sectionName;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public boolean isHeadless() {
        if (sectionName.length() == 0) 
            return true;
         else
            return false;
    }
    public void addToHive(String key, String value ) {
        keyValueMap.put(key, value);
    }
    public void addChildren(sectionHive childObj) {
        children.add(childObj);
    }
    
    public void addChildrenAt(sectionHive childObj, int index) {
        children.add(index, childObj);
    }
    public sectionHive[] getChildren() {
        return children.toArray(new sectionHive[children.size()]);
    }
    public boolean hasChildren(){
        if (children.size() > 0 )
            return true;
        else
            return false;
    }
    
    public String toString() {
        return this.getName();
    }

    public ArrayList<sectionHive> getChildrenVector() {
        return children;
    }
}

