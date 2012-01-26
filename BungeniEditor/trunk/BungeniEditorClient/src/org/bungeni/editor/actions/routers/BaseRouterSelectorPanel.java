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
    toolbarAction theSubAction;
    SelectorDialogModes theMode;
    
    public BaseRouterSelectorPanel(){
        //nothing to do
    }
    
    public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, toolbarAction aSubAction, SelectorDialogModes dlgMode) {
        ooDocument = ooDoc;
        parentFrame = parentFrm;
        theSubAction = aSubAction;
        theMode = dlgMode;
    }

    abstract public void initialize() ;

    public void setContainerFrame(Window frame) {
        containerFrame = frame;
    }

    public void setParentFrame(JFrame frm) {
        this.parentFrame = frm;
    }

    public void setOOComponentHelper(OOComponentHelper ooDoc) {
        this.ooDocument = ooDoc;
    }

    abstract public Component getPanelComponent();

}
