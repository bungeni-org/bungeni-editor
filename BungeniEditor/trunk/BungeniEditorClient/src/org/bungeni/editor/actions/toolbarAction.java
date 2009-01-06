/*
 * toolbarActionSettings.java
 *
 * Created on August 17, 2007, 5:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions;

import java.util.HashMap;
import java.util.Vector;
import org.bungeni.editor.selectors.SelectorDialogModes;

/**
 *
 * @author Administrator
 */
public class toolbarAction {
    toolbarAction parent;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(toolbarAction.class.getName());
    Vector<toolbarAction> containedActions;
    private static String ROOT_ACTION_DISPLAY="Editing Actions";
    public static String ACTION_TYPE_SECTION="section";
    public static String ACTION_TYPE_MARKUP = "markup";
         
    private String action_name;
    private String action_order;
    private String action_class;
    private String doc_type;
    private String action_type;
    private String action_naming_convention;
    private String action_numbering_convention;
   // private String action_parent;
    private String action_icon;
    private String action_display_text;
    private String action_dimension;
    private String action_section_type;
    private String action_edit_dlg_allowed;
    private String action_dialog_class;
    private SelectorDialogModes theMode = null;
    
    /****act upon what ? *****/
    private String selectedSection;
    /**** and do what action ?*****/
    private String selectedSectionActionCommand;
    
    /** Creates a new instance of toolbarActionSettings */
    public toolbarAction(Vector<String> actionDesc, HashMap action_mapping) {
        log.debug("in toolbarAction constructor");
        try {
        
        containedActions = new Vector<toolbarAction>();     
        parent = null;
        
        action_name = (String) safeGet(actionDesc, action_mapping, "ACTION_NAME");
        action_order = (String) safeGet(actionDesc, action_mapping, "ACTION_ORDER");
        action_class = (String) safeGet(actionDesc, action_mapping, "ACTION_CLASS");
        doc_type = (String) safeGet(actionDesc, action_mapping, "DOC_TYPE");
        action_type = (String) safeGet(actionDesc, action_mapping, "ACTION_TYPE");
        action_naming_convention = (String) safeGet(actionDesc, action_mapping, "ACTION_NAMING_CONVENTION");
        action_numbering_convention = (String) safeGet(actionDesc, action_mapping, "ACTION_NUMBERING_CONVENTION");
     //   action_parent = (String) safeGet(actionDesc, action_mapping, "ACTION_PARENT");
        action_icon = (String) safeGet(actionDesc, action_mapping, "ACTION_ICON");
        action_display_text = (String) safeGet(actionDesc, action_mapping, "ACTION_DISPLAY_TEXT");
        action_dimension = (String) safeGet(actionDesc, action_mapping, "ACTION_DIMENSION");
        action_section_type = (String) safeGet(actionDesc, action_mapping, "ACTION_SECTION_TYPE");
        action_edit_dlg_allowed = (String) safeGet(actionDesc, action_mapping, "ACTION_EDIT_DLG_ALLOWED");
        action_dialog_class = (String) safeGet(actionDesc, action_mapping, "ACTION_DIALOG_CLASS");
        } catch (Exception e) {
            log.debug("error in toolbarAction constructor : " + e.getMessage());
            e.printStackTrace();
        }
        log.debug("finished toolbarAction constructor");
    }
    
    public String action_name() {
        return this.action_name;
    }
    
    public String action_class() {
        return this.action_class;
    }
    
    public String action_display_text(){
        return this.action_display_text;
    }
    
    public String action_naming_convention(){
        return this.action_naming_convention;
    }
    
    public String action_numbering_convention(){
        return this.action_numbering_convention;
    }
    
    public String action_type(){
        return this.action_type;
    }
    
    public String action_section_type() {
        return this.action_section_type;
    }
    
    public int action_order() {
       return Integer.parseInt(this.action_order);
    }
    
    public int action_edit_dlg_allowed(){
        return Integer.parseInt(this.action_edit_dlg_allowed);
    }
    
    public String action_dialog_class(){
        if (action_dialog_class == null)
            return new String("");
        else
            return this.action_dialog_class;
    }
    
    /*
     *Used only for defining the root action
     */
    public toolbarAction(String action) {
        if (action.equals("rootAction")){
            parent = null;
            action_name="editor_root";
            containedActions = new Vector<toolbarAction>();
            action_display_text = ROOT_ACTION_DISPLAY;
        }
        if (action.equals("invisibleRootAction")) {
            parent=null;
            action_name="invisible_root";
            containedActions = new Vector<toolbarAction>();
            action_display_text = "INIVISIBLE ROOT";
        }
        if (action.equals("selectionAction")) {
            parent=null;
            action_name="selection_root";
            containedActions = new Vector<toolbarAction>();
            action_display_text = "Selection Actions";
        }
    }
    
    public static void makeLinktoChildren(toolbarAction theFather,
                                            toolbarAction[] childActions) {
        for (toolbarAction childAction : childActions) {
            theFather.containedActions.addElement(childAction);
            childAction.parent = theFather;
        }
    }

   public String toString() {
       return this.action_display_text;
   } 
   
   public boolean isTopLevelAction() {
            if (action_name().equals("editor_root") || 
                action_name().equals("parent") || 
                action_name().equals("selection_root"))
                return true;
            else 
                return false;
   }
   
   public toolbarAction getParent() {
       return parent;
   }
   
   public int getContainedActionsCount() {
       return containedActions.size();
   }
   
   public toolbarAction getContainedActionAt(int i) {
        return containedActions.elementAt(i);
   }
   
   public int getIndexOfContainedAction(toolbarAction childAction) {
       return containedActions.indexOf(childAction);
   }
   
   public String getSelectedSectionToActUpon(){
       return this.selectedSection;
   }
   
   public String getSelectedSectionActionCommand(){
       return this.selectedSectionActionCommand;
   }
   
   public void setSelectedActionToActUpon(String sect){
       this.selectedSection = sect;
   }
   
   public void setSelectedSectionActionCommand(String cmd){
        this.selectedSectionActionCommand = cmd;
   }
   
    public void brains() {
        System.out.println("action_name = "+ action_name);
       // System.out.println("action_parent = " + action_parent);
    }
    private Object safeGet (Vector<String> actions, HashMap map, String key){
        Object o = null ;
        if (map.containsKey(key)) 
        {
            log.debug("safeGet: key matched - "+ key);
            Integer column = (Integer) map.get(key);
            log.debug("safeGet: column matched - "+ column);
            o = actions.elementAt(column-1);
            if (o == null ) log.debug("Key was found but vector returned null : " + key);
            return o;
        }
        else {
             log.debug("Key : "+ key + " was not found");
            return null;
        }
            
    }
    
    public void setSelectorDialogMode (SelectorDialogModes mode) {
        theMode = mode;
    }
    
    public SelectorDialogModes getSelectorDialogMode(){
        return theMode;
    }
}
