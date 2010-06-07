package org.bungeni.trackchanges.ui;

import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Ashok Hariharan
 */
public class GroupedChangesTreeModel extends DefaultTreeModel {

    public GroupedChangesTreeModel(GroupedChangeNode rootNode){
        super(rootNode);
    }

}
