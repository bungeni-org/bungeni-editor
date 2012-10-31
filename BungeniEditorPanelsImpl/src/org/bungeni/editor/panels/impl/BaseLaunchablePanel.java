package org.bungeni.editor.panels.impl;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Ashok Hariharan
 */
public abstract class BaseLaunchablePanel extends JPanel implements ILaunchablePanel {
    protected OOComponentHelper ooDocument = null;
    protected JFrame            parentFrame;

    public void setOOComponentHandle(OOComponentHelper ooComponent) {
        this.ooDocument = ooComponent;
    }

    public abstract Component getObjectHandle();

    public abstract void initUI();

    public abstract String getPanelTitle();

    public void setParentWindowHandle(JFrame c) {
        this.parentFrame = c;
    }

    public JFrame getParentWindowHandle() {
        return this.parentFrame;
    }
}
