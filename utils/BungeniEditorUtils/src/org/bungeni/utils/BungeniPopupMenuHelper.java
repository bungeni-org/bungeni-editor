/*
 * BungeniPopupMenuHelper.java
 *
 * Created on October 19, 2007, 1:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.util.TreeMap;

/**
 *
 * @author Administrator
 */
public class BungeniPopupMenuHelper {
    
     private TreeMap<String,String> popupMenuMap = new TreeMap<String, String>();
       
    /** Creates a new instance of BungeniPopupMenuHelper */
    public BungeniPopupMenuHelper() {
    }
    
     public   BungeniPopupMenuHelper (String menu_name_to_load_from_settings) {
            //load the menu from settings, probably the db.
            if (menu_name_to_load_from_settings.equals("treeDocStructureTree")) {
                addItem("0_GOTO_SECTION", "Goto Section");
                addItem("1_ADD_PARA_BEFORE_SECTION", "Add Para Before Section");
                addItem("2_ADD_PARA_AFTER_SECTION", "Add Para After Section");
                addItem("3_DELETE_SECTION", "Remove This Section");
            } else if (menu_name_to_load_from_settings.equals("MoveSectionsDropMenu")) {
                addItem("0_MOVE_BEFORE", "Move Before Section");
                addItem("1_MOVE_AFTER", "Move After Section");
                addItem("2_MOVE_INSIDE", "Place Inside Section");
                addItem("3_CANCEL_ACTION", "Cancel");
            }
        }
        
     public void addItem(String menu_id, String text) {
            popupMenuMap.put(menu_id, text);
        }
        
    
     public TreeMap<String, String> getMenus() {
            return popupMenuMap;
        }
        
        
}
