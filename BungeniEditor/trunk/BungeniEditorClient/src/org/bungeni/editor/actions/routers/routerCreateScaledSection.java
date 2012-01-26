package org.bungeni.editor.actions.routers;

//~--- non-JDK imports --------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.actions.toolbarActionDeprecated;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class routerCreateScaledSection extends defaultRouter {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(routerCreateScaledSection.class.getName());

    /** Creates a new instance of routerMarkWorkflowAction */
    public routerCreateScaledSection() {
        super();
    }

    // !+ACTION_RECONF (rm, jan 2012) - action => parent of subAction
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction,
            javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
    // public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction,
    //        javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        
        //  CommonRouterActions.displaySubActionDialog(action, subAction, pFrame, ooDocument, true);
        CommonRouterActions.displaySubActionDialog(subAction, pFrame, ooDocument, true);
        
        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }
}
