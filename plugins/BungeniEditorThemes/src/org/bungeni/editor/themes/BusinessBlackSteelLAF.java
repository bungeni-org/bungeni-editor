package org.bungeni.editor.themes;

import javax.swing.LookAndFeel;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class BusinessBlackSteelLAF implements ILookAndFeel {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BusinessBlackSteelLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceBusinessBlackSteelLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceBusinessBlackSteelLookAndFeel();
        } catch (Exception ex) {
            log.error("BusinessBlackSteelLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }

}
