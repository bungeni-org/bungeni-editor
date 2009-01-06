/*
 * addDocumentIntoSection.java
 *
 * Created on December 20, 2007, 6:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;


import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.bungeni.editor.selectors.BungeniFormContext;

/**
 * 
 * @author Administrator
 */
public class addDocumentIntoSection implements Command {
    
    /**
     * Creates a new instance of addDocumentIntoSection
     * Requires: ooDocMetadataFieldSet object to be set prior to invoking the command
     */
    /*
     *Requires : current_section and document_fragment to be set in pre_insert_map
     */
     public boolean execute(Context context) throws Exception {
        BungeniFormContext formContext = (BungeniFormContext) context;
      //  IBungeniForm iForm = formContext.getBungeniForm();
        
            boolean bAddDocintoSection = CommonActions.action_addDocIntoSection(formContext.getOoDocument(), 
                   (String) formContext.popObjectFromFieldSet("document_import_section") /*getPreInsertMap().get("current_section")*/,
                   (String) formContext.popObjectFromFieldSet("document_fragment") /*getPreInsertMap().get("document_fragment")*/ );
            
        return false;
     }
}
