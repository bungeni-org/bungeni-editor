/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel;

/**
 *
 * @author undesa
 */
public class ModerateLAF implements ILookAndFeel {

        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ModerateLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceModerateLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceModerateLookAndFeel();
        } catch (Exception ex) {
            log.error("ModerateLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }
}
