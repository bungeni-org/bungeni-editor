/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel;

/**
 *
 * @author undesa
 */
public class CremeLAF implements ILookAndFeel {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CremeLAF.class.getName());

    public LookAndFeel newLAFInstance() {
       SubstanceCremeLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceCremeLookAndFeel();
        } catch (Exception ex) {
            log.error("CremeLAF:newLAFInstance :" + ex.getMessage());
        }   return sbslFeel;
        
    }

}
