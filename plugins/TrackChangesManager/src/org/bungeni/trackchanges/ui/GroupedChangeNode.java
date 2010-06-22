package org.bungeni.trackchanges.ui;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Ashok Hariharan
 */
public class GroupedChangeNode extends DefaultMutableTreeNode {
   

    public GroupedChangeNode(GroupedChange gchangeNode){
        super(gchangeNode);
    }

    public GroupedChangeNode(String sType, String sname, String sid, DocumentChange docChange) {
        super(new GroupedChange(sType, sname, sid, docChange));
    }
    
}
