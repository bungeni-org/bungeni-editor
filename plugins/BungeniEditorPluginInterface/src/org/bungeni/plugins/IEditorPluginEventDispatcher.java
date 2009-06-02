/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.plugins;

/**
 *
 * @author Ashok Hariharan
 */
public interface IEditorPluginEventDispatcher {
    public void dispatchEvent(String fromSource, Object[] msgObjects);
}
