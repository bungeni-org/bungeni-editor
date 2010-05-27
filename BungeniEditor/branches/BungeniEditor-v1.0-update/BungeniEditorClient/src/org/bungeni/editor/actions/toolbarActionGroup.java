/*
 * toolbarActionGroup.java
 *
 * Created on August 30, 2007, 7:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions;

import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class toolbarActionGroup {
    String groupTitle;
    String groupName;
    Vector<toolbarAction> actions = new Vector<toolbarAction>();
    
    /** Creates a new instance of toolbarActionGroup */
    public toolbarActionGroup(String groupName, String groupTitle) {
        this.groupName = groupName;
        this.groupTitle = groupTitle;
    }
    
    public void addAction(toolbarAction action) {
        actions.add(action);
    }
    
    public void removeAllActions() {
        actions.removeAllElements();
    }
    
    public String toString() {
        return groupTitle;
    }
}
