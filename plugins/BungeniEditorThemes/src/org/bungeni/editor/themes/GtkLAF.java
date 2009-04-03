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

    public LookAndFeel newLAFInstance() {
               return new GTKLookAndFeel();
    }

}
