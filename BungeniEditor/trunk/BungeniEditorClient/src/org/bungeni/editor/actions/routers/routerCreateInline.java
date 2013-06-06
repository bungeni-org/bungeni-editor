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
import org.bungeni.editor.actions.toolbarAction.actionSourceOrigin;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Applys a character style to a block of selected text
 * @author Ashok Hariharan
 */
public class routerCreateInline extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateInline.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerCreateInline() {
        super();
        
    }

    @Override
    public BungeniValidatorState route_TextSelectedInsert( toolbarAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
        // check if the action supports an inline Type markup
        boolean bState = false ; 
        if (subAction.getActionSource().equals(actionSourceOrigin.inlineType)) {
          String inlineTypeName = subAction.getInlineType();
          HashMap<String,String> inlineTypeMap = new HashMap<String,String>();
          inlineTypeMap.putAll(CommonRouterActions.get_newInlineMetadata(subAction));
          ooDocument.setSelectedTextAttributes(inlineTypeMap);
          bState = true;
        }
      if (bState) {
            return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
        } 
      else {
            return new BungeniValidatorState(true, new BungeniMsg("FAILURE"));
        } 
    }

}
