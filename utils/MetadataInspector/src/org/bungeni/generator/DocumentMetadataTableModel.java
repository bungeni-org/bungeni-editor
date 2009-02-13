/*
 * DocumentMetadataTableModel.java
 *
 * Created on October 27, 2007, 2:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.generator;

import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public class DocumentMetadataTableModel extends AbstractTableModel {
    DocumentMetadataSupplier metaSupplier;
    OOComponentHelper ooDocument;
    private static String[] column_names = {"Name", "Value" };
    private static org.apache.log4j.Logger log = Logger.getLogger(DocumentMetadataTableModel.class.getName());
  
    /** Creates a new instance of DocumentMetadataTableModel */
    public DocumentMetadataTableModel(OOComponentHelper ooDoc) {
        //retrieve set of metadata applicable for this document
        this.ooDocument = ooDoc;
        log.debug("in Constructor()");
        metaSupplier = new DocumentMetadataSupplier(ooDocument);
        log.debug("size of metaSupplier in Constructor()" + metaSupplier.getVisibleCount());
        //get the metadata from the document into the metadata map
        metaSupplier.loadMetadataFromDocument();
    }

    public int getRowCount() {
        return metaSupplier.getVisibleCount();
    }

    public DocumentMetadataSupplier getMetadataSupplier() {
        return this.metaSupplier;
    }
    
    public int getColumnCount() {
        return column_names.length;
    }

    @Override
    public String getColumnName(int column) {
        return column_names[column];
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0 )
            return false;
        if (column == 1 )
            return true;
        return false;
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        DocumentMetadata[] metas = this.metaSupplier.getDocumentMetadata();
        DocumentMetadata rowMeta = metas[rowIndex];
        log.debug("in getValueAt : rowMeta for row :"+ rowIndex + " , " + rowMeta.toString());
                
        if (columnIndex == 0){
            return rowMeta.getName();
        } else if (columnIndex == 1) {
            return rowMeta.getValue();
        }
        return rowMeta.getName();
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        DocumentMetadata[] metas = this.metaSupplier.getDocumentMetadata();
        DocumentMetadata rowMeta = metas[row];
        rowMeta.setValue(value.toString());
        fireTableCellUpdated(row, col);
    }
    
    @Override
    public Class getColumnClass(int col) { 
      return String.class;
  }
  
   public void setOOComponentHelper(OOComponentHelper ooDoc) {
       this.ooDocument = ooDoc;
   }
   public void refreshMetaData(){
    //John: the following are not needed
        //metaSupplier = new DocumentMetadataSupplier(ooDocument);
      // metaSupplier.setOOComponentHelper(this.ooDocument);
       log.debug("calling refreshMetadata, loadingMetadataFromDcoument");
        metaSupplier.loadMetadataFromDocument();
        fireTableDataChanged();
    }    
    
}
