/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

/**
 *
 * @author undesa
 */
public class BusinessBlackSteelLAF implements ILookAndFeel {

    public LookAndFeel newLAFInstance() {
        return new SubstanceBusinessBlackSteelLookAndFeel();
    }

}
