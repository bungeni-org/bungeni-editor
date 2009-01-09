/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.awt.Component;
import javax.swing.JFrame;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public abstract class BaseEditorDocMetadataDialog extends javax.swing.JPanel implements IEditorDocMetadataDialog {

    protected OOComponentHelper ooDocument;
    protected JFrame parentFrame;
    protected SelectorDialogModes theMode;
    
    public BaseEditorDocMetadataDialog(){
        super();
    }

    public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlgMode) {
        ooDocument = ooDoc;
        parentFrame = parentFrm;
        theMode = dlgMode;
    }

    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    abstract public Component getPanelComponent() ;
    
    
}
