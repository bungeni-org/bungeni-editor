package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class routerCreateSection extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateSection.class.getName());
 
    public String nameOfNewSection = "";
    
    /** Creates a new instance of routerCreateSection */
    public routerCreateSection() {
        super();
        
    }
    
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
     String newSectionName = "";
      newSectionName = CommonRouterActions.get_newSectionNameForAction(subAction, ooDocument);
      this.nameOfNewSection = newSectionName;
         if (newSectionName.length() == 0 ) {
             
         } else {
            boolean bAction = CommonRouterActions.action_createSystemContainerFromSelection(ooDocument, newSectionName);
            if (bAction ) {
                //set section type metadata
                CommonRouterActions.setSectionProperties(subAction, newSectionName, ooDocument);
                ooDocument.setSectionMetadataAttributes(newSectionName, CommonRouterActions.get_newSectionMetadata(subAction));
            } else {
                log.error("routeAction_TextSelectedInsertAction_CreateSection: error while creating section ");
                return new BungeniValidatorState(true, new BungeniMsg("FAILURE_CREATE_SECTION"));
            }
         }
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }
}
