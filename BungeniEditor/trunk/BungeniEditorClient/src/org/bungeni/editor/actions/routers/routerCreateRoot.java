/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextViewCursor;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.actions.toolbarActionDeprecated;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class routerCreateRoot extends defaultRouter {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateRoot.class.getName());
 
    public String nameOfNewSection = "";
    
    /** Creates a new instance of routerCreateSection */
    public routerCreateRoot() {
        super();
    }

    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
          BungeniValidatorState bvs =  new BungeniValidatorState(false, new BungeniMsg("FAILURE")); 
        try {
            String newSectionName = subAction.action_value();
            XTextViewCursor xCursor = ooDocument.getViewCursor();
            XText xText = xCursor.getText();
            XTextContent xSectionContent = ooDocument.createTextSection(newSectionName, (short) 1);
            xText.insertTextContent(xCursor, xSectionContent, true);
            HashMap<String,String> sectionMeta = new HashMap<String,String>(){
                {
                    put("BungeniSectionType", "body");
                }
            };
            ooDocument.setSectionMetadataAttributes(newSectionName, sectionMeta);
            bvs =  new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
        } catch (IllegalArgumentException ex) {
            log.error("route_TextSelectedInsert : " + ex.getMessage());
        } finally {
            return bvs;
        }
    }
}
