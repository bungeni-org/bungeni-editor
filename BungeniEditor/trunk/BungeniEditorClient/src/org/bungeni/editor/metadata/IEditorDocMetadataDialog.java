/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author undesa
 */
public interface IEditorDocMetadataDialog {
   public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlgMode) ;
   public void initialize();
   public Component getPanelComponent();
   public Dimension getFrameSize();
   public void setTabTitle(String sTitle);
   public String getTabTitle();
   public boolean applySelectedMetadata(BungeniFileSavePathFormat spf);
}
