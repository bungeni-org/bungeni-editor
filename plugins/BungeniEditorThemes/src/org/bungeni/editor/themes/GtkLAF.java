/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import javax.swing.LookAndFeel;
import org.bungeni.editor.interfaces.ui.ILookAndFeel;

/**
 *
 * @author undesa
 */
public class GtkLAF implements ILookAndFeel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GtkLAF.class.getName());

    public LookAndFeel newLAFInstance() {
        GTKLookAndFeel sbslFeel = null;
        try {
            sbslFeel = new GTKLookAndFeel();
        } catch (Exception ex) {
            log.error("GtkLAF:newLAFInstance :" + ex.getMessage());
        } finally {
            return sbslFeel;
        }
    }

}
