package org.bungeni.trackchanges.ui;

import java.util.HashMap;

/**
 * Interface class for tabs container
 * @author Ashok Hariharan
 */
public interface IChangesContainer {
    public void updatePanels(HashMap<String, Object> infomap);
    public void updateCurrentPanel(HashMap<String,Object> infomap);
    public void updatePanel(String panelName, final HashMap<String,Object> infoMap);
    public boolean startProgress();
    public boolean stopProgress();
}
