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
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author Administrator
 */
public class insertArrayAsBulletListAtCursor implements Command {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(insertArrayAsBulletListAtCursor.class.getName());
 
    /** Creates a new instance of replaceTextWithField */
    public insertArrayAsBulletListAtCursor() {
    }

    public boolean execute(Context context) throws Exception {
        boolean bRet = false;
        try {
            BungeniFormContext formContext = (BungeniFormContext) context;
            log.debug("executing command : "+ this.getClass().getName());
            String[] titles = (String[])formContext.popObjectFromFieldSet("tabled_document_titles");
            String[] uris  =  (String[])formContext.popObjectFromFieldSet("tabled_document_uris");
          
            bRet = CommonActions.action_InsertArrayAsBulletListAtCurrentCursor(formContext.getOoDocument(), 
                                                                titles,
                                                                uris);
        } catch (Exception ex) {
            log.error("Command:insertArrayAsBulletList: "+ ex.getMessage());    
            log.error("Command:insertArrayAsBulletList, stack-trace:" + CommonExceptionUtils.getStackTrace(ex));
            bRet = true;
        } finally {
            return !bRet;
        }
    }
    
}
