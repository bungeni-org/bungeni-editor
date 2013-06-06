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
import org.bungeni.extutils.CommonDocumentUtilFunctions;

/**
 *
 * @author undesa
 */
public class routerCreateUniqueReference extends defaultRouter {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateUniqueReference.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerCreateUniqueReference() {
        super();
    }
    
    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        BungeniValidatorState bRet = new BungeniValidatorState(true, new BungeniMsg("FAILURE"));
        try {
        String uniqueReferenceName = CommonDocumentUtilFunctions.getUniqueReferenceName(subAction.action_value()+ ":", ooDocument);
        routerCreateReference rcf = new routerCreateReference();
        subAction.setActionValue(uniqueReferenceName);
        
        BungeniValidatorState bvs = rcf.route_TextSelectedInsert(subAction, pFrame, ooDocument);
        // BungeniValidatorState bvs = rcf.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);

        bRet = bvs;
        } catch (Exception ex) {
            log.error("routerCreateUniqueReference : route_TextSelectedInsert : " + ex.getMessage());
        } finally {
            return bRet;
        }
        //subAction value has the reference name
       //this.insertRefMark(ooDocument, xCursor, subAction.action_value());
    }

}
