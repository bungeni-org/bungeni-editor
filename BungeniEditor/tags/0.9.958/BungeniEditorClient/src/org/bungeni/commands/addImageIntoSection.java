/*
 * addImageIntoSection.java
 *
 * Created on December 20, 2007, 3:45 PM
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
public class addImageIntoSection implements Command {
    
    /** Creates a new instance of addImageIntoSection */
    public addImageIntoSection() {
    }

    /**
     * Requires: current_section and selected_logo variables to be set in the preInserMap of the caller class
     */
    public boolean execute(Context context) throws Exception {
        BungeniFormContext formContext = (BungeniFormContext) context;
        //IBungeniForm iForm = formContext.getBungeniForm();
        String currentSection = (String) formContext.popObjectFromFieldSet("image_import_section"); //getPreInsertMap().get("current_section");
        String logoPath = (String) formContext.popObjectFromFieldSet("selected_logo"); //getPreInsertMap().get("selected_logo");
        boolean bAddImage = CommonActions.action_addImageIntoSection(formContext.getOoDocument(), 
                    currentSection, 
                    logoPath );
         //   if (bAddImage == false) {
         //       //displayFieldErrors();
         //       checkFieldsMessages.add("The image could not be inserted properly");
         //       return false;
         //   } 
                        
        return false;
    }
    
}
