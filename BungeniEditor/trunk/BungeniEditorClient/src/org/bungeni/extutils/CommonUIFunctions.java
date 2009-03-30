/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.extutils;

import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author undesa
 */
public class CommonUIFunctions {
    public static void setPanelBackground(JPanel panel){
        String genBackColor = BungeniEditorProperties.getEditorProperty("genericPanelBackColor");
        if (genBackColor.trim().length() == 0) {
            panel.setBackground(Color.LIGHT_GRAY);
        } else {
            panel.setBackground(Color.decode(genBackColor));
        }
    }
}
