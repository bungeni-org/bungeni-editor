/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceCremeCoffeeLookAndFeel;

/**
 *
 * @author undesa
 */
public class CafeCremeLAF implements ILookAndFeel {

      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CafeCremeLAF.class.getName());

      public LookAndFeel newLAFInstance() {
        SubstanceCremeCoffeeLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new SubstanceCremeCoffeeLookAndFeel();
        } catch (Exception ex) {
            log.error("CafeCremeLAF:newLAFInstance :" + ex.getMessage());
        }   return sbslFeel;
        
    }

}
