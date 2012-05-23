package org.bungeni.editor.selectors.general.ref;

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
import org.bungeni.extutils.CommonUIFunctions;

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





    @Override
    public Component getPanelComponent() {
        return this;
    }

    @Override
    public boolean preApplySelectedInsert() {

        return true;
    }

    @Override
    public boolean postApplySelectedInsert() {
        //!+FIX_THIS(ah,may-2012) - FIX this for edit mode inline metadata markup
        /**
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
        **/
        return true;
    }
}
