package org.bungeni.plugins;

import java.util.HashMap;

/**
 *
 * @author Ashok Hariharan
 */
public interface IEditorPlugin {
    /**
     * Send input params to the plugin as a key-value hash-map
     * @param params
     */
    public void setParams(HashMap params);
    /**
     * A standard exec() api
     * @return
     */
    public String exec();
    /**
     * An extended api that allows passing in multiple parameters and returning an Object
     * @param params
     * @return
     */
    public Object exec2(Object[] params);
}
