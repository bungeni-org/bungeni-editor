package org.bungeni.editor.actions.routers;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
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

    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction,
            javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        CommonRouterActions.displaySubActionDialog(action, subAction, pFrame, ooDocument, true);

        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }
}
