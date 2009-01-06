/*
 * DocumentMetadataTableModel2.java
 *
 * Created on February 20, 2008, 4:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.dialogs.metadatapanel;

import com.sun.star.beans.Property;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.bungeni.editor.metadata.*;
import org.bungeni.ooo.OOComponentHelper;
import com.sun.star.beans.UnknownPropertyException;

/**
 *
 * @author undesa
 */
public class DocumentMetadataTableModelLoad extends AbstractTableModel {
    private HashMap<String, DocumentMetadata> metadataMap = new HashMap<String, DocumentMetadata>();
    
//DocumentMetadataSupplierLoad metaSupplier;
    OOComponentHelper ooDocument;
    private static String[] column_names = {"Name", "Value" };
    private static org.apache.log4j.Logger log = Logger.getLogger(DocumentMetadataTableModelLoad.class.getName());
     Object[][] metadata;
    
    /** Creates a new instance of DocumentMetadataTableModel2 */
    public DocumentMetadataTableModelLoad(OOComponentHelper ooDoc) {
        
        this.ooDocument=ooDoc;
        getDocMeta();
       
        
    }
    
   public int getRowCount() {
    return metadata.length;
  }

  public int getColumnCount() {
    return column_names.length;
  }

  public String getColumnName(int column) {
    return column_names[column];
  }


  public Object getValueAt(int rowIndex, int columnIndex) {
   
    return metadata[rowIndex][columnIndex];
  }
  
 
  public Object [] searchArray(Object[] array, String val) {
	for (int j = 0;j < array.length;j++) {
		if (array[j] == val) {
			//return j;
                    
                    
		}else{
                    array[j]=null;
                }
                metadata = new Object[array.length][column_names.length];
	}
        return metadata;
	
}


  public void getDocMeta(){
     try{
         
          //get document metadata using ooDocument.getDocumentProperties()
        Property[] docProperties = ooDocument.getDocumentProperties();
        metadata = new Object[docProperties.length][column_names.length];
       
        for(int i=0; i < docProperties.length; i++) {
            
                String metaName=docProperties[i].Name;
                                       
                metadata[i][0]=metaName;
                metadata[i][1]=ooDocument.getPropertyValue(metaName);
               

            } 
     } catch(UnknownPropertyException ex){
          log.error(ex.getMessage());
      }
     
  }
}
