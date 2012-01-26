package org.bungeni.editor.actions.routers;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.numbering.ooo.OOoNumberingHelper;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.extutils.BungeniUUID;
import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextViewCursor;

import java.util.HashMap;

/**
 *
 * @author Ashok Hariharan
 */
public class routerCreateNumberedHeading extends defaultRouter {
    private static org.apache.log4j.Logger log                   =
        org.apache.log4j.Logger.getLogger(routerCreateSection.class.getName());
    private static final String            NUMBERED_SECTION_TYPE = OOoNumberingHelper.NUMBERING_SECTION_TYPE;

    /** Creates a new instance of routerCreateNumberedHeading */
    public routerCreateNumberedHeading() {}

    /*
     * This router action marks a heading as a numbered heading and write protects it after applying the associated style
     * apply_numbered_heading:style_name
     */
    // !+ACTION_RECONF (rm, jan 2012) - removed toolbarAction as var, class
    // toolbarAction is deprecated
    @Override
    //public BungeniValidatorState route_TextSelectedInsert(toolbarSubAction action, toolbarSubAction subAction,
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction,
            javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
        String sectionUUID = BungeniUUID.getStringUUID();

        // get the section properties for the numbered container type
        DocumentSection numberedSection     = DocumentSectionsContainer.getDocumentSectionByType(NUMBERED_SECTION_TYPE);
        String          numberedSectionName = numberedSection.getSectionNamePrefix() + sectionUUID;
        String          applyThisStyle      = subAction.action_value();

        if (action_markSelectionAsNumbered(ooDocument, numberedSection, numberedSectionName, applyThisStyle,
                                           sectionUUID)) {}

        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }

    private static boolean createSection(OOComponentHelper ooDoc, String sectionName) {
        boolean bRet = false;

        try {
            XTextViewCursor xCursor         = ooDoc.getViewCursor();
            XText           xText           = xCursor.getText();
            XTextContent    xSectionContent = ooDoc.createTextSection(sectionName, (short) 1);

            xText.insertTextContent(xCursor, xSectionContent, true);
            bRet = true;
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("createSection (IllegalArgumentException) " + ex.getMessage());
            bRet = false;
        } finally {
            return bRet;
        }
    }

    private static void applyStyleToSection(OOComponentHelper ooDoc, String applyStyle) {
        ooDoc.setSelectedTextStyle(applyStyle);
    }

    private static void applySectionMetadata(OOComponentHelper ooDoc, DocumentSection aSection, String sectionName,
            String sectionUUID) {
        HashMap<String, String> sectionMeta = new HashMap<String, String>();

        sectionMeta.put("BungeniSectionType", NUMBERED_SECTION_TYPE);
        sectionMeta.put("BungeniSectionVisibility", aSection.getSectionVisibilty());
        sectionMeta.put("BungeniSectionUUID", sectionUUID);
        ooDoc.setSectionMetadataAttributes(sectionName, sectionMeta);
    }

    public static boolean action_markSelectionAsNumbered(OOComponentHelper ooDoc, DocumentSection aSection,
            String sectionName, String applyStyle, String sectionUUID) {
        boolean bResult = false;

        try {

            // first insert the section
            if (createSection(ooDoc, sectionName)) {

                // now apply the heading style to the section content
                applyStyleToSection(ooDoc, applyStyle);

                // now apply the section metadata
                applySectionMetadata(ooDoc, aSection, sectionName, sectionUUID);

                // now protect the seciton
                ooDoc.protectSection(sectionName, true);
                bResult = true;
            }
        } catch (Exception ex) {
            bResult = false;
            log.error("in addTextSection : " + ex.getLocalizedMessage(), ex);
        } finally {
            return bResult;
        }
    }
}
