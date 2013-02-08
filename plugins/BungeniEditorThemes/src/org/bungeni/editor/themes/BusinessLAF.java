/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel;

/**
 *
 * @author undesa
 */
public class BusinessLAF implements ILookAndFeel {
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BusinessLAF.class.getName());

    public LookAndFeel newLAFInstance() {
       SubstanceBusinessLookAndFeel sbslFeel = null;
           try {
            sbslFeel = new SubstanceBusinessLookAndFeel();
        } catch (Exception ex) {
            log.error("BusinessLAF:newLAFInstance :" + ex.getMessage());
        }   return sbslFeel;
        
    }

}
