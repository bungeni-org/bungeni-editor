/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.util.HashMap;
import java.util.Set;

/**
 * Defines the metadata model for a Bungeni Document.
 * @author undesa
 */
public interface IEditorDocMetaModel {
    public void setup();
    public HashMap<String,String> getModelMap();
    public void updateItem(String itemName, String itemValue);
    public String getItem(String itemName);
    public Set<String> getMetaNames();
}
