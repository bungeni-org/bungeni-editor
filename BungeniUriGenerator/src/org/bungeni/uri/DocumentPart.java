/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.uri;

/**
 *
 * @author undesa
 */
public class DocumentPart {
    public String PartName;
    public String PartDescription;
    
    public DocumentPart(){
        PartName = PartDescription = "";
    }
    public DocumentPart(String pName, String pDesc) {
        PartName = pName;
        PartDescription = pDesc;
    }
    
    @Override
    public String toString() {
        return PartDescription;
    }
}
