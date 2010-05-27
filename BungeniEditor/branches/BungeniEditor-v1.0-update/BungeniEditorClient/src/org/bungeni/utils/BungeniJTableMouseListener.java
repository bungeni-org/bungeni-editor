/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;

/**
 * JTable double/single - click event listener helper class
 * @author Ashok Hariharan
 */
public abstract class BungeniJTableMouseListener extends MouseAdapter {
      abstract public void wasDoubleClicked(JTable tbl, int nRow, int nCol); 
      abstract public void wasSingleClicked(JTable tbl, int nRow, int nCol); 
    
 @Override
        public void mousePressed(MouseEvent e) {
            Object sourceTable = e.getSource();
          //  if (sourceTable.getClass().getName().equals(JTable.class.getName())) {
                JTable tbl = (JTable)sourceTable;
                Point p = e.getPoint();
                int nRow = tbl.rowAtPoint(p);
                int nCol = tbl.columnAtPoint(p);
                if (e.getClickCount() == 2) {
                    wasDoubleClicked(tbl, nRow, nCol);
                } 
                if (e.getClickCount() == 1) {
                    wasSingleClicked(tbl, nRow, nCol);
                }
            //}
         
            
        }
}
