/*
 * IDialogSelector.java
 *
 * Created on September 2, 2007, 4:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors;

import javax.swing.JDialog;
import javax.swing.JPanel;
import org.bungeni.editor.actions.toolbarActionDeprecated;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public interface IDialogSelector {
    public void setDialogMode(SelectorDialogModes mode );
    public SelectorDialogModes getDialogMode();
    public void setOOComponentHelper(OOComponentHelper ooComp);

    // !+ ACTION_RECONF (rm, jan 2012) - toolbarAction is deprecated
    // public void setToolbarAction(toolbarAction action);
    public void setToolbarAction(toolbarAction action);

    public void setParentDialog(JDialog dlg);
    public void setToolbarSubAction(toolbarAction subAction);
    //public void initObject(OOComponentHelper ooDoc, JDialog dlg,  toolbarSubAction act, toolbarSubAction subAct );
    public void initObject(OOComponentHelper ooDoc, JDialog dlg, toolbarAction subAct );
    public JPanel getPanel();
}
