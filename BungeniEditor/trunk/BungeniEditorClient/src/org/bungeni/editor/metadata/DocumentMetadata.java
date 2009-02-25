/*
 * DocumentMetadata.java
 *
 * Created on October 26, 2007, 1:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.util.Arrays;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * 
 * Document metadata container class
 * @author Administrator
 */
public class DocumentMetadata {

    private String Name;
    private String Type;
    private String DisplayName;
    private String DataType;
    private String Value = "";
    private boolean isVisible;
    private String TabularConfig;
    private static org.apache.log4j.Logger log = Logger.getLogger(DocumentMetadata.class.getName());
 
    /**
     * Creates a new instance of DocumentMetadata
     * @param name Name of metadata property
     * @param type Data type of metadata - document or section or text level
     * @param datatype data type of metadata value - string or datetime
     * @param displayname description displayed on the user interface
     */
    public DocumentMetadata(String name, String type, String datatype, String displayname, int visible, String tabularConfig) {
        Name = name;
        Type = type;
        DataType = datatype;
        DisplayName = displayname;
        if (visible == 1)
            isVisible = true;
        else
            isVisible = false;
        TabularConfig = tabularConfig;
    }
    
    /**
     * 
     */
    public void setName(String name ) {
        Name = name;
    } 
    
    public String getDisplayName(){
        return this.DisplayName;
    }
    
    public boolean IsVisible(){
        return isVisible;
    }
    
    public void setType(String type ) {
        Type = type;
    }
    
    public void setDisplayName(String dname) {
        this.DisplayName = dname;
    }
    
    public void setDataType (String datatype ) {
        DataType = datatype;
    }
    
    public String getName( ) {
         return Name;
    } 
    
    public String getType ( ) {
         return Type;
    }
    
    public String getDataType ( ) {
         return DataType;
    }
    
    public Vector getTabularConfig(){
        Vector vColumns = new Vector();
        try {
            if (TabularConfig.trim().length() == 0) {
                return vColumns;
            } else {
                String[] arrColumns = TabularConfig.split("~");
                vColumns = new  Vector(Arrays.asList(arrColumns));
            }
        } catch (Exception ex) {
            log.error("getTabularConfig : " + ex.getMessage());
        } finally {
            return vColumns;
        }
    }
    
    @Override
    public String toString(){
        return this.DisplayName;
    }
    
    public void setValue (String value) {
        this.Value = value;
    }
    
    public String getValue(){
        return Value;
    }
}
