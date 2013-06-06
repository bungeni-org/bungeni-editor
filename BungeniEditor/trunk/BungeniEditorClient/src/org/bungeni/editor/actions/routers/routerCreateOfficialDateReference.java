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

import java.util.HashMap;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Marks up the official date
 * @author Ashok Hariharan
 */
public class routerCreateOfficialDateReference extends defaultRouter {
    public routerCreateOfficialDateReference(){
        super();
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        HashMap<String,String> fromMap = new HashMap<String,String>(){
            {
                put("DateRef","BungeniOfficialDate");
            }
        };
        ooDocument.setSelectedTextAttributes(fromMap);
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
}
