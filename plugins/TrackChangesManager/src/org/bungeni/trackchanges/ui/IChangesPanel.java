package org.bungeni.trackchanges.ui;

import java.util.HashMap;

/**
 * Interface class for tabs panel
 * @author Ashok Hariharan
 */
public interface IChangesPanel {
    public void updatePanel(HashMap<String,Object> infomap);
    public String getPanelName();
    public void setPanelName(String panelName);
}
