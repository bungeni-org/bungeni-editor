package org.bungeni.editor.rulesimpl;

import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Table model for structural errors
 * @author Ashok Hariharan
 */
public class StructuralErrorTableModel extends AbstractTableModel {

    ArrayList<StructuralError> structuralErrors = new ArrayList<StructuralError>(0);
    private String[] columnNames = {"No.", "Error", "Checked"};
    XStream tblXmlStream = null;

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StructuralRulesParser.class.getName());


    public StructuralErrorTableModel(ArrayList<StructuralError> errors) {
        structuralErrors = errors;
        tblXmlStream = new XStream();
    }

    public ArrayList<StructuralError> getStructuralErrors(){
        return structuralErrors;
    }

    /**
     * Returns the table model as an xml stream
     * @return
     */
    public String asXmlStream(){
        String outXml = this.tblXmlStream.toXML(structuralErrors);
        return outXml;
    }

    /**
     * Loads the table model from an Xml Stream
     * @param xmlStream
     * @return
     */
    public boolean loadXmlStream(String xmlStream){
        boolean bState = false;
        try {
        ArrayList<StructuralError> newErrorList = (ArrayList<StructuralError>) this.tblXmlStream.fromXML(xmlStream);
            synchronized(this) {
                structuralErrors = newErrorList;
            }
            bState = true;
        } catch (Exception ex) {
            log.error("loadXmlStream : " + ex.getMessage());
        }
        return bState;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return structuralErrors.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
       StructuralError foundError = structuralErrors.get(row);

        switch (col) {
            case 0:
                return row + 1;
            case 1: 
                return StructuralErrorMessage.toErrorMessage(foundError);
            case 2:
                return foundError.errorChecked;
            default :
                return "column not available";
        }
    }

    @Override
    public Class getColumnClass(int c) {
        switch (c) {
            case 0: return Integer.class;
            case 1: return String.class;
            case 2: return Boolean.class;
            default: return String.class;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
       return col == 2;
    }

    @Override
    public void setValueAt(Object value, int row, int col){
        structuralErrors.get(row).errorChecked = (Boolean) value;
    }
}
