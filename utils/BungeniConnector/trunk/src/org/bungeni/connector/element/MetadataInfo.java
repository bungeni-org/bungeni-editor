/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.element;

/**
 *
 * @author Dave
 */

public class MetadataInfo{

    private Integer id;
    private String type;
    private String name;
    private String value;
    public static final String PACKAGE_ALIAS = "metadata";
    public static final String CLASS_ALIAS = "metadatainfo";
    public MetadataInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
