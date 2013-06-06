/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.editor.actions.routers;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import org.bungeni.editor.config.OntologiesReader;
import org.bungeni.extutils.CommonEditorXmlUtils;
import org.jdom.Element;
import org.jdom.JDOMException;

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
    private final String                   __EVENT_NAME__ = "BungeniEventName";

    String                                 nameOfNewSection        = "";

    /** Creates a new instance of routerCreateSection */
    public routerCreateEvent() {
        super();
    }

    private HashMap<String, String> getMetadataForEvent(String eventName) {
        String                  docType      = BungeniEditorPropertiesHelper.getCurrentDocType();
        HashMap<String, String> eventMetaMap = new HashMap<String, String>();
        Element ontoElement = null;
        try {
            ontoElement = OntologiesReader.getInstance().getOntologyByTypeAndName(docType, eventName);
        } catch (JDOMException ex) {
            log.error("Erorr loading ontologies xml", ex);
        }
        if (ontoElement != null) {
        // add custom metadata
            eventMetaMap.put(__EVENT_ONTOLOGY__, ontoElement.getAttributeValue("href"));
            eventMetaMap.put(__EVENT_ONTOLOGY_NAME__, ontoElement.getAttributeValue("name"));
            eventMetaMap.put("BungeniEventDesc",
                    CommonEditorXmlUtils.getLocalizedChildElementValue(
                        BungeniEditorPropertiesHelper.getLangAlpha3Part2(),
                        ontoElement,
                        "title"
                        )
                    );
            eventMetaMap.put(__EVENT_NAME__,nameOfNewSection);
        }
        return eventMetaMap;
    }

    // !+ACTION_RECONF (rm, jan 2012) - removing variable toolbarAction, class is deprecated
    @Override
    //public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction,
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction,
            javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        String newSectionName = "";

        newSectionName = CommonRouterActions.get_newSectionNameForAction(subAction, ooDocument);

        String sEventName = subAction.action_value().trim();

        this.nameOfNewSection = newSectionName;

        if (newSectionName.length() == 0) {
            log.error("routeAction_TextSelectedInsertAction_CreateEvent: error while creating section ");

            return new BungeniValidatorState(true, new BungeniMsg("FAILURE_CREATE_SECTION"));
        } else {
            boolean bAction = CommonRouterActions.action_createSystemContainerFromSelection(ooDocument, newSectionName);

            if (bAction) {

                // set section type metadata
                CommonRouterActions.setSectionProperties(subAction, newSectionName, ooDocument);

                HashMap<String, String> sectionMetadata = CommonRouterActions.get_newSectionMetadata(subAction);

                // query ontology values
                HashMap<String, String> customMeta = getMetadataForEvent(sEventName);

                // set custom meta
                sectionMetadata.put(__EVENT_ONTOLOGY__, customMeta.get(__EVENT_ONTOLOGY__));
                sectionMetadata.put(__EVENT_ONTOLOGY_NAME__, customMeta.get(__EVENT_ONTOLOGY_NAME__));
                sectionMetadata.put(__EVENT_NAME__, customMeta.get(__EVENT_NAME__));

                ooDocument.setSectionMetadataAttributes(newSectionName, sectionMetadata);
            } else {
                log.error("routeAction_TextSelectedInsertAction_CreateSection: error while creating section ");

                return new BungeniValidatorState(true, new BungeniMsg("FAILURE_CREATE_SECTION"));
            }
        }

        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }
}
