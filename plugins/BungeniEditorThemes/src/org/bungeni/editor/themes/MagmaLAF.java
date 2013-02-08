
package org.bungeni.editor.themes;

import javax.swing.LookAndFeel;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceMagellanLookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class MagmaLAF implements ILookAndFeel {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MagmaLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceMagellanLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceMagellanLookAndFeel();
        } catch (Exception ex) {
            log.error("MagmaLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }

}
