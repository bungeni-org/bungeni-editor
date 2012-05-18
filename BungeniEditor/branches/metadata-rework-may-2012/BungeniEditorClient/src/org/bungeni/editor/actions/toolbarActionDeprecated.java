package org.bungeni.editor.actions;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.selectors.SelectorDialogModes;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Ashok Hariharan
 */
//###################################
//          WARNING                 #
// this class is deprecated         #
//###################################
// !+ACTION_RECONF (rm, jan 2012) - this class is being folded into
// toolbarSubAction

public class toolbarActionDeprecated {
    // !+ACTION_RECONF (rm, jan 2012) - these variables are unused and are commented
    /**
    public static String                   ACTION_TYPE_MARKUP  = "markup";
    public static String                   ACTION_TYPE_SECTION = "section";
    private static String                  ROOT_ACTION_DISPLAY = "Editing Actions";
    **/
    
    private static org.apache.log4j.Logger log                 =
        org.apache.log4j.Logger.getLogger(toolbarActionDeprecated.class.getName());
    private SelectorDialogModes            theMode             = null;

    // !+ACTION_CONFIG (rm, jan 2012) - added this variable with detined class
    // handling events
    // changed the action class
    // private String                         action_class = "org.bungeni.editor.actions.EditorActionHandler" ;
    private String                         action_class = "org.bungeni.editor.actions.EditorSelectionActionHandler" ;
    private String                         action_dialog_class;
    private String                         action_display_text;

    // !+ACTION_RECONF (rm, jan 2012) - variable deprecated since it is unused
    //private String                         action_edit_dlg_allowed;

    // private String action_parent;
   // private String        action_icon;
    private String        action_name;
    //AH-10-03-11
    /**
    private String        action_naming_convention;
    private String        action_numbering_convention;
     **/
    // !+ACTION_RECONF(ah, jan-2012) - deprecating action_order as it is not used
    //private String        action_order;

    //AH-10-03-11
    /**
    private String        action_section_type;
    **/
    private String        action_type;

    //!+ACTION_RECONF (rm, jan 2012) - this action is unused, deprecating it
    /**
    Vector<toolbarAction> containedActions;
    **/
    
    private String        doc_type;

    // !+ACTION_RECONF (rm, jan 2012) - this var is unused, deprecating it
    /**
    toolbarAction         parent;
    **/
    
    /** **act upon what ? *** */
    // !+ACTION_RECONF (rm, jan 2012) - this var is unused, deprecated
    /**
    private String selectedSection;
    **/
    
    /** ** and do what action ?*** */
    // !+ACTION_RECONF (rm, jan 2012) - this var is unused, deprecated
    /**
    private String selectedSectionActionCommand;
    **/
    
    /*
     * Used only for defining the root action
     */
    // !+ACTION_RECONF (rm, jan 2012) - this constructor is currently unused
    // it is deprecated
    /**
    public toolbarAction(String action) {
        if (action.equals("rootAction")) {
            parent              = null;
            action_name         = "editor_root";
            containedActions    = new Vector<toolbarAction>();
            action_display_text = ROOT_ACTION_DISPLAY;
        }

        if (action.equals("invisibleRootAction")) {
            parent              = null;
            action_name         = "invisible_root";
            containedActions    = new Vector<toolbarAction>();
            action_display_text = "INIVISIBLE ROOT";
        }

        if (action.equals("selectionAction")) {
            parent              = null;
            action_name         = "selection_root";
            containedActions    = new Vector<toolbarAction>();
            action_display_text = "Selection Actions";
        }
    }
    **/
    
    /** Creates a new instance of toolbarActionSettings */
    public toolbarActionDeprecated(Vector<String> actionDesc, HashMap action_mapping) {
        log.debug("in toolbarAction constructor");

        try {
            // !+ACTION_RECONF (rm, jan 2012) - this variable is unused, deprecated
            /**
            containedActions = new Vector<toolbarAction>();
            **/

            // !+ACTION_RECONF (rm, jan 2012) - var is unused, deprecated
            /**
            parent           = null;
            **/
            
            action_name      = (String) safeGet(actionDesc, action_mapping, "ACTION_NAME");

            // !+ACTION_RECONF(ah, jan-2012) - deprecating action_order as it is not used
            //action_order     = (String) safeGet(actionDesc, action_mapping, "ACTION_ORDER");

            // !+ACTION_RECONF (rm, jan 2012) - Setting the action_class var to a
            // value at declaration, rather than obtained from db at run time
            // action_class     = (String) safeGet(actionDesc, action_mapping, "ACTION_CLASS");            
            
            doc_type         = (String) safeGet(actionDesc, action_mapping, "DOC_TYPE");
            action_type      = (String) safeGet(actionDesc, action_mapping, "ACTION_TYPE");

            // action_parent = (String) safeGet(actionDesc, action_mapping, "ACTION_PARENT");
            // action_icon             = (String) safeGet(actionDesc, action_mapping, "ACTION_ICON");
            action_display_text     = (String) safeGet(actionDesc, action_mapping, "ACTION_DISPLAY_TEXT");
            //AH-10-03-11
            /**
            action_section_type     = (String) safeGet(actionDesc, action_mapping, "ACTION_SECTION_TYPE");
            **/

            // !+ACTION_RECONF (rm, jan 2012) - variable initialization deprecated since the
            // variable action_edit_dlg_allowed is unused
            //action_edit_dlg_allowed = (String) safeGet(actionDesc, action_mapping, "ACTION_EDIT_DLG_ALLOWED");

            // !+ACTION_RECONF (rm, jan 2012) - changing statement to reflect the new schema for
            //  the table ACTION_SETTINGS2 that hosts both ACTION_SETTINGS and SUB_ACTION_SETTINGS
             action_dialog_class     = (String) safeGet(actionDesc, action_mapping, "ACTION_DIALOG_CLASS");
            //action_dialog_class     = (String) safeGet(actionDesc, action_mapping, "DIALOG_CLASS");

            // the below are deprecated fields - and no longer part of the action_settings table
            // this info is fetched instead from the document_section_types tbale
           //AH-10-03-11
            /**
            DocumentSection associatedSection = DocumentSectionsContainer.getDocumentSectionByType(action_section_type);
            if (associatedSection != null ) {
                action_naming_convention = associatedSection.getSectionNamePrefix();    // (String) safeGet(actionDesc, action_mapping, "ACTION_NAMING_CONVENTION");
                action_numbering_convention = associatedSection.getSectionNumberingStyle();    // (String) safeGet(actionDesc, action_mapping, "ACTION_NUMBERING_CONVENTION");
             }**/
            
        } catch (Exception e) {
            log.error("error in toolbarAction constructor : " + e.getMessage());
           
        }

        log.debug("finished toolbarAction constructor");
    }

