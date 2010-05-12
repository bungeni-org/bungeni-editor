/*
 * BungeniToolbarXMLTreeNodeProcessor.java
 *
 * Created on January 10, 2008, 6:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar;

/**
 *
 * @author Administrator
 */
public class BungeniToolbarXMLTreeNodeProcessor {
    private BungeniToolbarXMLAdapterNode adapterNode ;
    /** Creates a new instance of BungeniToolbarXMLTreeNodeProcessor */
    public BungeniToolbarXMLTreeNodeProcessor() {
    }
    
    public BungeniToolbarXMLTreeNodeProcessor(BungeniToolbarXMLAdapterNode node) {
        adapterNode = node;
    }
    
    public BungeniToolbarXMLAdapterNode getAdapterNode(){
        return adapterNode;
    }
    public String getTitle() {
       return getElementType("title");
    }

    public String getTarget(){
        return getElementType("target");
    }

    public String getVisible(){
        return getElementType("visible");
    }
    public String getMode(){
        return getElementType("mode");
    }
    
    public String getToolTip(){
        return getElementType("tooltip");
    }
    
    public String getNodeName() {
        return this.adapterNode.node.getName();
    }
    public String getElementType(String sType) {
        org.jdom.Attribute attrNode = adapterNode.node.getAttribute(sType);
        if (attrNode == null) {
            return null;
        } else {
            String retValue = (String) attrNode.getValue();
            if (retValue.equals("null"))
                return null;
            else
                return (String) attrNode.getValue();
        }
    }
    
}
