/*
 * routerCreateSection.java
 *
 * Created on March 11, 2008, 12:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;


import javax.swing.JFrame;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniFrame;

/**
 *
 * @author Administrator
 */
public class routerCreateSidenote extends defaultRouter {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateSidenote.class.getName());
 

    /** Creates a new instance of routerCreateSection */
    public routerCreateSidenote() {
        super();
        
    }
    
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
     // String styleName = subAction.action_value();
        BungeniFrame sidenoteFrame = new BungeniFrame("Enter Sidenote");
        routerCreateSideNote_panel  rcsp = new routerCreateSideNote_panel( ooDocument,  pFrame,  sidenoteFrame);
       
        sidenoteFrame.setSize(328, 171);
        sidenoteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       // sidenoteFrame.setSize(meta.getFrameSize());
        sidenoteFrame.add(rcsp);
        sidenoteFrame.setVisible(true);
        sidenoteFrame.setAlwaysOnTop(true);
          return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
    }

    
    
    /**** 
     *
     * private APIs for this action 
     *
     ****/

}
