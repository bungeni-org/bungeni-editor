package org.bungeni.editor.selectors.debaterecord.speech;

//~--- non-JDK imports --------------------------------------------------------

import java.awt.Component;
import org.bungeni.editor.actions.routers.CommonRouterActions;
import org.bungeni.editor.actions.routers.CommonRouterActions.TypeCreationState;
import org.bungeni.editor.actions.toolbarAction.actionSourceOrigin;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;

/**
 *
 * @author Ashok Hariharan
 */
public class Main extends BaseMetadataContainerPanel {
    private static org.apache.log4j.Logger log             = org.apache.log4j.Logger.getLogger(Main.class.getName());
   
    public Main() {
        super();
    }





    @Override
    public Component getPanelComponent() {
        return this;
    }

    @Override
    public boolean preApplySelectedInsert() {

        // create the section if it doesnt exist over here...
        makeMetaEditable();

        if (theSubAction.getActionSource().equals(actionSourceOrigin.sectionType)) {
            TypeCreationState scs =  CommonRouterActions.action_createSection(theSubAction, ooDocument);
            this.mainSectionName = scs.typeName;
        }
        //!+FIX_THIS for other types
        /**
        String newSection = getActionSectionName();

        if (!ooDocument.hasSection(newSection)) {

            // creat ethe new section
            XTextViewCursor xCursor         = ooDocument.getViewCursor();
            XText           xText           = xCursor.getText();
            XTextContent    xSectionContent = ooDocument.createTextSection(newSection, SECTION_COLUMNS);

            try {
                xText.insertTextContent(xCursor, xSectionContent, true);
            } catch (com.sun.star.lang.IllegalArgumentException ex) {
                log.error("IllegalArgumentException, preMainApply : " + ex.getMessage());
            }

            mainSectionName = newSection;

            // get section poperties
            XTextSection xSection = ooDocument.getSection(newSection);

            CommonRouterActions.setSectionProperties(theSubAction, newSection, ooDocument);
            ooDocument.setSectionMetadataAttributes(xSection, CommonRouterActions.get_newSectionMetadata(theSubAction));
        }
        ***/
        return true;
    }

    @SuppressWarnings("empty-statement")
  
    @Override
    public boolean postApplySelectedInsert() {
        if (sectionMetadataEditor != null) {
            if (sectionMetadataEditor.bMetadataEditable) {
                if (mainSectionName.length() > 0) {
                    if (!sectionMetadataEditor.hasMetadataEditableFlag(ooDocument, mainSectionName)) {
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, mainSectionName);
                    } else {
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, mainSectionName);
                    }
                }
            }
        }

        return true;
    }
}
