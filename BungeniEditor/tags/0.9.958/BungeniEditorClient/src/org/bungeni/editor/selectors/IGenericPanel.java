
package org.bungeni.editor.selectors;

import java.awt.Component;
import java.awt.Window;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Generic selector panel class with methods for setting the OOo handle and accesing the frame
 * @author Ashok Hariharan
 */
public interface IGenericPanel {
   /**
    * The Frame containing the selector panel
    * @param frame
    */
   public void setContainerFrame (Window frame);
   /**
    * The handle to the openoffice document
    * @param ooDocument
    */
   public void setOOComponentHelper(OOComponentHelper ooDocument);
   /**
    * Returns a handle to the current selector panel, usually a 'return this;'
    * @return
    */
   public Component getPanelComponent();
  
}
