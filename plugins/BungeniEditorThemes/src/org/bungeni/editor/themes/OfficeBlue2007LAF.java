package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class OfficeBlue2007LAF implements ILookAndFeel {

      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OfficeBlue2007LAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceOfficeBlue2007LookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceOfficeBlue2007LookAndFeel();
        } catch (Exception ex) {
            log.error("OfficeBlue2007LAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }

}
