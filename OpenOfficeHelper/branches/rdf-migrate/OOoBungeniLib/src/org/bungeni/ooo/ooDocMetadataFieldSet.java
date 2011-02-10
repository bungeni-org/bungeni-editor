/*
 * ooDocMetadataFieldSet.java
 *
 * Created on December 20, 2007, 11:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo;

/**
 *
 * @author Administrator
 */
public class ooDocMetadataFieldSet {
    
    private String metadataName;
    private String metadataValue;
    
    /** Creates a new instance of ooDocMetadataFieldSet */
    public ooDocMetadataFieldSet(String name, String value) {
        setMetadataName(name);
        setMetadataValue(value);
    }

    public String getMetadataName() {
        return metadataName;
    }

    public void setMetadataName(String metadataName) {
        this.metadataName = metadataName;
    }

    public String getMetadataValue() {
        return metadataValue;
    }

    public void setMetadataValue(String metadataValue) {
        this.metadataValue = metadataValue;
    }
    
}
