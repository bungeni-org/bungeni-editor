package org.bungeni.editor.selectors.debaterecord.speech;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.actions.routers.CommonRouterActions;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;

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
    private static org.apache.log4j.Logger log             = org.apache.log4j.Logger.getLogger(Main.class.getName());
    private final short                    SECTION_COLUMNS = 1;

    public Main() {
        super();
    }

    public static void main(String[] args) {
        Main   m = new Main();
        JFrame f = new JFrame("MastHead title");

        f.add(m);
        f.pack();
        f.setVisible(true);
    }



    @Override
    public Component getPanelComponent() {
        return this;
    }

    @Override
    public boolean preApplySelectedInsert() {

        // create the section if it doesnt exist over here...
        makeMetaEditable();

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

        return true;
    }

    @SuppressWarnings("empty-statement")
    public String getActionSectionName() {

        // get the action naming convention
        String numberingConvention = theSubAction.section_numbering_convention();

        if (numberingConvention.equals("none") || numberingConvention.equals("single")) {
            return theSubAction.section_naming_convention();
        } else if (numberingConvention.equals("serial")) {

            // get highest section name possible
            int iStart = 1;

            for (; ooDocument.hasSection(theSubAction.section_naming_convention() + iStart); iStart++);

            return theSubAction.section_naming_convention() + iStart;
        } else {
            return theSubAction.section_naming_convention();
        }
    }

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
