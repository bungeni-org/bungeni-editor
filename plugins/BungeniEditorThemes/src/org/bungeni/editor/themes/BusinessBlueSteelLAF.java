package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class BusinessBlueSteelLAF implements ILookAndFeel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BusinessBlueSteelLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceBusinessBlueSteelLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceBusinessBlueSteelLookAndFeel();
        } catch (Exception ex) {
            log.error("BusinessBlueSteelLAF:newLAFInstance :" + ex.getMessage());
        } 
            return sbslFeel;
        
    }

}
