/*
 * SectionMetadataLoad.java
 *
 * Created on February 21, 2008, 7:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.generator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class SectionMetadataLoad extends AbstractTableModel{
    
     OOComponentHelper ooDocument;
     String sectionName;
     private static String[] column_names = {"Section", "Value" };
     HashMap<String, String> sectionMetadataMap=new HashMap<String, String>();
      private static org.apache.log4j.Logger log = Logger.getLogger(SectionMetadataLoad.class.getName());
      Object[][] sectionMetadata;
    /** Creates a new instance of SectionMetadataLoad */
    public SectionMetadataLoad(OOComponentHelper ooDoc, String sectName) {
        this.ooDocument=ooDoc;
        this.sectionName=sectName;
        getSectionMetadata(sectName);
    }

    public int getRowCount() {
         return sectionMetadata.length;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 1)
            return true;
        else 
            return false;
    }
    public int getColumnCount() {
         return column_names.length;
    }
    
    @Override
  public String getColumnName(int column) {
    return column_names[column];
  }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return sectionMetadata[rowIndex][columnIndex];
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        sectionMetadata[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    public void getSectionMetadata(String sectionName){
       try{
           //ooDocument.getSectionMetadataAttributes returns a hashmap value
            sectionMetadataMap=ooDocument.getSectionMetadataAttributes(sectionName);
            log.debug("SectionMetadataLoad SectionName: " + sectionName);
            if(sectionMetadataMap.size()>0){
                sectionMetadata= new Object[sectionMetadataMap.size()][column_names.length];
                Iterator metaIterator = sectionMetadataMap.keySet().iterator();

                while(metaIterator.hasNext()){
                        for(int i=0; i< sectionMetadataMap.size(); i++) {
                            String metaName = (String) metaIterator.next();
                            sectionMetadata[i][0]=metaName;
                            sectionMetadata[i][1]=sectionMetadataMap.get(metaName);
                            
                        }
                    }  
            }else{
                sectionMetadata= new Object[0][column_names.length];
                log.debug("sectionMetadataMap size=0");
            }
       }catch(NoSuchElementException ex){
           log.error(ex.getMessage());
       }
       
        
        
    }
    
}
