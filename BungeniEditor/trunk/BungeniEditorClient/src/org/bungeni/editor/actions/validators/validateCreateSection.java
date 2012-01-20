/*
 * validateCreateSection.java
 *
 * Created on March 3, 2008, 8:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.bungeni.editor.actions.validators;

import org.apache.log4j.Logger;
import org.bungeni.editor.actions.*;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.extutils.CommonPropertyFunctions;

/**
 *
 * @author Administrator
 */
public class validateCreateSection extends defaultValidator {
   private static org.apache.log4j.Logger log = Logger.getLogger(validateCreateSection.class.getName());
 
    /** Creates a new instance of validateCreateSection */
    public validateCreateSection() {
        super();
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as argument
    public BungeniValidatorState check_TextSelectedInsert(toolbarSubAction subAction, OOComponentHelper ooDocument) {
        boolean bstate;
        //1st tier, root container check
        bstate= check_rootContainerExists(ooDocument);
        if (!bstate) {
            //if it doesnt exist fail
             return new BungeniValidatorState(false, new BungeniMsg("ROOT_CONTAINER_NOT_EXIST")); 
        }
        //2nd tier validation ...check up the hierarchy
        //check if there is a current section, and if the section can be created in the current section
        String currentSectionname = ooDocument.currentSectionName();
        bstate = check_containment(subAction, ooDocument, currentSectionname);
        if (!bstate) {
            //if containment check fails return fals and fail
            return new BungeniValidatorState(false, new BungeniMsg("INVALID_SECTION_CONTAINER"));
        }
        //3rd tier validation
        //check if section already exists (only for single type section)
        bstate = check_actionSectionExists( subAction, ooDocument);
        if (bstate) {
            //check if action section exists... 
            //if it already exists, fail
            return new BungeniValidatorState(false, new BungeniMsg("SECTION_EXISTS"));
        }
        
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

   

    private boolean check_rootContainerExists(OOComponentHelper ooDocument){
        String rootSectionname = CommonPropertyFunctions.getDocumentRootSection();
        if (ooDocument.hasSection(rootSectionname)){
            return true;
        } else {
            return false;
        }
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as an arg
    private boolean check_containment(toolbarSubAction subAction, OOComponentHelper ooDocument, String currentSectionname){
         boolean bstate = false;
         if (currentSectionname.equals(CommonPropertyFunctions.getDocumentRootSection())) {
                //current section is the root section
                //can the section be added inside the root section ?
               // bstate = check_containment_RootSection(action, subAction, ooDocument, currentSectionname);
               log.error("check_containment has been deprecated");
         } else {
                //current section is not the root section
                //can the section be added inside this section
                // bstate = check_containment_Section(action, subAction, ooDocument, currentSectionname);
                log.error("check_containment has been deprecated");

         }
        return bstate;
    }
    /* DISABLED r1681 ACTION_PARENT Needs to be deprecated.
    private boolean check_containment_RootSection(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument, String currentSectionname) {
        //check if the current section can have the root section as a parent
        String strQuery = "Select count(*) as THE_COUNT from ACTION_PARENT where ACTION_NAME='"+action.action_name()+"'";
        dbSettings.Connect();
        QueryResults qr = dbSettings.QueryResults(strQuery);
        dbSettings.EndConnect();
        if (qr.hasResults()) {
            String[] theCount = qr.getSingleColumnResult("THE_COUNT");
            if (theCount[0].equals("0")) {
                // the section can have the root section as its container
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }*/

    /*
    private boolean check_containment_Section(toolbarAction action, toolbarSubAction subAction, OOComponentHelper ooDocument, String currentSectionname) {
        
        boolean bstate = false;
        try {
        //get valid parent actions
        String strActionName = action.action_name();
        dbSettings.Connect();
        String fetchParentQuery = SettingsQueryFactory.Q_FETCH_PARENT_ACTIONS(strActionName);
        QueryResults qr = dbSettings.QueryResults(fetchParentQuery);
        dbSettings.EndConnect();
        String[] actionSectionTypes = qr.getSingleColumnResult("ACTION_SECTION_TYPE");
        //there can be multiple parents... so we iterate through the array if one of them is a valid parent
        
        HashMap<String,String> sectionMetadata = ooDocument.getSectionMetadataAttributes(currentSectionname);
        //if (sectionMetadata.get)
        String strDocSectionType = "";
        if (sectionMetadata.containsKey("BungeniSectionType")) {
                strDocSectionType = sectionMetadata.get("BungeniSectionType").trim();
                //check the doc section type against the array of valid action section types
                for (String sectionType: actionSectionTypes) {
                     if (strDocSectionType.equals(sectionType.trim())) {
                           bstate = true;
                            break;
                     }  else {
                           bstate = false;
                     }
                 }
          } else {
            bstate = false;
        }
        } catch (Exception ex) {
            log.error("check_containmentSection : " + ex.getMessage());
            log.error("check_containmentSection : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return bstate;
        }
    } */

    // !+ACTION_RECONF (rm, jan 2012)- removed toolbarAction as argument
    private boolean check_actionSectionExists(toolbarSubAction subAction, OOComponentHelper ooDocument) {
        if (subAction.section_numbering_convention().equals("single")) {
            if (ooDocument.hasSection(subAction.section_naming_convention())) {
                return true;
            } else {
                return false;
            }
        } else { //if section style is not single then multiple instances of the section can be created.
            return false;
        }
    }
}
