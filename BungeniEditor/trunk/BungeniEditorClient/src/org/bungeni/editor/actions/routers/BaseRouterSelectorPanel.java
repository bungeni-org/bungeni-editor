/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import java.awt.Component;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public abstract class BaseRouterSelectorPanel extends JPanel implements IRouterSelectorPanel {
    OOComponentHelper ooDocument;
    JFrame parentFrame;
    Window containerFrame;
    toolbarSubAction theSubAction;
    toolbarAction theAction;
    SelectorDialogModes theMode;
    
    public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, toolbarAction aAction, toolbarSubAction aSubAction, SelectorDialogModes dlgMode) {
        ooDocument = ooDoc;
        parentFrame = parentFrm;
        theAction = aAction;
        theSubAction = aSubAction;
        theMode = dlgMode;
    }

    abstract public void initialize() ;

    public void setContainerFrame(Window frame) {
        containerFrame = frame;
    }

    abstract public Component getPanelComponent();

}
