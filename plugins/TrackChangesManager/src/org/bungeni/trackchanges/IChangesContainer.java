package org.bungeni.trackchanges;

import java.util.HashMap;

/**
 * Interface class for tabs container
 * @author Ashok Hariharan
 */
public interface IChangesContainer {
    public void updatePanels(HashMap<String, Object> infomap);
    public void updateCurrentPanel(HashMap<String,Object> infomap);
    public boolean startProgress();
    public boolean stopProgress();
}