    public String action_name() {
        return this.action_name;
    }

    public String action_class() {
        return this.action_class;
    }

    public String action_display_text() {
        return this.action_display_text;
    }

    //AH-10-03-11
    /**
    public String action_naming_convention() {
        return this.action_naming_convention;
    }

   
    public String action_numbering_convention() {
        return this.action_numbering_convention;
    }
    **/
    
        public String action_type() {
        return this.action_type;
    }

    //AH-10-03-11 -- REMOVED TO toolbarSubAction
    /**
    public String action_section_type() {
        return this.action_section_type;
    }
    **/

    // !+ACTION_RECONF(ah, jan-2012)
    /*
    public int action_order() {
        return Integer.parseInt(this.action_order);
    }
    */

    // !+ACTION_RECONF (rm, jan 2012) - this method is currently
    // unused. Deprecated as this class is collapsed into
    // toolbarSubAction
    /**
    public int action_edit_dlg_allowed() {
        return Integer.parseInt(this.action_edit_dlg_allowed);
    }
    **/
    
    public String action_dialog_class() {
        if (action_dialog_class == null) {
            return "";
        } else {
            return this.action_dialog_class;
        }
    }

    /**
     *
     * @param theFather
     * @param childActions
     */
    /**
     * !+UNUSED_CLASS(ah, jan-2012)
    public static void makeLinktoChildren(toolbarAction theFather, toolbarAction[] childActions) {
        for (toolbarAction childAction : childActions) {
            theFather.containedActions.addElement(childAction);
            childAction.parent = theFather;
        }
    }
    **/
    @Override
    public String toString() {
        return this.action_display_text;
    }

    public boolean isTopLevelAction() {
        if (action_name().equals("editor_root") || action_name().equals("parent")
                || action_name().equals("selection_root")) {
            return true;
        } else {
            return false;
        }
    }

    // !+ACTION_RECONF (rm, jan 2012) - this method is unused, deprecated
    /**
    public toolbarAction getParent() {
        return parent;
    }
    **/

    // !+ACTION_RECONF (rm, jan 2012) - these methods are unused, deprecated
    // as this class is collapsed into toolbarSubAction
    /**
    public int getContainedActionsCount() {
        return containedActions.size();
    }
    
    
    public toolbarAction getContainedActionAt(int i) {
        return containedActions.elementAt(i);
    }


    public int getIndexOfContainedAction(toolbarAction childAction) {
        return containedActions.indexOf(childAction);
    }
    
    
    public String getSelectedSectionToActUpon() {
        return this.selectedSection;
    }
    
    
    public String getSelectedSectionActionCommand() {
        return this.selectedSectionActionCommand;
    }

    public void setSelectedActionToActUpon(String sect) {
        this.selectedSection = sect;
    }
        
    public void setSelectedSectionActionCommand(String cmd) {
        this.selectedSectionActionCommand = cmd;
    }    

    public void brains() {
        System.out.println("action_name = " + action_name);

        // System.out.println("action_parent = " + action_parent);
    }
    **/
    
    private Object safeGet(Vector<String> actions, HashMap map, String key) {
        Object o = null;

        if (map.containsKey(key)) {
            log.debug("safeGet: key matched - " + key);

            Integer column = (Integer) map.get(key);

            log.debug("safeGet: column matched - " + column);
            o = actions.elementAt(column - 1);

            if (o == null) {
                log.debug("Key was found but vector returned null : " + key);
            }

            return o;
        } else {
            log.debug("Key : " + key + " was not found");

            return null;
        }
    }

    public void setSelectorDialogMode(SelectorDialogModes mode) {
        theMode = mode;
    }

    public SelectorDialogModes getSelectorDialogMode() {
        return theMode;
    }
}
