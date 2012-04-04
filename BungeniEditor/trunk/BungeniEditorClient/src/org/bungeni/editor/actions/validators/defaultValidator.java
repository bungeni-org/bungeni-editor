
package org.bungeni.editor.actions.validators;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;



/**
 *
 * @author Administrator
 */
// !+ ACTION_ReCONF (rm, jan 2012) - toolbarAction has been deprecated,
// all functionality now within subActionToolbar
public class defaultValidator implements IBungeniActionValidator {
  
    /** Creates a new instance of validateCreateSection */
    public defaultValidator() {
        
    }

    /**
    public BungeniValidatorState check(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument){
        switch(subAction.getSelectorDialogMode()) {
            case TEXT_INSERTION:
                return check_FullInsert(action, subAction, ooDocument);
            case DOCUMENT_LEVEL_ACTION:
                return check_DocumentLevelAction(action, subAction, ooDocument);
            case TEXT_EDIT:
                return check_FullEdit(action, subAction, ooDocument);
            case TEXT_SELECTED_EDIT:
                return check_TextSelectedEdit(action, subAction, ooDocument);
            case TEXT_SELECTED_INSERT:
                return check_TextSelectedInsert(action, subAction, ooDocument);
            default:
                return new BungeniValidatorState(false, new BungeniMsg("DIALOG_MODE_MISSING"));
        }
    }
    public BungeniValidatorState check_DocumentLevelAction(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) {
           return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    public BungeniValidatorState check_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) {
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    public BungeniValidatorState check_TextSelectedEdit(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) {
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    public BungeniValidatorState check_FullInsert(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) {
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    public BungeniValidatorState check_FullEdit(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument) {
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
    **/

    public BungeniValidatorState check(toolbarAction subAction, OOComponentHelper ooDocument){
        switch(subAction.getSelectorDialogMode()) {
            case TEXT_INSERTION:
                return check_FullInsert(subAction, ooDocument);
            case DOCUMENT_LEVEL_ACTION:
                return check_DocumentLevelAction(subAction, ooDocument);
            case TEXT_EDIT:
                return check_FullEdit(subAction, ooDocument);
            case TEXT_SELECTED_EDIT:
                return check_TextSelectedEdit(subAction, ooDocument);
            case TEXT_SELECTED_INSERT:
                return check_TextSelectedInsert(subAction, ooDocument);
            default:
                return new BungeniValidatorState(false, new BungeniMsg("DIALOG_MODE_MISSING"));
        }
    }
    public BungeniValidatorState check_DocumentLevelAction(toolbarAction subAction, OOComponentHelper ooDocument) {
           return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }

    public BungeniValidatorState check_TextSelectedInsert(toolbarAction subAction, OOComponentHelper ooDocument) {
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }

    public BungeniValidatorState check_TextSelectedEdit(toolbarAction subAction, OOComponentHelper ooDocument) {
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }

    public BungeniValidatorState check_FullInsert(toolbarAction subAction, OOComponentHelper ooDocument) {
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }

    public BungeniValidatorState check_FullEdit(toolbarAction subAction, OOComponentHelper ooDocument) {
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }

 }
