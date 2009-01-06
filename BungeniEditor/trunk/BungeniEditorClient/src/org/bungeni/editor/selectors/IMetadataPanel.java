/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors;

import java.awt.Component;

/**
 *
 * @author undesa
 */
public interface IMetadataPanel {
    public String getPanelName();
    public Component getPanelComponent();
    //public BaseMetadataContainerPanel getContainerPanel();
    public void initVariables(BaseMetadataContainerPanel panel);
    public boolean doApply();
    public boolean doUpdateEvent();
    public boolean doValidate();
    //public void initVariables(OOComponentHelper ooDoc, JFrame pFrame, toolbarAction tAction, toolbarSubAction tSubAction, SelectorDialogModes smode) ;

}
