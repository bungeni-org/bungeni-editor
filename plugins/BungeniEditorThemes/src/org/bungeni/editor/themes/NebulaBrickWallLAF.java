package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.jvnet.substance.skin.SubstanceNebulaBrickWallLookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class NebulaBrickWallLAF implements ILookAndFeel {

  

      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NebulaBrickWallLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceNebulaBrickWallLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceNebulaBrickWallLookAndFeel();
        } catch (Exception ex) {
            log.error("NebulaBrickWallLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }
}
