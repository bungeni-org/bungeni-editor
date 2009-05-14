package org.bungeni.plugins;

import java.util.HashMap;

/**
 *
 * @author Ashok Hariharan
 */
public interface IEditorPlugin {
    public void setParams(HashMap params);
    public String exec();
}
