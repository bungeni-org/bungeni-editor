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
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.IEditorActionEvent;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Interface that must be implmeneted by all Panel classes.
 * Panel classes are the containers for action buttons.
 * @author Administrator
 * Interface to extend Panel UI
 */
public interface IFloatingPanel  {
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
  public IEditorActionEvent getEventClass(toolbarAction action);

  public void setParentWindowHandle(JFrame c);
  public JFrame getParentWindowHandle();
  public void initUI();
  public void setSectionChangeInfo(String sectionChange);
}
