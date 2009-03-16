/*
 * validateCreateSection.java
 *
 * Created on March 3, 2008, 8:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.bungeni.editor.actions.validators;

import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;



/**
 *
 * @author Administrator
 */
public class defaultValidator implements IBungeniActionValidator {
   protected BungeniClientDB dbSettings;
 
    /** Creates a new instance of validateCreateSection */
    public defaultValidator() {
          dbSettings = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
    }

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

 }
