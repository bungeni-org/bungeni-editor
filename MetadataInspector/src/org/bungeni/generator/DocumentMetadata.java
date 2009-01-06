/*
 * DocumentMetadata.java
 *
 * Created on October 26, 2007, 1:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.generator;

/**
 * 
 * Document metadata container class
 * @author Administrator
 */
public class DocumentMetadata {

    private String Name;
 
    private String Value = "";
 
    /**
     * Creates a new instance of DocumentMetadata
     * @param name Name of metadata property
     * @param type Data type of metadata - document or section or text level
     * @param datatype data type of metadata value - string or datetime
     * @param displayname description displayed on the user interface
     */
    public DocumentMetadata(String name,String value) {
        Name = name;
        Value = value;
    }
    
    /**
     * 
     */
    public void setName(String name ) {
        Name = name;
    } 
    
    
    public String getName( ) {
         return Name;
    } 
    
    
    public void setValue (String value) {
        this.Value = value;
    }
    
    public String getValue(){
        return Value;
    }
}
