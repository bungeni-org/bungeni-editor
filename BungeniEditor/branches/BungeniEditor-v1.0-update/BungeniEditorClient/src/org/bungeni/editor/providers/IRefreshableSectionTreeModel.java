/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import org.bungeni.utils.BungeniBNode;

/**
 *
 * @author Administrator
 */
public interface IRefreshableSectionTreeModel {
    public void newRootNode();
    public void updateTreeModel (BungeniBNode refreshNode);
}
