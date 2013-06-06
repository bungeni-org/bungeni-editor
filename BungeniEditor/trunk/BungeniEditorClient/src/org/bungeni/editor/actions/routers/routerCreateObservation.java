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

import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextViewCursor;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public class routerCreateObservation extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateObservation.class.getName());
 
    public String nameOfNewSection = "";
    
    /** Creates a new instance of routerCreateSection */
    public routerCreateObservation() {
        super();
        
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    //public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
     String newSectionName = "";
     routerApplyStyle ras = new routerApplyStyle();
     subAction.setActionValue("observation");

     //ras.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);
     ras.route_TextSelectedInsert(subAction, pFrame, ooDocument);
     
     newSectionName = CommonRouterActions.get_newSectionNameForAction(subAction, ooDocument);
         if (newSectionName.length() == 0 ) {
             
         } else {
            boolean bAction = action_createSystemContainerFromSelection(ooDocument, newSectionName);
            if (bAction ) {
                //set section type metadata
                CommonRouterActions.setSectionProperties(subAction, newSectionName, ooDocument);
                //setSectionProperties(action, newSectionName, ooDocument);
                ooDocument.setSectionMetadataAttributes(newSectionName, CommonRouterActions.get_newSectionMetadata(subAction));
            } else {
                log.error("routeAction_TextSelectedInsertAction_CreateSection: error while creating section ");
                return new BungeniValidatorState(true, new BungeniMsg("FAILURE_CREATE_SECTION"));
            }
         }      
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
    

 private boolean action_createSystemContainerFromSelection(OOComponentHelper ooDoc, String systemContainerName){
        boolean bResult = false; 
        try {
        XTextViewCursor xCursor = ooDoc.getViewCursor();
        XText xText = xCursor.getText();
        XTextContent xSectionContent = ooDoc.createTextSection(systemContainerName, (short)1);
        xText.insertTextContent(xCursor, xSectionContent , true); 
        bResult = true;
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            bResult = false;
            log.error("in addTextSection : "+ex.getLocalizedMessage(), ex);
        }  finally {
            return bResult;
        }
    }


}
