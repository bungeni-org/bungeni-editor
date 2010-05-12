/*
 * replaceTextWithField.java
 *
 * Created on February 4, 2008, 12:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.bungeni.editor.selectors.BungeniFormContext;
import org.bungeni.ooo.ooDocFieldSet;

/**
 *
 * @author Administrator
 */
public class insertHtmlDocumentIntoSection implements Command {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(insertHtmlDocumentIntoSection.class.getName());
 
    /** Creates a new instance of replaceTextWithField */
    public insertHtmlDocumentIntoSection() {
    }

    public boolean execute(Context context) throws Exception {
        boolean bRet = false;
        try {
            BungeniFormContext formContext = (BungeniFormContext) context;
            log.debug("executing command : insetHtmlDocumentIntoSection");
            String sectionName = (String) formContext.popObjectFromFieldSet("import_html_section");
            String pathToFile = (String) formContext.popObjectFromFieldSet("import_html_file");
            String styleText = (String) formContext.popObjectFromFieldSet("import_html_style");
            bRet = CommonActions.action_insertHtmlDocumentIntoSection(formContext.getOoDocument(), 
                                                                sectionName, pathToFile, styleText);

        } catch (Exception ex) {
            log.error("Command:insertHtmlDocumentIntoSection: "+ ex.getMessage());            
            bRet = true;
        } finally {
            return !bRet;
        }
    }
    
}
