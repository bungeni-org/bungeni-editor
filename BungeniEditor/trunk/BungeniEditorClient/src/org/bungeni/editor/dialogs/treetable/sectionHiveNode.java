/*
 * sectionHiveNode.java
 *
 * Created on February 22, 2008, 10:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.dialogs.treetable;
import org.bungeni.utils.IteratorAsEnumeration;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;
import java.awt.*;
import org.jdesktop.swingx.treetable.TreeTableNode;
/**
 * 
 * TreeTableNode implementation used by The section metadata view panel
 * @author undesa
 */
public class sectionHiveNode implements TreeTableNode {
        private sectionHive hive;
        private sectionHiveNode parent;
        private ArrayList<TreeTableNode> children = new ArrayList<TreeTableNode>();
    /** Creates a new instance of sectionHiveNode */
         public sectionHiveNode(sectionHive thehive, sectionHiveNode aParent) {
             hive = thehive;
             parent = aParent;
             if (hive.hasChildren()) {
                 for (sectionHive obj : hive.getChildren()) {
                     children.add(new sectionHiveNode(obj, this));
                 }
            }
        }
         
    
    public TreeTableNode getChildAt(int i) {
       return children.get(i);
    }

    public int getChildCount() {
       return children.size();
    }

    public TreeTableNode getParent() {
       return this.parent;
    }

    public sectionHive getHive() {
        return hive;
    }
    public int getIndex(TreeNode treeNode) {
       return children.indexOf((TreeTableNode)treeNode);
    }

    public boolean getAllowsChildren() {
        if (hive.sectionName.length() == 0 )
            return false;
        else
            return true;
    }

    public boolean isLeaf() {
        return hive.isHeadless();
    }

    public Enumeration<org.jdesktop.swingx.treetable.TreeTableNode> children() {
        return (Enumeration<org.jdesktop.swingx.treetable.TreeTableNode>)new IteratorAsEnumeration(children.iterator());
    }
    
    @Override
    public String toString()
    {
            return hive.getName();
    }

    public Object getValueAt(int arg0) {
        return ((sectionHiveNode)children.get(arg0)).getHive().getName();
        //throw new UnsupportedOperationException("Not supported yet.");
    }


    public int getColumnCount() {
        return 1;
    }

 

    public boolean isEditable(int arg0) {
        return false;
    }

    public void setValueAt(Object arg0, int arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUserObject() {
       Object s=new Object();
       return s;
    }

    public void setUserObject(Object object) {
    }


}
