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


public class setSectionMetadata implements Command {
       private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(setSectionMetadata.class.getName());
    /**
     * Creates a new instance of setSectionMetadataForAction
     * Requires: the new_section variable to be set in the preInsertMap prior to invocation.
     */
      public boolean execute(Context context) {
        boolean bRet = false;
          try {
            BungeniFormContext formContext = (BungeniFormContext) context;
            //IBungeniForm iForm = formContext.getBungeniForm();
            String newSectionname = (String) formContext.popObjectFromFieldSet("new_section");
            HashMap<String,String> sectionMeta= (HashMap<String,String>) formContext.popObjectFromFieldSet("section_metadata_map");
            formContext.getOoDocument().setSectionMetadataAttributes(newSectionname, sectionMeta);
            bRet = false;
          } catch (Exception ex) {
              log.error("Command: setSectionMetadata : " + ex.getMessage());
              bRet = true;
          } finally {
                return bRet;
          }
      }
}
