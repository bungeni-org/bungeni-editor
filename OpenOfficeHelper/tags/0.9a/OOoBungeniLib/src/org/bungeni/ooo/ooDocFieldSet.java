/*
 * ooDocFieldSet.java
 *
 * Created on December 20, 2007, 11:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo;

/**
 *
 * @author Administrator
 */
public class ooDocFieldSet {
    
    private String fieldName;
    private String fieldValue;
    private String fieldContainer;
    
    /** Creates a new instance of ooDocFieldSet */
    public ooDocFieldSet() {
    }

    public ooDocFieldSet(String name, String value, String container) {
        this.fieldContainer = container;
        this.fieldName = name;
        this.fieldValue = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldContainer() {
        return fieldContainer;
    }

    public void setFieldContainer(String fieldContainer) {
        this.fieldContainer = fieldContainer;
    }
    
}
