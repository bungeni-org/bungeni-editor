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
public class insertArrayAsBulletList implements Command {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(insertArrayAsBulletList.class.getName());
 
    /** Creates a new instance of replaceTextWithField */
    public insertArrayAsBulletList() {
    }

    public boolean execute(Context context) throws Exception {
        boolean bRet = false;
        try {
            BungeniFormContext formContext = (BungeniFormContext) context;
            log.debug("executing command : insertArrayAsBulletList");
            String bookmarkAnchor = (String) formContext.popObjectFromFieldSet("bullet_list_begin_bookmark");
            String[] titles = (String[]) formContext.popObjectFromFieldSet("tabled_document_titles");
            String[] uris  =  (String[]) formContext.popObjectFromFieldSet("tabled_document_uris");
          
            bRet = CommonActions.action_insertArrayAsBulletList(formContext.getOoDocument(), 
                                                                bookmarkAnchor,
                                                                titles,
                                                                uris);
        } catch (Exception ex) {
            log.error("Command:insertArrayAsBulletList: "+ ex.getMessage());            
            bRet = true;
        } finally {
            return !bRet;
        }
    }
    
}
