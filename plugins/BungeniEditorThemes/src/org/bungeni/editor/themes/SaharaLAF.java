package org.bungeni.editor.themes;

import javax.swing.LookAndFeel;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceSaharaLookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class SaharaLAF implements ILookAndFeel {


      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaharaLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceSaharaLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceSaharaLookAndFeel();
        } catch (Exception ex) {
            log.error("NebulaLAF:newLAFInstance :" + ex.getMessage());
        } 
        return sbslFeel;
        
    }
}
