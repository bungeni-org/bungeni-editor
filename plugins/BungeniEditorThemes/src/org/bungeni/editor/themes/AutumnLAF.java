/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.jvnet.substance.skin.SubstanceAutumnLookAndFeel;

/**
 *
 * @author undesa
 */
public class AutumnLAF implements ILookAndFeel {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AutumnLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        SubstanceAutumnLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceAutumnLookAndFeel();
        } catch (Exception ex) {
            log.error("AutumnLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }

}
