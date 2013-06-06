/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * This is the action router base class, used for routing actions from the editor application into the openoffice editor
 * @author Administrator
 */
// !+ACTION_RECONF (rm, jan 2012) - the interface methods implemented
// have been reimplemeted to exclude the use of toolbarAction as an arg
public class defaultRouter implements IBungeniActionRouter {
    //protected BungeniClientDB dbSettings;
    /**
     * default constructor, inititlizaed from the base class via super();
     */
    public defaultRouter() {
          //dbSettings = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
    }

    /**
     * This is the main action router switch construtor. It is almost never neccessary to override this, instead overrid the action specific events called from the switch case
     * @param action
     * @param subAction
     * @param pFrame
     * @param ooDoc
     * @return
     */

    //!+ACTION_RECONF (rm, jan 2012) - reimplementing method to not use toolbarAction
    /**
    public BungeniValidatorState route(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDoc) {
      switch(subAction.getSelectorDialogMode()) {
            case TEXT_INSERTION:
                return route_FullInsert(action, subAction, pFrame, ooDoc);
            case DOCUMENT_LEVEL_ACTION:
                return route_DocumentLevelAction(action, subAction, pFrame,  ooDoc);
            case TEXT_EDIT:
                return route_FullEdit(action, subAction, pFrame,  ooDoc);
            case TEXT_SELECTED_EDIT:
                return route_TextSelectedEdit(action, subAction, pFrame,  ooDoc);
            case TEXT_SELECTED_INSERT:
                return route_TextSelectedInsert(action, subAction,  pFrame, ooDoc);
            default:
                return new BungeniValidatorState(false, new BungeniMsg("DIALOG_MODE_MISSING"));
        }
    }
    **/
    // public BungeniValidatorState route(toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDoc) {
    public BungeniValidatorState route(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDoc) {
      switch(subAction.getSelectorDialogMode()) {
            case TEXT_INSERTION:
                return route_FullInsert(subAction,pFrame, ooDoc);
            case DOCUMENT_LEVEL_ACTION:
                return route_DocumentLevelAction(subAction, pFrame,  ooDoc);
            case TEXT_EDIT:
                return route_FullEdit(subAction, pFrame,  ooDoc);
            case TEXT_SELECTED_EDIT:
                return route_TextSelectedEdit(subAction, pFrame,  ooDoc);
            case TEXT_SELECTED_INSERT:
                return route_TextSelectedInsert(subAction,  pFrame, ooDoc);
            default:
                return new BungeniValidatorState(false, new BungeniMsg("DIALOG_MODE_MISSING"));
        }
    }

    // !+ACTION_RECONF (rm, jan 2012) - reimplemeting these methods to drop toolbarAction
    // as an argument
    public BungeniValidatorState route_DocumentLevelAction(toolbarAction subAction, javax.swing.JFrame pFrame,  OOComponentHelper ooDocument) {
       return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
       return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    public BungeniValidatorState route_TextSelectedEdit(toolbarAction subAction,javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
       return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    public BungeniValidatorState route_FullInsert(toolbarAction subAction,javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
       return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    public BungeniValidatorState route_FullEdit(toolbarAction subAction,javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
       return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }    
}
