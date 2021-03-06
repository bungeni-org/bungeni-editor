package org.bungeni.editor.actions;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.selectors.SelectorDialogModes;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class toolbarSubAction {
    private static org.apache.log4j.Logger log      =
        org.apache.log4j.Logger.getLogger(toolbarSubAction.class.getName());
  //  private String[]                       profiles = null;
    private SelectorDialogModes            theMode  = null;
    private String                         action_class;
    private String                         action_display_text;
    private String                         action_type;
    private String                         action_value;
    
    private String                         dialog_class;
    private String                         doc_type;
    private String                         parent_action_name;
    private String                         router_class;
    private String                         sub_action_name;
    private String                         sub_action_order;
    private String                         sub_action_state;
   
    private String                         validator_class;

    /** Creates a new instance of toolbarSubAction */
    public toolbarSubAction(Vector<String> actionDesc, HashMap action_mapping) {
        this.sub_action_name     = (String) safeGet(actionDesc, action_mapping, "SUB_ACTION_NAME");
        this.sub_action_order    = (String) safeGet(actionDesc, action_mapping, "SUB_ACTION_ORDER");
        this.doc_type            = (String) safeGet(actionDesc, action_mapping, "DOC_TYPE");
        this.action_type         = (String) safeGet(actionDesc, action_mapping, "ACTION_TYPE");
        this.sub_action_state    = (String) safeGet(actionDesc, action_mapping, "SUB_ACTION_STATE");
        this.parent_action_name  = (String) safeGet(actionDesc, action_mapping, "PARENT_ACTION_NAME");
        this.action_display_text = (String) safeGetString(actionDesc, action_mapping, "ACTION_DISPLAY_TEXT");
        this.action_class        = (String) safeGetString(actionDesc, action_mapping, "ACTION_CLASS");
        this.validator_class     = (String) safeGetString(actionDesc, action_mapping, "VALIDATOR_CLASS");
        this.router_class        = (String) safeGetString(actionDesc, action_mapping, "ROUTER_CLASS");
        this.dialog_class        = (String) safeGetString(actionDesc, action_mapping, "DIALOG_CLASS");
        /*
        String sProfiles = (String) safeGetString(actionDesc, action_mapping, "PROFILES");

        this.profiles = sProfiles.split(",");

        for (int i = 0; i < profiles.length; i++) {
            profiles[i] = profiles[i].trim();
        }*/

       // buildCommandChain((String) safeGet(actionDesc, action_mapping, "COMMAND_CHAIN"));
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
        } else {
            return ((String) o).trim();
        }
    }

    private Object safeGet(Vector<String> actions, HashMap map, String key) {
        Object o = null;

        if (map.containsKey(key)) {
            log.debug("safeGet: key matched - " + key);

            Integer column = (Integer) map.get(key);

            log.debug("safeGet: column matched - " + column);
            o = actions.elementAt(column - 1);

            if (o == null) {
                log.debug("Key was found but vector reqturned null : " + key);
            }

            return o;
        } else {
            log.debug("Key : " + key + " was not found");

            return null;
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



    public String validator_class() {
        return this.validator_class;
    }

    public String router_class() {
        return this.router_class;
    }

    public String dialog_class() {
        return this.dialog_class;
    }

    public String action_value() {
        return this.action_value;
    }

   
/*
    public String[] profiles() {
        return this.profiles;
    }

    public boolean hasProfile(String pProfile) {
        for (int i = 0; i < profiles.length; i++) {
            if (pProfile.equals(profiles[i])) {
                return true;
            }
        }

        return false;
    }*/



    public void setActionValue(String value) {
        this.action_value = value;
    }

    public void setSelectorDialogMode(SelectorDialogModes mode) {
        theMode = mode;
    }

    public SelectorDialogModes getSelectorDialogMode() {
        return theMode;
    }
}
