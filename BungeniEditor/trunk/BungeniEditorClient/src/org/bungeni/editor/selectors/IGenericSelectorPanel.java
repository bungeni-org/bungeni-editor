

package org.bungeni.editor.selectors;

import java.awt.Component;
import java.awt.Window;
import javax.swing.JFrame;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Ashok
 */
public interface IGenericSelectorPanel extends IGenericPanel {
   /**
    * This is the pseudo-constructor function that sets up the class with the required parameters
    * @param ooDoc
    * @param parentFrm
    * @param aAction
    * @param aSubAction
    * @param dlgMode
    */
   // !+ACTION_RECONF (rm, jan 2012)
   // aAction => parentAction of aSubAction
 
   public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, toolbarSubAction aSubAction, SelectorDialogModes dlgMode) ;
   /**
    * initialize() is always invoked after initVariables();
    */
   public void initialize();
   /**
    * The Frame containing the selector panel
    * @param frame
    */
   public void setContainerFrame (Window frame);
   /**
    * Returns a handle to the current selector panel, usually a 'return this;'
    * @return
    */
   public Component getPanelComponent();
  
}
