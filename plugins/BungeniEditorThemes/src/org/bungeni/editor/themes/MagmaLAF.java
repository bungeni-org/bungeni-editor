/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.jvnet.substance.skin.SubstanceMagmaLookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class MagmaLAF implements ILookAndFeel {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MagmaLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceMagmaLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceMagmaLookAndFeel();
        } catch (Exception ex) {
            log.error("MagmaLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }

}
