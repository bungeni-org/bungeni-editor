package org.bungeni.editor.selectors.debaterecord.questions;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.actions.routers.CommonRouterActions;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;
import org.bungeni.editor.selectors.panelInfo;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;

import java.awt.Component;



/**
 *
 * @author Ashok Hariharan
 */
public class Main extends BaseMetadataContainerPanel {
    private static org.apache.log4j.Logger log                   =
        org.apache.log4j.Logger.getLogger(Main.class.getName());
  
    private String m_questionsSectionName = "";
    
    public Main() {
        super();
    }

    @Override
    public Component getPanelComponent() {
        return this;
    }

    @Override
    public void updateAllPanels() {
        try {
            for (panelInfo p : m_activePanels) {
                p.getPanelObject().doUpdateEvent();
            }
        } catch (Exception ex) {
            log.error("updateAllPanels : " + ex.getMessage());
        }
    }

    @Override
    public boolean preApplySelectedInsert() {

        //
        makeMetaEditable();

        // create the section if it doesnt exist over here...

        String newSection = getActionSectionName();

        if (!ooDocument.hasSection(newSection)) {

            // creat ethe new section
            m_questionsSectionName = newSection;

            XTextViewCursor xCursor         = ooDocument.getViewCursor();
            XText           xText           = xCursor.getText();
            XTextContent    xSectionContent = ooDocument.createTextSection(newSection, (short) 1);

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

        return true;
    }

    @Override
    public boolean postApplySelectedInsert() {
        if (sectionMetadataEditor != null) {
            if (sectionMetadataEditor.bMetadataEditable) {
                if (m_questionsSectionName.length() > 0) {
                    if (!sectionMetadataEditor.hasMetadataEditableFlag(ooDocument, m_questionsSectionName)) {
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, m_questionsSectionName);
                    } else {
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, m_questionsSectionName);
                    }
                }
            }
        }

        return true;
    }

}
