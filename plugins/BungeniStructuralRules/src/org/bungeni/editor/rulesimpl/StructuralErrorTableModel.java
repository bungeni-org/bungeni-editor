/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Table model for structural errors
 * @author Ashok Hariharan
 */
public class StructuralErrorTableModel extends AbstractTableModel {
    ArrayList<StructuralError> structuralErrors = new ArrayList<StructuralError>(0);
    private String[] columnNames = {"No.", "Error", "Checked"};

    public StructuralErrorTableModel(ArrayList<StructuralError> errors) {
        structuralErrors = errors;
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
