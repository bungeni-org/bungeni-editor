package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.jvnet.substance.skin.SubstanceNebulaLookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class NebulaLAF implements ILookAndFeel {



      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NebulaLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceNebulaLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceNebulaLookAndFeel();
        } catch (Exception ex) {
            log.error("NebulaLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }
    
}
