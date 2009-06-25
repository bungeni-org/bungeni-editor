/*
 * BaseClassForITabbedPanel.java
 *
 * Created on May 20, 2008, 3:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.panels.impl;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public abstract class BaseClassForITabbedPanel extends JPanel implements ITabbedPanel {
    
    /*** interface impplmenentation varibales */
    protected OOComponentHelper ooDocument;
    protected JFrame parentFrame;
    protected JPanel parentPanel;
    protected String panelTitle;
    protected Integer panelLoadOrder;
    
    /** Creates a new instance of BaseClassForITabbedPanel */
    public BaseClassForITabbedPanel() {
    }
   /**
     * Required functions for ITabbedPanel interface
     **/
    public void setOOComponentHandle(OOComponentHelper ooComponent) {
        this.ooDocument = ooComponent;
    }

    /**
     * Required for ITabbedPanel interface
     * @return - OOComponentHelper - the openoffice document handle
     */
    public OOComponentHelper getOOComponentHandle(){
        return this.ooDocument;
    }

    public Component getObjectHandle() {
        return this;
    }

    public void setParentHandles(JFrame c, JPanel containerPanel) {
        parentFrame = c;
        parentPanel = containerPanel;
    }

    public JFrame getParentWindowHandle() {
        return parentFrame;
    }

    public JPanel getParentPanelHandle() {
        return parentPanel;
    }

    public void setPanelTitle(String titleOfPanel) {
        panelTitle = titleOfPanel;
    }

    public String getPanelTitle() {
        return panelTitle;
    }

    public Integer getPanelLoadOrder() {
        return panelLoadOrder;
    }

    public void setPanelLoadOrder(Integer loadOrder) {
        panelLoadOrder = loadOrder;
    }

    public void initialize() {
      // common initialization stuff here
    }

    abstract public void refreshPanel();
    
    
    
}
