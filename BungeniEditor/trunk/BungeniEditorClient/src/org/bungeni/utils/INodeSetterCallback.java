package org.bungeni.utils;

/**
 *
 * @author Ashok Hariharan
 */
public interface INodeSetterCallback {
    public void setName(String name);
    public String getName();
    public void nodeSetter(BungeniBNode aNode);
}
