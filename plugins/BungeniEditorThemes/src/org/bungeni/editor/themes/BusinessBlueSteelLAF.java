package org.bungeni.editor.themes;

import org.bungeni.editor.interfaces.ui.ILookAndFeel;
import javax.swing.LookAndFeel;
import org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel;

/**
 *
 * @author Ashok Hariharan
 */
public class BusinessBlueSteelLAF implements ILookAndFeel {

    public LookAndFeel newLAFInstance() {
        return new SubstanceBusinessBlueSteelLookAndFeel();
    }

}
