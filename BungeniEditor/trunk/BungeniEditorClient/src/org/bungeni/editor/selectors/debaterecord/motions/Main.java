package org.bungeni.editor.selectors.debaterecord.motions;

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


import javax.swing.JFrame;

/**
 *
 * @author Ashok Hariharan
 */
public class Main extends BaseMetadataContainerPanel {
    private static org.apache.log4j.Logger log                 =
        org.apache.log4j.Logger.getLogger(Main.class.getName());
    private final short                    SECTION_COLUMNS     = 1;
    private String                         m_motionSectionName = "";
    
    public Main() {
        super();
    }

    @Override
    protected void setupPanels() {
        super.setupPanels();
        this.enableAllChildPanels(false);
    }

  

    @Override
    public Component getPanelComponent() {
        return this;
    }

    @Override
    public void updateAllPanels() {
        for (panelInfo p : m_activePanels) {
            p.getPanelObject().doUpdateEvent();
        }
    }

    @Override
    public boolean preApplySelectedInsert() {
        makeMetaEditable();

        // create the section if it doesnt exist over here...
        String newSection = getActionSectionName();

        if (!ooDocument.hasSection(newSection)) {
            m_motionSectionName = newSection;

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

        return true;
    }

    @Override
    public boolean postApplySelectedInsert() {
        if (sectionMetadataEditor != null) {
            if (sectionMetadataEditor.bMetadataEditable) {
                if (m_motionSectionName.length() > 0) {
                    if (!sectionMetadataEditor.hasMetadataEditableFlag(ooDocument, m_motionSectionName)) {
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, m_motionSectionName);
                    } else {
                        sectionMetadataEditor.setMetadataEditableFlag(ooDocument, m_motionSectionName);
                    }
                }
            }
        }

        return true;
    }

}
