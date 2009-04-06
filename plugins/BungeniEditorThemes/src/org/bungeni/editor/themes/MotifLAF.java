/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import javax.swing.LookAndFeel;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;

/**
 *The Motif look and feel
 * @author Ashok
 */
public class MotifLAF implements ILookAndFeel {


    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MotifLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        MotifLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new MotifLookAndFeel();
        } catch (Exception ex) {
            log.error("MotifLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }


}
