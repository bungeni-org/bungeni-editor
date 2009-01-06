/*
 * toolbarSubAction.java
 *
 * Created on November 28, 2007, 10:19 PM
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
public class toolbarSubAction {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(toolbarSubAction.class.getName());
 
    private String doc_type;
    private String parent_action_name;
    private String sub_action_name;
    private String sub_action_order;
    private String sub_action_state;
    private String action_type;
    private String action_display_text;
    private String action_fields;
    private String action_class;
    private String system_container;
    private String action_value;
    private String validator_class;
    private String router_class;
    private String dialog_class;
    private String command_chain;
    private String command_catalog;
    private SelectorDialogModes theMode=null;
    /** Creates a new instance of toolbarSubAction */
    public toolbarSubAction(Vector<String> actionDesc, HashMap action_mapping) {
        
        this.sub_action_name = (String) safeGet(actionDesc, action_mapping, "SUB_ACTION_NAME");
        this.sub_action_order = (String) safeGet(actionDesc, action_mapping, "SUB_ACTION_ORDER");
        this.doc_type = (String) safeGet(actionDesc, action_mapping, "DOC_TYPE");
        this.action_type = (String) safeGet(actionDesc, action_mapping, "ACTION_TYPE");
        this.sub_action_state = (String) safeGet(actionDesc, action_mapping, "SUB_ACTION_STATE");
        this.parent_action_name = (String) safeGet(actionDesc, action_mapping, "PARENT_ACTION_NAME");
        this.action_fields = (String) safeGet(actionDesc, action_mapping, "ACTION_FIELDS");
        this.action_display_text = (String) safeGetString(actionDesc, action_mapping, "ACTION_DISPLAY_TEXT");
        this.action_class = (String) safeGetString(actionDesc, action_mapping, "ACTION_CLASS");
        this.system_container = (String) safeGetString(actionDesc, action_mapping, "SYSTEM_CONTAINER");
        this.validator_class = (String)safeGetString(actionDesc, action_mapping, "VALIDATOR_CLASS");
        this.router_class = (String)safeGetString(actionDesc, action_mapping, "ROUTER_CLASS");
        this.dialog_class = (String)safeGetString(actionDesc, action_mapping, "DIALOG_CLASS");
        buildCommandChain((String)safeGet(actionDesc, action_mapping, "COMMAND_CHAIN"));
    }
    
    @Override
    public String toString() {
        return this.action_display_text();
    }
 
    private String safeGetString(Vector<String> actions, HashMap map, String key) {
        Object o = null;
        o = safeGet(actions, map, key);
        if (o == null) {
            return null;
        } else 
            return ((String)o).trim();
    }
    private Object safeGet (Vector<String> actions, HashMap map, String key){
        Object o = null ;
        if (map.containsKey(key)) 
        {
            log.debug("safeGet: key matched - "+ key);
            Integer column = (Integer) map.get(key);
            log.debug("safeGet: column matched - "+ column);
            o = actions.elementAt(column-1);
            if (o == null ) log.debug("Key was found but vector reqturned null : " + key);
            return o;
        }
        else {
             log.debug("Key : "+ key + " was not found");
            return null;
        }
            
    }
    

    private void buildCommandChain(String chain ) {
        if (chain == null ) {
            log.debug("buildCommandChain: chain is null");
            this.command_catalog = "";
            this.command_chain = "";
            return;
        } 
        if (chain.length() == 0 ) {
            log.debug("buildCommandChain: chain length is 0");
            this.command_catalog = "";
            this.command_chain = "";
            return;
        }
        if (chain.indexOf(":") != -1) {
            log.debug("buildCommandChain: chain has index :");
            String[] arrChain = chain.split("[:]");
            this.command_catalog = arrChain[0];
            this.command_chain = arrChain[1];
        }   else {
            this.command_catalog = "";
            this.command_chain = "";
        } 
    }
    
    public String doc_type() {
        return doc_type;
    }

    public String parent_action_name() {
        return parent_action_name;
    }

  
    public String sub_action_name() {
        return sub_action_name;
    }

    public String action_class() {
        return action_class;
    }
  
    public String sub_action_order() {
        return sub_action_order;
    }

  
    public String sub_action_state() {
        return sub_action_state;
    }

  
    public String action_type() {
        return action_type;
    }

  
    public String action_display_text() {
        return action_display_text;
    }

  
    public String action_fields() {
        return action_fields;
    }
    
    public String system_container() {
        return this.system_container;
    }
    
    public String validator_class(){
        return this.validator_class;
    }
    
    public String router_class(){
        return this.router_class;
    }
    
    public String dialog_class(){
        return this.dialog_class;
    }
    public String action_value(){
        return this.action_value;
    }

    public String action_command_chain(){
        return this.command_chain;
    }
    
    public String action_command_catalog(){
        return this.command_catalog;
    }
    public void setActionValue(String value) {
        this.action_value = value;
    }
    public void setSelectorDialogMode (SelectorDialogModes mode) {
        theMode = mode;
    }
    
    public SelectorDialogModes getSelectorDialogMode(){
        return theMode;
    }
}
