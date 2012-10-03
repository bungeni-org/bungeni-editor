/*
 * CommonPropertyFunctions.java
 *
 * Created on December 6, 2007, 11:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.extutils;

import org.bungeni.editor.config.DocTypesReader;

/**
 *
 * @author Administrator
 */
public class CommonPropertyFunctions {
    
    /** Creates a new instance of CommonPropertyFunctions */
    public CommonPropertyFunctions() {
    }
    
    public static String getDocumentRootSection(){
        String documentMode = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
        String rootName = DocTypesReader.getInstance().getRootForDocType(documentMode);
        return rootName;
    }
    
   
}
