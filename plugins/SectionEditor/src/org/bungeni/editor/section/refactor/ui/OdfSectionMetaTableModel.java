/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.ui;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.Node;

/**
 *
 * @author undesa
 */
public class OdfSectionMetaTableModel extends DefaultTableModel {
    private static String[] COLUMNS = {"Name", "Value"};
    private ArrayList<Node> sectionMetaList = new ArrayList<Node>(0);
  

    public OdfSectionMetaTableModel(ArrayList<Node> nodeList) {
        sectionMetaList = nodeList;
    }
    
    public void resetModel(ArrayList<Node> nodeList) {
        this.sectionMetaList = nodeList;
        fireTableDataChanged();
    }
    
    @Override
    public int getColumnCount(){
        return COLUMNS.length;
    }
    
    @Override
    public String getColumnName(int columnNo){
        return COLUMNS[columnNo];
    }
    
    @Override
    public Object getValueAt(int rowNo, int colNo){
        Node foundNode = sectionMetaList.get(rowNo);
        if (colNo == 0) {
            return foundNode.getNodeName();
        } else {
            return foundNode.getNodeValue();
        }
    }
    
    @Override
    public int getRowCount() {
        if (sectionMetaList == null) 
            return 0;
        else
            return sectionMetaList.size();
    }

    @Override
     public Class getColumnClass(int col) { 
      return String.class;
  }
}
