package org.bungeni.editor.actions;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.editor.actions.routers.routerFactory;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.error.ErrorMessages;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.MessageBox;
import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFrame;

/**
 *
 * @author Ashok Hariharan
 */
public class EditorSelectionActionHandler implements IEditorActionEvent {
    private static org.apache.log4j.Logger log         = Logger.getLogger(EditorSelectionActionHandler.class.getName());
    private ErrorMessages                  errorMsgObj = new ErrorMessages();
    private BungeniClientDB                dbSettings;
    private toolbarAction                  m_parentAction;
    private toolbarSubAction               m_subAction;
    private OOComponentHelper              ooDocument;
    private JFrame                         parentFrame;

    /** Creates a new instance of EditorSelectionActionHandler */
    public EditorSelectionActionHandler() {
        dbSettings = new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(),
                                         DefaultInstanceFactory.DEFAULT_DB());
    }

    public void doCommand(OOComponentHelper ooDocument, toolbarAction action, JFrame c) {}

    public void doCommand(OOComponentHelper ooDocument, toolbarSubAction action, JFrame c) {

        // the modes available are either text_select_insert or edit
        this.ooDocument     = ooDocument;
        this.parentFrame    = c;
        this.m_subAction    = action;
        this.m_parentAction = get_parentAction();
        this.m_parentAction.setSelectorDialogMode(action.getSelectorDialogMode());

        int nValid = -1;

        // BungeniMessage theMessage ;
        // all error returns < 0 indicate failure and stoppagte
        // all error returns > 0 indicate that processing can go ahead
        BungeniValidatorState validObj = _validateAction();

        if (validObj.state) {    // state alright, route action
            BungeniValidatorState routeObj = _routeAction();
        } else {

            // state false....
            // show error message
            String msg = validObj.msg.getMessageString();

            MessageBox.OK(null, msg);
        }
    }

    /**
     * The action routing architecture allows adding pre-validation check before routing the action.
     * Pre-validation checks can be added per action-subAction combination and allow runtime aborting of user generated actions.
     * e.g. if a user action is invalid because of some metadata in the doucment or some specific state of the document, a validation action
     * allows a pre-check of the user action.
     * By default the recommended validator to use for an action is the defaultValidator which always to 'true'
     * (Refer to the settings table sub_action_settings )
     * @return
     */
    private BungeniValidatorState _validateAction() {

        // get subAction validator class
        org.bungeni.editor.actions.validators.IBungeniActionValidator validatorObject = null;

        validatorObject = org.bungeni.editor.actions.validators.validatorFactory.getValidatorClass(m_subAction);

        BungeniValidatorState validState = validatorObject.check(this.m_parentAction, this.m_subAction,
                                               this.ooDocument);

        return validState;
    }

    /**
     * Routes the action by instantiating the object to route to using the IBungeniActionRouter interface
     * @return
     */
    private BungeniValidatorState _routeAction() {
        log.debug("_routeAction : calling _routeAction()");

        org.bungeni.editor.actions.routers.IBungeniActionRouter routerObject = null;

        routerObject = routerFactory.getRouterClass(m_subAction);

        BungeniValidatorState validState = routerObject.route(m_parentAction, m_subAction, parentFrame, ooDocument);

        return validState;
    }

    /**
     * returns the parent toolbarAction object for a toolbarSubAction object
     * @return toolbarAction object or null if the parent cannot be deterimined
     */
    private toolbarAction get_parentAction() {
        Vector<Vector<String>> resultRows = new Vector<Vector<String>>();
        toolbarAction          action     = null;

        try {
            String currentDocumentType = BungeniEditorProperties.getEditorProperty("activeDocumentMode");

            dbSettings.Connect();

            QueryResults qr = dbSettings.QueryResults(SettingsQueryFactory.Q_FETCH_ACTION_BY_NAME(currentDocumentType,
                                  this.m_subAction.parent_action_name()));

            dbSettings.EndConnect();

            if (qr == null) {
                throw new Exception("QueryResults returned null");
            }

            if (qr.hasResults()) {
                HashMap columns = qr.columnNameMap();

                // child actions are present
                // call the result nodes recursively...
                resultRows = qr.theResults();

                // should alwayrs return a single result
                Vector<java.lang.String> tableRow = new Vector<java.lang.String>();

                tableRow = resultRows.elementAt(0);
                action   = new toolbarAction(tableRow, columns);
            }
        } catch (Exception ex) {
            log.error("getParentSection: " + ex.getMessage());
        } finally {
            return action;
        }
    }

    public void doCommand(OOComponentHelper ooDocument, ArrayList<String> action, JFrame parentFrame) {}
}
