/*
 * setSectionMetadataForAction.java
 *
 * Created on December 20, 2007, 5:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;

/**
 *
 * @author Administrator
 */



import java.util.HashMap;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.bungeni.editor.selectors.BungeniFormContext;
import org.bungeni.editor.selectors.IBungeniForm;


public class replaceLinkInSectionByName implements Command {
       private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(renameSection.class.getName());
    /**
     * Creates a new instance of setSectionMetadataForAction
     * Requires: the new_section variable to be set in the preInsertMap prior to invocation.
     */
      public boolean execute(Context context) {
        boolean bRet = false;
          try {
            BungeniFormContext formContext = (BungeniFormContext) context;
          //(OOComponentHelper ooDoc, String sectionName, String hyperLinkName, String hyperLinkText, String hyperLinkUrl, boolean isProtected) {
            String sectionName = (String) formContext.popObjectFromFieldSet("inside_section");
            String hyperLinkName = (String) formContext.popObjectFromFieldSet("hyperlink_name");
            String hyperLinkText = (String) formContext.popObjectFromFieldSet("hyperlink_text");
            String hyperLinkUrl = (String) formContext.popObjectFromFieldSet("hyperlink_url");
            Boolean isProtected = (Boolean)formContext.popObjectFromFieldSet("is_protected");
            
            CommonActions.action_replaceLinkInSectionByName(formContext.getOoDocument(), 
                    sectionName, 
                    hyperLinkName, 
                    hyperLinkText, 
                    hyperLinkUrl, 
                    isProtected.booleanValue());
            
            bRet = false;
          } catch (Exception ex) {
              log.error("Command: renameSection : " + ex.getMessage());
              bRet = true;
          } finally {
                return bRet;
          }
      }
}
