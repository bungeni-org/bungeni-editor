package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Creates a narrative section
 * @author ashok hariharan
 */
public class routerCreateNarrative extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateNarrative.class.getName());
 
    public String nameOfNewSection = "";
    
    /** Creates a new instance of routerCreateSection */
    public routerCreateNarrative() {
        super();
        
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    //public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
     String newSectionName = "";
     //routerApplyStyle ras = new routerApplyStyle();
     //subAction.setActionValue("observation");
     //ras.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);
     
     newSectionName = CommonRouterActions.get_newSectionNameForAction(subAction, ooDocument);
         if (newSectionName.length() == 0 ) {
             log.error("*** No section name was returned for this action **** ");
         } else {
            boolean bAction = CommonRouterActions.action_createSystemContainerFromSelection(ooDocument, newSectionName);
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
    



}
