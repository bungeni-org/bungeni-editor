/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.panels.impl;

import java.awt.Component;
import javax.swing.JFrame;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public interface ILaunchablePanel {
    /**
     * Set the OpenOffice component handle in the Panel class
     */
  public void setOOComponentHandle (OOComponentHelper ooComponent);
    /**
     * Get the current object handle as a "Component" type object
     */
  public Component getObjectHandle();
  public void initUI();
  public void setParentWindowHandle(JFrame c);
  public String getPanelTitle();
  public JFrame getParentWindowHandle();
  

}
