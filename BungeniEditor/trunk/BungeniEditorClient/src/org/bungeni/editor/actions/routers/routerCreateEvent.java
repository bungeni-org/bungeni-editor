package org.bungeni.editor.actions.routers;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;

/**
 *
 * @author Ashok Hariharan
 */
public class routerCreateEvent extends defaultRouter {
    private static org.apache.log4j.Logger log                     =
        org.apache.log4j.Logger.getLogger(routerCreateSection.class.getName());
    private final String                   __EVENT_ONTOLOGY_NAME__ = "BungeniOntologyName";

    // private final String __EVENT_ONTOLOGY_DESC__ = "BungeniOntologyName";

    private final String                   __EVENT_ONTOLOGY__      = "BungeniOntology";
    String                                 nameOfNewSection        = "";

    /** Creates a new instance of routerCreateSection */
    public routerCreateEvent() {
        super();
    }

    private HashMap<String, String> getMetadataForEvent(String eventName) {
        String                  docType      = BungeniEditorPropertiesHelper.getCurrentDocType();
        HashMap<String, String> eventMetaMap = new HashMap<String, String>();
        BungeniClientDB         db           = BungeniClientDB.defaultConnect();
        QueryResults            qr           =
            db.ConnectAndQuery(SettingsQueryFactory.Q_FETCH_ONTOLOGY_FOR_EVENT(docType, eventName));

        // ONTOLOGY, EVENT_NAME, EVENT_DESC
        String[] sOntology  = qr.getSingleColumnResult("ONTOLOGY");
        String[] sEventName = qr.getSingleColumnResult("EVENT_NAME");
        String[] sEventDesc = qr.getSingleColumnResult("EVENT_DESC");

        // add custom metadata
        eventMetaMap.put(__EVENT_ONTOLOGY__, sOntology[0]);
        eventMetaMap.put(__EVENT_ONTOLOGY_NAME__, sEventName[0]);
        eventMetaMap.put("BungeniEventDesc", sEventDesc[0]);

        return eventMetaMap;
    }

    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction,
            javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        String newSectionName = "";

        newSectionName = CommonRouterActions.get_newSectionNameForAction(action, ooDocument);

        String sEventName = subAction.action_value().trim();

        this.nameOfNewSection = newSectionName;

        if (newSectionName.length() == 0) {
            log.error("routeAction_TextSelectedInsertAction_CreateEvent: error while creating section ");

            return new BungeniValidatorState(true, new BungeniMsg("FAILURE_CREATE_SECTION"));
        } else {
            boolean bAction = CommonRouterActions.action_createSystemContainerFromSelection(ooDocument, newSectionName);

            if (bAction) {

                // set section type metadata
                CommonRouterActions.setSectionProperties(action, newSectionName, ooDocument);

                HashMap<String, String> sectionMetadata = CommonRouterActions.get_newSectionMetadata(action);

                // query ontology values
                HashMap<String, String> customMeta = getMetadataForEvent(sEventName);

                // set custom meta
                sectionMetadata.put(__EVENT_ONTOLOGY__, customMeta.get(__EVENT_ONTOLOGY__));
                sectionMetadata.put(__EVENT_ONTOLOGY_NAME__, customMeta.get(__EVENT_ONTOLOGY_NAME__));
                ooDocument.setSectionMetadataAttributes(newSectionName, sectionMetadata);
            } else {
                log.error("routeAction_TextSelectedInsertAction_CreateSection: error while creating section ");

                return new BungeniValidatorState(true, new BungeniMsg("FAILURE_CREATE_SECTION"));
            }
        }

        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }
}
