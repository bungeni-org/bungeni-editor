package org.bungeni.editor.panels.impl;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Interface that must be implmeneted by all Panel classes.
 * Panel classes are the containers for action buttons.
 * @author Administrator
 * Interface to extend Panel UI
 */
public interface ITabbedPanel {

    /**
     * Set the OpenOffice component handle in the Panel class
     */
    public void setOOComponentHandle(OOComponentHelper ooComponent);

    public OOComponentHelper getOOComponentHandle();

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

    /**
     * * put all the panel initialization code here
     * called just before adding the panel to the tab container
     */
    public void initialize();

    /**
     * put all the code to refresh the panels here
     */
    public void refreshPanel();

    public void cleanup();

    /**
     * Set custom objects - this allows passing custom values to tabbed panels
     * beyond the standard document and component handles
     * @param customMap
     */
    public void setCustomObjectMap(HashMap customMap);

    /**
     * Get custom objects
     * @return
     */
    public HashMap getCustomObjectMap();

}
