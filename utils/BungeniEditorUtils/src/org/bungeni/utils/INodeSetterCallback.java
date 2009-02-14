/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import org.bungeni.utils.BungeniBNode;

/**
 *
 * @author undesa
 */
public interface INodeSetterCallback {
    public void setName(String name);
    public String getName();
    public void nodeSetter(BungeniBNode aNode);
}
