/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
