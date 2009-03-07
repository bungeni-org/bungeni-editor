/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.integrate;

import javax.swing.JDialog;
import javax.swing.JFrame;
import org.bungeni.editor.plugin.EditorPlugin;
import org.bungeni.editor.section.refactor.ui.panelSectionRefactor;


/**
 *
 * @author undesa
 */
public class SectionRefactorExt extends EditorPlugin {

    @Override
    public boolean invoke() {
              JDialog mframe = new JDialog();
              mframe.setTitle("Restructure Document");
              String fileName = "/home/undesa/Desktop/ken_bill_2009_1_10_eng_main.odt";
              panelSectionRefactor panel = new panelSectionRefactor(fileName);
              panel.setParentFrame(mframe);
              mframe.add(panel);
             mframe.setSize(500, 365);
             mframe.pack();
             mframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
             mframe.setVisible(true);
            return true;
    }
  

}
