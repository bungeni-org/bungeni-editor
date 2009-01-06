/*
 * IFloatingPanel.java
 *
 * Created on July 31, 2007, 2:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.panels.impl;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Interface that must be implmeneted by all Panel classes.
 * Panel classes are the containers for action buttons.
 * @author Administrator
 * Interface to extend Panel UI
 */
public interface ITabbedPanel  {
    /**
     * Set the OpenOffice component handle in the Panel class
     */
  public void setOOComponentHandle (OOComponentHelper ooComponent);
    /**
     * Get the current object handle as a "Component" type object
     */
  public Component getObjectHandle();
    /**
     * get the handle of the class that manages toolbar events for all actions
     */
  
 public void setPanelTitle(String titleOfPanel);
 public String getPanelTitle();
 
 public Integer getPanelLoadOrder();
 public void setPanelLoadOrder(Integer loadOrder);
 
 public void setParentHandles(JFrame parentFrame, JPanel containerPanel);
 public JFrame getParentWindowHandle();
 public JPanel getParentPanelHandle();
 /*** put all the panel initialization code here 
  called just before adding the panel to the tab container
  ***/
 public void initialize();
 /**
  * put all the code to refresh the panels here
  */
 public void refreshPanel();
 
}
