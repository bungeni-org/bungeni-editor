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


public class renameSection implements Command {
       private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(renameSection.class.getName());
    /**
     * Creates a new instance of setSectionMetadataForAction
     * Requires: the new_section variable to be set in the preInsertMap prior to invocation.
     */
      public boolean execute(Context context) {
        boolean bRet = false;
          try {
            BungeniFormContext formContext = (BungeniFormContext) context;
            //IBungeniForm iForm = formContext.getBungeniForm();
            String fromSection = (String) formContext.popObjectFromFieldSet("from_section");
            String toSection = (String) formContext.popObjectFromFieldSet("to_section");
            //HashMap<String,String> sectionMeta= (HashMap<String,String>) formContext.getPreInsertMap().get("section_metadata_map");
            //formContext.getOoDocument().setSectionMetadataAttributes(newSectionname, sectionMeta);
            CommonActions.action_renameSection(formContext.getOoDocument(), fromSection, toSection);
            bRet = false;
          } catch (Exception ex) {
              log.error("Command: renameSection : " + ex.getMessage());
              bRet = true;
          } finally {
                return bRet;
          }
      }
}
